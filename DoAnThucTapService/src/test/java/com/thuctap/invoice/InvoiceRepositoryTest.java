package com.thuctap.invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.invoice.Invoice;
import com.thuctap.common.invoice.InvoiceDetail;
import com.thuctap.common.invoice.InvoiceDetailId;
import com.thuctap.common.product_variant.ProductVariant;
import com.thuctap.inventory_employee.InventoryEmployeeRepository;
import com.thuctap.product_variant.ProductVariantRepository;

import jakarta.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class InvoiceRepositoryTest {
	

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceDetailRepository invoiceDetailRepository;

    @Autowired
    private InventoryEmployeeRepository inventoryEmployeeRepository;

    @Autowired
    private ProductVariantRepository productVariantRepository;

    @Autowired
    private EntityManager entityManager;
    
    
    //@Test
    void insert10Invoices() {
        int employeeId = 2;
        List<String> skus = List.of("LG4_2020_1", "LG4_2020_2", "LG4_2020_3", "LG4_2020_4");

        InventoryEmployee employee = inventoryEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Random random = new Random();

        for (int i = 0; i < 10; i++) {
            LocalDateTime createdAt = LocalDateTime.now().minusDays(i);

            Invoice invoice = new Invoice();
            invoice.setInvoiceCode("TEST_INV_" + System.currentTimeMillis() + (1000 + i));
            invoice.setTax(BigDecimal.valueOf(random.nextInt(5) * 1.0));
            invoice.setEmployee(employee);
            invoice.setInventory(employee.getInventory());
            invoice.setCreatedAt(createdAt);

            Invoice savedInvoice = invoiceRepository.save(invoice);

            
            int productCount = 1 + random.nextInt(2);
            for (int j = 0; j < productCount; j++) {
                String sku = skus.get(random.nextInt(skus.size()));
                ProductVariant variant = productVariantRepository.findBySkuCode(sku)
                        .orElseThrow(() -> new RuntimeException("SKU not found: " + sku));

                InvoiceDetail detail = new InvoiceDetail();
                detail.setId(new InvoiceDetailId(savedInvoice.getId(), sku));
                detail.setInvoice(savedInvoice);
                detail.setProductVariant(variant);
                detail.setUnitPrice(variant.getPrice());
                detail.setQuantity(1 + random.nextInt(3));

                invoiceDetailRepository.save(detail);
            }

            // Flush and clear for clean persistence context (optional but good for tests)
            entityManager.flush();
            entityManager.clear();
        }

        System.out.println("✅ Inserted 10 test invoices with details.");
    }
    
    
//@Test
    void insertInvoicesInLast6Months() {
    	 int employeeId = 2;
    	    List<String> skus = List.of("LG4_2020_1", "LG4_2020_2", "LG4_2020_3", "LG4_2020_4");

    	    InventoryEmployee employee = inventoryEmployeeRepository.findById(employeeId)
    	            .orElseThrow(() -> new RuntimeException("Employee not found"));

    	    Random random = new Random();

    	    for (int i = 0; i < 30; i++) {
    	        
    	        int randomDays = random.nextInt(180); // 0–179
    	        LocalDateTime createdAt = LocalDateTime.now().minusDays(randomDays);

    	        Invoice invoice = createInvoice(employee, createdAt);
    	        Invoice savedInvoice = invoiceRepository.save(invoice);

    	        generateInvoiceDetails(savedInvoice, skus, random);

    	        entityManager.flush();
    	        entityManager.clear();
    	    }

    	    System.out.println("✅ Inserted 30 test invoices randomly in last 6 months.");
    }
    
    @Test
    void insertInvoicesForSpecificMonth() {
        int employeeId = 2;
        int targetMonth = 1; 
        int targetYear = 2025;

        List<String> skus = List.of("LG4_2020_1", "LG4_2020_2", "LG4_2020_3", "LG4_2020_4");

        InventoryEmployee employee = inventoryEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Random random = new Random();

        for (int day = 1; day <= 30; day++) {
            LocalDateTime createdAt = LocalDateTime.of(targetYear, targetMonth, day, 10, 0);

            Invoice invoice = createInvoice(employee, createdAt);
            Invoice savedInvoice = invoiceRepository.save(invoice);

            generateInvoiceDetails(savedInvoice, skus, random);

            entityManager.flush();
            entityManager.clear();
        }

        System.out.printf("✅ Inserted 30 invoices for month %02d/%d\n", targetMonth, targetYear);
    }
    
    private Invoice createInvoice(InventoryEmployee employee, LocalDateTime createdAt) {
        Random random = new Random();

        Invoice invoice = new Invoice();
        invoice.setInvoiceCode("TEST_INV_" + System.currentTimeMillis() + random.nextInt(999));
        invoice.setTax(BigDecimal.valueOf(random.nextInt(5)));
        invoice.setEmployee(employee);
        invoice.setInventory(employee.getInventory());
        invoice.setCreatedAt(createdAt);

        return invoice;
    }
    
    private void generateInvoiceDetails(Invoice invoice, List<String> skus, Random random) {
        int productCount = 1 + random.nextInt(2); 

        for (int j = 0; j < productCount; j++) {
            String sku = skus.get(random.nextInt(skus.size()));
            ProductVariant variant = productVariantRepository.findBySkuCode(sku)
                    .orElseThrow(() -> new RuntimeException("SKU not found: " + sku));

            InvoiceDetail detail = new InvoiceDetail();
            detail.setId(new InvoiceDetailId(invoice.getId(), sku));
            detail.setInvoice(invoice);
            detail.setProductVariant(variant);
            detail.setUnitPrice(variant.getPrice());
            detail.setQuantity(1 + random.nextInt(3));

            invoiceDetailRepository.save(detail);
        }
    }
    
    
}
	

