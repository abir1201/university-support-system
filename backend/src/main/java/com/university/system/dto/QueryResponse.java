package com.university.system.dto;

import java.util.Objects;

/**
 * DTO for query API response — carries the AI or KB response back to the client.
 */
public class QueryResponse {
    private Long queryId;
    private String studentId;
    private String category;
    private String handlerUsed;
    private String response;
    private String status;
    private boolean aiInvoked;

    public QueryResponse() {}

    private QueryResponse(Builder builder) {
        this.queryId = builder.queryId;
        this.studentId = builder.studentId;
        this.category = builder.category;
        this.handlerUsed = builder.handlerUsed;
        this.response = builder.response;
        this.status = builder.status;
        this.aiInvoked = builder.aiInvoked;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long queryId;
        private String studentId;
        private String category;
        private String handlerUsed;
        private String response;
        private String status;
        private boolean aiInvoked;

        public Builder queryId(Long queryId) {
            this.queryId = queryId;
            return this;
        }

        public Builder studentId(String studentId) {
            this.studentId = studentId;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder handlerUsed(String handlerUsed) {
            this.handlerUsed = handlerUsed;
            return this;
        }

        public Builder response(String response) {
            this.response = response;
            return this;
        }

        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder aiInvoked(boolean aiInvoked) {
            this.aiInvoked = aiInvoked;
            return this;
        }

        public QueryResponse build() {
            return new QueryResponse(this);
        }
    }

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getHandlerUsed() {
        return handlerUsed;
    }

    public void setHandlerUsed(String handlerUsed) {
        this.handlerUsed = handlerUsed;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAiInvoked() {
        return aiInvoked;
    }

    public void setAiInvoked(boolean aiInvoked) {
        this.aiInvoked = aiInvoked;
    }

    @Override
    public String toString() {
        return "QueryResponse{" +
            "queryId=" + queryId +
            ", studentId='" + studentId + '\'' +
            ", category='" + category + '\'' +
            ", handlerUsed='" + handlerUsed + '\'' +
            ", response='" + response + '\'' +
            ", status='" + status + '\'' +
            ", aiInvoked=" + aiInvoked +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryResponse that = (QueryResponse) o;
        return aiInvoked == that.aiInvoked &&
            Objects.equals(queryId, that.queryId) &&
            Objects.equals(studentId, that.studentId) &&
            Objects.equals(category, that.category) &&
            Objects.equals(handlerUsed, that.handlerUsed) &&
            Objects.equals(response, that.response) &&
            Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryId, studentId, category, handlerUsed, response, status, aiInvoked);
    }
}
