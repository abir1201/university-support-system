package com.university.system.repository;

import com.university.system.model.query.CourseInquiry;
import com.university.system.model.query.Query.QueryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseInquiryRepository extends JpaRepository<CourseInquiry, Long> {
    List<CourseInquiry> findByStudentId(String studentId);
    List<CourseInquiry> findByStatus(QueryStatus status);
    List<CourseInquiry> findByCourseCode(String courseCode);
}
