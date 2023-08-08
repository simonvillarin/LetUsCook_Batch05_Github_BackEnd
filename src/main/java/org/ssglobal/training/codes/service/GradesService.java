package org.ssglobal.training.codes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.GradesDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Grades;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Schedule;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Student;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradesService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Grades GRADES = org.ssglobal.training.codes.tables.Grades.GRADES;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	private final org.ssglobal.training.codes.tables.Schedule SCHEDULE = org.ssglobal.training.codes.tables.Schedule.SCHEDULE;
	private final org.ssglobal.training.codes.tables.Section SECTION = org.ssglobal.training.codes.tables.Section.SECTION;
	
	public List<GradesDTO> getAllGrades() {
		List<Grades> grades = dslContext.selectFrom(GRADES)
				.fetchInto(Grades.class);
		
		List<GradesDTO> gradesDTOs = new ArrayList<>();
		for (int i = 0; i < grades.size(); i++) {
			Professor professor = dslContext.selectFrom(PROFESSOR)
					.where(PROFESSOR.PROFESSOR_ID.eq(grades.get(i).getProfessorId()))
					.fetchOneInto(Professor.class);
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(grades.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(grades.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			
			GradesDTO gradesDTO = GradesDTO.builder()
					.gradeId(grades.get(i).getGradeId())
					.professor(professor)
					.student(student)
					.subject(subject)
					.prelim(grades.get(i).getPrelim())
					.midterm(grades.get(i).getMidterm())
					.finals(grades.get(i).getFinals())
					.yearLevel(grades.get(i).getYearLevel())
					.sem(grades.get(i).getSem())
					.academicYear(grades.get(i).getAcademicYear())
					.comment(grades.get(i).getComment())
					.remarks(grades.get(i).getRemarks())
					.dateModified(grades.get(i).getDateModified())
					.status(grades.get(i).getStatus())
					.build();
			gradesDTOs.add(gradesDTO);
		}
		return gradesDTOs;
	}
	
	public List<GradesDTO> getGradeById(Integer studentId) {
		List<Grades> grades = dslContext.selectFrom(GRADES)
				.where(GRADES.STUDENT_ID.eq(studentId))
				.fetchInto(Grades.class);
		
		List<GradesDTO> gradesDTOs = new ArrayList<>();
		for (int i = 0; i < grades.size(); i++) {
			if (grades.get(i).getStudentId().equals(studentId)) {
				Professor professor = dslContext.selectFrom(PROFESSOR)
						.where(PROFESSOR.PROFESSOR_ID.eq(grades.get(i).getProfessorId()))
						.fetchOneInto(Professor.class);
				Student student = dslContext.selectFrom(STUDENT)
						.where(STUDENT.STUDENT_ID.eq(grades.get(i).getStudentId()))
						.fetchOneInto(Student.class);
				Subject subject = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(grades.get(i).getSubjectId()))
						.fetchOneInto(Subject.class);
				
				GradesDTO gradesDTO = GradesDTO.builder()
						.gradeId(grades.get(i).getGradeId())
						.professor(professor)
						.student(student)
						.subject(subject)
						.prelim(grades.get(i).getPrelim())
						.midterm(grades.get(i).getMidterm())
						.finals(grades.get(i).getFinals())
						.yearLevel(grades.get(i).getYearLevel())
						.sem(grades.get(i).getSem())
						.academicYear(grades.get(i).getAcademicYear())
						.comment(grades.get(i).getComment())
						.remarks(grades.get(i).getRemarks())
						.dateModified(grades.get(i).getDateModified())
						.status(grades.get(i).getStatus())
						.build();
				gradesDTOs.add(gradesDTO);
			}
		}
		return gradesDTOs;
	}
	
	public HashSet<GradesDTO> getGradesBySection(Integer sectionId, Integer subjectId) {
		List<Grades> grades = dslContext.selectFrom(GRADES)
				.fetchInto(Grades.class);
		Section section = dslContext.selectFrom(SECTION)
				.where(SECTION.SECTION_ID.eq(sectionId))
				.fetchOneInto(Section.class);
		Subject sub = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_ID.eq(subjectId))
				.fetchOneInto(Subject.class);
		List<GradesDTO> gradesDTOs = new ArrayList<>();
		HashSet<GradesDTO> grade = new HashSet<>();
		for (int i = 0; i < grades.size(); i++) {
			Professor professor = dslContext.selectFrom(PROFESSOR)
					.where(PROFESSOR.PROFESSOR_ID.eq(grades.get(i).getProfessorId()))
					.fetchOneInto(Professor.class);
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(grades.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(grades.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			for (int j = 0; j < student.getSchedId().length; j++) {
				Schedule schedule = dslContext.selectFrom(SCHEDULE)
						.where(SCHEDULE.SCHED_ID.eq(student.getSchedId()[j]))
						.fetchOneInto(Schedule.class);
			
				if (schedule.getSection().equalsIgnoreCase(section.getSection()) && sub.getSubjectId() == schedule.getSubjectId() && student.getAcademicYear().equals(student.getAcademicYear()) && student.getSem().equals(student.getSem())) {
					GradesDTO gradesDTO = GradesDTO.builder()
							.gradeId(grades.get(i).getGradeId())
							.professor(professor)
							.student(student)
							.subject(subject)
							.prelim(grades.get(i).getPrelim())
							.midterm(grades.get(i).getMidterm())
							.finals(grades.get(i).getFinals())
							.yearLevel(grades.get(i).getYearLevel())
							.sem(grades.get(i).getSem())
							.academicYear(grades.get(i).getAcademicYear())
							.comment(grades.get(i).getComment())
							.remarks(grades.get(i).getRemarks())
							.dateModified(grades.get(i).getDateModified())
							.status(grades.get(i).getStatus())
							.build();
					gradesDTOs.add(gradesDTO);
				}
			}
			
		}
		List<GradesDTO> temp = new ArrayList<>();
		for (int i = 0; i < gradesDTOs.size(); i++) {
			if (gradesDTOs.get(i).getSubject().getSubjectId() == subjectId) {
				temp.add(gradesDTOs.get(i));
			}
		}
		for (int i = 0; i < temp.size(); i++) {
			grade.add(temp.get(i));
		}
		return grade;
	}
	
	public HashSet<GradesDTO> getGradesBySection(Integer sectionId) {
		List<Grades> grades = dslContext.selectFrom(GRADES)
				.fetchInto(Grades.class);
		Section section = dslContext.selectFrom(SECTION)
				.where(SECTION.SECTION_ID.eq(sectionId))
				.fetchOneInto(Section.class);
		List<GradesDTO> gradesDTOs = new ArrayList<>();
		HashSet<GradesDTO> grade = new HashSet<>();
		for (int i = 0; i < grades.size(); i++) {
			Professor professor = dslContext.selectFrom(PROFESSOR)
					.where(PROFESSOR.PROFESSOR_ID.eq(grades.get(i).getProfessorId()))
					.fetchOneInto(Professor.class);
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(grades.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(grades.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			for (int j = 0; j < student.getSchedId().length; j++) {
				Schedule schedule = dslContext.selectFrom(SCHEDULE)
						.where(SCHEDULE.SCHED_ID.eq(student.getSchedId()[j]))
						.fetchOneInto(Schedule.class);
			
				if (schedule.getSection().equalsIgnoreCase(section.getSection()) && student.getAcademicYear().equals(student.getAcademicYear()) && student.getSem().equals(student.getSem())) {
					GradesDTO gradesDTO = GradesDTO.builder()
							.gradeId(grades.get(i).getGradeId())
							.professor(professor)
							.student(student)
							.subject(subject)
							.prelim(grades.get(i).getPrelim())
							.midterm(grades.get(i).getMidterm())
							.finals(grades.get(i).getFinals())
							.yearLevel(grades.get(i).getYearLevel())
							.sem(grades.get(i).getSem())
							.academicYear(grades.get(i).getAcademicYear())
							.comment(grades.get(i).getComment())
							.remarks(grades.get(i).getRemarks())
							.dateModified(grades.get(i).getDateModified())
							.status(grades.get(i).getStatus())
							.build();
					gradesDTOs.add(gradesDTO);
				}
			}
			
		}
		for (int i = 0; i < gradesDTOs.size(); i++) {
			grade.add(gradesDTOs.get(i));
		}
		return grade;
	}
	
	public Response addGrade(Grades grade) {
		dslContext.insertInto(GRADES)
		.set(GRADES.PROFESSOR_ID, grade.getProfessorId())
		.set(GRADES.STUDENT_ID, grade.getStudentId())
		.set(GRADES.SUBJECT_ID, grade.getSubjectId())
		.set(GRADES.PRELIM, grade.getPrelim())
		.set(GRADES.MIDTERM, grade.getMidterm())
		.set(GRADES.FINALS, grade.getFinals())
		.set(GRADES.YEAR_LEVEL, grade.getYearLevel())
		.set(GRADES.SEM, grade.getSem())
		.set(GRADES.ACADEMIC_YEAR, grade.getAcademicYear())
		.set(GRADES.COMMENT, grade.getComment())
		.set(GRADES.REMARKS, grade.getRemarks())
		.set(GRADES.DATE_MODIFIED, grade.getDateModified())
		.set(GRADES.STATUS, true)
		.execute();
		
		return Response.builder()
				.status(201)
				.message("Grade successfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateGrade(Integer gradeId, Grades grade) {
		Grades _grade = dslContext.selectFrom(GRADES)
				.where(GRADES.GRADE_ID.eq(gradeId))
				.fetchOneInto(Grades.class);
		
		if (_grade != null) {
			dslContext.update(GRADES)
			.set(GRADES.DATE_MODIFIED, LocalDate.now())
			.where(GRADES.GRADE_ID.eq(gradeId))
			.execute();
			
			dslContext.update(GRADES)
			.set(GRADES.PRELIM, grade.getPrelim())
			.set(GRADES.MIDTERM, grade.getMidterm())
			.set(GRADES.FINALS, grade.getFinals())
			.set(GRADES.REMARKS, grade.getRemarks())
			.where(GRADES.GRADE_ID.eq(gradeId))
			.execute();
			
			if (grade.getProfessorId() != null) {
				dslContext.update(GRADES)
				.set(GRADES.PROFESSOR_ID, grade.getProfessorId())
				.where(GRADES.GRADE_ID.eq(gradeId))
				.execute();
			}
			if (grade.getStudentId() != null) {
				dslContext.update(GRADES)
				.set(GRADES.STUDENT_ID, grade.getStudentId())
				.where(GRADES.GRADE_ID.eq(gradeId))
				.execute();
			}
			if (grade.getSubjectId() != null) {
				dslContext.update(GRADES)
				.set(GRADES.SUBJECT_ID, grade.getSubjectId())
				.where(GRADES.GRADE_ID.eq(gradeId))
				.execute();
			}
			if (grade.getYearLevel() != null) {
				dslContext.update(GRADES)
				.set(GRADES.YEAR_LEVEL, grade.getYearLevel())
				.where(GRADES.GRADE_ID.eq(gradeId))
				.execute();
			}
			if (grade.getSem() != null) {
				dslContext.update(GRADES)
				.set(GRADES.SEM, grade.getSem())
				.where(GRADES.GRADE_ID.eq(gradeId))
				.execute();
			}
			if (grade.getAcademicYear() != null) {
				dslContext.update(GRADES)
				.set(GRADES.ACADEMIC_YEAR, grade.getAcademicYear())
				.where(GRADES.GRADE_ID.eq(gradeId))
				.execute();
			}
			if (grade.getComment() != null) {
				dslContext.update(GRADES)
				.set(GRADES.COMMENT, grade.getComment())
				.where(GRADES.GRADE_ID.eq(gradeId))
				.execute();
			}
			if (grade.getStatus() != null) {
				dslContext.update(GRADES)
				.set(GRADES.STATUS, grade.getStatus())
				.where(GRADES.GRADE_ID.eq(gradeId))
				.execute();
			}		
			return Response.builder()
					.status(200)
					.message("Grade successfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Grade not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}	
