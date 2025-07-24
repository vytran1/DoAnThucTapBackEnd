package com.thuctap.inventory_order;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.thuctap.audit.AuditRepository;
import com.thuctap.common.audit.Audit;
import com.thuctap.common.exceptions.OrderNotFoundException;
import com.thuctap.common.exceptions.ProductNotFoundException;
import com.thuctap.common.exceptions.ProductVariantSkuNotFoundException;
import com.thuctap.common.exceptions.StatusNotFoundException;
import com.thuctap.common.exceptions.SupplierNotFoundException;
import com.thuctap.common.importing_form.ImportingForm;
import com.thuctap.common.importing_form.ImportingFormDetail;
import com.thuctap.common.importing_status.ImportingStatus;
import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.inventory_order.InventoryOrder;
import com.thuctap.common.inventory_order.InventoryOrderDetail;
import com.thuctap.common.inventory_order.InventoryOrderDetailId;
import com.thuctap.common.inventory_order.InventoryOrderStatus;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.common.stocking.Stocking;
import com.thuctap.common.stocking.StockingId;
import com.thuctap.common.supplier.Supplier;
import com.thuctap.importing_form.ImportingFormDetailRepository;
import com.thuctap.importing_form.ImportingFormMapper;
import com.thuctap.importing_form.ImportingFormRepository;
import com.thuctap.importing_status.ImportingStatusRepository;
import com.thuctap.inventory.vytran.VInventoryRepository;
import com.thuctap.inventory_order.dto.InventoryOrderDetailForOverviewDTO;
import com.thuctap.inventory_order.dto.InventoryOrderDetailWithExpectedPriceDTO;
import com.thuctap.inventory_order.dto.InventoryOrderOverviewDTO;
import com.thuctap.inventory_order.dto.InventoryOrderPageDTO;
import com.thuctap.inventory_order.dto.InventoryOrderPageDTOAggregator;
import com.thuctap.inventory_order.dto.InventoryOrderSavingRequestAggregatorDTO;
import com.thuctap.inventory_order.dto.InventoryOrderStatusDTO;
import com.thuctap.inventory_order.dto.QuoteViewResponseDTO;
import com.thuctap.product_variant.ProductVariantRepository;
import com.thuctap.stocking.StockingRepository;
import com.thuctap.supplier.SupplierRepository;
import com.thuctap.utility.QuoteData;
import com.thuctap.utility.QuoteExcelReader;
import com.thuctap.utility.QuoteFileReader;
import com.thuctap.utility.QuoteItem;
import com.thuctap.utility.UtilityGlobal;





@Service
public class InventoryOrderService {
	
	@Autowired
	private InventoryOrderRepository orderRepository;
	
	@Autowired
	private InventoryOrderDetailRepository inventoryOrderDetailRepository;
	
	@Autowired
	private ProductVariantRepository productVariantRepository;
	
	@Autowired
	private ImportingStatusRepository importingStatusRepository;
	
	@Autowired
	private InventoryOrderStatusRepository inventoryOrderStatusRepository;
	
	@Autowired
	private VInventoryRepository vInventoryRepository;
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private StockingRepository stockingRepository;
	
	@Autowired
	private ImportingFormRepository importingFormRepository;
	
	@Autowired
	private ImportingFormDetailRepository importingFormDetailRepository;
	
	@Autowired
	private AuditRepository auditRepository;
	
	
	
	
	@Transactional(rollbackFor = {ProductVariantSkuNotFoundException.class,StatusNotFoundException.class})
	public void saveOrder(InventoryOrderSavingRequestAggregatorDTO requestDTO) throws ProductVariantSkuNotFoundException, StatusNotFoundException {
		
		
		InventoryOrder savedOrder = saveGeneralInformation(requestDTO);
			
		saveOrderDetails(requestDTO,savedOrder);
		
		saveInitStatus(savedOrder);
		
		
		
		
		
	}
	
	
	
	public InventoryOrderOverviewDTO getOverview(Integer id) throws OrderNotFoundException {
		
		Optional<InventoryOrder> orderOPT = orderRepository.findById(id);
		
		if(orderOPT.isEmpty()) {
			throw new OrderNotFoundException("Not Exist Order With Id: " + id);
		}
		
		
		InventoryOrderOverviewDTO resultDTO = setUpGeneralInformation(orderOPT.get());
		
		
		List<InventoryOrderDetailForOverviewDTO> orderDetails = inventoryOrderDetailRepository.findAllDetailsBelongToAOrderForOverview(id);
		
		resultDTO.setOrderDetails(orderDetails);
		
		int totalItems = calculateTotalItems(orderDetails);
		resultDTO.setLineItems(totalItems);
		
		
		return resultDTO;
	}
	
	
	public List<InventoryOrderStatusDTO> getStatus(Integer id){
		
		List<InventoryOrderStatus> listStatus = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(id);
		
		List<InventoryOrderStatusDTO> listStatusDTO = listStatus.stream().map(InventoryOrderMapper::toStatus).toList();
		
		return listStatusDTO;
	}
	
