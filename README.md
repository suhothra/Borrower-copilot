# Borrower Copilot

## Lokta Build Challenge

Borrower Copilot is a personal borrower self-assessment application designed to help Indian borrowers understand their borrowing position before approaching a lender.

The app answers four important questions:

1. Should I borrow at all?
2. How much can I safely borrow?
3. What is a fair interest-rate range for my profile?
4. What EMI or monthly outflow should I avoid crossing?

The app also generates a **Borrower Negotiation Card** that the borrower can use when comparing lender offers.

---

# Features

## O1 — Borrow Decision

The app provides one of three possible outcomes:

- **BORROW**
- **BORROW LESS**
- **DON'T BORROW**

The decision is based on affordability, existing debt, repayment stress and other borrower information.

A DON'T BORROW outcome is intentionally reachable when the borrower shows signs of financial stress.

---

## O2 — Borrowing Amount

Borrower Copilot clearly separates two different numbers:

### Safe Borrowing Amount

The amount the borrower can reasonably carry based on income, expenses, existing EMIs and affordability rules.

### Likely Lender Sanction

The amount a lender may potentially sanction based on factors such as:

- Income
- Borrower type
- Credit profile
- Repayment history
- Collateral
- Loan type

These numbers may be different.

The app recommends that borrowers use the **safe borrowing amount** rather than automatically borrowing the maximum amount a lender may approve.

---

## O3 — Fair Interest Rate

The app provides:

- A fair interest-rate range
- An estimated APR range

The rate is displayed as a range instead of a single number because lender pricing depends on multiple factors and borrower information may be incomplete.

Unknown information widens the estimate instead of being treated as a negative or zero value.

---

## O4 — EMI Ceiling

The app calculates:

- Safe monthly EMI ceiling
- Monthly disposable income
- Stress-case EMI
- Tenure trade-offs

The borrower can compare shorter and longer loan tenures.

Longer tenure may reduce monthly EMI but usually increases the total interest paid.

---

# Negotiation Card

The Borrower Negotiation Card summarises:

- Requested loan amount
- Recommended borrowing amount
- Safe EMI ceiling
- Fair interest-rate range
- Estimated APR
- Why the recommendation was generated
- Questions to ask the lender

The goal is to help borrowers compare lender offers more confidently.

---

# Question Design

Borrower Copilot uses a two-tier question design.

## Must Questions

The minimum borrower information includes:

- Age
- Employment type
- Monthly income
- Loan amount requested
- Loan purpose
- Loan type
- Existing EMIs
- Household expenses
- Credit score if known
- Repayment situation

The app can still generate results when some additional information is missing.

---

## Adaptive Questions

Additional questions depend on the borrower profile.

For example:

### Salaried Borrowers

The app may consider:

- Employment history
- Income stability
- Credit score
- Existing EMIs
- Emergency savings

### Self-Employed Borrowers

The app may additionally consider:

- Business history
- Income variation
- ITR income
- Business loans
- Collateral value

### Informal Income Borrowers

The app may consider:

- Income variation
- Existing app loans
- High-cost debt
- Recent EMI bounces
- Repayment stress

Additional information is only useful when it changes an output.

---

# Borrower Types Supported

The application supports:

- Salaried borrowers
- Self-employed borrowers
- Informal income borrowers

The assessment rules adapt based on the selected borrower type.

---

# Loan Products

The rules support loan products including:

- Personal Loan
- Business Loan
- Secured Business Loan
- Loan Against Property
- Two-Wheeler Loan
- Gold Loan

The recommended product may change based on:

- Loan purpose
- Borrower profile
- Collateral availability
- Affordability
- Repayment risk

---

# Three Challenge Borrowers

The app includes the three borrower examples provided in the Lokta challenge.

## Priya

- Age: 29
- Location: Bengaluru
- Employment: Salaried
- Software Engineer
- Monthly income: ₹1,10,000
- Existing car EMI: ₹14,000
- Credit score: 780
- Requested loan: ₹8,00,000
- Purpose: Wedding

See:

```text
PRIYA_RUNTHROUGH.md