package com.job.portal.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.job.portal.constant.StudentApplicationStatus;
import com.job.portal.dto.StudentDto;
import com.job.portal.dto.StudentVisibilityChangeDto;
import com.job.portal.entity.Admin;
import com.job.portal.entity.Attachment;
import com.job.portal.entity.Company;
import com.job.portal.entity.Student;
import com.job.portal.entity.StudentApplication;
import com.job.portal.exception.AppException;
import com.job.portal.exception.NotFoundException;
import com.job.portal.repository.AdminRepository;
import com.job.portal.repository.AttachmentRepository;
import com.job.portal.repository.CompanyRepository;
import com.job.portal.repository.StudentApplicationRepository;
import com.job.portal.repository.StudentRepository;

import jakarta.transaction.Transactional;

@Service
public class StudentService {

	@Autowired
	StudentRepository studentRepo;

	@Autowired
	AdminRepository adminRepo;

	@Autowired
	StudentApplicationRepository studentApplicationRepo;

	@Autowired
	CompanyRepository companyRepo;

	@Autowired
	AttachmentRepository attachmentRepo;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	CompanyService companyService;

	public Student createStudent(StudentDto studentDto) {
		try {

			Admin admin = adminRepo.findById(studentDto.getUserId());

			if (admin == null) {
				throw new NotFoundException("Admin id not found");
			}

			if (studentRepo.existsByUserName(studentDto.getUserName())) {
				throw new AppException("Username already exists");
			}

			if (studentRepo.existsByEmail(studentDto.getEmail())) {
				throw new AppException("Email ID is already exists");
			}

			Attachment profilePicture = null;
			Attachment resume = null;
			if (studentDto.getProfilePictureId() != null) {
				profilePicture = attachmentRepo.findById(studentDto.getProfilePictureId())
						.orElseThrow(() -> new NotFoundException("Profile picture id not found"));
			}

			if (studentDto.getResumeId() != null) {
				resume = attachmentRepo.findById(studentDto.getResumeId())
						.orElseThrow(() -> new NotFoundException("Resume id not found"));
			}

			Student student = new Student();
			student.setUserName(studentDto.getUserName());
			student.setDateOfBirth(studentDto.getDateOfBirth());
			student.setFirstName(studentDto.getFirstName());
			student.setLastName(studentDto.getLastName());
			student.setEmail(studentDto.getEmail());
			student.setCgpa(studentDto.getCgpa());
			student.setOverallBacklogs(studentDto.getOverallBacklogs());
			student.setActiveBacklogs(studentDto.getActiveBacklogs());
			student.setPhoneNumber(studentDto.getPhoneNumber());
			student.setSslcMarks(studentDto.getSslcMarks());
			student.setHscMarks(studentDto.getHscMarks());
			student.setDepartment(studentDto.getDepartment());
			student.setVisibility(true);
			student.setProfilePicture(profilePicture);
			student.setResume(resume);
			Student saveStudent = studentRepo.save(student);
			studentApplicationApply(saveStudent);
			return saveStudent;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	@Transactional
	public Student updateStudent(long studentId, StudentDto studentDto) {
		try {
			Student student = null;
			Admin admin = adminRepo.findById(studentDto.getUserId());
			if (admin == null) {
				student = studentRepo.findById(studentDto.getUserId())
						.orElseThrow(() -> new NotFoundException("logged student id not found"));
				if (student.getId() != studentId) {
					throw new NotFoundException("You don't have a permission to update this user details");
				}

			} else {
				student = studentRepo.findById(studentId)
						.orElseThrow(() -> new NotFoundException("Student id not found"));

			}
			if (studentDto.getUserName() != null && !studentDto.getUserName().isEmpty()) {
				if (!studentDto.getUserName().equals(student.getUserName())) {
					if (studentRepo.existsByUserName(studentDto.getUserName())) {
						throw new AppException("Username already exists");
					}
				}
				student.setUserName(studentDto.getUserName());
			}
			if (studentDto.getDateOfBirth() != null && !studentDto.getDateOfBirth().isEmpty()) {
				student.setDateOfBirth(studentDto.getDateOfBirth());
			}
			if (studentDto.getFirstName() != null && !studentDto.getFirstName().isEmpty()) {
				student.setFirstName(studentDto.getFirstName());
			}
			if (studentDto.getLastName() != null && !studentDto.getLastName().isEmpty()) {
				student.setLastName(studentDto.getLastName());
			}
			if (studentDto.getEmail() != null && !studentDto.getEmail().isEmpty()) {
				if (!studentDto.getEmail().equals(student.getEmail())) {
					if (studentRepo.existsByEmail(studentDto.getEmail())) {
						throw new AppException("Email ID is already exists");
					}
				}
				student.setEmail(studentDto.getEmail());
			}
			if (studentDto.getCgpa() >= 0) {
				student.setCgpa(studentDto.getCgpa());
			}
			if (studentDto.getOverallBacklogs() >= 0) {
				student.setOverallBacklogs(studentDto.getOverallBacklogs());
			}
			if (studentDto.getActiveBacklogs() >= 0) {
				student.setActiveBacklogs(studentDto.getActiveBacklogs());
			}
			if (studentDto.getPhoneNumber() != null && !studentDto.getPhoneNumber().isEmpty()) {
				student.setPhoneNumber(studentDto.getPhoneNumber());
			}
			if (studentDto.getSslcMarks() >= 0) {
				student.setSslcMarks(studentDto.getSslcMarks());
			}
			if (studentDto.getHscMarks() >= 0) {
				student.setHscMarks(studentDto.getHscMarks());
			}
			if (studentDto.getDepartment() != null && !studentDto.getDepartment().isEmpty()) {
				student.setDepartment(studentDto.getDepartment());
			}

			Attachment profilePicture = null;
			if (studentDto.getProfilePictureId() != null) {
				profilePicture = attachmentRepo.findById(studentDto.getProfilePictureId())
						.orElseThrow(() -> new NotFoundException("Profile picture id not found"));
				student.setProfilePicture(profilePicture);
			}
			Attachment resume = null;
			if (studentDto.getResumeId() != null) {
				resume = attachmentRepo.findById(studentDto.getResumeId())
						.orElseThrow(() -> new NotFoundException("Resume id not found"));
				student.setResume(resume);
			}

			Student saveStudent = studentRepo.save(student);
			studentApplicationApply(saveStudent);

			List<Long> removeApplications = new ArrayList<>();
			List<Company> listOfCompany = companyRepo.findAll();
			listOfCompany.stream().forEach(company -> {
				if (!companyService.isValidForApplication(saveStudent, company)) {
					StudentApplication application = studentApplicationRepo
							.findByStudentIdAndCompanyId(saveStudent.getId(), company.getId());
					removeApplications.add(application.getId());
				}
			});
			studentApplicationRepo.deleteAllByIdIn(removeApplications);

			return saveStudent;
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	private void studentApplicationApply(Student student) {
		List<Company> listOfCompany = companyRepo.findAll();
		listOfCompany.stream().forEach(company -> {
			if (student.isVisibility()) {
				if (!studentApplicationRepo.existsByStudentId(student.getId())) {
					StudentApplication studentApplication = new StudentApplication();
					studentApplication.setStudent(student);
					studentApplication.setCompany(company);
					studentApplication.setStatus(StudentApplicationStatus.NOT_APPLIED.name());
					studentApplication.setJobRounds(companyService.createJobRounds(company, student));
					studentApplicationRepo.save(studentApplication);
				}
			}
		});
	}

	public Student updateVisibility(long adminId, StudentVisibilityChangeDto visibilityChangeDto) {
		try {
			Admin admin = adminRepo.findById(adminId);
			if (admin == null) {
				throw new NotFoundException("Admin id not Found");
			}
			Student student = studentRepo.findById(visibilityChangeDto.getStudentId())
					.orElseThrow(() -> new NotFoundException("Student id not found"));
			student.setVisibility(visibilityChangeDto.isVisibility());
			return studentRepo.save(student);

		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public Student getStudent(long studentId) {
		try {
			return studentRepo.findById(studentId).orElseThrow(() -> new NotFoundException("Student id not found"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public List<Student> getAllStudent() {
		try {
			return studentRepo.findAll();
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public void deleteStudent(long studentId, long adminId) {
		try {
			Admin admin = adminRepo.findById(adminId);
			if (admin == null) {
				throw new NotFoundException("Admin id not found");
			}
			studentRepo.deleteById(studentId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public Attachment fileUpload(MultipartFile file) {
		try {
			Attachment uploadFile = new Attachment();
			uploadFile.setUploadFile(Base64.getEncoder().encode(file.getBytes()));
			uploadFile.setFileData(new String(uploadFile.getUploadFile(), StandardCharsets.UTF_8));
			return attachmentRepo.save(uploadFile);
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException(e.getMessage());
		}
	}

	public String readExcelFile(MultipartFile file) throws IOException {
		List<Student> students = new ArrayList<>();
		StringBuilder existingStudents = new StringBuilder();
		
		Set<Student> errorStudents = new HashSet<>();

		try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rows = sheet.iterator();

			// Skip the header row
			if (rows.hasNext()) {
				rows.next();
			}

			while (rows.hasNext()) {
				Row currentRow = rows.next();
				Iterator<Cell> cellsInRow = currentRow.iterator();

				Student student = new Student();

				while (cellsInRow.hasNext()) {
					Cell currentCell = cellsInRow.next();
					int columnIndex = currentCell.getColumnIndex();

					switch (columnIndex) {
					case 0:
						if (currentCell.getCellType() == CellType.STRING) {
							student.setUserName(currentCell.getStringCellValue());
						}
						break;
					case 1:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							double data = currentCell.getNumericCellValue();
							Date dob = DateUtil.getJavaDate(data);
							Calendar dobCalendar = Calendar.getInstance();
							dobCalendar.setTime(dob);
							Calendar today = Calendar.getInstance();
							int age = today.get(Calendar.YEAR) - dobCalendar.get(Calendar.YEAR);
							if (today.get(Calendar.DAY_OF_YEAR) < dobCalendar.get(Calendar.DAY_OF_YEAR)) {
								age--;
							}
							if (age > 18) {
								SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
								student.setDateOfBirth(sdf.format(dob));
								student.setVisibility(true);
							} else {
								errorStudents.add(student);
								existingStudents
										.append(student.getUserName() + " - Age not valid." );
							}
						}
						break;
					case 2:
						if (currentCell.getCellType() == CellType.STRING) {
							student.setFirstName(currentCell.getStringCellValue());
						}
						break;
					case 3:
						if (currentCell.getCellType() == CellType.STRING) {
							student.setLastName(currentCell.getStringCellValue());
						}
						break;
					case 4:
						if (currentCell.getCellType() == CellType.STRING) {
							String email = currentCell.getStringCellValue();
							String gmailPattern = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*@gmail\\.com$";
							if (email.matches(gmailPattern)) {
								student.setEmail(email);
							} else {
								errorStudents.add(student);
								existingStudents.append(student.getUserName() + " - Email not valid.");
							}
						}
					case 5:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							float cgpa = (float) currentCell.getNumericCellValue();
							if (cgpa < 0 || cgpa > 10) {
								errorStudents.add(student);
								existingStudents
										.append(student.getUserName() + " - CGPA not valid.");
							} else {
								student.setCgpa(cgpa);
							}
						}
						break;
					case 6:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							int overallBacklogs = (int) currentCell.getNumericCellValue();
							if (overallBacklogs < 0) {
								errorStudents.add(student);
								existingStudents.append(
										student.getUserName() + " - Over all back logs not valid.");
							} else {
								student.setOverallBacklogs(overallBacklogs);
							}
						}
						break;
					case 7:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							int activeBacklogs = (int) currentCell.getNumericCellValue();
							if (activeBacklogs < 0) {
								errorStudents.add(student);
								existingStudents.append(
										student.getUserName() + " - Active back logs not valid.");
							} else {
								student.setActiveBacklogs(activeBacklogs);
							}
						}
						break;
					case 8:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							String phoneNumber = String.valueOf((long) currentCell.getNumericCellValue());
							if (!phoneNumber.matches("\\d{10}")) {
								errorStudents.add(student);
								existingStudents.append(
										student.getUserName() + " - Phone number not valid.");
							} else {
								long num = Long.parseLong(phoneNumber);
								if (num < 0) {
									errorStudents.add(student);
									existingStudents.append(student.getUserName()
											+ " - Phone number not valid.");
								} else {
									student.setPhoneNumber(phoneNumber);
								}
							}
						}
						break;
					case 9:
					case 10:
						if (currentCell.getCellType() == CellType.NUMERIC) {
							int marks = (int) currentCell.getNumericCellValue();
							if (marks < 0 || marks > 100) {
								errorStudents.add(student);
								existingStudents.append(
										student.getUserName() + " - HSC or SSLC mark not valid.");
							} else {
								if (columnIndex == 9) {
									student.setSslcMarks(marks);
								} else {
									student.setHscMarks(marks);
								}
							}
						}
						break;
					case 11:
						if (currentCell.getCellType() == CellType.STRING) {
							student.setDepartment(currentCell.getStringCellValue());
						}
						break;
					}
				}
				students.add(student);
			}
		} catch (IOException e) {
			throw new IOException("Error occurred while reading the Excel file", e);
		} catch (Exception e) {
			throw new RuntimeException("Error occurred while processing the Excel file", e);
		}
		
		Set<Student> listOfStudents = new HashSet<>();
		

		for (Student student : students) {
			if (studentRepo.existsByUserName(student.getUserName())) {
				if (existingStudents.length() > 0) {
					existingStudents.append(", ");
				} 
				existingStudents.append(student.getUserName() + " - USN number already exists");
			}
			else {
				listOfStudents.add(student);
			}
		}
		List<Student> finalStudentsList = new ArrayList<>();
		for(Student student :listOfStudents) {
			if(!errorStudents.contains(student)) {
				finalStudentsList.add(student);
			}
		}
		studentRepo.saveAll(finalStudentsList);
		
		if (existingStudents.length() > 0) {
			return existingStudents.toString();
		} else {
			return "Excel file uploaded successfully";
		}
	}
}
