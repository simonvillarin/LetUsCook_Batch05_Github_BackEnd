package org.ssglobal.training.codes.dto;


import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Schedule;
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
public class SubjectDetailDTO {
	private Integer historyId;
	private Student student;
	private Professor professor;
	private Subject subject;
	private String sem;
	private String yearLevel;
	private String academicYear;
	private String section;
	private Schedule sched;
	private Boolean activeDeactive;
	
}