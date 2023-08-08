package org.ssglobal.training.codes.dto;

import java.util.List;

import org.ssglobal.training.codes.tables.pojos.Student;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentHistoryDTO {
	private Integer historyId;
	private Student student;
	private List<SchedDTO> schedules;
	private String yearLevel;
	private String sem;
	private String academicYear;
	private Boolean activeDeactive;
}
