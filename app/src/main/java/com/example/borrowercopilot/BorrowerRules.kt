package com.example.borrowercopilot

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

// ============================================================
// ENUMS
// ============================================================

enum class BorrowerType(val displayName: String) {
    SALARIED("Salaried"),
    SELF_EMPLOYED("Self-employed"),
    INFORMAL("Informal / Variable Income")
}

enum class LoanType(val displayName: String) {
    PERSONAL("Personal Loan"),
    HOME("Home Loan"),
    LAP("Loan Against Property"),
    GOLD("Gold Loan"),
    TWO_WHEELER("Two-Wheeler Loan"),
    BUSINESS("Business Loan")
}

enum class LoanPurpose(val displayName: String) {
    WEDDING("Wedding / Personal Expense"),
    HOME_PURCHASE("Home Purchase"),
    BUSINESS_EXPANSION("Business Expansion"),
    VEHICLE("Vehicle Purchase"),
    PRODUCTIVE_ASSET("Productive Asset"),
    DEBT_CONSOLIDATION("Debt Consolidation"),
    EDUCATION("Education"),
    MEDICAL("Medical"),
    OTHER("Other")
}

enum class CreditScoreBand(val displayName: String) {
    EXCELLENT("Excellent (750+)"),
    GOOD("Good (700–749)"),
    FAIR("Fair (650–699)"),
    POOR("Poor (Below 650)"),
    UNKNOWN("I don't know")
}

enum class BorrowDecision {
    BORROW,
    BORROW_LESS,
    DONT_BORROW
}

enum class ConfidenceLevel(val displayName: String) {
    HIGH("High"),
    MEDIUM("Medium"),
    LOW("Low")
}

// ============================================================
// BORROWER PROFILE
// ============================================================

data class BorrowerProfile(

    // Must questions
    val borrowerType: BorrowerType,
    val loanType: LoanType = LoanType.PERSONAL,
    val loanPurpose: LoanPurpose = LoanPurpose.OTHER,

    val age: Int = 30,

    val monthlyIncome: Double,
    val householdExpenses: Double,
    val existingEmi: Double,

    val desiredLoanAmount: Double,
    val tenureMonths: Int,

    val creditScore: CreditScoreBand,

    // Income information
    val incomeStability: String = "Unknown",
    val employmentYears: Double = 0.0,
    val businessYears: Double = 0.0,

    // Informal / variable income
    val monthlyIncomeVariation: Double = -1.0,

    // Additional questions
    val emergencySavingsMonths: Double = -1.0,
    val hasRecentEmiBounce: Boolean? = null,

    val itrAnnualIncome: Double = -1.0,

    // Collateral
    val collateralValue: Double = -1.0,
    val hasCollateral: Boolean? = null,

    // Co-applicant
    val coApplicantMonthlyIncome: Double = -1.0,

    // Existing debt details
    val outstandingDebt: Double = -1.0,
    val highCostDebt: Boolean? = null,

    // Productive loan information
    val expectedAdditionalMonthlyIncome: Double = -1.0,

    // Offers received
    val lenderOfferedRate: Double = -1.0
)

// ============================================================
// TENURE OPTION
// ============================================================

data class TenureOption(
    val months: Int,
    val emi: Double,
    val totalInterest: Double
)

// ============================================================
// ASSESSMENT RESULT
// ============================================================

data class AssessmentResult(

    // O1
    val decision: BorrowDecision,
    val decisionTitle: String,
    val decisionReason: String,

    // O2
    val safeLoanAmount: Double,
    val likelySanctionAmount: Double,
    val recommendedLoanAmount: Double,

    val safeAmountWhy: String,
    val sanctionAmountWhy: String,

    // O3
    val minInterestRate: Double,
    val maxInterestRate: Double,

    val processingFeePercent: Double,

    val minEstimatedApr: Double,
    val maxEstimatedApr: Double,

    val interestRateWhy: String,

    // O4
    val safeEmi: Double,
    val stressCaseEmi: Double,

    val monthlyDisposableIncome: Double,

    val recommendedTenure: Int,

    val tenureOptions: List<TenureOption>,

    val emiWhy: String,
    val stressCaseExplanation: String,

    // Product recommendation
    val recommendedProduct: String,
    val productReason: String,

    // Confidence
    val confidence: ConfidenceLevel,
    val confidenceReason: String,

    // Explainability
    val reasons: List<String>,

    // Negotiation
    val negotiationTips: List<String>,

    // Transparency
    val assumptions: List<String>
)

