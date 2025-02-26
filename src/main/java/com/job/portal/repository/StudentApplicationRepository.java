package com.job.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.portal.entity.StudentApplication;

@Repository
public interface StudentApplicationRepository extends JpaRepository<StudentApplication, Long> {
	
	List<StudentApplication> findAllByCompanyId(long comapanyId);
	
	boolean existsByStudentId(long studentId);

	StudentApplication findByStudentIdAndCompanyId(long id, Long id2);

	void deleteAllByIdIn(List<Long> removeApplications);

	
	
}
