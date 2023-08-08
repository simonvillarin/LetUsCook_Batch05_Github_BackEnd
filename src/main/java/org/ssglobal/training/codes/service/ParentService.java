package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Image;
import org.ssglobal.training.codes.tables.pojos.Parent;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParentService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Parent PARENT = org.ssglobal.training.codes.tables.Parent.PARENT;
	private final org.ssglobal.training.codes.tables.Image IMAGE = org.ssglobal.training.codes.tables.Image.IMAGE;
	
	public List<Parent> getAllParents() {
		return dslContext.selectFrom(PARENT)
				.fetchInto(Parent.class);
	}
	
	public Parent getParentById(Integer parentId) {
		Parent parent = dslContext.selectFrom(PARENT)
				.where(PARENT.PARENT_ID.eq(parentId))
				.fetchOneInto(Parent.class);
		System.out.println(parentId + "parentId");
		if (parent != null) {
			return parent;
		} else {
			throw new RuntimeException("Parent not found");
		}
	}
	
	public Response updateParentImage(Integer parentId, MultipartFile image) {
		Parent _parent = dslContext.selectFrom(PARENT)
				.where(PARENT.PARENT_ID.eq(parentId))
				.fetchOneInto(Parent.class);
		if (_parent != null) {
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
			
			dslContext.update(PARENT)
			.set(PARENT.IMAGE, createImageLink(image.getOriginalFilename()))
			.where(PARENT.PARENT_ID.eq(parentId))
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
					.message("Parent not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateParentBanner(Integer parentId, MultipartFile banner) {
		Parent _parent = dslContext.selectFrom(PARENT)
				.where(PARENT.PARENT_ID.eq(parentId))
				.fetchOneInto(Parent.class);
		if (_parent != null) {
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
			
			dslContext.update(PARENT)
			.set(PARENT.BANNER, createImageLink(banner.getOriginalFilename()))
			.where(PARENT.PARENT_ID.eq(parentId))
			.execute();
			
			return Response.builder()
					.status(200)
					.message("Parent banner sucessfully updated")
					.timestamp(LocalDateTime.now())
					.build();
			
		} else {
			return Response.builder()
					.status(404)
					.message("Parent not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateParentWithImage(Integer parentId, Parent parent, MultipartFile file) {
		Parent _parent = dslContext.selectFrom(PARENT)
				.where(PARENT.PARENT_ID.eq(parentId))
				.fetchOneInto(Parent.class);
		if (_parent != null) {
			Image image = null;
			try {
				image = new Image(file.getOriginalFilename(), file.getContentType(), file.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (image != null) {
				Image _image = dslContext.selectFrom(IMAGE)
						.where(IMAGE.FILENAME.eq(image.getFilename()))
						.fetchOneInto(Image.class);
				if (_image == null) {
					dslContext.insertInto(IMAGE)
					.set(IMAGE.FILENAME, image.getFilename())
					.set(IMAGE.MIME_TYPE, image.getMimeType())
					.set(IMAGE.DATA, image.getData())
					.execute();
				}
			}
			
			dslContext.update(PARENT)
			.set(PARENT.IMAGE, createImageLink(file.getOriginalFilename()))
			.where(PARENT.PARENT_ID.eq(parentId))
			.execute();
			
			 if (parent.getFirstname() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.FIRSTNAME, parent.getFirstname())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getMiddlename() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.MIDDLENAME, parent.getMiddlename())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getLastname() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.LASTNAME, parent.getLastname())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getSuffix() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.SUFFIX, parent.getSuffix())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getAddress() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.ADDRESS, parent.getAddress())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getContact() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.CONTACT, parent.getContact())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 } 
			 if (parent.getEmail() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.CONTACT, parent.getEmail())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getRelationship() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.RELATIONSHIP, parent.getRelationship())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getActiveDeactive() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.ACTIVE_DEACTIVE, parent.getActiveDeactive())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 String imagee = createImageLink(file.getOriginalFilename());
			 return Response.builder()
						.status(200)
						.message(imagee)
						.timestamp(LocalDateTime.now())
						.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Parent not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response updateParent(Integer parentId, Parent parent) {
		Parent _parent = dslContext.selectFrom(PARENT)
				.where(PARENT.PARENT_ID.eq(parentId))
				.fetchOneInto(Parent.class);
		if (_parent != null) {
			 if (parent.getFirstname() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.FIRSTNAME, parent.getFirstname())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getMiddlename() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.MIDDLENAME, parent.getMiddlename())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getLastname() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.LASTNAME, parent.getLastname())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getSuffix() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.SUFFIX, parent.getSuffix())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getAddress() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.ADDRESS, parent.getAddress())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getContact() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.CONTACT, parent.getContact())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getRelationship() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.RELATIONSHIP, parent.getRelationship())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getEmail() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.EMAIL, parent.getEmail())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 if (parent.getActiveDeactive() != null) {
				 dslContext.update(PARENT)
				 .set(PARENT.ACTIVE_DEACTIVE, parent.getActiveDeactive())
				 .where(PARENT.PARENT_ID.eq(parentId))
				 .execute();
			 }
			 return Response.builder()
						.status(200)
						.message("Parent sucessfully created")
						.timestamp(LocalDateTime.now())
						.build();
		} else {
			return Response.builder()
					.status(404)
					.message("Parent not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	
	public Response deleteParent(Integer parentId) {
		Parent parent = dslContext.selectFrom(PARENT)
				.where(PARENT.PARENT_ID.eq(parentId))
				.fetchOneInto(Parent.class);
		if(parent != null) {
			dslContext.delete(PARENT)
			.where(PARENT.PARENT_ID.eq(parentId))
			.execute();
			return Response.builder()
					.status(201)
					.message("Parent successfully deleted")
					.timestamp(LocalDateTime.now())
					.build();
		}else {
			return Response.builder()
					.status(404)
					.message("Parent not found")
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