// ============================================================
// BORROWER RULES ENGINE
// ============================================================

object BorrowerRules {

    fun assess(profile: BorrowerProfile): AssessmentResult {

        // ----------------------------------------------------
        // 1. CLEAN BASIC INPUTS
        // ----------------------------------------------------

        val income = max(0.0, profile.monthlyIncome)

        val coApplicantIncome =
            if (profile.coApplicantMonthlyIncome >= 0)
                profile.coApplicantMonthlyIncome
            else
                0.0

        val totalIncome = income + coApplicantIncome

        val expenses = max(0.0, profile.householdExpenses)

        val existingEmi = max(0.0, profile.existingEmi)

        val disposableIncome =
            max(0.0, totalIncome - expenses - existingEmi)

        val debtRatio =
            if (totalIncome > 0)
                existingEmi / totalIncome
            else
                1.0


        // ----------------------------------------------------
        // 2. CONFIDENCE CALCULATION
        // ----------------------------------------------------

        var answeredAdditionalQuestions = 0
        var possibleAdditionalQuestions = 7

        if (profile.creditScore != CreditScoreBand.UNKNOWN) {
            answeredAdditionalQuestions++
        }

        if (
            profile.emergencySavingsMonths >= 0
        ) {
            answeredAdditionalQuestions++
        }

        if (
            profile.hasRecentEmiBounce != null
        ) {
            answeredAdditionalQuestions++
        }

        if (
            profile.monthlyIncomeVariation >= 0
        ) {
            answeredAdditionalQuestions++
        }

        if (
            profile.itrAnnualIncome >= 0
        ) {
            answeredAdditionalQuestions++
        }

        if (
            profile.collateralValue >= 0 ||
            profile.hasCollateral != null
        ) {
            answeredAdditionalQuestions++
        }

        if (
            profile.outstandingDebt >= 0 ||
            profile.highCostDebt != null
        ) {
            answeredAdditionalQuestions++
        }

        val answerRatio =
            answeredAdditionalQuestions.toDouble() /
                    possibleAdditionalQuestions.toDouble()

        val confidence = when {
            answerRatio >= 0.70 -> ConfidenceLevel.HIGH
            answerRatio >= 0.35 -> ConfidenceLevel.MEDIUM
            else -> ConfidenceLevel.LOW
        }

        val confidenceReason = when (confidence) {

            ConfidenceLevel.HIGH ->
                "You provided several additional financial details, so the estimate can use a narrower range."

            ConfidenceLevel.MEDIUM ->
                "Some important information is missing, so the app keeps the estimates moderately wide."

            ConfidenceLevel.LOW ->
                "You answered mainly the minimum questions. The app widens estimates because important financial details are unknown."
        }


        // ----------------------------------------------------
        // 3. SAFE EMI AFFORDABILITY
        // ----------------------------------------------------

        var affordabilityRatio = when (profile.borrowerType) {

            BorrowerType.SALARIED -> 0.40

            BorrowerType.SELF_EMPLOYED -> 0.35

            BorrowerType.INFORMAL -> 0.30
        }


        // Income stability adjustment

        when (
            profile.incomeStability.lowercase()
        ) {

            "low",
            "low / unpredictable",
            "unpredictable" -> {

                affordabilityRatio -= 0.08
            }

            "moderate" -> {

                affordabilityRatio -= 0.03
            }

            "unknown" -> {

                affordabilityRatio -= 0.04
            }
        }


        // Borrower type adjustments

        when (profile.borrowerType) {

            BorrowerType.SALARIED -> {

                if (
                    profile.employmentYears > 0 &&
                    profile.employmentYears < 1
                ) {

                    affordabilityRatio -= 0.04
                }
            }

            BorrowerType.SELF_EMPLOYED -> {

                if (
                    profile.businessYears > 0 &&
                    profile.businessYears < 2
                ) {

                    affordabilityRatio -= 0.05
                }

                if (
                    profile.businessYears >= 10
                ) {

                    affordabilityRatio += 0.03
                }
            }

            BorrowerType.INFORMAL -> {

                if (
                    profile.monthlyIncomeVariation > 30
                ) {

                    affordabilityRatio -= 0.06
                }

                if (
                    profile.monthlyIncomeVariation < 0
                ) {

                    affordabilityRatio -= 0.04
                }
            }
        }


        // Emergency savings adjustment

        if (
            profile.emergencySavingsMonths >= 0 &&
            profile.emergencySavingsMonths < 1
        ) {

            affordabilityRatio -= 0.05
        }

        if (
            profile.emergencySavingsMonths >= 6
        ) {

            affordabilityRatio += 0.02
        }


        // EMI bounce adjustment

        if (
            profile.hasRecentEmiBounce == true
        ) {

            affordabilityRatio -= 0.10
        }


        // High cost debt adjustment

        if (
            profile.highCostDebt == true
        ) {

            affordabilityRatio -= 0.06
        }


        // Clamp affordability ratio

        affordabilityRatio =
            affordabilityRatio.coerceIn(
                0.10,
                0.45
            )


        val safeEmi =
            max(
                0.0,
                disposableIncome * affordabilityRatio
            )


        // ----------------------------------------------------
        // 4. STRESS TEST
        // ----------------------------------------------------

        val stressedIncome =
            totalIncome * 0.80

        val stressedDisposable =
            max(
                0.0,
                stressedIncome -
                        expenses -
                        existingEmi
            )

        val stressCaseEmi =
            max(
                0.0,
                stressedDisposable *
                        affordabilityRatio
            )


        // ----------------------------------------------------
        // 5. INTEREST RATE BAND
        // ----------------------------------------------------

        var minRate: Double
        var maxRate: Double

        when (profile.loanType) {

            LoanType.HOME -> {

                minRate = 8.0
                maxRate = 10.5
            }

            LoanType.LAP -> {

                minRate = 9.0
                maxRate = 13.0
            }

            LoanType.GOLD -> {

                minRate = 9.0
                maxRate = 18.0
            }

            LoanType.TWO_WHEELER -> {

                minRate = 10.0
                maxRate = 18.0
            }

            LoanType.BUSINESS -> {

                minRate = 11.0
                maxRate = 20.0
            }

            LoanType.PERSONAL -> {

                minRate = 11.0
                maxRate = 22.0
            }
        }


        // Credit score adjustment

        when (profile.creditScore) {

            CreditScoreBand.EXCELLENT -> {

                maxRate -= 3.0
            }

            CreditScoreBand.GOOD -> {

                minRate += 0.5
                maxRate -= 1.5
            }

            CreditScoreBand.FAIR -> {

                minRate += 1.5
            }

            CreditScoreBand.POOR -> {

                minRate += 4.0
                maxRate += 4.0
            }

            CreditScoreBand.UNKNOWN -> {

                minRate -= 1.0
                maxRate += 3.0
            }
        }


        // Income type adjustments

        when (profile.borrowerType) {

            BorrowerType.SELF_EMPLOYED -> {

                maxRate += 1.0
            }

            BorrowerType.INFORMAL -> {

                maxRate += 2.0
            }

            BorrowerType.SALARIED -> {

                // No additional penalty
            }
        }


        // Recent bounce

        if (
            profile.hasRecentEmiBounce == true
        ) {

            minRate += 2.0
            maxRate += 3.0
        }


        // High cost debt

        if (
            profile.highCostDebt == true
        ) {

            maxRate += 2.0
        }


        // Strong business history

        if (
            profile.borrowerType ==
            BorrowerType.SELF_EMPLOYED &&
            profile.businessYears >= 10
        ) {

            maxRate -= 1.0
        }


        // Collateral improves rate estimate

        if (
            profile.hasCollateral == true &&
            profile.collateralValue > 0 &&
            profile.loanType != LoanType.GOLD
        ) {

            minRate -= 1.0
            maxRate -= 2.0
        }


        // Confidence widens range

        when (confidence) {

            ConfidenceLevel.HIGH -> {
                // Keep range tighter
            }

            ConfidenceLevel.MEDIUM -> {

                minRate -= 0.5
                maxRate += 0.5
            }

            ConfidenceLevel.LOW -> {

                minRate -= 1.0
                maxRate += 2.0
            }
        }


        minRate =
            max(5.0, minRate)

        maxRate =
            max(
                minRate + 1.0,
                maxRate
            )


        // ----------------------------------------------------
        // 6. PROCESSING FEE + APR
        // ----------------------------------------------------

        val processingFeePercent =
            when (profile.loanType) {

                LoanType.HOME -> 0.50

                LoanType.LAP -> 1.00

                LoanType.GOLD -> 1.00

                LoanType.TWO_WHEELER -> 1.50

                LoanType.BUSINESS -> 1.50

                LoanType.PERSONAL -> 2.00
            }


        /*
         Approximation:
         Processing fees increase the effective cost.

         We use a transparent simplified APR estimate
         because this app does not know the lender's
         exact amortization schedule or all mandatory charges.
        */

        val minApr =
            minRate +
                    processingFeePercent * 0.8

        val maxApr =
            maxRate +
                    processingFeePercent * 1.2


        // ----------------------------------------------------
        // 7. SAFE LOAN AMOUNT
        // ----------------------------------------------------

        val averageRate =
            (minRate + maxRate) / 2.0

        val safeLoanAmount =
            calculatePrincipalFromEmi(
                emi = safeEmi,
                annualInterestRate =
                    averageRate,
                months =
                    profile.tenureMonths
            )


        // ----------------------------------------------------
        // 8. LIKELY LENDER SANCTION
        // ----------------------------------------------------

        var sanctionMultiplier =
            when (profile.borrowerType) {

                BorrowerType.SALARIED -> 1.20

                BorrowerType.SELF_EMPLOYED -> 1.10

                BorrowerType.INFORMAL -> 1.00
            }


        // Credit score

        when (profile.creditScore) {

            CreditScoreBand.EXCELLENT ->
                sanctionMultiplier += 0.15

            CreditScoreBand.GOOD ->
                sanctionMultiplier += 0.08

            CreditScoreBand.FAIR ->
                sanctionMultiplier -= 0.05

            CreditScoreBand.POOR ->
                sanctionMultiplier -= 0.20

            CreditScoreBand.UNKNOWN ->
                sanctionMultiplier -= 0.05
        }


        // Strong business history

        if (
            profile.businessYears >= 10
        ) {

            sanctionMultiplier += 0.10
        }


        // Collateral

        if (
            profile.hasCollateral == true &&
            profile.collateralValue > 0
        ) {

            sanctionMultiplier += 0.20
        }


        // Recent bounce

        if (
            profile.hasRecentEmiBounce == true
        ) {

            sanctionMultiplier -= 0.25
        }


        sanctionMultiplier =
            sanctionMultiplier.coerceIn(
                0.40,
                1.60
            )


        var likelySanctionAmount =
            safeLoanAmount *
                    sanctionMultiplier


        // Collateral based cap for secured loans

        if (
            profile.hasCollateral == true &&
            profile.collateralValue > 0 &&
            (
                    profile.loanType ==
                            LoanType.LAP ||
                            profile.loanType ==
                            LoanType.BUSINESS
                    )
        ) {

            val collateralCap =
                profile.collateralValue * 0.60

            likelySanctionAmount =
                min(
                    likelySanctionAmount,
                    collateralCap
                )
        }


        likelySanctionAmount =
            max(
                0.0,
                likelySanctionAmount
            )


        // ----------------------------------------------------
        // 9. RECOMMENDED LOAN AMOUNT
        // ----------------------------------------------------

        val recommendedLoanAmount =
            min(
                safeLoanAmount,
                profile.desiredLoanAmount
            )


        // ----------------------------------------------------
        // 10. PRODUCT RECOMMENDATION
        // ----------------------------------------------------

        var recommendedProduct =
            profile.loanType.displayName

        var productReason =
            "The assessment uses the loan type you selected."


        // Ravi-style secured recommendation

        if (
            profile.borrowerType ==
            BorrowerType.SELF_EMPLOYED &&
            profile.hasCollateral == true &&
            profile.collateralValue > 0 &&
            profile.loanPurpose ==
            LoanPurpose.BUSINESS_EXPANSION
        ) {

            recommendedProduct =
                "Secured Business Loan or Loan Against Property"

            productReason =
                "You have collateral and a business purpose. A secured product may offer a lower rate than an unsecured business loan."
        }


        // Anita-style productive asset

        if (
            profile.loanPurpose ==
            LoanPurpose.PRODUCTIVE_ASSET &&
            profile.expectedAdditionalMonthlyIncome > 0
        ) {

            productReason =
                "The loan may create additional income. The assessment still uses your current income for affordability and treats future income as uncertain."
        }


        // ----------------------------------------------------
        // 11. BORROW DECISION
        // ----------------------------------------------------

        val reasons =
            mutableListOf<String>()

        var decision: BorrowDecision

        var title: String

        var decisionReason: String


        when {

            // No affordability

            income <= 0 ||
                    disposableIncome <= 0 ||
                    safeEmi <= 0 -> {

                decision =
                    BorrowDecision.DONT_BORROW

                title =
                    "Don't borrow right now"

                decisionReason =
                    "Your current income and essential expenses leave little or no safe room for another EMI."

                reasons +=
                    "Disposable income after expenses and existing EMIs is too low."
            }


            // Very high debt burden

            debtRatio >= 0.50 -> {

                decision =
                    BorrowDecision.DONT_BORROW

                title =
                    "Don't borrow right now"

                decisionReason =
                    "A very large share of your income is already committed to existing EMIs."

                reasons +=
                    "Existing EMIs are 50% or more of your monthly income."
            }


            // Recent repayment distress + high cost debt

            profile.hasRecentEmiBounce == true &&
                    profile.highCostDebt == true -> {

                decision =
                    BorrowDecision.DONT_BORROW

                title =
                    "Stabilise existing debt first"

                decisionReason =
                    "A recent EMI bounce and expensive existing debt indicate current repayment stress."

                reasons +=
                    "Recent repayment difficulty is a strong warning signal."

                reasons +=
                    "High-cost existing debt should be addressed before adding another loan."
            }


            // Requested amount too high

            profile.desiredLoanAmount >
                    safeLoanAmount * 1.20 -> {

                decision =
                    BorrowDecision.BORROW_LESS

                title =
                    "Borrow less"

                decisionReason =
                    "The amount you requested is significantly above the amount your current cash flow can safely support."

                reasons +=
                    "Requested amount is above the estimated safe borrowing amount."
            }


            // Moderate debt burden

            debtRatio >= 0.35 -> {

                decision =
                    BorrowDecision.BORROW_LESS

                title =
                    "Borrow cautiously"

                decisionReason =
                    "Your existing debt already uses a significant share of monthly income."

                reasons +=
                    "Existing EMI burden requires a conservative new borrowing amount."
            }


            // Low income stability

            profile.incomeStability
                .lowercase()
                .contains("low") ||

                    profile.hasRecentEmiBounce == true -> {

                decision =
                    BorrowDecision.BORROW_LESS

                title =
                    "Borrow cautiously"

                decisionReason =
                    "Income uncertainty or repayment risk means you should maintain a larger financial buffer."

                reasons +=
                    "Income stability or repayment history requires a conservative approach."
            }


            else -> {

                decision =
                    BorrowDecision.BORROW

                title =
                    "Borrowing appears manageable"

                decisionReason =
                    "Based on the information provided, the estimated EMI fits within a cautious affordability range."

                reasons +=
                    "Your income, expenses and existing EMIs leave room for a cautious new EMI."
            }
        }


        // ----------------------------------------------------
        // 12. EXPLAINABILITY
        // ----------------------------------------------------

        reasons +=
            "Your monthly income considered for this assessment is ₹${totalIncome.toInt()}."

        reasons +=
            "After household expenses of ₹${expenses.toInt()} and existing EMIs of ₹${existingEmi.toInt()}, estimated disposable income is ₹${disposableIncome.toInt()}."

        reasons +=
            "The app applies a cautious affordability ratio of ${(affordabilityRatio * 100).toInt()}% to your disposable income."

        reasons +=
            "Your recommended EMI ceiling is ₹${safeEmi.toInt()} per month."

        reasons +=
            "Estimated fair interest range: ${
                String.format("%.1f", minRate)
            }% to ${
                String.format("%.1f", maxRate)
            }%."


