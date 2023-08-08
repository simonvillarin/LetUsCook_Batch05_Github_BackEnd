package org.ssglobal.training.codes.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.ssglobal.training.codes.tables.pojos.Professor;
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
public class GradesDTO {
	private Integer gradeId;
	private Professor professor;
	private Student student;
	private Subject subject;
	private BigDecimal prelim;
	private BigDecimal midterm;
	private BigDecimal finals;
	private String yearLevel;
	private String sem;
	private String academicYear;
	private String comment;
	private String remarks;
	private LocalDate dateModified;
	private Boolean status;
}
