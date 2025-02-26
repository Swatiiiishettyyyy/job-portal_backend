package com.job.portal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoggedUserDto {
	
	private long id;

	private String userName;

	private String firstName;

	private String lastName;

	private String email;
	
	private String role;
	
}
