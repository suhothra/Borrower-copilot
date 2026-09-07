@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.borrowercopilot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@OptIn(ExperimentalMaterial3Api::class)

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                BorrowerCopilotApp()
            }
        }
    }
}

@Composable
fun BorrowerCopilotApp() {

    var screen by remember { mutableStateOf("home") }
    var profile by remember { mutableStateOf<BorrowerProfile?>(null) }
    var result by remember { mutableStateOf<AssessmentResult?>(null) }

    when (screen) {

        "home" -> HomeScreen(
            onStart = {
                screen = "assessment"
            },
            onExamples = {
                screen = "examples"
            }
        )

        "assessment" -> AssessmentScreen(
            onBack = {
                screen = "home"
            },
            onComplete = { newProfile ->
                profile = newProfile
                result = BorrowerRules.assess(newProfile)
                screen = "results"
            }
        )

        "examples" -> ExamplesScreen(
            onBack = {
                screen = "home"
            },
            onSelect = { exampleProfile ->
                profile = exampleProfile
                result = BorrowerRules.assess(exampleProfile)
                screen = "results"
            }
        )

        "results" -> {
            val currentProfile = profile
            val currentResult = result

            if (currentProfile != null && currentResult != null) {
                ResultsScreen(
                    profile = currentProfile,
                    result = currentResult,
                    onBack = {
                        screen = "assessment"
                    },
                    onHome = {
                        screen = "home"
                    },
                    onCard = {
                        screen = "card"
                    }
                )
            }
        }

        "card" -> {
            val currentProfile = profile
            val currentResult = result

            if (currentProfile != null && currentResult != null) {
                NegotiationCardScreen(
                    profile = currentProfile,
                    result = currentResult,
                    onBack = {
                        screen = "results"
                    }
                )
            }
        }
    }
}


/* =========================================================
   HOME
   ========================================================= */

@Composable
fun HomeScreen(
    onStart: () -> Unit,
    onExamples: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Borrower Copilot",
                        fontWeight = FontWeight.Bold
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Know your borrowing power before meeting a lender.",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "This self-assessment helps you understand:"
            )

            InfoCard(
                "O1",
                "Should you borrow, borrow less, or not borrow?"
            )

            InfoCard(
                "O2",
                "How much can you safely carry versus what a lender may sanction?"
            )

            InfoCard(
                "O3",
                "What is a fair interest-rate range and estimated APR?"
            )

            InfoCard(
                "O4",
                "What EMI ceiling should you avoid crossing?"
            )

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Assessment")
            }

            OutlinedButton(
                onClick = onExamples,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Try Challenge Examples")
            }

            Text(
                "No login. No bureau pull. No personal data stored.",
                fontSize = 13.sp
            )
        }
    }
}


@Composable
fun InfoCard(
    title: String,
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                title,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )

            Spacer(Modifier.width(16.dp))

            Text(text)
        }
    }
}


/* =========================================================
   ASSESSMENT
   ========================================================= */

