package com.ridemitra.app.ui.screens.driver.dashboard

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverActiveRide
import com.ridemitra.app.domain.model.DriverRideStatus
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.components.RideMitraTextField
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
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

/**
 * Screen 8: Active Rides Screen.
 * Contains:
 * - Passenger details & contact
 * - Pickup location (Airport terminal pillar)
 * - Destination & Route info
 * - Passenger count & luggage info
 * - Ride status transitions (ACCEPTED -> DRIVER_ARRIVING -> DRIVER_ARRIVED -> IN_PROGRESS -> COMPLETED)
 * - OTP verification interface
 * - Navigation placeholder
 * - Emergency roadside support
 * Note: Does not implement fake live tracking.
 */
@Composable
fun DriverActiveRidesScreen(
    activeRide: DriverActiveRide?,
    onUpdateStatus: (DriverRideStatus) -> Unit,
    onVerifyOtpAndStart: (String) -> Boolean,
    onCompleteTrip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    var enteredOtp by remember { mutableStateOf("") }
    var otpError by remember { mutableStateOf<String?>(null) }
    var isOtpVerified by remember { mutableStateOf(activeRide?.status == DriverRideStatus.IN_PROGRESS) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Active Trip Management",
            subtitle = if (activeRide != null) "Trip #${activeRide.bookingId} in execution" else "No ongoing airport trip"
        )

        if (activeRide == null) {
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
                        text = "No Active Ride in Progress",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Accept a booking request from the Requests tab to start navigating to the passenger's pickup pillar.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        ),
                        textAlign = TextAlign.Center
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
                // Trip Status Banner
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
                                text = "CURRENT TRIP STATUS",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TealAccent,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = activeRide.status.label,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = CardBackground
                                )
                            )
                        }

                        StatusBadge(
                            text = "#${activeRide.bookingId}",
                            variant = BadgeVariant.TEAL
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Passenger Details & Calling Card
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
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFDBEAFE)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = RoyalBlue,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Column {
                                    Text(
                                        text = activeRide.passengerName,
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "${activeRide.passengerCount} Passenger(s) • ${activeRide.luggageSummary}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextSecondary,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }

                            // Call button
                            IconButton(
                                onClick = {
                                    try {
                                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${activeRide.passengerPhone}"))
                                        context.startActivity(intent)
                                    } catch (_: Exception) {}
                                },
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(Color(0xFFE0F7F4))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = "Call Passenger",
                                    tint = TealAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Route & Pickup Pillar Details
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Navigation,
                                contentDescription = null,
                                tint = RoyalBlue,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Airport Route Details",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Column {
                            Text(
                                text = "Pickup Meeting Point:",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                            )
                            Text(
                                text = activeRide.pickupPillar,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Column {
                            Text(
                                text = "Drop Destination:",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted)
                            )
                            Text(
                                text = activeRide.destination,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Distance: ${activeRide.distanceKm} km (~${activeRide.estimatedDurationMinutes} mins)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = "Cash to Collect: ₹${activeRide.remainingCashToCollect.toInt()}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF166534),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // OTP Verification Box (Required before trip starts)
                if (activeRide.status != DriverRideStatus.IN_PROGRESS && activeRide.status != DriverRideStatus.COMPLETED) {
                    RideMitraCard(
                        modifier = Modifier.fillMaxWidth(),
                        backgroundColor = Color(0xFFEFF6FF),
                        borderColor = Color(0xFFBFDBFE),
                        elevation = 2.dp
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Key,
                                    contentDescription = null,
                                    tint = RoyalBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Passenger Trip Start OTP",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Ask the passenger for their 4-digit start OTP when they board at the pillar.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    RideMitraTextField(
                                        value = enteredOtp,
                                        onValueChange = {
                                            if (it.length <= 4) enteredOtp = it
                                        },
                                        placeholder = "Enter 4-digit OTP",
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                RideMitraButton(
                                    text = "Verify OTP",
                                    onClick = {
                                        if (enteredOtp == activeRide.expectedOtp) {
                                            otpError = null
                                            isOtpVerified = true
                                            onUpdateStatus(DriverRideStatus.IN_PROGRESS)
                                        } else {
                                            otpError = "Incorrect OTP. Please check passenger screen."
                                        }
                                    },
                                    modifier = Modifier.width(120.dp),
                                    height = 48.dp,
                                    gradient = PrimaryGradient
                                )
                            }

                            if (otpError != null) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = otpError!!,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = ErrorRed,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Status Transition Action Buttons
                when (activeRide.status) {
                    DriverRideStatus.ACCEPTED -> {
                        RideMitraButton(
                            text = "Mark 'Arriving at Airport Pillar'",
                            onClick = { onUpdateStatus(DriverRideStatus.DRIVER_ARRIVING) },
                            gradient = PrimaryGradient
                        )
                    }

                    DriverRideStatus.DRIVER_ARRIVING -> {
                        RideMitraButton(
                            text = "Mark 'Arrived at Pillar ${activeRide.pickupPillar}'",
                            onClick = { onUpdateStatus(DriverRideStatus.DRIVER_ARRIVED) },
                            gradient = HeroTealGradient
                        )
                    }

                    DriverRideStatus.DRIVER_ARRIVED -> {
                        Text(
                            text = "Verify passenger OTP above to start trip.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 11.sp
                            ),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    DriverRideStatus.IN_PROGRESS -> {
                        RideMitraButton(
                            text = "Complete Trip & Collect Balance (₹${activeRide.remainingCashToCollect.toInt()})",
                            onClick = onCompleteTrip,
                            gradient = HeroTealGradient
                        )
                    }

                    else -> Unit
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Roadside Support Hotline
                RideMitraOutlinedButton(
                    text = "Emergency Roadside Support (24/7)",
                    onClick = {
                        try {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:112"))
                            context.startActivity(intent)
                        } catch (_: Exception) {}
                    },
                    borderColor = BorderLight,
                    textColor = TextSecondary
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
