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
import com.thuctap.inventory_order.InventoryOrderDetailRepository;
import com.thuctap.inventory_order.dto.InventoryOrderDetailForOverviewDTO;
import com.thuctap.setting.SettingRepository;
import com.thuctap.utility.EmailContent;
import com.thuctap.utility.EmailTemplateFactory;
import com.thuctap.utility.EmailUtility;

@Service
public class OrderMailHandler {

	@Autowired
	private InventoryOrderDetailRepository inventoryOrderDetailRepository;
	
	@Autowired
	private SettingRepository settingRepository;
	
	
	@RabbitListener(queues = RabbitMQConfig.QUEUE_MAIL_ORDER_CREATED)
	public void handlerOrderCreated(String content) {
		
		Integer inventoryOrderId = Integer.valueOf(content);
		
		List<InventoryOrderDetailForOverviewDTO> details = inventoryOrderDetailRepository.findAllDetailsBelongToAOrderForOverview(inventoryOrderId);
		
		List<Setting> mailServer = settingRepository.findByType("MAIL_SERVER");
		
		List<Setting> orderMailTemplate = settingRepository.findByType("MAIL_TEMPLATE");
		
		JavaMailSender sender = EmailUtility.prepareMailSender(mailServer);
		
		String fixedRecipient = "n21dcvt128@student.ptithcm.edu.vn";
		
		String from = mailServer.stream()
				.filter(s -> "MAIL_FROM".equals(s.getKey()))
				.findFirst()
				.map(Setting::getValue)
				.orElseThrow(() -> new IllegalArgumentException("Missing MAIL_FROM"));
		
		Map<String,Object> map = new HashMap<>();
		map.put("formId",inventoryOrderId);
		map.put("mailTemplate",orderMailTemplate);
		map.put("details",details);
		
		EmailContent emailContent = EmailTemplateFactory.buildTemplate("INVENTORY_ORDER", map);
		
		EmailUtility.sendMail(sender, from, fixedRecipient, emailContent.getSubject(), emailContent.getBody(), true);
		
		System.out.println("Send Mail When Order With ID" + content + " was created");
		
	}
	
}
