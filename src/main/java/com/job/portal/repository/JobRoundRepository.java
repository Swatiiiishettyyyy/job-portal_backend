package com.job.portal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.job.portal.entity.JobRounds;

@Repository
public interface JobRoundRepository extends JpaRepository<JobRounds, Long>{

	void deleteAllByIdIn(List<Long> listOfDeleteJobRounds);

}
