package org.ssglobal.training.codes.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDTO {
	private Integer programId;
	private String programCode;
	private String programTitle;
	private BigDecimal firstYearFirstSemMin;
	private BigDecimal firstYearFirstSemMax;
	private BigDecimal firstYearSecondSemMin;
	private BigDecimal firstYearSecondSemMax;
	private BigDecimal secondYearFirstSemMin;
	private BigDecimal secondYearFirstSemMax;
	private BigDecimal secondYearSecondSemMin;
	private BigDecimal secondYearSecondSemMax;
	private BigDecimal thirdYearFirstSemMin;
	private BigDecimal thirdYearFirstSemMax;
	private BigDecimal thirdYearSecondSemMin;
	private BigDecimal thirdYearSecondSemMax;
	private BigDecimal fourthYearFirstSemMin;
	private BigDecimal fourthYearFirstSemMax;
	private BigDecimal fourthYearSecondSemMin;
	private BigDecimal fourthYearSecondSemMax;
	private List<SubjectDTO> majors;
	private List<SubjectDTO> minors;
	private List<SubjectDTO> electives;
	private Boolean status;
}