        val safeAmountWhy =
            "Your estimated safe loan amount is based on a monthly EMI ceiling of ₹${safeEmi.toInt()}, your selected tenure, and the middle of your estimated interest-rate range."


        val sanctionAmountWhy =
            "The likely lender sanction is estimated separately from your safe amount using borrower type, credit profile, repayment history and collateral information."


        val interestRateWhy =
            "Your interest range depends on loan type, borrower income type, credit information, repayment history and the amount of information you provided."


        val emiWhy =
            "The EMI ceiling of ₹${safeEmi.toInt()} is calculated from your disposable income of ₹${disposableIncome.toInt()} using a cautious affordability buffer."


        val stressCaseExplanation =
            "Stress case: if total monthly income falls by 20%, the estimated safe EMI falls to approximately ₹${stressCaseEmi.toInt()}."


        // ----------------------------------------------------
        // 13. TENURE OPTIONS
        // ----------------------------------------------------

        val tenureCandidates =
            listOf(
                36,
                60,
                84
            )

        val tenureOptions =
            tenureCandidates
                .filter {

                    it >= 12 &&
                            it <= 360
                }
                .map { months ->

                    val loanForComparison =
                        if (
                            recommendedLoanAmount > 0
                        ) {
                            recommendedLoanAmount
                        } else {
                            safeLoanAmount
                        }

                    val emi =
                        calculateEmi(
                            principal =
                                loanForComparison,
                            annualInterestRate =
                                averageRate,
                            months =
                                months
                        )

                    val totalPaid =
                        emi * months

                    val totalInterest =
                        max(
                            0.0,
                            totalPaid -
                                    loanForComparison
                        )

                    TenureOption(
                        months = months,
                        emi = emi,
                        totalInterest =
                            totalInterest
                    )
                }


