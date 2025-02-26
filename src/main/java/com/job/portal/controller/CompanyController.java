package com.job.portal.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.job.portal.dto.CompanyDto;
import com.job.portal.dto.CompanyVisibilityChangeDto;
import com.job.portal.dto.MessageDto;
import com.job.portal.entity.Company;
import com.job.portal.service.CompanyService;

@RestController
@RequestMapping("/api/v1/company")
@CrossOrigin("*")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@PostMapping
	public ResponseEntity<Company> createCompany(@RequestBody CompanyDto companyDto) {
		Company company = companyService.createCompany(companyDto);
		return new ResponseEntity<>(company, HttpStatus.CREATED);
	}

	@PutMapping("/{companyId}")
	public ResponseEntity<Company> updateCompany(@PathVariable("companyId") long companyId,
			@RequestBody CompanyDto companyDto) {
		Company updatedCompany = companyService.updateCompany(companyId,
				companyDto);
		return new ResponseEntity<>(updatedCompany, HttpStatus.OK);
	}

	@PutMapping("/visibility/{adminId}")
	public ResponseEntity<Company> updateVisibility(@PathVariable("adminId") long adminId,
			@RequestBody CompanyVisibilityChangeDto companyVisibilityChangeDto) {
		Company updatedCompany = companyService.updateVisibility(adminId,
				companyVisibilityChangeDto);
		return ResponseEntity.ok(updatedCompany);
	}

	@GetMapping("/{companyId}")
	public ResponseEntity<Company> getCompany(@PathVariable("companyId") long companyId) {
		Company company = companyService.getCompany(companyId);
		return ResponseEntity.ok(company);
	}

	@GetMapping
	public ResponseEntity<List<Company>> getAllCompany(@RequestParam(required = false) String searchField) {
		List<Company> allCompany;
		if (searchField != null && !searchField.isEmpty()) {
			allCompany = companyService.getAllComapany(searchField);
		} else {
			allCompany = companyService.getAllComapany();
		}
		return ResponseEntity.ok(allCompany);
	}
	
	@GetMapping("/active")
	public ResponseEntity<List<Company>> getAllActiveCompanies() {
		List<Company> activeCompanies = companyService.getAllActiveCompanies();
		return new ResponseEntity<>(activeCompanies, HttpStatus.OK);
	}

	@DeleteMapping("/{companyId}/{adminId}")
	public ResponseEntity<MessageDto> deleteCompany(@PathVariable("companyId") long companyId,
			@PathVariable("adminId") long adminId) {
		companyService.deleteCompany(companyId, adminId);
		MessageDto company = new MessageDto("Company details deleted successfully", LocalDateTime.now());
		return new ResponseEntity<MessageDto>(company, HttpStatus.OK);
	}
	
	
}
