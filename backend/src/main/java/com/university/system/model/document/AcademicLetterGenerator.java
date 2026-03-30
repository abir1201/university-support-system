package com.university.system.model.document;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Generates official academic letters (enrollment, completion, good standing).
 * Implements DocumentGenerator (POLYMORPHISM via interface).
 * Uses TemplateFormatter (COMPOSITION over inheritance).
 */
@Component
public class AcademicLetterGenerator implements DocumentGenerator {

    private final TemplateFormatter templateFormatter;

    public AcademicLetterGenerator(TemplateFormatter templateFormatter) {
        this.templateFormatter = templateFormatter;
    }

    private static final String TEMPLATE = """
        To Whom It May Concern,

        This letter certifies that {{studentName}} (ID: {{studentId}})
        is a {{status}} student at {{university}}, enrolled in the
        {{program}} program since {{enrollmentDate}}.

        Current Academic Standing: {{standing}}
        CGPA: {{cgpa}}

        This letter is issued upon the student's request for the purpose of: {{purpose}}.

        Issued by {{registrar}} on {{date}}.

        Registrar's Signature: ________________________
        Official Seal: [SEAL]
        """;

    @Override
    public String generate(Map<String, String> data) {
        String formatted = templateFormatter.format(TEMPLATE, data);
        return templateFormatter.addLetterhead(formatted);
    }

    @Override
    public String getDocumentType() {
        return "ACADEMIC_LETTER";
    }
}
