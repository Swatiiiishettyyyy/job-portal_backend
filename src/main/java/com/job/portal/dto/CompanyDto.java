package com.job.portal.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompanyDto {

	private String companyName;

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSalaryPackage() {
		return salaryPackage;
	}

	public void setSalaryPackage(String salaryPackage) {
		this.salaryPackage = salaryPackage;
	}

	public Float getCgpaCriteria() {
		return cgpaCriteria;
	}

	public void setCgpaCriteria(Float cgpaCriteria) {
		this.cgpaCriteria = cgpaCriteria;
	}

	public Integer getOverAllbacklogsCriteria() {
		return overAllbacklogsCriteria;
	}

	public void setOverAllbacklogsCriteria(Integer overAllbacklogsCriteria) {
		this.overAllbacklogsCriteria = overAllbacklogsCriteria;
	}

	public Integer getActiveBacklogsCriteria() {
		return activeBacklogsCriteria;
	}

	public void setActiveBacklogsCriteria(Integer activeBacklogsCriteria) {
		this.activeBacklogsCriteria = activeBacklogsCriteria;
	}

	public Float getSslcCriteria() {
		return sslcCriteria;
	}

	public void setSslcCriteria(Float sslcCriteria) {
		this.sslcCriteria = sslcCriteria;
	}

	public Float getHscCriteria() {
		return hscCriteria;
	}

	public void setHscCriteria(Float hscCriteria) {
		this.hscCriteria = hscCriteria;
	}

	public String getJobRole() {
		return jobRole;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	public List<String> getDepartment() {
		return department;
	}

	public void setDepartment(List<String> department) {
		this.department = department;
	}

	public Boolean getFirstRound() {
		return firstRound;
	}

	public void setFirstRound(Boolean firstRound) {
		this.firstRound = firstRound;
	}

	public Boolean getSecondRound() {
		return secondRound;
	}

	public void setSecondRound(Boolean secondRound) {
		this.secondRound = secondRound;
	}

	public Boolean getThirdRound() {
		return thirdRound;
	}

	public void setThirdRound(Boolean thirdRound) {
		this.thirdRound = thirdRound;
	}

	public Boolean getFourthRound() {
		return fourthRound;
	}

	public void setFourthRound(Boolean fourthRound) {
		this.fourthRound = fourthRound;
	}

	public Boolean getFifthRound() {
		return fifthRound;
	}

	public void setFifthRound(Boolean fifthRound) {
		this.fifthRound = fifthRound;
	}

	public String getFirstRoundName() {
		return firstRoundName;
	}

	public void setFirstRoundName(String firstRoundName) {
		this.firstRoundName = firstRoundName;
	}

	public String getSecondRoundName() {
		return secondRoundName;
	}

	public void setSecondRoundName(String secondRoundName) {
		this.secondRoundName = secondRoundName;
	}

	public String getThirdRoundName() {
		return thirdRoundName;
	}

	public void setThirdRoundName(String thirdRoundName) {
		this.thirdRoundName = thirdRoundName;
	}

	public String getFourthRoundName() {
		return fourthRoundName;
	}

	public void setFourthRoundName(String fourthRoundName) {
		this.fourthRoundName = fourthRoundName;
	}

	public String getFifthRoundName() {
		return fifthRoundName;
	}

	public void setFifthRoundName(String fifthRoundName) {
		this.fifthRoundName = fifthRoundName;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	private String location;

	private String salaryPackage;

	private Float cgpaCriteria;

	private Integer overAllbacklogsCriteria;
	
	private Integer activeBacklogsCriteria;

	private Float sslcCriteria;

	private Float hscCriteria;

	private String jobRole;

	private String jobDescription;

	private List<String> department;

	private Boolean firstRound;

	private Boolean secondRound;

	private Boolean thirdRound;

	private Boolean fourthRound;

	private Boolean fifthRound;

	private String firstRoundName;

	private String secondRoundName;

	private String thirdRoundName;

	private String fourthRoundName;

	private String fifthRoundName;

	private long adminId;
}
