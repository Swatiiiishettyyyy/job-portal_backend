package com.job.portal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.portal.entity.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
	
	Admin findByUserNameAndPassword(String userName,String password);
	
	Admin findById(long adminId);
}
