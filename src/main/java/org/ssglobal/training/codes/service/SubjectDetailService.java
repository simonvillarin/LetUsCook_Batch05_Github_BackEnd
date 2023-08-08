package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.SubjectDetailDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Schedule;
import org.ssglobal.training.codes.tables.pojos.Student;
import org.ssglobal.training.codes.tables.pojos.Subject;
import org.ssglobal.training.codes.tables.pojos.SubjectDetailHistory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectDetailService {

	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.SubjectDetailHistory SUBJECTDETAIL = org.ssglobal.training.codes.tables.SubjectDetailHistory.SUBJECT_DETAIL_HISTORY;
	private final org.ssglobal.training.codes.tables.Schedule SCHEDULE =  org.ssglobal.training.codes.tables.Schedule.SCHEDULE;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	
	public List<SubjectDetailDTO> getAllSubjectHistory(){
		List<SubjectDetailHistory> subject = dslContext.selectFrom(SUBJECTDETAIL).fetchInto(SubjectDetailHistory.class);
		List<SubjectDetailDTO> subjDTOs = new ArrayList<>();
		
		for(int i = 0; i < subject.size(); i++) {
			
			Schedule schedule = dslContext.selectFrom(SCHEDULE)
					.where(SCHEDULE.SCHED_ID.eq(subject.get(i).getSubjectId()))
					.fetchOneInto(Schedule.class);
			Professor prof = dslContext.selectFrom(PROFESSOR)
					.where(PROFESSOR.PROFESSOR_ID.eq(subject.get(i).getProfessorId()))
					.fetchOneInto(Professor.class);
			Subject subj = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(subject.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(subject.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			
			SubjectDetailDTO subjectDetailDTO = SubjectDetailDTO.builder()
					.historyId(subject.get(i).getHistoryId())
					.student(student)
					.professor(prof)
					.subject(subj)
					.sem(subject.get(i).getSem())
					.yearLevel(subject.get(i).getYearLevel())
					.academicYear(subject.get(i).getAcademicYear())
					.section(subject.get(i).getSection())
					.sched(schedule)
					.activeDeactive(subject.get(i).getActiveDeactive())
					.build();
			subjDTOs.add(subjectDetailDTO);
		}
		return subjDTOs;
	}
	
	public SubjectDetailDTO getSubjectHistoryById(Integer historyId) {
		SubjectDetailHistory subject = dslContext.selectFrom(SUBJECTDETAIL)
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.fetchOneInto(SubjectDetailHistory.class);
		if (subject != null) {
			Schedule schedule = dslContext.selectFrom(SCHEDULE)
					.where(SCHEDULE.SCHED_ID.eq(subject.getSubjectId()))
					.fetchOneInto(Schedule.class);
			Professor prof = dslContext.selectFrom(PROFESSOR)
					.where(PROFESSOR.PROFESSOR_ID.eq(subject.getProfessorId()))
					.fetchOneInto(Professor.class);
			Subject subj = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(subject.getSubjectId()))
					.fetchOneInto(Subject.class);
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(subject.getStudentId()))
					.fetchOneInto(Student.class);
			
			return SubjectDetailDTO.builder()
					.historyId(subject.getHistoryId())
					.student(student)
					.professor(prof)
					.subject(subj)
					.sem(subject.getSem())
					.yearLevel(subject.getYearLevel())
					.academicYear(subject.getAcademicYear())
					.section(subject.getSection())
					.sched(schedule)
					.activeDeactive(subject.getActiveDeactive())
					.build();
			
		} else {
			throw new RuntimeException("Subject not found");
		}
	}
	
	public Response addSubject(SubjectDetailHistory subject) {
		dslContext.insertInto(SUBJECTDETAIL)
		.set(SUBJECTDETAIL.STUDENT_ID, subject.getStudentId())
		.set(SUBJECTDETAIL.PROFESSOR_ID, subject.getProfessorId())
		.set(SUBJECTDETAIL.SUBJECT_ID, subject.getSubjectId())
		.set(SUBJECTDETAIL.SEM, subject.getSem())
		.set(SUBJECTDETAIL.YEAR_LEVEL, subject.getYearLevel())
		.set(SUBJECTDETAIL.ACADEMIC_YEAR, subject.getAcademicYear())
		.set(SUBJECTDETAIL.SECTION, subject.getSection())
		.set(SUBJECTDETAIL.SCHED_ID, subject.getSchedId())
		.set(SUBJECTDETAIL.ACTIVE_DEACTIVE, subject.getActiveDeactive())
		.execute();
		return Response.builder()
				.status(201)
				.message("Subject detail history successfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateSubject(Integer historyId, SubjectDetailHistory subject) {
		SubjectDetailHistory _subject = dslContext.selectFrom(SUBJECTDETAIL)
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.fetchOneInto(SubjectDetailHistory.class);
		if (_subject != null) {
			if(subject.getStudentId() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.STUDENT_ID, subject.getStudentId())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			if(subject.getProfessorId() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.PROFESSOR_ID, subject.getProfessorId())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			if(subject.getSubjectId() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.SUBJECT_ID, subject.getSubjectId())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			if(subject.getSem() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.SEM, subject.getSem())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			if(subject.getYearLevel() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.YEAR_LEVEL, subject.getYearLevel())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			if(subject.getAcademicYear() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.ACADEMIC_YEAR, subject.getAcademicYear())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			if(subject.getSection() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.SECTION, subject.getSection())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			if(subject.getSchedId() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.SCHED_ID, subject.getSchedId())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			if(subject.getActiveDeactive() != null) {
				dslContext.update(SUBJECTDETAIL)
				.set(SUBJECTDETAIL.ACTIVE_DEACTIVE, subject.getActiveDeactive())
				.where(SUBJECTDETAIL.HISTORY_ID.eq(historyId))
				.execute();
			}
			return Response.builder()
					.status(200)
					.message("Subject detail history successfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Subject history not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
		
		
	}
}