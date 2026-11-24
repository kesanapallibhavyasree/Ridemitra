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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.DividerLight
import com.ridemitra.app.ui.theme.ErrorRed
import com.ridemitra.app.ui.theme.HeroTealGradient
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.SurfaceVariantLight
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

/**
 * Driver Earnings Screen
 * Clearly labels all simulated payout data, trips, and commission calculations as DEMO DATA.
 */
@Composable
fun DriverEarningsScreen(
    earningsSummary: DriverEarningsSummary,
    onDownloadStatement: () -> Unit,
    onRequestInstantPayout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTimeframe by remember { mutableStateOf(EarningsTimeframe.TODAY) }

    val sampleHistoricalTransactions = remember {
        listOf(
            DriverEarningsTransaction(
                tripId = "TRIP-BLR-8921",
                dateFormatted = "Today, 03:45 PM",
                routeSummary = "BLR Airport T1 -> Whitefield ITPL",
                totalPassengerFare = 890.0,
                platformCommission = 89.0, // 10% platform take
                netDriverPayout = 801.0,
                paymentMode = "NEFT direct to Bank A/C **4920",
                isDemoData = true
            ),
            DriverEarningsTransaction(
                tripId = "TRIP-BLR-8910",
                dateFormatted = "Today, 11:15 AM",
                routeSummary = "BLR Airport T2 -> Koramangala 4th Block",
                totalPassengerFare = 640.0,
                platformCommission = 64.0,
                netDriverPayout = 576.0,
                paymentMode = "Instant IMPS to Bank A/C **4920",
                isDemoData = true
            ),
            DriverEarningsTransaction(
                tripId = "TRIP-BLR-8804",
                dateFormatted = "Yesterday, 08:20 PM",
                routeSummary = "BLR Airport T1 -> Electronic City Phase 1",
                totalPassengerFare = 1120.0,
                platformCommission = 112.0,
                netDriverPayout = 1008.0,
                paymentMode = "NEFT direct to Bank A/C **4920",
                isDemoData = true
            )
        )
    }

    val activeTransactions = if (earningsSummary.recentTransactions.isNotEmpty()) {
        earningsSummary.recentTransactions
    } else {
        sampleHistoricalTransactions
    }

    val displayedNetEarnings = when (selectedTimeframe) {
        EarningsTimeframe.TODAY -> earningsSummary.todayNetEarnings
        EarningsTimeframe.THIS_WEEK -> earningsSummary.weeklyNetEarnings
        EarningsTimeframe.THIS_MONTH -> earningsSummary.monthlyNetEarnings
    }

    val displayedCompletedTrips = when (selectedTimeframe) {
        EarningsTimeframe.TODAY -> earningsSummary.todayCompletedTrips
        EarningsTimeframe.THIS_WEEK -> (earningsSummary.todayCompletedTrips + 10)
        EarningsTimeframe.THIS_MONTH -> earningsSummary.totalCompletedTrips
    }

    val displayedCommission = when (selectedTimeframe) {
        EarningsTimeframe.TODAY -> earningsSummary.platformCommissionDeductedToday
        EarningsTimeframe.THIS_WEEK -> (earningsSummary.platformCommissionDeductedToday * 4)
        EarningsTimeframe.THIS_MONTH -> 3800.0
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Driver Earnings & Payouts",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    StatusBadge(text = "DEMO DATA", variant = BadgeVariant.NEUTRAL)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Demonstration financial ledger and 100% solo discount compensation preview.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }

        // Demo Data Notice Banner
        item {
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFFEF3C7),
                borderColor = Color(0xFFFDE68A),
                elevation = 1.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = WarningAmber,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = "NOTICE: All earnings, payouts, and completed trip counts displayed on this screen are simulated DEMO DATA for Phase 3 review.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF92400E),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        // Timeframe Filter Chips
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                EarningsTimeframe.values().forEach { timeframe ->
                    FilterChip(
                        selected = selectedTimeframe == timeframe,
                        onClick = { selectedTimeframe = timeframe },
                        label = { Text(timeframe.label, fontSize = 12.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = RoyalBlue,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Big Primary Net Payout Card
        item {
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = PrimaryNavy,
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.padding(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "NET TAKE-HOME EARNINGS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TealAccent,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                        StatusBadge(text = "DEMO DATA", variant = BadgeVariant.NEUTRAL)
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "₹${displayedNetEarnings.toInt()}",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CardBackground,
                            fontSize = 36.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$displayedCompletedTrips Completed Trips [DEMO DATA]",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        )
                        Text(
                            text = "Platform Take: ₹${displayedCommission.toInt()} [DEMO]",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = Color(0xFF28486E))
                    Spacer(modifier = Modifier.height(12.dp))

                    // 100% Solo Discount Driver Protection Guarantee
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = SuccessGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "100% Driver Payout Guarantee: Promotional passenger discounts are fully platform-funded.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SuccessGreen,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

        // Payout Actions (Demo)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                RideMitraButton(
                    text = "Request Instant Payout",
                    onClick = onRequestInstantPayout,
                    gradient = HeroTealGradient,
                    leadingIcon = Icons.Default.AccountBalanceWallet,
                    modifier = Modifier.weight(1f),
                    height = 46.dp
                )

                RideMitraOutlinedButton(
                    text = "Download PDF",
                    onClick = onDownloadStatement,
                    borderColor = RoyalBlue,
                    textColor = RoyalBlue,
                    leadingIcon = Icons.Default.Download,
                    modifier = Modifier.weight(0.9f),
                    height = 46.dp
                )
            }
        }

        // Recent Settled Transactions Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Settlement Ledger (Recent Trips)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                )
                Text(
                    text = "${activeTransactions.size} Records [DEMO]",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                )
            }
        }

        // Transaction Line Items
        items(activeTransactions, key = { it.tripId }) { tx ->
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = tx.tripId,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue
                                )
                            )
                            Text(
                                text = tx.dateFormatted,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "+₹${tx.netDriverPayout.toInt()}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SuccessGreen
                                )
                            )
                            Text(
                                text = "Net Credited [DEMO]",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = tx.routeSummary,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Gross Fare: ₹${tx.totalPassengerFare.toInt()} | Platform Fee: ₹${tx.platformCommission.toInt()}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                        Text(
                            text = tx.paymentMode,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
