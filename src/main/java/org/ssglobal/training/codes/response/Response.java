package org.ssglobal.training.codes.response;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response {
	private Integer status;
	private String message;
	private LocalDateTime timestamp;
}
