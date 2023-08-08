package org.ssglobal.training.codes.request;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectRequest {
	private String programTitle;
	private String subjectCode;
	private String subjectTitle;
	private BigDecimal units;
	private List<String> preRequisites;
	private String type;
	private Boolean activeDeactive;
}
