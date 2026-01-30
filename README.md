# Document Workflow Demo

## Overview

This project is a small engineering experiment that demonstrates how a
**document-centric system** can be designed and implemented in a way that is
**structured, auditable, and safe to evolve**.

The goal is not to define a universal architecture,
but to explore what happens when a system is built strictly around a clearly identified domain core.

It is **not a product** and does not focus on UI polish.  
The purpose of this demo is to show how complex business systems can be organized around a clear **domain core** with strict rules enforced on the backend.

This kind of structure is commonly found in:
- Approval workflows
- Financial or accounting systems
- Compliance-driven platforms
- Asynchronous integration systems

---

## What This System Models

The system models the lifecycle of a **business document** —  
a record that must go through a controlled process before reaching a final state.

Examples in real systems could be:
- An invoice
- An expense report
- A transaction request
- Any record that requires review, approval, and audit

The key idea is that **a document cannot be modified freely**.
Every change must follow explicit rules and transitions.

---

## Domain Core: Document

The entire system is organized around a single domain core: **Document**.

Document is not presented as a universal solution.  
It is simply how the domain core manifested in this particular experiment.

Everything else exists to support the document lifecycle:
- Users interact with documents
- Roles determine who can act on a document
- Audit logs record every state-changing action

There is no generic CRUD behavior.
All operations are expressed as **domain actions** on a document.

---

## Document Lifecycle (State Machine)

The document follows a strict, backend-enforced state machine:
DRAFT
→ SUBMITTED
→ UNDER_REVIEW
→ APPROVED / REJECTED
→ ARCHIVED

Key principles:
- States cannot be skipped
- Invalid transitions are rejected at the service layer
- The frontend cannot bypass backend rules
- Each transition represents a real business decision

This makes the system predictable and safe under change.

---

## Roles and State-Bound Permissions

Permissions are **bound to business states**, not just endpoints.

Roles in this demo:
- **Creator** – creates and edits drafts, submits documents
- **Reviewer** – reviews submitted documents and requests changes
- **Approver** – makes final approval or rejection decisions

Examples:
- A Creator cannot approve a document
- A Reviewer cannot edit draft content
- An Approver cannot act before review

This reflects how real enterprise systems enforce responsibility and separation of concerns.

---

## Audit and Traceability

Every state-changing action generates an **audit record**, including:
- Who performed the action
- When it happened
- From which state to which state
- Optional reason or note

Audit logs make the system:
- Traceable
- Safer to debug
- Safer to extend in future phases
- Suitable for compliance-driven environments

---

## Why This Structure Matters

This demo focuses on **structure, not features**.

It demonstrates:
- How to design around a clear domain core
- How to enforce rules where they matter (backend)
- How to avoid fragile, UI-driven workflows
- How to build systems that can grow without collapsing

The same structure can be applied to many real-world systems that require correctness, control, and accountability.

This demo does not suggest that all systems should follow this structure.

Instead, it shows how identifying and protecting a domain core
can simplify decision-making and reduce accidental complexity,
regardless of system type.
---

## Notes

- UI is intentionally minimal and server-rendered
- The demo prioritizes clarity of flow over completeness
- The system is designed to be extended with additional rules, integrations, or persistence strategies