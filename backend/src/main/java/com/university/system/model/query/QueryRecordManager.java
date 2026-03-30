package com.university.system.model.query;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * QueryRecordManager maintains an in-memory audit log of all query routing decisions.
 * In production this would persist to the database via a repository.
 *
 * OOP Concept: ENCAPSULATION — internal record list is private and only
 * accessible through controlled methods.
 */
@Component
public class QueryRecordManager {

    public record QueryRecord(
        String studentId,
        String category,
        String handlerUsed,
        boolean aiInvoked,
        LocalDateTime timestamp
    ) {}

    private final List<QueryRecord> records = new ArrayList<>();

    public void record(String studentId, String category, String handler, boolean aiInvoked) {
        records.add(new QueryRecord(studentId, category, handler, aiInvoked, LocalDateTime.now()));
    }

    public List<QueryRecord> getAll() {
        return List.copyOf(records);
    }

    public Map<String, Long> getCategoryStats() {
        return records.stream()
            .collect(Collectors.groupingBy(QueryRecord::category, Collectors.counting()));
    }

    public long countAiInvocations() {
        return records.stream().filter(QueryRecord::aiInvoked).count();
    }
}
