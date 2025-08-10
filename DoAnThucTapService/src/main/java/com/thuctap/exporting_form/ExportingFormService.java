package com.thuctap.exporting_form;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.thuctap.common.exceptions.ExportingFormNotFoundException;
import com.thuctap.common.exceptions.TransporterNotFoundException;
import com.thuctap.common.exporting_form.ExportingForm;
import com.thuctap.common.exporting_form.ExportingFormDetail;
import com.thuctap.common.exporting_form.ExportingFormStatus;
import com.thuctap.common.exporting_form.ExportingFormStatusId;
import com.thuctap.common.exporting_form.QuotePriceData;
import com.thuctap.common.exporting_status.ExportingStatus;
import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.stocking.Stocking;
import com.thuctap.common.stocking.StockingId;
import com.thuctap.common.transporter.Transporter;
import com.thuctap.exporting_form.dto.CreateExportingFormDetailDTO;
import com.thuctap.exporting_form.dto.CreateExportingFormRequest;
import com.thuctap.exporting_form.dto.ExportingFormDetailOverviewDTO;
import com.thuctap.exporting_form.dto.ExportingFormOverviewDTO;
import com.thuctap.exporting_form.dto.ExportingFormStatusDTO;
import com.thuctap.exporting_form.dto.QuotePriceDataDTO;
import com.thuctap.exporting_form.dto.QuotePriceInformationAggregator;
import com.thuctap.stocking.StockingRepository;
import com.thuctap.transporter.TransporterRepository;

@Service
public class ExportingFormService {
	
	
	@Autowired
	private ExportingFormRepository exportingFormRepository;
	
	@Autowired
	private ExportingFormDetailRepository exportingFormDetailRepository;
	
	@Autowired
	private ExportingFormStatusRepository exportingFormStatusRepository;
	
	@Autowired
	private StockingRepository stockingRepository;
	
	@Autowired
	private ExportingStatusRepository exportingStatusRepository;
	
	@Autowired
	private TransporterRepository transporterRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	
	
	@Transactional()
	public void saveExportingForm(CreateExportingFormRequest request) {
		
		
		ExportingForm exportingForm = ExportingFormMapper.toExportingForm(request);
		
		ExportingForm savedExportingForm = exportingFormRepository.save(exportingForm);
		
		saveExportingFormDetail(savedExportingForm,request);
		
		updateStatus(savedExportingForm,1,true,true);
	}
	
	
	public ExportingFormOverviewDTO getExportingFormOverview(Integer formId) throws ExportingFormNotFoundException {
		checkWhetherExportingFormExist(formId);
		
		ExportingFormOverviewDTO result = exportingFormRepository.getExportingFormOverview(formId);
		
		if(Objects.isNull(result.getCommitEmployee())) {
			result.setCommitEmployee("");
		}
		
		setDetailForExportingForm(result,formId);
		
		
		return result;
	}
	
	public List<ExportingFormStatusDTO> getExportingFormStatus(Integer formId) throws ExportingFormNotFoundException{
		checkWhetherExportingFormExist(formId);
		
		List<ExportingFormStatus> statuses = exportingFormStatusRepository.getAllStatusBelongToOneExportingForm(formId);
		
		List<ExportingFormStatusDTO> statusesDTO = statuses.stream().map(ExportingFormMapper::toStatusDTO).toList();
		
		return statusesDTO;
	}
	
	
	@Transactional()
	public ExportingFormStatusDTO updateStatusAcceptAndTransferByTransporter(
			 Integer formId,
			 String transporterCode,
			 String secretKey,
			 QuotePriceData quotePriceData
			) throws ExportingFormNotFoundException, TransporterNotFoundException,IllegalStateException {
		
		ExportingForm exportingForm = getExportingForm(formId);
		
		Transporter transporter = getTransporter(transporterCode);
		
		authenticationForTransporter(transporter,secretKey);
		
		validateExportingFormAndTransporter(exportingForm,transporterCode);
		
		validateBeforeUpdateAcceptAndTransferStatus(formId);
		
		BigDecimal quoteShippingFee = calculateShippingFeeQuotedByTransporterWithDiscount(formId,quotePriceData);
		
		exportingForm.setQuoteShippingFee(quoteShippingFee);
		
		exportingForm.setQuotePriceData(quotePriceData);
		
		exportingFormRepository.save(exportingForm);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,2,false,false);
		