	@Transactional(rollbackFor = {IllegalArgumentException.class,IllegalStateException.class,Exception.class})
	public void quotePrice(Integer orderId, String supplierCode, MultipartFile quoteFile, String secretKey) throws Exception {
		
		InventoryOrder inventoryOrder = checkExistingOfOrder(orderId);
		
		Supplier supplier = validateSupplier(supplierCode, inventoryOrder, secretKey);
		
		validateOrder(inventoryOrder, supplier);
		
		QuoteData quote = parseQuoteFromFile(quoteFile,"ACCEPT");
		
		System.out.println(quote.getQuoteInformation().toString());
		
		List<QuoteItem> items = quote.getQuoteItems();
		
		for(QuoteItem item : items) {
			System.out.println(item.toString());
		}
		
		List<InventoryOrderDetail> orderDetails = inventoryOrderDetailRepository.findAllDetailsBelongToAOrder(orderId);
		
		validateItemInExcelFile(items, orderDetails);
		
		System.out.println("Shipping Fee " + quote.getQuoteInformation().getShippingFee().toString());
		
		inventoryOrder.setQuoteShippingFee(quote.getQuoteInformation().getShippingFee());
		orderRepository.save(inventoryOrder);
		
		updateQuotePrice(items,orderDetails);
		
		updateStatusForAccept(inventoryOrder);
		
		saveFile(quoteFile,inventoryOrder,"quote_file");
		
	}
	
	public void rejectQuotation(Integer orderId, String supplierCode, MultipartFile quoteFile, String secretKey) throws Exception  {
		InventoryOrder inventoryOrder = checkExistingOfOrder(orderId);
		
		Supplier supplier = validateSupplier(supplierCode, inventoryOrder, secretKey);
		
		validateOrder(inventoryOrder, supplier);
		
		
		QuoteData quote = parseQuoteFromFile(quoteFile,"REJECT");
		
		System.out.println(quote.getQuoteInformation().toString());
		
		updateStatusForReject(inventoryOrder);
		
		saveFile(quoteFile,inventoryOrder,"quote_file");
		
	}
	
	public QuoteViewResponseDTO getQuotationInformation(Integer orderId) throws OrderNotFoundException, FileNotFoundException {
		
		InventoryOrder orderOPT = checkExistingOfOrder(orderId);
		
		String quoteType = getQuoteStatusType(orderId);
		QuoteViewResponseDTO quoteInformation = new QuoteViewResponseDTO();
		if(quoteType.equals("PENDING")) {
			quoteInformation.setData(null);
			quoteInformation.setStatus(false);
			quoteInformation.setStatusType(quoteType);
			return quoteInformation;
		}
		
		File quoteFile = getQuoteFile(orderId);
		
		
		QuoteData data = parseQuoteFromFile(quoteFile, quoteType);
		
		
		
		quoteInformation.setData(data);
		quoteInformation.setStatus(quoteType.equals("ACCEPT"));
		quoteInformation.setStatusType(quoteType);
		
		if(quoteInformation.isStatus()) {
			List<InventoryOrderDetailForOverviewDTO> details = inventoryOrderDetailRepository.findAllDetailsBelongToAOrderForOverview(orderId);
			quoteInformation.setDetails(details);
		}
		
		return quoteInformation;
		
	}
	
	public void acceptQuotationByWarehouse(Integer orderId) throws OrderNotFoundException, IllegalStateException, StatusNotFoundException {
		
		InventoryOrder order = checkExistingOfOrder(orderId);
		
		checkValidationOfOrderBeforeUpdateCostPrice(orderId);
		
		List<InventoryOrderDetail> orderDetails = inventoryOrderDetailRepository.findAllDetailsBelongToAOrder(orderId);
		
		updateCostPrice(orderDetails);
		
		updateShippingFee(order);
		
		updateStatus(order,true,4);
		
	}
	
	public void rejectQuotationByWarehouse(Integer orderId) throws OrderNotFoundException, IllegalStateException, StatusNotFoundException {
		
		InventoryOrder order = checkExistingOfOrder(orderId);
		
		checkValidationOfOrderBeforeUpdateCostPrice(orderId);
		
		updateStatus(order,true,5);
		
	}
	
	public InventoryOrderStatusDTO rejectOrder(Integer orderId) throws OrderNotFoundException,IllegalStateException, StatusNotFoundException {
		
		InventoryOrder order = checkExistingOfOrder(orderId);
		
		checkValidationOrderBeforeRejectIt(orderId);
		
		InventoryOrderStatusDTO status = updateStatus(order,true,6);
		
		return status;
	}
	
	public InventoryOrderStatusDTO updatePayingStatus(Integer orderId) throws OrderNotFoundException,IllegalStateException, StatusNotFoundException {
		
		InventoryOrder order = checkExistingOfOrder(orderId);
		
		checkValidationOrderBeforeUpdatingPayingStatus(orderId);
		
		InventoryOrderStatusDTO status = updateStatus(order,true,7);
		
		return status;
		
	}
	
	public InventoryOrderStatusDTO confirmPayingBySupplier(Integer orderId, String supplierCode, String secretKey) throws OrderNotFoundException, SupplierNotFoundException, StatusNotFoundException {
		
		InventoryOrder inventoryOrder = checkExistingOfOrder(orderId);
		
		Supplier supplier = checkExistingOfSupplier(supplierCode);
		
		validateSupplier(supplierCode, inventoryOrder, secretKey);
		
		checkValidationOrderBeforeConfirmingPayingBySupplier(orderId);	
		
		InventoryOrderStatusDTO status = updateStatus(inventoryOrder,false,8);
		
		return status;
		
	}
	
