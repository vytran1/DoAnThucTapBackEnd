package com.thuctap.audit;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thuctap.common.audit.Audit;

public interface AuditRepository extends JpaRepository<Audit,Integer> {

}
