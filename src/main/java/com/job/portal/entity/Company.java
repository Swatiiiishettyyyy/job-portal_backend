package com.job.portal.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "company_name")
	private String companyName;

	@Column(name = "location")
	private String location;

	@Column(name = "salary_package")
	private String salaryPackage;

	@Column(name = "cgpa_criteria")
	private float cgpaCriteria;

	@Column(name = "over_all_backlogs_criteria")
	private int overAllbacklogsCriteria;
	
	@Column(name = "active_backlogs_criteria")
	private int activeBacklogsCriteria;

	@Column(name = "sslc_criteria")
	private float sslcCriteria;

	@Column(name = "hsc_criteria")
	private float hscCriteria;

	@Column(name = "job_role")
	private String jobRole;

	@Column(name = "job_description", length = 10000)
	private String jobDescription;

	@Column(name = "department")
	private String department;

	@Column(name = "first_round")
	private boolean firstRound;

	@Column(name = "second_round")
	private boolean secondRound;

	@Column(name = "third_round")
	private boolean thirdRound;

	@Column(name = "fourth_round")
	private boolean fourthRound;

	@Column(name = "fifth_round")
	private boolean fifthRound;

	@Column(name = "first_round_name")
	private String firstRoundName;

	@Column(name = "second_round_name")
	private String secondRoundName;

	@Column(name = "third_round_name")
	private String thirdRoundName;

	@Column(name = "fourth_round_name")
	private String fourthRoundName;

	@Column(name = "fifth_round_name")
	private String fifthRoundName;

	@Column(name = "visibility")
	private boolean visibility;
	
	@JsonIgnore
	@OneToMany(cascade = { CascadeType.PERSIST,CascadeType.REMOVE})
	private List<StudentApplication> studentApplications = new ArrayList<>();

}
