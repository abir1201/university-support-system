package com.university.system.service;

import com.university.system.dto.QueryRequest;
import com.university.system.dto.QueryResponse;
import com.university.system.model.query.*;
import com.university.system.repository.CourseInquiryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * QueryHandlingService orchestrates the full student query lifecycle:
 *   1. Receive raw query
 *   2. Infer category (IntelligentRouting)
 *   3. Route to FAQ / KnowledgeBase / AI (QueryRouter)
 *   4. Persist the query with the response
 *   5. Record the routing decision (QueryRecordManager)
 *
 * This is the SERVICE LAYER — it coordinates domain objects without
 * exposing internal logic to controllers.
 *
 * SOLID: SINGLE RESPONSIBILITY — manages only query processing workflow.
 * SOLID: DEPENDENCY INVERSION — depends on abstractions (QueryHandler interface).
 */
@Service
@Transactional
public class QueryHandlingService {

    private final QueryRouter queryRouter;
    private final IntelligentRouting intelligentRouting;
    private final QueryRecordManager queryRecordManager;
    private final AIService aiService;
    private final CourseInquiryRepository courseInquiryRepository;

    public QueryHandlingService(QueryRouter queryRouter,
                                 IntelligentRouting intelligentRouting,
                                 QueryRecordManager queryRecordManager,
                                 AIService aiService,
                                 CourseInquiryRepository courseInquiryRepository) {
        this.queryRouter = queryRouter;
        this.intelligentRouting = intelligentRouting;
        this.queryRecordManager = queryRecordManager;
        this.aiService = aiService;
        this.courseInquiryRepository = courseInquiryRepository;
    }

    /**
     * Processes an incoming student query end-to-end.
     */
    public QueryResponse processQuery(QueryRequest request) {
        // Step 1: Resolve category — use provided or infer from description
        String category = (request.getCategory() != null && !request.getCategory().isBlank())
            ? request.getCategory()
            : intelligentRouting.inferCategory(request.getDescription());

        // Step 2: Route through the handler chain
        String handlerResponse = queryRouter.route(category, request.getDescription());
        String handlerName = queryRouter.resolveHandlerName(category);
        boolean aiInvoked = false;

        // Step 3: If FAQ/KB could not handle → call AI
        if (handlerResponse == null) {
            handlerResponse = aiService.query(category, request.getDescription());
            handlerName = "AI_CHATBOT";
            aiInvoked = true;
        }

        // Step 4: Persist as a CourseInquiry (extend for other subtypes as needed)
        CourseInquiry record = new CourseInquiry();
        record.setStudentId(request.getStudentId());
        record.setStudentName(request.getStudentName());
        record.setSubject(request.getSubject() != null ? request.getSubject() : category);
        record.setDescription(request.getDescription());
        record.setAiResponse(handlerResponse);
        record.setStatus(Query.QueryStatus.RESOLVED);
        record.setCourseCode("GENERAL");
        record.setInquiryType("GENERAL_INQUIRY");
        CourseInquiry saved = courseInquiryRepository.save(record);

        // Step 5: Audit record
        queryRecordManager.record(request.getStudentId(), category, handlerName, aiInvoked);

        return QueryResponse.builder()
            .queryId(saved.getId())
            .studentId(request.getStudentId())
            .category(category)
            .handlerUsed(handlerName)
            .response(handlerResponse)
            .status("RESOLVED")
            .aiInvoked(aiInvoked)
            .build();
    }

    /**
     * Returns routing statistics for the dashboard.
     */
    @Transactional(readOnly = true)
    public QueryRecordManager.QueryRecord[] getRecentRecords() {
        return queryRecordManager.getAll().toArray(new QueryRecordManager.QueryRecord[0]);
    }
}
