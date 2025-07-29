package com.thuctap.reports;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thuctap.reports.dto.ReportAggregator;
import com.thuctap.reports.dto.ReportRequest;
import com.thuctap.utility.UtilityGlobal;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

	
	private final AdminRevenueReportService adminService;
	private final DirectorRevenueReportService directorService;

	public ReportController(AdminRevenueReportService adminService, DirectorRevenueReportService directorService) {
		this.adminService = adminService;
		this.directorService = directorService;
	}

	@PostMapping("/admin")
	@PreAuthorize("hasAuthority('SUPER_ADMIN')")
	public ResponseEntity<?> getAdminReport(@RequestBody ReportRequest request) {
		ReportAggregator result = adminService.getRevenueReportForAdmin(request);
		return ResponseEntity.ok(result);
	}

	@PostMapping("/director")
	@PreAuthorize("hasAuthority('DIRECTOR')")
	public ResponseEntity<?> getDirectorReport(@RequestBody ReportRequest request) {
		Integer inventoryId = UtilityGlobal.getInventoryIdOfCurrentLoggedUser();
		ReportAggregator result = directorService.getRevenueReportForDirector(request, inventoryId);
		
		
		return ResponseEntity.ok(result);
	}
	
	
	
	
}
