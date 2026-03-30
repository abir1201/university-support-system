package com.university.system.model.document;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Generates formal meeting reports and minutes.
 * Implements DocumentGenerator (POLYMORPHISM).
 */
@Component
public class MeetingReportGenerator implements DocumentGenerator {

    private final TemplateFormatter templateFormatter;

    public MeetingReportGenerator(TemplateFormatter templateFormatter) {
        this.templateFormatter = templateFormatter;
    }

    private static final String TEMPLATE = """
        MEETING REPORT
        Meeting Title    : {{meetingTitle}}
        Date & Time      : {{meetingDate}} at {{meetingTime}}
        Venue            : {{venue}}
        Chaired By       : {{chairperson}}
        Prepared By      : {{preparedBy}}

        ATTENDEES:
        {{attendees}}

        AGENDA:
        {{agenda}}

        DISCUSSION POINTS:
        {{discussion}}

        DECISIONS MADE:
        {{decisions}}

        ACTION ITEMS:
        {{actionItems}}

        NEXT MEETING: {{nextMeeting}}

        Prepared on: {{date}}
        """;

    @Override
    public String generate(Map<String, String> data) {
        String formatted = templateFormatter.format(TEMPLATE, data);
        return templateFormatter.addLetterhead(formatted);
    }

    @Override
    public String getDocumentType() {
        return "MEETING_REPORT";
    }
}
