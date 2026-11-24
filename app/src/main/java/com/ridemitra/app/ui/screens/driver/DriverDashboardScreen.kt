package com.ridemitra.app.ui.screens.driver

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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Icon
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
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.components.StatusBadge
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

@Composable
fun DriverDashboardScreen(
    onSwitchRole: () -> Unit,
    onActionClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isOnline by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    val statusColor by animateColorAsState(
        targetValue = if (isOnline) SuccessGreen else TextMuted,
        label = "statusColor"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Driver Profile & Online Status Header
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
                            text = "Rajesh Kumar",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Verified Driver",
                            tint = TealAccent,
                            modifier = Modifier.size(16.dp)
                        )
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
                            text = if (isOnline) "ONLINE • Receiving Airport Trips" else "OFFLINE",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = statusColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            // Role Switcher
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

        Spacer(modifier = Modifier.height(16.dp))

        // Online / Offline Availability Switch Banner
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = if (isOnline) Color(0xFFF0FDF4) else Color(0xFFF8FAFC),
            borderColor = if (isOnline) Color(0xFF86EFAC) else Color(0xFFCBD5E1),
            elevation = 2.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = if (isOnline) "You are Ready for Airport Dispatches" else "You are Currently Offline",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (isOnline) Color(0xFF166534) else TextSecondary
                        )
                    )
                    Text(
                        text = if (isOnline) "GPS Availability: Simulated Location [DEMO DATA]" else "Toggle switch to start receiving rides",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isOnline) Color(0xFF15803D) else TextMuted
                        )
                    )
                }

                Switch(
                    checked = isOnline,
                    onCheckedChange = { isOnline = it },
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

        // Earnings Summary & Vehicle Details Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Earnings Card
            RideMitraCard(
                modifier = Modifier.weight(1f),
                backgroundColor = PrimaryNavy,
                borderColor = Color(0xFF28486E),
                elevation = 4.dp
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
                        text = "₹2,840 [DEMO]",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CardBackground
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "5 Airport Trips [DEMO] • 100% Payout",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            // Vehicle Capacity Rule Card
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
                        text = "Swift Dzire",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "4-Seater (Max 3 Riders)",
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

        // Live Incoming Ride Request (Interactive Card)
        Text(
            text = "Incoming Ride Request",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(10.dp))

        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            borderColor = TealAccent.copy(alpha = 0.5f),
            elevation = 5.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusBadge(
                        text = "SHARED AIRPORT POOL [DEMO DATA]",
                        variant = BadgeVariant.TEAL
                    )
                    Text(
                        text = "₹980 Fare [DEMO]",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = PrimaryNavy
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Route breakdown
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariantLight)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Pickup: IGI Airport Terminal 3 (Pillar 8)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Drop 1: Cyber Hub, DLF Phase 2 (Rider: Amit)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary
                        )
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Drop 2: Golf Course Road, Sec 54 (Rider: Priya)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Business Rule Note
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "10% advance deposit already secured. OTP required to start.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Actions: Accept / Reject
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    RideMitraOutlinedButton(
                        text = "Decline",
                        onClick = { onActionClicked("Declined Request") },
                        modifier = Modifier.weight(1f),
                        borderColor = Color(0xFFCBD5E1),
                        textColor = Color(0xFF64748B),
                        height = 46.dp
                    )

                    RideMitraButton(
                        text = "Accept Ride",
                        onClick = { onActionClicked("Accepted Ride Request (Trip #RM-408)") },
                        modifier = Modifier.weight(1.5f),
                        gradient = HeroTealGradient,
                        height = 46.dp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Platform Policy Assurance Card
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFFEFF6FF),
            borderColor = Color(0xFFBFDBFE),
            elevation = 1.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Driver Income Protection Policy",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue
                        )
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "When passengers receive a 10% solo ride promotional discount, RideMitra fully funds the discount. Your driver payout is 100% protected and never reduced.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFF1E40AF),
                        lineHeight = 17.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