        // ----------------------------------------------------
        // 14. NEGOTIATION TIPS
        // ----------------------------------------------------

        val negotiationTips =
            mutableListOf<String>()


        negotiationTips +=
            "Ask the lender for the complete APR and total cost, not only the headline interest rate."

        negotiationTips +=
            "Ask for all processing, insurance and mandatory charges in writing."

        negotiationTips +=
            "Compare at least two lenders before accepting an offer."

        negotiationTips +=
            "Do not agree to an EMI above ₹${safeEmi.toInt()} per month based on this assessment."


        if (
            profile.lenderOfferedRate > 0
        ) {

            when {

                profile.lenderOfferedRate >
                        maxRate -> {

                    negotiationTips +=
                        "The lender's offered rate of ${String.format("%.1f", profile.lenderOfferedRate)}% is above your estimated fair range. Ask the lender to explain the risk premium and charges."
                }

                profile.lenderOfferedRate <
                        minRate -> {

                    negotiationTips +=
                        "The offered rate is below the estimated range. Check whether mandatory fees or bundled products increase the actual APR."
                }

                else -> {

                    negotiationTips +=
                        "The offered rate falls inside your estimated fair range. Still compare the APR and total charges."
                }
            }
        }


        if (
            profile.desiredLoanAmount >
            safeLoanAmount
        ) {

            negotiationTips +=
                "Try to reduce the requested amount toward ₹${safeLoanAmount.toInt()}, which is your estimated safe borrowing amount."
        }


