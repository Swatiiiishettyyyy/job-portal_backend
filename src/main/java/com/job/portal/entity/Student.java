package com.job.portal.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Student {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "user_name", unique = true)
	private String userName;

	@Column(name = "date_of_birth")
	private String dateOfBirth;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "cgpa")
	private float cgpa;

	@Column(name = "overall_backlogs")
	private int overallBacklogs;

	@Column(name = "active_backlogs")
	private int activeBacklogs;

	@Column(name = "phone_number")
	private String phoneNumber;

	@Column(name = "sslc_marks")
	private float sslcMarks;

	@Column(name = "hsc_marks")
	private float hscMarks;

	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private Attachment profilePicture;
	
	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private Attachment resume;

	@Column(name = "department")
	private String department;
	
	@Column(name = "visibility")
	private boolean visibility;
	
}
