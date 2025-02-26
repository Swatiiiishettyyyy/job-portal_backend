package com.job.portal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationStatusChangeDto {

	private String roundStatus;
	
	private Integer round;

	private long adminId;
	
	private long studentApplicationId; 
}
