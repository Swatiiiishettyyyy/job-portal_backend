package com.job.portal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentVisibilityChangeDto {
	
	private long studentId;
	
	private boolean visibility;
}
