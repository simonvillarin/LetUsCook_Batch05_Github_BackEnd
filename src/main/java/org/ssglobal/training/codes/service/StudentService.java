package org.ssglobal.training.codes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.ssglobal.training.codes.dto.ScheduleDTO;
import org.ssglobal.training.codes.dto.StudentDTO;
import org.ssglobal.training.codes.request.StudentRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Account;
import org.ssglobal.training.codes.tables.pojos.Admin;
import org.ssglobal.training.codes.tables.pojos.Image;
import org.ssglobal.training.codes.tables.pojos.Parent;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Program;
import org.ssglobal.training.codes.tables.pojos.Room;
import org.ssglobal.training.codes.tables.pojos.Schedule;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Student;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentService {
	private final DSLContext dslContext;
	private final PasswordEncoder passwordEncoder;
	private final PasswordGenerator passwordGenerator;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Program PROGRAM = org.ssglobal.training.codes.tables.Program.PROGRAM;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	private final org.ssglobal.training.codes.tables.Schedule SCHEDULE = org.ssglobal.training.codes.tables.Schedule.SCHEDULE;
	private final org.ssglobal.training.codes.tables.Account ACCOUNT = org.ssglobal.training.codes.tables.Account.ACCOUNT;
	private final org.ssglobal.training.codes.tables.Parent PARENT = org.ssglobal.training.codes.tables.Parent.PARENT;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Section SECTION = org.ssglobal.training.codes.tables.Section.SECTION;
	private final org.ssglobal.training.codes.tables.Room ROOM = org.ssglobal.training.codes.tables.Room.ROOM;
	private final org.ssglobal.training.codes.tables.Image IMAGE = org.ssglobal.training.codes.tables.Image.IMAGE;
	private final org.ssglobal.training.codes.tables.Admin ADMIN = org.ssglobal.training.codes.tables.Admin.ADMIN;
	
	public List<StudentDTO> getAllStudents() {
		List<Student> students = dslContext.selectFrom(STUDENT)
				.fetchInto(Student.class);
		
		List<StudentDTO> studentDTOs = new ArrayList<>();
		for (int i = 0; i < students.size(); i++) {		
			Program program = dslContext.selectFrom(PROGRAM)
					.where(PROGRAM.PROGRAM_ID.eq(students.get(i).getProgramId()))
					.fetchOneInto(Program.class);
			Parent parent = dslContext.selectFrom(PARENT)
					.where(PARENT.PARENT_ID.eq(students.get(i).getParentId()))
					.fetchOneInto(Parent.class);
			List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
			List<ScheduleDTO> tempSchedDTOs = new ArrayList<>();
			HashSet<ScheduleDTO> scheds = new HashSet<>();
			
			if (students.get(i).getSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int j = 0; j < students.get(i).getSchedId().length; j++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(students.get(i).getSchedId()[j]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
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
				
				for (int j = 0; j < scheduleDTOs.size(); j++) {
					scheds.add(scheduleDTOs.get(j));
				}
			}
			
			
			if (students.get(i).getTempSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int j = 0; j < students.get(i).getTempSchedId().length; j++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(students.get(i).getTempSchedId()[j]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
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

					if (tempSchedDTOs.size() > 0) {
						boolean isExist = false;
						for (int k = 0; k < tempSchedDTOs.size(); k++) {
							if (tempSchedDTOs.get(k).getSubject().getSubjectId() == schedules.get(j).getSubjectId() 
									&& tempSchedDTOs.get(k).getSection().getSection().equalsIgnoreCase(schedules.get(j).getSection())) {
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
							tempSchedDTOs.add(scheduleDTO);
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
						tempSchedDTOs.add(scheduleDTO);
					}
				}
			}	
			
			HashSet<ScheduleDTO> tempSched = new HashSet<>();
			for (int j = 0; j < tempSchedDTOs.size(); j++) {
				tempSched.add(tempSchedDTOs.get(j));
			}
			
			String type = "";
			HashSet<String> sections = new HashSet<>();
			
			if (students.get(i).getSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int j = 0; j < students.get(i).getSchedId().length; j++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(students.get(i).getSchedId()[j]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
				}
			}
			
			if (students.get(i).getSchedId() == null) {
				type = "";
			} else if (sections.size() > 1){
				type = "Irregular";
			} else {
				type = "Regular";
			}
			
			
			StudentDTO studentDTO = StudentDTO.builder()
					.studentId(students.get(i).getStudentId())
					.studentNo(students.get(i).getStudentNo())
					.program(program)
					.schedules(scheds)
					.tempSched(tempSched)
					.yearLevel(students.get(i).getYearLevel())
					.sem(students.get(i).getSem())
					.academicYear(students.get(i).getAcademicYear())
					.firstname(students.get(i).getFirstname())
					.middlename(students.get(i).getMiddlename())
					.lastname(students.get(i).getLastname())
					.suffix(students.get(i).getSuffix())
					.gender(students.get(i).getGender())
					.civilStatus(students.get(i).getCivilStatus())
					.birthdate(students.get(i).getBirthdate())
					.birthplace(students.get(i).getBirthplace())
					.citizenship(students.get(i).getCitizenship())
					.religion(students.get(i).getReligion())
					.unit(students.get(i).getUnit())
					.street(students.get(i).getStreet())
					.subdivision(students.get(i).getSubdivision())
					.barangay(students.get(i).getBarangay())
					.city(students.get(i).getCity())
					.province(students.get(i).getProvince())
					.zipcode(students.get(i).getZipcode())
					.telephone(students.get(i).getTelephone())
					.mobile(students.get(i).getMobile())
					.email(students.get(i).getEmail())
					.lastSchoolAttended(students.get(i).getLastSchoolAttended())
					.programTaken(students.get(i).getProgramTaken())
					.lastSem(students.get(i).getLastSem())
					.lastYearLevel(students.get(i).getYearLevel())
					.lastSchoolYear(students.get(i).getLastSchoolYear())
					.dateOfGraduation(students.get(i).getDateOfGraduation())
					.parent(parent)
					.image(students.get(i).getImage())
					.banner(students.get(i).getBanner())
					.type(type)
					.dateEnrolled(students.get(i).getDateEnrolled())
					.appId(students.get(i).getAppId())
					.activeDeactive(students.get(i).getActiveDeactive())
					.build();
			studentDTOs.add(studentDTO);
		}
		return studentDTOs;
	}
	
	
	public StudentDTO getStudentById(Integer studentId) {
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		if (student != null) {	
			Program program = dslContext.selectFrom(PROGRAM)
					.where(PROGRAM.PROGRAM_ID.eq(student.getProgramId()))
					.fetchOneInto(Program.class);
			Parent parent = dslContext.selectFrom(PARENT)
					.where(PARENT.PARENT_ID.eq(student.getParentId()))
					.fetchOneInto(Parent.class);
			List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
			List<ScheduleDTO> tempSchedDTOs = new ArrayList<>();
			HashSet<ScheduleDTO> scheds = new HashSet<>();
			
			if (student.getSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int i = 0; i < student.getSchedId().length; i++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(student.getSchedId()[i]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
				}
				
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
				for (int i = 0; i < scheduleDTOs.size(); i++) {
					scheds.add(scheduleDTOs.get(i));
				}
			}
			
			
			if (student.getTempSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int i = 0; i < student.getTempSchedId().length; i++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(student.getTempSchedId()[i]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
				}
				
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

					if (tempSchedDTOs.size() > 0) {
						boolean isExist = false;
						for (int k = 0; k < tempSchedDTOs.size(); k++) {
							if (tempSchedDTOs.get(k).getSubject().getSubjectId() == schedules.get(i).getSubjectId() 
									&& tempSchedDTOs.get(k).getSection().getSection().equalsIgnoreCase(schedules.get(i).getSection())) {
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
							tempSchedDTOs.add(scheduleDTO);
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
						tempSchedDTOs.add(scheduleDTO);
					}
				}
			}
			
			HashSet<ScheduleDTO> tempSched = new HashSet<>();
			for (int i = 0; i < tempSchedDTOs.size(); i++) {
				tempSched.add(tempSchedDTOs.get(i));
			}
			
			String type = "";
			HashSet<String> sections = new HashSet<>();
			
			if (student.getSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int i = 0; i < student.getSchedId().length; i++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(student.getSchedId()[i]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
						sections.add(schedule.getSection());
					}
				}
			}
			
			if (student.getSchedId() == null) {
				type = "";
			} else if (sections.size() > 1){
				type = "Irregular";
			} else {
				type = "Regular";
			}
			
			return StudentDTO.builder()
					.studentId(student.getStudentId())
					.studentNo(student.getStudentNo())
					.program(program)
					.schedules(scheds)
					.tempSched(tempSched)
					.yearLevel(student.getYearLevel())
					.sem(student.getSem())
					.academicYear(student.getAcademicYear())
					.firstname(student.getFirstname())
					.middlename(student.getMiddlename())
					.lastname(student.getLastname())
					.suffix(student.getSuffix())
					.gender(student.getGender())
					.civilStatus(student.getCivilStatus())
					.birthdate(student.getBirthdate())
					.birthplace(student.getBirthplace())
					.citizenship(student.getCitizenship())
					.religion(student.getReligion())
					.image(student.getImage())
					.unit(student.getUnit())
					.street(student.getStreet())
					.subdivision(student.getSubdivision())
					.barangay(student.getBarangay())
					.city(student.getCity())
					.province(student.getProvince())
					.zipcode(student.getZipcode())
					.telephone(student.getTelephone())
					.mobile(student.getMobile())
					.email(student.getEmail())
					.lastSchoolAttended(student.getLastSchoolAttended())
					.programTaken(student.getProgramTaken())
					.lastSem(student.getLastSem())
					.lastYearLevel(student.getYearLevel())
					.lastSchoolYear(student.getLastSchoolYear())
					.dateOfGraduation(student.getDateOfGraduation())
					.parent(parent)
					.image(student.getImage())
					.banner(student.getBanner())
					.type(type)
					.dateEnrolled(student.getDateEnrolled())
					.appId(student.getAppId())
					.activeDeactive(student.getActiveDeactive())
					.build();
		} else {
			throw new RuntimeException("Student not found");
		}
	}
	
	public List<StudentDTO> getStudentByParentId(Integer parentID) {
		List<Student> students =  dslContext.selectFrom(STUDENT)
				.where(STUDENT.PARENT_ID.eq(parentID))
				.fetchInto(Student.class);		
		
		List<StudentDTO> studentDTOs = new ArrayList<>();
		for (int i = 0; i < students.size(); i++) {		
			Program program = dslContext.selectFrom(PROGRAM)
					.where(PROGRAM.PROGRAM_ID.eq(students.get(i).getProgramId()))
					.fetchOneInto(Program.class);
			Parent parent = dslContext.selectFrom(PARENT)
					.where(PARENT.PARENT_ID.eq(students.get(i).getParentId()))
					.fetchOneInto(Parent.class);
			List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
			List<ScheduleDTO> tempSchedDTOs = new ArrayList<>();
			HashSet<ScheduleDTO> scheds = new HashSet<>();
			
			if (students.get(i).getSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int j = 0; j < students.get(i).getSchedId().length; j++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(students.get(i).getSchedId()[j]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
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
				
				for (int j = 0; j < scheduleDTOs.size(); j++) {
					scheds.add(scheduleDTOs.get(j));
				}
			}
			
			
			if (students.get(i).getTempSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int j = 0; j < students.get(i).getTempSchedId().length; j++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(students.get(i).getTempSchedId()[j]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
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

					if (tempSchedDTOs.size() > 0) {
						boolean isExist = false;
						for (int k = 0; k < tempSchedDTOs.size(); k++) {
							if (tempSchedDTOs.get(k).getSubject().getSubjectId() == schedules.get(j).getSubjectId() 
									&& tempSchedDTOs.get(k).getSection().getSection().equalsIgnoreCase(schedules.get(j).getSection())) {
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
							tempSchedDTOs.add(scheduleDTO);
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
						tempSchedDTOs.add(scheduleDTO);
					}
				}
			}	
			
			HashSet<ScheduleDTO> tempSched = new HashSet<>();
			for (int j = 0; j < tempSchedDTOs.size(); j++) {
				tempSched.add(tempSchedDTOs.get(j));
			}
			
			String type = "";
			HashSet<String> sections = new HashSet<>();
			
			if (students.get(i).getSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int j = 0; j < students.get(i).getSchedId().length; j++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(students.get(i).getSchedId()[j]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
						sections.add(schedule.getSection());
					}
				}
			}

			if (students.get(i).getSchedId() == null) {
				type = "";
			} else if (sections.size() > 1){
				type = "Irregular";
			} else {
				type = "Regular";
			}
			
			
			StudentDTO studentDTO = StudentDTO.builder()
					.studentId(students.get(i).getStudentId())
					.studentNo(students.get(i).getStudentNo())
					.program(program)
					.schedules(scheds)
					.tempSched(tempSched)
					.yearLevel(students.get(i).getYearLevel())
					.sem(students.get(i).getSem())
					.academicYear(students.get(i).getAcademicYear())
					.firstname(students.get(i).getFirstname())
					.middlename(students.get(i).getMiddlename())
					.lastname(students.get(i).getLastname())
					.suffix(students.get(i).getSuffix())
					.gender(students.get(i).getGender())
					.civilStatus(students.get(i).getCivilStatus())
					.birthdate(students.get(i).getBirthdate())
					.birthplace(students.get(i).getBirthplace())
					.citizenship(students.get(i).getCitizenship())
					.religion(students.get(i).getReligion())
					.unit(students.get(i).getUnit())
					.street(students.get(i).getStreet())
					.subdivision(students.get(i).getSubdivision())
					.barangay(students.get(i).getBarangay())
					.city(students.get(i).getCity())
					.province(students.get(i).getProvince())
					.zipcode(students.get(i).getZipcode())
					.telephone(students.get(i).getTelephone())
					.mobile(students.get(i).getMobile())
					.email(students.get(i).getEmail())
					.lastSchoolAttended(students.get(i).getLastSchoolAttended())
					.programTaken(students.get(i).getProgramTaken())
					.lastSem(students.get(i).getLastSem())
					.lastYearLevel(students.get(i).getYearLevel())
					.lastSchoolYear(students.get(i).getLastSchoolYear())
					.dateOfGraduation(students.get(i).getDateOfGraduation())
					.parent(parent)
					.image(students.get(i).getImage())
					.banner(students.get(i).getBanner())
					.type(type)
					.dateEnrolled(students.get(i).getDateEnrolled())
					.appId(students.get(i).getAppId())
					.activeDeactive(students.get(i).getActiveDeactive())
					.build();
			studentDTOs.add(studentDTO);
		}
		return studentDTOs;
	}
	
	public StudentDTO getStudentByStudentNo(String studentNo) {
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_NO.eq(studentNo))
				.fetchOneInto(Student.class);
		if (student != null) {	
			Program program = dslContext.selectFrom(PROGRAM)
					.where(PROGRAM.PROGRAM_ID.eq(student.getProgramId()))
					.fetchOneInto(Program.class);
			Parent parent = dslContext.selectFrom(PARENT)
					.where(PARENT.PARENT_ID.eq(student.getParentId()))
					.fetchOneInto(Parent.class);
			List<ScheduleDTO> scheduleDTOs = new ArrayList<>();
			List<ScheduleDTO> tempSchedDTOs = new ArrayList<>();
			HashSet<ScheduleDTO> scheds = new HashSet<>();
			
			if (student.getSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int i = 0; i < student.getSchedId().length; i++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(student.getSchedId()[i]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
				}
				
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
				for (int i = 0; i < scheduleDTOs.size(); i++) {
					scheds.add(scheduleDTOs.get(i));
				}
			}
			
			if (student.getTempSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int i = 0; i < student.getTempSchedId().length; i++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(student.getTempSchedId()[i]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
					}
				}
				
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

					if (tempSchedDTOs.size() > 0) {
						boolean isExist = false;
						for (int k = 0; k < tempSchedDTOs.size(); k++) {
							if (tempSchedDTOs.get(k).getSubject().getSubjectId() == schedules.get(i).getSubjectId() 
									&& tempSchedDTOs.get(k).getSection().getSection().equalsIgnoreCase(schedules.get(i).getSection())) {
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
							tempSchedDTOs.add(scheduleDTO);
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
						tempSchedDTOs.add(scheduleDTO);
					}

				}
			}
			
			HashSet<ScheduleDTO> tempSched = new HashSet<>();
			for (int i = 0; i < tempSchedDTOs.size(); i++) {
				tempSched.add(tempSchedDTOs.get(i));
			}
			
			String type = "";
			HashSet<String> sections = new HashSet<>();
			
			if (student.getSchedId() != null) {
				List<Schedule> schedules = new ArrayList<>();
				for (int j = 0; j < student.getSchedId().length; j++) {
					Schedule schedule = dslContext.selectFrom(SCHEDULE)
							.where(SCHEDULE.SCHED_ID.eq(student.getSchedId()[j]))
							.fetchOneInto(Schedule.class);
					if (schedule.getActiveDeactive() == true) {
						schedules.add(schedule);
						sections.add(schedule.getSection());
					}
				}
			}

			if (student.getSchedId() == null) {
				type = "";
			} else if (sections.size() > 1){
				type = "Irregular";
			} else {
				type = "Regular";
			}
			
			
			return StudentDTO.builder()
					.studentId(student.getStudentId())
					.studentNo(student.getStudentNo())
					.program(program)
					.schedules(scheds)
					.tempSched(tempSched)
					.yearLevel(student.getYearLevel())
					.sem(student.getSem())
					.academicYear(student.getAcademicYear())
					.firstname(student.getFirstname())
					.middlename(student.getMiddlename())
					.lastname(student.getLastname())
					.suffix(student.getSuffix())
					.gender(student.getGender())
					.civilStatus(student.getCivilStatus())
					.birthdate(student.getBirthdate())
					.birthplace(student.getBirthplace())
					.citizenship(student.getCitizenship())
					.religion(student.getReligion())
					.unit(student.getUnit())
					.street(student.getStreet())
					.subdivision(student.getSubdivision())
					.barangay(student.getBarangay())
					.city(student.getCity())
					.province(student.getProvince())
					.zipcode(student.getZipcode())
					.telephone(student.getTelephone())
					.mobile(student.getMobile())
					.email(student.getEmail())
					.lastSchoolAttended(student.getLastSchoolAttended())
					.programTaken(student.getProgramTaken())
					.lastSem(student.getLastSem())
					.lastYearLevel(student.getYearLevel())
					.lastSchoolYear(student.getLastSchoolYear())
					.dateOfGraduation(student.getDateOfGraduation())
					.parent(parent)
					.image(student.getImage())
					.banner(student.getBanner())
					.type(type)
					.dateEnrolled(student.getDateEnrolled())
					.appId(student.getAppId())
					.activeDeactive(student.getActiveDeactive())
					.build();
		} else {
			return null;
		}
	}
	
	
	public Response addStudents(List<StudentRequest> students) {
		for (int i = 0; i < students.size(); i++) {
			Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(students.get(i).getEmail())).fetchOneInto(Admin.class);
			Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(students.get(i).getEmail()))
					.fetchOneInto(Professor.class);
			Student student = dslContext.selectFrom(STUDENT).where(STUDENT.EMAIL.eq(students.get(i).getEmail())).fetchOneInto(Student.class);
			if (admin != null || professor != null || student != null) {
				return Response.builder()
						.status(409)
						.message("Email already exists")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				String parentNo = generateUserId();
				dslContext.insertInto(PARENT)
				.set(PARENT.PARENT_NO, parentNo)
				.set(PARENT.FIRSTNAME, students.get(i).getParentFirstname())
				.set(PARENT.MIDDLENAME, students.get(i).getParentMiddlename())
				.set(PARENT.LASTNAME, students.get(i).getParentLastname())
				.set(PARENT.SUFFIX, students.get(i).getParentSuffix())
				.set(PARENT.ADDRESS, students.get(i).getParentAddress())
				.set(PARENT.CONTACT, students.get(i).getParentContact())
				.set(PARENT.EMAIL, students.get(i).getParentEmail())
				.set(PARENT.RELATIONSHIP, students.get(i).getParentRelationship())
				.set(PARENT.IMAGE, "")
				.set(PARENT.BANNER, "")
				.set(PARENT.ACTIVE_DEACTIVE, true)
				.execute();
				
				Parent parent = dslContext.selectFrom(PARENT)
						.orderBy(PARENT.PARENT_ID.desc())
						.limit(1)
						.fetchOneInto(Parent.class);
				
				String studentNo = generateUserId();
				dslContext.insertInto(STUDENT)
				.set(STUDENT.STUDENT_NO, studentNo)
				.set(STUDENT.PROGRAM_ID, students.get(i).getProgram().getProgramId())
				.set(STUDENT.YEAR_LEVEL, students.get(i).getYearLevel())
				.set(STUDENT.SEM, students.get(i).getSem())
				.set(STUDENT.ACADEMIC_YEAR, students.get(i).getAcademicYear())
				.set(STUDENT.FIRSTNAME, students.get(i).getFirstname())
				.set(STUDENT.MIDDLENAME, students.get(i).getMiddlename())
				.set(STUDENT.LASTNAME, students.get(i).getLastname())
				.set(STUDENT.SUFFIX, students.get(i).getSuffix())
				.set(STUDENT.GENDER, students.get(i).getGender())
				.set(STUDENT.CIVIL_STATUS, students.get(i).getCivilStatus())
				.set(STUDENT.BIRTHDATE, students.get(i).getBirthdate())
				.set(STUDENT.BIRTHPLACE, students.get(i).getBirthplace())
				.set(STUDENT.CITIZENSHIP, students.get(i).getCitizenship())
				.set(STUDENT.RELIGION, students.get(i).getReligion())
				.set(STUDENT.UNIT, students.get(i).getUnit())
				.set(STUDENT.STREET, students.get(i).getStreet())
				.set(STUDENT.SUBDIVISION, students.get(i).getSubdivision())
				.set(STUDENT.BARANGAY, students.get(i).getBarangay())
				.set(STUDENT.CITY, students.get(i).getCity())
				.set(STUDENT.PROVINCE, students.get(i).getProvince())
				.set(STUDENT.ZIPCODE, students.get(i).getZipcode())
				.set(STUDENT.TELEPHONE, students.get(i).getTelephone())
				.set(STUDENT.MOBILE, students.get(i).getMobile())
				.set(STUDENT.EMAIL, students.get(i).getEmail())
				.set(STUDENT.LAST_SCHOOL_ATTENDED, students.get(i).getLastSchoolAttended())
				.set(STUDENT.PROGRAM_TAKEN, students.get(i).getProgramTaken())
				.set(STUDENT.LAST_SEM, students.get(i).getLastSem())
				.set(STUDENT.LAST_YEAR_LEVEL, students.get(i).getLastYearLevel())
				.set(STUDENT.LAST_SCHOOL_YEAR, students.get(i).getLastSchoolYear())
				.set(STUDENT.DATE_OF_GRADUATION, students.get(i).getDateOfGraduation())
				.set(STUDENT.PARENT_ID, parent.getParentId())
				.set(STUDENT.IMAGE, "")
				.set(STUDENT.DATE_ENROLLED, LocalDate.now())
				.set(STUDENT.BANNER, "")
				.set(STUDENT.APP_ID, students.get(i).getAppId())
				.set(STUDENT.ACTIVE_DEACTIVE, true)
				.execute();
				
			}
		}
		return Response.builder()
				.status(201)
				.message("Student successfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response addStudent(StudentRequest student) {
		Student email = dslContext.selectFrom(STUDENT)
				.where(STUDENT.EMAIL.eq(student.getEmail()))
				.fetchOneInto(Student.class);
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(student.getEmail())).fetchOneInto(Admin.class);
		Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(student.getEmail()))
				.fetchOneInto(Professor.class);
		Parent par = dslContext.selectFrom(PARENT).where(PARENT.EMAIL.eq(student.getEmail())).fetchOneInto(Parent.class);
		
		if (email != null || admin != null || professor != null || par != null) {
			return Response.builder()
					.status(409)
					.message("Email already exists")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			Parent existingParent = dslContext.selectFrom(PARENT)
					.where(PARENT.FIRSTNAME.similarTo(student.getParentFirstname()))
					.and(PARENT.MIDDLENAME.similarTo(student.getParentMiddlename()))
					.and(PARENT.LASTNAME.similarTo(student.getParentLastname()))
					.and(PARENT.SUFFIX.similarTo(student.getParentSuffix()))
					.and(PARENT.EMAIL.similarTo(student.getParentEmail()))
					.fetchOneInto(Parent.class);
			Integer parentId = 0;
			if (existingParent == null) {
				String parentNo = generateUserId();
				dslContext.insertInto(PARENT)
				.set(PARENT.PARENT_NO, parentNo)
				.set(PARENT.FIRSTNAME, student.getParentFirstname())
				.set(PARENT.MIDDLENAME, student.getParentMiddlename())
				.set(PARENT.LASTNAME, student.getParentLastname())
				.set(PARENT.SUFFIX, student.getParentSuffix())
				.set(PARENT.ADDRESS, student.getParentAddress())
				.set(PARENT.CONTACT, student.getParentContact())
				.set(PARENT.EMAIL, student.getParentEmail())
				.set(PARENT.RELATIONSHIP, student.getParentRelationship())
				.set(PARENT.IMAGE, "")
				.set(PARENT.BANNER, "")
				.set(PARENT.ACTIVE_DEACTIVE, true)
				.execute();
				
				Parent parent = dslContext.selectFrom(PARENT)
						.orderBy(PARENT.PARENT_ID.desc())
						.limit(1)
						.fetchOneInto(Parent.class);
				
				parentId = parent.getParentId();
				
				String parentPassword = passwordGenerator.generatePassword();
				dslContext.insertInto(ACCOUNT)
				.set(ACCOUNT.USER_ID, parent.getParentId())
				.set(ACCOUNT.USERNAME, parentNo)
				.set(ACCOUNT.PASSWORD, passwordEncoder.encode(parentPassword))
				.set(ACCOUNT.PASS, parentPassword)
				.set(ACCOUNT.TYPE, "PARENT")
				.set(ACCOUNT.ACTIVE_DEACTIVE, true)
				.execute();
			} else {
				parentId = existingParent.getParentId();
			}
			String studentNo = generateUserId();
			dslContext.insertInto(STUDENT)
			.set(STUDENT.STUDENT_NO, studentNo)
			.set(STUDENT.PROGRAM_ID, student.getProgram().getProgramId())
			.set(STUDENT.YEAR_LEVEL, student.getYearLevel())
			.set(STUDENT.SEM, student.getSem())
			.set(STUDENT.ACADEMIC_YEAR, student.getAcademicYear())
			.set(STUDENT.FIRSTNAME, student.getFirstname())
			.set(STUDENT.MIDDLENAME, student.getMiddlename())
			.set(STUDENT.LASTNAME, student.getLastname())
			.set(STUDENT.SUFFIX, student.getSuffix())
			.set(STUDENT.GENDER, student.getGender())
			.set(STUDENT.CIVIL_STATUS, student.getCivilStatus())
			.set(STUDENT.BIRTHDATE, student.getBirthdate())
			.set(STUDENT.BIRTHPLACE, student.getBirthplace())
			.set(STUDENT.CITIZENSHIP, student.getCitizenship())
			.set(STUDENT.RELIGION, student.getReligion())
			.set(STUDENT.UNIT, student.getUnit())
			.set(STUDENT.STREET, student.getStreet())
			.set(STUDENT.SUBDIVISION, student.getSubdivision())
			.set(STUDENT.BARANGAY, student.getBarangay())
			.set(STUDENT.CITY, student.getCity())
			.set(STUDENT.PROVINCE, student.getProvince())
			.set(STUDENT.ZIPCODE, student.getZipcode())
			.set(STUDENT.TELEPHONE, student.getTelephone())
			.set(STUDENT.MOBILE, student.getMobile())
			.set(STUDENT.EMAIL, student.getEmail())
			.set(STUDENT.LAST_SCHOOL_ATTENDED, student.getLastSchoolAttended())
			.set(STUDENT.PROGRAM_TAKEN, student.getProgramTaken())
			.set(STUDENT.LAST_SEM, student.getLastSem())
			.set(STUDENT.LAST_YEAR_LEVEL, student.getLastYearLevel())
			.set(STUDENT.LAST_SCHOOL_YEAR, student.getLastSchoolYear())
			.set(STUDENT.DATE_OF_GRADUATION, student.getDateOfGraduation())
			.set(STUDENT.PARENT_ID, parentId)
			.set(STUDENT.IMAGE, "")
			.set(STUDENT.DATE_ENROLLED, LocalDate.now())
			.set(STUDENT.BANNER, "")
			.set(STUDENT.APP_ID, student.getAppId())
			.set(STUDENT.ACTIVE_DEACTIVE, true)
			.execute();
			
			Student _student = dslContext.selectFrom(STUDENT)
					.orderBy(STUDENT.STUDENT_ID.desc())
					.limit(1)
					.fetchOneInto(Student.class);
			
			String studentPassword = passwordGenerator.generatePassword();
			dslContext.insertInto(ACCOUNT)
			.set(ACCOUNT.USER_ID, _student.getStudentId())
			.set(ACCOUNT.USERNAME, _student.getStudentNo())
			.set(ACCOUNT.PASSWORD, passwordEncoder.encode(studentPassword))
			.set(ACCOUNT.PASS, studentPassword)
			.set(ACCOUNT.TYPE, "STUDENT")
			.set(ACCOUNT.ACTIVE_DEACTIVE, true)
			.execute();
			
			
			
			Account account = dslContext.selectFrom(ACCOUNT)
					.where(ACCOUNT.USER_ID.eq(_student.getStudentId()))
					.fetchOneInto(Account.class);
			
			Account _parent =  dslContext.selectFrom(ACCOUNT)
					.where(ACCOUNT.USER_ID.eq(_student.getParentId()))
					.fetchOneInto(Account.class);
			
			return Response.builder()
					.status(201)
					.message(account.getUsername() + "-" + account.getPass() + "-" + _parent.getUsername() + "-" + _parent.getPass())
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response addStudentWithImage(StudentRequest student, MultipartFile image) {
		Student email = dslContext.selectFrom(STUDENT)
				.where(STUDENT.EMAIL.eq(student.getEmail()))
				.fetchOneInto(Student.class);
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(student.getEmail())).fetchOneInto(Admin.class);
		Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(student.getEmail()))
				.fetchOneInto(Professor.class);
		if (email != null || admin != null || professor != null) {
			return Response.builder()
					.status(409)
					.message("Email already exists")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			Image img = null;
			try {
				img = new Image(image.getOriginalFilename(), image.getContentType(), image.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (image != null) {
				Image _image = dslContext.selectFrom(IMAGE)
						.where(IMAGE.FILENAME.eq(img.getFilename()))
						.fetchOneInto(Image.class);
				if (_image == null) {
					dslContext.insertInto(IMAGE)
					.set(IMAGE.FILENAME, img.getFilename())
					.set(IMAGE.MIME_TYPE, img.getMimeType())
					.set(IMAGE.DATA, img.getData())
					.execute();
				}
			}
			
			String parentNo = generateUserId();
			dslContext.insertInto(PARENT)
			.set(PARENT.PARENT_NO, parentNo)
			.set(PARENT.FIRSTNAME, student.getParentFirstname())
			.set(PARENT.MIDDLENAME, student.getParentMiddlename())
			.set(PARENT.LASTNAME, student.getParentLastname())
			.set(PARENT.SUFFIX, student.getParentSuffix())
			.set(PARENT.ADDRESS, student.getParentAddress())
			.set(PARENT.CONTACT, student.getParentContact())
			.set(PARENT.EMAIL, student.getParentEmail())
			.set(PARENT.RELATIONSHIP, student.getParentRelationship())
			.set(PARENT.IMAGE, "")
			.set(PARENT.BANNER, "")
			.set(PARENT.ACTIVE_DEACTIVE, true)
			.execute();
			
			Parent parent = dslContext.selectFrom(PARENT)
					.orderBy(PARENT.PARENT_ID.desc())
					.limit(1)
					.fetchOneInto(Parent.class);
			
			String studentNo = generateUserId();
			dslContext.insertInto(STUDENT)
			.set(STUDENT.STUDENT_NO, studentNo)
			.set(STUDENT.PROGRAM_ID, student.getProgram().getProgramId())
			.set(STUDENT.YEAR_LEVEL, student.getYearLevel())
			.set(STUDENT.SEM, student.getSem())
			.set(STUDENT.ACADEMIC_YEAR, student.getAcademicYear())
			.set(STUDENT.FIRSTNAME, student.getFirstname())
			.set(STUDENT.MIDDLENAME, student.getMiddlename())
			.set(STUDENT.LASTNAME, student.getLastname())
			.set(STUDENT.SUFFIX, student.getSuffix())
			.set(STUDENT.GENDER, student.getGender())
			.set(STUDENT.CIVIL_STATUS, student.getCivilStatus())
			.set(STUDENT.BIRTHDATE, student.getBirthdate())
			.set(STUDENT.BIRTHPLACE, student.getBirthplace())
			.set(STUDENT.CITIZENSHIP, student.getCitizenship())
			.set(STUDENT.RELIGION, student.getReligion())
			.set(STUDENT.UNIT, student.getUnit())
			.set(STUDENT.STREET, student.getStreet())
			.set(STUDENT.SUBDIVISION, student.getSubdivision())
			.set(STUDENT.BARANGAY, student.getBarangay())
			.set(STUDENT.CITY, student.getCity())
			.set(STUDENT.PROVINCE, student.getProvince())
			.set(STUDENT.ZIPCODE, student.getZipcode())
			.set(STUDENT.TELEPHONE, student.getTelephone())
			.set(STUDENT.MOBILE, student.getMobile())
			.set(STUDENT.EMAIL, student.getEmail())
			.set(STUDENT.LAST_SCHOOL_ATTENDED, student.getLastSchoolAttended())
			.set(STUDENT.PROGRAM_TAKEN, student.getProgramTaken())
			.set(STUDENT.LAST_SEM, student.getLastSem())
			.set(STUDENT.LAST_YEAR_LEVEL, student.getLastYearLevel())
			.set(STUDENT.LAST_SCHOOL_YEAR, student.getLastSchoolYear())
			.set(STUDENT.DATE_OF_GRADUATION, student.getDateOfGraduation())
			.set(STUDENT.PARENT_ID, parent.getParentId())
			.set(STUDENT.IMAGE, createImageLink(image.getOriginalFilename()))
			.set(STUDENT.DATE_ENROLLED, LocalDate.now())
			.set(STUDENT.BANNER, "")
			.set(STUDENT.APP_ID, student.getAppId())
			.set(STUDENT.ACTIVE_DEACTIVE, true)
			.execute();
			
			Student _student = dslContext.selectFrom(STUDENT)
					.orderBy(STUDENT.STUDENT_ID.desc())
					.limit(1)
					.fetchOneInto(Student.class);
			
			String studentPassword = passwordGenerator.generatePassword();
			dslContext.insertInto(ACCOUNT)
			.set(ACCOUNT.USER_ID, _student.getStudentId())
			.set(ACCOUNT.USERNAME, _student.getEmail())
			.set(ACCOUNT.PASSWORD, passwordEncoder.encode(studentPassword))
			.set(ACCOUNT.PASS, studentPassword)
			.set(ACCOUNT.TYPE, "STUDENT")
			.set(ACCOUNT.ACTIVE_DEACTIVE, true)
			.execute();
			
			String parentPassword = passwordGenerator.generatePassword();
			dslContext.insertInto(ACCOUNT)
			.set(ACCOUNT.USER_ID, parent.getParentId())
			.set(ACCOUNT.USERNAME, parentNo)
			.set(ACCOUNT.PASSWORD, passwordEncoder.encode(parentPassword))
			.set(ACCOUNT.PASS, parentPassword)
			.set(ACCOUNT.TYPE, "PARENT")
			.set(ACCOUNT.ACTIVE_DEACTIVE, true)
			.execute();
			
			return Response.builder()
					.status(201)
					.message("Student successfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateImage(Integer studentId, MultipartFile image) {
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		if (student != null) {
			Image img = null;
			try {
				img = new Image(image.getOriginalFilename(), image.getContentType(), image.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (img != null) {
				Image _image = dslContext.selectFrom(IMAGE)
						.where(IMAGE.FILENAME.eq(img.getFilename()))
						.fetchOneInto(Image.class);
				if (_image == null) {
					dslContext.insertInto(IMAGE)
					.set(IMAGE.FILENAME, img.getFilename())
					.set(IMAGE.MIME_TYPE, img.getMimeType())
					.set(IMAGE.DATA, img.getData())
					.execute();
				}
			}
			
			dslContext.update(STUDENT)
			.set(STUDENT.IMAGE, createImageLink(image.getOriginalFilename()))
			.where(STUDENT.STUDENT_ID.eq(studentId))
			.execute();
			
			String imagee = createImageLink(image.getOriginalFilename());
			
			return Response.builder()
					.status(200)
					.message(imagee)
					.timestamp(LocalDateTime.now())
					.build();
			
		} else {
			return Response.builder()
					.status(404)
					.message("Student not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateBanner(Integer studentId, MultipartFile banner) {
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		if (student != null) {
			Image img = null;
			try {
				img = new Image(banner.getOriginalFilename(),banner.getContentType(), banner.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (img != null) {
				Image _image = dslContext.selectFrom(IMAGE)
						.where(IMAGE.FILENAME.eq(img.getFilename()))
						.fetchOneInto(Image.class);
				if (_image == null) {
					dslContext.insertInto(IMAGE)
					.set(IMAGE.FILENAME, img.getFilename())
					.set(IMAGE.MIME_TYPE, img.getMimeType())
					.set(IMAGE.DATA, img.getData())
					.execute();
				}
			}
			
			dslContext.update(STUDENT)
			.set(STUDENT.BANNER, createImageLink(banner.getOriginalFilename()))
			.where(STUDENT.STUDENT_ID.eq(studentId))
			.execute();
			
			return Response.builder()
					.status(200)
					.message("Student banner sucessfully updated")
					.timestamp(LocalDateTime.now())
					.build();
			
		} else {
			return Response.builder()
					.status(404)
					.message("Student not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateStudentWithImage(Integer studentId, StudentRequest student, MultipartFile image) {
		Student _student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		Student email = dslContext.selectFrom(STUDENT)
				.where(STUDENT.EMAIL.eq(student.getEmail()))
				.fetchOneInto(Student.class);
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(student.getEmail())).fetchOneInto(Admin.class);
		Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(student.getEmail()))
				.fetchOneInto(Professor.class);
		if (_student != null) {
			if (email != null || admin != null || professor != null) {
				return Response.builder()
						.status(409)
						.message("Email already exists")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				Image img = null;
				try {
					img = new Image(image.getOriginalFilename(), image.getContentType(), image.getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (img != null) {
					Image _image = dslContext.selectFrom(IMAGE)
							.where(IMAGE.FILENAME.eq(img.getFilename()))
							.fetchOneInto(Image.class);
					if (_image == null) {
						dslContext.insertInto(IMAGE)
						.set(IMAGE.FILENAME, img.getFilename())
						.set(IMAGE.MIME_TYPE, img.getMimeType())
						.set(IMAGE.DATA, img.getData())
						.execute();
					}
				}
				
				dslContext.update(STUDENT)
				.set(STUDENT.IMAGE, createImageLink(image.getOriginalFilename()))
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.execute();
				
				if (student.getProgram() != null) {
					Program program = dslContext.selectFrom(PROGRAM)
							.where(PROGRAM.PROGRAM_ID.eq(student.getProgram().getProgramId()))
							.fetchOneInto(Program.class);
					
					dslContext.update(STUDENT)
					.set(STUDENT.PROGRAM_ID, program.getProgramId())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getSchedId() != null || student.getSchedId().length == 0) {
					dslContext.update(STUDENT)
					.set(STUDENT.SCHED_ID, student.getSchedId())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getTempSchedId() != null || student.getTempSchedId().length == 0) {
					dslContext.update(STUDENT)
					.set(STUDENT.TEMP_SCHED_ID, student.getTempSchedId())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getYearLevel() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.YEAR_LEVEL, student.getYearLevel())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getSem() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.SEM, student.getSem())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getAcademicYear() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.ACADEMIC_YEAR, student.getAcademicYear())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getFirstname() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.FIRSTNAME, student.getFirstname())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getMiddlename() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.MIDDLENAME, student.getMiddlename())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getLastname() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.LASTNAME, student.getLastname())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getSuffix() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.SUFFIX, student.getSuffix())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getGender() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.GENDER, student.getGender())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getCivilStatus() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.CIVIL_STATUS, student.getCivilStatus())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getBirthdate() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.BIRTHDATE, student.getBirthdate())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getBirthplace() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.BIRTHPLACE, student.getBirthplace())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getCitizenship() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.CITIZENSHIP, student.getCitizenship())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getReligion() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.RELIGION, student.getReligion())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getUnit() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.UNIT, student.getUnit())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getStreet() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.STREET, student.getStreet())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getSubdivision() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.SUBDIVISION, student.getSubdivision())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getBarangay() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.BARANGAY, student.getBarangay())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getCity() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.CITY, student.getCity())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getProvince() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.PROVINCE, student.getProvince())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getZipcode() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.ZIPCODE, student.getZipcode())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getTelephone() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.TELEPHONE, student.getTelephone())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getMobile() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.MOBILE, student.getMobile())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getEmail() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.EMAIL, student.getEmail())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				if (student.getActiveDeactive() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.ACTIVE_DEACTIVE, student.getActiveDeactive())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				return Response.builder()
						.status(200)
						.message("Student successfully updated")
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Student not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateStudent(Integer studentId, StudentRequest student) {
		Student _student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		Student email = dslContext.selectFrom(STUDENT)
				.where(STUDENT.EMAIL.eq(student.getEmail()))
				.fetchOneInto(Student.class);
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(student.getEmail())).fetchOneInto(Admin.class);
		Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(student.getEmail()))
				.fetchOneInto(Professor.class);
		Parent par = dslContext.selectFrom(PARENT).where(PARENT.EMAIL.eq(student.getEmail())).fetchOneInto(Parent.class);
		
		System.out.println(student.toString());
		
		if (_student != null) {
			if (email != null || admin != null || professor != null || par != null) {
				return Response.builder()
						.status(409)
						.message("Email already exists")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				if (student.getProgram() != null) {
					Program program = dslContext.selectFrom(PROGRAM)
							.where(PROGRAM.PROGRAM_ID.eq(student.getProgram().getProgramId()))
							.fetchOneInto(Program.class);
					
					dslContext.update(STUDENT)
					.set(STUDENT.PROGRAM_ID, program.getProgramId())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getSchedId() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.SCHED_ID, student.getSchedId())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getTempSchedId() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.TEMP_SCHED_ID, student.getTempSchedId())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getYearLevel() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.YEAR_LEVEL, student.getYearLevel())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getSem() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.SEM, student.getSem())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getAcademicYear() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.ACADEMIC_YEAR, student.getAcademicYear())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getFirstname() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.FIRSTNAME, student.getFirstname())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getMiddlename() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.MIDDLENAME, student.getMiddlename())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getLastname() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.LASTNAME, student.getLastname())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getSuffix() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.SUFFIX, student.getSuffix())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getGender() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.GENDER, student.getGender())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getCivilStatus() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.CIVIL_STATUS, student.getCivilStatus())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getBirthdate() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.BIRTHDATE, student.getBirthdate())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getBirthplace() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.BIRTHPLACE, student.getBirthplace())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getCitizenship() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.CITIZENSHIP, student.getCitizenship())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getReligion() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.RELIGION, student.getReligion())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getUnit() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.UNIT, student.getUnit())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getStreet() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.STREET, student.getStreet())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getSubdivision() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.SUBDIVISION, student.getSubdivision())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getBarangay() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.BARANGAY, student.getBarangay())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getCity() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.CITY, student.getCity())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getProvince() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.PROVINCE, student.getProvince())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getZipcode() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.ZIPCODE, student.getZipcode())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getTelephone() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.TELEPHONE, student.getTelephone())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getMobile() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.MOBILE, student.getMobile())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getEmail() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.EMAIL, student.getEmail())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getAppId() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.APP_ID, student.getAppId())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}
				
				if (student.getActiveDeactive() != null) {
					dslContext.update(STUDENT)
					.set(STUDENT.ACTIVE_DEACTIVE, student.getActiveDeactive())
					.where(STUDENT.STUDENT_ID.eq(studentId))
					.execute();
				}	
				
				return Response.builder()
						.status(200)
						.message("Student successfully updated")
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Student not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response deleteStudent(Integer studentId) {
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		if(student != null) {
			dslContext.delete(STUDENT)
			.where(STUDENT.STUDENT_ID.eq(studentId))
			.execute();
			
			return Response.builder()
					.status(201)
					.message("Student successfully deleted")
					.timestamp(LocalDateTime.now())
					.build();
		}else {
			return Response.builder()
					.status(404)
					.message("Student not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
    
    private String createImageLink(String filename) {
		return ServletUriComponentsBuilder.fromCurrentRequest()
				.replacePath("/lms/image/" + filename)
				.toUriString();
    }
    
    private String generateUserId() {
    	LocalDate now = LocalDate.now();
		Random random = new Random();
	    int randomNumber = random.nextInt(90000) + 10000;
		String userId = String.valueOf(now.getYear()) + String.valueOf(randomNumber);
		
		while (true) {
			Student _student = dslContext.selectFrom(ACCOUNT)
					.where(ACCOUNT.USERNAME.eq(userId))
					.fetchOneInto(Student.class);
			if (_student != null) {
			    randomNumber = random.nextInt(90000) + 10000;
				userId = String.valueOf(now.getYear()) + String.valueOf(randomNumber);
			} else {
				break;
			}
		}
		return userId;
    }
}
