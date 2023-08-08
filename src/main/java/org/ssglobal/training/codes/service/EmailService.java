package org.ssglobal.training.codes.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.ssglobal.training.codes.dto.ContactForm;
import org.ssglobal.training.codes.request.EmailRequest;
import org.ssglobal.training.codes.response.Response;
import org.ssglobal.training.codes.tables.pojos.Admin;
import org.ssglobal.training.codes.tables.pojos.Parent;
import org.ssglobal.training.codes.tables.pojos.Professor;
import org.ssglobal.training.codes.tables.pojos.Student;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	private final DSLContext dslContext;
	private final org.ssglobal.training.codes.tables.Admin ADMIN = org.ssglobal.training.codes.tables.Admin.ADMIN;
	private final org.ssglobal.training.codes.tables.Professor PROFESSOR = org.ssglobal.training.codes.tables.Professor.PROFESSOR;
	private final org.ssglobal.training.codes.tables.Student STUDENT = org.ssglobal.training.codes.tables.Student.STUDENT;
	private final org.ssglobal.training.codes.tables.Parent PARENT = org.ssglobal.training.codes.tables.Parent.PARENT;
	private final JavaMailSender mailSender;
	@Value("${spring.mail.username}")
	private String sender;

	private Map<String, Integer> otpMap = new HashMap<>(); // Stores OTP codes and their expiration time

	public Integer sendEmail(String sendTo) {
		Random random = new Random();
		int randomNumber = random.nextInt(900000) + 100000;

		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(sender);
			message.setTo(sendTo);
			message.setText("Dear User,\r\n" + "\r\n" + "Your One-Time Password (OTP) for authentication is: "
					+ randomNumber + ".\r\n" + "\r\n"
					+ "Please use this OTP to complete your login process. The OTP will expire in 5 minutes.\r\n"
					+ "\r\n" + "If you did not request this OTP, please ignore this email.\r\n" + "\r\n"
					+ "Thank you,\r\n" + "SMA University");
			message.setSubject("Your One-Time Password (OTP)");
			mailSender.send(message);

			otpMap.put(sendTo, randomNumber); // Store OTP code and its expiration time
			scheduleOtpExpiry(sendTo); // Schedule the OTP code for expiration after 5 minutes
		} catch (Exception e) {
			e.printStackTrace();
		}
		return randomNumber;
	}
	
	public boolean isOtpExpired(String email) {
	    return !otpMap.containsKey(email);
	}
	
	public void sendEmail(EmailRequest email) {
		System.out.println(email.toString());
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(sender);
			message.setTo(email.getEmail());
			message.setText(email.getBody());
			message.setSubject(email.getSubject());
			mailSender.send(message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public String sendEmail1(ContactForm form) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("andreiember22@gmail.com");
            message.setFrom(sender);
            message.setSubject("Inquiry " + "from " + form.getSetFrom());
            message.setText(form.getMessage());
            mailSender.send(message);
            return "Email sent successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error sending email.";
        }
    }

	private void scheduleOtpExpiry(String email) {
		int expirationTime = 5 * 60 * 1000; // 5 minutes in milliseconds
		new java.util.Timer().schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				otpMap.remove(email); // Remove the expired OTP code from the map
			}
		}, expirationTime);
	}

	public boolean verifyOtp(String email, int otp) {
		Integer storedOtp = otpMap.get(email);
		return storedOtp != null && storedOtp == otp;
	}

	public Response checkEmail(String email) {
		Admin admin = dslContext.selectFrom(ADMIN).where(ADMIN.EMAIL.eq(email)).fetchOneInto(Admin.class);
		Professor professor = dslContext.selectFrom(PROFESSOR).where(PROFESSOR.EMAIL.eq(email))
				.fetchOneInto(Professor.class);
		Student student = dslContext.selectFrom(STUDENT).where(STUDENT.EMAIL.eq(email)).fetchOneInto(Student.class);
		Parent parent = dslContext.selectFrom(PARENT).where(PARENT.EMAIL.eq(email)).fetchOneInto(Parent.class);
		if (admin != null || professor != null || student != null || parent != null) {
			if (admin != null) {
				return Response.builder()
						.status(200)
						.message(String.valueOf(admin.getAdminId()))
						.timestamp(LocalDateTime.now())
						.build();
			} else if (professor != null) {
				return Response.builder()
						.status(200)
						.message(String.valueOf(professor.getProfessorId()))
						.timestamp(LocalDateTime.now())
						.build();
			} else if (student != null){
				return Response.builder()
						.status(200)
						.message(String.valueOf(student.getStudentId()))
						.timestamp(LocalDateTime.now())
						.build();
			} else {
				return Response.builder()
						.status(200)
						.message(String.valueOf(parent.getParentId()))
						.timestamp(LocalDateTime.now())
						.build();
			}
		} else {
			return Response.builder()
					.status(404)
					.message("Email not found")
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
}