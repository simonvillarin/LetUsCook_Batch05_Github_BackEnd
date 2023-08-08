package org.ssglobal.training.codes.dto;

import java.math.BigDecimal;
import java.util.List;

import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDTO {
	private Integer subjectId;
	private String subjectCode;
	private String subjectTitle;
	private BigDecimal units;
	private List<Subject> preRequisites;
	private String type;
	private Boolean activeDeactive;
}
