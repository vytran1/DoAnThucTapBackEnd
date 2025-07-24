package com.thuctap.utility;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class QuoteExcelReader implements QuoteFileReader {
	
	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	
	@Override
	public QuoteData readQuoteAcceptData(InputStream inputStream) throws IllegalArgumentException {
		Workbook workbook;
	    try {
	        workbook = WorkbookFactory.create(inputStream);
	    } catch (IOException | EncryptedDocumentException e) {
	    	throw new IllegalArgumentException("The uploaded file is not a valid Excel file", e);
	    }

	    try {
			try (workbook) {
				 Sheet infoSheet = workbook.getSheet("information");
			     System.out.println("infoSheet is null? " + (infoSheet == null));
			     Sheet detailSheet = workbook.getSheet("order_details");
			     System.out.println("detailSheet is null? " + (detailSheet == null));

			     if (infoSheet == null || detailSheet == null) {
			         throw new IllegalArgumentException("Excel file must contain sheets named 'information' and 'order_details'");
			     }

			     QuoteInformation info = parseQuoteInformation(infoSheet);
			     List<QuoteItem> items = parseQuoteItems(detailSheet);
			     return new QuoteData(info, items);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	
	
	
	@Override
	public QuoteData readQuoteRejectData(InputStream inputStream) {
		 try (Workbook workbook = WorkbookFactory.create(inputStream)) {
		        Sheet infoSheet = workbook.getSheet("information");

		        if (infoSheet == null) {
		            throw new IllegalArgumentException("Excel must contain 'information' sheet.");
		        }

		        QuoteInformation info = parseQuoteInformationForReject(infoSheet);
		        return new QuoteData(info, new ArrayList<>());
		    } catch (Exception e) {
		        throw new IllegalArgumentException("Invalid reject quote file: " + e.getMessage(), e);
		    }
	}
	
	public QuoteData readInvoiceData(InputStream inputStream) {
	    try (Workbook workbook = WorkbookFactory.create(inputStream)) {
	        Sheet detailSheet = workbook.getSheet("invoice_details");

	        if (detailSheet == null) {
	            throw new IllegalArgumentException("Excel file must contain a sheet named 'invoice_details'");
	        }

	        List<QuoteItem> invoiceItems = parseInvoiceItems(detailSheet);
	        return new QuoteData(null, invoiceItems);
	    } catch (Exception e) {
	        throw new IllegalArgumentException("Invalid invoice file: " + e.getMessage(), e);
	    }
	}
	
	public void validateInvoiceFileHasRequiredSheets(InputStream inputStream) {
	        try (Workbook workbook = WorkbookFactory.create(inputStream)) {
	            boolean hasInformation = false;
	            boolean hasInvoiceDetails = false;

	            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	                String sheetName = workbook.getSheetName(i).trim().toLowerCase();
	                if (sheetName.equals("information")) {
	                    hasInformation = true;
	                }
	                if (sheetName.equals("invoice_details")) {
	                    hasInvoiceDetails = true;
	                }
	            }

	            if (!hasInformation || !hasInvoiceDetails) {
	                throw new IllegalArgumentException("Invoice file must contain both 'information' and 'invoice_details' sheets.");
	            }
	        } catch (IOException e) {
	            throw new RuntimeException("Failed to read invoice file", e);
	        }
	    }
	
	
	private List<QuoteItem> parseInvoiceItems(Sheet sheet) {
	    List<QuoteItem> items = new ArrayList<>();
	    for (Row row : sheet) {
	        if (row.getRowNum() == 0) continue; 

	        QuoteItem item = new QuoteItem();
	        item.setSku(getStringCell(row, 0));
	        item.setProductName(getStringCell(row, 1));

	        BigDecimal unitPrice = getBigDecimalCell(row, 2);
	        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) <= 0) {
	            throw new IllegalArgumentException("Unit price must be greater than 0 at row " + (row.getRowNum() + 1));
	        }
	        item.setQuotedPrice(unitPrice); 

	        String quantityStr = getStringCell(row, 3);
	        try {
	            int quantity = Integer.parseInt(quantityStr);
	            if (quantity <= 0) {
	                throw new IllegalArgumentException("Quantity must be > 0 at row " + (row.getRowNum() + 1));
	            }
	            item.setQuantity(quantity);
	        } catch (NumberFormatException e) {
	            throw new IllegalArgumentException("Invalid quantity at row " + (row.getRowNum() + 1));
	        }

	        items.add(item);
	    }
	    return items;
	}
	
	
	private QuoteInformation parseQuoteInformationForReject(Sheet sheet) {
	    Map<String, String> map = extractKeyValue(sheet);

	    QuoteInformation info = new QuoteInformation();
	    setCommonFields(info, map);

	    String reason = map.get("Reason");
	    if (reason == null || reason.isBlank()) {
	        throw new IllegalArgumentException("Missing rejection reason.");
	    }
	    info.setReason(reason);
	    return info;
	}
	
	private Map<String, String> extractKeyValue(Sheet sheet) {
	    Map<String, String> map = new HashMap<>();
	    for (Row row : sheet) {
	        if (row.getRowNum() == 0) continue;
	        String key = UtilityGlobal.getCellValue(row.getCell(0)).trim();
	        String value = UtilityGlobal.getCellValue(row.getCell(1)).trim();
	        if (!key.isBlank()) map.put(key, value);
	    }
	    return map;
	}
	
	private void setCommonFields(QuoteInformation info, Map<String, String> map) {
	    info.setCompanyName(map.get("Company Name"));
	    info.setCompanyCode(map.get("Company Code"));
	    info.setAddress(map.get("Address"));
	    info.setPhone(map.get("Phone"));
	    info.setEmail(map.get("Email"));
	    info.setQuotedBy(map.get("Quoted By"));
	    info.setPosition(map.get("Position"));
	    info.setOrderCode(map.get("Order Code"));

	    if (map.containsKey("Quotation Date")) {
	        info.setQuotationDate(LocalDateTime.parse(map.get("Quotation Date"), DATE_TIME_FORMATTER));
	    }
	}




	private QuoteInformation parseQuoteInformation(Sheet sheet) {
	    Map<String, String> map = new HashMap<>();
	    for (Row row : sheet) {
	        if (row.getRowNum() == 0) continue;
	        String key = UtilityGlobal.getCellValue(row.getCell(0)).trim();
	        String value = UtilityGlobal.getCellValue(row.getCell(1)).trim();
	        if (!key.isBlank()) map.put(key, value);
	    }

	    QuoteInformation info = new QuoteInformation();
	    info.setCompanyName(map.get("Company Name"));
	    info.setCompanyCode(map.get("Company Code"));
	    info.setAddress(map.get("Address"));
	    info.setPhone(map.get("Phone"));
	    info.setEmail(map.get("Email"));
	    info.setQuotedBy(map.get("Quoted By"));
	    info.setPosition(map.get("Position"));
	    info.setQuotationDate(LocalDateTime.parse(map.get("Quotation Date"),DATE_TIME_FORMATTER));
	    info.setOrderCode(map.get("Order Code"));
	    
	    String shippingFeeStr = map.get("Shipping Fee");
	    if (shippingFeeStr == null || shippingFeeStr.isBlank()) {
	        throw new IllegalArgumentException("Shipping Fee is missing in the Excel file.");
	    }

	    BigDecimal shippingFee = new BigDecimal(shippingFeeStr);
	    if (shippingFee.compareTo(BigDecimal.ZERO) <= 0) {
	        throw new IllegalArgumentException("Shipping Fee must be greater than 0.");
	    }

	    info.setShippingFee(shippingFee); // <- dùng lại cái vừa parse xong
	    return info;
	}
	
	private BigDecimal parseBigDecimal(String val) {
	    return (val == null || val.isBlank()) ? null : new BigDecimal(val);
	}
	  
	  private List<QuoteItem> parseQuoteItems(Sheet sheet) {
	        List<QuoteItem> items = new ArrayList();
	        for (Row row : sheet) {
	            if (row.getRowNum() == 0) continue;

	            QuoteItem item = new QuoteItem();
	            item.setSku(getStringCell(row, 0));
	            item.setProductName(getStringCell(row, 1));
	            
	            BigDecimal quotedPrice = getBigDecimalCell(row, 2);
	            if (quotedPrice == null || quotedPrice.compareTo(BigDecimal.ZERO) <= 0) {
	                throw new IllegalArgumentException("Quoted Price must be greater than 0 at row " + (row.getRowNum() + 1));
	            }
	            
	            item.setQuotedPrice(quotedPrice);
	            item.setCurrency(getStringCell(row, 3));
	            item.setDescription(getStringCell(row, 4));
	            items.add(item);
	        }
	        return items;
	    }
	
	 private String getStringCell(Row row, int index) {
	        return UtilityGlobal.getCellValue(row.getCell(index)).trim();
	 }

	 private BigDecimal getBigDecimalCell(Row row, int index) {
	        String val = getStringCell(row, index);
	        return val.isBlank() ? null : new BigDecimal(val);
	 }

}
