package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.EvaluationDTO;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Evaluation;
import org.ssglobal.training.codes.tables.pojos.Student;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Evaluation EVALUATION = org.ssglobal.training.codes.tables.Evaluation.EVALUATION;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	
	public List<EvaluationDTO> getEvaluationBySubjectId(Integer subjectId) {
		List<Evaluation> evals = dslContext.selectFrom(EVALUATION)
				.where(EVALUATION.SUBJECT_ID.eq(subjectId))
				.fetchInto(Evaluation.class);
		List<EvaluationDTO> evalDTOs = new ArrayList<>();
		for (int i = 0; i < evals.size(); i++) {
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(evals.get(i).getSubjectId()))
					.fetchOneInto(Subject.class);
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(evals.get(i).getStudentId()))
					.fetchOneInto(Student.class);
			EvaluationDTO eval = EvaluationDTO.builder()
					.evaluationId(evals.get(i).getEvalId())
					.subject(subject)
					.student(student)
					.answers(evals.get(i).getAnswers())
					.build();
			evalDTOs.add(eval);
		}
		return evalDTOs;
	}
	
	public EvaluationDTO getEvaluationBySubjectId(Integer subjectId, Integer studentId) {
		Evaluation evals = dslContext.selectFrom(EVALUATION)
				.where(EVALUATION.SUBJECT_ID.eq(subjectId).and(EVALUATION.STUDENT_ID.eq(studentId)))
				.fetchOneInto(Evaluation.class);
		if (evals != null) {
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_ID.eq(evals.getSubjectId()))
					.fetchOneInto(Subject.class);
			Student student = dslContext.selectFrom(STUDENT)
					.where(STUDENT.STUDENT_ID.eq(evals.getStudentId()))
					.fetchOneInto(Student.class);
			return EvaluationDTO.builder()
					.evaluationId(evals.getEvalId())
					.subject(subject)
					.student(student)
					.answers(evals.getAnswers())
					.build();
		} else {
			String[] arr = new String[1];
			return EvaluationDTO.builder()
					.evaluationId(0)
					.subject(new Subject())
					.student(new Student())
					.answers(arr)
					.build();
		}
	}
	
	public Response addEvaluation(Evaluation evaluation) {
		dslContext.insertInto(EVALUATION)
		.set(EVALUATION.SUBJECT_ID, evaluation.getSubjectId())
		.set(EVALUATION.STUDENT_ID, evaluation.getStudentId())
		.set(EVALUATION.ANSWERS, evaluation.getAnswers())
		.execute();
		
		return Response.builder()
				.status(201)
				.message("Evaluation sucessfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateEvaluation(Integer evaluationId, Evaluation evaluation) {
		Evaluation _evaluation = dslContext.selectFrom(EVALUATION)
				.where(EVALUATION.EVAL_ID.eq(evaluationId))
				.fetchOneInto(Evaluation.class);
		
		if (_evaluation != null) {
			if (evaluation.getStudentId() != null ) {
				dslContext.update(EVALUATION)
				.set(EVALUATION.STUDENT_ID, evaluation.getStudentId())
				.where(EVALUATION.EVAL_ID.eq(evaluationId))
				.execute();
			}
			if (evaluation.getAnswers() != null) {
				dslContext.update(EVALUATION)
				.set(EVALUATION.ANSWERS, evaluation.getAnswers())
				.where(EVALUATION.EVAL_ID.eq(evaluationId))
				.execute();
			}
			return Response.builder()
					.status(201)
					.message("Evaluation sucessfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Evaluation not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}