@Composable
fun AssessmentScreen(
    onBack: () -> Unit,
    onComplete: (BorrowerProfile) -> Unit
) {

    var borrowerType by remember {
        mutableStateOf(BorrowerType.SALARIED)
    }

    var loanType by remember {
        mutableStateOf(LoanType.PERSONAL)
    }

    var loanPurpose by remember {
        mutableStateOf(LoanPurpose.OTHER)
    }

    var age by remember {
        mutableStateOf("")
    }

    var monthlyIncome by remember {
        mutableStateOf("")
    }

    var householdExpenses by remember {
        mutableStateOf("")
    }

    var existingEmi by remember {
        mutableStateOf("")
    }

    var desiredLoanAmount by remember {
        mutableStateOf("")
    }

    var tenureMonths by remember {
        mutableStateOf("36")
    }

    var creditScore by remember {
        mutableStateOf(CreditScoreBand.UNKNOWN)
    }

    var incomeStability by remember {
        mutableStateOf("Unknown")
    }

    var employmentYears by remember {
        mutableStateOf("")
    }

    var businessYears by remember {
        mutableStateOf("")
    }

    var incomeVariation by remember {
        mutableStateOf("")
    }

    var emergencySavings by remember {
        mutableStateOf("")
    }

    var recentEmiBounce by remember {
        mutableStateOf<Boolean?>(null)
    }

    var itrAnnualIncome by remember {
        mutableStateOf("")
    }

    var collateralValue by remember {
        mutableStateOf("")
    }

    var hasCollateral by remember {
        mutableStateOf<Boolean?>(null)
    }

    var coApplicantIncome by remember {
        mutableStateOf("")
    }

    var outstandingDebt by remember {
        mutableStateOf("")
    }

    var highCostDebt by remember {
        mutableStateOf<Boolean?>(null)
    }

    var expectedAdditionalIncome by remember {
        mutableStateOf("")
    }

    var lenderOfferedRate by remember {
        mutableStateOf("")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Borrower Assessment")
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                "Must Questions",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "These answers are enough to produce all four outputs."
            )

            SectionTitle("1. Income Type")

            EnumSelector(
                items = BorrowerType.values().toList(),
                selected = borrowerType,
                label = { it.displayName },
                onSelected = { borrowerType = it }
            )

            SectionTitle("2. Loan Type")

            EnumSelector(
                items = LoanType.values().toList(),
                selected = loanType,
                label = { it.displayName },
                onSelected = { loanType = it }
            )

            SectionTitle("3. Loan Purpose")

            EnumSelector(
                items = LoanPurpose.values().toList(),
                selected = loanPurpose,
                label = { it.displayName },
                onSelected = { loanPurpose = it }
            )

            NumberInput(
                "4. Age",
                age,
                { age = it }
            )

            MoneyInput(
                "5. Net Monthly Income",
                monthlyIncome,
                { monthlyIncome = it }
            )

            MoneyInput(
                "6. Household Expenses Per Month",
                householdExpenses,
                { householdExpenses = it }
            )

            MoneyInput(
                "7. Existing EMIs Per Month",
                existingEmi,
                { existingEmi = it }
            )

            MoneyInput(
                "8. Amount You Want to Borrow",
                desiredLoanAmount,
                { desiredLoanAmount = it }
            )

            NumberInput(
                "9. Desired Tenure in Months",
                tenureMonths,
                { tenureMonths = it }
            )

            SectionTitle("10. Credit Score")

            EnumSelector(
                items = CreditScoreBand.values().toList(),
                selected = creditScore,
                label = { it.displayName },
                onSelected = { creditScore = it }
            )

            SectionTitle("Income Stability")

            StringSelector(
                options = listOf(
                    "High",
                    "Moderate",
                    "Low / Unpredictable",
                    "Unknown"
                ),
                selected = incomeStability,
                onSelected = {
                    incomeStability = it
                }
            )

            Divider()

            Text(
                "Additional Questions",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "Each additional answer can improve confidence or change your result."
            )

            when (borrowerType) {

                BorrowerType.SALARIED -> {
                    NumberInput(
                        "Years in Employment",
                        employmentYears,
                        { employmentYears = it }
                    )
                }

                BorrowerType.SELF_EMPLOYED -> {
                    NumberInput(
                        "Years Running Business",
                        businessYears,
                        { businessYears = it }
                    )

                    MoneyInput(
                        "Annual Income Shown in ITR",
                        itrAnnualIncome,
                        { itrAnnualIncome = it },
                        "Leave blank if unknown"
                    )
                }

                BorrowerType.INFORMAL -> {
                    NumberInput(
                        "Monthly Income Variation (%)",
                        incomeVariation,
                        { incomeVariation = it },
                        "Leave blank if unknown"
                    )
                }
            }

            NumberInput(
                "Emergency Savings Available (Months)",
                emergencySavings,
                { emergencySavings = it },
                "Leave blank if unknown"
            )

            TriStateQuestion(
                "Has any EMI bounced recently?",
                recentEmiBounce
            ) {
                recentEmiBounce = it
            }

            TriStateQuestion(
                "Do you have collateral available?",
                hasCollateral
            ) {
                hasCollateral = it
            }

            if (hasCollateral == true) {
                MoneyInput(
                    "Estimated Collateral Value",
                    collateralValue,
                    { collateralValue = it }
                )
            }

            MoneyInput(
                "Co-applicant Monthly Income",
                coApplicantIncome,
                { coApplicantIncome = it },
                "Leave blank if none"
            )

            MoneyInput(
                "Total Outstanding Debt",
                outstandingDebt,
                { outstandingDebt = it },
                "Leave blank if unknown"
            )

            TriStateQuestion(
                "Do you currently have high-cost debt?",
                highCostDebt
            ) {
                highCostDebt = it
            }

            if (
                loanPurpose == LoanPurpose.PRODUCTIVE_ASSET ||
                loanPurpose == LoanPurpose.BUSINESS_EXPANSION
            ) {
                MoneyInput(
                    "Expected Additional Monthly Income from Loan",
                    expectedAdditionalIncome,
                    { expectedAdditionalIncome = it },
                    "Leave blank if unknown"
                )
            }

            NumberInput(
                "Lender Offered Interest Rate (%)",
                lenderOfferedRate,
                { lenderOfferedRate = it },
                "Leave blank if no offer yet"
            )

            Spacer(Modifier.height(10.dp))

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    val profile = BorrowerProfile(
                        borrowerType = borrowerType,
                        loanType = loanType,
                        loanPurpose = loanPurpose,

                        age = age.toIntOrNull() ?: 30,

                        monthlyIncome =
                            monthlyIncome.toDoubleOrNull() ?: 0.0,

                        householdExpenses =
                            householdExpenses.toDoubleOrNull() ?: 0.0,

                        existingEmi =
                            existingEmi.toDoubleOrNull() ?: 0.0,

                        desiredLoanAmount =
                            desiredLoanAmount.toDoubleOrNull() ?: 0.0,

                        tenureMonths =
                            tenureMonths.toIntOrNull() ?: 36,

                        creditScore = creditScore,

                        incomeStability = incomeStability,

                        employmentYears =
                            employmentYears.toDoubleOrNull() ?: 0.0,

                        businessYears =
                            businessYears.toDoubleOrNull() ?: 0.0,

                        monthlyIncomeVariation =
                            incomeVariation.toDoubleOrNull() ?: -1.0,

                        emergencySavingsMonths =
                            emergencySavings.toDoubleOrNull() ?: -1.0,

                        hasRecentEmiBounce =
                            recentEmiBounce,

                        itrAnnualIncome =
                            itrAnnualIncome.toDoubleOrNull() ?: -1.0,

                        collateralValue =
                            collateralValue.toDoubleOrNull() ?: -1.0,

                        hasCollateral =
                            hasCollateral,

                        coApplicantMonthlyIncome =
                            coApplicantIncome.toDoubleOrNull() ?: -1.0,

                        outstandingDebt =
                            outstandingDebt.toDoubleOrNull() ?: -1.0,

                        highCostDebt =
                            highCostDebt,

                        expectedAdditionalMonthlyIncome =
                            expectedAdditionalIncome.toDoubleOrNull() ?: -1.0,

                        lenderOfferedRate =
                            lenderOfferedRate.toDoubleOrNull() ?: -1.0
                    )

                    onComplete(profile)
                }
            ) {
                Text(
                    "Get My Borrowing Assessment",
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}


/* =========================================================
   RESULTS
   ========================================================= */

@Composable
fun ResultsScreen(
    profile: BorrowerProfile,
    result: AssessmentResult,
    onBack: () -> Unit,
    onHome: () -> Unit,
    onCard: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Your Assessment")
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ResultCard("O1 · Should you borrow?") {

                Text(
                    decisionText(result.decision),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    result.decisionTitle,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(result.decisionReason)
            }

            ResultCard("O2 · How much should you borrow?") {

                AmountRow(
                    "Safe amount",
                    result.safeLoanAmount
                )

                Text(
                    result.safeAmountWhy,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(10.dp))

                AmountRow(
                    "Likely lender sanction",
                    result.likelySanctionAmount
                )

                Text(
                    result.sanctionAmountWhy,
                    fontSize = 13.sp
                )

                Divider()

                AmountRow(
                    "Recommended amount",
                    result.recommendedLoanAmount
                )
            }

            ResultCard("O3 · Fair Interest Rate") {

                Text(
                    "${formatRate(result.minInterestRate)} – " +
                            formatRate(result.maxInterestRate),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Estimated all-in APR: " +
                            "${formatRate(result.minEstimatedApr)} – " +
                            formatRate(result.maxEstimatedApr)
                )

                Text(
                    "Assumed processing fee: " +
                            formatRate(result.processingFeePercent)
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    result.interestRateWhy,
                    fontSize = 13.sp
                )
            }

            ResultCard("O4 · EMI Ceiling") {

                AmountRow(
                    "Safe EMI ceiling",
                    result.safeEmi
                )

                AmountRow(
                    "Stress-case EMI",
                    result.stressCaseEmi
                )

                AmountRow(
                    "Monthly disposable income",
                    result.monthlyDisposableIncome
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    result.emiWhy,
                    fontSize = 13.sp
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    result.stressCaseExplanation,
                    fontWeight = FontWeight.Bold
                )
            }

            ResultCard("Tenure Trade-Off") {

                result.tenureOptions.forEach { option ->

                    Text(
                        "${option.months} months"
                    )

                    Text(
                        "EMI: ${formatRupees(option.emi)}"
                    )

                    Text(
                        "Total interest: " +
                                formatRupees(option.totalInterest)
                    )

                    Divider(
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }

            ResultCard("Recommended Product") {

                Text(
                    result.recommendedProduct,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(8.dp))

                Text(result.productReason)
            }

            ResultCard("Confidence") {

                Text(
                    result.confidence.displayName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(result.confidenceReason)
            }

            ResultCard("Why did we reach this result?") {

                result.reasons.forEach { reason ->
                    Text("• $reason")
                    Spacer(Modifier.height(6.dp))
                }
            }

            ResultCard("Negotiation Tips") {

                result.negotiationTips.forEach { tip ->
                    Text("• $tip")
                    Spacer(Modifier.height(6.dp))
                }
            }

            ResultCard("Assumptions & Limits") {

                result.assumptions.forEach { assumption ->
                    Text("• $assumption")
                    Spacer(Modifier.height(6.dp))
                }
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onCard
            ) {
                Text("Open Negotiation Card")
            }

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = onHome
            ) {
                Text("Start New Assessment")
            }

            Spacer(Modifier.height(30.dp))
        }
    }
}


/* =========================================================
   NEGOTIATION CARD
   ========================================================= */

@Composable
fun NegotiationCardScreen(
    profile: BorrowerProfile,
    result: AssessmentResult,
    onBack: () -> Unit
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Negotiation Card")
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "My Borrower Negotiation Card",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            ResultCard("My Request") {

                Text(
                    "Requested amount: " +
                            formatRupees(profile.desiredLoanAmount)
                )

                Text(
                    "Loan: ${profile.loanType.displayName}"
                )

                Text(
                    "Purpose: ${profile.loanPurpose.displayName}"
                )
            }

            ResultCard("What I Should Use") {

                Text(
                    formatRupees(result.recommendedLoanAmount),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Recommended borrowing amount"
                )

                Spacer(Modifier.height(8.dp))

                Text(
                    "Safe EMI ceiling: " +
                            "${formatRupees(result.safeEmi)} / month"
                )
            }

            ResultCard("Fair Rate for My Profile") {

                Text(
                    "${formatRate(result.minInterestRate)} – " +
                            formatRate(result.maxInterestRate),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    "Estimated APR: " +
                            "${formatRate(result.minEstimatedApr)} – " +
                            formatRate(result.maxEstimatedApr)
                )
            }

            if (profile.lenderOfferedRate > 0) {

                ResultCard("Lender Offer") {

                    Text(
                        "Offered rate: " +
                                formatRate(profile.lenderOfferedRate)
                    )

                    if (
                        profile.lenderOfferedRate >
                        result.maxInterestRate
                    ) {
                        Text(
                            "This is above the estimated fair range.",
                            fontWeight = FontWeight.Bold
                        )
                    } else {
                        Text(
                            "Compare the full APR and charges before accepting."
                        )
                    }
                }
            }

            ResultCard("Why?") {

                result.reasons.take(4).forEach { reason ->
                    Text("• $reason")
                    Spacer(Modifier.height(6.dp))
                }
            }

            ResultCard("What I Will Ask the Lender") {

                Text(
                    "• What is the complete APR?"
                )

                Text(
                    "• What are all processing and mandatory charges?"
                )

                Text(
                    "• What are the prepayment and foreclosure charges?"
                )

                Text(
                    "• Can you explain why my quoted rate differs from my profile range?"
                )
            }

            Text(
                "This is a self-assessment tool, not a lender approval or guarantee.",
                fontSize = 12.sp
            )

            Spacer(Modifier.height(30.dp))
        }
    }
}


