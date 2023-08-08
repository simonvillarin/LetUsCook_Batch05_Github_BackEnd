package org.ssglobal.training.codes.dto;

import org.ssglobal.training.codes.tables.pojos.Program;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SectionDTO {
	private Integer sectionId;
	private Program program;
	private String sectionName;
	private String section;
	private Boolean activeDeactive;
}
