package com.zettamine.boot.email;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.zettamine.boot.constants.AppConstants;
import com.zettamine.boot.entity.User;

import jakarta.activation.URLDataSource;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
@Slf4j
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;

	public boolean sendEmail(User user, Boolean isExistingUser) {
		String content =null;
		String subject =null;
		if (isExistingUser) {
				content = forgotPasswordContents(user.getFirstName(), user.getLastName(),  user.getEmail(),user.getTempPwd());
				subject =AppConstants.FORGOT_PASSWORD;
		}else {
				content = getEmailContents(user.getFirstName(), user.getLastName(), user.getDesignation(), user.getEmail(),user.getTempPwd());
				subject =AppConstants.WELCOME_MAIL;
		}
		
		String from =AppConstants.EMAIL_FROM;
		String to = user.getEmail();
		
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,true);
			
			helper.setSubject(subject);
			helper.setFrom(from);
			helper.setTo(to);;
			helper.setText(content, true);
			URLDataSource dataSource = new URLDataSource(this.getClass().getResource("/images/logo.png"));
			helper.addInline("company", dataSource);
			
			mailSender.send(mimeMessage);
			return true;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getEmailContents(String fName, String lName, String role, String email, String password) {
		try (InputStream fis = new ClassPathResource("email-template/user-email.html").getInputStream()) {
            String str = IOUtils.toString(fis, "UTF-8");
            return String.format(str, fName, lName, role, email, password);
        } catch (IOException e) {
            log.error("Failed to load email template: {}", e.getMessage());
            return "";
        }
	}
	
	private String forgotPasswordContents(String fName, String lName,  String email, String password) {
		try (InputStream fis = new ClassPathResource("email-template/forgot-password-email.html").getInputStream()) {
            String str = IOUtils.toString(fis, "UTF-8");
            return String.format(str, fName, lName, email, password);
        } catch (IOException e) {
            log.error("Failed to load email template: {}", e.getMessage());
            return "";
        }
	}

}