	public InventoryOrderStatusDTO updateShippingStatus(Integer orderId, String supplierCode, String secretKey) throws OrderNotFoundException, SupplierNotFoundException, StatusNotFoundException {
		
		InventoryOrder inventoryOrder = checkExistingOfOrder(orderId);
		
		Supplier supplier = checkExistingOfSupplier(supplierCode);
		
		validateSupplier(supplierCode, inventoryOrder, secretKey);
		
		checkValidationOrderBeforeUpdatingShippingStatusBySupplier(orderId);
		
		InventoryOrderStatusDTO status = updateStatus(inventoryOrder,false,9);
		
		return status;
	}
	
	public InventoryOrderStatusDTO updateArrivingStatus(Integer orderId, String supplierCode, String secretKey, MultipartFile invoiceFile) throws OrderNotFoundException, SupplierNotFoundException, StatusNotFoundException,IllegalArgumentException,Exception {
		InventoryOrder inventoryOrder = checkExistingOfOrder(orderId);
		
		Supplier supplier = checkExistingOfSupplier(supplierCode);
		
		validateSupplier(supplierCode, inventoryOrder, secretKey);
		
		checkValidationOrderBeforeUpdatingArrivingStatusBySupplier(orderId);
		
		checkInvoiceFileSheets(invoiceFile);
		
		InventoryOrderStatusDTO status = updateStatus(inventoryOrder,false,10);
		
		saveFile(invoiceFile, inventoryOrder,"invoice_");
		
		return status;
	}
	
	
	
	public InventoryOrderStatusDTO updateCheckedStatus(Integer orderId) throws OrderNotFoundException, StatusNotFoundException {
		InventoryOrder inventoryOrder = checkExistingOfOrder(orderId);
		
		checkValidationOrderBeforeUpdatingCheckedStatusByEmployee(orderId);
		
		InventoryOrderStatusDTO status = updateStatus(inventoryOrder,true,11);
		
		return status;

	}
	

	@Transactional(rollbackFor = {Exception.class, RuntimeException.class, Throwable.class})
	public InventoryOrderStatusDTO updateFinishStatus(Integer orderId) throws OrderNotFoundException, StatusNotFoundException {
		
		InventoryOrder inventoryOrder = checkExistingOfOrder(orderId);
		
		checkValidationOrderBeforeUpdatingFinishedStatusByEmployee(orderId);
		
		List<InventoryOrderDetail> details = inventoryOrderDetailRepository.findAllDetailsBelongToAOrder(orderId);
		
		updateCompletedAtOfOrder(inventoryOrder);
		
		updateStock(inventoryOrder,details);
		
		createImportingForm(inventoryOrder,details);
		
		List<String> listSkuCode = details.stream().map((detail) -> detail.getProductVariant().getSku()).toList();
		
		updateProductPrice(listSkuCode);
		
		InventoryOrderStatusDTO result = updateStatus(inventoryOrder,true,12);
		
		return result;
		
	}
	
	public InventoryOrderPageDTOAggregator search(LocalDateTime startDate, 
												LocalDateTime endDate,
												int pageNum,
												int pageSize,
												String sortField,
												String sortDir
			) {
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
		
		Page<InventoryOrderPageDTO> pages = orderRepository.search(startDate, endDate, pageable);
		
		InventoryOrderPageDTOAggregator aggregator = new InventoryOrderPageDTOAggregator();
		aggregator.setOrders(pages.getContent());
		aggregator.setPage(UtilityGlobal.buildPageDTO(sortField, sortDir, pages));
		
		return aggregator;
	}
	
	private void updateProductPrice(List<String> listSkuCode) {
		for (String sku : listSkuCode) {
			List<BigDecimal> prices = importingFormDetailRepository.findRecentCostPricesBySku(sku,
					PageRequest.of(0, 5));

			if (prices.isEmpty())
				continue;

			BigDecimal sum = prices.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal avg = sum.divide(BigDecimal.valueOf(prices.size()),RoundingMode.HALF_UP);

			ProductVariant variant = productVariantRepository.findProductVariantIdBySkuCode(sku).get();
			BigDecimal oldPrice = variant.getPrice();
			
			variant.setPrice(avg);
			
			ProductVariant savedProduct =productVariantRepository.save(variant);
			
			if(savedProduct != null) {
				saveChangesForProductVariantPrice(sku, oldPrice,avg);
			}
		
		}

	}
	
	private void saveChangesForProductVariantPrice(String sku, BigDecimal oldPrice, BigDecimal newPrice) {
		
		Audit audit = new Audit();
		audit.setAction("UPDATE");
		audit.setCreatedAt(LocalDateTime.now());
		audit.setFieldName("price");
		audit.setInventoryEmployee(new InventoryEmployee(UtilityGlobal.getIdOfCurrentLoggedUser()));
		audit.setIsDelete(false);
		audit.setNewValue(newPrice.toString());
		audit.setOldValue(oldPrice.toString());
		audit.setRecordId(sku);
		audit.setTableName("ProductVariant");
		
		auditRepository.save(audit);
		
	}



	private void updateCompletedAtOfOrder(InventoryOrder order) {
		order.setCompletedAt(LocalDateTime.now());
		orderRepository.save(order);
	}
	
