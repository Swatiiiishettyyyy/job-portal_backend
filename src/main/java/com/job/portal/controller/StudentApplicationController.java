package com.job.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job.portal.dto.ApplicationStatusChangeDto;
import com.job.portal.dto.StudentApplicationDto;
import com.job.portal.entity.StudentApplication;
import com.job.portal.service.StudentApplicationService;

@RestController
@RequestMapping("/api/v1/application")
@CrossOrigin("*")
public class StudentApplicationController {

	@Autowired
	StudentApplicationService applicationService;

	@PostMapping
	public ResponseEntity<StudentApplication> createStudentApplication(
			@RequestBody StudentApplicationDto studentApplicationDto) {
		StudentApplication studentApplication = applicationService.createStudentApplication(studentApplicationDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(studentApplication);
	}

	@GetMapping("/{studentApplicationId}")
	public ResponseEntity<StudentApplication> getStudentApplication(@PathVariable long studentApplicationId) {
		StudentApplication studentApplication = applicationService.getStudentApplication(studentApplicationId);
		return ResponseEntity.ok(studentApplication);
	}

	@GetMapping
	public ResponseEntity<List<StudentApplication>> searchStudentApplications(
			@RequestParam(required = false) Long companyId, @RequestParam(required = true) Long userId) {
		List<StudentApplication> studentApplications = applicationService.searchStudentApplications(companyId, userId);
		return ResponseEntity.ok(studentApplications);
	}

	@PutMapping("/status")
	public ResponseEntity<StudentApplication> updateStudentApplicationStatus(@RequestBody ApplicationStatusChangeDto statusChangeDto) {
		StudentApplication updatedStudentApplication = applicationService.updateStatus(statusChangeDto);
		return new ResponseEntity<>(updatedStudentApplication, HttpStatus.OK);
	}
	
	@PutMapping("/apply/{studentApplicationId}/{studentId}")
	public ResponseEntity<StudentApplication> applyCompany(@PathVariable("studentApplicationId") long studentAplicationId,@PathVariable("studentId") long studentId) {
		StudentApplication application = applicationService.applyCompany(studentAplicationId,studentId);
		return new ResponseEntity<>(application, HttpStatus.OK);
	}
	
	@PostMapping("/sendEmailToAll")
    public ResponseEntity<String> sendEmailForStudents(@RequestParam(required = true) long adminId, @RequestParam(required = true) long companyId) {
		String email = applicationService.sendEmailForStudents(adminId, companyId);
		return ResponseEntity.ok(email);
	}
	
	@PostMapping("/sendEmailToSpecificUser")
	public ResponseEntity<String> sendEmailSpecificUser(@RequestParam(required = true) long adminId, @RequestParam(required = true) long studentApplicationId) {
		String email = applicationService.sendEmailSpecificUser(adminId, studentApplicationId);
		return ResponseEntity.ok(email);
	}

}
