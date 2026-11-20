package com.ridemitra.app.ui.screens.passenger

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.Airport
import com.ridemitra.app.domain.model.Destination
import com.ridemitra.app.domain.model.FareEstimate
import com.ridemitra.app.domain.model.RideOptionType
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.HeroTealGradient
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary
import com.ridemitra.app.ui.theme.WarningAmber

/**
 * Screen 14: Fare Breakdown Screen.
 * Fully transparent, non-random pricing display:
 * - Base airport flag down
 * - Distance charge
 * - Time and congestion charge
 * - Terminal toll and parking fee
 * - 10% platform promotional discount (on solo rides)
 * - 10% advance deposit required now
 * - Remaining 90% payable at drop
 * - Cancellation refund policy (5% if > 1 hr)
 */
@Composable
fun FareBreakdownScreen(
    airport: Airport,
    destination: Destination,
    rideType: RideOptionType,
    fare: FareEstimate,
    onProceedToConfirmation: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "14. Itemized Fare Breakdown",
            subtitle = "Guaranteed transparent pricing before confirmation",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Receipt Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 3.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AirportIconBadge(
                                icon = Icons.Default.ReceiptLong,
                                size = 38.dp,
                                iconSize = 20.dp,
                                tint = PrimaryNavy,
                                background = Color(0xFFDBEAFE)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = if (rideType == RideOptionType.SHARED_POOL) "Shared Pool Fare Receipt" else "Solo Cab Fare Receipt",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy
                                    )
                                )
                                Text(
                                    text = "${destination.name} (${destination.distanceKm} km)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        StatusBadge(
                            text = if (fare.isSolo) "SOLO CAB" else "CARPOOL",
                            variant = if (fare.isSolo) BadgeVariant.TEAL else BadgeVariant.ROYAL
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(14.dp))

                    FareItemRow("Base Airport Flag-Down", "₹${fare.baseFare.toInt()}")
                    FareItemRow("Distance Charge (${fare.routeDistanceKm} km @ ₹16.5/km)", "₹${fare.distanceCharge.toInt()}")
                    FareItemRow("Time & Congestion (~${fare.routeDurationMinutes} mins @ ₹2/min)", "₹${fare.timeCharge.toInt()}")
                    FareItemRow("${airport.code} Commercial Toll & Parking", "₹${fare.airportTollFee.toInt()}")

                    if (fare.isSolo && fare.discountAmount > 0) {
                        Spacer(modifier = Modifier.height(6.dp))
                        FareItemRow(
                            title = "10% Platform Solo Promo Discount",
                            amount = "- ₹${fare.roundedDiscountAmount}",
                            isDiscount = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Total Estimated Fare
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Estimated Fare",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Text(
                            text = "₹${fare.roundedFinalFare}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryNavy
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mandatory 10% Advance Deposit Rule Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0FDF4))
                    .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "10% Advance Deposit Required Now:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        )
                        Text(
                            text = "₹${fare.roundedAdvanceDeposit}",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF166534)
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Remaining balance of ₹${fare.roundedRemainingBalance} (90%) is payable directly to your driver upon arrival at destination.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF15803D),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Cancellation Policy Rule Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFEF3C7))
                    .border(1.dp, Color(0xFFFDE68A), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = WarningAmber,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Strict Cancellation Policy:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                        )
                        Text(
                            text = "• Cancel > 1 hour before pickup: 5% of total ride fare refunded (₹${fare.roundedCancellationRefund}).\n• Cancel <= 1 hour before pickup: Non-refundable (0% refund).\n• Post scheduled departure: Non-refundable (0% refund).",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFFB45309),
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }

            // Driver Payout Note for Solo Rides
            if (fare.isSolo) {
                Spacer(modifier = Modifier.height(12.dp))
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFFEFF6FF),
                    borderColor = Color(0xFFBFDBFE),
                    elevation = 1.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = RoyalBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "RideMitra funds your 10% promo discount. Driver receives their full agreed payout of ₹${fare.roundedOriginalFare}.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = RoyalBlue,
                                fontWeight = FontWeight.Medium,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            RideMitraButton(
                text = "Pay 10% Advance (₹${fare.roundedAdvanceDeposit}) & Confirm",
                onClick = onProceedToConfirmation,
                gradient = PrimaryGradient,
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FareItemRow(
    title: String,
    amount: String,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isDiscount) Color(0xFF0F8C7B) else TextSecondary,
                fontWeight = if (isDiscount) FontWeight.Bold else FontWeight.Normal
            )
        )
        Text(
            text = amount,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isDiscount) Color(0xFF0F8C7B) else TextPrimary,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}
