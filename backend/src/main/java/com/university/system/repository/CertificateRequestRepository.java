package com.university.system.repository;

import com.university.system.model.ticket.CertificateRequest;
import com.university.system.model.ticket.ServiceRequest.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, Long> {
    Optional<CertificateRequest> findByTicketNumber(String ticketNumber);
    List<CertificateRequest> findByStudentId(String studentId);
    List<CertificateRequest> findByStatus(TicketStatus status);
}
