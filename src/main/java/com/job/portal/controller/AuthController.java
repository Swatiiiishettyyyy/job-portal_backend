package com.job.portal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.job.portal.dto.LoggedUserDto;
import com.job.portal.dto.LoginDto;
import com.job.portal.service.AuthService;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin("*")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public ResponseEntity<LoggedUserDto> login(@RequestBody LoginDto loginDto) {
		 LoggedUserDto login = authService.login(loginDto);
		 return new ResponseEntity<LoggedUserDto>(login,HttpStatus.OK);
	}
}
