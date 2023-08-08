package org.ssglobal.training.codes.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class EvalRequest {
	private String section;
	private Integer subjectId;
	private Boolean activeDeactive;
}
