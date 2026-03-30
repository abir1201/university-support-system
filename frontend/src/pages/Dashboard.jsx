import React, { useState } from "react";
import QueryForm from "../components/QueryForm";
import TicketForm from "../components/TicketForm";
import DocumentPanel from "../components/DocumentPanel";

/**
 * Dashboard — main page with tab navigation for the three domains.
 */
export default function Dashboard() {
  const [tab, setTab] = useState("query");

  const tabs = [
    { id: "query",    label: "Student Queries" },
    { id: "ticket",   label: "Service Requests" },
    { id: "document", label: "Documents & Reports" },
  ];

  return (
    <div className="dashboard">
      <header className="app-header">
        <div className="header-content">
          <h1>University Management System</h1>
          <p>Student Query Handling · Service Tickets · Documentation</p>
        </div>
      </header>

      <nav className="tab-nav">
        {tabs.map((t) => (
          <button
            key={t.id}
            className={`tab-btn ${tab === t.id ? "active" : ""}`}
            onClick={() => setTab(t.id)}
          >
            {t.label}
          </button>
        ))}
      </nav>

      <main className="main-content">
        {tab === "query"    && <QueryForm />}
        {tab === "ticket"   && <TicketForm />}
        {tab === "document" && <DocumentPanel />}
      </main>

      <footer className="app-footer">
        <p>University Management System v1.0 · Powered by Groq AI</p>
      </footer>
    </div>
  );
}
