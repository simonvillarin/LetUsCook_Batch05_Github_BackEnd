package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.CurriculumDTO;
import org.ssglobal.training.codes.request.CurriculumRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Curriculum;
import org.ssglobal.training.codes.tables.pojos.Program;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurriculumService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Curriculum CURRICULUM = org.ssglobal.training.codes.tables.Curriculum.CURRICULUM;
	private final org.ssglobal.training.codes.tables.Program PROGRAM = org.ssglobal.training.codes.tables.Program.PROGRAM;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	
	public List<CurriculumDTO> getAllCurriculum() {
		List<Curriculum> curriculums = dslContext.selectFrom(CURRICULUM).fetchInto(Curriculum.class);
		List<CurriculumDTO> curriculumDTOs = new ArrayList<>();
		
		for (int i = 0; i < curriculums.size(); i++) {
			Program program = dslContext.selectFrom(PROGRAM).where(PROGRAM.PROGRAM_ID.eq(curriculums.get(i).getProgramId())).fetchOneInto(Program.class);
			Subject subject = dslContext.selectFrom(SUBJECT).where(SUBJECT.SUBJECT_ID.eq(curriculums.get(i).getSubjectId())).fetchOneInto(Subject.class);
			
			CurriculumDTO curriculumDTO = CurriculumDTO.builder()
					.curriculumId(curriculums.get(i).getCurriculumId())
					.program(program.getProgramTitle())
					.subject(subject.getSubjectTitle())
					.sem(curriculums.get(i).getSem())
					.yearLevel(curriculums.get(i).getYearLevel())
					.activeDeactive(curriculums.get(i).getActiveDeactive())
					.build();
			curriculumDTOs.add(curriculumDTO);
		}
		return curriculumDTOs;
	}
	
	public CurriculumDTO getCurriculumById(Integer curriculumId) {
		Curriculum curriculum = dslContext.selectFrom(CURRICULUM)
				.where(CURRICULUM.CURRICULUM_ID.eq(curriculumId))
				.fetchOneInto(Curriculum.class);
		if (curriculum != null) {
			Program program = dslContext.selectFrom(PROGRAM).where(PROGRAM.PROGRAM_ID.eq(curriculum.getProgramId())).fetchOneInto(Program.class);
			Subject subject = dslContext.selectFrom(SUBJECT).where(SUBJECT.SUBJECT_ID.eq(curriculum.getSubjectId())).fetchOneInto(Subject.class);
			return CurriculumDTO.builder()
					.curriculumId(curriculum.getCurriculumId())
					.program(program.getProgramTitle())
					.subject(subject.getSubjectTitle())
					.sem(curriculum.getSem())
					.yearLevel(curriculum.getYearLevel())
					.activeDeactive(curriculum.getActiveDeactive())
					.build();
		} else {
			throw new RuntimeException("Curriculum not found");
		}
	}
	
	public Response addCurriculums(List<CurriculumRequest> curriculums) {
		for (int i = 0; i < curriculums.size(); i++) {
			Program program = dslContext.selectFrom(PROGRAM)
					.where(PROGRAM.PROGRAM_TITLE.eq(curriculums.get(i).getProgram()))
					.fetchOneInto(Program.class);
			Subject subject = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_TITLE.eq(curriculums.get(i).getSubject()))
					.fetchOneInto(Subject.class);
			
			dslContext.insertInto(CURRICULUM)
			.set(CURRICULUM.PROGRAM_ID, program.getProgramId())
			.set(CURRICULUM.SUBJECT_ID, subject.getSubjectId())
			.set(CURRICULUM.SEM, curriculums.get(i).getSem())
			.set(CURRICULUM.YEAR_LEVEL, curriculums.get(i).getYearLevel())
			.set(CURRICULUM.ACTIVE_DEACTIVE, true)
			.execute();
		}
		return Response.builder()
				.status(201)
				.message("Curriculum sucessfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response addCurriculum(CurriculumRequest curriculum) {
		Program program = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_TITLE.eq(curriculum.getProgram()))
				.fetchOneInto(Program.class);
		Subject subject = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_TITLE.eq(curriculum.getSubject()))
				.fetchOneInto(Subject.class);
		
		dslContext.insertInto(CURRICULUM)
		.set(CURRICULUM.PROGRAM_ID, program.getProgramId())
		.set(CURRICULUM.SUBJECT_ID, subject.getSubjectId())
		.set(CURRICULUM.SEM, curriculum.getSem())
		.set(CURRICULUM.YEAR_LEVEL, curriculum.getYearLevel())
		.set(CURRICULUM.ACTIVE_DEACTIVE, true)
		.execute();
		
		return Response.builder()
				.status(201)
				.message("Curriculum sucessfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateCurriculum(Integer curriculumId, CurriculumRequest curriculum) {
		Curriculum _curriculum = dslContext.selectFrom(CURRICULUM)
				.where(CURRICULUM.CURRICULUM_ID.eq(curriculumId))
				.fetchOneInto(Curriculum.class);
		Program program = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_TITLE.eq(curriculum.getProgram()))
				.fetchOneInto(Program.class);
		Subject subject = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_TITLE.eq(curriculum.getSubject()))
				.fetchOneInto(Subject.class);
		
		if (_curriculum != null) {
			if (curriculum.getProgram() != null) {
				dslContext.update(CURRICULUM)
				.set(CURRICULUM.PROGRAM_ID, program.getProgramId())
				.where(CURRICULUM.CURRICULUM_ID.eq(curriculumId))
				.execute();
			}
			if (curriculum.getSubject() != null ) {
				dslContext.update(CURRICULUM)
				.set(CURRICULUM.SUBJECT_ID, subject.getSubjectId())
				.where(CURRICULUM.SUBJECT_ID.eq(curriculumId))
				.execute();
			}
			if (curriculum.getSem() != null) {
				dslContext.update(CURRICULUM)
				.set(CURRICULUM.SEM, curriculum.getSem())
				.where(CURRICULUM.CURRICULUM_ID.eq(curriculumId))
				.execute();
			}
			if (curriculum.getYearLevel() != null) {
				dslContext.update(CURRICULUM)
				.set(CURRICULUM.YEAR_LEVEL, curriculum.getYearLevel())
				.where(CURRICULUM.CURRICULUM_ID.eq(curriculumId))
				.execute();
			}
			if (curriculum.getActiveDeactive() != null) {
				dslContext.update(CURRICULUM)
				.set(CURRICULUM.ACTIVE_DEACTIVE, curriculum.getActiveDeactive())
				.where(CURRICULUM.CURRICULUM_ID.eq(curriculumId))
				.execute();
			}
			return Response.builder()
					.status(201)
					.message("Curriculum sucessfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Curriculum not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}