/**
 * API service layer — all HTTP calls go through here.
 * Centralizes base URL and default headers.
 * React components never call axios directly (DEPENDENCY INVERSION).
 */

import axios from "axios";

const BASE = "/api";  // proxied to http://localhost:8080 in development

export const QueryAPI = {
  submit: (payload) => axios.post(`${BASE}/queries`, payload),
  getRecords: () => axios.get(`${BASE}/queries/records`),
};

export const TicketAPI = {
  create: (payload) => axios.post(`${BASE}/tickets`, payload),
  updateStatus: (ticketNumber, status, notes) =>
    axios.patch(`${BASE}/tickets/${ticketNumber}/status`, null, {
      params: { status, notes },
    }),
  runEscalationCheck: () => axios.post(`${BASE}/tickets/escalation-check`),
};

export const DocumentAPI = {
  generate: (payload) => axios.post(`${BASE}/documents/generate`, payload),
  summarize: (content) =>
    axios.post(`${BASE}/documents/summarize`, { content }),
  generateAuditReport: (payload) =>
    axios.post(`${BASE}/documents/audit-report`, payload),
};
