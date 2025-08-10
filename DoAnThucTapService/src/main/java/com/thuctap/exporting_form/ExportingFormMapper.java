package com.thuctap.exporting_form;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import com.thuctap.common.exporting_form.ExportingForm;
import com.thuctap.common.exporting_form.ExportingFormDetail;
import com.thuctap.common.exporting_form.ExportingFormDetailId;
import com.thuctap.common.exporting_form.ExportingFormStatus;
import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.transporter.Transporter;
import com.thuctap.exporting_form.dto.CreateExportingFormDetailDTO;
import com.thuctap.exporting_form.dto.CreateExportingFormRequest;
import com.thuctap.exporting_form.dto.ExportingFormStatusDTO;
import com.thuctap.utility.UtilityGlobal;

public class ExportingFormMapper {
	
	
	public static ExportingForm toExportingForm(CreateExportingFormRequest request) {
		
		ExportingForm exportingForm = new ExportingForm();
		Integer currentLoggedUserId = UtilityGlobal.getIdOfCurrentLoggedUser();
		Integer currentLoggedUserInventoryId = UtilityGlobal.getInventoryIdOfCurrentLoggedUser();
		
		exportingForm.setExportingFormCode("EFC_" + LocalDateTime.now().toString());
		exportingForm.setMoveToInventory(new Inventory(request.getInventory()));
		exportingForm.setMoveFromInventory(new Inventory(currentLoggedUserInventoryId));
		exportingForm.setTransporter(new Transporter(request.getTransporter()));
		exportingForm.setCreateEmployee(new InventoryEmployee(currentLoggedUserId));
		exportingForm.setCreatedAt(LocalDateTime.now());
		exportingForm.setCompletedAt(null);
		exportingForm.setShippingFee(BigDecimal.ZERO);
		exportingForm.setQuoteShippingFee(BigDecimal.ZERO);

		return exportingForm;
	}
	
	public static ExportingFormDetail toExportingFormDetail(CreateExportingFormDetailDTO detailDTO,ExportingForm savedExportingForm) {
		
		ExportingFormDetail detail = new ExportingFormDetail();
		
		detail.setId(new ExportingFormDetailId(savedExportingForm.getId(),detailDTO.getSku()));
		detail.setQuantity(detailDTO.getQuantity());
		detail.setExportingForm(savedExportingForm);
		
		
		return detail;
	}
	
	
	public static ExportingFormStatusDTO toStatusDTO(ExportingFormStatus status) {
		ExportingFormStatusDTO statusDTO = new ExportingFormStatusDTO();
		
		DateTimeFormatter formatter = UtilityGlobal.getVietnamDateTimeFormatter();
		String createdAtFormatted = status.getCreatedAt() != null
	            ? status.getCreatedAt().format(formatter)
	            : null;
		
		
		statusDTO.setCreatedAt(createdAtFormatted);
		statusDTO.setDescription(status.getStatus().getDescription());
		statusDTO.setStatus(status.getStatus().getName());
		
		if(!Objects.isNull(status.getEmployee())) {
			statusDTO.setUpdater(status.getEmployee().getFullName());
		}
		
		if(!Objects.isNull(status.getTransporter())) {
			statusDTO.setUpdater(status.getTransporter().getName());
		}
				
		return statusDTO;
	}
	
}
