package org.ssglobal.training.codes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumDTO {
	 private Integer curriculumId;
	 private String program;
	 private String subject;
	 private String sem;
	 private String yearLevel;
	 private Boolean activeDeactive;
}