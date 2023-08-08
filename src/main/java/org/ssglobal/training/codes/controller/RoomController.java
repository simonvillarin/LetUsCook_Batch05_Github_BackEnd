package org.ssglobal.training.codes.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.service.RoomService;
import org.ssglobal.training.codes.tables.pojos.Room;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lms")
public class RoomController {
	private final RoomService roomService;
	
	@GetMapping("/rooms")
	public ResponseEntity<List<Room>> getAllRooms() {
		return ResponseEntity.ok(roomService.getAllRoom());
	}
	
	@GetMapping("/room/{id}")
	public ResponseEntity<Room> getRoomById(@PathVariable("id") Integer roomId) {
		return ResponseEntity.ok(roomService.getRoomById(roomId));
	}
	
	@PostMapping("/room")
	public ResponseEntity<Response> addRoom(@RequestBody Room room) {
		return ResponseEntity.status(HttpStatus.CREATED).body(roomService.addRoom(room));
	}
	
	@PutMapping("/room/{id}")
	public ResponseEntity<Response> updateRoom(@PathVariable("id") Integer roomId, @RequestBody Room room) {
		return ResponseEntity.ok(roomService.updateRoom(roomId, room));
	}
	
	@DeleteMapping("/room/{id}")
	public ResponseEntity<Response> deleteRoom(@PathVariable("id")Integer roomId) {
		return ResponseEntity.ok(roomService.deleteRoom(roomId));
	}
}
