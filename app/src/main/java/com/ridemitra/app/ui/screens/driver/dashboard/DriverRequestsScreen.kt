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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
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
import com.ridemitra.app.ui.theme.WarningAmber

/**
 * Driver Booking Requests Screen
 * Features:
 * - Clear labeling of sample dispatch requests as DEMO DATA.
 * - Prevents accepting bookings if driver is not approved or has expired documents.
 * - Displays N-1 passenger capacity compatibility.
 */
@Composable
fun DriverRequestsScreen(
    requests: List<DriverBookingRequest>,
    isEligibleForBookings: Boolean = true,
    eligibilityBlockReason: String? = null,
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
            subtitle = if (requests.isNotEmpty()) "${requests.size} dispatch requests available [DEMO DATA]" else "Awaiting airport passenger requests"
        )

        // Block banner if ineligible
        if (!isEligibleForBookings) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF2F2))
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = eligibilityBlockReason ?: "Dispatch Ineligible: Approval and valid non-expired documents required to accept trips.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF991B1B),
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

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
                        text = "RideMitra matches drivers parked near airport arrival terminal pillars. Switch online on the home dashboard to receive incoming dispatches.",
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
                            // Top Row: Type & Driver Payout (Labeled DEMO DATA)
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StatusBadge(
                                        text = if (req.rideType == RideOptionType.SHARED_POOL) "SHARED POOL" else "SOLO RIDE",
                                        variant = if (req.rideType == RideOptionType.SHARED_POOL) BadgeVariant.TEAL else BadgeVariant.ROYAL
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    StatusBadge(text = "DEMO DATA", variant = BadgeVariant.NEUTRAL)
                                }

                                Text(
                                    text = "₹${req.estimatedDriverPayout.toInt()} Payout [DEMO]",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = SuccessGreen
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // Route Details
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.NearMe,
                                    contentDescription = null,
                                    tint = RoyalBlue,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(
                                        text = "Pickup: ${req.passengerPickup}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Drop: ${req.destination}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Route Metrics Pill
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceVariantLight)
                                    .padding(horizontal = 10.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "${req.distanceKm} km • ~${req.estimatedDurationMinutes} mins",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextPrimary,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    )
                                )
                                Text(
                                    text = req.requestedDepartureTime,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = RoyalBlue,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Passenger Count & Luggage
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Group,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${req.passengerCount} Passengers",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Luggage,
                                        contentDescription = null,
                                        tint = TextSecondary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = req.luggageSummary,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            if (req.flightNumber != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Flight: ${req.flightNumber}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TealAccent,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = BorderLight)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Action Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                RideMitraOutlinedButton(
                                    text = "Decline",
                                    onClick = { onRejectRequest(req) },
                                    borderColor = BorderLight,
                                    textColor = TextSecondary,
                                    modifier = Modifier.weight(0.7f),
                                    height = 42.dp
                                )

                                RideMitraButton(
                                    text = if (isEligibleForBookings) "Accept Dispatch" else "Ineligible to Accept",
                                    onClick = {
                                        if (isEligibleForBookings) onAcceptRequest(req)
                                    },
                                    gradient = if (isEligibleForBookings) HeroTealGradient else PrimaryGradient,
                                    leadingIcon = if (isEligibleForBookings) Icons.Default.Check else Icons.Default.Warning,
                                    modifier = Modifier.weight(1.3f),
                                    height = 42.dp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
