package com.job.portal.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.job.portal.constant.StudentApplicationStatus;
import com.job.portal.dto.ApplicationStatusChangeDto;
import com.job.portal.dto.EmailDto;
import com.job.portal.dto.StudentApplicationDto;
import com.job.portal.entity.Admin;
import com.job.portal.entity.Company;
import com.job.portal.entity.JobRounds;
import com.job.portal.entity.Student;
import com.job.portal.entity.StudentApplication;
import com.job.portal.exception.NotFoundException;
import com.job.portal.repository.AdminRepository;
import com.job.portal.repository.CompanyRepository;
import com.job.portal.repository.JobRoundRepository;
import com.job.portal.repository.StudentApplicationRepository;
import com.job.portal.repository.StudentRepository;
import com.job.portal.utils.EmailService;

import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Service
public class StudentApplicationService {

	@Autowired
	StudentApplicationRepository studentApplicationRepo;

	@Autowired
	AdminRepository adminRepo;

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	CompanyRepository companyRepo;

	@Autowired
	JobRoundRepository jobRoundRepo;

	@Autowired
	EntityManager entityManager;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	EmailService emailService;

	public StudentApplication createStudentApplication(StudentApplicationDto studentApplicationDto) {
		try {
			Admin admin = adminRepo.findById(studentApplicationDto.getAdminId());
			if (admin == null) {
				throw new NotFoundException("Admin id not found");
			}
			Student student = studentRepo.findById(studentApplicationDto.getStudentId())
					.orElseThrow(() -> new NotFoundException("Student id not found"));
			Company company = companyRepo.findById(studentApplicationDto.getCompanyId())
					.orElseThrow(() -> new NotFoundException("Company id not found"));
			if (student.isVisibility()) {
				if (companyService.isValidForApplication(student, company)) {
					StudentApplication studentApplication = new StudentApplication();
					studentApplication.setStudent(student);
					studentApplication.setCompany(company);
					studentApplication.setStatus(StudentApplicationStatus.NOT_APPLIED.name());
					studentApplication.setJobRounds(createJobRounds(company, student));
					return studentApplicationRepo.save(studentApplication);
				} else {
					throw new NotFoundException("You are not eligible for this Company application");
				}
			} else {
				throw new NotFoundException(
						"This student is in an inactive state and is not able to apply for this job.");
			}
		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	private List<JobRounds> createJobRounds(Company company, Student student) {
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

	public StudentApplication getStudentApplication(long studentApplicationId) {
		try {
			return studentApplicationRepo.findById(studentApplicationId)
					.orElseThrow(() -> new NotFoundException("Student Application id not found "));
		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	public List<StudentApplication> searchStudentApplications(Long companyId, long userId) {
		try {

			CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
			CriteriaQuery<StudentApplication> criteriaQuery = criteriaBuilder.createQuery(StudentApplication.class);
			Root<StudentApplication> root = criteriaQuery.from(StudentApplication.class);
			List<Predicate> predicates = new ArrayList<>();

			Admin admin = adminRepo.findById(userId);
			if (admin == null) {
				studentRepo.findById(userId)
						.orElseThrow(() -> new NotFoundException("You don't have a permission see this information"));
				Join<StudentApplication, Student> studentJoin = root.join("student");
				predicates.add(criteriaBuilder.equal(studentJoin.get("id"), userId));
			}
			Join<StudentApplication, Company> companyJoin = root.join("company");
			if (companyId != null) {
				predicates.add(criteriaBuilder.equal(companyJoin.get("id"), companyId));
			}
			predicates.add(criteriaBuilder.isTrue(companyJoin.get("visibility")));
			criteriaQuery.where(predicates.toArray(new Predicate[0]));
			return entityManager.createQuery(criteriaQuery).getResultList();

		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	public StudentApplication updateStatus(ApplicationStatusChangeDto statusChangeDto) {
		try {

			StudentApplication studentApplication = studentApplicationRepo
					.findById(statusChangeDto.getStudentApplicationId())
					.orElseThrow(() -> new NotFoundException("Student Application id not found"));

			if (studentApplication.getStatus().equals(StudentApplicationStatus.NOT_APPLIED.name())) {
				throw new NotFoundException("This student was not applied for this company");
			}
			
			if (studentApplication.getStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
				throw new NotFoundException("This student was before round not selcted for this company");
			}
			
			if(!studentApplication.getStudent().isVisibility()) {
			    throw new NotFoundException("This student is in an inactive state. Unable to update status.");
			}

			Admin admin = adminRepo.findById(statusChangeDto.getAdminId());
			if (admin == null) {
				throw new NotFoundException("Admin id not found");
			}
			
			int roundCount =0 ;
			
			if(studentApplication.getCompany().isFirstRound()) {
				roundCount++;
			}
			
			if(studentApplication.getCompany().isSecondRound()) {
				roundCount++;
			}
			
			if(studentApplication.getCompany().isThirdRound()) {
				roundCount++;
			}
			
			if(studentApplication.getCompany().isFourthRound()) {
				roundCount++;
			}
			
			if(studentApplication.getCompany().isFifthRound()) {
				roundCount++;
			}
			

			if (statusChangeDto.getRound() != null) {
				if (statusChangeDto.getRoundStatus() != null && !statusChangeDto.getRoundStatus().isEmpty()) {
					switch (statusChangeDto.getRound()) {
					case 1:
						studentApplication.getJobRounds().stream().filter(e -> e.getRound() == 1).forEach(f -> {
							f.setDate(LocalDateTime.now());
							f.setRoundStatus(statusChangeDto.getRoundStatus());
						});

						if (roundCount == 1) {
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.SELECTED.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						} 
						if(roundCount>1){
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.INPROGRESS.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						}
						break;
					case 2:
						studentApplication.getJobRounds().stream().filter(e -> e.getRound() == 2).forEach(f -> {
							f.setDate(LocalDateTime.now());
							f.setRoundStatus(statusChangeDto.getRoundStatus());
						});
						if (roundCount == 2) {
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.SELECTED.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						} 
						if(roundCount>2){
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.INPROGRESS.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						}
						break;
					case 3:
						studentApplication.getJobRounds().stream().filter(e -> e.getRound() == 3).forEach(f -> {
							f.setDate(LocalDateTime.now());
							f.setRoundStatus(statusChangeDto.getRoundStatus());
						});
						if (roundCount == 3) {
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.SELECTED.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						} 
						if(roundCount>3){
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.INPROGRESS.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						}
						break;
					case 4:
						studentApplication.getJobRounds().stream().filter(e -> e.getRound() == 4).forEach(f -> {
							f.setDate(LocalDateTime.now());
							f.setRoundStatus(statusChangeDto.getRoundStatus());
						});
						if (roundCount == 4) {
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.SELECTED.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						} 
						if(roundCount>4){
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.INPROGRESS.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						}
						break;
					case 5:
						studentApplication.getJobRounds().stream().filter(e -> e.getRound() == 5).forEach(f -> {
							f.setDate(LocalDateTime.now());
							f.setRoundStatus(statusChangeDto.getRoundStatus());
						});
						if (roundCount == 5) {
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.SELECTED.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						} 
						if(roundCount>5){
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.INPROGRESS.name());
							}
							if (statusChangeDto.getRoundStatus().equals(StudentApplicationStatus.NOT_SELECTED.name())) {
								studentApplication.setStatus(StudentApplicationStatus.NOT_SELECTED.name());
							}
						}
						break;
					default:
						throw new IllegalArgumentException("Unexpected value: " + statusChangeDto.getRound());
					}
				} else {
					throw new NotFoundException("Round status not Found");
				}
			}
			return studentApplicationRepo.save(studentApplication);
		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		}
	}
	
	public StudentApplication applyCompany(long studentApplicationId, long studentId) {
		try {
			StudentApplication studentApplication = studentApplicationRepo.findById(studentApplicationId)
					.orElseThrow(() -> new NotFoundException("Student Application id not found"));

			Student student = studentRepo.findById(studentId)
					.orElseThrow(() -> new NotFoundException("Student id not found"));

			if(!student.isVisibility()) {
			    throw new NotFoundException("You are in an inactive state. You are not able to apply for this job.");
			}
			
			if (studentApplication.getStudent().getId() == student.getId()) {
				studentApplication.setStatus(StudentApplicationStatus.APPLIED.name());
				studentApplication.setAppliedDate(LocalDateTime.now());
				return studentApplicationRepo.save(studentApplication);
			} else {
				throw new NotFoundException("You don't have a permisssion apply this company");
			}
		} catch (Exception e) {
			throw new NotFoundException(e.getMessage());
		}
	}
	
	public String sendEmailForStudents(long adminId, long companyId) {
		try {
			Admin admin = adminRepo.findById(adminId);
			if (admin == null) {
				throw new NotFoundException("Admin id not found");
			}
			List<StudentApplication> listOfStudentApplication = studentApplicationRepo.findAllByCompanyId(companyId);
			long emailsSentCount = listOfStudentApplication.stream()
					.filter(application -> application.getStatus().equals(StudentApplicationStatus.NOT_APPLIED.name())
							&& application.getStudent().isVisibility())
					.map(application -> {
						EmailDto emailDto = new EmailDto();
						emailDto.setStudentEmail(application.getStudent().getEmail());
						emailDto.setStudentFirstName(application.getStudent().getFirstName());
						emailDto.setStudentLastName(application.getStudent().getLastName());
						emailDto.setCompanyName(application.getCompany().getCompanyName());
						try {
							emailService.sendEmail(
									"Application Opportunity with " + application.getCompany().getCompanyName(),
									"recruitment", emailDto);
							return 1;
						} catch (MessagingException | IOException e) {
							e.printStackTrace();
							return 0;
						}
					}).count();

			return emailsSentCount + " emails sent successfully";

		} catch (Exception e) {
			throw new NotFoundException("Error sending emails: " + e.getMessage());
		}
	}

	public String sendEmailSpecificUser(long adminId, long studentApplicationId) {
	    try {
	        Admin admin = adminRepo.findById(adminId);
	        if (admin == null) {
	            throw new NotFoundException("Admin with id " + adminId + " not found");
	        }

	        StudentApplication studentApplication = studentApplicationRepo.findById(studentApplicationId)
	                .orElseThrow(() -> new NotFoundException("Student Application with id " + studentApplicationId + " not found"));

	        Student student = studentApplication.getStudent();
	        Company company = studentApplication.getCompany();

	        if (student == null || company == null) {
	            throw new NotFoundException("Student or Company not found for Student Application id " + studentApplicationId);
	        }

	        if (student.isVisibility()) {
	            if (studentApplication.getStatus().equals(StudentApplicationStatus.NOT_APPLIED.name())) {
	                EmailDto emailDto = new EmailDto();
	                emailDto.setStudentEmail(student.getEmail());
	                emailDto.setStudentFirstName(student.getFirstName());
	                emailDto.setStudentLastName(student.getLastName());
	                emailDto.setCompanyName(company.getCompanyName());

	                emailService.sendEmail("Application Opportunity with " + company.getCompanyName(), "recruitment", emailDto);

	                return "Email sent successfully";
	            } else {
	                throw new NotFoundException("This student has already applied for the job.");
	            }
	        } else {
	            throw new NotFoundException("This student is inactive and cannot apply for this job.");
	        }

	    } catch (NotFoundException e) {
	        throw e;
	    } catch (Exception e) {
	        throw new NotFoundException("Error sending email: " + e.getMessage());
	    }
	}

}
