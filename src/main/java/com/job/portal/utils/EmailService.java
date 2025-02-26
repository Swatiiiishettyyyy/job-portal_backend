package com.job.portal.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.job.portal.dto.EmailDto;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String senderMail;
	
	@Async
	public void sendEmail(String subject, String type, EmailDto emailDto)
	        throws jakarta.mail.MessagingException, IOException {
	    MimeMessage message = javaMailSender.createMimeMessage();
	    MimeMessageHelper msg = new MimeMessageHelper(message, true);
	    msg.setFrom(new InternetAddress(senderMail, "Placement Team"));
	    msg.setTo(emailDto.getStudentEmail());
	    msg.setSubject(subject);
	    StringBuilder textBuilder = new StringBuilder();
	    if (type.equals("recruitment")) {
	        textBuilder.append("Hi ").append(emailDto.getStudentFirstName()).append(" ").append(emailDto.getStudentLastName()).append(",<br><br>")
	                .append("We are delighted to inform you that you are eligible to apply for "+emailDto.getCompanyName()+". If you are interested, we encourage you to proceed with your application.<br><br>")
	                .append("Should you have any questions or require further information, please to contact us our placement team.<br><br>")
	                .append("Best regards,<br>")
	                .append("Placement Team");
	    }
	    msg.setText(textBuilder.toString(), true);
	    javaMailSender.send(message);
	}

}
