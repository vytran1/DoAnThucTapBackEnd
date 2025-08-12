package com.thuctap.utility;

import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.thuctap.common.setting.Setting;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

public class EmailUtility {
	
	private static final Logger log = LoggerFactory.getLogger(EmailUtility.class);
	
	public static JavaMailSender prepareMailSender(List<Setting> settings) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        
        String host = getSettingValue(settings, "MAIL_HOST");
        String port = getSettingValue(settings, "MAIL_PORT");
        String username = getSettingValue(settings, "MAIL_USERNAME");
        String password = getSettingValue(settings, "MAIL_PASSWORD");
        String smtpAuth = getSettingValue(settings, "SMTP_AUTH");
        String smtpSecured = getSettingValue(settings, "SMTP_SECURED");

        mailSender.setHost(host);
        mailSender.setPort(Integer.parseInt(port));
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", smtpSecured);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");

        return mailSender;
    }
	
	public static void sendMail(JavaMailSender mailSender, String from, String to, String subject, String content,
			boolean isHtml)  {

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(from);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(content, isHtml);
		} catch (MessagingException e) {
			log.error("Error when sending email in case creating transfer form",e);
		}


		mailSender.send(message);
	}
	
	 private static String getSettingValue(List<Setting> settings, String key) {
	        return settings.stream()
	                .filter(s -> key.equals(s.getKey()))
	                .findFirst()
	                .map(Setting::getValue)
	                .orElseThrow(() -> new IllegalArgumentException("Missing setting: " + key));
	 }
	
}
