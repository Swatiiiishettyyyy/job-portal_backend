package com.job.portal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDto {

	private String userName;
	
	private String dateOfBirth;
	
	private String firstName;

	private String lastName;
	
	private String email;
	
	private float cgpa;
	
	private int overallBacklogs;
	
	private int activeBacklogs;
	
	private String phoneNumber;
	
	private float sslcMarks;
	
	private float hscMarks;
	
	private String department;
	
	private long userId;
	
	private Long profilePictureId;
	
	private Long resumeId;
}
