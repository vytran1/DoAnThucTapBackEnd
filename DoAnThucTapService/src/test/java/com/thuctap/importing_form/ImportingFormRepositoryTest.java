package com.thuctap.importing_form;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.inventory_employee.InventoryEmployeeRepository;
import com.thuctap.inventory_order.InventoryOrderDetailRepository;
import com.thuctap.inventory_order.InventoryOrderRepository;
import com.thuctap.product_variant.ProductVariantRepository;
import com.thuctap.supplier.SupplierRepository;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class ImportingFormRepositoryTest {
		
	@Autowired
	private ImportingFormRepository importingFormRepository;
	
	@Autowired
	private ImportingFormDetailRepository importingFormDetailRepository;
	
	@Autowired
	private InventoryOrderRepository inventoryOrderRepository;
	
	@Autowired
	private InventoryOrderDetailRepository inventoryOrderDetailRepository;
	
	@Autowired 
	private ProductVariantRepository productVariantRepository;
	
	@Autowired 
	private InventoryEmployeeRepository inventoryEmployeeRepository;
	
	@Autowired
	private SupplierRepository supplierRepository;
	
	@Test
	void insertImportingFormsWithOrders() {
		 int employeeId = 1;
		 int supplierId = 2;
		 List<String> skus = List.of("LG4_2020_1", "LG4_2020_2", "LG4_2020_3", "LG4_2020_4");
		 
		 InventoryEmployee employee = inventoryEmployeeRepository.findById(employeeId)
			        .orElseThrow(() -> new RuntimeException("Employee not found"));
		 Inventory inventory = employee.getInventory();
		 
		 var supplier = supplierRepository.findById(supplierId)
		            .orElseThrow(() -> new RuntimeException("Supplier not found"));
		 
		 var random = new java.util.Random();
		 
		 for (int i = 0; i < 15; i++) {
			 	var order = new com.thuctap.common.inventory_order.InventoryOrder();
		        order.setOrderCode("ORD_" + System.currentTimeMillis() + "_" + i);
		        order.setShippingFee(java.math.BigDecimal.valueOf(random.nextInt(50)));
		        order.setQuoteShippingFee(java.math.BigDecimal.valueOf(random.nextInt(100)));
		        order.setEmployee(employee);
		        order.setInventory(inventory);
		        order.setSupplier(supplier);
		        order.setCreatedAt(java.time.LocalDateTime.now().minusDays(random.nextInt(7)));
		        order.setUpdatedAt(order.getCreatedAt());

		        var savedOrder = inventoryOrderRepository.save(order);
		        
		        int productCount = 1 + random.nextInt(3);
		        
		        for (int j = 0; j < productCount; j++) {
		            String sku = skus.get(random.nextInt(skus.size()));
		            var variant = productVariantRepository.findBySkuCode(sku)
		                    .orElseThrow(() -> new RuntimeException("Product variant not found: " + sku));

		            var detail = new com.thuctap.common.inventory_order.InventoryOrderDetail();
		            var detailId = new com.thuctap.common.inventory_order.InventoryOrderDetailId(savedOrder.getId(), sku);

		            detail.setId(detailId);
		            detail.setOrder(savedOrder);
		            detail.setProductVariant(variant);
		            detail.setCostPrice(variant.getPrice().subtract(java.math.BigDecimal.valueOf(5)));
		            detail.setQuoteCostPrice(variant.getPrice());
		            detail.setExpectedPrice(variant.getPrice().add(java.math.BigDecimal.valueOf(2)));
		            detail.setQuantity(1 + random.nextInt(3));

		            inventoryOrderDetailRepository.save(detail);
		        }
		        
		        var importingForm = new com.thuctap.common.importing_form.ImportingForm();
		        importingForm.setImportingFormCode("IMP_" + System.currentTimeMillis() + "_" + i);
		        importingForm.setOrder(savedOrder);
		        importingForm.setEmployee(employee);
		        importingForm.setInventory(inventory);
		        importingForm.setSupplier(supplier);
		        importingForm.setShippingFee(savedOrder.getShippingFee());
		        importingForm.setCreatedAt(savedOrder.getCreatedAt().plusHours(4));
		        importingForm.setCompletedAt(importingForm.getCreatedAt().plusDays(1));

		        var savedForm = importingFormRepository.save(importingForm);
		        
		        
		        for (String sku : skus) {
		            var variant = productVariantRepository.findBySkuCode(sku).orElse(null);
		            if (variant == null) continue;

		            var formDetail = new com.thuctap.common.importing_form.ImportingFormDetail();
		            var formDetailId = new com.thuctap.common.importing_form.ImportingFormDetailId(savedForm.getId(), sku);

		            formDetail.setId(formDetailId);
		            formDetail.setImportingForm(savedForm);
		            formDetail.setProductVariant(variant);
		            formDetail.setCostPrice(variant.getPrice().subtract(java.math.BigDecimal.valueOf(8)));
		            formDetail.setQuantity(1 + random.nextInt(5));

		            importingFormDetailRepository.save(formDetail);
		        }
		        
		 }
		 
		 System.out.println("✅ Inserted 10 inventory orders + importing forms + details.");
	}
	
	
	
	
}

