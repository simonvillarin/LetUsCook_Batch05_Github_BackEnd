package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.ProgramDTO;
import org.ssglobal.training.codes.dto.SubjectDTO;
import org.ssglobal.training.codes.request.ProgramRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Program;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProgramService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Program PROGRAM = org.ssglobal.training.codes.tables.Program.PROGRAM;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	
	public List<ProgramDTO> getAllProgram() {
		List<Program> programs = dslContext.selectFrom(PROGRAM).fetchInto(Program.class);
		
		List<ProgramDTO> programDTOs = new ArrayList<>();
		for (int i = 0; i < programs.size(); i++) {
			List<SubjectDTO> majors = new ArrayList<>();
			if (programs.get(i).getMajors() != null) {
				for (int j = 0; j < programs.get(i).getMajors().length; j++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(programs.get(i).getMajors()[j]))
							.fetchOneInto(Subject.class);
					List<Subject> subs = new ArrayList<>();
					if (subject.getPreRequisites() != null) {
						for (int k = 0; k < subject.getPreRequisites().length; k++) {
							Subject sub = dslContext.selectFrom(SUBJECT)
									.where(SUBJECT.SUBJECT_ID.eq(subject.getPreRequisites()[k]))
									.fetchOneInto(Subject.class);
							subs.add(sub);
						}
					}
					SubjectDTO subjectDTO = SubjectDTO.builder()
							.subjectId(subject.getSubjectId())
							.subjectCode(subject.getSubjectCode())
							.subjectTitle(subject.getSubjectTitle())
							.units(subject.getUnits())
							.preRequisites(subs)
							.type(subject.getType())
							.activeDeactive(subject.getActiveDeactive())
							.build();
					majors.add(subjectDTO);
				}
			}
			
			List<SubjectDTO> minors = new ArrayList<>();
			if (programs.get(i).getMinors() != null) {			
				for (int j = 0; j < programs.get(i).getMinors().length; j++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(programs.get(i).getMinors()[j]))
							.fetchOneInto(Subject.class);
					List<Subject> subs = new ArrayList<>();
					if (subject.getPreRequisites() != null) {
						for (int k = 0; k < subject.getPreRequisites().length; k++) {
							Subject sub = dslContext.selectFrom(SUBJECT)
									.where(SUBJECT.SUBJECT_ID.eq(subject.getPreRequisites()[k]))
									.fetchOneInto(Subject.class);
							subs.add(sub);
						}
					}
					SubjectDTO subjectDTO = SubjectDTO.builder()
							.subjectId(subject.getSubjectId())
							.subjectCode(subject.getSubjectCode())
							.subjectTitle(subject.getSubjectTitle())
							.units(subject.getUnits())
							.preRequisites(subs)
							.type(subject.getType())
							.activeDeactive(subject.getActiveDeactive())
							.build();
					minors.add(subjectDTO);
				}
			}
			
			List<SubjectDTO> electives = new ArrayList<>();
			if (programs.get(i).getElectives() != null) {
				for (int j = 0; j < programs.get(i).getElectives().length; j++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(programs.get(i).getElectives()[j]))
							.fetchOneInto(Subject.class);
					List<Subject> subs = new ArrayList<>();
					if (subject.getPreRequisites() != null) {
						for (int k = 0; k < subject.getPreRequisites().length; k++) {
							Subject sub = dslContext.selectFrom(SUBJECT)
									.where(SUBJECT.SUBJECT_ID.eq(subject.getPreRequisites()[k]))
									.fetchOneInto(Subject.class);
							subs.add(sub);
						}
					}
					SubjectDTO subjectDTO = SubjectDTO.builder()
							.subjectId(subject.getSubjectId())
							.subjectCode(subject.getSubjectCode())
							.subjectTitle(subject.getSubjectTitle())
							.units(subject.getUnits())
							.preRequisites(subs)
							.type(subject.getType())
							.activeDeactive(subject.getActiveDeactive())
							.build();
					electives.add(subjectDTO);
				}
			}
			
			ProgramDTO programDTO = ProgramDTO.builder()
					.programId(programs.get(i).getProgramId())
					.programCode(programs.get(i).getProgramCode())
					.programTitle(programs.get(i).getProgramTitle())
					.firstYearFirstSemMin(programs.get(i).getFirstYearFirstSemMin())
					.firstYearFirstSemMax(programs.get(i).getFirstYearFirstSemMax())
					.firstYearSecondSemMin(programs.get(i).getFirstYearSecondSemMin())
					.firstYearSecondSemMax(programs.get(i).getFirstYearSecondSemMax())
					.secondYearFirstSemMin(programs.get(i).getSecondYearFirstSemMin())
					.secondYearFirstSemMax(programs.get(i).getSecondYearFirstSemMax())
					.secondYearSecondSemMin(programs.get(i).getSecondYearSecondSemMin())
					.secondYearSecondSemMax(programs.get(i).getSecondYearSecondSemMax())
					.thirdYearFirstSemMin(programs.get(i).getThirdYearFirstSemMin())
					.thirdYearFirstSemMax(programs.get(i).getThirdYearFirstSemMax())
					.thirdYearSecondSemMin(programs.get(i).getThirdYearSecondSemMin())
					.thirdYearSecondSemMax(programs.get(i).getThirdYearSecondSemMax())
					.fourthYearFirstSemMin(programs.get(i).getFourthYearFirstSemMin())
					.fourthYearFirstSemMax(programs.get(i).getFourthYearFirstSemMax())
					.fourthYearSecondSemMin(programs.get(i).getFourthYearSecondSemMin())
					.fourthYearSecondSemMax(programs.get(i).getFourthYearSecondSemMax())
					.majors(majors)
					.minors(minors)
					.electives(electives)
					.status(programs.get(i).getStatus())
					.build();
			programDTOs.add(programDTO);
		}
		return programDTOs;
	}
	
	public ProgramDTO getProgramById(Integer programId) {
		Program program = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_ID.eq(programId))
				.fetchOneInto(Program.class);	
		if (program != null) {
			List<SubjectDTO> majors = new ArrayList<>();
			for (int j = 0; j < program.getMajors().length; j++) {
				Subject subject = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(program.getMajors()[j]))
						.fetchOneInto(Subject.class);
				List<Subject> subs = new ArrayList<>();
				if (subject.getPreRequisites() != null) {
					for (int i = 0; i < subject.getPreRequisites().length; i++) {
						Subject sub = dslContext.selectFrom(SUBJECT)
								.where(SUBJECT.SUBJECT_ID.eq(subject.getPreRequisites()[i]))
								.fetchOneInto(Subject.class);
						subs.add(sub);
					}
				}
				SubjectDTO subjectDTO = SubjectDTO.builder()
						.subjectId(subject.getSubjectId())
						.subjectCode(subject.getSubjectCode())
						.subjectTitle(subject.getSubjectTitle())
						.units(subject.getUnits())
						.preRequisites(subs)
						.type(subject.getType())
						.activeDeactive(subject.getActiveDeactive())
						.build();
				majors.add(subjectDTO);
			}
			
			List<SubjectDTO> minors = new ArrayList<>();
			if (program.getMinors() != null) {
				for (int j = 0; j < program.getMinors().length; j++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(program.getMinors()[j]))
							.fetchOneInto(Subject.class);
					List<Subject> subs = new ArrayList<>();
					if (subject.getPreRequisites() != null) {
						for (int i = 0; i < subject.getPreRequisites().length; i++) {
							Subject sub = dslContext.selectFrom(SUBJECT)
									.where(SUBJECT.SUBJECT_ID.eq(subject.getPreRequisites()[i]))
									.fetchOneInto(Subject.class);
							subs.add(sub);
						}
					}
					SubjectDTO subjectDTO = SubjectDTO.builder()
							.subjectId(subject.getSubjectId())
							.subjectCode(subject.getSubjectCode())
							.subjectTitle(subject.getSubjectTitle())
							.units(subject.getUnits())
							.preRequisites(subs)
							.type(subject.getType())
							.activeDeactive(subject.getActiveDeactive())
							.build();
					minors.add(subjectDTO);
				}
				
			}
			List<SubjectDTO> electives = new ArrayList<>();
			if (program.getElectives() != null) {
				for (int j = 0; j < program.getElectives().length; j++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_ID.eq(program.getElectives()[j]))
							.fetchOneInto(Subject.class);
					List<Subject> subs = new ArrayList<>();
					if (subject.getPreRequisites() != null) {
						for (int i = 0; i < subject.getPreRequisites().length; i++) {
							Subject sub = dslContext.selectFrom(SUBJECT)
									.where(SUBJECT.SUBJECT_ID.eq(subject.getPreRequisites()[i]))
									.fetchOneInto(Subject.class);
							subs.add(sub);
						}
					}
					SubjectDTO subjectDTO = SubjectDTO.builder()
							.subjectId(subject.getSubjectId())
							.subjectCode(subject.getSubjectCode())
							.subjectTitle(subject.getSubjectTitle())
							.units(subject.getUnits())
							.preRequisites(subs)
							.type(subject.getType())
							.activeDeactive(subject.getActiveDeactive())
							.build();
					electives.add(subjectDTO);
				}
			}
			return ProgramDTO.builder()
					.programId(program.getProgramId())
					.programCode(program.getProgramCode())
					.programTitle(program.getProgramTitle())
					.firstYearFirstSemMin(program.getFirstYearFirstSemMin())
					.firstYearFirstSemMax(program.getFirstYearFirstSemMax())
					.firstYearSecondSemMin(program.getFirstYearSecondSemMin())
					.firstYearSecondSemMax(program.getFirstYearSecondSemMax())
					.secondYearFirstSemMin(program.getSecondYearFirstSemMin())
					.secondYearFirstSemMax(program.getSecondYearFirstSemMax())
					.secondYearSecondSemMin(program.getSecondYearSecondSemMin())
					.secondYearSecondSemMax(program.getSecondYearSecondSemMax())
					.thirdYearFirstSemMin(program.getThirdYearFirstSemMin())
					.thirdYearFirstSemMax(program.getThirdYearFirstSemMax())
					.thirdYearSecondSemMin(program.getThirdYearSecondSemMin())
					.thirdYearSecondSemMax(program.getThirdYearSecondSemMax())
					.fourthYearFirstSemMin(program.getFourthYearFirstSemMin())
					.fourthYearFirstSemMax(program.getFourthYearFirstSemMax())
					.fourthYearSecondSemMin(program.getFourthYearSecondSemMin())
					.fourthYearSecondSemMax(program.getFourthYearSecondSemMax())
					.majors(majors)
					.minors(minors)
					.electives(electives)
					.status(program.getStatus())
					.build();
		} else {
			throw new RuntimeException("Program not found");
		}
	}
	
	public Response addProgram(ProgramRequest program) {
		Program programCode = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_CODE.eq(program.getProgramCode()))
				.fetchOneInto(Program.class);
		Program programTitle = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_TITLE.eq(program.getProgramTitle()))
				.fetchOneInto(Program.class);
		
		if (programCode != null) {
			return Response.builder()
					.status(409)
					.message("Program Code already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else if (programTitle != null) {
			return Response.builder()
					.status(409)
					.message("Program Title already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			Integer id = 0;
			if (program.getMajors() != null) {
				Integer[] majors = new Integer[program.getMajors().length];
				for (int i = 0; i < program.getMajors().length; i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_TITLE.eq(program.getMajors()[i]))
							.fetchOneInto(Subject.class);
					majors[i] = subject.getSubjectId();
				}
				dslContext.insertInto(PROGRAM)
				.set(PROGRAM.MAJORS, majors)
				.execute();
				
				Program prog = dslContext.selectFrom(PROGRAM)
				.orderBy(PROGRAM.PROGRAM_ID.desc())
				.limit(1)
				.fetchOneInto(Program.class);
				
				id = prog.getProgramId();
			}
			
			if (program.getMinors() != null) {
				Integer[] minors = new Integer[program.getMinors().length];
				for (int i = 0; i < program.getMinors().length; i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_TITLE.eq(program.getMinors()[i]))
							.fetchOneInto(Subject.class);
					minors[i] = subject.getSubjectId();
				}
				if (id == 0) {
					dslContext.insertInto(PROGRAM)
					.set(PROGRAM.MINORS, minors)
					.execute();
					
					Program prog = dslContext.selectFrom(PROGRAM)
					.orderBy(PROGRAM.PROGRAM_ID.desc())
					.limit(1)
					.fetchOneInto(Program.class);
					
					id = prog.getProgramId();
				} else {
					dslContext.update(PROGRAM)
					.set(PROGRAM.MINORS, minors)
					.where(PROGRAM.PROGRAM_ID.eq(id))
					.execute();
				}
			}
			
			if (program.getElectives() != null) {
				Integer[] electives = new Integer[program.getElectives().length];
				for (int i = 0; i < program.getElectives().length; i++) {
					Subject subject = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_TITLE.eq(program.getElectives()[i]))
							.fetchOneInto(Subject.class);
					electives[i] = subject.getSubjectId();
				}
				if (id == 0) {
					dslContext.insertInto(PROGRAM)
					.set(PROGRAM.ELECTIVES, electives)
					.execute();
					
					Program prog = dslContext.selectFrom(PROGRAM)
					.orderBy(PROGRAM.PROGRAM_ID.desc())
					.limit(1)
					.fetchOneInto(Program.class);
					
					id = prog.getProgramId();
				} else {
					dslContext.update(PROGRAM)
					.set(PROGRAM.ELECTIVES, electives)
					.where(PROGRAM.PROGRAM_ID.eq(id))
					.execute();
				}
			}
			
			if (id == 0) {
				dslContext.insertInto(PROGRAM)
				.set(PROGRAM.PROGRAM_CODE, program.getProgramCode())
				.set(PROGRAM.PROGRAM_TITLE, program.getProgramTitle())
				.set(PROGRAM.FIRST_YEAR_FIRST_SEM_MIN, program.getFirstYearFirstSemMin())
				.set(PROGRAM.FIRST_YEAR_FIRST_SEM_MAX, program.getFirstYearFirstSemMax())
				.set(PROGRAM.FIRST_YEAR_SECOND_SEM_MIN, program.getFirstYearSecondSemMin())
				.set(PROGRAM.FIRST_YEAR_SECOND_SEM_MAX, program.getFirstYearSecondSemMax())
				.set(PROGRAM.SECOND_YEAR_FIRST_SEM_MIN, program.getSecondYearFirstSemMin())
				.set(PROGRAM.SECOND_YEAR_FIRST_SEM_MAX, program.getSecondYearFirstSemMax())
				.set(PROGRAM.SECOND_YEAR_SECOND_SEM_MIN, program.getSecondYearSecondSemMin())
				.set(PROGRAM.SECOND_YEAR_SECOND_SEM_MAX, program.getSecondYearSecondSemMax())
				.set(PROGRAM.THIRD_YEAR_FIRST_SEM_MIN, program.getThirdYearFirstSemMin())
				.set(PROGRAM.THIRD_YEAR_FIRST_SEM_MAX, program.getThirdYearFirstSemMax())
				.set(PROGRAM.THIRD_YEAR_SECOND_SEM_MIN, program.getThirdYearSecondSemMin())
				.set(PROGRAM.THIRD_YEAR_SECOND_SEM_MAX, program.getThirdYearSecondSemMax())
				.set(PROGRAM.FOURTH_YEAR_FIRST_SEM_MIN, program.getFourthYearFirstSemMin())
				.set(PROGRAM.FOURTH_YEAR_FIRST_SEM_MAX, program.getFourthYearFirstSemMax())
				.set(PROGRAM.FOURTH_YEAR_SECOND_SEM_MIN, program.getFourthYearSecondSemMin())
				.set(PROGRAM.FOURTH_YEAR_SECOND_SEM_MAX, program.getFourthYearSecondSemMax())
				.set(PROGRAM.STATUS, true)
				.execute();	
			} else {
				dslContext.update(PROGRAM)
				.set(PROGRAM.PROGRAM_CODE, program.getProgramCode())
				.set(PROGRAM.PROGRAM_TITLE, program.getProgramTitle())
				.set(PROGRAM.FIRST_YEAR_FIRST_SEM_MIN, program.getFirstYearFirstSemMin())
				.set(PROGRAM.FIRST_YEAR_FIRST_SEM_MAX, program.getFirstYearFirstSemMax())
				.set(PROGRAM.FIRST_YEAR_SECOND_SEM_MIN, program.getFirstYearSecondSemMin())
				.set(PROGRAM.FIRST_YEAR_SECOND_SEM_MAX, program.getFirstYearSecondSemMax())
				.set(PROGRAM.SECOND_YEAR_FIRST_SEM_MIN, program.getSecondYearFirstSemMin())
				.set(PROGRAM.SECOND_YEAR_FIRST_SEM_MAX, program.getSecondYearFirstSemMax())
				.set(PROGRAM.SECOND_YEAR_SECOND_SEM_MIN, program.getSecondYearSecondSemMin())
				.set(PROGRAM.SECOND_YEAR_SECOND_SEM_MAX, program.getSecondYearSecondSemMax())
				.set(PROGRAM.THIRD_YEAR_FIRST_SEM_MIN, program.getThirdYearFirstSemMin())
				.set(PROGRAM.THIRD_YEAR_FIRST_SEM_MAX, program.getThirdYearFirstSemMax())
				.set(PROGRAM.THIRD_YEAR_SECOND_SEM_MIN, program.getThirdYearSecondSemMin())
				.set(PROGRAM.THIRD_YEAR_SECOND_SEM_MAX, program.getThirdYearSecondSemMax())
				.set(PROGRAM.FOURTH_YEAR_FIRST_SEM_MIN, program.getFourthYearFirstSemMin())
				.set(PROGRAM.FOURTH_YEAR_FIRST_SEM_MAX, program.getFourthYearFirstSemMax())
				.set(PROGRAM.FOURTH_YEAR_SECOND_SEM_MIN, program.getFourthYearSecondSemMin())
				.set(PROGRAM.FOURTH_YEAR_SECOND_SEM_MAX, program.getFourthYearSecondSemMax())
				.set(PROGRAM.STATUS, true)
				.where(PROGRAM.PROGRAM_ID.eq(id))
				.execute();	
			}
			return Response.builder()
					.status(201)
					.message("Program successfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateProgram(Integer programId, ProgramRequest program) {
		Program _program = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_ID.eq(programId))
				.fetchOneInto(Program.class);
		Program programCode = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_CODE.eq(program.getProgramCode()))
				.fetchOneInto(Program.class);
		Program programTitle = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_TITLE.eq(program.getProgramTitle()))
				.fetchOneInto(Program.class);
		
		if (_program != null) {
			if (programCode != null) {
				return Response.builder()
						.status(409)
						.message("Program code already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else if (programTitle != null) {
				return Response.builder()
						.status(409)
						.message("Program Title already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				if (program.getProgramCode() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.PROGRAM_CODE, program.getProgramCode())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getProgramTitle() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.PROGRAM_TITLE, program.getProgramTitle())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getFirstYearFirstSemMin() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.FIRST_YEAR_FIRST_SEM_MIN, program.getFirstYearFirstSemMin())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getFirstYearFirstSemMax() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.FIRST_YEAR_FIRST_SEM_MAX, program.getFirstYearFirstSemMax())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getFirstYearSecondSemMin() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.FIRST_YEAR_SECOND_SEM_MIN, program.getFirstYearSecondSemMin())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getFirstYearSecondSemMax() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.FIRST_YEAR_SECOND_SEM_MAX, program.getFirstYearSecondSemMax())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getSecondYearFirstSemMin() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.SECOND_YEAR_FIRST_SEM_MIN, program.getSecondYearFirstSemMin())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getSecondYearFirstSemMax() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.SECOND_YEAR_FIRST_SEM_MAX, program.getSecondYearFirstSemMax())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getSecondYearSecondSemMin() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.SECOND_YEAR_SECOND_SEM_MIN, program.getSecondYearSecondSemMin())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getSecondYearSecondSemMax() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.SECOND_YEAR_SECOND_SEM_MAX, program.getSecondYearSecondSemMax())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getThirdYearFirstSemMin() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.THIRD_YEAR_FIRST_SEM_MIN, program.getThirdYearFirstSemMin())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getThirdYearFirstSemMax() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.THIRD_YEAR_FIRST_SEM_MAX, program.getThirdYearFirstSemMax())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getThirdYearSecondSemMin() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.THIRD_YEAR_SECOND_SEM_MIN, program.getThirdYearSecondSemMin())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getThirdYearSecondSemMax() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.THIRD_YEAR_SECOND_SEM_MAX, program.getThirdYearSecondSemMax())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getFourthYearFirstSemMin() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.FOURTH_YEAR_FIRST_SEM_MIN, program.getFourthYearFirstSemMin())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getFourthYearFirstSemMax() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.FOURTH_YEAR_FIRST_SEM_MAX, program.getFourthYearFirstSemMax())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getFourthYearSecondSemMin() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.FOURTH_YEAR_SECOND_SEM_MIN, program.getFourthYearSecondSemMin())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getFourthYearSecondSemMax() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.FOURTH_YEAR_SECOND_SEM_MAX, program.getFourthYearSecondSemMax())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getMajors() != null) {
					Integer[] majors = new Integer[program.getMajors().length];
					for (int i = 0; i < program.getMajors().length; i++) {
						Subject subject = dslContext.selectFrom(SUBJECT)
								.where(SUBJECT.SUBJECT_TITLE.eq(program.getMajors()[i]))
								.fetchOneInto(Subject.class);
						majors[i] = subject.getSubjectId();
					}
					
					dslContext.update(PROGRAM)
					.set(PROGRAM.MAJORS, majors)
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getMinors() != null) {
					Integer[] minors = new Integer[program.getMinors().length];
					for (int i = 0; i < program.getMinors().length; i++) {
						Subject subject = dslContext.selectFrom(SUBJECT)
								.where(SUBJECT.SUBJECT_TITLE.eq(program.getMinors()[i]))
								.fetchOneInto(Subject.class);
						minors[i] = subject.getSubjectId();
					}
					
					dslContext.update(PROGRAM)
					.set(PROGRAM.MINORS, minors)
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getElectives() != null) {
					Integer[] electives = new Integer[program.getElectives().length];
					for (int i = 0; i < program.getElectives().length; i++) {
						Subject subject = dslContext.selectFrom(SUBJECT)
								.where(SUBJECT.SUBJECT_TITLE.eq(program.getElectives()[i]))
								.fetchOneInto(Subject.class);
						electives[i] = subject.getSubjectId();
					}
					
					dslContext.update(PROGRAM)
					.set(PROGRAM.ELECTIVES, electives)
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				if (program.getStatus() != null) {
					dslContext.update(PROGRAM)
					.set(PROGRAM.STATUS, program.getStatus())
					.where(PROGRAM.PROGRAM_ID.eq(programId))
					.execute();
				}
				return Response.builder()
						.status(200)
						.message("Program successfully updated")
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Program not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}
