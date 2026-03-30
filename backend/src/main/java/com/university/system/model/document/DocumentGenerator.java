package com.university.system.model.document;

import java.util.Map;

/**
 * DocumentGenerator interface — defines the contract for all document-generation strategies.
 * Demonstrates INTERFACE SEGREGATION and POLYMORPHISM:
 * - AcademicLetterGenerator, NoticeGenerator, MeetingReportGenerator all implement this.
 * - Each has a different template and generation logic.
 */
public interface DocumentGenerator {

    /**
     * Generates and returns the document content as a String.
     * @param data key-value pairs to inject into the template
     */
    String generate(Map<String, String> data);

    /**
     * Returns the document type identifier.
     */
    String getDocumentType();

    /**
     * Returns the file extension this generator produces.
     */
    default String getFileExtension() {
        return "pdf";
    }
}
