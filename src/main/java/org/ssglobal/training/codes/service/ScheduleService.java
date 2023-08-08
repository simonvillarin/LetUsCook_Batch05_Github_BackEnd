package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.ScheduleDTO;
import org.ssglobal.training.codes.request.SchedRequest;
import org.ssglobal.training.codes.request.ScheduleRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Program;
import org.ssglobal.training.codes.tables.pojos.Room;
import org.ssglobal.training.codes.tables.pojos.Schedule;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Student;
import org.ssglobal.training.codes.tables.pojos.StudentHistory;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ScheduleService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Schedule SCHEDULE = org.ssglobal.training.codes.tables.Schedule.SCHEDULE;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Section SECTION = org.ssglobal.training.codes.tables.Section.SECTION;
	private final org.ssglobal.training.codes.tables.Room ROOM = org.ssglobal.training.codes.tables.Room.ROOM;
	private final org.ssglobal.training.codes.tables.ProfessorLoad LOAD = org.ssglobal.training.codes.tables.ProfessorLoad.PROFESSOR_LOAD;
	private final org.ssglobal.training.codes.tables.StudentHistory HISTORY = org.ssglobal.training.codes.tables.StudentHistory.STUDENT_HISTORY;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Program PROGRAM = org.ssglobal.training.codes.tables.Program.PROGRAM;
	
	public List<ScheduleDTO> getScheduleById(Integer professorId) {
		List<Schedule> schedules = dslContext.selectFrom(SCHEDULE)
				.where(SCHEDULE.PROFESSOR_ID.eq(professorId))
				.fetchInto(Schedule.class);
		List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

		for (int i = 0; i < schedules.size(); i++) {
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(schedules.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			Professor professor = dslContext.selectFrom(PROFESSOR)
					.where(PROFESSOR.PROFESSOR_ID.eq(schedules.get(i).getProfessorId()))
					.fetchOneInto(Professor.class);
			Section section = dslContext.selectFrom(SECTION)
					.where(SECTION.SECTION_.eq(schedules.get(i).getSection()))
					.fetchOneInto(Section.class);
			Room room = dslContext.selectFrom(ROOM)
					.where(ROOM.ROOM_NUMBER.eq(schedules.get(i).getRoom()))
					.fetchOneInto(Room.class);
			List<Integer> schedIds = new ArrayList<>();
			List<String> temp = new ArrayList<>();
			List<String> days = new ArrayList<>();

			for (int j = 0; j < schedules.size(); j++) {
				if (schedules.get(i).getSubjectId() == schedules.get(j).getSubjectId() 
						&& schedules.get(i).getSection().equalsIgnoreCase(schedules.get(j).getSection())) {
					schedIds.add(schedules.get(j).getSchedId());
					temp.add(schedules.get(j).getDay());
				}
			}

			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).equalsIgnoreCase("Monday")) {
					days.add(temp.get(j));
				}
			}

			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).equalsIgnoreCase("Tuesday")) {
					days.add(temp.get(j));
				}
			}

			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).equalsIgnoreCase("Wednesday")) {
					days.add(temp.get(j));
				}
			}

			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).equalsIgnoreCase("Thursday")) {
					days.add(temp.get(j));
				}
			}

			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).equalsIgnoreCase("Friday")) {
					days.add(temp.get(j));
				}
			}

			for (int j = 0; j < temp.size(); j++) {
				if (temp.get(j).equalsIgnoreCase("Saturday")) {
					days.add(temp.get(j));
				}
			}

			if (scheduleDTOs.size() > 0) {
				boolean isExist = false;
				for (int k = 0; k < scheduleDTOs.size(); k++) {
					if (scheduleDTOs.get(k).getSubject().getSubjectId() == schedules.get(i).getSubjectId() 
							&& scheduleDTOs.get(k).getSection().getSection().equalsIgnoreCase(schedules.get(i).getSection())) {
						isExist = true;
					}
				}
				if (!isExist) {
					ScheduleDTO scheduleDTO = ScheduleDTO.builder()
							.schedId(schedIds)
							.subject(subject)
							.days(days)
							.startTime(schedules.get(i).getStartTime())
							.endTime(schedules.get(i).getEndTime().plusMinutes(1))
							.section(section)
							.room(room)
							.professor(professor)
							.activeDeactive(schedules.get(i).getActiveDeactive())
							.build();
					scheduleDTOs.add(scheduleDTO);
				}
			} else {
				ScheduleDTO scheduleDTO = ScheduleDTO.builder()
						.schedId(schedIds)
						.subject(subject)
						.days(days)
						.startTime(schedules.get(i).getStartTime())
						.endTime(schedules.get(i).getEndTime().plusMinutes(1))
						.section(section).room(room)
						.professor(professor)
						.activeDeactive(schedules.get(i).getActiveDeactive())
						.build();
				scheduleDTOs.add(scheduleDTO);
			}
		}
		return scheduleDTOs;
	}
	
	public List<ScheduleDTO> getSchedByStudentId(Integer studentId){
		List<StudentHistory> history = dslContext.selectFrom(HISTORY)
				.where(HISTORY.STUDENT_ID.eq(studentId))
				.fetchInto(StudentHistory.class);
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		Program program = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_ID.eq(student.getProgramId()))
				.fetchOneInto(Program.class);
		List<Schedule> schedules = dslContext.selectFrom(SCHEDULE)
				.where(SCHEDULE.ACTIVE_DEACTIVE.eq(true))
				.fetchInto(Schedule.class);
		if (history.size() > 0 && history.get(0).getSchedId().length > 0) {
			List<Integer> subjectTaken = new ArrayList<>();
			for (int i = 0; i < history.size(); i++) {
				if (history.get(i).getSchedId() != null) {
					for (int j = 0; j < history.get(i).getSchedId().length; j++) {
						subjectTaken.add(history.get(i).getSchedId()[j]);
					}
				}
			}
			
			HashSet<Subject> subj = new HashSet<>();
			for (int i = 0; i < subjectTaken.size(); i++) {
				Schedule schedule = dslContext.selectFrom(SCHEDULE)
						.where(SCHEDULE.SCHED_ID.eq(subjectTaken.get(i)))
						.fetchOneInto(Schedule.class);
				Subject subject = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(schedule.getSubjectId()))
						.fetchOneInto(Subject.class);
				subj.add(subject);
			}
			List<Subject> subTaken = new ArrayList<>(subj);
			
			List<Subject> subProgram = new ArrayList<>();
			if (program.getMajors() != null) {
				for(int i = 0; i < program.getMajors().length ;i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(program.getMajors()[i]))
							.fetchOneInto(Subject.class);
					subProgram.add(subject);
				}
			}
			
			if (program.getMinors() != null) {
				for(int i = 0; i < program.getMinors().length ;i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(program.getMinors()[i]))
							.fetchOneInto(Subject.class);
					subProgram.add(subject);
				}
			}
			
			if (program.getElectives() != null) {
				for(int i = 0; i < program.getElectives().length ;i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(program.getElectives()[i]))
							.fetchOneInto(Subject.class);
					subProgram.add(subject);
				}
			}
			
			List<Schedule> scheds = new ArrayList<>();
			for (int i = 0; i < schedules.size(); i++) {
				for (int j = 0; j < subProgram.size(); j++) {
					if (schedules.get(i).getSubjectId() ==  subProgram.get(j).getSubjectId()) {
						Schedule schedule = dslContext.selectFrom(SCHEDULE)
								.where(SCHEDULE.SCHED_ID.eq(schedules.get(i).getSchedId()))
								.fetchOneInto(Schedule.class);
						scheds.add(schedule);
					}
				}
			}
			
			List<Schedule> schedss = new ArrayList<>();
			if (student.getYearLevel().equals("First Year") || student.getYearLevel().equals("Second Year")) {
				for (int i = 0; i < scheds.size(); i++) {
					boolean isExists = false;
					for (int j = 0; j < subTaken.size(); j++) {
						 if (scheds.get(i).getSubjectId() == subTaken.get(j).getSubjectId() && !subTaken.get(j).getType().equals("Elective")) {
							isExists = true;
						 }
					}
					if (!isExists) {
						 Schedule schedule = dslContext.selectFrom(SCHEDULE)
									.where(SCHEDULE.SCHED_ID.eq(scheds.get(i).getSchedId()))
									.fetchOneInto(Schedule.class);
							schedss.add(schedule);
					}
				}
			} else {
				for (int i = 0; i < scheds.size(); i++) {
					boolean isExists = false;
					for (int j = 0; j < subTaken.size(); j++) {
						 if (scheds.get(i).getSubjectId() == subTaken.get(j).getSubjectId()) {
							isExists = true;
						 }
					}
					if (!isExists) {
						 Schedule schedule = dslContext.selectFrom(SCHEDULE)
									.where(SCHEDULE.SCHED_ID.eq(scheds.get(i).getSchedId()))
									.fetchOneInto(Schedule.class);
							schedss.add(schedule);
					}
				}
			}
			
			List<Schedule> schedsss = new ArrayList<>();
			for (int i = 0; i < schedss.size(); i++) {
				for (int j = 0; j < subTaken.size(); j++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(schedss.get(i).getSubjectId()))
							.fetchOneInto(Subject.class);
					int initialLength = subject.getPreRequisites().length;
					int count = 0;
					for (int k = 0; k < subject.getPreRequisites().length; k++) {
						Subject sub = dslContext.selectFrom(SUBJECT)
								.where(SUBJECT.SUBJECT_ID.eq(subject.getPreRequisites()[k]))
								.fetchOneInto(Subject.class);
						if (sub.getSubjectId() == subTaken.get(j).getSubjectId()) {
							count++;
						}
					}
					if (initialLength == count) {
						Schedule schedule = dslContext.selectFrom(SCHEDULE)
								.where(SCHEDULE.SCHED_ID.eq(schedss.get(i).getSchedId()))
								.fetchOneInto(Schedule.class);
						schedsss.add(schedule);
					}
				}
			}
			List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
			for (int i = 0; i < schedsss.size(); i++) {
				Subject subject = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(schedsss.get(i).getSubjectId()))
						.fetchOneInto(Subject.class);
				Professor professor = dslContext.selectFrom(PROFESSOR)
						.where(PROFESSOR.PROFESSOR_ID.eq(schedsss.get(i).getProfessorId()))
						.fetchOneInto(Professor.class);
				Section section = dslContext.selectFrom(SECTION)
						.where(SECTION.SECTION_.eq(schedsss.get(i).getSection()))
						.fetchOneInto(Section.class);
				Room room = dslContext.selectFrom(ROOM)
						.where(ROOM.ROOM_NUMBER.eq(schedsss.get(i).getRoom()))
						.fetchOneInto(Room.class);
				List<Integer> schedIds = new ArrayList<>();
				List<String> temp = new ArrayList<>();
				List<String> days = new ArrayList<>();

				for (int j = 0; j < schedules.size(); j++) {
					if (schedsss.get(i).getSubjectId() == schedules.get(j).getSubjectId() 
							&& schedsss.get(i).getSection().equalsIgnoreCase(schedules.get(j).getSection())) {
						schedIds.add(schedules.get(j).getSchedId());
						temp.add(schedules.get(j).getDay());
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Monday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Tuesday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Wednesday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Thursday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Friday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Saturday")) {
						days.add(temp.get(j));
					}
				}

				if (scheduleDTOs.size() > 0) {
					boolean isExist = false;
					for (int k = 0; k < scheduleDTOs.size(); k++) {
						if (scheduleDTOs.get(k).getSubject().getSubjectId() == schedsss.get(i).getSubjectId()
								&& scheduleDTOs.get(k).getSection().getSection().equalsIgnoreCase(schedsss.get(i).getSection())) {
							isExist = true;
						}
					}
					if (!isExist) {
						ScheduleDTO scheduleDTO = ScheduleDTO.builder()
								.schedId(schedIds)
								.subject(subject)
								.days(days)
								.startTime(schedsss.get(i).getStartTime())
								.endTime(schedsss.get(i).getEndTime().plusMinutes(1))
								.section(section)
								.room(room)
								.professor(professor)
								.activeDeactive(schedsss.get(i).getActiveDeactive())
								.build();
						scheduleDTOs.add(scheduleDTO);
					}
				} else {
					ScheduleDTO scheduleDTO = ScheduleDTO.builder()
							.schedId(schedIds)
							.subject(subject)
							.days(days)
							.startTime(schedsss.get(i).getStartTime())
							.endTime(schedsss.get(i).getEndTime().plusMinutes(1))
							.section(section)
							.room(room)
							.professor(professor)
							.activeDeactive(schedsss.get(i).getActiveDeactive())
							.build();
					scheduleDTOs.add(scheduleDTO);
				}
			}
			return scheduleDTOs;
		} else {
			
			
			List<Subject> subProgram = new ArrayList<>();
			if (program.getMajors() != null) {
				for(int i = 0; i < program.getMajors().length ;i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(program.getMajors()[i]))
							.fetchOneInto(Subject.class);
					subProgram.add(subject);
				}
			}
			
			if (program.getMinors() != null) {
				for(int i = 0; i < program.getMinors().length ;i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(program.getMinors()[i]))
							.fetchOneInto(Subject.class);
					subProgram.add(subject);
				}
			}
			
			if (program.getElectives() != null) {
				for(int i = 0; i < program.getElectives().length ;i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(program.getElectives()[i]))
							.fetchOneInto(Subject.class);
					subProgram.add(subject);
				}
			}
			
			List<Schedule> scheds = new ArrayList<>();
			for (int i = 0; i < schedules.size(); i++) {
				for (int j = 0; j < subProgram.size(); j++) {
					if (schedules.get(i).getSubjectId() ==  subProgram.get(j).getSubjectId()) {
						scheds.add(schedules.get(i));
					}
				}
			}
			
			List<Schedule> schedss = new ArrayList<>();
			for (int i = 0; i < scheds.size(); i++) {
				Subject subject = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(scheds.get(i).getSubjectId()))
						.fetchOneInto(Subject.class);
				if (subject.getPreRequisites().length == 0 && !subject.getType().equalsIgnoreCase("Elective")) {
					schedss.add(scheds.get(i));
				}
			}
			
			List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
			for (int i = 0; i < schedss.size(); i++) {
				Subject subject = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(schedss.get(i).getSubjectId()))
						.fetchOneInto(Subject.class);
				Professor professor = dslContext.selectFrom(PROFESSOR)
						.where(PROFESSOR.PROFESSOR_ID.eq(schedss.get(i).getProfessorId()))
						.fetchOneInto(Professor.class);
				Section section = dslContext.selectFrom(SECTION)
						.where(SECTION.SECTION_.eq(schedss.get(i).getSection()))
						.fetchOneInto(Section.class);
				Room room = dslContext.selectFrom(ROOM)
						.where(ROOM.ROOM_NUMBER.eq(schedss.get(i).getRoom()))
						.fetchOneInto(Room.class);
				List<Integer> schedIds = new ArrayList<>();
				List<String> temp = new ArrayList<>();
				List<String> days = new ArrayList<>();

				for (int j = 0; j < schedules.size(); j++) {
					if (schedss.get(i).getSubjectId() == schedules.get(j).getSubjectId() 
							&& schedss.get(i).getSection().equalsIgnoreCase(schedules.get(j).getSection())) {
						schedIds.add(schedules.get(j).getSchedId());
						temp.add(schedules.get(j).getDay());
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Monday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Tuesday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Wednesday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Thursday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Friday")) {
						days.add(temp.get(j));
					}
				}

				for (int j = 0; j < temp.size(); j++) {
					if (temp.get(j).equalsIgnoreCase("Saturday")) {
						days.add(temp.get(j));
					}
				}

				if (scheduleDTOs.size() > 0) {
					boolean isExist = false;
					for (int k = 0; k < scheduleDTOs.size(); k++) {
						if (scheduleDTOs.get(k).getSubject().getSubjectId() == subject.getSubjectId()
								&& scheduleDTOs.get(k).getSection().getSection().equalsIgnoreCase(section.getSection())) {
							isExist = true;
						}
					}
					if (!isExist) {
						ScheduleDTO scheduleDTO = ScheduleDTO.builder()
								.schedId(schedIds)
								.subject(subject)
								.days(days)
								.startTime(schedss.get(i).getStartTime())
								.endTime(schedss.get(i).getEndTime().plusMinutes(1))
								.section(section)
								.room(room)
								.professor(professor)
								.activeDeactive(schedss.get(i).getActiveDeactive())
								.build();
						scheduleDTOs.add(scheduleDTO);
					}
				} else {
					ScheduleDTO scheduleDTO = ScheduleDTO.builder()
							.schedId(schedIds)
							.subject(subject)
							.days(days)
							.startTime(schedss.get(i).getStartTime())
							.endTime(schedss.get(i).getEndTime().plusMinutes(1))
							.section(section)
							.room(room)
							.professor(professor)
							.activeDeactive(schedss.get(i).getActiveDeactive())
							.build();
					scheduleDTOs.add(scheduleDTO);
				}
			}
			return scheduleDTOs;
		}
	}
	
	

	public Response addSchedule(ScheduleRequest schedule) {
		Subject subject = dslContext.selectFrom(SUBJECT).where(SUBJECT.SUBJECT_TITLE.eq(schedule.getSubject()))
				.fetchOneInto(Subject.class);

		if (checkSchedule(schedule)) {
			return Response.builder()
					.status(409)
					.message("Schedule already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else if (checkSchedule1(schedule)) {
			return Response.builder()
					.status(409)
					.message("Schedule already exist")
					.timestamp(LocalDateTime.now())
					.build();
		}else if (checkSchedule2(schedule)) {
			return Response.builder()
					.status(409)
					.message("Schedule already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			for (int i = 0; i < schedule.getDays().size(); i++) {
				dslContext.insertInto(SCHEDULE).set(SCHEDULE.SUBJECT_ID, subject.getSubjectId())
				.set(SCHEDULE.DAY, schedule.getDays().get(i))
				.set(SCHEDULE.START_TIME, schedule.getStartTime())
				.set(SCHEDULE.END_TIME, schedule.getEndTime().minusMinutes(1))
				.set(SCHEDULE.SECTION, schedule.getSection())
				.set(SCHEDULE.ROOM, schedule.getRoom())
				.set(SCHEDULE.PROFESSOR_ID, schedule.getProfessorId())
				.set(SCHEDULE.ACTIVE_DEACTIVE, true)
				.execute();
				
				Schedule _sched = dslContext.selectFrom(SCHEDULE)
						.orderBy(SCHEDULE.SCHED_ID.desc())
						.limit(1)
						.fetchOneInto(Schedule.class);

				dslContext.insertInto(LOAD)
				.set(LOAD.PROFESSOR_ID, schedule.getProfessorId())
				.set(LOAD.SCHED_ID, _sched.getSchedId())
				.execute();
			}

			return Response.builder()
					.status(201)
					.message("Schedule sucessfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public boolean checkSchedule(ScheduleRequest sched) {
		boolean isSchedTaken = false;
		for (int i = 0; i < sched.getDays().size(); i++) {
			Schedule schedule = dslContext.selectFrom(SCHEDULE)
					.where(SCHEDULE.DAY.eq(sched.getDays().get(i))
							.and(SCHEDULE.ACTIVE_DEACTIVE.eq(true))
							.and(SCHEDULE.SECTION.eq(sched.getSection()))
							.and(SCHEDULE.START_TIME.between(sched.getStartTime(), sched.getEndTime().minusMinutes(1))
									.or(SCHEDULE.END_TIME.between(sched.getStartTime(), sched.getEndTime().minusMinutes(1)))))
					.fetchOneInto(Schedule.class);
			if (schedule != null) {
				return true;
			}
		}

		return isSchedTaken;
	}
	
	public boolean checkSchedule1(ScheduleRequest sched) {
		boolean isSchedTaken = false;
		for (int i = 0; i < sched.getDays().size(); i++) {
			Schedule schedule = dslContext.selectFrom(SCHEDULE)
					.where(SCHEDULE.DAY.eq(sched.getDays().get(i))
							.and(SCHEDULE.ACTIVE_DEACTIVE.eq(true))
							.and(SCHEDULE.PROFESSOR_ID.eq(sched.getProfessorId()))
							.and(SCHEDULE.START_TIME.between(sched.getStartTime(), sched.getEndTime().minusMinutes(1))
									.or(SCHEDULE.END_TIME.between(sched.getStartTime(), sched.getEndTime().minusMinutes(1)))))
					.fetchOneInto(Schedule.class);
			if (schedule != null) {
				return true;
			}
		}

		return isSchedTaken;
	}
	
	public boolean checkSchedule2(ScheduleRequest sched) {
		boolean isSchedTaken = false;
		for (int i = 0; i < sched.getDays().size(); i++) {
			List<Schedule> schedule = dslContext.selectFrom(SCHEDULE)
					.where(SCHEDULE.DAY.eq(sched.getDays().get(i))
							.and(SCHEDULE.ACTIVE_DEACTIVE.eq(true))
							.and(SCHEDULE.ROOM.eq(sched.getRoom()))
							.and(SCHEDULE.START_TIME.between(sched.getStartTime(), sched.getEndTime().minusMinutes(1))
									.or(SCHEDULE.END_TIME.between(sched.getStartTime(), sched.getEndTime().minusMinutes(1)))))
					.fetchInto(Schedule.class);
			if (schedule.size() > 0) {
				return true;
			}
		}

		return isSchedTaken;
	}
	
	public boolean checkSchedule3(SchedRequest sched) {
		boolean isSchedTaken = false;
        for (int i = 0; i < sched.getDays().size(); i++) {
            List<Schedule> schedule = dslContext.selectFrom(SCHEDULE)
                    .where(SCHEDULE.DAY.eq(sched.getDays().get(i))
                            .and(SCHEDULE.SCHED_ID.notIn(sched.getSchedId()))
                            .and(SCHEDULE.ACTIVE_DEACTIVE.eq(true))
                            .and(SCHEDULE.ROOM.eq(sched.getRoom()))
                            .and(SCHEDULE.START_TIME.between(sched.getStartTime(), sched.getEndTime())
                                    .or(SCHEDULE.END_TIME.between(sched.getStartTime(), sched.getEndTime()))))
                    .fetchInto(Schedule.class);
            if (schedule.size() > 0) {
                return true;
            }
        }

        return isSchedTaken;
	}
	
	public boolean checkSchedule(SchedRequest sched) {
        boolean isSchedTaken = false;
        for (int i = 0; i < sched.getDays().size(); i++) {
            Schedule schedule = dslContext.selectFrom(SCHEDULE)
                    .where(SCHEDULE.DAY.eq(sched.getDays().get(i))
                            .and(SCHEDULE.SCHED_ID.notIn(sched.getSchedId()))
                            .and(SCHEDULE.ACTIVE_DEACTIVE.eq(true))
                            .and(SCHEDULE.PROFESSOR_ID.eq(sched.getProfessorId()))
                            .and(SCHEDULE.START_TIME.between(sched.getStartTime(), sched.getEndTime())
                                    .or(SCHEDULE.END_TIME.between(sched.getStartTime(), sched.getEndTime()))))
                    .fetchOneInto(Schedule.class);
            if (schedule != null) {
                return true;
            }
        }

        return isSchedTaken;
    }
	

	public Response updateSchedule(SchedRequest schedule) {
        if (checkSchedule(schedule)) {
            return Response.builder().status(409).message("Schedule already exist").timestamp(LocalDateTime.now())
                    .build();
        } else if (checkSchedule3(schedule)) {
        	 return Response.builder().status(409).message("Schedule already exist").timestamp(LocalDateTime.now())
                     .build();
        } else {
        	Subject subject = dslContext.selectFrom(SUBJECT).where(SUBJECT.SUBJECT_TITLE.eq(schedule.getSubject()))
    				.fetchOneInto(Subject.class);
        	List<Integer> schedIds = new ArrayList<>();
        	if (schedule.getSchedId().size() > schedule.getDays().size()) {
        		 int size = schedule.getSchedId().size() - schedule.getDays().size();
        		 for (int i = size; i > 0; i--) { 
        			 Schedule sched = dslContext.selectFrom(SCHEDULE)
                             .where(SCHEDULE.SCHED_ID.eq(schedule.getSchedId().get(i)))
                             .fetchOneInto(Schedule.class);
        			 schedIds.add(sched.getSchedId());
        			 
        			 dslContext.update(SCHEDULE)
        			 .set(SCHEDULE.ACTIVE_DEACTIVE, false)
        			 .where(SCHEDULE.SCHED_ID.eq(schedule.getSchedId().get(i)))
        			 .execute();
        		 }
        		 List<Integer> temp = new ArrayList<>();
        		 for (int i = 0; i < schedule.getSchedId().size(); i++) {
        			 boolean isExists = false;
        			 for (int j = 0; j < schedIds.size(); j++) {
        				 if (schedIds.get(j).equals(schedule.getSchedId().get(i))) {
        					 isExists = true;
        				 }
        			 }
        			 if (isExists == false) {
        				 temp.add(schedule.getSchedId().get(i));
        			 }
        		 }
        		 for (int i = 0; i < temp.size(); i++) {
        			 dslContext.update(SCHEDULE)
                 	.set(SCHEDULE.SUBJECT_ID, subject.getSubjectId())
                     .set(SCHEDULE.DAY, schedule.getDays().get(i))
                     .set(SCHEDULE.START_TIME, schedule.getStartTime())
                     .set(SCHEDULE.END_TIME, schedule.getEndTime().minusMinutes(1))
                     .set(SCHEDULE.SECTION, schedule.getSection())
                     .set(SCHEDULE.ROOM, schedule.getRoom())
                     .set(SCHEDULE.ACTIVE_DEACTIVE, true)
         			 .set(SCHEDULE.PROFESSOR_ID, schedule.getProfessorId())
                     .where(SCHEDULE.SCHED_ID.eq(temp.get(i)))
                     .execute();
        		 }
        	} else if (schedule.getSchedId().size() < schedule.getDays().size()) {
            	int updatedScheds = 0;
                for (int i = 0; i < schedule.getSchedId().size(); i++) {
                    dslContext.update(SCHEDULE)
                	.set(SCHEDULE.SUBJECT_ID, subject.getSubjectId())
                    .set(SCHEDULE.DAY, schedule.getDays().get(i))
                    .set(SCHEDULE.START_TIME, schedule.getStartTime())
                    .set(SCHEDULE.END_TIME, schedule.getEndTime().minusMinutes(1))
                    .set(SCHEDULE.SECTION, schedule.getSection())
                    .set(SCHEDULE.ROOM, schedule.getRoom())
                    .set(SCHEDULE.ACTIVE_DEACTIVE, true)
        			.set(SCHEDULE.PROFESSOR_ID, schedule.getProfessorId())
                    .where(SCHEDULE.SCHED_ID.eq(schedule.getSchedId().get(i)))
                    .execute();
                    
                    updatedScheds += 1;        
                }
                
                for (int i = updatedScheds; i < schedule.getDays().size(); i++) {
                	 dslContext.insertInto(SCHEDULE)
                	 .set(SCHEDULE.SUBJECT_ID, subject.getSubjectId())
                     .set(SCHEDULE.DAY, schedule.getDays().get(i))
                     .set(SCHEDULE.START_TIME, schedule.getStartTime())
                     .set(SCHEDULE.END_TIME, schedule.getEndTime().minusMinutes(1))
                     .set(SCHEDULE.SECTION, schedule.getSection())
                     .set(SCHEDULE.ROOM, schedule.getRoom())
                     .set(SCHEDULE.PROFESSOR_ID, schedule.getProfessorId())
                     .set(SCHEDULE.ACTIVE_DEACTIVE, true)
                     .execute();
                	 
                	 Schedule _sched = dslContext.selectFrom(SCHEDULE)
                             .orderBy(SCHEDULE.SCHED_ID.desc())
                             .limit(1)
                             .fetchOneInto(Schedule.class);
                		
                     dslContext.insertInto(LOAD)
                     .set(LOAD.PROFESSOR_ID, schedule.getProfessorId())
                     .set(LOAD.SCHED_ID, _sched.getSchedId())
                     .execute();
                }
        	} else {
        		 for (int i = 0; i < schedule.getSchedId().size(); i++) {
                     dslContext.update(SCHEDULE)
                 	.set(SCHEDULE.SUBJECT_ID, subject.getSubjectId())
                     .set(SCHEDULE.DAY, schedule.getDays().get(i))
                     .set(SCHEDULE.START_TIME, schedule.getStartTime())
                     .set(SCHEDULE.END_TIME, schedule.getEndTime().minusMinutes(1))
                     .set(SCHEDULE.SECTION, schedule.getSection())
                     .set(SCHEDULE.ROOM, schedule.getRoom())
                     .set(SCHEDULE.ACTIVE_DEACTIVE, true)
         			 .set(SCHEDULE.PROFESSOR_ID, schedule.getProfessorId())
                     .where(SCHEDULE.SCHED_ID.eq(schedule.getSchedId().get(i)))
                     .execute();
                 }
        	}
            
            return Response.builder()
                    .status(200)
                    .message("Schedule sucessfully updated")
                    .timestamp(LocalDateTime.now())
                    .build();
        }
	}
	
	public Response deleteSchedule(Integer[] schedId, SchedRequest schedule) {
		for (int i = 0; i < schedId.length; i++) {
			Schedule sched = dslContext.selectFrom(SCHEDULE).where(SCHEDULE.SCHED_ID.eq(schedId[i]))
					.fetchOneInto(Schedule.class);
			if (sched != null) {
				dslContext.update(SCHEDULE)
				.set(SCHEDULE.ACTIVE_DEACTIVE, schedule.getActiveDeactive())
				.where(SCHEDULE.SCHED_ID.eq(schedId[i]))
				.execute();
			} else {
				return Response.builder().status(404).message("Schedule not found").timestamp(LocalDateTime.now()).build();
			}
		}
		return Response.builder().status(200).message("Schedule sucessfully deleted").timestamp(LocalDateTime.now())
				.build();

		
	}
}