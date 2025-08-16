package com.thuctap.utility;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thuctap.common.setting.Setting;
import com.thuctap.exporting_form.dto.ExportingFormDetailOverviewDTO;
import com.thuctap.inventory_order.dto.InventoryOrderDetailForOverviewDTO;

public class EmailTemplateFactory {
	
		private static final Logger log = LoggerFactory.getLogger(EmailUtility.class);
	
	 public static EmailContent buildTemplate(String type, Map<String, Object> data) {
	        switch (type) {
	            case "TRANSFER":
	                return buildTransferTemplate(data);
	            case "INVENTORY_ORDER":
	            	return buildOrderTemplate(data);
	            default:
	                throw new IllegalArgumentException("Unknown email type: " + type);
	        }
	 }
	 
	 
	 
	 private static EmailContent buildTransferTemplate(Map<String, Object> data) {
		 
			List<Setting> templateSettings = (List<Setting>) data.get("mailTemplate");

			Map<String, String> templateMap = templateSettings.stream()
					.collect(Collectors.toMap(Setting::getKey, Setting::getValue));

			String subjectTemplate = templateMap.get("TRANSFER_FORM_SUBJECT");
			String bodyTemplate = templateMap.get("TRANSFER_FORM_CONTENT");

			Integer formId = (Integer) data.get("formId");
			String subject = subjectTemplate.replace("<code>", String.valueOf(formId));

			List<ExportingFormDetailOverviewDTO> details = (List<ExportingFormDetailOverviewDTO>) data.get("details");

			StringBuilder rows = new StringBuilder();
			for (ExportingFormDetailOverviewDTO detail : details) {
				log.info("Detail Sku " + detail.getSku() + " Detail Quantity " + detail.getQuantity());
				rows.append("<tr>").append("<td>").append(detail.getSku()).append("</td>").append("<td>")
						.append(detail.getQuantity()).append("</td>").append("</tr>");
			}

			String body = bodyTemplate.replace("<!-- ROWS -->", rows.toString());

			return new EmailContent(subject, body);
	 }
	 
	 private static EmailContent buildOrderTemplate(Map<String,Object> data) {
		 
		 Integer formId = (Integer) data.get("formId");
		 
		 List<Setting> templateSettings = (List<Setting>) data.get("mailTemplate");
		 
		 List<InventoryOrderDetailForOverviewDTO> details = (List<InventoryOrderDetailForOverviewDTO>) data.get("details");
		 
		 Map<String, String> templateMap = templateSettings.stream()
					.collect(Collectors.toMap(Setting::getKey, Setting::getValue));
		 
		 String subjectTemplate = templateMap.get("ORDER_FORM_SUBJECT");
		 String bodyTemplate = templateMap.get("ORDER_FORM_CONTENT");
		 
		 String subject = subjectTemplate.replace("<code>", String.valueOf(formId));
		 
		 StringBuilder rows = new StringBuilder();
		 
		 for(InventoryOrderDetailForOverviewDTO detail : details) {
			 rows.append("<tr>")
			 					.append("<td>").append(detail.getSku()).append("</td>")
			 					.append("<td>").append(detail.getQuantity()).append("</td>")
			 					.append("<td>").append(detail.getExpectedPrice()).append("</td>")
			 	.append("</tr>");
		 }
		 
		 String body = bodyTemplate.replace("<!-- ROWS -->",rows.toString());
		 	
		 return new EmailContent(subject, body);
	 }
	 
	 
	 

}
