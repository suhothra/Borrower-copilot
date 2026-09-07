Borrower Copilot – RULES.md
Version 1.0

This document explains the rules, thresholds, assumptions, and calculations used by Borrower Copilot.

The app is a borrower self-assessment tool. It does not make lending decisions and does not guarantee lender approval.

1. Core Principle

Borrower Copilot intentionally separates:

1.What a lender may sanction

2.What the borrower can safely afford

These two numbers can be different.

The app recommends the lower, safer amount based on the borrower's cash flow.
| What                    | Value / Rule                                                         | Why                                                                              | Source                   |
| ----------------------- | -------------------------------------------------------------------- | -------------------------------------------------------------------------------- | ------------------------ |
| Recommended loan amount | Minimum of safe loan amount and requested loan amount                | Prevent recommending more than the borrower asked for or can safely carry        | My judgement             |
| Likely lender sanction  | Safe loan amount × sanction multiplier                               | Estimates how lender underwriting factors may differ from borrower affordability | My judgement             |
| Safe loan amount        | Calculated from safe EMI, tenure and estimated average interest rate | Converts monthly affordability into a borrowing amount                           | Standard EMI mathematics |
The borrower type affects:

1.Affordability ratio
2.Interest-rate range
3.Sanction estimate
4.Adaptive questions
5.Product recommendation

3. Loan Products

The app currently supports:
| Product               |
| --------------------- |
| Personal Loan         |
| Home Loan             |
| Loan Against Property |
| Gold Loan             |
| Two-Wheeler Loan      |
| Business Loan         |

4. Credit Score Bands
   | Credit Profile |                  Score |
   | -------------- | ---------------------: |
   | Excellent      |                   750+ |
   | Good           |                700–749 |
   | Fair           |                650–699 |
   | Poor           |              Below 650 |
   | Unknown        | Borrower does not know |
   Unknown is not treated as zero or poor credit.

Instead, the interest-rate estimate is widened and confidence may be lower.

5. Confidence Rules

The app tracks how much additional information is available.

Additional information counted

The confidence calculation considers:

1.Credit score
2.Emergency savings
3.Recent EMI bounce information
4.Monthly income variation
5.ITR annual income
6.Collateral information
7.Outstanding or high-cost debt information
| Answer Ratio | Confidence | Why                                                                  | Source       |
| ------------ | ---------- | -------------------------------------------------------------------- | ------------ |
| 70% or more  | High       | More financial information allows a narrower estimate                | My judgement |
| 35% to 69%   | Medium     | Some important information is still missing                          | My judgement |
| Below 35%    | Low        | Estimates must remain wider because important information is unknown | My judgement |

6. Safe EMI Affordability
Base Affordability Ratio
   | Borrower Type              | Base Ratio |
   | -------------------------- | ---------: |
   | Salaried                   |        40% |
   | Self-employed              |        35% |
   | Informal / Variable Income |        30% |
The percentage is applied to disposable income, not gross income.
Disposable Income =
   Total Monthly Income
   − Household Expenses
   − Existing EMIs
Safe EMI =
   Disposable Income × Adjusted Affordability Ratio
Why these ratios?

These are cautious product assumptions designed to ensure that the borrower keeps a financial buffer after essential expenses and existing debt.

Source: My judgement

7. Income Stability Adjustments
   | Income Stability    |           Adjustment |
   | ------------------- | -------------------: |
   | Low / Unpredictable | −8 percentage points |
   | Moderate            | −3 percentage points |
   | Unknown             | −4 percentage points |
   | High                |         No reduction |
Why?

Less predictable income means the borrower should keep a larger safety buffer.

Source: My judgement

8. Employment and Business History Adjustments
   | Condition                   |           Adjustment |
   | --------------------------- | -------------------: |
   | Employment less than 1 year | −4 percentage points |
   | Condition                         |           Adjustment |
   | --------------------------------- | -------------------: |
   | Business less than 2 years        | −5 percentage points |
   | Business history 10 years or more | +3 percentage points |
Why?

Short employment or business history can indicate less predictable income. Long business history is treated as a positive stability signal.

Source: My judgement

9. Informal Income Variation Rules
   | Condition                  |           Adjustment |
   | -------------------------- | -------------------: |
   | Income variation above 30% | −6 percentage points |
   | Income variation unknown   | −4 percentage points |

Why?

Large income variation means that a borrower may not reliably earn the same amount every month.

Source: My judgement

