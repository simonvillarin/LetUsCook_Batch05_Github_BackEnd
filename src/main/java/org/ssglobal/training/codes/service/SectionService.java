package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.SectionDTO;
import org.ssglobal.training.codes.request.SectionRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Program;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Student;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SectionService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Section SECTION = org.ssglobal.training.codes.tables.Section.SECTION;
	private final org.ssglobal.training.codes.tables.Program PROGRAM = org.ssglobal.training.codes.tables.Program.PROGRAM;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	
	public List<SectionDTO> getAllSections() {
		List<Section> sections = dslContext.selectFrom(SECTION)
				.fetchInto(Section.class);
		List<SectionDTO> sectionDTOs = new ArrayList<>();
		for (int i = 0; i < sections.size(); i++) {
			Program program = dslContext.selectFrom(PROGRAM)
					.where(PROGRAM.PROGRAM_ID.eq(sections.get(i).getProgramId()))
					.fetchOneInto(Program.class);
			SectionDTO section = SectionDTO.builder()
					.sectionId(sections.get(i).getSectionId())
					.program(program)
					.sectionName(sections.get(i).getSectionName())
					.section(sections.get(i).getSection())
					.activeDeactive(sections.get(i).getActiveDeactive())
					.build();
			sectionDTOs.add(section);
		}
		return sectionDTOs;
	}
	
	public List<String> getSectionByProgram(Integer studentId) {
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.STUDENT_ID.eq(studentId))
				.fetchOneInto(Student.class);
		List<String> sections = new ArrayList<>();
		if (student != null) {
			List<Section> section = dslContext.selectFrom(SECTION)
					.fetchInto(Section.class);
			for (int i = 0; i < section.size(); i++) {
				if (section.get(i).getProgramId() == student.getProgramId()) {
					sections.add(section.get(i).getSection());
				}
			}
		}
		return sections;
	}
	
	public Response addSection(SectionRequest section) {
		Program program = dslContext.selectFrom(PROGRAM)
				.where(PROGRAM.PROGRAM_CODE.eq(section.getProgramCode()))
				.fetchOneInto(Program.class);
		Section _section = dslContext.selectFrom(SECTION)
				.where(SECTION.SECTION_.eq(program.getProgramCode() + " " + section.getSectionName()))
				.fetchOneInto(Section.class);
		if (_section != null) {
			return Response.builder()
					.status(409)
					.message("Section name already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			dslContext.insertInto(SECTION)
			.set(SECTION.PROGRAM_ID, program.getProgramId())
			.set(SECTION.SECTION_NAME, section.getSectionName())
			.set(SECTION.SECTION_,  section.getProgramCode() + " " + section.getSectionName())
			.set(SECTION.ACTIVE_DEACTIVE, true)
			.execute();
			
			return Response.builder()
					.status(201)
					.message("Section successfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateSection(Integer sectionId, SectionRequest section) {
		Section sec = dslContext.selectFrom(SECTION)
				.where(SECTION.SECTION_ID.eq(sectionId))
				.fetchOneInto(Section.class);
		Section sectionName = dslContext.selectFrom(SECTION)
				.where(SECTION.SECTION_NAME.eq(section.getSectionName()))
				.fetchOneInto(Section.class);
		if (sec != null) {
			if (sectionName != null) {
				return Response.builder()
						.status(409)
						.message("Section name already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				if (section.getProgramCode() != null) {
					Program program = dslContext.selectFrom(PROGRAM)
							.where(PROGRAM.PROGRAM_CODE.eq(section.getProgramCode()))
							.fetchOneInto(Program.class);
					dslContext.update(SECTION)
					.set(SECTION.PROGRAM_ID, program.getProgramId())
					.set(SECTION.SECTION_, program.getProgramCode() + " " + sec.getSectionName())
					.where(SECTION.SECTION_ID.eq(sectionId))
					.execute();
				}
				if (section.getSectionName() != null) {
					Program program = dslContext.selectFrom(PROGRAM)
							.where(PROGRAM.PROGRAM_ID.eq(sec.getProgramId()))
							.fetchOneInto(Program.class);
					dslContext.update(SECTION)
					.set(SECTION.SECTION_NAME, section.getSectionName())
					.set(SECTION.SECTION_, program.getProgramCode() + " " + section.getSectionName())
					.where(SECTION.SECTION_ID.eq(sectionId))
					.execute();
				}
				if (section.getActiveDeactive() != null) {
					dslContext.update(SECTION)
					.set(SECTION.ACTIVE_DEACTIVE, section.getActiveDeactive())
					.where(SECTION.SECTION_ID.eq(sectionId))
					.execute();
				}
				return Response.builder()
						.status(200)
						.message("Section successfully updated")
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Section not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}
