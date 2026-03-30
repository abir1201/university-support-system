import React, { useState } from "react";
import { DocumentAPI } from "../services/api";

/**
 * DocumentPanel — generates academic letters, notices, and meeting reports.
 */
export default function DocumentPanel() {
  const [docType, setDocType] = useState("ACADEMIC_LETTER");
  const [fields, setFields] = useState({});
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const docTypes = [
    { value: "ACADEMIC_LETTER", label: "Academic Letter" },
    { value: "NOTICE",          label: "University Notice" },
    { value: "MEETING_REPORT",  label: "Meeting Report" },
  ];

  // Dynamic field sets per document type
  const fieldConfig = {
    ACADEMIC_LETTER: ["studentName", "studentId", "program", "enrollmentDate", "standing", "cgpa", "purpose"],
    NOTICE:          ["noticeNumber", "subject", "body", "deadline", "contactEmail", "authorizedBy", "issuedBy"],
    MEETING_REPORT:  ["meetingTitle", "meetingDate", "meetingTime", "venue", "chairperson", "agenda", "decisions"],
  };

  const handleChange = (e) =>
    setFields((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleGenerate = async () => {
    setLoading(true);
    try {
      const { data } = await DocumentAPI.generate({
        documentType: docType,
        templateData: fields,
      });
      setResult(data.content);
    } catch {
      setResult("Document generation failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Document Generator</h2>
      <select value={docType} onChange={(e) => setDocType(e.target.value)}>
        {docTypes.map((d) => (
          <option key={d.value} value={d.value}>{d.label}</option>
        ))}
      </select>

      <div className="form" style={{ marginTop: "1rem" }}>
        {(fieldConfig[docType] || []).map((field) => (
          <input
            key={field}
            name={field}
            placeholder={field.replace(/([A-Z])/g, " $1").trim()}
            value={fields[field] || ""}
            onChange={handleChange}
          />
        ))}
        <button onClick={handleGenerate} disabled={loading}>
          {loading ? "Generating…" : "Generate Document"}
        </button>
      </div>

      {result && (
        <div className="response-box doc-preview">
          <h3>Generated Document</h3>
          <pre>{result}</pre>
        </div>
      )}
    </div>
  );
}