	private void updateStock(InventoryOrder inventoryOrder,List<InventoryOrderDetail> details) {
		
		Integer orderId = inventoryOrder.getId();
		
		Inventory inventory = inventoryOrder.getInventory();
		
		Integer inventoryId = inventory.getId();
		
		
		
		List<Stocking> stockings = new ArrayList<>();
		
		for(InventoryOrderDetail detail : details) {
			
			ProductVariant productVariant = detail.getProductVariant();
			
			Optional<Stocking> stockingOPT = stockingRepository.findById(new StockingId(inventoryId,productVariant.getSku()));
			
			if(stockingOPT.isEmpty()) {
				Stocking newStocking = new Stocking();
				newStocking.setId(new StockingId(inventoryId,productVariant.getSku()));
				newStocking.setInventory(inventory);
				newStocking.setProductVariant(productVariant);
				newStocking.setQuantity(detail.getQuantity());
				stockings.add(newStocking);
			}else {
				Stocking existingStocking = stockingOPT.get();
				existingStocking.setQuantity(existingStocking.getQuantity() + detail.getQuantity());
			}
			
		}
		
		stockingRepository.saveAll(stockings);
	}
	
	private void createImportingForm(InventoryOrder inventoryOrder,List<InventoryOrderDetail> details) {
		
		ImportingForm importingForm = ImportingFormMapper.toImportingForm(inventoryOrder);
		
		ImportingForm savedImportingForm = importingFormRepository.save(importingForm);
		
		List<ImportingFormDetail> importingFormDetails = details.stream().map((detail) -> ImportingFormMapper.toImportingFormDetail(savedImportingForm,detail)).toList();
		
		importingFormDetailRepository.saveAll(importingFormDetails);
	}
	
	
	private void checkInvoiceFileSheets(MultipartFile invoiceFile) throws IllegalArgumentException, Exception {
		// TODO Auto-generated method stub
		QuoteExcelReader excelReader = new QuoteExcelReader();
		
		excelReader.validateInvoiceFileHasRequiredSheets(invoiceFile.getInputStream());
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
	
	private void checkValidationOrderBeforeUpdatingFinishedStatusByEmployee(Integer orderId) {
		List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);
		
		Set<String> currentStatusNames = statuses.stream().map(s -> s.getStatus().getName())
				.collect(Collectors.toSet());
		
		this.ensureStatusNotContains(currentStatusNames,"FINISH","Cannot operate this action. Because supplier already update status FINISH");
		
		this.ensureStatusNotContains(currentStatusNames,"REJECT_ORDER","Cannot operate this action. The order has been rejected.");
		
		this.ensureStatusContains(currentStatusNames,"REVIEWED_DECIDED_PRICE","Cannot proceed with payment. The price has not been reviewed.");

		this.ensureStatusContains(currentStatusNames,"ACCEPTED_PRICE","Cannot proceed with payment. The price has not been accepted.");
		
		this.ensureStatusNotContains(currentStatusNames,"REVIEWED_REJECT","Cannot proceed with payment. The supplier has already refused to support this order.");

		this.ensureStatusContains(currentStatusNames,"PAYING","Cannot proceed. The warehouse has not initiated payment for this order.");

		this.ensureStatusContains(currentStatusNames,"PAYED","Cannot proceed. The supplier has not confirmed payment.");

		this.ensureStatusContains(currentStatusNames,"SHIPPING","Cannot proceed. The supplier has not updated shipping status.");

		this.ensureStatusContains(currentStatusNames,"ARRIVING","Cannot proceed. The supplier has not deliver goods to warehouse.");
		
		this.ensureStatusContains(currentStatusNames,"CHECKED","Cannot proceed. The warehouse has not checked the goods");

	}
	
	private void checkValidationOrderBeforeUpdatingCheckedStatusByEmployee(Integer orderId) {
		List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);
		
		Set<String> currentStatusNames = statuses.stream().map(s -> s.getStatus().getName())
				.collect(Collectors.toSet());
		
		this.ensureStatusNotContains(currentStatusNames,"CHECKED","Cannot operate this action. Because supplier already update status CHECKED");
		
		this.ensureStatusNotContains(currentStatusNames,"REJECT_ORDER","Cannot operate this action. The order has been rejected.");

		this.ensureStatusContains(currentStatusNames,"REVIEWED_DECIDED_PRICE","Cannot proceed with payment. The price has not been reviewed.");

		this.ensureStatusContains(currentStatusNames,"ACCEPTED_PRICE","Cannot proceed with payment. The price has not been accepted.");
		
		this.ensureStatusNotContains(currentStatusNames,"REVIEWED_REJECT","Cannot proceed with payment. The supplier has already refused to support this order.");
		
		this.ensureStatusContains(currentStatusNames,"PAYING","Cannot proceed. The warehouse has not initiated payment for this order.");
		
		this.ensureStatusContains(currentStatusNames,"PAYED","Cannot proceed. The supplier has not confirmed payment.");
		
		this.ensureStatusContains(currentStatusNames,"SHIPPING","Cannot proceed. The supplier has not updated shipping status.");
		
