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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.thuctap.common.exceptions.ExportingFormNotFoundException;
import com.thuctap.common.exceptions.TransporterNotFoundException;
import com.thuctap.common.exporting_form.ExportingForm;
import com.thuctap.common.exporting_form.ExportingFormDetail;
import com.thuctap.common.exporting_form.ExportingFormStatus;
import com.thuctap.common.exporting_form.ExportingFormStatusId;
import com.thuctap.common.exporting_form.QuotePriceData;
import com.thuctap.common.exporting_status.ExportingStatus;
import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.common.stocking.Stocking;
import com.thuctap.common.stocking.StockingId;
import com.thuctap.common.transporter.Transporter;
import com.thuctap.event.EventPublisher;
import com.thuctap.event.RabbitMQConfig;
import com.thuctap.exporting_form.dto.CreateExportingFormDetailDTO;
import com.thuctap.exporting_form.dto.CreateExportingFormRequest;
import com.thuctap.exporting_form.dto.ExportingFormDetailOverviewDTO;
import com.thuctap.exporting_form.dto.ExportingFormOverviewDTO;
import com.thuctap.exporting_form.dto.ExportingFormPageAggregator;
import com.thuctap.exporting_form.dto.ExportingFormPageDTO;
import com.thuctap.exporting_form.dto.ExportingFormStatusDTO;
import com.thuctap.exporting_form.dto.QuotePriceDataDTO;
import com.thuctap.exporting_form.dto.QuotePriceInformationAggregator;
import com.thuctap.inventory_employee.InventoryEmployeeRepository;
import com.thuctap.product_variant.ProductVariantRepository;
import com.thuctap.stocking.StockingRepository;
import com.thuctap.transporter.TransporterRepository;
import com.thuctap.utility.UtilityGlobal;

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
	private ProductVariantRepository productVariantRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private InventoryEmployeeRepository inventoryEmployeeRepository;
	
	@Autowired
	private EventPublisher eventPublisher;
	
	@Autowired
	private ApplicationEventPublisher springPublisher;
	
	
	@Transactional()
	public void saveExportingForm(CreateExportingFormRequest request) {
		
		
		ExportingForm exportingForm = ExportingFormMapper.toExportingForm(request);
		
		ExportingForm savedExportingForm = exportingFormRepository.save(exportingForm);
		
		saveExportingFormDetail(savedExportingForm,request);
		
		updateStatus(savedExportingForm,1,true,true);
		
		springPublisher.publishEvent(new ExportingFormCreatedEvent(savedExportingForm.getId()));
	}
	
	
	public ExportingFormPageAggregator search(
			LocalDateTime startDate, 
			LocalDateTime endDate, 
			int pageNum, 
			int pageSize, 
			String sortField, 
			String sortDir) {
		
		Pageable pageable = UtilityGlobal.setUpPageRequest(pageNum,pageSize, sortField, sortDir);
		
		Integer inventoryId = UtilityGlobal.getInventoryIdOfCurrentLoggedUser();
		
		Page<ExportingFormPageDTO> pages = exportingFormRepository.search(startDate, endDate, inventoryId, pageable);
		
		ExportingFormPageAggregator result = new ExportingFormPageAggregator();
		
		result.setExportingForms(pages.getContent());
		result.setPageDTO(UtilityGlobal.buildPageDTO(sortField, sortDir, pages));
		
		return result;
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
	
	@Transactional()
	public ExportingFormStatusDTO updateReviewAndRejectedStatusByTransporter( Integer formId,
			 String transporterCode,
			 String secretKey,
			 QuotePriceData quotePriceData) throws ExportingFormNotFoundException, TransporterNotFoundException {
		
		ExportingForm exportingForm = getExportingForm(formId);
		
		Transporter transporter = getTransporter(transporterCode);
		
		authenticationForTransporter(transporter,secretKey);
		
		validateExportingFormAndTransporter(exportingForm,transporterCode);
		
		validateBeforeUpdateReviewAndRejectedStatus(formId);
		
		exportingForm.setQuotePriceData(quotePriceData);
		exportingFormRepository.save(exportingForm);
		
		updateStockBackToInventory(exportingForm);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,3,false,false);
		
		return result;
	}
	
	
	public ExportingFormStatusDTO updateAcceptPriceStatusByInventory(Integer formId) throws ExportingFormNotFoundException {
		
		ExportingForm exportingForm = getExportingForm(formId);
		
		validateOperator(exportingForm,true);
		
		validateBeforeUpdateStatusAcceptPrice(formId);

		exportingForm.setShippingFee(exportingForm.getQuoteShippingFee());
		
		exportingFormRepository.save(exportingForm);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,19,true,true);
		
		return result;
		
	}
	
	@Transactional()
	public ExportingFormStatusDTO updateRejectPriceStatusByInventory(Integer formId) throws ExportingFormNotFoundException {
		
		ExportingForm exportingForm = getExportingForm(formId);
		
		validateOperator(exportingForm,true);
		
		validateBeforeUpdateRejectPriceStatus(formId);
		
		updateStockBackToInventory(exportingForm);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,20,true,true);
		
		return result;
	}
	
	
	
	
	
	
	public ExportingFormStatusDTO updatePayingStatus(Integer formId) throws ExportingFormNotFoundException {
		
		ExportingForm exportingForm = getExportingForm(formId);
		
		validateOperator(exportingForm,true);
		
		validateBeforeUpdatePayingStatus(formId);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,4,true,true);
		
		return result;
	}
	
	public ExportingFormStatusDTO updatePayedStatus(Integer formId,
			 String transporterCode,
			 String secretKey) throws ExportingFormNotFoundException, TransporterNotFoundException {
		
		ExportingForm exportingForm = getExportingForm(formId);
		
		Transporter transporter = getTransporter(transporterCode);
		
		authenticationForTransporter(transporter,secretKey);
		
		validateExportingFormAndTransporter(exportingForm,transporterCode);
		
		validateBeforeUpdateStatusPayed(formId);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,5,false,false);
		
		return result;
	}
	
	public ExportingFormStatusDTO updateGivingProductStatus(Integer formId) throws ExportingFormNotFoundException {
		ExportingForm exportingForm = getExportingForm(formId);
		
		validateOperator(exportingForm,true);
		
		validateBeforeUpdateGivingProductStatus(formId);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,6,true,true);
		
		return result;
	}
	
	public ExportingFormStatusDTO updateShippingStatusByTransporter(
			 Integer formId,
			 String transporterCode,
			 String secretKey) throws ExportingFormNotFoundException, TransporterNotFoundException{
		ExportingForm exportingForm = getExportingForm(formId);
		
		Transporter transporter = getTransporter(transporterCode);
		
		authenticationForTransporter(transporter,secretKey);
		
		validateExportingFormAndTransporter(exportingForm,transporterCode);
		
		validateBeforeUpdateShippingStatus(formId);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,7,false,false);
		
		return result;	
	}
	
	
	public ExportingFormStatusDTO updateArrivingStatusByTransporter(
			 Integer formId,
			 String transporterCode,
			 String secretKey
			)throws ExportingFormNotFoundException, TransporterNotFoundException {
		ExportingForm exportingForm = getExportingForm(formId);
		
		Transporter transporter = getTransporter(transporterCode);
		
		authenticationForTransporter(transporter,secretKey);
		
		validateExportingFormAndTransporter(exportingForm,transporterCode);
		
		validateBeforeUpdateArrivingStatus(formId);
		
		ExportingFormStatusDTO result = updateStatus(exportingForm,8,false,false);
		
		return result;
	}
	
	
	@Transactional()
	public ExportingFormStatusDTO updateFinishStatusByInventory(Integer formId) throws ExportingFormNotFoundException {
		ExportingForm exportingForm = getExportingForm(formId);
		
		validateOperator(exportingForm,false);
		
		validateBeforeUpdateFinishStatus(formId);
		
		updateStockForMoveToInventory(exportingForm);
		
		exportingForm.setCompletedAt(LocalDateTime.now());
		InventoryEmployee inventoryEmployee = inventoryEmployeeRepository.findById(UtilityGlobal.getIdOfCurrentLoggedUser()).get();
		exportingForm.setReceiveEmployee(inventoryEmployee);
		
		ExportingForm savedExportingForm =  exportingFormRepository.save(exportingForm);
		
		ExportingFormStatusDTO result = updateStatus(savedExportingForm,9,true,false);
		
		return result;
	}
	
	public QuotePriceInformationAggregator getQuotePriceInformation(Integer formId) throws ExportingFormNotFoundException {
		
		ExportingForm exportingForm = getExportingForm(formId);

		boolean hasQuote = exportingFormStatusRepository.hasReviewDecisionOrReject(formId);
		QuotePriceInformationAggregator result = new QuotePriceInformationAggregator();
		result.setHasQuote(hasQuote);

		if (!hasQuote) {
			return result;
		}

		boolean isAccepted = exportingFormStatusRepository.hasReviewDecidedTransport(formId);
		result.setReject(!isAccepted);

		QuotePriceData quotePriceData = exportingForm.getQuotePriceData();
		result.setQuotePriceData(new QuotePriceDataDTO(quotePriceData));
		if (isAccepted) {
			Long totalQuantity = exportingFormDetailRepository.getTotalQuantityByFormId(formId);
			result.setTotalQuantity(totalQuantity);
			result.setTotalValueWithoutDiscount(
					calculateShippingFeeQuotedByTransporterWithoutDiscount(formId, quotePriceData));
			result.setTotalValueWithDiscount(
					calculateShippingFeeQuotedByTransporterWithDiscount(formId, quotePriceData));
		}
		return result;
	}
	
	
	private void validateBeforeUpdateFinishStatus(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"FINISH","This status already exist");
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected");
		
		ensureStatusNotContains(statusesSet,"REJECT_PRICE","The inventory has already been rejected this exporting form");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		ensureStatusContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form is not quoted by transpoter");
		
		ensureStatusContains(statusesSet,"ACCEPT_PRICE","This exporting form is not accepted price by the inventory");
		
		ensureStatusContains(statusesSet,"PAYING","This exporting form is not payed by inventory");
		
		ensureStatusContains(statusesSet,"PAYED","This exporting form is comfirmed paying by the transporter");
		
		ensureStatusContains(statusesSet,"GIVING_PRODUCT","The inventory does not give product to transporter");
		
		ensureStatusContains(statusesSet,"SHIPPING","The transporter is not shipping product to target inventory");
		
		ensureStatusContains(statusesSet,"ARRIVING","The transporter does not arrived at the target inventory");

	}
	
	
	private void validateBeforeUpdateArrivingStatus(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"ARRIVING","This status already exist");
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected");
		
		ensureStatusNotContains(statusesSet,"REJECT_PRICE","The inventory has already been rejected this exporting form");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		ensureStatusContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form is not quoted by transpoter");
		
		ensureStatusContains(statusesSet,"ACCEPT_PRICE","This exporting form is not accepted price by the inventory");
		
		ensureStatusContains(statusesSet,"PAYING","This exporting form is not payed by inventory");
		
		ensureStatusContains(statusesSet,"PAYED","This exporting form is comfirmed paying by the transporter");
		
		ensureStatusContains(statusesSet,"GIVING_PRODUCT","The inventory does not give product to transporter");
		
		ensureStatusContains(statusesSet,"SHIPPING","The transporter is not shipping product to target inventory");

	}
	
	private void validateBeforeUpdateShippingStatus(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"SHIPPING","This status already exist");
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected");
		
		ensureStatusNotContains(statusesSet,"REJECT_PRICE","The inventory has already been rejected this exporting form");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		ensureStatusContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form is not quoted by transpoter");
		
		ensureStatusContains(statusesSet,"ACCEPT_PRICE","This exporting form is not accepted price by the inventory");
		
		ensureStatusContains(statusesSet,"PAYING","This exporting form is not payed by inventory");
		
		ensureStatusContains(statusesSet,"PAYED","This exporting form is comfirmed paying by the transporter");
		
		ensureStatusContains(statusesSet,"GIVING_PRODUCT","The inventory does not give product to transporter");

	}
	
	private void validateBeforeUpdateGivingProductStatus(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"GIVING_PRODUCT","This status already exist");
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected");
		
		ensureStatusNotContains(statusesSet,"REJECT_PRICE","The inventory has already been rejected this exporting form");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		ensureStatusContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form is not quoted by transpoter");
		
		ensureStatusContains(statusesSet,"ACCEPT_PRICE","This exporting form is not accepted price by the inventory");
		
		ensureStatusContains(statusesSet,"PAYING","This exporting form is not payed by inventory");
		
		ensureStatusContains(statusesSet,"PAYED","This exporting form is comfirmed paying by the transporter");
	}
	
	private void validateBeforeUpdateRejectPriceStatus(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"REJECT_PRICE","This status already exist");
		
		ensureStatusNotContains(statusesSet,"ACCEPT_PRICE","The inventory has already been accept shipping price of this exporting form");
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		ensureStatusContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form is not quoted by transpoter");
		
	}
	
	private void validateBeforeUpdateStatusAcceptPrice(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"ACCEPT_PRICE","This status already exist");
		
		ensureStatusNotContains(statusesSet,"REJECT_PRICE","The inventory has already been rejected this exporting form");
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		ensureStatusContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form is not quoted by transpoter");
	}
	
	
	private void validateOperator(ExportingForm exportingForm, boolean operatedByEmployeeInFromInventory) {
		
		Integer inventoryIdOfCurrentLoggedUser = UtilityGlobal.getInventoryIdOfCurrentLoggedUser();
		
		if(operatedByEmployeeInFromInventory) {
			
			Integer inventoryIdOfFromInventory = exportingForm.getMoveFromInventory().getId();
			
			if(!inventoryIdOfCurrentLoggedUser.equals(inventoryIdOfFromInventory)) {
				throw new IllegalStateException("You do not have permission for this exporting form");
			}
			
			
		}else {
			
			Integer inventoryIdOfToInventory = exportingForm.getMoveToInventory().getId();
			
			if(!inventoryIdOfCurrentLoggedUser.equals(inventoryIdOfToInventory)) {
				throw new IllegalStateException("You do not have permission for this exporting form");
			}
			
			
		}
		
	}
	
	
	private void updateStockBackToInventory(ExportingForm exportingForm) {
		
		Inventory fromInventory = exportingForm.getMoveFromInventory();
		
		List<ExportingFormDetailOverviewDTO> details = exportingFormDetailRepository.getAllDetailsById(exportingForm.getId());
		
		List<Stocking> stockings = new ArrayList<>();
		
		for(ExportingFormDetailOverviewDTO detail : details) {
			String sku = detail.getSku();
			Integer quantity = detail.getQuantity();
			
			Stocking stocking = stockingRepository.findById(new StockingId(fromInventory.getId(), sku)).get();
			stocking.setQuantity(stocking.getQuantity() + quantity);
			stockings.add(stocking);
		}
		
		stockingRepository.saveAll(stockings);
	}
	
	private void validateBeforeUpdateStatusPayed(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"PAYED","This status already exist");
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected");
		
		ensureStatusNotContains(statusesSet,"REJECT_PRICE","This exporting form have already been rejected");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		ensureStatusContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form is not quoted by transpoter");
		
		ensureStatusContains(statusesSet,"ACCEPT_PRICE","This exporting form is not accepted price by the inventory");
		
		ensureStatusContains(statusesSet,"PAYING","This exporting form is not payed by inventory");

	}
	
	private void validateBeforeUpdatePayingStatus(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"PAYING","This status already exist");
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This exporting form have already been rejected");
		
		ensureStatusNotContains(statusesSet,"REJECT_PRICE","This exporting form have already been rejected");

		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		ensureStatusContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form is not quoted by transpoter");
		
		ensureStatusContains(statusesSet,"ACCEPT_PRICE","This exporting form is not accepted price by the inventory");

		
	}
	
	
	private void validateBeforeUpdateReviewAndRejectedStatus(Integer formId) {
		List<String> statuses = exportingFormStatusRepository.getStatusNamesByFormId(formId);
		
		Set<String> statusesSet = new HashSet<>(statuses);
		
		ensureStatusNotContains(statusesSet,"REVIEW_REJECT","This status already exist");
		
		ensureStatusNotContains(statusesSet,"REVIEW_DECIDED_TRANSPORT","This exporting form alreaduy be accepted");
		
		ensureStatusContains(statusesSet,"CREATED","This exporting form is not initialized");
		
		
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
	
	private void updateStockForMoveToInventory(ExportingForm exportingForm) {
		
		Inventory moveToInventory = exportingForm.getMoveToInventory();
		Integer inventoryId = moveToInventory.getId();
		
		List<ExportingFormDetailOverviewDTO> details = exportingFormDetailRepository.getAllDetailsById(exportingForm.getId());
		
		List<Stocking> stockings = new ArrayList<>();
		
		for(ExportingFormDetailOverviewDTO detail : details) {
			
			String sku = detail.getSku();
			
			Integer quantity = detail.getQuantity();
			
			Optional<ProductVariant> productVariant = productVariantRepository.findBySkuCode(sku);
			
			Optional<Stocking> stockingOPT = stockingRepository.findById(new StockingId(inventoryId, sku));
			
			if(stockingOPT.isEmpty()) {
				Stocking newStocking = new Stocking();
				newStocking.setId(new StockingId(inventoryId, sku));
				newStocking.setQuantity(quantity);
				newStocking.setInventory(moveToInventory);
				newStocking.setProductVariant(productVariant.get());
				stockings.add(newStocking);
			}else {
				Stocking oldStocking = stockingOPT.get();
				oldStocking.setQuantity(oldStocking.getQuantity() + quantity);
				stockings.add(oldStocking);
			}
		}
		
		stockingRepository.saveAll(stockings);
		
		
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
