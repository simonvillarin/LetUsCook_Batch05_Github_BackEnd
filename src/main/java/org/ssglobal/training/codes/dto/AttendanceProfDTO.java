package org.ssglobal.training.codes.dto;

import java.time.LocalDate;

import org.ssglobal.training.codes.tables.pojos.Professor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceProfDTO {
	private Integer attendanceId;
	private Professor professor;
	private LocalDate date;
	private Boolean status;
}