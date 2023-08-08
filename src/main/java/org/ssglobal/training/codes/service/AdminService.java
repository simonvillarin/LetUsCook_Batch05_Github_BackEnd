package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.ssglobal.training.codes.request.AdminRequest;
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
public class AdminService {
	private final DSLContext dslContext;
	private final PasswordEncoder passwordEncoder;
	private final org.ssglobal.training.codes.tables.Admin ADMIN = org.ssglobal.training.codes.tables.Admin.ADMIN;
	private final org.ssglobal.training.codes.tables.Account ACCOUNT = org.ssglobal.training.codes.tables.Account.ACCOUNT;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Parent PARENT = org.ssglobal.training.codes.tables.Parent.PARENT;
	private final org.ssglobal.training.codes.tables.Image IMAGE = org.ssglobal.training.codes.tables.Image.IMAGE;
	
	public List<Admin> getAllAdmin() {
		return dslContext.selectFrom(ADMIN)
				.fetchInto(Admin.class);
	}
	
	public Admin getAdminById(Integer adminId) {
		Admin admin = dslContext.selectFrom(ADMIN)
				.where(ADMIN.ADMIN_ID.eq(adminId))
				.fetchOneInto(Admin.class);
		if (admin != null) {
			return admin;
		} else {
			throw new RuntimeException("Admin not found");
		}
	}
	
	public Response addAdmin(AdminRequest admin, MultipartFile image) {
		Admin telephone = dslContext.selectFrom(ADMIN)
				.where(ADMIN.TELEPHONE.eq(admin.getTelephone()))
				.fetchOneInto(Admin.class);
		Admin mobile = dslContext.selectFrom(ADMIN)
				.where(ADMIN.MOBILE.eq(admin.getMobile()))
				.fetchOneInto(Admin.class);
		Admin email = dslContext.selectFrom(ADMIN)
				.where(ADMIN.EMAIL.eq(admin.getEmail()))
				.fetchOneInto(Admin.class);
		Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(admin.getEmail()))
				.fetchOneInto(Professor.class);
		Student student = dslContext.selectFrom(STUDENT).where(STUDENT.EMAIL.eq(admin.getEmail())).fetchOneInto(Student.class);
		Parent parent = dslContext.selectFrom(PARENT).where(PARENT.EMAIL.eq(admin.getEmail())).fetchOneInto(Parent.class);
		
