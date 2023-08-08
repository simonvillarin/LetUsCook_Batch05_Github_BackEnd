package org.ssglobal.training.codes.dto;

import java.time.LocalDate;
import java.time.LocalTime;

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
public class AttendanceStudentDTO {
	private Integer attendanceId;
	private Student student;
	private Subject subject;
	private String sem;
	private String yearLevel;
	private String academicYear;
	private LocalDate date;
	private LocalTime time;
	private String status;
	private Boolean activeDeactive;
}