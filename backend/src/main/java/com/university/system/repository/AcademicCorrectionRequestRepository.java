package com.university.system.repository;

import com.university.system.model.ticket.AcademicCorrectionRequest;
import com.university.system.model.ticket.ServiceRequest.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademicCorrectionRequestRepository
    extends JpaRepository<AcademicCorrectionRequest, Long> {

    Optional<AcademicCorrectionRequest> findByTicketNumber(String ticketNumber);
    List<AcademicCorrectionRequest> findByStudentId(String studentId);
    List<AcademicCorrectionRequest> findByStatus(TicketStatus status);
}
