import React, { useState } from "react";
import { QueryAPI } from "../services/api";

/**
 * QueryForm — lets students submit queries.
 * Category is optional; IntelligentRouting auto-detects it server-side.
 */
export default function QueryForm() {
  const [form, setForm] = useState({
    studentId: "", studentName: "", category: "", description: "", subject: "",
  });
  const [response, setResponse] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const categories = [
    "", "COURSE_INQUIRY", "EXAM_SCHEDULE", "CREDIT_TRANSFER",
    "ACADEMIC_POLICY", "CERTIFICATE", "INTERNSHIP_GUIDELINE", "NOTICE_DEADLINE",
  ];

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const { data } = await QueryAPI.submit(form);
      setResponse(data);
    } catch (err) {
      setError(err.response?.data?.error || "Submission failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Submit a Query</h2>
      <form onSubmit={handleSubmit} className="form">
        <input name="studentId"   placeholder="Student ID *"   value={form.studentId}   onChange={handleChange} required />
        <input name="studentName" placeholder="Full Name *"     value={form.studentName} onChange={handleChange} required />
        <input name="subject"     placeholder="Subject"         value={form.subject}     onChange={handleChange} />
        <select name="category" value={form.category} onChange={handleChange}>
          {categories.map((c) => (
            <option key={c} value={c}>{c || "— Auto-detect category —"}</option>
          ))}
        </select>
        <textarea
          name="description"
          placeholder="Describe your query in detail *"
          value={form.description}
          onChange={handleChange}
          required
          rows={4}
        />
        <button type="submit" disabled={loading}>
          {loading ? "Processing…" : "Submit Query"}
        </button>
      </form>

      {error && <div className="error">{error}</div>}

      {response && (
        <div className="response-box">
          <h3>Response</h3>
          <p><strong>Category:</strong> {response.category}</p>
          <p><strong>Handler:</strong> {response.handlerUsed}</p>
          <p><strong>AI Invoked:</strong> {response.aiInvoked ? "Yes" : "No"}</p>
          <div className="ai-response">{response.response}</div>
        </div>
      )}
    </div>
  );
}
