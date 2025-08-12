package com.thuctap.exporting_form;

public class ExportingFormCreatedEvent {
	private final Integer exportingFormId;

	public ExportingFormCreatedEvent(Integer exportingFormId) {
		this.exportingFormId = exportingFormId;
	}

	public Integer getExportingFormId() {
		return exportingFormId;
	}
}
