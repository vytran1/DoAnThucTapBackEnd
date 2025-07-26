package com.thuctap.invoice;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.thuctap.common.invoice.Invoice;
import com.thuctap.stocking.dto.StockingSummaryDTO;

public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {
	
	
	
	
	
}
