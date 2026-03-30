package com.university.system.repository;

import com.university.system.model.document.AuditReportManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditReportRepository extends JpaRepository<AuditReportManager, Long> {
    List<AuditReportManager> findByAuditedDepartment(String department);
    List<AuditReportManager> findByAuditPeriod(String period);
}
