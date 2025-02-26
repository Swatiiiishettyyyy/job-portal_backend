package com.job.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.portal.entity.Student;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
	
	Student findByUserNameAndDateOfBirth(String userName,String password);

	boolean existsByUserName(String userName);

	boolean existsByEmail(String email);
	
}
