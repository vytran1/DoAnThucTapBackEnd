package com.thuctap.importing_form.dto;

import java.util.List;

import com.thuctap.utility.PageDTO;

public class ImportingFormPageAggregator {
	
	private List<ImportingFormPageDTO> forms;
	private PageDTO page;
	public ImportingFormPageAggregator() {
		super();
		// TODO Auto-generated constructor stub
	}
	public List<ImportingFormPageDTO> getForms() {
		return forms;
	}
	public void setForms(List<ImportingFormPageDTO> forms) {
		this.forms = forms;
	}
	public PageDTO getPage() {
		return page;
	}
	public void setPage(PageDTO page) {
		this.page = page;
	}
	
	
	
	
}
