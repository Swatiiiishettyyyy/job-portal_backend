package com.job.portal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job.portal.dto.LoggedUserDto;
import com.job.portal.dto.LoginDto;
import com.job.portal.entity.Admin;
import com.job.portal.entity.Student;
import com.job.portal.exception.AppException;
import com.job.portal.exception.NotFoundException;
import com.job.portal.repository.AdminRepository;
import com.job.portal.repository.StudentRepository;

@Service
public class AuthService {
	
	@Autowired
	AdminRepository adminRepo;
	
	@Autowired
	StudentRepository studentRepo;
	
	public LoggedUserDto login(LoginDto loginDto) {
	    try {
	        Admin admin = adminRepo.findByUserNameAndPassword(loginDto.getUserName(), loginDto.getPassword());
	        if (admin != null) {
	        	LoggedUserDto loggedUserDto = new LoggedUserDto();
	        	loggedUserDto.setId(admin.getId());
	        	loggedUserDto.setFirstName(admin.getFirstName());
	        	loggedUserDto.setLastName(admin.getLastName());
	        	loggedUserDto.setUserName(admin.getUserName());
	        	loggedUserDto.setEmail(admin.getEmail());
	        	loggedUserDto.setRole("ADMIN");
	            return loggedUserDto;
	        } else {
	            Student student = studentRepo.findByUserNameAndDateOfBirth(loginDto.getUserName(), loginDto.getPassword());
	            if (student != null && student.isVisibility()) {
	            	LoggedUserDto loggedUserDto = new LoggedUserDto();
	            	loggedUserDto.setId(student.getId());
	            	loggedUserDto.setFirstName(student.getFirstName());
	            	loggedUserDto.setLastName(student.getLastName());
	            	loggedUserDto.setUserName(student.getUserName());
	            	loggedUserDto.setEmail(student.getEmail());
	            	loggedUserDto.setRole("STUDENT");
	                return loggedUserDto;
	            } else {
	                throw new NotFoundException("Unauthorized entry");
	            }
	        }

	    } catch (Exception e) {
	        throw new AppException(e.getMessage());
	    }
	}
}
