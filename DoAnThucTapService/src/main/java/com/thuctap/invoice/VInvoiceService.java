package com.thuctap.invoice;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.thuctap.invoice.dto.VInvoiceDetailForReport;
import com.thuctap.stocking.StockingRepository;
import com.thuctap.utility.UtilityGlobal;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

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
		
		byte[] report = generateReport(dto, savedInvoice, employee, inventory);
		
		return report;
	}
	
	private byte[] generateReport(VInvoiceDTO dto,Invoice savedInvoice, InventoryEmployee employee, Inventory inventory) {
		
		
		
		
		try {
			InputStream reportStream = this.getClass().getResourceAsStream("/templates/bill.jasper");
			Map<String,Object> parametersInReport = new HashMap<>();
			setUpParameters(parametersInReport, dto, employee, savedInvoice, inventory);
			JasperPrint jasperPrint = JasperFillManager.fillReport(reportStream, parametersInReport, new JREmptyDataSource());
			return JasperExportManager.exportReportToPdf(jasperPrint);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			throw new RuntimeException("Error while generating the report file", e);
			
		}
		
		
		
		
	}
	
	
	private void setUpParameters(Map<String,Object> parametersInReport,VInvoiceDTO dto,InventoryEmployee employee,Invoice savedInvoice,Inventory inventory) {
		String invoiceCode = savedInvoice.getInvoiceCode();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
		String createdDate = savedInvoice.getCreatedAt().format(formatter);
		BigDecimal tax = dto.getTax();
		
		
		BigDecimal totalWithoutTax = calculateTotal(dto.getDetails());
		BigDecimal totalIncludeTax = totalWithoutTax.add(tax);
		
		
		List<VInvoiceDetailForReport> listDetails = dto.getDetails().stream().map(VInvoiceMapper::toInvoiceDetailForReport).toList();
		
		String inventoryAddress = inventory.getFullAddress();
		String lastName = employee.getLastName();
		String fullName = employee.getFullName();
		
		parametersInReport.put("inventoryAddress",inventoryAddress);
		parametersInReport.put("invoiceCode",invoiceCode);
		parametersInReport.put("createdDate",createdDate);
		parametersInReport.put("Tax",tax);
		parametersInReport.put("totalPaid",totalIncludeTax);
		parametersInReport.put("employeeLastName",lastName);
		parametersInReport.put("employeeFullName",fullName);
		parametersInReport.put("tableData",new JRBeanCollectionDataSource(listDetails));
	}
	
	
	
	
	private BigDecimal calculateTotal(List<VInvoiceDetailDTO> details) {
		
		BigDecimal result = new BigDecimal(0);
		
		for(VInvoiceDetailDTO detail : details) {
			result = result.add(detail.getUnitPrice());
		}
		
		return result;
		
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