/* =========================================================
   EXAMPLES
   ========================================================= */

@Composable
fun ExamplesScreen(
    onBack: () -> Unit,
    onSelect: (BorrowerProfile) -> Unit
) {

    val priya = BorrowerProfile(
        borrowerType = BorrowerType.SALARIED,
        loanType = LoanType.PERSONAL,
        loanPurpose = LoanPurpose.WEDDING,

        age = 29,

        monthlyIncome = 110000.0,
        householdExpenses = 28000.0,
        existingEmi = 14000.0,

        desiredLoanAmount = 800000.0,
        tenureMonths = 36,

        creditScore = CreditScoreBand.EXCELLENT,

        incomeStability = "High",
        employmentYears = 5.0,

        emergencySavingsMonths = 3.0,

        hasRecentEmiBounce = false,

        outstandingDebt = -1.0,
        highCostDebt = false
    )

    val ravi = BorrowerProfile(
        borrowerType = BorrowerType.SELF_EMPLOYED,
        loanType = LoanType.BUSINESS,
        loanPurpose = LoanPurpose.BUSINESS_EXPANSION,

        age = 42,

        monthlyIncome = 60000.0,
        householdExpenses = 25000.0,
        existingEmi = 0.0,

        desiredLoanAmount = 1500000.0,
        tenureMonths = 60,

        creditScore = CreditScoreBand.UNKNOWN,

        incomeStability = "Moderate",

        businessYears = 14.0,

        itrAnnualIncome = 420000.0,

        emergencySavingsMonths = -1.0,

        hasRecentEmiBounce = false,

        collateralValue = 4500000.0,
        hasCollateral = true,

        coApplicantMonthlyIncome = 18000.0,

        highCostDebt = false,

        expectedAdditionalMonthlyIncome = 15000.0
    )

    val anita = BorrowerProfile(
        borrowerType = BorrowerType.INFORMAL,
        loanType = LoanType.TWO_WHEELER,
        loanPurpose = LoanPurpose.PRODUCTIVE_ASSET,

        age = 35,

        monthlyIncome = 28000.0,
        householdExpenses = 20000.0,
        existingEmi = 5000.0,

        desiredLoanAmount = 150000.0,
        tenureMonths = 36,

        creditScore = CreditScoreBand.UNKNOWN,

        incomeStability = "Low / Unpredictable",

        monthlyIncomeVariation = 30.0,

        emergencySavingsMonths = 0.0,

        hasRecentEmiBounce = true,

        outstandingDebt = 35000.0,

        highCostDebt = true,

        expectedAdditionalMonthlyIncome = 10000.0
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Challenge Examples")
                },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                "Run the three borrowers from the challenge.",
                fontSize = 18.sp
            )

            ExampleCard(
                name = "Priya",
                description =
                    "Salaried software engineer · ₹1.1L income · excellent credit · wedding loan"
            ) {
                onSelect(priya)
            }

            ExampleCard(
                name = "Ravi",
                description =
                    "Kirana owner · unknown credit score · property collateral · business loan"
            ) {
                onSelect(ravi)
            }

            ExampleCard(
                name = "Anita",
                description =
                    "Informal income · repayment stress · high-cost debt · productive asset loan"
            ) {
                onSelect(anita)
            }
        }
    }
}


