package com.job.portal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.job.portal.constant.StudentApplicationStatus;
import com.job.portal.dto.CompanyDto;
import com.job.portal.dto.CompanyVisibilityChangeDto;
import com.job.portal.entity.Admin;
import com.job.portal.entity.Company;
import com.job.portal.entity.JobRounds;
import com.job.portal.entity.Student;
import com.job.portal.entity.StudentApplication;
import com.job.portal.exception.AppException;
import com.job.portal.exception.NotFoundException;
import com.job.portal.repository.AdminRepository;
import com.job.portal.repository.CompanyRepository;
import com.job.portal.repository.JobRoundRepository;
import com.job.portal.repository.StudentApplicationRepository;
import com.job.portal.repository.StudentRepository;

@Service
public class CompanyService {

	@Autowired
	CompanyRepository companyRepo;

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	AdminRepository adminRepo;

	@Autowired
	StudentApplicationRepository studentApplicationRepo;

	@Autowired
	JobRoundRepository jobRoundRepo;

	public Company createCompany(CompanyDto companyDto) {
		try {

			Admin admin = adminRepo.findById(companyDto.getAdminId());

			if (admin == null) {
				throw new NotFoundException("Admin id not found");
			}

			Company company = new Company();
			company.setCompanyName(companyDto.getCompanyName());
			company.setLocation(companyDto.getLocation());
			company.setSalaryPackage(companyDto.getSalaryPackage());
			company.setCgpaCriteria(companyDto.getCgpaCriteria());
			company.setSslcCriteria(companyDto.getSslcCriteria());
			company.setHscCriteria(companyDto.getHscCriteria());
			company.setJobRole(companyDto.getJobRole());

			if (companyDto.getJobDescription() != null && !companyDto.getJobDescription().isEmpty()) {
				company.setJobDescription(companyDto.getJobDescription());
			}
			company.setDepartment(companyDto.getDepartment().stream().collect(Collectors.joining(",")));

			if (companyDto.getFirstRound() != null) {
				company.setFirstRound(companyDto.getFirstRound());
			}
			if (companyDto.getSecondRound() != null) {
				company.setSecondRound(companyDto.getSecondRound());
			}
			if (companyDto.getThirdRound() != null) {
				company.setThirdRound(companyDto.getThirdRound());
			}
			if (companyDto.getFourthRound() != null) {
				company.setFourthRound(companyDto.getFourthRound());
			}
			if (companyDto.getFifthRound() != null) {
				company.setFifthRound(companyDto.getFifthRound());
			}
			if (companyDto.getFirstRoundName() != null && !companyDto.getFirstRoundName().isEmpty()) {
				company.setFirstRoundName(companyDto.getFirstRoundName());
			}
			if (companyDto.getSecondRoundName() != null && !companyDto.getSecondRoundName().isEmpty()) {
				company.setSecondRoundName(companyDto.getSecondRoundName());
			}
			if (companyDto.getThirdRoundName() != null && !companyDto.getThirdRoundName().isEmpty()) {
				company.setThirdRoundName(companyDto.getThirdRoundName());
			}
			if (companyDto.getFourthRoundName() != null && !companyDto.getFourthRoundName().isEmpty()) {
				company.setFourthRoundName(companyDto.getFourthRoundName());
			}
			if (companyDto.getFifthRoundName() != null && !companyDto.getFifthRoundName().isEmpty()) {
				company.setFifthRoundName(companyDto.getFifthRoundName());
			}
			if (companyDto.getActiveBacklogsCriteria() != null) {
				company.setActiveBacklogsCriteria(companyDto.getActiveBacklogsCriteria());
			}
			if (companyDto.getOverAllbacklogsCriteria() != null) {
				company.setOverAllbacklogsCriteria(companyDto.getOverAllbacklogsCriteria());
			}
			company.setVisibility(true);

			List<Student> listofStudent = studentRepo.findAll();
			List<StudentApplication> saveAllApplication = new ArrayList<>();
			listofStudent.stream().filter(student -> isValidForApplication(student, company)).map(student -> {
				if (student.isVisibility()) {
					StudentApplication studentApplication = new StudentApplication();
					studentApplication.setStudent(student);
					studentApplication.setCompany(company);
					studentApplication.setStatus(StudentApplicationStatus.NOT_APPLIED.name());
					studentApplication.setJobRounds(createJobRounds(company, student));
					saveAllApplication.add(studentApplication);
					return studentApplication;
				} else {
					return null;
				}
			}).collect(Collectors.toList());

			company.setStudentApplications(studentApplicationRepo.saveAll(saveAllApplication));
			return companyRepo.save(company);
		} catch (Exception e) {
			e.getStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public boolean isValidForApplication(Student student, Company company) {
		String[] listOfDepartments = company.getDepartment().split(",");
		List<String> departments = Arrays.asList(listOfDepartments);
		return departments.contains(student.getDepartment())
				&& student.getOverallBacklogs() <= company.getOverAllbacklogsCriteria()
				&& student.getActiveBacklogs() <= company.getActiveBacklogsCriteria()
				&& student.getSslcMarks() >= company.getSslcCriteria()
				&& student.getHscMarks() >= company.getHscCriteria() && student.getCgpa() >= company.getCgpaCriteria();
	}

	public List<JobRounds> createJobRounds(Company company, Student student) {
		return IntStream.rangeClosed(1, 5).mapToObj(i -> {
			JobRounds jobRounds = new JobRounds();
			jobRounds.setCompany(company);
			jobRounds.setStudent(student);
			String roundName;
			switch (i) {
			case 1:
				roundName = company.getFirstRoundName();
				break;
			case 2:
				roundName = company.getSecondRoundName();
				break;
			case 3:
				roundName = company.getThirdRoundName();
				break;
			case 4:
				roundName = company.getFourthRoundName();
				break;
			case 5:
				roundName = company.getFifthRoundName();
				break;
			default:
				throw new IllegalArgumentException("Unexpected value: " + i);
			}
			if (roundName != null && !roundName.isEmpty()) {
				jobRounds.setRoundName(roundName);
				jobRounds.setRound(i);
				return jobRoundRepo.save(jobRounds);
			} else {
				return null;
			}
		}).collect(Collectors.toList());
	}

	@Transactional
	public Company updateCompany(long companyId, CompanyDto companyDto) {
		try {
			Admin admin = adminRepo.findById(companyDto.getAdminId());
			if (admin == null) {
				throw new NotFoundException("Admin id not found");
			}

			Company company = companyRepo.findById(companyId)
					.orElseThrow(() -> new NotFoundException("Company ID not found"));

			if (isValidString(companyDto.getCompanyName())) {
				company.setCompanyName(companyDto.getCompanyName());
			}
			if (isValidString(companyDto.getLocation())) {
				company.setLocation(companyDto.getLocation());
			}
			if (isValidString(companyDto.getSalaryPackage())) {
				company.setSalaryPackage(companyDto.getSalaryPackage());
			}
			if (companyDto.getCgpaCriteria() != null) {
				company.setCgpaCriteria(companyDto.getCgpaCriteria());
			}
			if (companyDto.getActiveBacklogsCriteria() != null) {
				company.setActiveBacklogsCriteria(companyDto.getActiveBacklogsCriteria());
			}
			if (companyDto.getOverAllbacklogsCriteria() != null) {
				company.setOverAllbacklogsCriteria(companyDto.getOverAllbacklogsCriteria());
			}
			if (companyDto.getSslcCriteria() != null) {
				company.setSslcCriteria(companyDto.getSslcCriteria());
			}
			if (companyDto.getHscCriteria() != null) {
				company.setHscCriteria(companyDto.getHscCriteria());
			}
			if (isValidString(companyDto.getJobRole())) {
				company.setJobRole(companyDto.getJobRole());
			}
			if (isValidString(companyDto.getJobDescription())) {
				company.setJobDescription(companyDto.getJobDescription());
			}
			if (companyDto.getDepartment() != null && !companyDto.getDepartment().isEmpty()) {
				company.setDepartment(companyDto.getDepartment().stream().collect(Collectors.joining(",")));
			}
			if (companyDto.getFirstRound() != null) {
				if (companyDto.getFirstRound()) {
					company.setFirstRound(companyDto.getFirstRound());
				} else {
					company.setFirstRound(false);
					company.setFirstRoundName(null);
				}
			}
			if (companyDto.getSecondRound() != null) {
				if (companyDto.getSecondRound()) {
					company.setSecondRound(companyDto.getSecondRound());
				} else {
					company.setSecondRound(false);
					company.setSecondRoundName(null);
				}
			}
			if (companyDto.getThirdRound() != null) {
				if (companyDto.getThirdRound()) {
					company.setThirdRound(companyDto.getThirdRound());
				} else {
					company.setThirdRound(false);
					company.setThirdRoundName(null);
				}
			}
			if (companyDto.getFourthRound() != null) {
				if (companyDto.getFourthRound()) {
					company.setFourthRound(companyDto.getFourthRound());
				} else {
					company.setFourthRound(false);
					company.setFourthRoundName(null);
				}
			}
			if (companyDto.getFifthRound() != null) {
				if (companyDto.getFifthRound()) {
					company.setFifthRound(companyDto.getFifthRound());
				} else {
					company.setFifthRound(false);
					company.setFifthRoundName(null);
				}

			}
			if (isValidString(companyDto.getFirstRoundName())) {
				company.setFirstRoundName(companyDto.getFirstRoundName());
			}
			if (isValidString(companyDto.getSecondRoundName())) {
				company.setSecondRoundName(companyDto.getSecondRoundName());
			}
			if (isValidString(companyDto.getThirdRoundName())) {
				company.setThirdRoundName(companyDto.getThirdRoundName());
			}
			if (isValidString(companyDto.getFourthRoundName())) {
				company.setFourthRoundName(companyDto.getFourthRoundName());
			}
			if (isValidString(companyDto.getFifthRoundName())) {
				company.setFifthRoundName(companyDto.getFifthRoundName());
			}

			if (companyDto != null) {
				List<StudentApplication> listOfApplications = studentApplicationRepo.findAllByCompanyId(companyId);

				listOfApplications.forEach(application -> {

					int companyRoundCount = 0;

					if (application.getCompany().isFirstRound()) {
						companyRoundCount++;
					}

					if (application.getCompany().isSecondRound()) {
						companyRoundCount++;
					}

					if (application.getCompany().isThirdRound()) {
						companyRoundCount++;
					}

					if (application.getCompany().isFourthRound()) {
						companyRoundCount++;
					}

					if (application.getCompany().isFifthRound()) {
						companyRoundCount++;
					}
					int applicationRoundCount = application.getJobRounds().size();
					if (applicationRoundCount == companyRoundCount) {
						List<JobRounds> listOfJobRounds = application.getJobRounds().stream()
								.map(round -> updateRoundName(round, companyDto)).collect(Collectors.toList());
						application.setJobRounds(listOfJobRounds);
						studentApplicationRepo.save(application);
					}

					if (applicationRoundCount > companyRoundCount) {
						final int finalCompanyRoundCount = companyRoundCount;
						List<JobRounds> listOfJobRounds = application.getJobRounds().stream()
								.map(round -> updateRoundName(round, companyDto)).collect(Collectors.toList());
						application.setJobRounds(listOfJobRounds);
						studentApplicationRepo.save(application);

						application.getJobRounds().stream().forEach(round -> {
							if (round.getRound() > finalCompanyRoundCount) {
								jobRoundRepo.deleteById(round.getId());
							}
						});
					}

					if (applicationRoundCount < companyRoundCount) {

						List<JobRounds> listOfJobRounds = application.getJobRounds();

						JobRounds jobRounds = new JobRounds();
						jobRounds.setCompany(company);
						jobRounds.setStudent(application.getStudent());
						if (isValidString(companyDto.getFirstRoundName())) {
							jobRounds.setRound(1);
							jobRounds.setRoundName(companyDto.getFirstRoundName());
						}

						if (isValidString(companyDto.getSecondRoundName())) {
							jobRounds.setRound(2);
							jobRounds.setRoundName(companyDto.getSecondRoundName());
						}

						if (isValidString(companyDto.getThirdRoundName())) {
							jobRounds.setRound(3);
							jobRounds.setRoundName(companyDto.getThirdRoundName());
						}

						if (isValidString(companyDto.getFourthRoundName())) {
							jobRounds.setRound(4);
							jobRounds.setRoundName(companyDto.getFourthRoundName());
						}

						if (isValidString(companyDto.getFifthRoundName())) {
							jobRounds.setRound(5);
							jobRounds.setRoundName(companyDto.getFifthRoundName());
						}
						JobRounds save = jobRoundRepo.save(jobRounds);

						listOfJobRounds.add(save);
						application.setJobRounds(listOfJobRounds);
						studentApplicationRepo.save(application);
					}

				});
				company.setStudentApplications(listOfApplications);
			}

			List<Student> listofStudent = studentRepo.findAll();
			List<StudentApplication> saveAllApplication = new ArrayList<>();
			List<StudentApplication> listOfStudentApplications = studentApplicationRepo.findAllByCompanyId(companyId);
			List<Long> listOfApplicationStudent = listOfStudentApplications.stream().map(f -> f.getStudent().getId())
					.collect(Collectors.toList());
			listofStudent.stream().filter(student -> isValidForApplication(student, company)).map(student -> {
				if (!listOfApplicationStudent.contains(student.getId()) && student.isVisibility()) {
					StudentApplication studentApplication = new StudentApplication();
					studentApplication.setStudent(student);
					studentApplication.setCompany(company);
					studentApplication.setStatus(StudentApplicationStatus.NOT_APPLIED.name());
					studentApplication.setJobRounds(createJobRounds(company, student));
					saveAllApplication.add(studentApplication);
					return studentApplication;
				} else {
					return null;
				}
			}).collect(Collectors.toList());

			
			removeNonEligibleStudents(listofStudent, company);
			
			company.setStudentApplications(studentApplicationRepo.saveAll(saveAllApplication));

			return companyRepo.save(company);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}
	
	public void removeNonEligibleStudents(List<Student> listOfStudents, Company company) {
		List<Long> removeApplications = new ArrayList<>();
		listOfStudents.stream().filter(student -> !isValidForApplication(student, company)).forEach(student -> {
			StudentApplication application = studentApplicationRepo.findByStudentIdAndCompanyId(student.getId(),
					company.getId());
			if (application != null) {
				removeApplications.add(application.getId());
			}
		});

		studentApplicationRepo.deleteAllByIdIn(removeApplications);
	}

	private JobRounds updateRoundName(JobRounds round, CompanyDto companyDto) {
		switch (round.getRound()) {
		case 1:
			if (isValidString(companyDto.getFirstRoundName())) {
				round.setRoundName(companyDto.getFirstRoundName());
			}
			break;
		case 2:
			if (isValidString(companyDto.getSecondRoundName())) {
				round.setRoundName(companyDto.getSecondRoundName());
			}
			break;
		case 3:
			if (isValidString(companyDto.getThirdRoundName())) {
				round.setRoundName(companyDto.getThirdRoundName());
			}
			break;
		case 4:
			if (isValidString(companyDto.getFourthRoundName())) {
				round.setRoundName(companyDto.getFourthRoundName());
			}
			break;
		case 5:
			if (isValidString(companyDto.getFifthRoundName())) {
				round.setRoundName(companyDto.getFifthRoundName());
			}
			break;
		default:
			break;
		}
		return round;
	}

	private boolean isValidString(String str) {
		return str != null && !str.isEmpty();
	}

	public Company updateVisibility(long adminId, CompanyVisibilityChangeDto companyVisibilityChangeDto) {
		try {
			Admin admin = adminRepo.findById(adminId);
			if (admin == null) {
				throw new NotFoundException("Admin id not Found");
			}
			Company company = companyRepo.findById(companyVisibilityChangeDto.getCompanyId())
					.orElseThrow(() -> new NotFoundException("Company id not found"));
			company.setVisibility(companyVisibilityChangeDto.isVisibility());
			return companyRepo.save(company);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public Company getCompany(long companyId) {
		try {
			return companyRepo.findById(companyId).orElseThrow(() -> new NotFoundException("Company ID not found"));
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public List<Company> getAllComapany() {
		try {
			return companyRepo.findAll();
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public List<Company> getAllComapany(String searchField) {
		try {
			return companyRepo.findAllByCompanyNameContainingIgnoreCaseOrLocationContainingIgnoreCase(searchField,
					searchField);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public List<Company> getAllActiveCompanies() {
		try {
			return companyRepo.findAllByVisibility(true);
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

	public void deleteCompany(long companyId, long adminId) {
		try {

			Admin admin = adminRepo.findById(adminId);
			if (admin == null) {
				throw new NotFoundException("Admin id not Found");
			}

			companyRepo.deleteById(companyId);

		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
	}

}
