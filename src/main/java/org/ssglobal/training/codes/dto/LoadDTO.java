package org.ssglobal.training.codes.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoadDTO {
	private List<Integer> loadId;
	private List<ScheduleDTO> schedules;
}
