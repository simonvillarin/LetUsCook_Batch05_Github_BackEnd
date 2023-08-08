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
public class GradeDTO {
	private List<GradesDTO> firstYearFirstTerm;
	private List<GradesDTO> firstYearSecondTerm;
	private List<GradesDTO> secondYearFirstTerm;
	private List<GradesDTO> secondYearSecondTerm;
	private List<GradesDTO> thirdYearFirstTerm;
	private List<GradesDTO> thirdYearSecondTerm;
	private List<GradesDTO> fourthYearFirstTerm;
	private List<GradesDTO> fourthYearSecondTerm;
}
