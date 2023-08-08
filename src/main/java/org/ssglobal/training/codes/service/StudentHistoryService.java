package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.SchedDTO;
import org.ssglobal.training.codes.dto.StudentHistoryDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.AttendanceStudent;
import org.ssglobal.training.codes.tables.pojos.Eval;
import org.ssglobal.training.codes.tables.pojos.Evaluation;
import org.ssglobal.training.codes.tables.pojos.Grades;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Room;
import org.ssglobal.training.codes.tables.pojos.Schedule;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Student;
import org.ssglobal.training.codes.tables.pojos.StudentHistory;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentHistoryService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.StudentHistory SH = org.ssglobal.training.codes.tables.StudentHistory.STUDENT_HISTORY;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Schedule SCHEDULE = org.ssglobal.training.codes.tables.Schedule.SCHEDULE;
	private final org.ssglobal.training.codes.tables.Section SECTION = org.ssglobal.training.codes.tables.Section.SECTION;
	private final org.ssglobal.training.codes.tables.Room ROOM = org.ssglobal.training.codes.tables.Room.ROOM;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	private final org.ssglobal.training.codes.tables.Grades GRADE = org.ssglobal.training.codes.tables.Grades.GRADES;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.AttendanceStudent ATTENDANCE = org.ssglobal.training.codes.tables.AttendanceStudent.ATTENDANCE_STUDENT;
	private final org.ssglobal.training.codes.tables.Evaluation EVALUATION = org.ssglobal.training.codes.tables.Evaluation.EVALUATION;
	private final org.ssglobal.training.codes.tables.Eval EVAL = org.ssglobal.training.codes.tables.Eval.EVAL;
	
	public List<StudentHistoryDTO> getSHById(Integer studentId) {
		List<StudentHistory> history = dslContext.selectFrom(SH)
				.where(SH.STUDENT_ID.eq(studentId))
				.fetchInto(StudentHistory.class);
		List<StudentHistoryDTO> studentHistoryDTOs = new ArrayList<>();
		if(history.size() > 0) {
			for (int i = 0; i < history.size(); i++) {
				Student student = dslContext.selectFrom(STUDENT)
						.where(STUDENT.STUDENT_ID.eq(studentId))
						.fetchOneInto(Student.class);
				List<SchedDTO> scheduleDTOs = new ArrayList<>();
				if (history.get(i).getSchedId() != null) {
					for (int j = 0; j < history.get(i).getSchedId().length; j++) {
						Schedule schedule = dslContext.selectFrom(SCHEDULE)
								.where(SCHEDULE.SCHED_ID.eq(history.get(i).getSchedId()[j]))
								.fetchOneInto(Schedule.class);
						Subject subject = dslContext.selectFrom(SUBJECT)
								.where(SUBJECT.SUBJECT_ID.eq(schedule.getSubjectId()))
								.fetchOneInto(Subject.class);
						Professor professor = dslContext.selectFrom(PROFESSOR)
								.where(PROFESSOR.PROFESSOR_ID.eq(schedule.getProfessorId()))
								.fetchOneInto(Professor.class);
						Section section = dslContext.selectFrom(SECTION)
								.where(SECTION.SECTION_.eq(schedule.getSection()))
								.fetchOneInto(Section.class);
						Room room = dslContext.selectFrom(ROOM)
								.where(ROOM.ROOM_NUMBER.eq(schedule.getRoom()))
								.fetchOneInto(Room.class);
						SchedDTO scheduleDTO = SchedDTO.builder()
								.schedId(schedule.getSchedId())
								.subject(subject)
								.day(schedule.getDay())
								.startTime(schedule.getStartTime())
								.endTime(schedule.getEndTime())
								.section(section)
								.room(room)
								.professor(professor)
								.activeDeactive(true).build();
						scheduleDTOs.add(scheduleDTO);
					}
					
				}
				
				StudentHistoryDTO studentDTO = StudentHistoryDTO
						.builder()
						.historyId(history.get(i).getHistoryId())
						.student(student)
						.schedules(scheduleDTOs)
						.yearLevel(history.get(i).getYearLevel())
						.sem(history.get(i).getSem())
						.academicYear(history.get(i).getAcademicYear())
						.activeDeactive(history.get(i).getActiveDeactive())
						.build();
				studentHistoryDTOs.add(studentDTO);
			}
			return studentHistoryDTOs;
		}else {
			return studentHistoryDTOs;
		}
	}
	
	public List<StudentHistoryDTO> getStudentByStudentNo(String studentNo) {
		List<StudentHistory> history = dslContext.selectFrom(SH)
				.fetchInto(StudentHistory.class);
		List<StudentHistoryDTO> studentHistoryDTOs = new ArrayList<>();
		for (int i = 0; i < history.size(); i++) {
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(history.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			if (student.getStudentNo().equalsIgnoreCase(studentNo)) {
				List<SchedDTO> scheduleDTOs = new ArrayList<>();
				for (int j = 0; j < history.get(i).getSchedId().length; j++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(history.get(i).getSchedId()[j]))
							.fetchOneInto(Schedule.class);
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(schedule.getSubjectId()))
							.fetchOneInto(Subject.class);
					Professor professor = dslContext.selectFrom(PROFESSOR)
							.where(PROFESSOR.PROFESSOR_ID.eq(schedule.getProfessorId()))
							.fetchOneInto(Professor.class);
					Section section = dslContext.selectFrom(SECTION)
							.where(SECTION.SECTION_.eq(schedule.getSection()))
							.fetchOneInto(Section.class);
					Room room = dslContext.selectFrom(ROOM)
							.where(ROOM.ROOM_NUMBER.eq(schedule.getRoom()))
							.fetchOneInto(Room.class);
					SchedDTO scheduleDTO = SchedDTO.builder()
							.schedId(schedule.getSchedId())
							.subject(subject)
							.day(schedule.getDay())
							.startTime(schedule.getStartTime())
							.endTime(schedule.getEndTime())
							.section(section)
							.room(room)
							.professor(professor)
							.activeDeactive(true).build();
					scheduleDTOs.add(scheduleDTO);
					
				}
				
				StudentHistoryDTO studentDTO = StudentHistoryDTO
						.builder()
						.historyId(history.get(i).getHistoryId())
						.student(student)
						.schedules(scheduleDTOs)
						.yearLevel(history.get(i).getYearLevel())
						.sem(history.get(i).getSem())
						.academicYear(history.get(i).getAcademicYear())
						.activeDeactive(history.get(i).getActiveDeactive())
						.build();
				studentHistoryDTOs.add(studentDTO);
			}
		}
		return studentHistoryDTOs;
	}
	
	public Response addSh(StudentHistory sh) {
		StudentHistory shh = dslContext.selectFrom(SH)
				.where(SH.STUDENT_ID.eq(sh.getStudentId()))
				.fetchOneInto(StudentHistory.class);
		if (shh != null) {
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < shh.getSchedId().length; i++) {
				list.add(shh.getSchedId()[i]);
			}
			
			for (int i = 0; i < sh.getSchedId().length; i++) {
				Schedule schedule = dslContext.selectFrom(SCHEDULE)
						.where(SCHEDULE.SCHED_ID.eq(sh.getSchedId()[i]))
						.fetchOneInto(Schedule.class);
				Grades grade = dslContext.selectFrom(GRADE)
						.where(GRADE.STUDENT_ID.eq(sh.getStudentId()))
						.and(GRADE.SUBJECT_ID.eq(schedule.getSubjectId()))
						.fetchOneInto(Grades.class);
				List<AttendanceStudent> attendance = dslContext.selectFrom(ATTENDANCE)
						.where(ATTENDANCE.STUDENT_ID.eq(sh.getStudentId()))
						.and(ATTENDANCE.SUBJECT_ID.eq(schedule.getSubjectId()))
						.fetchInto(AttendanceStudent.class);
				Evaluation evaluation = dslContext.selectFrom(EVALUATION)
						.where(EVALUATION.STUDENT_ID.eq(sh.getStudentId()))
						.and(EVALUATION.SUBJECT_ID.eq(schedule.getSubjectId()))
						.fetchOneInto(Evaluation.class);
				List<Eval> eval = dslContext.selectFrom(EVAL)
						.where(EVAL.SUBJECT_ID.eq(schedule.getSubjectId()))
						.fetchInto(Eval.class);
				if (grade != null) {
					if (grade.getRemarks().equals("Passed")) {
						list.add(schedule.getSchedId());
					} else {
						dslContext.deleteFrom(GRADE)
						.where(GRADE.GRADE_ID.eq(grade.getGradeId()))
						.execute();
						
						for (int j = 0; j < attendance.size(); j++) {
							dslContext.deleteFrom(ATTENDANCE)
							.where(ATTENDANCE.ATTENDANCE_ID.eq(attendance.get(j).getAttendanceId()))
							.execute();
						}
						
						if (evaluation != null) {
							dslContext.deleteFrom(EVALUATION)
							.where(EVALUATION.EVAL_ID.eq(evaluation.getEvalId()))
							.execute();
						}
						
						for (int j = 0; j < eval.size(); j++) {
							dslContext.deleteFrom(EVAL)
							.where(EVAL.EVAL_ID.eq(eval.get(j).getEvalId()))
							.execute();
						}
					}
				}
			}
			
			Integer[] schedIds = new Integer[list.size()];
			for (int i = 0; i < schedIds.length; i++) {
				schedIds[i] = list.get(i);
			}
			
			dslContext.update(SH)
			.set(SH.SCHED_ID, schedIds)
			.set(SH.YEAR_LEVEL, sh.getYearLevel())
			.set(SH.SEM, sh.getSem())
			.set(SH.ACADEMIC_YEAR, sh.getAcademicYear())
			.where(SH.HISTORY_ID.eq(shh.getHistoryId()))
			.execute();
		} else {
			List<Integer> list = new ArrayList<>();
			for (int i = 0; i < sh.getSchedId().length; i++) {
				Schedule schedule = dslContext.selectFrom(SCHEDULE)
						.where(SCHEDULE.SCHED_ID.eq(sh.getSchedId()[i]))
						.fetchOneInto(Schedule.class);
				Grades grade = dslContext.selectFrom(GRADE)
						.where(GRADE.STUDENT_ID.eq(sh.getStudentId()))
						.and(GRADE.SUBJECT_ID.eq(schedule.getSubjectId()))
						.fetchOneInto(Grades.class);
				if (grade != null) {
					if (grade.getRemarks().equals("Passed")) {
						list.add(schedule.getSchedId());
					} else {
						dslContext.delete(GRADE)
						.where(GRADE.GRADE_ID.eq(grade.getGradeId()))
						.execute();
					}
				}
			}
			
			Integer[] schedIds = new Integer[list.size()];
			for (int i = 0; i < schedIds.length; i++) {
				schedIds[i] = list.get(i);
			}
			
			dslContext.insertInto(SH)
			.set(SH.STUDENT_ID, sh.getStudentId())
			.set(SH.SCHED_ID, schedIds)
			.set(SH.YEAR_LEVEL, sh.getYearLevel())
			.set(SH.SEM, sh.getSem())
			.set(SH.ACADEMIC_YEAR, sh.getAcademicYear())
			.set(SH.ACTIVE_DEACTIVE, true)
			.execute();
		}
		
		return Response.builder()
				.status(201)
				.message("Student history successfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateSh(Integer studentId, StudentHistoryDTO sh) {
		List<StudentHistory> studentHistory = dslContext.selectFrom(SH)
				.where(SH.STUDENT_ID.eq(studentId))
				.fetchInto(StudentHistory.class);
		if (studentHistory.size() > 0) {
			for (int i = 0; i < studentHistory.size(); i++) {
				if (sh.getActiveDeactive() != null) {
					dslContext.update(SH)
					.set(SH.ACTIVE_DEACTIVE, sh.getActiveDeactive())
					.where(SH.HISTORY_ID.eq(studentHistory.get(i).getHistoryId()))
					.execute();
				}
			}
			return Response.builder()
					.status(200)
					.message("Student history successfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		}else {
			return Response.builder()
					.status(404)
					.message("Student history not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}
