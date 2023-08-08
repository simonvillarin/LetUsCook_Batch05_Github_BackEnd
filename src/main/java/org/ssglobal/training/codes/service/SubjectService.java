package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.SubjectDTO;
import org.ssglobal.training.codes.request.SubjectRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Subject SUBJECT = org.ssglobal.training.codes.tables.Subject.SUBJECT;
	
	public List<SubjectDTO> getAllSubjects() {
		List<Subject> subjects = dslContext.selectFrom(SUBJECT)
				.fetchInto(Subject.class);
		List<SubjectDTO> subjectDTOs = new ArrayList<>();
		for (int i = 0; i < subjects.size(); i++) {
			List<Subject> subs = new ArrayList<>();
			for (int j = 0; j < subjects.get(i).getPreRequisites().length; j++) {
				Subject sub = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(subjects.get(i).getPreRequisites()[j]))
						.fetchOneInto(Subject.class);
				subs.add(sub);
			}
			SubjectDTO subjectDTO = SubjectDTO.builder()
					.subjectId(subjects.get(i).getSubjectId())
					.subjectCode(subjects.get(i).getSubjectCode())
					.subjectTitle(subjects.get(i).getSubjectTitle())
					.units(subjects.get(i).getUnits())
					.preRequisites(subs)
					.type(subjects.get(i).getType())
					.activeDeactive(subjects.get(i).getActiveDeactive())
					.build();
			subjectDTOs.add(subjectDTO);
		}
		return subjectDTOs;
	}
	
	public List<Subject> getAllMajors() {
		List<Subject> subjects = dslContext.selectFrom(SUBJECT).where(SUBJECT.TYPE.eq("Major")).fetchInto(Subject.class);
		if (subjects != null) {
			return subjects;
		} else {
			return null;
		}
		
	}
	
	public List<Subject> getAllMinors() {
		List<Subject> subjects = dslContext.selectFrom(SUBJECT).where(SUBJECT.TYPE.eq("Minor")).fetchInto(Subject.class);
		if (subjects != null) {
			return subjects;
		} else {
			return null;
		}
	}
	
	public List<Subject> getAllElectives() {
		List<Subject> subjects = dslContext.selectFrom(SUBJECT).where(SUBJECT.TYPE.eq("Elective")).fetchInto(Subject.class);
		if (subjects != null) {
			return subjects;
		} else {
			return null;
		}
	}
	
	public SubjectDTO getSubjectById(Integer subjectId) {
		Subject subject = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_ID.eq(subjectId))
				.fetchOneInto(Subject.class);
		
		if (subject != null) {
			List<Subject> subs = new ArrayList<>();
			for (int i = 0; i < subject.getPreRequisites().length; i++) {
				Subject sub = dslContext.selectFrom(SUBJECT)
						.where(SUBJECT.SUBJECT_ID.eq(subject.getPreRequisites()[i]))
						.fetchOneInto(Subject.class);
				subs.add(sub);
			}
			return SubjectDTO.builder()
					.subjectId(subject.getSubjectId())
					.subjectCode(subject.getSubjectCode())
					.subjectTitle(subject.getSubjectTitle())
					.units(subject.getUnits())
					.preRequisites(subs)
					.type(subject.getType())
					.activeDeactive(subject.getActiveDeactive())
					.build();
		} else {
			throw new RuntimeException("Subject not found");
		}
	}
	
	public Response addSubjects(List<SubjectRequest> subjects) {
		for (int j = 0; j < subjects.size(); j++) {
			Subject subjectCode = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_CODE.eq(subjects.get(j).getSubjectCode()))
					.fetchOneInto(Subject.class);	
			Subject subjectTitle = dslContext.selectFrom(SUBJECT)
					.where(SUBJECT.SUBJECT_TITLE.eq(subjects.get(j).getSubjectTitle()))
					.fetchOneInto(Subject.class);
			
			if (subjectCode != null) {
				return Response.builder()
						.status(409)
						.message("Subject code already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else if (subjectTitle != null) {
				return Response.builder()
						.status(409)
						.message("Subject title already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				int size = 0;
				Integer[] preRequisites = new Integer[size];
				if (subjects.get(j).getPreRequisites() != null) {
					size = subjects.get(j).getPreRequisites().size();
					preRequisites = new Integer[size];
					for (int i = 0; i < subjects.get(j).getPreRequisites().size(); i++) {
						Subject _subjects = dslContext.selectFrom(SUBJECT)
								.where(SUBJECT.SUBJECT_TITLE.eq(subjects.get(j).getPreRequisites().get(i)))
								.fetchOneInto(Subject.class);
						preRequisites[j] = _subjects.getSubjectId();
					}
				}
				dslContext.insertInto(SUBJECT)
				.set(SUBJECT.SUBJECT_CODE, subjects.get(j).getSubjectCode())
				.set(SUBJECT.SUBJECT_TITLE, subjects.get(j).getSubjectTitle())
				.set(SUBJECT.UNITS, subjects.get(j).getUnits())
				.set(SUBJECT.PRE_REQUISITES, preRequisites)
				.set(SUBJECT.TYPE, subjects.get(j).getType())
				.set(SUBJECT.ACTIVE_DEACTIVE, true)
				.execute();
			}
		}
		return Response.builder()
				.status(201)
				.message("Subjects sucessfully created")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response addSubject(SubjectRequest subject) {
		Subject subjectCode = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_CODE.eq(subject.getSubjectCode()))
				.fetchOneInto(Subject.class);	
		Subject subjectTitle = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_TITLE.eq(subject.getSubjectTitle()))
				.fetchOneInto(Subject.class);
		if (subjectCode != null) {
			return Response.builder()
					.status(409)
					.message("Subject code already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else if (subjectTitle != null) {
			return Response.builder()
					.status(409)
					.message("Subject title already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			int size = 0;
			Integer[] preRequisites = new Integer[size];
			if (subject.getPreRequisites() != null) {
				size = subject.getPreRequisites().size();
				preRequisites = new Integer[size];
				for (int i = 0; i < subject.getPreRequisites().size(); i++) {
					Subject subjects = dslContext.selectFrom(SUBJECT)
							.where(SUBJECT.SUBJECT_TITLE.eq(subject.getPreRequisites().get(i)))
							.fetchOneInto(Subject.class);
					preRequisites[i] = subjects.getSubjectId();
				}
			}
			
			dslContext.insertInto(SUBJECT)
			.set(SUBJECT.SUBJECT_CODE, subject.getSubjectCode())
			.set(SUBJECT.SUBJECT_TITLE, subject.getSubjectTitle())
			.set(SUBJECT.UNITS, subject.getUnits())
			.set(SUBJECT.PRE_REQUISITES, preRequisites)
			.set(SUBJECT.TYPE, subject.getType())
			.set(SUBJECT.ACTIVE_DEACTIVE, true)
			.execute();
			
			return Response.builder()
					.status(201)
					.message("Subject sucessfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateSubject(Integer subjectId, SubjectRequest subject) {
		Subject _subject = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_ID.eq(subjectId))
				.fetchOneInto(Subject.class);
		Subject subjectCode = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_CODE.eq(subject.getSubjectCode()))
				.fetchOneInto(Subject.class);	
		Subject subjectTitle = dslContext.selectFrom(SUBJECT)
				.where(SUBJECT.SUBJECT_TITLE.eq(subject.getSubjectTitle()))
				.fetchOneInto(Subject.class);
		
		if (_subject != null) {
			if (subjectCode != null) {
				return Response.builder()
						.status(409)
						.message("Subject code already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else if (subjectTitle != null) {
				return Response.builder()
						.status(409)
						.message("Subject title already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				if (subject.getSubjectCode() != null) {
					dslContext.update(SUBJECT)
					.set(SUBJECT.SUBJECT_CODE, subject.getSubjectCode())
					.where(SUBJECT.SUBJECT_ID.eq(subjectId))
					.execute();
				}
				if (subject.getSubjectTitle() != null) {
					dslContext.update(SUBJECT)
					.set(SUBJECT.SUBJECT_TITLE, subject.getSubjectTitle())
					.where(SUBJECT.SUBJECT_ID.eq(subjectId))
					.execute();
				}
				if (subject.getUnits() != null) {
					dslContext.update(SUBJECT)
					.set(SUBJECT.UNITS, subject.getUnits())
					.where(SUBJECT.SUBJECT_ID.eq(subjectId))
					.execute();
				}
				if (subject.getPreRequisites() != null) {
					int size = 0;
					Integer[] preRequisites = new Integer[size];
					if (subject.getPreRequisites() != null) {
						size = subject.getPreRequisites().size();
						preRequisites = new Integer[size];
						for (int i = 0; i < subject.getPreRequisites().size(); i++) {
							Subject subjects = dslContext.selectFrom(SUBJECT)
									.where(SUBJECT.SUBJECT_TITLE.eq(subject.getPreRequisites().get(i)))
									.fetchOneInto(Subject.class);
							preRequisites[i] = subjects.getSubjectId();
						}
					}
					dslContext.update(SUBJECT)
					.set(SUBJECT.PRE_REQUISITES, preRequisites)
					.where(SUBJECT.SUBJECT_ID.eq(subjectId))
					.execute();
				}
				if (subject.getType() != null) {
					dslContext.update(SUBJECT)
					.set(SUBJECT.TYPE, subject.getType())
					.where(SUBJECT.SUBJECT_ID.eq(subjectId))
					.execute();
				}
				if (subject.getActiveDeactive() != null) {
					dslContext.update(SUBJECT)
					.set(SUBJECT.ACTIVE_DEACTIVE, subject.getActiveDeactive())
					.where(SUBJECT.SUBJECT_ID.eq(subjectId))
					.execute();
				}	
				return Response.builder()
						.status(201)
						.message("Subject sucessfully updated")
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Subject not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}
