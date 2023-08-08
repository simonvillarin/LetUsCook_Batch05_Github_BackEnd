package org.ssglobal.training.codes.request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramRequest {
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
	private String[] majors;
	private String[] minors;
	private String[] electives;
	private Boolean status;
}
