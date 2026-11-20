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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.ridemitra.app.domain.model.FlightInfo
import com.ridemitra.app.domain.model.RideBooking
import com.ridemitra.app.domain.model.getDestinationsForAirport
import com.ridemitra.app.domain.model.indianAirportsList
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.ErrorRed
import com.ridemitra.app.ui.theme.HeroTealGradient
import com.ridemitra.app.ui.theme.PillShape
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
 * Screen 1: Passenger Home Screen.
 * Fully corrected based on Section 4:
 * - NO hardcoded Delhi Airport; selectable airport component supports all Indian airports.
 * - NO fixed/random preview prices; fares computed strictly upon destination choice.
 * - NO fake flight status or mock matches.
 * - Clean entry points to booking flow, safety SOS, active rides, and offers.
 */
@Composable
fun PassengerHomeScreen(
    selectedAirport: Airport,
    selectedTerminal: String,
    flightInfo: FlightInfo,
    activeBooking: RideBooking?,
    onSelectAirport: () -> Unit,
    onEnterFlightDetails: () -> Unit,
    onSearchDestination: () -> Unit,
    onOpenMapPicker: () -> Unit,
    onStartBookingFlow: () -> Unit,
    onOpenSafetySos: () -> Unit,
    onOpenNotifications: () -> Unit,
    onSwitchRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val popularDestinations = getDestinationsForAirport(selectedAirport.code)

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // 1. Top Header: Welcome, Role Switcher, and Notifications
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Welcome to RideMitra",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryNavy
                    )
                )
                Text(
                    text = "Land Together. Ride Together. Save Together.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                // Notifications Icon Button
                IconButton(onClick = onOpenNotifications) {
                    BadgedBox(badge = {
                        Badge(containerColor = ErrorRed) {
                            Text("2", color = Color.White, fontSize = 9.sp)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = PrimaryNavy,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

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
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Selectable Origin Airport Card (NOT hardcoded to Delhi)
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 3.dp,
            onClick = onSelectAirport
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    AirportIconBadge(
                        icon = Icons.Default.FlightLand,
                        size = 46.dp,
                        iconSize = 24.dp,
                        tint = RoyalBlue,
                        background = Color(0xFFDBEAFE)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "ORIGIN AIRPORT",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = RoyalBlue,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            StatusBadge(text = selectedAirport.code, variant = BadgeVariant.ROYAL)
                        }

                        Text(
                            text = selectedAirport.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            ),
                            maxLines = 1
                        )

                        Text(
                            text = "${selectedAirport.city} • ${selectedTerminal.ifEmpty { selectedAirport.defaultTerminal }}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEFF6FF))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Change",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RoyalBlue,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Flight Details Banner (Optional / Transparent - No fake mock data)
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = if (flightInfo.isConfigured) PrimaryNavy else Color(0xFFF8FAFC),
            borderColor = if (flightInfo.isConfigured) Color(0xFF28486E) else BorderLight,
            elevation = 2.dp,
            onClick = onEnterFlightDetails
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(if (flightInfo.isConfigured) Color(0x3320BFA9) else SurfaceVariantLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AirplanemodeActive,
                            contentDescription = null,
                            tint = if (flightInfo.isConfigured) TealAccent else RoyalBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = if (flightInfo.isConfigured) "Flight ${flightInfo.flightNumber} Linked" else "Arriving by Flight?",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (flightInfo.isConfigured) CardBackground else PrimaryNavy
                            )
                        )
                        Text(
                            text = if (flightInfo.isConfigured) "${flightInfo.airline} • ${flightInfo.arrivalTime}" else "Link flight to protect against arrival delays",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (flightInfo.isConfigured) Color(0xFF94A3B8) else TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }

                StatusBadge(
                    text = if (flightInfo.isConfigured) "LINKED" else "+ ADD FLIGHT",
                    variant = if (flightInfo.isConfigured) BadgeVariant.TEAL else BadgeVariant.NEUTRAL
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Primary Journey Booking Trigger Card
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 4.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Where are you heading from ${selectedAirport.code}?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrimaryNavy
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Choose your destination. Transparent fares are calculated based on your exact route distance and duration.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Search field trigger
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(SurfaceVariantLight)
                        .border(1.dp, BorderLight, RoundedCornerShape(12.dp))
                        .clickable { onSearchDestination() }
                        .padding(horizontal = 14.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = RoyalBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Search destination (e.g. Connaught Place, BKC, Whitefield)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextMuted,
                                fontSize = 12.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Popular Destination Chips for Quick Selection
                Text(
                    text = "Popular Routes from ${selectedAirport.city}:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    popularDestinations.take(3).forEach { dest ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(PillShape)
                                .background(SurfaceVariantLight)
                                .border(1.dp, BorderLight, PillShape)
                                .clickable { onSearchDestination() }
                                .padding(vertical = 8.dp, horizontal = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dest.name.split(",")[0],
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    fontSize = 11.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                RideMitraButton(
                    text = "Plan Journey & Calculate Fare",
                    onClick = onStartBookingFlow,
                    gradient = HeroTealGradient,
                    trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Active Trip Quick Card (If booking exists)
        if (activeBooking != null) {
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE),
                elevation = 3.dp,
                onClick = onStartBookingFlow
            ) {
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
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Active Booking #${activeBooking.bookingId}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "Pickup: ${activeBooking.pickupPillar} • OTP: ${activeBooking.startOtp}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = RoyalBlue,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    StatusBadge(text = "TRACK", variant = BadgeVariant.ROYAL)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // 6. RideMitra Fair Rules Banner
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = Color(0xFFF8FAFC),
            borderColor = BorderLight,
            elevation = 1.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RideMitra Airport Policies",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• Strict N-1 Seat Rule: Max capacity = vehicle seats - 1 (never overbooked).\n• 10% Advance Deposit: Remaining 90% payable at destination.\n• Solo Promo: 10% platform discount if no pool match; full driver payout protected.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 17.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7. Emergency SOS Quick Access
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFEF2F2))
                .border(1.dp, Color(0xFFFECDD3), RoundedCornerShape(14.dp))
                .clickable { onOpenSafetySos() }
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFEE2E2)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Sos,
                        contentDescription = "SOS",
                        tint = ErrorRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "RideMitra Safety Shield",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ErrorRed
                        )
                    )
                    Text(
                        text = "24/7 Police 112 & Airport Security Emergency SOS",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF991B1B),
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
