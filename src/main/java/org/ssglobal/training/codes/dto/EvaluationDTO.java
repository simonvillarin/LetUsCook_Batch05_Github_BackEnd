package org.ssglobal.training.codes.dto;

import org.ssglobal.training.codes.tables.pojos.Student;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationDTO {
	private Integer evaluationId;
	private Subject subject;
	private Student student;
	private String[] answers; 
}