package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Room;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Room ROOM = org.ssglobal.training.codes.tables.Room.ROOM;
	
	public List<Room> getAllRoom() {
		return dslContext.selectFrom(ROOM).fetchInto(Room.class);
	}
	
	public Room getRoomById(Integer roomId) {
		Room room = dslContext.selectFrom(ROOM)
				.where(ROOM.ROOM_ID.eq(roomId))
				.fetchOneInto(Room.class);
		if (room != null) {
			return room;
		} else {
			throw new RuntimeException("Room not found");
		}
	}
	
	public Response addRoom(Room room) {
		Room roomNumber = dslContext.selectFrom(ROOM)
				.where(ROOM.ROOM_NUMBER.eq(room.getRoomNumber()))
				.fetchOneInto(Room.class);
		
		if (roomNumber != null) {
			return Response.builder()
					.status(409)
					.message("Room number already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			dslContext.insertInto(ROOM)
			.set(ROOM.ROOM_NUMBER, room.getRoomNumber())
			.set(ROOM.ROOM_CAPACITY, room.getRoomCapacity())
			.set(ROOM.NUM_OF_STUDENTS,  0)
			.set(ROOM.ACTIVE_DEACTIVE, true)
			.execute();
			
			return Response.builder()
					.status(201)
					.message("Room successfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateRoom(Integer roomId, Room room) {
		Room _room = dslContext.selectFrom(ROOM)
				.where(ROOM.ROOM_ID.eq(roomId)).fetchOneInto(Room.class);
				
		if (_room != null) {
			if (room.getRoomNumber() != null) {
				Room roomNumber = dslContext.selectFrom(ROOM)
						.where(ROOM.ROOM_NUMBER.eq(room.getRoomNumber()))
						.fetchOneInto(Room.class);
				
				if (roomNumber != null) {
					return Response.builder()
							.status(409)
							.message("Room number already exist")
							.timestamp(LocalDateTime.now())
							.build();
				}		
				dslContext.update(ROOM)
				.set(ROOM.ROOM_NUMBER, room.getRoomNumber())
				.where(ROOM.ROOM_ID.eq(roomId))
				.execute();
			}
			if (room.getRoomCapacity() != null) {
				dslContext.update(ROOM)
				.set(ROOM.ROOM_CAPACITY, room.getRoomCapacity())
				.where(ROOM.ROOM_ID.eq(roomId))
				.execute();
			}
			if (room.getNumOfStudents() != null) {
				dslContext.update(ROOM)
				.set(ROOM.NUM_OF_STUDENTS, room.getNumOfStudents())
				.where(ROOM.ROOM_ID.eq(roomId))
				.execute();
			}
			if (room.getActiveDeactive() != null) {
				dslContext.update(ROOM)
				.set(ROOM.ACTIVE_DEACTIVE, room.getActiveDeactive())
				.where(ROOM.ROOM_ID.eq(roomId))
				.execute();
			}
			return Response.builder()
					.status(201)
					.message("Room sucessfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Room not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response deleteRoom(Integer roomId) {
        Room room = dslContext.selectFrom(ROOM)
				.where(ROOM.ROOM_ID.eq(roomId)).fetchOneInto(Room.class);

        if (room != null) {
        	dslContext.deleteFrom(ROOM)
            .where(ROOM.ROOM_ID.eq(roomId))
            .execute();
            return Response.builder()
                    .status(200)
                    .message("Room successfully deleted")
                    .timestamp(LocalDateTime.now())
                    .build();
        } else {
            return Response.builder()
                    .status(404)
                    .message("Room not found")
                    .timestamp(LocalDateTime.now())
                    .build();
        }
    }
}
