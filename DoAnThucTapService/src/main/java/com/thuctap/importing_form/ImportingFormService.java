package com.thuctap.importing_form;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.thuctap.common.exceptions.ImportingFormNotFoundException;
import com.thuctap.importing_form.dto.ImportingFormDetailOverviewDTO;
import com.thuctap.importing_form.dto.ImportingFormOverviewDTO;
import com.thuctap.importing_form.dto.ImportingFormPageAggregator;
import com.thuctap.importing_form.dto.ImportingFormPageDTO;
import com.thuctap.utility.QuoteData;
import com.thuctap.utility.QuoteExcelReader;
import com.thuctap.utility.QuoteFileReader;
import com.thuctap.utility.UtilityGlobal;

import io.jsonwebtoken.io.IOException;

@Service
public class ImportingFormService {

	@Autowired
	private ImportingFormRepository importingFormRepository;
	
	@Autowired
	private ImportingFormDetailRepository importingFormDetailRepository;
	
	

	public ImportingFormPageAggregator search(LocalDateTime startDate, LocalDateTime endDate, int pageNum, int pageSize, String sortField, String sortDir) {
		
	
		
		Pageable pageable = UtilityGlobal.setUpPageRequest(pageNum,pageSize, sortField, sortDir);
		
		Page<ImportingFormPageDTO> pages = importingFormRepository.search(startDate, endDate,pageable);
		
		ImportingFormPageAggregator result = new ImportingFormPageAggregator();
		
		result.setForms(pages.getContent());
		result.setPage(UtilityGlobal.buildPageDTO(sortField, sortDir, pages));
		
		return result;
		
	}
	
	
	public ImportingFormOverviewDTO getOverview(Integer formId) throws ImportingFormNotFoundException {
		
		checkExistOfImportingForm(formId);
		
		ImportingFormOverviewDTO overviewDTO = importingFormRepository.findImportingFormOverviewById(formId);
		
		List<ImportingFormDetailOverviewDTO> details = importingFormDetailRepository.findDetailsByImportingFormId(formId);
		
		overviewDTO.setDetails(details);
		
		setTotalItems(overviewDTO);
		
		return overviewDTO;
		
	}
	
	public QuoteData getInvoiceInformation(Integer formId) {
		
		Integer orderId = importingFormRepository.findOrderIdByImportingFormId(formId);
		
		Path invoicePath = getInvoiceFilePath(orderId);
		
		QuoteExcelReader reader = new QuoteExcelReader();
		
		try (InputStream inputStream = Files.newInputStream(invoicePath)) {
	        return reader.readInvoiceData(inputStream);
	    } catch (java.io.IOException e) {
	        throw new RuntimeException("Failed to read invoice file: " + invoicePath, e);
	    }
		
		
		
	}
	
	private Path getInvoiceFilePath(Integer orderId) {
	    String folderPath = "C:/DoAnThucTapImages/InventoryOrder/" + orderId;
	    Path folder = Paths.get(folderPath);
	    String prefix = "invoice_";

	    try (Stream<Path> files = Files.list(folder)) {
	        Optional<Path> matchedFile = files
	            .filter(path -> path.getFileName().toString().toLowerCase().startsWith(prefix))
	            .findFirst();

	        if (matchedFile.isEmpty()) {
	            throw new IllegalArgumentException("No invoice file found in folder for order " + orderId);
	        }

	        return matchedFile.get();

	    } catch (java.io.IOException e) {
	        throw new RuntimeException("Failed to access invoice folder: " + folderPath, e);
	    }
	}
	
	
	
	
	private void checkExistOfImportingForm(Integer formId) throws ImportingFormNotFoundException {
		boolean isExist = importingFormRepository.existsById(formId);
		
		if(!isExist) {
			throw new ImportingFormNotFoundException("Not Found Importing Form With id " + formId);
		}
		
	}


	private void setTotalItems(ImportingFormOverviewDTO overviewDTO) {
		
		List<ImportingFormDetailOverviewDTO> details = overviewDTO.getDetails();
		
		Integer countTotal = 0;
		
		for(ImportingFormDetailOverviewDTO detail : details) {
			countTotal += detail.getQuantity();
		}
		
		overviewDTO.setLineItems(countTotal);
		
	}
	
	
	
	
}
