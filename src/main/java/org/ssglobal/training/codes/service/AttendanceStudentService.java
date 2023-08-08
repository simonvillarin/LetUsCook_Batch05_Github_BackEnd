package org.ssglobal.training.codes.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.AttendanceStudentDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.AttendanceStudent;
import org.ssglobal.training.codes.tables.pojos.Schedule;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Student;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttendanceStudentService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.AttendanceStudent ATTENDANCE =  org.ssglobal.training.codes.tables.AttendanceStudent.ATTENDANCE_STUDENT;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	private final org.ssglobal.training.codes.tables.Schedule SCHEDULE = org.ssglobal.training.codes.tables.Schedule.SCHEDULE;
	private final org.ssglobal.training.codes.tables.Section SECTION = org.ssglobal.training.codes.tables.Section.SECTION;

	public List<AttendanceStudentDTO> getAllAttendance(){
		List<AttendanceStudent> attendance = dslContext.selectFrom(ATTENDANCE)
				.fetchInto(AttendanceStudent.class);
		List<AttendanceStudentDTO> attendanceDTOs = new ArrayList<>();
		
		for(int i = 0; i < attendance.size(); i++){
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(attendance.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(attendance.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			
			AttendanceStudentDTO attendanceDTO = AttendanceStudentDTO.builder()
					.attendanceId(attendance.get(i).getAttendanceId())
					.student(student)
					.subject(subject)
					.sem(attendance.get(i).getSem())
					.yearLevel(attendance.get(i).getYearLevel())
					.academicYear(attendance.get(i).getAcademicYear())
					.date(attendance.get(i).getDate())
					.time(attendance.get(i).getTime())
					.status(attendance.get(i).getStatus())
					.activeDeactive(attendance.get(i).getActiveDeactive())
					.build();
			attendanceDTOs.add(attendanceDTO);
			
		}
		return attendanceDTOs;
	}
	
	public List<AttendanceStudentDTO> getAttendanceByStudentId(Integer studentId){
		List<AttendanceStudent> attendance = dslContext.selectFrom(ATTENDANCE)
				.where(ATTENDANCE.STUDENT_ID.eq(studentId))
				.fetchInto(AttendanceStudent.class);
		List<AttendanceStudentDTO> attendanceDTOs = new ArrayList<>();
		
		for(int i = 0; i < attendance.size(); i++){
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(attendance.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(attendance.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			if (attendance.get(i).getYearLevel().equals(student.getYearLevel()) && attendance.get(i).getSem().equals(student.getSem())) {
				AttendanceStudentDTO attendanceDTO = AttendanceStudentDTO.builder()
						.attendanceId(attendance.get(i).getAttendanceId())
						.student(student)
						.subject(subject)
						.sem(attendance.get(i).getSem())
						.yearLevel(attendance.get(i).getYearLevel())
						.academicYear(attendance.get(i).getAcademicYear())
						.date(attendance.get(i).getDate())
						.time(attendance.get(i).getTime())
						.status(attendance.get(i).getStatus())
						.activeDeactive(attendance.get(i).getActiveDeactive())
						.build();
				attendanceDTOs.add(attendanceDTO);
			}
		}
		return attendanceDTOs;
	}
	
	public List<AttendanceStudentDTO> getAttendanceById(Integer studentId, Integer subjectId) {
		List<AttendanceStudent> attendance = dslContext.selectFrom(ATTENDANCE)
				.where(ATTENDANCE.STUDENT_ID.eq(studentId)).and(ATTENDANCE.SUBJECT_ID.eq(subjectId))
				.fetchInto(AttendanceStudent.class);
		List<AttendanceStudentDTO> attendanceDTOs = new ArrayList<>();
		
		for(int i = 0; i < attendance.size(); i++){
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(attendance.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(attendance.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			if (attendance.get(i).getStudentId().equals(studentId) && attendance.get(i).getYearLevel().equals(student.getYearLevel()) && attendance.get(i).getSem().equals(student.getSem())) {
				AttendanceStudentDTO attendanceDTO = AttendanceStudentDTO.builder()
						.attendanceId(attendance.get(i).getAttendanceId())
						.student(student)
						.subject(subject)
						.sem(attendance.get(i).getSem())
						.yearLevel(attendance.get(i).getYearLevel())
						.academicYear(attendance.get(i).getAcademicYear())
						.date(attendance.get(i).getDate())
						.time(attendance.get(i).getTime())
						.status(attendance.get(i).getStatus())
						.activeDeactive(attendance.get(i).getActiveDeactive())
						.build();
				attendanceDTOs.add(attendanceDTO);
			}
		}
		System.out.println(attendanceDTOs.toString());
		return attendanceDTOs;
	}
	
	public HashSet<AttendanceStudentDTO> getAttendanceBySection(Integer sectionId, Integer subjectId) {
		List<AttendanceStudent> attendance = dslContext.selectFrom(ATTENDANCE)
				.fetchInto(AttendanceStudent.class);
		Section section = dslContext.selectFrom(SECTION)
				.where(SECTION.SECTION_ID.eq(sectionId))
				.fetchOneInto(Section.class);
		Subject sub = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_ID.eq(subjectId))
				.fetchOneInto(Subject.class);
		List<AttendanceStudentDTO> attDTOs = new ArrayList<>();
		HashSet<AttendanceStudentDTO> att = new HashSet<>();
		for (int i = 0; i < attendance.size(); i++) {
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(attendance.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(attendance.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			for (int j = 0; j < student.getSchedId().length; j++) {
				Schedule schedule = dslContext.selectFrom(SCHEDULE)
						.where(SCHEDULE.SCHED_ID.eq(student.getSchedId()[j]))
						.fetchOneInto(Schedule.class);
			
				if (schedule.getSection().equalsIgnoreCase(section.getSection()) && sub.getSubjectId() == schedule.getSubjectId() && attendance.get(i).getYearLevel().equals(student.getYearLevel()) 
						&& attendance.get(i).getSem().equals(student.getSem())) {
					AttendanceStudentDTO attendanceDTO = AttendanceStudentDTO.builder()
							.attendanceId(attendance.get(i).getAttendanceId())
							.student(student)
							.subject(subject)
							.sem(attendance.get(i).getSem())
							.yearLevel(attendance.get(i).getYearLevel())
							.academicYear(attendance.get(i).getAcademicYear())
							.date(attendance.get(i).getDate())
							.time(attendance.get(i).getTime())
							.status(attendance.get(i).getStatus())
							.activeDeactive(attendance.get(i).getActiveDeactive())
							.build();
					attDTOs.add(attendanceDTO);
				}
			}
			
		}
		List<AttendanceStudentDTO> temp = new ArrayList<>();
		for (int i = 0; i < attDTOs.size(); i++) {
			if (attDTOs.get(i).getSubject().getSubjectId() == subjectId) {
				temp.add(attDTOs.get(i));
			}
		}
		for (int i = 0; i < temp.size(); i++) {
			att.add(temp.get(i));
		}
		
		List<AttendanceStudentDTO> attend = new ArrayList<>(att);
		for (int i = 0; i < attend.size(); i++) {
			AttendanceStudent attendance1 = dslContext.selectFrom(ATTENDANCE)
					.where(ATTENDANCE.STUDENT_ID.eq(attend.get(i).getStudent().getStudentId()))
					.and(ATTENDANCE.SUBJECT_ID.eq(attend.get(i).getSubject().getSubjectId()))
					.and(ATTENDANCE.DATE.eq(LocalDate.now()))
					.fetchOneInto(AttendanceStudent.class);
			if (attendance1 == null) {
				if (attend.get(i).getStudent().getSchedId() != null) {
					for (int j = 0; j < attend.get(i).getStudent().getSchedId().length; j++) {
						Schedule sched = dslContext.selectFrom(SCHEDULE)
								.where(SCHEDULE.SCHED_ID.eq(attend.get(i).getStudent().getSchedId()[j]))
								.fetchOneInto(Schedule.class);
						 LocalDate currentDate = LocalDate.now();
					     DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
					     String fullDayName = dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault());
						if (sched.getDay().equalsIgnoreCase(fullDayName) && sched.getSubjectId() == subjectId) {
							dslContext.insertInto(ATTENDANCE)
							.set(ATTENDANCE.STUDENT_ID, attend.get(i).getStudent().getStudentId())
							.set(ATTENDANCE.SUBJECT_ID, attend.get(i).getSubject().getSubjectId())
							.set(ATTENDANCE.SEM, attend.get(i).getStudent().getSem())
							.set(ATTENDANCE.YEAR_LEVEL, attend.get(i).getStudent().getYearLevel())
							.set(ATTENDANCE.ACADEMIC_YEAR, attend.get(i).getStudent().getAcademicYear())
							.set(ATTENDANCE.DATE, LocalDate.now())
							.set(ATTENDANCE.ACTIVE_DEACTIVE, true)
							.execute();
						}
					}
				}
			} 
		}
		return att;
	}
	
	public HashSet<AttendanceStudentDTO> getAttendanceBySection(Integer sectionId) {
		List<AttendanceStudent> attendance = dslContext.selectFrom(ATTENDANCE)
				.fetchInto(AttendanceStudent.class);
		Section section = dslContext.selectFrom(SECTION)
				.where(SECTION.SECTION_ID.eq(sectionId))
				.fetchOneInto(Section.class);
		List<AttendanceStudentDTO> attDTOs = new ArrayList<>();
		HashSet<AttendanceStudentDTO> att = new HashSet<>();
		for (int i = 0; i < attendance.size(); i++) {
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(attendance.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(attendance.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			for (int j = 0; j < student.getSchedId().length; j++) {
				Schedule schedule = dslContext.selectFrom(SCHEDULE)
						.where(SCHEDULE.SCHED_ID.eq(student.getSchedId()[j]))
						.fetchOneInto(Schedule.class);
			
				if (schedule.getSection().equalsIgnoreCase(section.getSection()) && attendance.get(i).getYearLevel().equals(student.getYearLevel()) && attendance.get(i).getSem().equals(student.getSem())) {
					AttendanceStudentDTO attendanceDTO = AttendanceStudentDTO.builder()
							.attendanceId(attendance.get(i).getAttendanceId())
							.student(student)
							.subject(subject)
							.sem(attendance.get(i).getSem())
							.yearLevel(attendance.get(i).getYearLevel())
							.academicYear(attendance.get(i).getAcademicYear())
							.date(attendance.get(i).getDate())
							.time(attendance.get(i).getTime())
							.status(attendance.get(i).getStatus())
							.activeDeactive(attendance.get(i).getActiveDeactive())
							.build();
					attDTOs.add(attendanceDTO);
				}
			}
			
		}
		for (int i = 0; i < attDTOs.size(); i++) {
			att.add(attDTOs.get(i));
		}
		return att;
	}
	
	public Response addAttendance(AttendanceStudent attendance) {
		dslContext.insertInto(ATTENDANCE)
		.set(ATTENDANCE.STUDENT_ID, attendance.getStudentId())
		.set(ATTENDANCE.SUBJECT_ID, attendance.getSubjectId())
		.set(ATTENDANCE.SEM, attendance.getSem())
		.set(ATTENDANCE.YEAR_LEVEL, attendance.getYearLevel())
		.set(ATTENDANCE.ACADEMIC_YEAR, attendance.getAcademicYear())
		.set(ATTENDANCE.STATUS, attendance.getStatus())
		.set(ATTENDANCE.ACTIVE_DEACTIVE, true)
		.execute();
		return Response.builder()
				.status(201)
				.message("Attendance successfully created")
				.timestamp(LocalDateTime.now())
				.build();
		
	}
	
	public Response addAttendance(Integer sectionId, Integer subjectId) {
		List<Student> students = dslContext.selectFrom(STUDENT)
				.fetchInto(Student.class);
		for (int i = 0; i < students.size(); i++) {
			for (int j = 0; j < students.get(i).getSchedId().length; j++) {
				Schedule schedule = dslContext.selectFrom(SCHEDULE)
						.where(SCHEDULE.SCHED_ID.eq(students.get(i).getSchedId()[j]))
						.fetchOneInto(Schedule.class);
				Section section = dslContext.selectFrom(SECTION)
						.where(SECTION.SECTION_.eq(schedule.getSection()))
						.fetchOneInto(Section.class);
				Subject subject = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(schedule.getSubjectId()))
						.fetchOneInto(Subject.class);
				if (section.getSectionId() == sectionId && subject.getSubjectId() == subjectId) {
					List<AttendanceStudent> attendance = dslContext.selectFrom(ATTENDANCE)
							.where(ATTENDANCE.STUDENT_ID.eq(students.get(i).getStudentId()))
							.fetchInto(AttendanceStudent.class);

					if (attendance.size() > 0) {
						dslContext.insertInto(ATTENDANCE)
						.set(ATTENDANCE.STUDENT_ID, students.get(i).getStudentId())
						.set(ATTENDANCE.SUBJECT_ID, subject.getSubjectId())
						.set(ATTENDANCE.SEM, students.get(i).getSem())
						.set(ATTENDANCE.YEAR_LEVEL, students.get(i).getYearLevel())
						.set(ATTENDANCE.ACADEMIC_YEAR, students.get(i).getAcademicYear())
						.set(ATTENDANCE.ACTIVE_DEACTIVE, true)
						.execute();
					}
				}
			}
		}
		return null;
	}
	
	public Response updateSubject(Integer attendaceId, AttendanceStudent attendance) {
		AttendanceStudent _attendance = dslContext.selectFrom(ATTENDANCE)
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.fetchOneInto(AttendanceStudent.class);
		if(_attendance != null) {
			if(attendance.getStudentId() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.STUDENT_ID, attendance.getStudentId())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			if(attendance.getSem() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.SEM, attendance.getSem())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			if(attendance.getYearLevel() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.YEAR_LEVEL, attendance.getYearLevel())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			if(attendance.getAcademicYear() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.ACADEMIC_YEAR, attendance.getAcademicYear())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			if(attendance.getStudentId() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.STUDENT_ID, attendance.getStudentId())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			if(attendance.getDate() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.DATE, attendance.getDate())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			if(attendance.getTime() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.TIME, attendance.getTime())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			if(attendance.getStatus() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.STATUS, attendance.getStatus())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			if(attendance.getActiveDeactive() != null) {
				dslContext.update(ATTENDANCE)
				.set(ATTENDANCE.ACTIVE_DEACTIVE, attendance.getActiveDeactive())
				.where(ATTENDANCE.ATTENDANCE_ID.eq(attendaceId))
				.execute();
			}
			return Response.builder()
					.status(200)
					.message("Attendance successfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Attendance not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}