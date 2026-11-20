package com.ridemitra.app.ui.screens.passenger

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.BookingStatus
import com.ridemitra.app.domain.model.RideBooking
import com.ridemitra.app.domain.model.sampleActiveBookings
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Passenger Track Tab Screen:
 * Displays live GPS route telemetry, pickup pillar guidance, driver contact, and start OTP
 * when a ride is active. Displays a clean empty state when no ride is in progress.
 */
@Composable
fun PassengerTrackScreen(
    activeBooking: RideBooking? = sampleActiveBookings.firstOrNull(),
    onBookNewRide: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Live Ride Tracking",
            subtitle = if (activeBooking != null) "Trip #${activeBooking.bookingId} in transit" else "No active ride currently dispatched"
        )

        if (activeBooking == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AirportIconBadge(
                        icon = Icons.Default.NearMe,
                        size = 64.dp,
                        iconSize = 32.dp,
                        tint = RoyalBlue,
                        background = Color(0xFFDBEAFE)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "No Active Ride to Track",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Book an airport pool or solo ride to track your cab in real-time.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    RideMitraButton(
                        text = "Book Airport Ride",
                        onClick = onBookNewRide,
                        gradient = PrimaryGradient
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                // Simulated live tracking map canvas
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFE2E8F0))
                        .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val start = Offset(size.width * 0.2f, size.height * 0.8f)
                        val cabPos = Offset(size.width * 0.45f, size.height * 0.5f)
                        val end = Offset(size.width * 0.8f, size.height * 0.2f)

                        // Route line
                        drawLine(
                            color = RoyalBlue,
                            start = start,
                            end = end,
                            strokeWidth = 6f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
                        )

                        // Pickup point
                        drawCircle(color = PrimaryNavy, radius = 10f, center = start)

                        // Cab moving position
                        drawCircle(color = TealAccent, radius = 14f, center = cabPos)
                        drawCircle(color = PrimaryNavy, radius = 6f, center = cabPos)

                        // Drop point
                        drawCircle(color = Color(0xFFEF4444), radius = 12f, center = end)
                    }

                    // Floating Live Tracking Pill
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 10.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xEE142B45))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(SuccessGreen)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Driver En Route to ${activeBooking.pickupPillar} • ~4 mins away",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = CardBackground,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Start OTP Banner Card
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = PrimaryNavy,
                    borderColor = Color(0xFF28486E),
                    elevation = 3.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "TRIP START OTP",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TealAccent,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                            )
                            Text(
                                text = "Share with driver upon board",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Text(
                            text = activeBooking.startOtp,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = CardBackground,
                                letterSpacing = 4.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Assigned Driver & Cab details
                if (activeBooking.driverVehicleInfo != null) {
                    val driver = activeBooking.driverVehicleInfo
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
                                    AirportIconBadge(
                                        icon = Icons.Default.DirectionsCar,
                                        size = 40.dp,
                                        iconSize = 20.dp,
                                        tint = RoyalBlue,
                                        background = Color(0xFFDBEAFE)
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = driver.vehicleModel,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        )
                                        Text(
                                            text = "Plate: ${driver.vehicleNumber} • Driver: ${driver.driverName}",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondary
                                            )
                                        )
                                    }
                                }

                                StatusBadge(text = "${driver.rating}★", variant = BadgeVariant.ROYAL)
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
                                        text = "Meeting Point:",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                    )
                                    Text(
                                        text = activeBooking.pickupPillar,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = RoyalBlue
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Terminal:",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                                    )
                                    Text(
                                        text = activeBooking.terminal,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Safety Assurance
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
                            tint = TealAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Route telemetry is monitored 24/7 by RideMitra safety system.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