10. Emergency Savings Adjustments
    | Emergency Savings      |           Adjustment |
    | ---------------------- | -------------------: |
    | Less than 1 month      | −5 percentage points |
    | 6 months or more       | +2 percentage points |
    | Between 1 and 6 months |        No adjustment |
    | Unknown                | No direct adjustment |
Why?

Low savings provide little protection during income shocks. Higher emergency savings provide a stronger repayment buffer.

Source: My judgement

11. Recent EMI Bounce
    | Condition         | Affordability Adjustment |
    | ----------------- | -----------------------: |
    | Recent EMI bounce |    −10 percentage points |
Why?

A recent EMI bounce is treated as evidence of repayment stress.

Source: My judgement

12. High-Cost Debt
    | Condition               | Affordability Adjustment |
    | ----------------------- | -----------------------: |
    | High-cost existing debt |     −6 percentage points |
Why?

Expensive existing debt can consume cash flow and increase repayment risk.

Source: My judgement

13. Affordability Ratio Limits
    | Minimum | Maximum |
    | ------: | ------: |
    |     10% |     45% |
The affordability ratio cannot go below 10% or above 45%.

Why?

This prevents extreme answers from producing an unrealistic zero-like or excessively aggressive affordability ratio.

Source: My judgement

14. Stress Test

The app tests a simple income shock.
Income falls by 20%
Stressed Income =
Total Income × 80%

Then:
Stressed Disposable Income =
Stressed Income
− Household Expenses
− Existing EMIs
Stress Case EMI =
Stressed Disposable Income × Affordability Ratio
Why?

The borrower should understand how an EMI becomes less affordable if income falls.

Source: My judgement

15. Base Interest Rate Bands

These are indicative starting bands.
| Loan Type             | Base Rate Range |
| --------------------- | --------------- |
| Home Loan             | 8.0% – 10.5%    |
| Loan Against Property | 9.0% – 13.0%    |
| Gold Loan             | 9.0% – 18.0%    |
| Two-Wheeler Loan      | 10.0% – 18.0%   |
| Business Loan         | 11.0% – 20.0%   |
| Personal Loan         | 11.0% – 22.0%   |
Important

These are indicative assumptions used by this prototype.

Actual lender rates can differ based on lender, borrower profile, product, location, collateral, credit history, documentation and market conditions.

Source: My judgement / prototype assumptions

16. Credit Score Interest Rate Adjustments
    | Credit Band | Minimum Rate Adjustment | Maximum Rate Adjustment |
    | ----------- | ----------------------: | ----------------------: |
    | Excellent   |                       0 |                    −3.0 |
    | Good        |                    +0.5 |                    −1.5 |
    | Fair        |                    +1.5 |                       0 |
    | Poor        |                    +4.0 |                    +4.0 |
    | Unknown     |                    −1.0 |                    +3.0 |
Why does Unknown widen both directions?

Unknown credit information should not automatically be treated as poor credit.

Instead, the range becomes wider because the app has less certainty.

17. Income Type Interest Rate Adjustments
    | Borrower Type | Maximum Rate Adjustment |
    | ------------- | ----------------------: |
    | Salaried      |                       0 |
    | Self-employed |                    +1.0 |
    | Informal      |                    +2.0 |
Why?

Variable income can create greater uncertainty for lender pricing.

Source: My judgement

18. Repayment History Interest Rate Adjustments
    | Condition         | Min Rate | Max Rate |
    | ----------------- | -------: | -------: |
    | Recent EMI bounce |     +2.0 |     +3.0 |
    | Condition      | Max Rate Adjustment |
    | -------------- | ------------------: |
    | High-cost debt |                +2.0 |
Why?

Repayment distress and expensive debt are treated as higher-risk signals.

Source: My judgement

19. Strong Business History Rate Adjustment
    | Condition                                        | Maximum Rate Adjustment |
    | ------------------------------------------------ | ----------------------: |
    | Self-employed with business history of 10+ years |                    −1.0 |
Why?

Long-running businesses may have more established income history.

Source: My judgement

20. Collateral Interest Rate Adjustment

If:

Collateral is available
Collateral value is greater than zero
Loan is not Gold Loan

Then:
| Adjustment   |                  Value |
| ------------ | ---------------------: |
| Minimum rate |  −1.0 percentage point |
| Maximum rate | −2.0 percentage points |
Why?

Collateral can reduce lender risk and may allow access to secured products.

Source: My judgement

21. Confidence and Interest Rate Range

Missing information widens the interest-rate range.
| Confidence | Adjustment         |
| ---------- | ------------------ |
| High       | No widening        |
| Medium     | Min −0.5, Max +0.5 |
| Low        | Min −1.0, Max +2.0 |
Principle

