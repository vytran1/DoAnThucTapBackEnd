package com.thuctap.event.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.thuctap.common.setting.Setting;
import com.thuctap.event.RabbitMQConfig;
import com.thuctap.exporting_form.ExportingFormDetailRepository;
import com.thuctap.exporting_form.dto.ExportingFormDetailOverviewDTO;
import com.thuctap.setting.SettingRepository;
import com.thuctap.utility.EmailContent;
import com.thuctap.utility.EmailTemplateFactory;
import com.thuctap.utility.EmailUtility;



@Service
public class TransferMailHandler {

	@Autowired
	private ExportingFormDetailRepository exportingFormDetailRepository;
	
	@Autowired
	private SettingRepository settingRepository;
	
	
	@RabbitListener(queues = RabbitMQConfig.QUEUE_MAIL_TRANSFER_CREATED)
	public void handleTransferCreated(String content) {
		Integer exportingFormId = Integer.valueOf(content);
		
		List<ExportingFormDetailOverviewDTO> details = exportingFormDetailRepository.getAllDetailsById(exportingFormId);
		
		List<Setting> mailServer = settingRepository.findByType("MAIL_SERVER");
		
		List<Setting> transferMailTemplate = settingRepository.findByType("MAIL_TEMPLATE");
		
		
		Map<String,Object> data = new HashMap<>();
		
		data.put("formId",exportingFormId);
		data.put("details",details);
		data.put("mailTemplate",transferMailTemplate);
		
		JavaMailSender sender = EmailUtility.prepareMailSender(mailServer);
		
		EmailContent emailContent = EmailTemplateFactory.buildTemplate("TRANSFER", data);
		
		
		String fixedRecipient = "n21dcvt128@student.ptithcm.edu.vn";
		
		String from = mailServer.stream()
		        .filter(s -> "MAIL_FROM".equals(s.getKey()))
		        .findFirst()
		        .map(Setting::getValue)
		        .orElseThrow(() -> new IllegalArgumentException("Missing MAIL_FROM"));
		
		
		
		EmailUtility.sendMail(sender, from, fixedRecipient, emailContent.getSubject(), emailContent.getBody(), true);
		
		
		
		
	}
	
}
