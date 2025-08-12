package com.thuctap.exporting_form.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.thuctap.utility.PageDTO;

public class ExportingFormPageAggregator {
	
	@JsonProperty("forms")
	private List<ExportingFormPageDTO> exportingForms;
	
	@JsonProperty("page")
	private PageDTO pageDTO;

	public ExportingFormPageAggregator() {
		super();
		// TODO Auto-generated constructor stub
	}

	public List<ExportingFormPageDTO> getExportingForms() {
		return exportingForms;
	}

	public void setExportingForms(List<ExportingFormPageDTO> exportingForms) {
		this.exportingForms = exportingForms;
	}

	public PageDTO getPageDTO() {
		return pageDTO;
	}

	public void setPageDTO(PageDTO pageDTO) {
		this.pageDTO = pageDTO;
	}
	
	
	
	
}
