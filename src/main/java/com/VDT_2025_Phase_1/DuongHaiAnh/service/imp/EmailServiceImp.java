package com.VDT_2025_Phase_1.DuongHaiAnh.service.imp;

import com.VDT_2025_Phase_1.DuongHaiAnh.service.MailerService;
import com.VDT_2025_Phase_1.DuongHaiAnh.setting.SystemSetting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImp implements MailerService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailSenderEmail;

    @Override
    public void sendOtp(String toEmail, String otp) {
        String otpExpiration = String.valueOf(SystemSetting.OTPEXPIRATION);
        String subject = "Account Verification – Your OTP Code";

        String body = """
            Hello,
            
            You have requested to verify your account using a One-Time Password (OTP).

            ➤ Your OTP code is: %s

            This code is valid for %s minutes. Please do not share it with anyone for security reasons.

            If you did not request this, please ignore this email.

            Best regards,
            Support Team
            """.formatted(otp, otpExpiration);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(mailSenderEmail);

        mailSender.send(message);
    }
}
