package com.ridemitra.app.ui.screens.passenger

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.BookingStatus
import com.ridemitra.app.domain.model.RideBooking
import com.ridemitra.app.domain.model.RideOptionType
import com.ridemitra.app.domain.model.sampleActiveBookings
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
 * Screen 16: My Rides Screen.
 * Shows active and upcoming airport rides, start OTP, driver details, and
 * cancellation action evaluating exact business refund rules:
 * - > 1 hour before scheduled departure: refund 5% of total ride fare.
 * - <= 1 hour or post-departure: no refund.
 */
@Composable
fun MyRidesScreen(
    activeBookings: List<RideBooking> = sampleActiveBookings,
    onViewRideHistory: () -> Unit,
    onBookNewRide: () -> Unit,
    onRideCancelled: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var bookingToCancel by remember { mutableStateOf<RideBooking?>(null) }
    var cancellationNotice by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "My Airport Rides",
            subtitle = "Active & Upcoming Scheduled Bookings",
            actions = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { onViewRideHistory() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = "History",
                            tint = RoyalBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Past Rides",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = RoyalBlue,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        )

        if (activeBookings.isEmpty()) {
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
                        text = "No Active Airport Bookings",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Heading to or from the airport? Book a shared pool or solo ride with live flight tracking.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(activeBookings, key = { it.bookingId }) { booking ->
                    val fare = booking.fareEstimate

                    RideMitraCard(
                        modifier = Modifier.fillMaxWidth(),
                        borderColor = RoyalBlue.copy(alpha = 0.4f),
                        elevation = 3.dp
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Top Row: Status & Booking ID
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    StatusBadge(
                                        text = booking.status.label.uppercase(),
                                        variant = when (booking.status) {
                                            BookingStatus.CONFIRMED -> BadgeVariant.SUCCESS
                                            BookingStatus.IN_PROGRESS -> BadgeVariant.TEAL
                                            else -> BadgeVariant.ROYAL
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "#${booking.bookingId}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = TextMuted,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }

                                Text(
                                    text = if (booking.rideType == RideOptionType.SHARED_POOL) "Shared Pool" else "Solo Cab",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = RoyalBlue
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Start OTP Banner
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PrimaryNavy)
                                    .padding(horizontal = 14.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TRIP START OTP:",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TealAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    text = booking.startOtp,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = CardBackground,
                                        letterSpacing = 4.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            // Pickup & Drop Route
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(TealAccent)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Pickup: ${booking.pickupPillar} (${booking.terminal})",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PrimaryNavy
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .size(8.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xFFEF4444))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Drop: ${booking.destination.name} (${booking.destination.locality})",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextPrimary
                                        )
                                    )
                                }
                            }

                            if (booking.driverVehicleInfo != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                HorizontalDivider(color = BorderLight)
                                Spacer(modifier = Modifier.height(10.dp))

                                val driver = booking.driverVehicleInfo
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "${driver.vehicleModel} • ${driver.vehicleNumber}",
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        )
                                        Text(
                                            text = "Driver: ${driver.driverName} (${driver.rating}★)",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }

                                    StatusBadge(
                                        text = "N-1 COMPLIANT",
                                        variant = BadgeVariant.TEAL
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = BorderLight)
                            Spacer(modifier = Modifier.height(12.dp))

                            // Payment summary & Cancel CTA
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "10% Advance Paid: ₹${fare.roundedAdvanceDeposit}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = SuccessGreen,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = "Balance due at drop: ₹${fare.roundedRemainingBalance}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = TextSecondary
                                        )
                                    )
                                }

                                RideMitraOutlinedButton(
                                    text = "Cancel",
                                    onClick = { bookingToCancel = booking },
                                    borderColor = Color(0xFFFCA5A5),
                                    textColor = ErrorRed,
                                    height = 36.dp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Cancellation Confirmation Dialog (Evaluating strict business refund tier)
        if (bookingToCancel != null) {
            val booking = bookingToCancel!!
            val eligibleRefund = booking.calculateCancellationRefund()

            AlertDialog(
                onDismissRequest = { bookingToCancel = null },
                title = {
                    Text(
                        text = "Cancel Booking #${booking.bookingId}?",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Cancellation Policy Evaluation:",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        if (eligibleRefund > 0.0) {
                            Text(
                                text = "• Scheduled pickup is more than 1 hour away.\n• You are eligible for a 5% refund of total ride fare (₹${eligibleRefund.toInt()}).",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF15803D),
                                    lineHeight = 17.sp
                                )
                            )
                        } else {
                            Text(
                                text = "• Scheduled pickup is within 1 hour or post-departure.\n• As per airport regulations, this cancellation is non-refundable (0% refund).",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFFB45309),
                                    lineHeight = 17.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Note: Refund requests are submitted directly to the airport compliance desk. No fake automated refunds are issued.",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontSize = 10.sp
                            )
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onRideCancelled(booking.bookingId)
                            bookingToCancel = null
                        }
                    ) {
                        Text("Confirm Cancellation", color = ErrorRed, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { bookingToCancel = null }) {
                        Text("Keep Ride", color = RoyalBlue)
                    }
                }
            )
        }
    }
}