		if (telephone != null) {
			return Response.builder()
					.status(409)
					.message("Telephone number already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else if (mobile != null) {
			return Response.builder()
					.status(409)
					.message("Mobile number already exist")
					.timestamp(LocalDateTime.now())
					.build();
		} else if (email != null || professor != null || student != null || parent != null) {
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

			dslContext.insertInto(ADMIN)
			.set(ADMIN.FIRSTNAME, admin.getFirstname())
			.set(ADMIN.MIDDLENAME, admin.getMiddlename())
			.set(ADMIN.LASTNAME, admin.getLastname())
			.set(ADMIN.SUFFIX, admin.getSuffix())
			.set(ADMIN.GENDER, admin.getGender())
			.set(ADMIN.CIVIL_STATUS, admin.getCivilStatus())
			.set(ADMIN.BIRTHDATE, admin.getBirthdate())
			.set(ADMIN.BIRTHPLACE, admin.getBirthplace())
			.set(ADMIN.CITIZENSHIP, admin.getCitizenship())
			.set(ADMIN.RELIGION, admin.getReligion())
			.set(ADMIN.UNIT, admin.getUnit())
			.set(ADMIN.STREET, admin.getStreet())
			.set(ADMIN.SUBDIVISION, admin.getSubdivision())
			.set(ADMIN.BARANGAY, admin.getBarangay())
			.set(ADMIN.CITY, admin.getCity())
			.set(ADMIN.PROVINCE, admin.getProvince())
			.set(ADMIN.ZIPCODE, admin.getZipcode())
			.set(ADMIN.TELEPHONE, admin.getTelephone())
			.set(ADMIN.MOBILE, admin.getMobile())
			.set(ADMIN.EMAIL, admin.getEmail())
			.set(ADMIN.IMAGE, createImageLink(image.getOriginalFilename()))
			.execute();
			
			Admin _admin = dslContext.selectFrom(ADMIN)
					.where(ADMIN.EMAIL.eq(admin.getEmail()))
					.fetchOneInto(Admin.class);
			
			dslContext.insertInto(ACCOUNT)
			.set(ACCOUNT.USER_ID, _admin.getAdminId())
			.set(ACCOUNT.USERNAME, admin.getUsername())
			.set(ACCOUNT.PASSWORD, passwordEncoder.encode(admin.getPassword()))
			.set(ACCOUNT.PASS, admin.getPassword())
			.set(ACCOUNT.TYPE, "ADMIN")
			.set(ACCOUNT.ACTIVE_DEACTIVE, true)
			.execute();	
			
			return Response.builder()
					.status(201)
					.message("Admin successfully created")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateImage(Integer adminId, MultipartFile image) {
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
		dslContext.update(ADMIN)
		.set(ADMIN.IMAGE, createImageLink(image.getOriginalFilename()))
		.where(ADMIN.ADMIN_ID.eq(adminId))
		.execute();
		
		String imagee = createImageLink(image.getOriginalFilename());
		
		return Response.builder()
				.status(201)
				.message(imagee)
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateBanner(Integer adminId, MultipartFile banner) {
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
		dslContext.update(ADMIN)
		.set(ADMIN.BANNER_IMAGE, createImageLink(banner.getOriginalFilename()))
		.where(ADMIN.ADMIN_ID.eq(adminId))
		.execute();
		
		return Response.builder()
				.status(201)
				.message("Admin banner successfully updated")
				.timestamp(LocalDateTime.now())
				.build();
	}
	
	public Response updateAdmin(Integer adminId, AdminRequest admin) {
		Admin _adminId = dslContext.selectFrom(ADMIN)
				.where(ADMIN.ADMIN_ID.eq(adminId))
				.fetchOneInto(Admin.class);
		Admin telephone = dslContext.selectFrom(ADMIN)
				.where(ADMIN.TELEPHONE.eq(admin.getTelephone()))
				.fetchOneInto(Admin.class);
		Admin mobile = dslContext.selectFrom(ADMIN)
				.where(ADMIN.MOBILE.eq(admin.getMobile()))
				.fetchOneInto(Admin.class);
		Admin email = dslContext.selectFrom(ADMIN)
				.where(ADMIN.EMAIL.eq(admin.getEmail()))
				.fetchOneInto(Admin.class);
		Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(admin.getEmail()))
				.fetchOneInto(Professor.class);
		Student student = dslContext.selectFrom(STUDENT).where(STUDENT.EMAIL.eq(admin.getEmail())).fetchOneInto(Student.class);
		Parent parent = dslContext.selectFrom(PARENT).where(PARENT.EMAIL.eq(admin.getEmail())).fetchOneInto(Parent.class);
		
		if (_adminId != null) {
			if (telephone != null) {
				return Response.builder()
						.status(409)
						.message("Telephone number already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else if (mobile != null) {
				return Response.builder()
						.status(409)
						.message("Mobile number already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else if (email != null || professor != null || student != null || parent != null) {
				return Response.builder()
						.status(409)
						.message("Email already exist")
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				if (admin.getFirstname() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.FIRSTNAME, admin.getFirstname())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getMiddlename() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.MIDDLENAME, admin.getMiddlename())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				} 
				if (admin.getLastname() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.LASTNAME, admin.getLastname())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getSuffix() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.SUFFIX, admin.getSuffix())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getGender() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.GENDER, admin.getGender())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getCivilStatus() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.CIVIL_STATUS, admin.getCivilStatus())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getBirthdate() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.BIRTHDATE, admin.getBirthdate())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getBirthplace() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.BIRTHPLACE, admin.getBirthplace())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getCitizenship() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.CITIZENSHIP, admin.getCitizenship())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getReligion() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.RELIGION, admin.getReligion())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getUnit() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.UNIT, admin.getUnit())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getStreet() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.STREET, admin.getStreet())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getSubdivision() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.SUBDIVISION, admin.getSubdivision())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getBarangay() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.BARANGAY, admin.getBarangay())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getCity() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.CITY, admin.getCity())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getProvince() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.PROVINCE, admin.getProvince())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getZipcode() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.ZIPCODE, admin.getZipcode())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getTelephone() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.TELEPHONE, admin.getTelephone())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				}
				if (admin.getMobile() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.MOBILE, admin.getMobile())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				} 
				if (admin.getEmail() != null) {
					dslContext.update(ADMIN)
					.set(ADMIN.EMAIL, admin.getEmail())
					.where(ADMIN.ADMIN_ID.eq(adminId))
					.execute();
				} 
				if (admin.getPassword() != null) {			
					Account account = dslContext.selectFrom(ACCOUNT)
							.where(ACCOUNT.USER_ID.eq(adminId))
							.fetchOneInto(Account.class);
					
					dslContext.update(ACCOUNT)
				    .set(ACCOUNT.PASSWORD, passwordEncoder.encode(admin.getPassword()))
				    .set(ACCOUNT.PASS, admin.getPassword())
				    .where(ACCOUNT.ACCOUNT_ID.eq(account.getAccountId()))
				    .execute();
				}
				if (admin.getActiveDeactive() != null) {
					Account account = dslContext.selectFrom(ACCOUNT)
							.where(ACCOUNT.USER_ID.eq(adminId))
							.fetchOneInto(Account.class);
					
					dslContext.update(ACCOUNT)
					.set(ACCOUNT.ACTIVE_DEACTIVE, admin.getActiveDeactive())
					.where(ACCOUNT.ACCOUNT_ID.eq(account.getAccountId()))
					.execute();
				}
				return Response.builder()
						.status(200)
						.message("Admin successfully updated")
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Admin not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	 private String createImageLink(String filename) {
			return ServletUriComponentsBuilder.fromCurrentRequest()
					.replacePath("/lms/image/" + filename)
					.toUriString();
	 }
}
