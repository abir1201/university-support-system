import React, { useState } from "react";
import { TicketAPI } from "../services/api";

/**
 * TicketForm — students submit service request tickets.
 */
export default function TicketForm() {
  const [form, setForm] = useState({
    studentId: "", studentName: "", email: "", serviceType: "",
    requestDetails: "", certificateType: "", purpose: "",
    courseCode: "", correctionType: "", currentValue: "",
    requestedValue: "", permissionType: "",
  });
  const [ticket, setTicket] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const serviceTypes = [
    { value: "CERTIFICATE_REQUEST", label: "Certificate Request" },
    { value: "ACADEMIC_CORRECTION",  label: "Academic Record Correction" },
    { value: "COURSE_PERMISSION",    label: "Course Permission" },
  ];

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const { data } = await TicketAPI.create(form);
      setTicket(data);
    } catch (err) {
      setError(err.response?.data?.error || "Ticket creation failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="card">
      <h2>Service Request</h2>
      <form onSubmit={handleSubmit} className="form">
        <input name="studentId"   placeholder="Student ID *"   value={form.studentId}   onChange={handleChange} required />
        <input name="studentName" placeholder="Full Name *"     value={form.studentName} onChange={handleChange} required />
        <input name="email"       placeholder="Email *"         value={form.email}       onChange={handleChange} required type="email" />
        <select name="serviceType" value={form.serviceType} onChange={handleChange} required>
          <option value="">— Select Service Type —</option>
          {serviceTypes.map((s) => (
            <option key={s.value} value={s.value}>{s.label}</option>
          ))}
        </select>
        <textarea
          name="requestDetails"
          placeholder="Describe your request in detail *"
          value={form.requestDetails}
          onChange={handleChange}
          required
          rows={4}
        />
        <button type="submit" disabled={loading}>
          {loading ? "Submitting…" : "Submit Request"}
        </button>
      </form>

      {error && <div className="error">{error}</div>}

      {ticket && (
        <div className="response-box success">
          <h3>Ticket Created</h3>
          <p><strong>Ticket Number:</strong> {ticket.ticketNumber}</p>
          <p><strong>Status:</strong> {ticket.status}</p>
          <p><strong>Priority:</strong> {ticket.priority}</p>
          <p><strong>Assigned To:</strong> {ticket.assignedTo}</p>
          <p>You will receive an email notification at each status update.</p>
        </div>
      )}
    </div>
  );
}
