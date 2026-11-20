package com.ridemitra.app.ui.screens.driver.dashboard

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverBookingRequest
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
import com.ridemitra.app.ui.theme.ErrorRed
import com.ridemitra.app.ui.theme.HeroTealGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Screen 7: Booking Requests Screen.
 * Displays incoming passenger bookings when available:
 * - Passenger pickup location
 * - Destination
 * - Distance
 * - Estimated journey duration
 * - Passenger count
 * - Luggage information
 * - Requested departure time
 * - Estimated driver payout
 * - Ride type (Shared/Solo)
 * Actions: Accept, Reject, View details.
 * Note: If backend is unavailable, shows meaningful clean empty state: "No ride requests available right now."
 */
@Composable
fun DriverRequestsScreen(
    requests: List<DriverBookingRequest>,
    onAcceptRequest: (DriverBookingRequest) -> Unit,
    onRejectRequest: (DriverBookingRequest) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Booking Requests",
            subtitle = if (requests.isNotEmpty()) "${requests.size} dispatch requests available" else "Awaiting airport passenger requests"
        )

        if (requests.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AirportIconBadge(
                        icon = Icons.Default.DirectionsCar,
                        size = 64.dp,
                        iconSize = 32.dp,
                        tint = TextSecondary,
                        background = SurfaceVariantLight
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "No ride requests available right now.",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "RideMitra matches drivers parked near airport arrival terminal pillars. Keep your GPS active to receive instant dispatches.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(requests, key = { it.requestId }) { req ->
                    RideMitraCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = RoyalBlue.copy(alpha = 0.4f),
                        elevation = 4.dp
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Top Row: Type & Driver Payout
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                StatusBadge(
                                    text = if (req.rideType == RideOptionType.SHARED_POOL) "SHARED POOL (${req.passengerCount} RIDERS)" else "SOLO PRIVATE CAB",
                                    variant = if (req.rideType == RideOptionType.SHARED_POOL) BadgeVariant.ROYAL else BadgeVariant.TEAL
                                )

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "₹${req.estimatedDriverPayout.toInt()}",
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = PrimaryNavy
                                        )
                                    )
                                    Text(
                                        text = "Guaranteed Net Payout",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = SuccessGreen,
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Route Info
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SurfaceVariantLight)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = "Pickup: ${req.passengerPickup}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy
                                    )
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Drop: ${req.destination}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Meta specs: distance, duration, luggage, time
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${req.distanceKm} km • ~${req.estimatedDurationMinutes} mins",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Luggage: ${req.luggageSummary}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = "Departure: ${req.requestedDepartureTime} (At Terminal Pillar)",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = RoyalBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = BorderLight)
                            Spacer(modifier = Modifier.height(14.dp))

                            // Actions: Decline / Accept
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RideMitraOutlinedButton(
                                    text = "Decline",
                                    onClick = { onRejectRequest(req) },
                                    modifier = Modifier.weight(1f),
                                    borderColor = BorderLight,
                                    textColor = TextSecondary,
                                    height = 44.dp
                                )

                                RideMitraButton(
                                    text = "Accept Ride",
                                    onClick = { onAcceptRequest(req) },
                                    modifier = Modifier.weight(1.4f),
                                    gradient = HeroTealGradient,
                                    height = 44.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
