# Borrower Copilot – 5 Minute Walkthrough

## 1. Introduction

Borrower Copilot is a borrower self-assessment tool designed for Indian borrowers.

The goal is to help a borrower answer four questions before approaching a lender:

1. Should I borrow at all?
2. How much can I safely borrow?
3. What is a fair interest rate for my profile?
4. What EMI should I agree to?

The app also provides a Negotiation Card that summarises the borrower's position.

---

# 2. App Flow

The borrower opens the application and can either:

- Start a new assessment
- Use one of the three challenge examples:
    - Priya
    - Ravi
    - Anita

The borrower provides information about:

- Income
- Employment type
- Loan request
- Existing EMIs
- Household expenses
- Credit score, if known
- Repayment situation

The app then applies borrower rules locally and produces the four outputs.

---

# 3. O1 — Should I Borrow?

The first output gives one of three verdicts:

## BORROW

Used when affordability and repayment conditions appear manageable.

## BORROW LESS

Used when the requested amount is above the estimated safe borrowing amount.

## DON'T BORROW

Used when the borrower shows strong repayment stress or financial risk.

For example, Anita receives a DON'T BORROW result because of:

- Existing high-cost debt
- A recent EMI bounce
- Informal and variable income
- Very limited remaining monthly cash flow

The app intentionally allows DON'T BORROW as a legitimate result.

---

# 4. O2 — How Much Should I Borrow?

The app intentionally shows two different numbers:

## Likely Lender Sanction

This is an estimate of what a lender may potentially approve.

## Safe Borrowing Amount

This is the amount the borrower can reasonably carry based on affordability.

The borrower is advised to use the safe borrowing amount.

The two numbers may be different.

For example, Priya may have a lender sanction estimate higher than her recommended safe borrowing amount.

This distinction is one of the core ideas of Borrower Copilot.

---

# 5. O3 — Fair Interest Rate

Borrower Copilot does not pretend to know the exact interest rate a lender will offer.

Instead, it shows:

- A fair interest-rate range
- An estimated APR range

The range changes depending on information such as:

- Income type
- Credit score
- Repayment history
- Loan type
- Collateral
- Income stability

When information is missing, the app widens the range.

For example:

> Unknown credit score does not mean poor credit.

Instead, it creates more uncertainty and therefore a wider interest-rate range.

---

# 6. O4 — EMI Ceiling

The app calculates a cautious monthly EMI ceiling.

The calculation considers:

- Monthly income
- Household expenses
- Existing EMIs
- Borrower risk
- Income stability

The borrower is advised not to agree to an EMI above this ceiling.

The app also includes a stress case.

For the stress case, income is reduced by approximately 20%.

This helps borrowers understand whether the loan would still be manageable if their income falls.

---

# 7. Adaptive Question Design

The app is designed so that different borrower types can receive different questions.

## Salaried Borrowers

Additional information may include:

- Employment history
- Job stability
- Credit score

## Self-Employed Borrowers

Additional information may include:

- Business history
- Income variation
- ITR income
- Collateral

## Informal Income Borrowers

Additional information may include:

- Income variation
- Existing app loans
- Recent EMI bounces
- Repayment stress

The goal is to avoid asking every borrower the same unnecessary questions.

---

# 8. Challenge Borrowers

## Priya

Priya is a salaried software engineer with:

- Stable income
- Strong credit score
- Existing car EMI

The app demonstrates a BORROW decision and separates her likely lender sanction from her safe borrowing amount.

---

## Ravi

Ravi is a self-employed kirana store owner with:

- Variable income
- Long business history
- No known credit score
- Valuable unencumbered property

The app demonstrates why Ravi may be better suited for a secured business loan or Loan Against Property.

His unknown credit score widens the interest-rate range.

---

## Anita

Anita has:

- Informal and variable income
- Existing high-cost app loans
- A recent EMI bounce
- Limited monthly cash flow

The app produces a DON'T BORROW verdict.

The assessment prioritises stabilising existing debt before adding another loan.

---

# 9. Negotiation Card

The Negotiation Card gives the borrower a simple summary that can be used when speaking with lenders.

It includes:

- Loan requested
- Recommended borrowing amount
- Safe EMI ceiling
- Fair interest-rate range
- Estimated APR
- Reasons behind the recommendation
- Questions to ask the lender

The borrower can use the card to compare lender offers more confidently.

---

# 10. Rules Engine

The rules are separated from the user interface.

The rule logic is documented in:

```text
RULES.md