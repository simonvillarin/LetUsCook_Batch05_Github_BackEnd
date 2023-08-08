package org.ssglobal.training.codes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.AppDTO;
import org.ssglobal.training.codes.request.AppRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Admin;
import org.ssglobal.training.codes.tables.pojos.Application;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Program;
import org.ssglobal.training.codes.tables.pojos.Student;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ApplicationService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Application APPLICATION = org.ssglobal.training.codes.tables.Application.APPLICATION;
	private final org.ssglobal.training.codes.tables.Program PROGRAM = org.ssglobal.training.codes.tables.Program.PROGRAM;
	private final org.ssglobal.training.codes.tables.Admin ADMIN = org.ssglobal.training.codes.tables.Admin.ADMIN;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Parent PARENT = org.ssglobal.training.codes.tables.Parent.PARENT;
	
	public List<AppDTO> getAllApplications() {
		List<Application> apps = dslContext.selectFrom(APPLICATION)
				.orderBy(APPLICATION.APP_ID.desc())
				.fetchInto(Application.class);
		
		List<AppDTO> appDTOs = new ArrayList<>();
		for (int i = 0; i < apps.size(); i++) {
			Program program = dslContext.selectFrom(PROGRAM)
					.where(PROGRAM.PROGRAM_ID.eq(apps.get(i).getProgramId()))
					.fetchOneInto(Program.class);
			AppDTO appDTO = AppDTO.builder()
					.appId(apps.get(i).getAppId())
					.studentId(apps.get(i).getStudentId())
					.program(program)
					.yearLevel(apps.get(i).getYearLevel())
					.sem(apps.get(i).getSem())
					.academicYear(apps.get(i).getAcademicYear())
					.firstname(apps.get(i).getFirstname())
					.middlename(apps.get(i).getMiddlename())
					.lastname(apps.get(i).getLastname())
					.suffix(apps.get(i).getSuffix())
					.gender(apps.get(i).getGender())
					.civilStatus(apps.get(i).getCivilStatus())
					.birthdate(apps.get(i).getBirthdate())
					.birthplace(apps.get(i).getBirthplace())
					.citizenship(apps.get(i).getCitizenship())
					.religion(apps.get(i).getReligion())
					.unit(apps.get(i).getUnit())
					.street(apps.get(i).getStreet())
					.subdivision(apps.get(i).getSubdivision())
					.barangay(apps.get(i).getBarangay())
					.city(apps.get(i).getCity())
					.province(apps.get(i).getProvince())
					.zipcode(apps.get(i).getZipcode())
					.telephone(apps.get(i).getTelephone())
					.mobile(apps.get(i).getMobile())
					.email(apps.get(i).getEmail())
					.lastSchoolAttended(apps.get(i).getLastSchoolAttended())
					.programTaken(apps.get(i).getProgramTaken())
					.lastSem(apps.get(i).getLastSem())
					.lastYearLevel(apps.get(i).getYearLevel())
					.lastSchoolYear(apps.get(i).getLastSchoolYear())
					.dateOfGraduation(apps.get(i).getDateOfGraduation())
					.parentFirstname(apps.get(i).getParentFirstname())
					.parentMiddlename(apps.get(i).getParentMiddlename())
					.parentLastname(apps.get(i).getParentLastname())
					.parentSuffix(apps.get(i).getParentSuffix())
					.parentAddress(apps.get(i).getParentAddress())
					.parentContact(apps.get(i).getParentContact())
					.parentEmail(apps.get(i).getParentEmail())
					.parentRelationship(apps.get(i).getParentRelationship())
					.applicationDate(apps.get(i).getApplicationDate())
					.status(apps.get(i).getStatus())
					.build();
			appDTOs.add(appDTO);
		}
		return appDTOs;
	}
	
	public AppDTO getApplicationById(Integer appId) {
		Application app = dslContext.selectFrom(APPLICATION)
				.where(APPLICATION.APP_ID.eq(appId))
				.fetchOneInto(Application.class);
		if (app != null) {
			Program program = dslContext.selectFrom(PROGRAM)
					.where(PROGRAM.PROGRAM_ID.eq(app.getProgramId()))
					.fetchOneInto(Program.class);
			return AppDTO.builder()
					.appId(app.getAppId())
					.studentId(app.getStudentId())
					.program(program)
					.yearLevel(app.getYearLevel())
					.sem(app.getSem())
					.academicYear(app.getAcademicYear())
					.firstname(app.getFirstname())
					.middlename(app.getMiddlename())
					.lastname(app.getLastname())
					.suffix(app.getSuffix())
					.gender(app.getGender())
					.civilStatus(app.getCivilStatus())
					.birthdate(app.getBirthdate())
					.birthplace(app.getBirthplace())
					.citizenship(app.getCitizenship())
					.religion(app.getReligion())
					.unit(app.getUnit())
					.street(app.getStreet())
					.subdivision(app.getSubdivision())
					.barangay(app.getBarangay())
					.city(app.getCity())
					.province(app.getProvince())
					.zipcode(app.getZipcode())
					.telephone(app.getTelephone())
					.mobile(app.getMobile())
					.email(app.getEmail())
					.lastSchoolAttended(app.getLastSchoolAttended())
					.programTaken(app.getProgramTaken())
					.lastSem(app.getLastSem())
					.lastYearLevel(app.getYearLevel())
					.lastSchoolYear(app.getLastSchoolYear())
					.dateOfGraduation(app.getDateOfGraduation())
					.parentFirstname(app.getParentFirstname())
					.parentMiddlename(app.getParentMiddlename())
					.parentLastname(app.getParentLastname())
					.parentSuffix(app.getParentSuffix())
					.parentAddress(app.getParentAddress())
					.parentContact(app.getParentContact())
					.parentEmail(app.getParentEmail())
					.parentRelationship(app.getParentRelationship())
					.applicationDate(app.getApplicationDate())
					.status(app.getStatus())
					.build();
		} else {
			throw new RuntimeException("Application not found");
		}
	}
	
	public Response addApplication(AppRequest app) {
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(app.getEmail())).fetchOneInto(Admin.class);
		Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(app.getEmail()))
				.fetchOneInto(Professor.class);
		Student student = dslContext.selectFrom(STUDENT).where(STUDENT.EMAIL.eq(app.getEmail())).fetchOneInto(Student.class);
		System.out.println(app.toString());
		
		if (admin != null || professor != null || student != null) {
			return Response.builder()
					.status(409)
					.message("Email already exists")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			List<Application> existingParent = dslContext.selectFrom(APPLICATION)
					.where(APPLICATION.PARENT_FIRSTNAME.similarTo(app.getParentFirstname()))
					.and(APPLICATION.PARENT_MIDDLENAME.similarTo(app.getParentMiddlename()))
					.and(APPLICATION.PARENT_LASTNAME.similarTo(app.getParentLastname()))
					.and(APPLICATION.PARENT_SUFFIX.similarTo(app.getParentSuffix()))
					.and(APPLICATION.PARENT_EMAIL.eq(app.getParentEmail()))
					.fetchInto(Application.class);
			if (existingParent.size() == 0) {
				List<Application> parentEmail = dslContext.selectFrom(APPLICATION)
				        .where(APPLICATION.PARENT_EMAIL.eq(app.getParentEmail()))
				        .fetchInto(Application.class);
				
				if (parentEmail.size() == 0) {
					Program program = dslContext.selectFrom(PROGRAM)
							.where(PROGRAM.PROGRAM_CODE.eq(app.getProgramCode()))
							.fetchOneInto(Program.class);
					
					dslContext.insertInto(APPLICATION)
					.set(APPLICATION.STUDENT_ID, app.getStudentId())
					.set(APPLICATION.PROGRAM_ID, program.getProgramId())
					.set(APPLICATION.YEAR_LEVEL, app.getYearLevel())
					.set(APPLICATION.SEM, app.getSem())
					.set(APPLICATION.ACADEMIC_YEAR, app.getAcademicYear())
					.set(APPLICATION.FIRSTNAME, app.getFirstname())
					.set(APPLICATION.MIDDLENAME, app.getMiddlename())
					.set(APPLICATION.LASTNAME, app.getLastname())
					.set(APPLICATION.SUFFIX, app.getSuffix())
					.set(APPLICATION.GENDER, app.getGender())
					.set(APPLICATION.CIVIL_STATUS, app.getCivilStatus())
					.set(APPLICATION.BIRTHDATE, app.getBirthdate())
					.set(APPLICATION.BIRTHPLACE, app.getBirthplace())
					.set(APPLICATION.CITIZENSHIP, app.getCitizenship())
					.set(APPLICATION.RELIGION, app.getReligion())
					.set(APPLICATION.UNIT, app.getUnit())
					.set(APPLICATION.STREET, app.getStreet())
					.set(APPLICATION.SUBDIVISION, app.getSubdivision())
					.set(APPLICATION.BARANGAY, app.getBarangay())
					.set(APPLICATION.CITY, app.getCity())
					.set(APPLICATION.PROVINCE, app.getProvince())
					.set(APPLICATION.ZIPCODE, app.getZipcode())
					.set(APPLICATION.TELEPHONE, app.getTelephone())
					.set(APPLICATION.MOBILE, app.getMobile())
					.set(APPLICATION.EMAIL, app.getEmail())
					.set(APPLICATION.LAST_SCHOOL_ATTENDED, app.getLastSchoolAttended())
					.set(APPLICATION.PROGRAM_TAKEN, app.getProgramTaken())
					.set(APPLICATION.LAST_SEM, app.getLastSem())
					.set(APPLICATION.LAST_YEAR_LEVEL, app.getLastYearLevel())
					.set(APPLICATION.LAST_SCHOOL_YEAR, app.getLastSchoolYear())
					.set(APPLICATION.DATE_OF_GRADUATION, app.getDateOfGraduation())
					.set(APPLICATION.PARENT_FIRSTNAME, app.getParentFirstname())
					.set(APPLICATION.PARENT_MIDDLENAME, app.getParentMiddlename())
					.set(APPLICATION.PARENT_LASTNAME, app.getParentLastname())
					.set(APPLICATION.PARENT_SUFFIX, app.getSuffix())
					.set(APPLICATION.PARENT_ADDRESS, app.getParentAddress())
					.set(APPLICATION.PARENT_CONTACT, app.getParentContact())
					.set(APPLICATION.PARENT_EMAIL, app.getParentEmail())
					.set(APPLICATION.PARENT_RELATIONSHIP, app.getParentRelationship())
					.set(APPLICATION.STATUS, "Pending")
					.set(APPLICATION.APPLICATION_DATE, LocalDate.now())
					.execute();
				} else {
					return Response.builder()
							.status(409)
							.message("Email already exists")
							.timestamp(LocalDateTime.now())
							.build();
				}
			} else {
					Program program = dslContext.selectFrom(PROGRAM)
							.where(PROGRAM.PROGRAM_CODE.eq(app.getProgramCode()))
							.fetchOneInto(Program.class);
					
					dslContext.insertInto(APPLICATION)
					.set(APPLICATION.STUDENT_ID, app.getStudentId())
					.set(APPLICATION.PROGRAM_ID, program.getProgramId())
					.set(APPLICATION.YEAR_LEVEL, app.getYearLevel())
					.set(APPLICATION.SEM, app.getSem())
					.set(APPLICATION.ACADEMIC_YEAR, app.getAcademicYear())
					.set(APPLICATION.FIRSTNAME, app.getFirstname())
					.set(APPLICATION.MIDDLENAME, app.getMiddlename())
					.set(APPLICATION.LASTNAME, app.getLastname())
					.set(APPLICATION.SUFFIX, app.getSuffix())
					.set(APPLICATION.GENDER, app.getGender())
					.set(APPLICATION.CIVIL_STATUS, app.getCivilStatus())
					.set(APPLICATION.BIRTHDATE, app.getBirthdate())
					.set(APPLICATION.BIRTHPLACE, app.getBirthplace())
					.set(APPLICATION.CITIZENSHIP, app.getCitizenship())
					.set(APPLICATION.RELIGION, app.getReligion())
					.set(APPLICATION.UNIT, app.getUnit())
					.set(APPLICATION.STREET, app.getStreet())
					.set(APPLICATION.SUBDIVISION, app.getSubdivision())
					.set(APPLICATION.BARANGAY, app.getBarangay())
					.set(APPLICATION.CITY, app.getCity())
					.set(APPLICATION.PROVINCE, app.getProvince())
					.set(APPLICATION.ZIPCODE, app.getZipcode())
					.set(APPLICATION.TELEPHONE, app.getTelephone())
					.set(APPLICATION.MOBILE, app.getMobile())
					.set(APPLICATION.EMAIL, app.getEmail())
					.set(APPLICATION.LAST_SCHOOL_ATTENDED, app.getLastSchoolAttended())
					.set(APPLICATION.PROGRAM_TAKEN, app.getProgramTaken())
					.set(APPLICATION.LAST_SEM, app.getLastSem())
					.set(APPLICATION.LAST_YEAR_LEVEL, app.getLastYearLevel())
					.set(APPLICATION.LAST_SCHOOL_YEAR, app.getLastSchoolYear())
					.set(APPLICATION.DATE_OF_GRADUATION, app.getDateOfGraduation())
					.set(APPLICATION.PARENT_FIRSTNAME, app.getParentFirstname())
					.set(APPLICATION.PARENT_MIDDLENAME, app.getParentMiddlename())
					.set(APPLICATION.PARENT_LASTNAME, app.getParentLastname())
					.set(APPLICATION.PARENT_SUFFIX, app.getSuffix())
					.set(APPLICATION.PARENT_ADDRESS, app.getParentAddress())
					.set(APPLICATION.PARENT_CONTACT, app.getParentContact())
					.set(APPLICATION.PARENT_EMAIL, app.getParentEmail())
					.set(APPLICATION.PARENT_RELATIONSHIP, app.getParentRelationship())
					.set(APPLICATION.STATUS, "Pending")
					.set(APPLICATION.APPLICATION_DATE, LocalDate.now())
					.execute();
				
			}
			return Response.builder()
					.status(201)
					.message("Application successfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateApplication(Integer applicationId, AppRequest application) {
		Application _application = dslContext.selectFrom(APPLICATION)
				.where(APPLICATION.APP_ID.eq(applicationId))
				.fetchOneInto(Application.class);
		if(_application != null) {
			if (application.getStatus() != null) {
				dslContext.update(APPLICATION)
				.set(APPLICATION.STATUS, application.getStatus())
				.set(APPLICATION.STUDENT_ID, application.getStudentId())
				.where(APPLICATION.APP_ID.eq(applicationId))
				.execute();
			}
			
			return Response.builder()
					.status(201)
					.message("Application successfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		}else {
			return Response.builder()
					.status(404)
					.message("Application not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response deleteApplication(Integer applicationId) {
		Application application = dslContext.selectFrom(APPLICATION)
				.where(APPLICATION.APP_ID.eq(applicationId))
				.fetchOneInto(Application.class);
		if(application != null) {
			dslContext.delete(APPLICATION)
			.where(APPLICATION.APP_ID.eq(applicationId))
			.execute();
			
			return Response.builder()
					.status(201)
					.message("Application successfully deleted")
					.timestamp(LocalDateTime.now())
					.build();
		}else {
			return Response.builder()
					.status(404)
					.message("Application not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}
