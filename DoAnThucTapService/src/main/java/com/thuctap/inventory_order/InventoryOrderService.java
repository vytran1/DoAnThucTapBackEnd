package com.thuctap.inventory_order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.thuctap.common.exceptions.OrderNotFoundException;
import com.thuctap.common.exceptions.ProductNotFoundException;
import com.thuctap.common.exceptions.ProductVariantSkuNotFoundException;
import com.thuctap.common.exceptions.StatusNotFoundException;
import com.thuctap.common.importing_status.ImportingStatus;
import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.inventory_order.InventoryOrder;
import com.thuctap.common.inventory_order.InventoryOrderDetail;
import com.thuctap.common.inventory_order.InventoryOrderDetailId;
import com.thuctap.common.inventory_order.InventoryOrderStatus;
import com.thuctap.common.product_variant.ProductVariant;

import com.thuctap.importing_status.ImportingStatusRepository;
import com.thuctap.inventory.vytran.VInventoryRepository;
import com.thuctap.inventory_order.dto.InventoryOrderDetailForOverviewDTO;
import com.thuctap.inventory_order.dto.InventoryOrderDetailWithExpectedPriceDTO;
import com.thuctap.inventory_order.dto.InventoryOrderOverviewDTO;
import com.thuctap.inventory_order.dto.InventoryOrderSavingRequestAggregatorDTO;
import com.thuctap.inventory_order.dto.InventoryOrderStatusDTO;
import com.thuctap.product_variant.ProductVariantRepository;
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
	
	
	
	
	
	
	private InventoryOrderOverviewDTO setUpGeneralInformation(InventoryOrder orderOPT) {
		InventoryOrderOverviewDTO resultDTO = new InventoryOrderOverviewDTO();
		resultDTO.setOrderCode(orderOPT.getOrderCode());
		resultDTO.setCreatedAt(orderOPT.getCreatedAt().toString());
		resultDTO.setEmployee(orderOPT.getEmployee().getFullName());
		
		if(Objects.equals(orderOPT.getCompletedAt(),null)) {
			resultDTO.setCompletedAt("");
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