/* =========================================================
   INPUT COMPONENTS
   ========================================================= */

@Composable
fun SectionTitle(
    text: String
) {
    Text(
        text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    )
}


@Composable
fun NumberInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = ""
) {

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(
                it.filter { character ->
                    character.isDigit() ||
                            character == '.'
                }
            )
        },
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        placeholder = {
            Text(placeholder)
        },
        singleLine = true
    )
}


@Composable
fun MoneyInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = ""
) {

    OutlinedTextField(
        value = value,
        onValueChange = {
            onValueChange(
                it.filter { character ->
                    character.isDigit() ||
                            character == '.'
                }
            )
        },
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(label)
        },
        placeholder = {
            Text(placeholder)
        },
        prefix = {
            Text("₹ ")
        },
        singleLine = true
    )
}


@Composable
fun <T> EnumSelector(
    items: List<T>,
    selected: T,
    label: (T) -> String,
    onSelected: (T) -> Unit
) {

    Column(
        modifier = Modifier.selectableGroup()
    ) {

        items.forEach { item ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected == item,
                        onClick = {
                            onSelected(item)
                        }
                    )
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selected == item,
                    onClick = null
                )

                Spacer(Modifier.width(8.dp))

                Text(label(item))
            }
        }
    }
}


@Composable
fun StringSelector(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {

    Column(
        modifier = Modifier.selectableGroup()
    ) {

        options.forEach { option ->

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = selected == option,
                        onClick = {
                            onSelected(option)
                        }
                    )
                    .padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                RadioButton(
                    selected = selected == option,
                    onClick = null
                )

                Spacer(Modifier.width(8.dp))

                Text(option)
            }
        }
    }
}


