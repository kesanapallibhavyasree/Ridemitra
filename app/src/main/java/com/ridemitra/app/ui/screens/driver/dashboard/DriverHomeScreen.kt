package com.ridemitra.app.ui.screens.driver.dashboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverApplication
import com.ridemitra.app.domain.model.DriverBookingRequest
import com.ridemitra.app.domain.model.DriverEarningsSummary
import com.ridemitra.app.domain.model.DriverVerificationStatus
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.DarkNavy
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
 * Driver Dashboard (Home Tab)
 * Clearly labels all simulated / placeholder dashboard figures as DEMO DATA:
 * - Earnings [DEMO DATA]
 * - Completed rides [DEMO DATA]
 * - Booking payouts [DEMO DATA]
 * - GPS simulated status [DEMO DATA]
 * - Driver ratings [DEMO DATA]
 * Strictly checks driver approval and document validity before allowing ride dispatches.
 */
@Composable
fun DriverHomeScreen(
    application: DriverApplication,
    earningsSummary: DriverEarningsSummary,
    incomingRequests: List<DriverBookingRequest>,
    isOnline: Boolean,
    onToggleOnline: (Boolean) -> Unit,
    onViewRequests: () -> Unit,
    onViewActiveRides: () -> Unit,
    onViewEarnings: () -> Unit,
    onCheckVerificationStatus: () -> Unit,
    onSwitchRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val isEligible = application.isEligibleForBookings

    val statusColor by animateColorAsState(
        targetValue = if (isOnline && isEligible) SuccessGreen else TextMuted,
        label = "statusColor"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // 1. Driver Profile & Role Switcher Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0F7F4)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = TealAccent,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = application.personalInfo.fullName.ifBlank { "Rajesh Kumar" },
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        if (isEligible) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Approved",
                                tint = TealAccent,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(statusColor)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isOnline && isEligible) "ONLINE • Ready for Airport Dispatches" else if (!isEligible) "DISPATCH BLOCKED" else "OFFLINE",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (!isEligible) ErrorRed else statusColor,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            // Role Switcher Pill
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(SurfaceVariantLight)
                    .clickable { onSwitchRole() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Switch",
                        tint = RoyalBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Switch",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue
                        )
                    )
                }
            }
        }

        // Eligibility Banner if not approved or documents expired
        if (!isEligible) {
            Spacer(modifier = Modifier.height(14.dp))
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFFEF2F2),
                borderColor = Color(0xFFFECDD3),
                elevation = 2.dp,
                onClick = onCheckVerificationStatus
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = ErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (application.hasExpiredDocuments) "Booking Blocked: Document(s) Expired" else "Account ${application.status.label}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = ErrorRed
                            )
                        )
                        Text(
                            text = if (application.hasExpiredDocuments)
                                "Renew expired documents to restore airport dispatch eligibility."
                            else
                                "Awaiting compliance approval before receiving dispatch requests.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF991B1B),
                                fontSize = 11.sp
                            )
                        )
                    }
                    StatusBadge(text = "CHECK", variant = BadgeVariant.WARNING)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Online / Offline Availability Switch Card (With GPS simulated notice)
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = if (isOnline && isEligible) Color(0xFFF0FDF4) else Color(0xFFF8FAFC),
            borderColor = if (isOnline && isEligible) Color(0xFF86EFAC) else BorderLight,
            elevation = 2.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (isOnline && isEligible) "Receiving Airport Dispatches" else if (!isEligible) "Dispatch Disabled (Ineligible)" else "You are Currently Offline",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isOnline && isEligible) Color(0xFF166534) else TextSecondary
                        )
                    )
                    Text(
                        text = if (isOnline && isEligible)
                            "GPS Availability: Simulated Location [DEMO DATA]"
                        else
                            "Toggle switch to activate dispatch simulation",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isOnline && isEligible) Color(0xFF15803D) else TextMuted,
                            fontSize = 11.sp
                        )
                    )
                }

                Switch(
                    checked = isOnline && isEligible,
                    onCheckedChange = { checked ->
                        if (isEligible) {
                            onToggleOnline(checked)
                        } else {
                            onCheckVerificationStatus()
                        }
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = CardBackground,
                        checkedTrackColor = TealAccent,
                        uncheckedThumbColor = Color(0xFF94A3B8),
                        uncheckedTrackColor = Color(0xFFE2E8F0)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Verification Status Bar
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFFEFF6FF),
            borderColor = Color(0xFFBFDBFE),
            elevation = 1.dp,
            onClick = onCheckVerificationStatus
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Driver Compliance Status",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Text(
                            text = if (application.hasExpiredDocuments) "Documents Expired" else application.status.label,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (application.hasExpiredDocuments) ErrorRed else RoyalBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                StatusBadge(text = "DETAILS", variant = BadgeVariant.ROYAL)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Metrics Summary Row (Labeled as DEMO DATA)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Net Earnings Card (Labeled DEMO DATA)
            RideMitraCard(
                modifier = Modifier.weight(1f),
                backgroundColor = PrimaryNavy,
                borderColor = Color(0xFF28486E),
                elevation = 4.dp,
                onClick = onViewEarnings
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TODAY'S EARNINGS",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TealAccent,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        )
                        StatusBadge(text = "DEMO DATA", variant = BadgeVariant.NEUTRAL)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (earningsSummary.todayNetEarnings > 0) "₹${earningsSummary.todayNetEarnings.toInt()}" else "₹0",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CardBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${earningsSummary.todayCompletedTrips} Completed Trips • Net Payout",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Registered Vehicle & Strict N-1 Capacity Card
            RideMitraCard(
                modifier = Modifier.weight(1f),
                borderColor = RoyalBlue.copy(alpha = 0.3f),
                elevation = 2.dp
            ) {
                Column {
                    Text(
                        text = "REGISTERED VEHICLE",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RoyalBlue,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "${application.vehicleDetails.manufacturer} ${application.vehicleDetails.model}".trim().ifBlank { "Swift Dzire" },
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        ),
                        maxLines = 1
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${application.vehicleDetails.passengerCapacityExclDriver}-Seater (Max ${application.vehicleDetails.allowedRideMitraCapacity} Riders - N-1 Rule)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = SuccessGreen,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Driver Rating Card (Labeled DEMO DATA)
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFFF8FAFC),
            borderColor = BorderLight,
            elevation = 1.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = WarningAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Driver Rating: ★ ${earningsSummary.driverRating}",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                }
                StatusBadge(text = "DEMO DATA", variant = BadgeVariant.NEUTRAL)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Incoming Booking Requests Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Incoming Booking Requests",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            )

            if (incomingRequests.isNotEmpty()) {
                Text(
                    text = "View All (${incomingRequests.size})",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = RoyalBlue,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.clickable { onViewRequests() }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (incomingRequests.isEmpty()) {
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFF8FAFC),
                borderColor = BorderLight,
                elevation = 1.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isOnline && isEligible) "No ride requests available right now." else "You are currently offline.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextSecondary
                        )
                    )
                    Text(
                        text = if (isOnline && isEligible) "New airport dispatch requests will appear here when passengers book." else "Switch online above to start receiving dispatch requests.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextMuted,
                            fontSize = 11.sp
                        ),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            val req = incomingRequests.first()
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = TealAccent.copy(alpha = 0.5f),
                elevation = 4.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatusBadge(
                            text = "AIRPORT DISPATCH [DEMO DATA]",
                            variant = BadgeVariant.TEAL
                        )
                        Text(
                            text = "₹${req.estimatedDriverPayout.toInt()} Payout [DEMO]",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryNavy
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Pickup: ${req.passengerPickup}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Text(
                        text = "Drop: ${req.destination} (${req.distanceKm} km • ~${req.estimatedDurationMinutes}m)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraButton(
                        text = if (isEligible) "Review & Accept Request" else "Account Ineligible to Accept",
                        onClick = onViewRequests,
                        gradient = if (isEligible) HeroTealGradient else PrimaryGradient,
                        height = 42.dp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
