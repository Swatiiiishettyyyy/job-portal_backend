package com.job.portal.exception;

import lombok.Data;

@Data
public class ErrorResponse {

	private String status;
	private String message;
	private long timeStamp;

}