		return result;
	}
	
	
	public QuotePriceInformationAggregator getQuotePriceInformation(Integer formId) throws ExportingFormNotFoundException {
		
		ExportingForm exportingForm = getExportingForm(formId);
		
		QuotePriceData quotePriceData = exportingForm.getQuotePriceData();
		
		
		Long totalQuantity = exportingFormDetailRepository.getTotalQuantityByFormId(formId);
		
		QuotePriceInformationAggregator result = new QuotePriceInformationAggregator();
		result.setQuotePriceData(new QuotePriceDataDTO(quotePriceData));
		result.setTotalQuantity(totalQuantity);
		result.setTotalValueWithoutDiscount(calculateShippingFeeQuotedByTransporterWithoutDiscount(formId, quotePriceData));
		result.setTotalValueWithDiscount(calculateShippingFeeQuotedByTransporterWithDiscount(formId, quotePriceData));
		
		return result;
	}
	
	
	
	
	
	private void validateBeforeUpdateAcceptAndTransferStatus(Integer formId) {
		
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		
		ensureStatusNotContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This status already exist");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
	
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected by transporter");
	}


	private BigDecimal calculateShippingFeeQuotedByTransporterWithoutDiscount(Integer formId, QuotePriceData quotePriceData) {
		
		BigDecimal result = BigDecimal.ZERO;
		
		Long totalQuantity = exportingFormDetailRepository.getTotalQuantityByFormId(formId);
		
		result = result.add(quotePriceData.getPricePerItem().multiply(BigDecimal.valueOf(totalQuantity)));
		
		result = result.add(quotePriceData.getPricePerKilometer().multiply(BigDecimal.valueOf(quotePriceData.getTotalKilometer())));
		
		
		return result;
	}
	
	
	private BigDecimal calculateShippingFeeQuotedByTransporterWithDiscount(Integer formId, QuotePriceData quotePriceData) {
	    BigDecimal baseFee = calculateShippingFeeQuotedByTransporterWithoutDiscount(formId, quotePriceData);
	    
	    if (quotePriceData.getDiscount() == null || BigDecimal.ZERO.compareTo(quotePriceData.getDiscount()) == 0) {
	        return baseFee; 
	    }

	    BigDecimal discountFraction = quotePriceData.getDiscount()
	            .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
	    
	    return baseFee.subtract(baseFee.multiply(discountFraction)).setScale(2, RoundingMode.HALF_UP);


	}
	
	


	private void validateExportingFormAndTransporter(ExportingForm exportingForm,String transporterCodeFromRequest) {
		
		String transporterCodeFromForm = exportingForm.getTransporter().getTransporterCode();
		
		if(!transporterCodeFromForm.equals(transporterCodeFromRequest)) {
			throw new IllegalStateException("You do not responsible for this transfer form");
		}
	}


	private void authenticationForTransporter(Transporter transporter, String rawKey) {
		
		String encodedKey = transporter.getSecretKey();
		
		boolean isMatches = passwordEncoder.matches(rawKey, encodedKey);
		
		if(!isMatches) {
			throw new IllegalStateException("Invalid Secret Key");
		}
	}


	private Transporter getTransporter(String transporterCode) throws TransporterNotFoundException {
		Optional<Transporter> transporter = transporterRepository.findByTransporterCode(transporterCode);
		
		if(transporter.isEmpty()) {
			throw new TransporterNotFoundException("Not Exist Transporter With the given code " + transporterCode);
		}
		
		return transporter.get();
	}
	
	
	private void ensureStatusNotContains(Set<String> currentStatuses, String invalidStatus, String message) {
	    if (currentStatuses.contains(invalidStatus)) {
	        throw new IllegalStateException(message);
	    }
	}
	
	private void ensureStatusContains(Set<String> currentStatuses, String validStatus, String message) {
	    if (!currentStatuses.contains(validStatus)) {
	        throw new IllegalStateException(message);
	    }
	}
	
	
	private void setDetailForExportingForm(ExportingFormOverviewDTO result,Integer formId) {
		List<ExportingFormDetailOverviewDTO> details = exportingFormDetailRepository.getAllDetailsById(formId);
		
		result.setDetails(details);
	}
	
	
	private void checkWhetherExportingFormExist(Integer formId) throws ExportingFormNotFoundException {
		Boolean isExist = exportingFormRepository.existsById(formId);
		
		if(!isExist) {
			throw new ExportingFormNotFoundException("Not Exist Exporting Form with the given id " + formId);
		}
	}
	
	private ExportingForm getExportingForm(Integer formId) throws ExportingFormNotFoundException {
		
		Optional<ExportingForm> exportingFormOPT = exportingFormRepository.findById(formId);
		
		if(exportingFormOPT.isEmpty()) {
			throw new ExportingFormNotFoundException("Not Exist Exporting Form with the given id " + formId);
		}
		return exportingFormOPT.get();
		
	}
	
	
	
	
	private ExportingFormStatusDTO updateStatus(ExportingForm savedExportingForm,Integer statusId,Boolean isUpdatedByEmployee, Boolean isUpdatedByCreateEmployee) {
		
		ExportingStatus status = exportingStatusRepository.findById(statusId).get();
		
		ExportingFormStatus exportingFormStatus = new ExportingFormStatus();
		
		exportingFormStatus.setId(new ExportingFormStatusId(savedExportingForm.getId(), statusId));
		exportingFormStatus.setCreatedAt(LocalDateTime.now());
		exportingFormStatus.setExportingForm(savedExportingForm);
		exportingFormStatus.setStatus(status);
		
		if(isUpdatedByEmployee) {
			
			if(isUpdatedByCreateEmployee) {
				exportingFormStatus.setEmployee(savedExportingForm.getCreateEmployee());
			}else {
				exportingFormStatus.setEmployee(savedExportingForm.getReceiveEmployee());
			}

		}else {
			exportingFormStatus.setTransporter(savedExportingForm.getTransporter());
		}
		
		
		ExportingFormStatus savedExportingFormStatus = exportingFormStatusRepository.save(exportingFormStatus);
		
		
		return ExportingFormMapper.toStatusDTO(savedExportingFormStatus);
		
	}
	
	private void saveExportingFormDetail(ExportingForm savedExportingForm, CreateExportingFormRequest request) {
		
		List<ExportingFormDetail> details = new ArrayList<>();
		
		for(CreateExportingFormDetailDTO detailDTO : request.getDetails()) {
			
			ExportingFormDetail detail = ExportingFormMapper.toExportingFormDetail(detailDTO, savedExportingForm);
			details.add(detail);
			
		}
		
		exportingFormDetailRepository.saveAll(details);
		
		updateStockForMoveFromInventory(details,savedExportingForm.getMoveFromInventory());
		
		
	}

	private void updateStockForMoveFromInventory(List<ExportingFormDetail> details, Inventory moveFromInventory) {
		
		List<Stocking> stockings = new ArrayList<>();		
		Integer inventoryId = moveFromInventory.getId();
		
		for(ExportingFormDetail detail : details) {
			
			String sku = detail.getId().getSku();
			Integer number = detail.getQuantity();
			
			
			Stocking stocking = stockingRepository.findById(new StockingId(inventoryId, sku)).get();
			Integer numberAfterModifying = stocking.getQuantity() - number;
			
			if(numberAfterModifying < 0) {
				throw new IllegalStateException("Not Enough Quantity For This Product");
			}
			
			stocking.setQuantity(numberAfterModifying);
			
			stockings.add(stocking);
			
		}
		
		stockingRepository.saveAll(stockings);
		
		
	}
	
	
}
