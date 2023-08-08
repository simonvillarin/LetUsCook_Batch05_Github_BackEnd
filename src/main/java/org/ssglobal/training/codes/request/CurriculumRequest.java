package org.ssglobal.training.codes.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumRequest {
	private String program;
	private String subject;
	private String yearLevel;
	private String sem;
	private Boolean activeDeactive;
}