        if (
            profile.creditScore ==
            CreditScoreBand.UNKNOWN
        ) {

            negotiationTips +=
                "Your credit score is unknown, so the rate estimate is wider. Check your credit report before negotiating."
        }


        // ----------------------------------------------------
        // 15. TRANSPARENCY / ASSUMPTIONS
        // ----------------------------------------------------

        val assumptions =
            mutableListOf<String>()

        assumptions +=
            "This is a self-assessment tool, not a lender credit decision."

        assumptions +=
            "Interest-rate bands are indicative assumptions and actual lender pricing may differ."

        assumptions +=
            "APR is estimated using the interest range and an assumed processing fee. Exact APR requires the lender's complete fee and repayment schedule."

        assumptions +=
            "Future income from a productive asset is not counted as guaranteed affordability."

        assumptions +=
            "Unknown information widens estimates instead of being treated as zero."


        // ----------------------------------------------------
        // RETURN RESULT
        // ----------------------------------------------------

        return AssessmentResult(

            decision = decision,

            decisionTitle = title,

            decisionReason =
                decisionReason,


            safeLoanAmount =
                safeLoanAmount,

            likelySanctionAmount =
                likelySanctionAmount,

            recommendedLoanAmount =
                recommendedLoanAmount,


            safeAmountWhy =
                safeAmountWhy,

            sanctionAmountWhy =
                sanctionAmountWhy,


            minInterestRate =
                minRate,

            maxInterestRate =
                maxRate,


            processingFeePercent =
                processingFeePercent,


            minEstimatedApr =
                minApr,

            maxEstimatedApr =
                maxApr,


            interestRateWhy =
                interestRateWhy,


            safeEmi =
                safeEmi,

            stressCaseEmi =
                stressCaseEmi,


            monthlyDisposableIncome =
                disposableIncome,


            recommendedTenure =
                profile.tenureMonths,


            tenureOptions =
                tenureOptions,


            emiWhy =
                emiWhy,

            stressCaseExplanation =
                stressCaseExplanation,


            recommendedProduct =
                recommendedProduct,

            productReason =
                productReason,


            confidence =
                confidence,

            confidenceReason =
                confidenceReason,


            reasons =
                reasons,


            negotiationTips =
                negotiationTips,


            assumptions =
                assumptions
        )
    }


    // ========================================================
    // EMI CALCULATOR
    // ========================================================

    fun calculateEmi(

        principal: Double,

        annualInterestRate: Double,

        months: Int

    ): Double {

        if (
            principal <= 0.0 ||
            months <= 0
        ) {

            return 0.0
        }


        val monthlyRate =
            annualInterestRate /
                    12.0 /
                    100.0


        if (
            monthlyRate == 0.0
        ) {

            return principal /
                    months
        }


        val factor =
            (1.0 + monthlyRate)
                .pow(
                    months.toDouble()
                )


        return principal *
                (
                        monthlyRate *
                                factor
                        ) /
                (
                        factor -
                                1.0
                        )
    }


    // ========================================================
    // LOAN AMOUNT FROM EMI
    // ========================================================

    private fun calculatePrincipalFromEmi(

        emi: Double,

        annualInterestRate: Double,

        months: Int

    ): Double {

        if (
            emi <= 0.0 ||
            months <= 0
        ) {

            return 0.0
        }


        val monthlyRate =
            annualInterestRate /
                    12.0 /
                    100.0


        if (
            monthlyRate == 0.0
        ) {

            return emi *
                    months
        }


        val factor =
            (
                    1.0 +
                            monthlyRate
                    ).pow(
                    months.toDouble()
                )


        return emi *
                (
                        factor -
                                1.0
                        ) /
                (
                        monthlyRate *
                                factor
                        )
    }
}