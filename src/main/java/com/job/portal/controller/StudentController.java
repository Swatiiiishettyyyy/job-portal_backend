package com.job.portal.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.job.portal.dto.MessageDto;
import com.job.portal.dto.StudentDto;
import com.job.portal.dto.StudentVisibilityChangeDto;
import com.job.portal.entity.Attachment;
import com.job.portal.entity.Student;
import com.job.portal.service.StudentService;

@RestController
@RequestMapping("/api/v1/student")
@CrossOrigin("*")
public class StudentController {

	@Autowired
	private StudentService studentService;

	@PostMapping
	public ResponseEntity<Student> createStudent(@RequestBody StudentDto studentDto) {
		Student createdStudent = studentService.createStudent(studentDto);
		return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
	}

	@PutMapping("/{studentId}")
	public ResponseEntity<Student> updateStudent(@PathVariable long studentId,@RequestBody StudentDto studentDto) {
		Student updatedStudent = studentService.updateStudent(studentId, studentDto);
		return ResponseEntity.ok(updatedStudent);
	}

	@GetMapping("/{studentId}")
	public ResponseEntity<Student> getStudent(@PathVariable long studentId) {
		Student student = studentService.getStudent(studentId);
		return ResponseEntity.ok(student);
	}

	@GetMapping
	public ResponseEntity<List<Student>> getAllStudents() {
		List<Student> students = studentService.getAllStudent();
		return ResponseEntity.ok(students);
	}

	@DeleteMapping("/{studentId}/{adminId}")
	public ResponseEntity<MessageDto> deleteStudent(@PathVariable long studentId, @PathVariable long adminId) {
		studentService.deleteStudent(studentId, adminId);
		MessageDto student = new MessageDto("Student deleted successfully", LocalDateTime.now());
		return new ResponseEntity<MessageDto>(student,HttpStatus.OK);
	}

	@PostMapping("/file")
    public ResponseEntity<Attachment> uploadResume(@RequestPart("file") MultipartFile file) {
        Attachment fileUpload = studentService.fileUpload(file);
        return ResponseEntity.ok(fileUpload);
    }

	@PostMapping("/upload-excel")
	public ResponseEntity<String> uploadExcel(@RequestPart("adminId") String adminId,
			@RequestPart("file") MultipartFile file) throws IOException {
		String students = studentService.readExcelFile(file);
		return new ResponseEntity<String>(students, HttpStatus.OK);
	}

	@PostMapping("/visibility/{adminId}")
	public ResponseEntity<?> updateVisibility(@PathVariable("adminId") long adminId,
			@RequestBody StudentVisibilityChangeDto visibilityChangeDto) {
		Student updatedStudent = studentService.updateVisibility(adminId, visibilityChangeDto);
		return ResponseEntity.ok(updatedStudent);
	}
}