Fewer answers = wider estimate.

This directly implements the requirement that confidence should widen with missing information.

22. Minimum Interest Rate Floor
    Minimum possible estimated rate = 5%

The maximum rate must always be at least:

Minimum Rate + 1%

Why?

Prevents unrealistic or inverted interest-rate ranges.

Source: My judgement

23. Processing Fee Assumptions
    | Loan Type             | Assumed Processing Fee |
    | --------------------- | ---------------------: |
    | Home Loan             |                  0.50% |
    | Loan Against Property |                  1.00% |
    | Gold Loan             |                  1.00% |
    | Two-Wheeler Loan      |                  1.50% |
    | Business Loan         |                  1.50% |
    | Personal Loan         |                  2.00% |
    Important

These are prototype assumptions.

Actual lender processing fees and mandatory charges can differ.

Source: My judgement

24. Estimated APR Formula

The app uses a simplified APR estimate.

Minimum APR =
Minimum Interest Rate
+ Processing Fee × 0.8
  Maximum APR =
  Maximum Interest Rate
+ Processing Fee × 1.2

Important Limitation

This is not an official lender APR calculation.

The exact APR requires:

*Actual disbursal amount
*Processing fees
*Insurance
*Mandatory charges
*Repayment schedule
*Exact amortisation details
The app clearly labels this as an estimate.

25. Safe Loan Amount

The app calculates:

Average Interest Rate =
(Minimum Rate + Maximum Rate) ÷ 2

Then calculates the principal amount supported by:

Safe EMI
Average interest rate
Selected tenure

using standard EMI mathematics.

26. Likely Lender Sanction Estimate

The lender estimate starts with:

Likely Sanction =
Safe Loan Amount × Sanction Multiplier
| Borrower Type | Base Multiplier |
| ------------- | --------------: |
| Salaried      |            1.20 |
| Self-employed |            1.10 |
| Informal      |            1.00 |

27. Credit Score Sanction Adjustments
    | Credit Band | Multiplier Adjustment |
    | ----------- | --------------------: |
    | Excellent   |                 +0.15 |
    | Good        |                 +0.08 |
    | Fair        |                 −0.05 |
    | Poor        |                 −0.20 |
    | Unknown     |                 −0.05 |

28. Other Sanction Adjustments
    | Condition                  | Adjustment |
    | -------------------------- | ---------: |
    | Business history 10+ years |      +0.10 |
    | Valid collateral           |      +0.20 |
    | Recent EMI bounce          |      −0.25 |

29. Sanction Multiplier Limits

The final sanction multiplier is limited to:

Minimum = 0.40
Maximum = 1.60

Why?

Prevents extreme profile adjustments from producing unrealistic estimates.

Source: My judgement

30. Collateral-Based Sanction Cap

For:

Loan Against Property, or
Business Loan

when valid collateral is provided:

Collateral Cap = Collateral Value × 60%

The likely sanction cannot exceed this cap.

Why?

Provides a conservative prototype cap for secured borrowing.

Source: My judgement

31. Product Recommendation Rules
    Default

The app initially recommends the loan type selected by the borrower.

Secured Business Recommendation

If the borrower:

*Is self-employed
*Has collateral
*Has positive collateral value
*Wants the loan for business expansion

Then recommend:

Secured Business Loan or Loan Against Property

Why?

The app encourages the borrower to compare secured options instead of automatically choosing a more expensive unsecured business loan.

32. Productive Asset Rule

If the loan purpose is:

Productive Asset

and expected additional income is provided, the app explains that:

*The asset may generate future income.
*Current affordability is still based on current income.
*Future income is treated as uncertain.

33. O1 – Borrow Decision Rules

The decision has three possible outputs:

BORROW
BORROW LESS
DON'T BORROW

*DON'T BORROW – Rule 1: No Affordability

Trigger if:

Income ≤ 0
OR
Disposable Income ≤ 0
OR
Safe EMI ≤ 0

*DON'T BORROW – Rule 2: Very High Debt Burden

Trigger if:

Existing EMI / Total Income ≥ 50%

*DON'T BORROW – Rule 3: Repayment Distress

Trigger if:

Recent EMI Bounce = Yes
AND
High-Cost Debt = Yes

This is designed to identify situations similar to Anita's profile, where adding new debt could worsen existing repayment stress.

34. BORROW LESS Rules
Requested Amount Too High

Trigger if:

