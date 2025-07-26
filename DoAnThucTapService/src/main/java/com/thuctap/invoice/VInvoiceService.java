package com.thuctap.invoice;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thuctap.common.inventory.Inventory;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.common.invoice.Invoice;
import com.thuctap.common.invoice.InvoiceDetail;
import com.thuctap.common.stocking.Stocking;
import com.thuctap.common.stocking.StockingId;
import com.thuctap.inventory.InventoryRepository;
import com.thuctap.inventory.vytran.VInventoryRepository;
import com.thuctap.inventory_employee.InventoryEmployeeRepository;
import com.thuctap.invoice.dto.VInvoiceDTO;
import com.thuctap.invoice.dto.VInvoiceDetailDTO;
import com.thuctap.stocking.StockingRepository;
import com.thuctap.utility.UtilityGlobal;

@Service
public class VInvoiceService {
	
	
	@Autowired
	private InventoryEmployeeRepository inventoryEmployeeRepository;
	
	
	@Autowired
	private VInventoryRepository inventoryRepository;
	
	@Autowired
	private StockingRepository stockingRepository;
	
	@Autowired
	private InvoiceRepository invoiceRepository;
	
	@Autowired
	private InvoiceDetailRepository invoiceDetailRepository;
	
	
	@Transactional(rollbackFor = Exception.class)
	public byte[] save(VInvoiceDTO dto) {
		
		InventoryEmployee employee = inventoryEmployeeRepository.findById(UtilityGlobal.getIdOfCurrentLoggedUser()).get();
		
		Inventory inventory = inventoryRepository.findByInventoryCode(UtilityGlobal.getInventoryCodeOfCurrentLoggedUser());
		
		
		Invoice savedInvoice = saveInvoiceGeneralInformation(dto, employee, inventory);
		
		saveInvoiceDetailInformation(savedInvoice, dto,inventory);
		
		
		return null;
	}
	
	private Invoice saveInvoiceGeneralInformation(VInvoiceDTO dto,InventoryEmployee employee,Inventory inventory) {
		Invoice invoice = VInvoiceMapper.toInvoice(dto,inventory,employee);
		
		Invoice savedInvoice = invoiceRepository.save(invoice);
		
		return savedInvoice;
	}
	
	private void saveInvoiceDetailInformation(Invoice savedInvoice,VInvoiceDTO dto,Inventory inventory ) {
		List<InvoiceDetail> details = dto.getDetails().stream().map(detail -> VInvoiceMapper.toInvoiceDetail(detail, savedInvoice)).toList();
		
		invoiceDetailRepository.saveAll(details);
		
		updateStock(details, inventory);
		
	}
	
	private void updateStock(List<InvoiceDetail> details,Inventory inventory ) {
		
		Integer inventoryId = inventory.getId();
		
		List<Stocking> stockings = new ArrayList<>();
		
		for(InvoiceDetail detail : details) {
			
			String sku = detail.getId().getSku();
			
			Stocking stocking = stockingRepository.findById(new StockingId(inventoryId, sku)).get();
			
			stocking.setQuantity(stocking.getQuantity() - detail.getQuantity());
			
			stockings.add(stocking);
		}
		
		stockingRepository.saveAll(stockings);
		
	}
	
	
}