		this.ensureStatusContains(currentStatusNames,"ARRIVING","Cannot proceed. The supplier has not deliver goods to warehouse.");

	}
	
	private void checkValidationOrderBeforeUpdatingArrivingStatusBySupplier(Integer orderId) {
		List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);
		
		Set<String> currentStatusNames = statuses.stream().map(s -> s.getStatus().getName())
				.collect(Collectors.toSet());
		
		this.ensureStatusNotContains(currentStatusNames,"ARRIVING","Cannot operate this action. Because supplier already update status SHIPPING");

		this.ensureStatusNotContains(currentStatusNames,"REJECT_ORDER","Cannot operate this action. The order has been rejected.");

		this.ensureStatusContains(currentStatusNames,"REVIEWED_DECIDED_PRICE","Cannot proceed with payment. The price has not been reviewed.");

		this.ensureStatusContains(currentStatusNames,"ACCEPTED_PRICE","Cannot proceed with payment. The price has not been accepted.");

		this.ensureStatusNotContains(currentStatusNames,"REVIEWED_REJECT","Cannot proceed with payment. The supplier has already refused to support this order.");

		this.ensureStatusContains(currentStatusNames,"PAYING","Cannot proceed. The warehouse has not initiated payment for this order.");

		this.ensureStatusContains(currentStatusNames,"PAYED","Cannot proceed. The supplier has not confirmed payment.");
		
		this.ensureStatusContains(currentStatusNames,"SHIPPING","Cannot proceed. The supplier has not updated shipping status.");


	}
	
	private void checkValidationOrderBeforeUpdatingShippingStatusBySupplier(Integer orderId) {
		List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);
		
		Set<String> currentStatusNames = statuses.stream().map(s -> s.getStatus().getName())
				.collect(Collectors.toSet());
		
		this.ensureStatusNotContains(currentStatusNames,"SHIPPING","Cannot operate this action. Because supplier already update status SHIPPING");
		
		this.ensureStatusNotContains(currentStatusNames,"REJECT_ORDER","Cannot operate this action. The order has been rejected.");
		
		this.ensureStatusContains(currentStatusNames,"REVIEWED_DECIDED_PRICE","Cannot proceed with payment. The price has not been reviewed.");
		
		this.ensureStatusContains(currentStatusNames,"ACCEPTED_PRICE","Cannot proceed with payment. The price has not been accepted.");
		
		this.ensureStatusNotContains(currentStatusNames,"REVIEWED_REJECT","Cannot proceed with payment. The supplier has already refused to support this order.");
		
		this.ensureStatusContains(currentStatusNames,"PAYING","Cannot proceed. The warehouse has not initiated payment for this order.");
		
		this.ensureStatusContains(currentStatusNames,"PAYED","Cannot proceed. The supplier has not confirmed payment.");
	}
	
	private void checkValidationOrderBeforeConfirmingPayingBySupplier(Integer orderId) {
		List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);

		Set<String> currentStatusNames = statuses.stream().map(s -> s.getStatus().getName())
				.collect(Collectors.toSet());
		
		if(currentStatusNames.contains("PAYED")) {
			throw new IllegalStateException("Cannot operate this action. Because the supplier already confirmed");
		}
		
		if (currentStatusNames.contains("REJECT_ORDER")) {
			throw new IllegalStateException("Cannot operate this action. The order has been rejected.");
		}
		
		if (!currentStatusNames.contains("REVIEWED_DECIDED_PRICE")) {
			throw new IllegalStateException("Cannot proceed with payment. The price has not been reviewed.");
		}
		
		if (!currentStatusNames.contains("ACCEPTED_PRICE")) {
			throw new IllegalStateException("Cannot proceed with payment. The price has not been accepted.");
		}
		
		if (currentStatusNames.contains("REVIEWED_REJECT")) {
			throw new IllegalStateException(
					"Cannot proceed with payment. The supplier has already refused to support this order.");
		}
		
		if(!currentStatusNames.contains("PAYING")) {
			throw new IllegalStateException("Cannot proceed. The warehouse has not initiated payment for this order.");
		}
		
		
		
		
	}



	private void checkValidationOrderBeforeUpdatingPayingStatus(Integer orderId) {
		List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);

		Set<String> currentStatusNames = statuses.stream().map(s -> s.getStatus().getName())
				.collect(Collectors.toSet());
		
		if(currentStatusNames.contains("PAYING")) {
			throw new IllegalStateException("Cannot proceed with payment. The order has been paying.");
		}
		
		if (currentStatusNames.contains("REJECT_ORDER")) {
			throw new IllegalStateException("Cannot proceed with payment. The order has been rejected.");
		}

		if (currentStatusNames.contains("REVIEWED_REJECT")) {
			throw new IllegalStateException(
					"Cannot proceed with payment. The supplier already has refuse to support this order");
		}

		if (!currentStatusNames.contains("REVIEWED_DECIDED_PRICE")) {
			throw new IllegalStateException("Cannot proceed with payment. The price has not been reviewed.");
		}
		
		if (currentStatusNames.contains("REJECT_PRICE")) {
			throw new IllegalStateException("Cannot proceed with payment. The price has been rejected.");
		}

		if (!currentStatusNames.contains("ACCEPTED_PRICE")) {
			throw new IllegalStateException("Cannot proceed with payment. The price has not been accepted.");
		}
	}
	
	private void checkValidationOrderBeforeRejectIt(Integer orderId) {
		
		List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);
		
		boolean hasAlreadyCanceled = statuses.stream().anyMatch(s -> s.getStatus().getName().equals("REJECT_ORDER"));
		
		if(hasAlreadyCanceled) {
			throw new IllegalStateException("This order already be canceled");
		}
		
		Set<String> forbiddenStatuses = Set.of("PAYING", "PAYED", "SHIPPING", "ARRIVING");
		
		boolean hasForbiddenStatus = statuses.stream()
			        .anyMatch(s -> forbiddenStatuses.contains(s.getStatus().getName()));
		
		if (hasForbiddenStatus) {
		        throw new IllegalStateException("This order cannot be rejected because it is already in progress or completed.");
		}
		
		
		
	}



	private InventoryOrderStatusDTO updateStatus(InventoryOrder order, boolean isUpdatedByEmployee, Integer statusId) throws StatusNotFoundException {
		
		
		Optional<ImportingStatus> statusOPT = importingStatusRepository.findById(statusId);
		
		if(statusOPT.isEmpty()) {
			throw new StatusNotFoundException("Not Exist Status With Id " + statusId);
		}
		
		InventoryOrderStatus orderStatus = new InventoryOrderStatus();
		
		orderStatus.setCreatedAt(LocalDateTime.now());
		
		if(isUpdatedByEmployee) {
			Integer loggedUserId = UtilityGlobal.getIdOfCurrentLoggedUser();
			orderStatus.setEmployee(new InventoryEmployee(loggedUserId));
		}else {
			orderStatus.setSupplier(order.getSupplier());
		}
		
		orderStatus.setOrder(order);
		orderStatus.setStatus(statusOPT.get());
		
		InventoryOrderStatus savedStatus = inventoryOrderStatusRepository.save(orderStatus);
		
		return InventoryOrderMapper.toStatus(savedStatus);
	}



	private void updateShippingFee(InventoryOrder order) {
		order.setShippingFee(order.getQuoteShippingFee());
		
		orderRepository.save(order);
	}
	
	private void updateCostPrice(List<InventoryOrderDetail> orderDetails) {
		
		for(InventoryOrderDetail detail : orderDetails) {
			detail.setCostPrice(detail.getQuoteCostPrice());
		}
		
		this.inventoryOrderDetailRepository.saveAll(orderDetails);
		
	}



	private void checkValidationOfOrderBeforeUpdateCostPrice(Integer orderId) {
		
		List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);
		
		boolean hasQuoteReview = statuses.stream().anyMatch(s -> s.getStatus().getName().equals("REVIEWED_DECIDED_PRICE"));
		
		boolean alreadyAccepted = statuses.stream().anyMatch(s -> s.getStatus().getName().equals("ACCEPTED_PRICE"));

		boolean alreadyRejected = statuses.stream().anyMatch(s -> s.getStatus().getName().equals("REJECT_PRICE"));

		boolean alreadyCancelled = statuses.stream().anyMatch(s -> s.getStatus().getName().equals("REJECT_ORDER"));
		
		
		if (!hasQuoteReview) {
			throw new IllegalStateException("This order has not received any quotation yet.");
		}
		
		if (alreadyAccepted) {
			throw new IllegalStateException("Order already has accepted quote.");
		}

		if (alreadyRejected) {
			throw new IllegalStateException("Order quote was already rejected.");
		}

		if (alreadyCancelled) {
			throw new IllegalStateException("Cannot accept quote for a cancelled order.");
		}
		
	}
	
	
	
	
	private QuoteData parseQuoteFromFile(File quoteFile, String type) {
	    try (InputStream inputStream = new FileInputStream(quoteFile)) {
	        QuoteFileReader reader = new QuoteExcelReader();
	        return switch (type) {
	            case "ACCEPT" -> reader.readQuoteAcceptData(inputStream);
	            case "REJECT" -> reader.readQuoteRejectData(inputStream);
	            default -> throw new IllegalArgumentException("Invalid quote type");
	        };
	    } catch (IOException | IllegalArgumentException e) {
	        throw new RuntimeException("Failed to read quote file: " + e.getMessage(), e);
	    }
	}
	
	
	private String getQuoteStatusType(Integer orderId) {
	    List<InventoryOrderStatus> statuses = inventoryOrderStatusRepository.getAllStatusBelongToOneOrder(orderId);
	    
	    if (statuses.stream().anyMatch(s -> s.getStatus().getName().equals("REVIEWED_DECIDED_PRICE"))) {
	        return "ACCEPT";
	    } else if (statuses.stream().anyMatch(s -> s.getStatus().getName().equals("REVIEWED_REJECT"))) {
	        return "REJECT";
	    }else {
	    	return "PENDING";
	    }
	    
	}
	
	private File getQuoteFile(Integer orderId) throws FileNotFoundException {
		String folderPath = "C:\\DoAnThucTapImages\\InventoryOrder\\" + orderId;

		File folder = new File(folderPath);

		if (!folder.exists() || !folder.isDirectory()) {
			throw new FileNotFoundException("Folder not found for order " + orderId);
		}

		File[] quoteFiles = folder.listFiles((dir, name) -> name.startsWith("quote_file"));
		if (quoteFiles == null || quoteFiles.length == 0) {
			throw new FileNotFoundException("No quote file found for order " + orderId);
		}

		File quoteFile = quoteFiles[0];
		
		return quoteFile;
	}
	
	



	private void saveFile(MultipartFile quoteFile, InventoryOrder inventoryOrder,String prefix) throws Exception {
		// TODO Auto-generated method stub
		String baseFolder = "C:\\DoAnThucTapImages\\InventoryOrder";
		String orderFolder = baseFolder + File.separator + inventoryOrder.getId();
		
		File directory = new File(orderFolder);
		if (!directory.exists()) {
		    directory.mkdirs();
		}
		
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String filename = prefix + timestamp + ".xlsx";
		
		File destinationFile = new File(orderFolder + File.separator + filename);
		try {
			quoteFile.transferTo(destinationFile);
			String relativePath = "/" + filename;
			inventoryOrder.setQuoteFile(relativePath);;
			orderRepository.save(inventoryOrder);
		} catch (IllegalStateException e) {
			throw new Exception(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Exception(e.getMessage());
		}
	}



	private void updateStatusForAccept(InventoryOrder inventoryOrder) throws StatusNotFoundException {
		
		Optional<ImportingStatus> statusOPT = importingStatusRepository.findById(2);
		
		if(statusOPT.isEmpty()) {
			throw new StatusNotFoundException("Not Found Status With ID " + 2);
		}
		
		InventoryOrderStatus orderStatus = new InventoryOrderStatus();
		orderStatus.setCreatedAt(LocalDateTime.now());
		orderStatus.setSupplier(inventoryOrder.getSupplier());
		orderStatus.setStatus(statusOPT.get());
		orderStatus.setOrder(inventoryOrder);
		
		inventoryOrderStatusRepository.save(orderStatus);
	}
	
	private void updateStatusForReject(InventoryOrder inventoryOrder) throws StatusNotFoundException {
		
		Optional<ImportingStatus> statusOPT = importingStatusRepository.findById(3);
		
		if(statusOPT.isEmpty()) {
			throw new StatusNotFoundException("Not Found Status With ID " + 3);
		}
		
		InventoryOrderStatus orderStatus = new InventoryOrderStatus();
		orderStatus.setCreatedAt(LocalDateTime.now());
		orderStatus.setSupplier(inventoryOrder.getSupplier());
		orderStatus.setStatus(statusOPT.get());
		orderStatus.setOrder(inventoryOrder);
		
		inventoryOrderStatusRepository.save(orderStatus);
	}



	private void updateQuotePrice(List<QuoteItem> items, List<InventoryOrderDetail> orderDetails) {
		
		Map<String, BigDecimal> skuToQuotePrice = items.stream()
			    .collect(Collectors.toMap(QuoteItem::getSku,QuoteItem::getQuotedPrice));
		
		for (InventoryOrderDetail detail : orderDetails) {
		    BigDecimal quotePrice = skuToQuotePrice.get(detail.getId().getSku());
		    if (quotePrice != null) {
		        detail.setQuoteCostPrice(quotePrice);  
		    }
		}
		
		inventoryOrderDetailRepository.saveAll(orderDetails);
	}



	private void validateItemInExcelFile(List<QuoteItem> items,List<InventoryOrderDetail> orderDetails) {
		Set<String> validSkus = orderDetails.stream().map(detail -> detail.getId().getSku())
				.collect(Collectors.toSet());

		Set<String> invalidSkus = new HashSet();

		for (QuoteItem item : items) {
			if (!validSkus.contains(item.getSku())) {
				invalidSkus.add(item.getSku());
			}
		}

		if (!invalidSkus.isEmpty()) {
			throw new IllegalArgumentException("The file contains SKUs that do not belong to the order: " + invalidSkus);
		}
	}
	
	private QuoteData parseQuoteFromFile(MultipartFile quoteFile,String type) {
	    try (InputStream inputStream = quoteFile.getInputStream()) {
	        QuoteFileReader reader = new QuoteExcelReader();
	        if(type == "ACCEPT") {	        	
	        	return reader.readQuoteAcceptData(inputStream);
	        }else if(type == "REJECT") {
	        	return reader.readQuoteRejectData(inputStream);
	        }else {
	        	return null;
	        }
	    } 
	    catch (IllegalArgumentException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	    catch (IOException | RuntimeException e) {
	        throw new RuntimeException("Failed to process quote file", e);
	    }
	}
	
	
	
	private Supplier validateSupplier(String supplierCode, InventoryOrder inventoryOrder, String secretKey) throws SupplierNotFoundException {
		
		Supplier supplier = checkExistingOfSupplier(supplierCode);
		
		if (!inventoryOrder.getSupplier().getId().equals(supplier.getId())) {
			throw new IllegalStateException("This supplier is not assigned to the order and cannot submit a quotation.");
		}
		
		if(Objects.equals(secretKey,null)) {
			throw new IllegalStateException("Not Found Secret Key");
		}
		
		if (!passwordEncoder.matches(secretKey, supplier.getSecretKey())) {
		    throw new IllegalStateException("Invalid secret key.");
		}
		
		return supplier;
	}
	
	
	private void validateOrder(InventoryOrder inventoryOrder,Supplier supplier ) {
		String currentStatusOfOrder = inventoryOrderStatusRepository.getCurrentStatusByOrderId(inventoryOrder.getId());
		
		if ("REJECT_ORDER".equalsIgnoreCase(currentStatusOfOrder)) {
		    throw new IllegalStateException("The order has been canceled and cannot be quoted.");
		}
		
		boolean isResponded = inventoryOrderStatusRepository.hasSupplierAlreadyResponded(inventoryOrder.getId(),supplier.getId());
		
		if(isResponded) {
			throw new IllegalStateException("The supplier has already responded to this order and cannot perform any further actions.");
		}
	}
	
	
	private InventoryOrder checkExistingOfOrder(Integer id) throws OrderNotFoundException {
		Optional<InventoryOrder> orderOPT = orderRepository.findById(id);
		
		if(orderOPT.isEmpty()) {
			throw new OrderNotFoundException("Not Exist Order With Id: " + id);
		}
		
		return orderOPT.get();
	}
	
	private Supplier checkExistingOfSupplier(String supplierCode) throws SupplierNotFoundException {
		
		Optional<Supplier> supplierOPT = supplierRepository.findBySupplierCode(supplierCode);
		
		if(supplierOPT.isEmpty()) {
			throw new SupplierNotFoundException("Not Exist Supplier With Supplier Code: " + supplierCode);
		}
		
		return supplierOPT.get();
		
	}
	
	
	
	
	
	
	private InventoryOrderOverviewDTO setUpGeneralInformation(InventoryOrder orderOPT) {
		InventoryOrderOverviewDTO resultDTO = new InventoryOrderOverviewDTO();
		resultDTO.setOrderCode(orderOPT.getOrderCode());
		resultDTO.setCreatedAt(orderOPT.getCreatedAt().toString());
		resultDTO.setEmployee(orderOPT.getEmployee().getFullName());
		
		if(Objects.equals(orderOPT.getCompletedAt(),null)) {
			resultDTO.setCompletedAt("");
		}else {
			resultDTO.setCompletedAt(orderOPT.getCompletedAt().toString());
		}
		
		String currentStatus = inventoryOrderStatusRepository.getCurrentStatusByOrderId(orderOPT.getId());
		resultDTO.setCurrentStatus(currentStatus);
		
		return resultDTO;
		
	}
	
	public Integer calculateTotalItems(List<InventoryOrderDetailForOverviewDTO> orderDetails) {
	    return orderDetails.stream()
	            .map(InventoryOrderDetailForOverviewDTO::getQuantity)
	            .filter(Objects::nonNull)
	            .reduce(0, Integer::sum);
	}



	private void saveInitStatus(InventoryOrder savedOrder) throws StatusNotFoundException {
		Optional<ImportingStatus> initStatus = importingStatusRepository.findById(1);
		
		if(initStatus.isEmpty()) {
			throw new StatusNotFoundException("Not Exist Status With ID 1");
		}
		
		InventoryOrderStatus status = new InventoryOrderStatus();
		status.setCreatedAt(LocalDateTime.now());
		status.setStatus(initStatus.get());
		status.setOrder(savedOrder);
		status.setEmployee(new InventoryEmployee(UtilityGlobal.getIdOfCurrentLoggedUser()));
		
		inventoryOrderStatusRepository.save(status);
	
	}
	
	
	private InventoryOrder saveGeneralInformation(InventoryOrderSavingRequestAggregatorDTO requestDTO) {
		InventoryOrder inventoryOrder = InventoryOrderMapper.toOrder(requestDTO);
		
		Inventory inventory = vInventoryRepository.findByInventoryCode(UtilityGlobal.getInventoryCodeOfCurrentLoggedUser());
		
		inventoryOrder.setInventory(inventory);
		
		InventoryOrder savedOrder = orderRepository.save(inventoryOrder);
		
		return savedOrder;
	}
	
	private void saveOrderDetails(InventoryOrderSavingRequestAggregatorDTO requestDTO,InventoryOrder savedOrder) throws ProductVariantSkuNotFoundException  {
		
		Integer orderId = savedOrder.getId();
		
		
		List<InventoryOrderDetail> details = setUpDetailList(requestDTO,orderId);
		
		
		inventoryOrderDetailRepository.saveAll(details);
		
	}
	
	
	private List<InventoryOrderDetail> setUpDetailList(InventoryOrderSavingRequestAggregatorDTO requestDTO, Integer orderId) throws ProductVariantSkuNotFoundException {
		List<InventoryOrderDetailWithExpectedPriceDTO> detailsDTO = requestDTO.getOrderDetails();
		
		List<InventoryOrderDetail> details = new ArrayList<>();
		
		
		for(InventoryOrderDetailWithExpectedPriceDTO dto : detailsDTO) {
			
			InventoryOrderDetail detail = InventoryOrderMapper.toDetail(dto, orderId);
			
			setProductForDetail(detail, dto, orderId);
			
			details.add(detail);
			
			System.out.println("Sku " + detail.getId().getSku() + " Order Id" + detail.getId().getOrderId());
		}
		
		
		
		
		return details;
	}
	
	
	
	
	
	private void setProductForDetail(InventoryOrderDetail detail,InventoryOrderDetailWithExpectedPriceDTO dto, Integer orderId) throws ProductVariantSkuNotFoundException  {
		
		Optional<ProductVariant> productVariantOPT = productVariantRepository.findProductVariantIdBySkuCode(dto.getSku());
		
		if(productVariantOPT.isEmpty()) {
			throw new ProductVariantSkuNotFoundException(dto.getSku());
		}
		
		detail.setId(new InventoryOrderDetailId(orderId,dto.getSku()));
		
		detail.setProductVariant(productVariantOPT.get());
		
		
	}
	
	
	
}