Requested Amount >
Safe Loan Amount × 1.20

Moderate Debt Burden

Trigger if:

Existing EMI / Total Income ≥ 35%

Income or Repayment Risk

Trigger if:

Income Stability contains "low"
OR
Recent EMI Bounce = Yes

35. BORROW Rule

If none of the DON'T BORROW or BORROW LESS conditions trigger:

BORROW

The app explains that:

Income
Expenses
Existing EMIs
Estimated EMI affordability

appear manageable based on the information provided.

36. Explainability Rules

Every assessment includes explanations for:

Monthly income considered
Household expenses
Existing EMI
Disposable income
Affordability ratio
Safe EMI
Estimated interest-rate range

This follows the product principle:

Every important number should have a reason.

37. Tenure Options

The app compares:

Tenure
36 months
60 months
84 months

For each tenure, the app calculates:

EMI
Total interest paid

Why?

A longer tenure generally reduces EMI but can increase total interest.

38. EMI Formula

The app uses standard reducing-balance EMI mathematics.

r = Annual Interest Rate ÷ 12 ÷ 100
EMI =
P × [r × (1+r)^n]
÷ [(1+r)^n − 1]

Where:

P = Principal
r = Monthly interest rate
n = Number of months

39. Negotiation Rules

Every borrower receives these negotiation suggestions:

1.Ask for complete APR and total cost.
2.Ask for processing, insurance and mandatory charges in writing.
3.Compare at least two lenders.
4.Avoid agreeing to an EMI above the calculated safe EMI.

Lender Rate Comparison

If the borrower enters a lender offer:

| Condition                      | App Response                                   |
| ------------------------------ | ---------------------------------------------- |
| Offered rate above fair range  | Ask lender to explain risk premium and charges |
| Offered rate below fair range  | Check mandatory fees and bundled products      |
| Offered rate inside fair range | Still compare APR and total charges            |

40. Unknown Information Rule

The app does not treat unknown information as zero.

Examples:

*Unknown credit score widens the interest range.
*Missing additional information lowers confidence.
*Low confidence widens the interest-rate range.
*Future income is not automatically counted as guaranteed affordability.

41. Transparency and Limitations

Borrower Copilot makes the following limitations explicit:

*This is a self-assessment tool, not a lender credit decision.
*Interest-rate bands are indicative assumptions.
*Actual lender pricing may differ.
*APR is estimated, not lender-calculated.
*Exact APR requires complete fees and repayment schedules.
*Future productive income is uncertain.
*Unknown information widens estimates instead of being treated as zero.

42. What This App Does Not Know

The current prototype does not include:

*Real credit bureau data
*Real lender underwriting models
*Bank transaction history
*Verified income documents
*Real-time lender interest rates
*Exact processing and insurance charges
*Full lender APR calculation
*Collateral valuation
*Legal loan eligibility checks

Therefore, all outputs are designed as decision-support estimates, not approvals or financial guarantees.

43. Summary of Key Thresholds
    | Rule                             |                       Value |
    | -------------------------------- | --------------------------: |
    | Salaried base affordability      |                         40% |
    | Self-employed base affordability |                         35% |
    | Informal base affordability      |                         30% |
    | Minimum affordability ratio      |                         10% |
    | Maximum affordability ratio      |                         45% |
    | High debt DON'T BORROW           |         EMI ≥ 50% of income |
    | Moderate debt BORROW LESS        |         EMI ≥ 35% of income |
    | Requested amount BORROW LESS     |        >120% of safe amount |
    | Recent bounce + high-cost debt   |                DON'T BORROW |
    | Income stress test               |        20% income reduction |
    | High confidence                  | ≥70% additional information |
    | Medium confidence                | ≥35% additional information |
    | Low confidence                   | <35% additional information |
    | Collateral sanction cap          |           60% of collateral |
    | Short employment adjustment      |                     <1 year |
    | Short business adjustment        |                    <2 years |
    | Strong business history          |                   ≥10 years |
    | Low emergency savings            |                    <1 month |
    | Strong emergency savings         |                   ≥6 months |
    | High informal income variation   |                        >30% |
    | Tenure comparison                |         36 / 60 / 84 months |

Final Disclaimer

Borrower Copilot is an educational borrower self-assessment tool. It does not provide a loan sanction, guarantee approval, or replace professional financial advice.

All rate bands, affordability thresholds, sanction estimates, processing fee assumptions, and risk adjustments in Version 1.0 should be treated as transparent prototype rules unless explicitly connected to a verified lender or regulatory source.
