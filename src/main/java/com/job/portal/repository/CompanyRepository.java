package com.job.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.portal.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>{
	
	List<Company> findAllByCompanyNameContainingIgnoreCase(String companyName);

	List<Company> findAllByCompanyNameContainingIgnoreCaseOrLocationContainingIgnoreCase(String companyName,
			String location);
	
	List<Company> findAllByVisibility(boolean visibility);
}
