package com.ridemitra.app.ui.screens.driver.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverEarningsSummary
import com.ridemitra.app.domain.model.DriverEarningsTransaction
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.DividerLight
import com.ridemitra.app.ui.theme.ErrorRed
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary
import com.ridemitra.app.ui.theme.WarningAmber

enum class EarningsTimeframe(val label: String) {
    TODAY("Today"),
    THIS_WEEK("This Week"),
    THIS_MONTH("This Month")
}

@Composable
fun DriverEarningsScreen(
    earningsSummary: DriverEarningsSummary,
    onDownloadStatement: () -> Unit,
    onRequestInstantPayout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableStateOf(EarningsTimeframe.TODAY) }
    var showDemoSettledTrips by remember { mutableStateOf(false) }

    // Demo settled transactions when driver wants to inspect historical audited statement
    val sampleHistoricalTransactions = remember {
        listOf(
            DriverEarningsTransaction(
                tripId = "TRIP-BLR-8921",
                dateFormatted = "Today, 03:45 PM",
                routeSummary = "BLR Airport T1 -> Whitefield ITPL",
                totalPassengerFare = 890.0,
                platformCommission = 89.0, // 10% standard platform take
                netDriverPayout = 801.0,
                paymentMode = "NEFT direct to HDFC A/C **4920"
            ),
            DriverEarningsTransaction(
                tripId = "TRIP-BLR-8910",
                dateFormatted = "Today, 11:15 AM",
                routeSummary = "BLR Airport T2 -> Koramangala 4th Block",
                totalPassengerFare = 640.0,
                platformCommission = 64.0,
                netDriverPayout = 576.0,
                paymentMode = "Instant IMPS to HDFC A/C **4920"
            ),
            DriverEarningsTransaction(
                tripId = "TRIP-BLR-8804",
                dateFormatted = "Yesterday, 08:20 PM",
                routeSummary = "BLR Airport T1 -> Electronic City Phase 1",
                totalPassengerFare = 1120.0,
                platformCommission = 112.0,
                netDriverPayout = 1008.0,
                paymentMode = "NEFT direct to HDFC A/C **4920"
            )
        )
    }

    val activeTransactions = if (showDemoSettledTrips) {
        sampleHistoricalTransactions
    } else {
        earningsSummary.recentTransactions
    }

    val displayedNetEarnings = when (selectedTimeframe) {
        EarningsTimeframe.TODAY -> if (showDemoSettledTrips) 1377.0 else earningsSummary.todayNetEarnings
        EarningsTimeframe.THIS_WEEK -> if (showDemoSettledTrips) 8450.0 else earningsSummary.weeklyNetEarnings
        EarningsTimeframe.THIS_MONTH -> if (showDemoSettledTrips) 34200.0 else earningsSummary.monthlyNetEarnings
    }

    val displayedCompletedTrips = when (selectedTimeframe) {
        EarningsTimeframe.TODAY -> if (showDemoSettledTrips) 2 else earningsSummary.todayCompletedTrips
        EarningsTimeframe.THIS_WEEK -> if (showDemoSettledTrips) 12 else (earningsSummary.todayCompletedTrips + 3)
        EarningsTimeframe.THIS_MONTH -> if (showDemoSettledTrips) 48 else earningsSummary.totalCompletedTrips
    }

    val displayedCommission = when (selectedTimeframe) {
        EarningsTimeframe.TODAY -> if (showDemoSettledTrips) 153.0 else earningsSummary.platformCommissionDeductedToday
        EarningsTimeframe.THIS_WEEK -> if (showDemoSettledTrips) 938.0 else (earningsSummary.platformCommissionDeductedToday * 3)
        EarningsTimeframe.THIS_MONTH -> if (showDemoSettledTrips) 3800.0 else 0.0
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column {
                Text(
                    text = "Driver Earnings",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                )
                Text(
                    text = "Transparent payouts, net settlements & zero hidden deductions",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary)
                )
            }
        }

        // Timeframe selector filter chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EarningsTimeframe.values().forEach { timeframe ->
                    FilterChip(
                        selected = selectedTimeframe == timeframe,
                        onClick = { selectedTimeframe = timeframe },
                        label = { Text(timeframe.label) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryNavy,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Net Earnings Card (Hero)
        item {
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = PrimaryNavy
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NET DRIVER PAYOUT (${selectedTimeframe.label.uppercase()})",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.7f),
                                letterSpacing = 1.sp
                            )
                        )
                        Box(
                            modifier = Modifier
                                .background(SuccessGreen.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Settled Direct to Bank",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "₹${"%.2f".format(displayedNetEarnings)}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White,
                            fontSize = 36.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = Color.White.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Trips Completed",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            )
                            Text(
                                text = "$displayedCompletedTrips rides",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Platform Fee (10%)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.7f)
                                )
                            )
                            Text(
                                text = "- ₹${"%.2f".format(displayedCommission)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = WarningAmber
                                )
                            )
                        }
                    }
                }
            }
        }

        // Solo Ride Promo Transparency Card
        item {
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = TealAccent.copy(alpha = 0.08f),
                borderColor = TealAccent.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(TealAccent.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = "Payout Guarantee",
                            tint = TealAccent,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "100% Payout Protection Guaranteed",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Text(
                            text = "When passengers receive RideMitra's 10% solo promo discount, the subsidy is 100% funded by RideMitra. Your agreed driver payout is NEVER discounted.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }

        // Payout Account & Action Card
        item {
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = CardBackground
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AccountBalance,
                                contentDescription = null,
                                tint = RoyalBlue,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Primary Settlement Account",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
                                )
                                Text(
                                    text = "HDFC Bank (A/C **4920)",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(SuccessGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Verified",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = DividerLight)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RideMitraOutlinedButton(
                            text = "Download PDF",
                            onClick = onDownloadStatement,
                            modifier = Modifier.weight(1f)
                        )
                        RideMitraButton(
                            text = "Instant Payout",
                            onClick = onRequestInstantPayout,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Toggle state bar for testing empty vs settled state
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryNavy.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = PrimaryNavy,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Recent Settled Trips",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                }

                FilterChip(
                    selected = showDemoSettledTrips,
                    onClick = { showDemoSettledTrips = !showDemoSettledTrips },
                    label = {
                        Text(
                            text = if (showDemoSettledTrips) "Viewing Demo Sample" else "Show Audit Sample",
                            fontSize = 11.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = RoyalBlue,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        // Transaction list or empty state
        if (activeTransactions.isEmpty()) {
            item {
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = CardBackground
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(BackgroundLight, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = "No transactions",
                                tint = TextMuted,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "No Settled Trips Yet Today",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Once you accept airport passenger rides and complete verified drop-offs, your detailed earnings ledger and net payouts will appear here in real-time.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                textAlign = TextAlign.Center
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        RideMitraOutlinedButton(
                            text = "View Sample Statement",
                            onClick = { showDemoSettledTrips = true }
                        )
                    }
                }
            }
        } else {
            items(activeTransactions) { transaction ->
                TransactionCard(transaction = transaction)
            }
        }

        // Footer notice
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RideMitra settles driver earnings directly via automated daily bank transfers at 23:59 IST. Instant withdrawals are subject to bank processing.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextMuted,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Composable
private fun TransactionCard(
    transaction: DriverEarningsTransaction,
    modifier: Modifier = Modifier
) {
    RideMitraCard(
        modifier = modifier.fillMaxWidth(),
        backgroundColor = CardBackground
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Trip ID and Payout amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = transaction.tripId,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue
                        )
                    )
                    Text(
                        text = transaction.dateFormatted,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "+₹${"%.2f".format(transaction.netDriverPayout)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = SuccessGreen
                        )
                    )
                    Text(
                        text = "Net Payout",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = DividerLight)
            Spacer(modifier = Modifier.height(10.dp))

            // Route description
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = transaction.routeSummary,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Fare breakdown grid
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundLight, RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Gross Passenger Fare",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = "₹${"%.2f".format(transaction.totalPassengerFare)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Platform Fee",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = "- ₹${"%.2f".format(transaction.platformCommission)}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ErrorRed
                        )
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Settlement Channel",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextSecondary,
                            fontSize = 10.sp
                        )
                    )
                    Text(
                        text = "Direct Bank",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                }
            }
        }
    }
}
