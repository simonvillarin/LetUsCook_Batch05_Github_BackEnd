package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.request.EvalRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Eval;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvalService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Eval EVAL = org.ssglobal.training.codes.tables.Eval.EVAL;
	private final org.ssglobal.training.codes.tables.Section SECTION = org.ssglobal.training.codes.tables.Section.SECTION;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	
	public Eval getEval(Integer subjectId, Integer sectionId ) {
		Eval _eval = dslContext.selectFrom(EVAL)
				.where(EVAL.SUBJECT_ID.eq(subjectId)).and(EVAL.SECTION_ID.eq(sectionId))
				.fetchOneInto(Eval.class);
		if (_eval != null) {
			return _eval;
		} else {
			return null;
		}
	}
	
	public List<Eval> getEvaluationBySubjectId(Integer subjectId){
		return  dslContext.selectFrom(EVAL)
				.where(EVAL.SUBJECT_ID.eq(subjectId))
				.fetchInto(Eval.class);
	}
	
	public Response addEvaluation(EvalRequest evaluation) {
		Section section = dslContext.selectFrom(SECTION)
		.where(SECTION.SECTION_.eq(evaluation.getSection()))
		.fetchOneInto(Section.class);
		Subject subject = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_ID.eq(evaluation.getSubjectId()))
				.fetchOneInto(Subject.class);
		Eval eval = dslContext.selectFrom(EVAL)
				.where(EVAL.SECTION_ID.eq(section.getSectionId()))
						.and(EVAL.SUBJECT_ID.eq(subject.getSubjectId()))
						.fetchOneInto(Eval.class);
		if (eval == null) {
			dslContext.insertInto(EVAL)
			.set(EVAL.SECTION_ID, section.getSectionId())
			.set(EVAL.SUBJECT_ID, subject.getSubjectId())
			.set(EVAL.ACTIVE_DEACTIVE, false)
			.execute();
		}
		
		return Response.builder()
				.status(201)
				.message("Evaluation sucessfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateEvaluation(Integer evalId, EvalRequest eval) {
		Eval _eval = dslContext.selectFrom(EVAL)
				.where(EVAL.EVAL_ID.eq(evalId))
				.fetchOneInto(Eval.class);
		
		if (_eval != null) {
			if (eval.getActiveDeactive() != null) {
				dslContext.update(EVAL)
				.set(EVAL.ACTIVE_DEACTIVE, eval.getActiveDeactive())
				.where(EVAL.EVAL_ID.eq(_eval.getEvalId()))
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
