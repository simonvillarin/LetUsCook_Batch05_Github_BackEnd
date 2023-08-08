package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.LoadDTO;
import org.ssglobal.training.codes.dto.ScheduleDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.ProfessorLoad;
import org.ssglobal.training.codes.tables.pojos.Room;
import org.ssglobal.training.codes.tables.pojos.Schedule;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessorLoadService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.ProfessorLoad LOAD = org.ssglobal.training.codes.tables.ProfessorLoad.PROFESSOR_LOAD;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	private final org.ssglobal.training.codes.tables.Schedule SCHEDULE = org.ssglobal.training.codes.tables.Schedule.SCHEDULE;
	private final org.ssglobal.training.codes.tables.Section SECTION = org.ssglobal.training.codes.tables.Section.SECTION;
	private final org.ssglobal.training.codes.tables.Room ROOM = org.ssglobal.training.codes.tables.Room.ROOM;
	
	public LoadDTO getAllProfessorLoadsByProfessorId(Integer professorId) {
		List<ProfessorLoad> professorLoads = dslContext.selectFrom(LOAD)
				.where(LOAD.PROFESSOR_ID.eq(professorId))
				.fetchInto(ProfessorLoad.class);
		List<Integer> loadIds = new ArrayList<>();
		List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
		List<Schedule> schedules = new ArrayList<>();

		for (int j = 0; j < professorLoads.size(); j++) {
			Schedule schedule = dslContext.selectFrom(SCHEDULE)
					.where(SCHEDULE.SCHED_ID.eq(professorLoads.get(j).getSchedId()))
					.fetchOneInto(Schedule.class);
			if (schedule.getActiveDeactive() == true) {
				schedules.add(schedule);
			}
			loadIds.add(professorLoads.get(j).getLoadId());
		}
		
		for (int j = 0; j < schedules.size(); j++) {
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(schedules.get(j).getSubjectId()))
					.fetchOneInto(Subject.class);
			Professor professor = dslContext.selectFrom(PROFESSOR)
					.where(PROFESSOR.PROFESSOR_ID.eq(schedules.get(j).getProfessorId()))
					.fetchOneInto(Professor.class);
			Section section = dslContext.selectFrom(SECTION)
					.where(SECTION.SECTION_.eq(schedules.get(j).getSection()))
					.fetchOneInto(Section.class);
			Room room = dslContext.selectFrom(ROOM)
					.where(ROOM.ROOM_NUMBER.eq(schedules.get(j).getRoom()))
					.fetchOneInto(Room.class);
			
			List<Integer> schedIds = new ArrayList<>();
			List<String> temp = new ArrayList<>();
			List<String> days = new ArrayList<>();

			for (int k = 0; k < schedules.size(); k++) {
				if (schedules.get(j).getSubjectId() == schedules.get(k).getSubjectId() 
						&& schedules.get(j).getSection().equalsIgnoreCase(schedules.get(k).getSection())) {
					schedIds.add(schedules.get(k).getSchedId());
					temp.add(schedules.get(k).getDay());
				}
			}

			for (int k = 0; k < temp.size(); k++) {
				if (temp.get(k).equalsIgnoreCase("Monday")) {
					days.add(temp.get(k));
				}
			}

			for (int k = 0; k < temp.size(); k++) {
				if (temp.get(k).equalsIgnoreCase("Tuesday")) {
					days.add(temp.get(k));
				}
			}

			for (int k = 0; k < temp.size(); k++) {
				if (temp.get(k).equalsIgnoreCase("Wednesday")) {
					days.add(temp.get(k));
				}
			}

			for (int k = 0; k < temp.size(); k++) {
				if (temp.get(k).equalsIgnoreCase("Thursday")) {
					days.add(temp.get(k));
				}
			}

			for (int k = 0; k < temp.size(); k++) {
				if (temp.get(k).equalsIgnoreCase("Friday")) {
					days.add(temp.get(k));
				}
			}

			for (int k = 0; k < temp.size(); k++) {
				if (temp.get(k).equalsIgnoreCase("Saturday")) {
					days.add(temp.get(k));
				}
			}

			if (scheduleDTOs.size() > 0) {
				boolean isExist = false;
				for (int k = 0; k < scheduleDTOs.size(); k++) {
					if (scheduleDTOs.get(k).getSubject().getSubjectId() == schedules.get(j).getSubjectId() 
							&& scheduleDTOs.get(k).getSection().getSection().equalsIgnoreCase(schedules.get(j).getSection())) {
						isExist = true;
					}
				}
				if (!isExist) {
					ScheduleDTO scheduleDTO = ScheduleDTO.builder()
							.schedId(schedIds)
							.subject(subject)
							.days(days)
							.startTime(schedules.get(j).getStartTime())
							.endTime(schedules.get(j).getEndTime().plusMinutes(1))
							.section(section)
							.room(room)
							.professor(professor)
							.activeDeactive(schedules.get(j).getActiveDeactive())
							.build();
					scheduleDTOs.add(scheduleDTO);
				}
			} else {
				ScheduleDTO scheduleDTO = ScheduleDTO.builder()
						.schedId(schedIds)
						.subject(subject)
						.days(days)
						.startTime(schedules.get(j).getStartTime())
						.endTime(schedules.get(j).getEndTime().plusMinutes(1))
						.section(section).room(room)
						.professor(professor)
						.activeDeactive(schedules.get(j).getActiveDeactive())
						.build();
				scheduleDTOs.add(scheduleDTO);
			}
		}
		
		return LoadDTO.builder()
				.loadId(loadIds)
				.schedules(scheduleDTOs)
				.build();
	}
	
	public Response addProfessorLoad(ProfessorLoad professorLoad) {
		dslContext.insertInto(LOAD)
		.set(LOAD.PROFESSOR_ID, professorLoad.getProfessorId())
		.set(LOAD.SCHED_ID, professorLoad.getSchedId())
		.execute();
		
		return Response.builder()
				.status(201)
				.message("Professor load successfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateProfessorLoad(Integer loadId, ProfessorLoad professorLoad) {
		ProfessorLoad _professorLoad = dslContext.selectFrom(LOAD)
				.where(LOAD.LOAD_ID.eq(loadId))
				.fetchOneInto(ProfessorLoad.class);
		if (_professorLoad != null) {	
			if (professorLoad.getSchedId() != null) {
				dslContext.update(LOAD)
				.set(LOAD.SCHED_ID, professorLoad.getSchedId())
				.where(LOAD.LOAD_ID.eq(loadId))
				.execute();
			}
			return Response.builder()
					.status(200)
					.message("Professor load successfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Professor load not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response deleteProfessorLoad(Integer loadId) {
		ProfessorLoad professorLoad = dslContext.selectFrom(LOAD)
				.where(LOAD.LOAD_ID.eq(loadId))
				.fetchOneInto(ProfessorLoad.class);
		if (professorLoad != null) {
			dslContext.deleteFrom(LOAD)
			.where(LOAD.LOAD_ID.eq(loadId))
			.execute();
			
			return Response.builder()
					.status(200)
					.message("Professor load successfully deleted")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Professor load not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}

