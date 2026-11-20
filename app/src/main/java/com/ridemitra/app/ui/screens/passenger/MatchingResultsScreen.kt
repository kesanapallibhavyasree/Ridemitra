package com.ridemitra.app.ui.screens.passenger

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
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
import com.ridemitra.app.domain.model.CoPassenger
import com.ridemitra.app.domain.model.Destination
import com.ridemitra.app.domain.model.DriverVehicleInfo
import com.ridemitra.app.domain.model.FareEstimate
import com.ridemitra.app.domain.model.RideOptionType
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

/**
 * Screen 12: Matching Results Screen.
 * Displays evaluated matches:
 * - Shared Pool: Verified co-passengers with flight #, drop locality, detour impact.
 * - If no pool compatible, clearly offers Solo Ride with 10% platform promotional discount.
 */
@Composable
fun MatchingResultsScreen(
    airport: Airport,
    destination: Destination,
    rideType: RideOptionType,
    fareEstimate: FareEstimate,
    onProceedToVehicleDetails: () -> Unit,
    onSwitchToSoloAlternative: () -> Unit,
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
            title = "12. Matching Results",
            subtitle = if (rideType == RideOptionType.SHARED_POOL) "Verified carpool co-traveler matched" else "Solo airport cab reserved",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Status Header Banner
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = if (rideType == RideOptionType.SHARED_POOL) Color(0xFFEFF6FF) else Color(0xFFF0FDF4),
                borderColor = if (rideType == RideOptionType.SHARED_POOL) Color(0xFFBFDBFE) else Color(0xFFBBF7D0),
                elevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AirportIconBadge(
                            icon = if (rideType == RideOptionType.SHARED_POOL) Icons.Default.Group else Icons.Default.DirectionsCar,
                            size = 40.dp,
                            iconSize = 20.dp,
                            tint = if (rideType == RideOptionType.SHARED_POOL) RoyalBlue else SuccessGreen,
                            background = if (rideType == RideOptionType.SHARED_POOL) Color(0xFFDBEAFE) else Color(0xFFDCFCE7)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (rideType == RideOptionType.SHARED_POOL) "Shared Match Confirmed" else "Dedicated Solo Cab Ready",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "Departure from ${airport.defaultTerminal}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    StatusBadge(
                        text = if (rideType == RideOptionType.SHARED_POOL) "1 MATCH" else "10% PROMO",
                        variant = if (rideType == RideOptionType.SHARED_POOL) BadgeVariant.ROYAL else BadgeVariant.TEAL
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (rideType == RideOptionType.SHARED_POOL) {
                // Matched Co-passenger Card
                Text(
                    text = "Matched Co-Passenger",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

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
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFDBEAFE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = RoyalBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Ananya Sharma",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Arrived on flight 6E-204",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            StatusBadge(text = "+8m detour", variant = BadgeVariant.TEAL)
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = "Co-Rider Drop Location:",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                                Text(
                                    text = "Along expressway corridor",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Luggage Compatibility:",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                )
                                Text(
                                    text = "1 Cabin Bag (Verified)",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = SuccessGreen
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                // Solo Option Card
                Text(
                    text = "Solo Cab Allocation",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Direct Private Airport Transfer",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Zero detour guarantee with direct route navigation from airport terminal.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "10% Platform Promotional Discount:",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F8C7B)
                                )
                            )
                            Text(
                                text = "- ₹${fareEstimate.roundedDiscountAmount}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF0F8C7B)
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Capacity & Rules Guarantee
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFF8FAFC),
                borderColor = BorderLight,
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
                        text = "Strict N-1 Seat Rule enforced: Middle seat kept empty for passenger luggage and comfort.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary CTA: View Driver & Vehicle Details
            RideMitraButton(
                text = "View Assigned Driver & Vehicle",
                onClick = onProceedToVehicleDetails,
                gradient = PrimaryGradient,
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
            )

            // Fallback / Alternative Toggle if currently in Shared Pool
            if (rideType == RideOptionType.SHARED_POOL) {
                Spacer(modifier = Modifier.height(10.dp))
                RideMitraOutlinedButton(
                    text = "Prefer Solo Ride Instead? (10% Off)",
                    onClick = onSwitchToSoloAlternative,
                    borderColor = TealAccent,
                    textColor = Color(0xFF0F8C7B)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
