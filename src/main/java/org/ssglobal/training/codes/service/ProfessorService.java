package org.ssglobal.training.codes.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Account;
import org.ssglobal.training.codes.tables.pojos.Admin;
import org.ssglobal.training.codes.tables.pojos.Image;
import org.ssglobal.training.codes.tables.pojos.Parent;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Student;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfessorService {
	private final DSLContext dslContext;
	private final PasswordEncoder passwordEncoder;
	private final PasswordGenerator passwordGenerator;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Image IMAGE = org.ssglobal.training.codes.tables.Image.IMAGE;
	private final org.ssglobal.training.codes.tables.Account ACCOUNT = org.ssglobal.training.codes.tables.Account.ACCOUNT;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Parent PARENT = org.ssglobal.training.codes.tables.Parent.PARENT;
	private final org.ssglobal.training.codes.tables.Admin ADMIN = org.ssglobal.training.codes.tables.Admin.ADMIN;
	
	public List<Professor> getAllProfessors() {
		return dslContext.selectFrom(PROFESSOR)
				.fetchInto(Professor.class);
	}
	
	public Professor getProfessorById(Integer professorId) {
		Professor professor = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
				.fetchOneInto(Professor.class);
		if (professor != null) {
			return professor;
		} else {
			throw new RuntimeException("Professor not found");
		}
	}
	
	public Response addProfessor(Professor professor) {
		Professor mobile = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.MOBILE.eq(professor.getMobile()))
				.fetchOneInto(Professor.class);
		Professor email = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Professor.class);
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Student.class);
		Parent parent = dslContext.selectFrom(PARENT)
				.where(PARENT.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Parent.class);
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(professor.getEmail())).fetchOneInto(Admin.class);
		if (mobile != null) {
			return Response.builder()
					.status(409)
					.message("Mobile number already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else if (email != null || student != null || admin != null || parent != null) {
			return Response.builder()
					.status(409)
					.message("Email already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			String professorNo = generateUserId();
			dslContext.insertInto(PROFESSOR)
			.set(PROFESSOR.PROFESSOR_NO, professorNo)
			.set(PROFESSOR.FIRSTNAME, professor.getFirstname())
			.set(PROFESSOR.MIDDLENAME, professor.getMiddlename())
			.set(PROFESSOR.LASTNAME, professor.getLastname())
			.set(PROFESSOR.SUFFIX, professor.getSuffix())
			.set(PROFESSOR.FULLNAME, professor.getFirstname() + " " + professor.getLastname())
			.set(PROFESSOR.GENDER, professor.getGender())
			.set(PROFESSOR.CIVIL_STATUS, professor.getCivilStatus())
			.set(PROFESSOR.BIRTHDATE, professor.getBirthdate())
			.set(PROFESSOR.BIRTHPLACE, professor.getBirthplace())
			.set(PROFESSOR.CITIZENSHIP, professor.getCitizenship())
			.set(PROFESSOR.RELIGION, professor.getReligion())
			.set(PROFESSOR.UNIT, professor.getUnit())
			.set(PROFESSOR.STREET, professor.getStreet())
			.set(PROFESSOR.SUBDIVISION, professor.getSubdivision())
			.set(PROFESSOR.BARANGAY, professor.getBarangay())
			.set(PROFESSOR.CITY, professor.getCity())
			.set(PROFESSOR.PROVINCE, professor.getProvince())
			.set(PROFESSOR.ZIPCODE, professor.getZipcode())
			.set(PROFESSOR.TELEPHONE, professor.getTelephone())
			.set(PROFESSOR.MOBILE, professor.getMobile())
			.set(PROFESSOR.EMAIL, professor.getEmail())
			.set(PROFESSOR.WORK, professor.getWork())
			.set(PROFESSOR.STATUS, professor.getStatus())
			.set(PROFESSOR.IMAGE, "")
			.set(PROFESSOR.BANNER, "")
			.set(PROFESSOR.ACTIVE_DEACTIVE, true)
			.execute();
			
			Professor prof = dslContext.selectFrom(PROFESSOR)
					.orderBy(PROFESSOR.PROFESSOR_ID.desc())
					.limit(1)
					.fetchOneInto(Professor.class);
			
			String password = passwordGenerator.generatePassword();
			dslContext.insertInto(ACCOUNT)
			.set(ACCOUNT.USER_ID, prof.getProfessorId())
			.set(ACCOUNT.USERNAME, professorNo)
			.set(ACCOUNT.PASSWORD, passwordEncoder.encode(password))
			.set(ACCOUNT.PASS, password)
			.set(ACCOUNT.TYPE, "PROFESSOR")
			.set(ACCOUNT.ACTIVE_DEACTIVE, true)
			.execute();
			
			Account account = dslContext.selectFrom(ACCOUNT)
					.where(ACCOUNT.USER_ID.eq(prof.getProfessorId()))
					.fetchOneInto(Account.class);
			
			return Response.builder()
					.status(201)
					.message(account.getUsername() + "-" + account.getPass())
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response addProfessorWithImage(Professor professor, MultipartFile image) {
		Professor mobile = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.MOBILE.eq(professor.getMobile()))
				.fetchOneInto(Professor.class);
		Professor email = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Professor.class);
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Student.class);
		Parent parent = dslContext.selectFrom(PARENT)
				.where(PARENT.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Parent.class);
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(professor.getEmail())).fetchOneInto(Admin.class);
		if (mobile != null) {
			return Response.builder()
					.status(409)
					.message("Mobile number already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else if (email != null || student != null || admin != null || parent != null) {
			return Response.builder()
					.status(409)
					.message("Email already exist")
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
			
			String professorNo = generateUserId();
			dslContext.insertInto(PROFESSOR)
			.set(PROFESSOR.PROFESSOR_NO, professorNo)
			.set(PROFESSOR.FIRSTNAME, professor.getFirstname())
			.set(PROFESSOR.MIDDLENAME, professor.getMiddlename())
			.set(PROFESSOR.LASTNAME, professor.getLastname())
			.set(PROFESSOR.SUFFIX, professor.getSuffix())
			.set(PROFESSOR.FULLNAME, professor.getFirstname() + " " + professor.getLastname())
			.set(PROFESSOR.GENDER, professor.getGender())
			.set(PROFESSOR.CIVIL_STATUS, professor.getCivilStatus())
			.set(PROFESSOR.BIRTHDATE, professor.getBirthdate())
			.set(PROFESSOR.BIRTHPLACE, professor.getBirthplace())
			.set(PROFESSOR.CITIZENSHIP, professor.getCitizenship())
			.set(PROFESSOR.RELIGION, professor.getReligion())
			.set(PROFESSOR.UNIT, professor.getUnit())
			.set(PROFESSOR.STREET, professor.getStreet())
			.set(PROFESSOR.SUBDIVISION, professor.getSubdivision())
			.set(PROFESSOR.BARANGAY, professor.getBarangay())
			.set(PROFESSOR.CITY, professor.getCity())
			.set(PROFESSOR.PROVINCE, professor.getProvince())
			.set(PROFESSOR.ZIPCODE, professor.getZipcode())
			.set(PROFESSOR.TELEPHONE, professor.getTelephone())
			.set(PROFESSOR.MOBILE, professor.getMobile())
			.set(PROFESSOR.EMAIL, professor.getEmail())
			.set(PROFESSOR.WORK, professor.getWork())
			.set(PROFESSOR.STATUS, professor.getStatus())
			.set(PROFESSOR.IMAGE, createImageLink(image.getOriginalFilename()))
			.set(PROFESSOR.BANNER, "")
			.set(PROFESSOR.ACTIVE_DEACTIVE, true)
			.execute();
			
			Professor prof = dslContext.selectFrom(PROFESSOR)
					.orderBy(PROFESSOR.PROFESSOR_ID.desc())
					.limit(1)
					.fetchOneInto(Professor.class);
			
			String password = passwordGenerator.generatePassword();
			dslContext.insertInto(ACCOUNT)
			.set(ACCOUNT.USER_ID, prof.getProfessorId())
			.set(ACCOUNT.USERNAME, professorNo)
			.set(ACCOUNT.PASSWORD, passwordEncoder.encode(password))
			.set(ACCOUNT.PASS, password)
			.set(ACCOUNT.TYPE, "PROFESSOR")
			.set(ACCOUNT.ACTIVE_DEACTIVE, true)
			.execute();
			
			Account account = dslContext.selectFrom(ACCOUNT)
					.where(ACCOUNT.USER_ID.eq(prof.getProfessorId()))
					.fetchOneInto(Account.class);
			
			return Response.builder()
					.status(201)
					.message(account.getUsername() + "-" + account.getPass())
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateImage(Integer professorId, MultipartFile image) {
		Professor professor = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
				.fetchOneInto(Professor.class);
		if (professor != null) {
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
			
			dslContext.update(PROFESSOR)
			.set(PROFESSOR.IMAGE, createImageLink(image.getOriginalFilename()))
			.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
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
					.message("Professor not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateBanner(Integer professorId, MultipartFile banner) {
		Professor professor = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
				.fetchOneInto(Professor.class);
		if (professor != null) {
			Image img = null;
			try {
				img = new Image(banner.getOriginalFilename(), banner.getContentType(), banner.getBytes());
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
			
			dslContext.update(PROFESSOR)
			.set(PROFESSOR.BANNER, createImageLink(banner.getOriginalFilename()))
			.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
			.execute();
			
			return Response.builder()
					.status(200)
					.message("Professor banner sucessfully updated")
					.timestamp(LocalDateTime.now())
					.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Professor not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateProfessorWithImage(Integer professorId, Professor professor, MultipartFile image) {
		Professor _professor = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
				.fetchOneInto(Professor.class);
		Professor mobile = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.MOBILE.eq(professor.getMobile()))
				.fetchOneInto(Professor.class);
		Professor email = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Professor.class);
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Student.class);
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(professor.getEmail())).fetchOneInto(Admin.class);
		
		if (_professor != null) {
			if (mobile != null) {
				return Response.builder()
						.status(409)
						.message("Mobile number already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else if (email != null || student != null || admin != null) {
				return Response.builder()
						.status(409)
						.message("Email already exist")
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
				
				dslContext.update(PROFESSOR)
				.set(PROFESSOR.IMAGE, createImageLink(image.getOriginalFilename()))
				.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
				.execute();
				
				if (professor.getFirstname() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.FIRSTNAME, professor.getFirstname())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getMiddlename() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.MIDDLENAME, professor.getMiddlename())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getLastname() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.LASTNAME, professor.getLastname())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getSuffix() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.SUFFIX, professor.getSuffix())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getFirstname() != null && professor.getLastname() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.FULLNAME, professor.getFirstname() + " " + professor.getLastname())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getGender() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.GENDER, professor.getGender())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getCivilStatus() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.CIVIL_STATUS, professor.getCivilStatus())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getBirthdate() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.BIRTHDATE, professor.getBirthdate())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getBirthplace() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.BIRTHPLACE, professor.getBirthplace())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getCitizenship() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.CITIZENSHIP, professor.getCitizenship())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getReligion() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.RELIGION, professor.getReligion())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getUnit() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.UNIT, professor.getUnit())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getStreet() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.STREET, professor.getStreet())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getSubdivision() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.SUBDIVISION, professor.getSubdivision())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getBarangay() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.BARANGAY, professor.getBarangay())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getCity() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.CITY, professor.getCity())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getProvince() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.PROVINCE, professor.getProvince())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getZipcode() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.ZIPCODE, professor.getZipcode())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getTelephone() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.TELEPHONE, professor.getTelephone())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getMobile() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.MOBILE, professor.getMobile())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getEmail() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.EMAIL, professor.getEmail())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getWork() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.WORK, professor.getWork())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getStatus() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.STATUS, professor.getStatus())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getActiveDeactive() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.ACTIVE_DEACTIVE, professor.getActiveDeactive())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
					
					Account account = dslContext.selectFrom(ACCOUNT)
							.where(ACCOUNT.USER_ID.eq(professorId))
							.fetchOneInto(Account.class);
					
					dslContext.update(ACCOUNT)
					.set(ACCOUNT.ACTIVE_DEACTIVE, professor.getActiveDeactive())
					.where(ACCOUNT.ACCOUNT_ID.eq(account.getAccountId()))
					.execute();
				}
				
				return Response.builder()
						.status(200)
						.message("Professor successfully updated")
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Professor not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
		
	}
	
	public Response updateProfessor(Integer professorId, Professor professor) {
		Professor _professor = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
				.fetchOneInto(Professor.class);
		Professor mobile = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.MOBILE.eq(professor.getMobile()))
				.fetchOneInto(Professor.class);
		Professor email = dslContext.selectFrom(PROFESSOR)
				.where(PROFESSOR.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Professor.class);
		Student student = dslContext.selectFrom(STUDENT)
				.where(STUDENT.EMAIL.eq(professor.getEmail()))
				.fetchOneInto(Student.class);
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(professor.getEmail())).fetchOneInto(Admin.class);
		
		
		if (_professor != null) {
			if (mobile != null) {
				return Response.builder()
						.status(409)
						.message("Mobile number already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else if (email != null || student != null || admin != null) {
				return Response.builder()
						.status(409)
						.message("Email already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				if (professor.getFirstname() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.FIRSTNAME, professor.getFirstname())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getMiddlename() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.MIDDLENAME, professor.getMiddlename())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getLastname() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.LASTNAME, professor.getLastname())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getSuffix() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.SUFFIX, professor.getSuffix())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getFirstname() != null && professor.getLastname() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.FULLNAME, professor.getFirstname() + " " + professor.getLastname())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getGender() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.GENDER, professor.getGender())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getCivilStatus() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.CIVIL_STATUS, professor.getCivilStatus())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getBirthdate() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.BIRTHDATE, professor.getBirthdate())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getBirthplace() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.BIRTHPLACE, professor.getBirthplace())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getCitizenship() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.CITIZENSHIP, professor.getCitizenship())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getReligion() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.RELIGION, professor.getReligion())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getUnit() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.UNIT, professor.getUnit())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getStreet() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.STREET, professor.getStreet())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getSubdivision() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.SUBDIVISION, professor.getSubdivision())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getBarangay() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.BARANGAY, professor.getBarangay())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getCity() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.CITY, professor.getCity())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getProvince() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.PROVINCE, professor.getProvince())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getZipcode() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.ZIPCODE, professor.getZipcode())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getTelephone() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.TELEPHONE, professor.getTelephone())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getMobile() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.MOBILE, professor.getMobile())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getEmail() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.EMAIL, professor.getEmail())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getWork() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.WORK, professor.getWork())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getStatus() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.STATUS, professor.getStatus())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
				}
				if (professor.getActiveDeactive() != null) {
					dslContext.update(PROFESSOR)
					.set(PROFESSOR.ACTIVE_DEACTIVE, professor.getActiveDeactive())
					.where(PROFESSOR.PROFESSOR_ID.eq(professorId))
					.execute();
					
					Account account = dslContext.selectFrom(ACCOUNT)
							.where(ACCOUNT.USER_ID.eq(professorId))
							.fetchOneInto(Account.class);
					
					dslContext.update(ACCOUNT)
					.set(ACCOUNT.ACTIVE_DEACTIVE, professor.getActiveDeactive())
					.where(ACCOUNT.ACCOUNT_ID.eq(account.getAccountId()))
					.execute();
				}
				
				return Response.builder()
						.status(200)
						.message("Professor successfully updated")
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Professor not found")
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
