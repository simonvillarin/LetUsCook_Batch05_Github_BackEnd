package org.ssglobal.training.codes.dto;

import java.time.LocalTime;

import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Room;
import org.ssglobal.training.codes.tables.pojos.Section;
import org.ssglobal.training.codes.tables.pojos.Subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SchedDTO {
	private Integer schedId;
	private Subject subject;
	private String day;
	private LocalTime startTime;
	private LocalTime endTime;
	private Section section;
	private Room room;
	private Boolean activeDeactive;
	private Professor professor;
}