@Composable
fun TriStateQuestion(
    question: String,
    value: Boolean?,
    onChange: (Boolean?) -> Unit
) {

    Column {

        Text(
            question,
            fontWeight = FontWeight.Medium
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            RadioButton(
                selected = value == true,
                onClick = {
                    onChange(true)
                }
            )

            Text("Yes")

            Spacer(Modifier.width(12.dp))

            RadioButton(
                selected = value == false,
                onClick = {
                    onChange(false)
                }
            )

            Text("No")

            Spacer(Modifier.width(12.dp))

            RadioButton(
                selected = value == null,
                onClick = {
                    onChange(null)
                }
            )

            Text("I don't know")
        }
    }
}


/* =========================================================
   RESULT COMPONENTS
   ========================================================= */

@Composable
fun ResultCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))

            content()
        }
    }
}


@Composable
fun AmountRow(
    label: String,
    amount: Double
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(label)

        Text(
            formatRupees(amount),
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(Modifier.height(6.dp))
}


@Composable
fun ExampleCard(
    name: String,
    description: String,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Text(
                name,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(6.dp))

            Text(description)

            Spacer(Modifier.height(8.dp))

            Text(
                "Tap to run assessment →",
                fontWeight = FontWeight.Bold
            )
        }
    }
}


/* =========================================================
   HELPERS
   ========================================================= */

fun decisionText(
    decision: BorrowDecision
): String {

    return when (decision) {

        BorrowDecision.BORROW ->
            "BORROW"

        BorrowDecision.BORROW_LESS ->
            "BORROW LESS"

        BorrowDecision.DONT_BORROW ->
            "DON'T BORROW"
    }
}


fun formatRupees(
    amount: Double
): String {

    return "₹${"%,.0f".format(amount)}"
}


fun formatRate(
    rate: Double
): String {

    return "${"%.1f".format(rate)}%"
}