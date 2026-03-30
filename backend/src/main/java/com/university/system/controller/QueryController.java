package com.university.system.controller;

import com.university.system.dto.QueryRequest;
import com.university.system.dto.QueryResponse;
import com.university.system.service.QueryHandlingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for the StudentQueryHandling domain.
 * Thin layer — delegates ALL logic to QueryHandlingService.
 * MVC: Controller only handles HTTP concerns (routing, validation, response codes).
 */
@RestController
@RequestMapping("/api/queries")
@CrossOrigin(origins = "http://localhost:3000")
public class QueryController {

    private final QueryHandlingService queryHandlingService;

    public QueryController(QueryHandlingService queryHandlingService) {
        this.queryHandlingService = queryHandlingService;
    }

    /**
     * POST /api/queries
     * Submit a new student query. Category is auto-detected if not provided.
     */
    @PostMapping
    public ResponseEntity<QueryResponse> submitQuery(@Valid @RequestBody QueryRequest request) {
        QueryResponse response = queryHandlingService.processQuery(request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/queries/records
     * Retrieves the routing audit log (in-memory, for dashboard use).
     */
    @GetMapping("/records")
    public ResponseEntity<?> getRecords() {
        return ResponseEntity.ok(queryHandlingService.getRecentRecords());
    }
}
