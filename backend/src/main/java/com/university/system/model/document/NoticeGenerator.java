package com.university.system.model.document;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Generates university notices and announcements.
 * Implements DocumentGenerator (POLYMORPHISM).
 */
@Component
public class NoticeGenerator implements DocumentGenerator {

    private final TemplateFormatter templateFormatter;

    public NoticeGenerator(TemplateFormatter templateFormatter) {
        this.templateFormatter = templateFormatter;
    }

    private static final String TEMPLATE = """
        NOTICE NO. {{noticeNumber}}
        Subject: {{subject}}
        Date: {{date}}
        Issued By: {{issuedBy}}

        Dear Students / Faculty / Staff,

        {{body}}

        Deadline / Effective Date: {{deadline}}

        For queries, contact: {{contactEmail}}

        Authorized by: {{authorizedBy}}
        {{registrar}}
        """;

    @Override
    public String generate(Map<String, String> data) {
        String formatted = templateFormatter.format(TEMPLATE, data);
        return templateFormatter.addLetterhead(formatted);
    }

    @Override
    public String getDocumentType() {
        return "NOTICE";
    }

    @Override
    public String getFileExtension() {
        return "pdf";
    }
}
