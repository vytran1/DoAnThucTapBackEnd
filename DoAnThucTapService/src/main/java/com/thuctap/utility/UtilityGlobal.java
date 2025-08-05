package com.thuctap.utility;

import java.text.SimpleDateFormat;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;

import com.thuctap.common.customer.Customer;
import com.thuctap.common.inventory_employees.InventoryEmployee;
import com.thuctap.security.CustomerAccountUserDetail;
import com.thuctap.security.CustomerUserDetails;

public class UtilityGlobal {
	
	
	public static Integer getIdOfCurrentLoggedUser() 
	{
		CustomerUserDetails customerUserDetails = (CustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		InventoryEmployee employee = customerUserDetails.getInventoryEmployee();
		Integer id = employee.getId();
		return id;
	}
	
	public static Integer getIfOfCurrentLoggedCustomer() {
		CustomerAccountUserDetail customerAccountUserDetail = (CustomerAccountUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Customer customer = customerAccountUserDetail.getCustomer();
		Integer id = customer.getId();
		return id;
	}
	
	
	public static String getInventoryCodeOfCurrentLoggedUser() {
		CustomerUserDetails customerUserDetails = (CustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		InventoryEmployee employee = customerUserDetails.getInventoryEmployee();
		String inventoryCode = employee.getInventory().getInventoryCode();
		return inventoryCode;
	}
	
	public static Integer getInventoryIdOfCurrentLoggedUser() {
		CustomerUserDetails customerUserDetails = (CustomerUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		InventoryEmployee employee = customerUserDetails.getInventoryEmployee();
		Integer inventortId = employee.getInventory().getId();
		return inventortId;
	}
	
	
	public static Pageable setUpPageRequest(Integer pageNum, Integer pageSize, String sortField, String sortDir) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1,pageSize,sort);
		
		return pageable;
	}
	
	public static PageDTO buildPageDTO(String sortField, String sortDir, Page<?> pages) {
		PageDTO pageInfo = new PageDTO();
		pageInfo.setPageNum(pages.getNumber() + 1);
		pageInfo.setPageSize(pages.getSize());
		pageInfo.setSortField(sortField);
		pageInfo.setSortDir(sortDir);
		pageInfo.setReverseDir(sortDir.equals("asc") ? "desc" : "asc");
		pageInfo.setTotalPages(pages.getTotalPages());
		pageInfo.setTotalItems(pages.getTotalElements());
		return pageInfo;
	}
	
	public static String getCellValue(Cell cell) {
	    if (cell == null) return "";
	    switch (cell.getCellType()) {
	        case STRING:
	            return cell.getStringCellValue().trim();
	        case NUMERIC:
	            if (DateUtil.isCellDateFormatted(cell)) {
	                return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
	            } else {
	                double value = cell.getNumericCellValue();
	                if (value == Math.floor(value)) {
	                    return String.valueOf((long) value);
	                }
	                return String.valueOf(value);
	            }
	        case BOOLEAN:
	            return String.valueOf(cell.getBooleanCellValue());
	        case FORMULA:
	            try {
	                return cell.getStringCellValue();
	            } catch (IllegalStateException e) {
	                return String.valueOf(cell.getNumericCellValue());
	            }
	        default:
	            return "";
	    }
	}

	
}
