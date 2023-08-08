package org.ssglobal.training.codes.service;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.tables.pojos.Image;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Image IMAGE = org.ssglobal.training.codes.tables.Image.IMAGE;
	
	public Image getImage(String filename) {
		Image image = dslContext.selectFrom(IMAGE)
				.where(IMAGE.FILENAME.eq(filename))
				.fetchOneInto(Image.class);
		if (image != null) {
			return image;
		} else {
			throw new RuntimeException("Image not found");
		}
	}
}
