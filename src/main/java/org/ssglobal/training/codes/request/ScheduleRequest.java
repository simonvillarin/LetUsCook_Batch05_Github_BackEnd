package org.ssglobal.training.codes.request;

import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
	private String subject;
	private List<String> days;
	private LocalTime startTime;
	private LocalTime endTime;
	private String section;
	private String room;
	private Boolean activeDeactive;
	private Integer professorId;
}
