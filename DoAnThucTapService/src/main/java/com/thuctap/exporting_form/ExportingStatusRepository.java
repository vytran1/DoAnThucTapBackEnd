package com.thuctap.exporting_form;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.exporting_status.ExportingStatus;

public interface ExportingStatusRepository extends JpaRepository<ExportingStatus,Integer> {

}
