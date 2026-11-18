package com.ridemitra.app.ui.screens.passenger

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Sos
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.CoPassenger
import com.ridemitra.app.domain.model.Destination
import com.ridemitra.app.domain.model.DriverVehicleInfo
import com.ridemitra.app.domain.model.FareEstimate
import com.ridemitra.app.domain.model.LuggageOption
import com.ridemitra.app.domain.model.RideMatchResult
import com.ridemitra.app.domain.model.RideOptionType
import com.ridemitra.app.domain.model.RidePreferences
import com.ridemitra.app.domain.model.popularAirportDestinations
import com.ridemitra.app.domain.pricing.AirportPricingEngine
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.components.RideMitraTextField
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
import com.ridemitra.app.ui.theme.WarningAmber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PassengerHomeScreen(
    onSwitchRole: () -> Unit,
    onActionClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Screen State
    var searchQuery by remember { mutableStateOf("") }
    var selectedDestination by remember { mutableStateOf<Destination?>(null) }
    var ridePreferences by remember { mutableStateOf(RidePreferences()) }
    var activeMatchResult by remember { mutableStateOf<RideMatchResult?>(null) }
    var isBookingConfirmed by remember { mutableStateOf(false) }

    // Dynamic Fare Calculation (Only evaluated when a destination is chosen)
    val dynamicFare: FareEstimate? = selectedDestination?.let { destination ->
        AirportPricingEngine.calculateFare(
            distanceKm = destination.distanceKm,
            durationMinutes = destination.estimatedDurationMinutes,
            rideType = ridePreferences.rideType,
            passengerCount = ridePreferences.passengerCount
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
            .animateContentSize()
    ) {
        // 1. Top Bar: Airport Badge & Role Switcher
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.FlightLand,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "DELHI AIRPORT (IGI - T3)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
                Text(
                    text = "Welcome to RideMitra",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
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

        Spacer(modifier = Modifier.height(16.dp))

        // 2. Flight Sync Banner Card
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = PrimaryNavy,
            borderColor = Color(0xFF28486E),
            elevation = 4.dp
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0x3320BFA9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AirplanemodeActive,
                            contentDescription = null,
                            tint = TealAccent,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Live Flight Sync Active",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = CardBackground
                            )
                        )
                        Text(
                            text = "Flight AI-802 from BOM • Landed • Gate 4",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF94A3B8)
                            )
                        )
                    }
                }

                StatusBadge(
                    text = "AUTO SYNC",
                    variant = BadgeVariant.TEAL
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. Destination Input & Route Selection Section
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 3.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Where are you heading from IGI Airport?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Enter your drop locality or select below. Fares are calculated on your exact route.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                RideMitraTextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        // Match with popular destinations if typed
                        val match = popularAirportDestinations.find {
                            it.name.contains(query, ignoreCase = true) || it.locality.contains(query, ignoreCase = true)
                        }
                        if (match != null) {
                            selectedDestination = match
                        } else if (query.isNotBlank()) {
                            // Custom destination entered: calculate distance deterministically based on input length
                            selectedDestination = Destination(
                                name = query,
                                locality = "NCR Region",
                                distanceKm = 15.0 + (query.length % 15),
                                estimatedDurationMinutes = 25 + (query.length % 20)
                            )
                        } else {
                            selectedDestination = null
                            activeMatchResult = null
                        }
                    },
                    placeholder = "Search destination (e.g., Cyber City, Connaught Place)",
                    leadingIcon = Icons.Default.Search,
                    trailingIcon = if (searchQuery.isNotEmpty()) Icons.Default.Close else null,
                    onTrailingIconClick = {
                        searchQuery = ""
                        selectedDestination = null
                        activeMatchResult = null
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Popular Destination Chips for Quick Selection
                Text(
                    text = "Popular Airport Routes:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    popularAirportDestinations.take(4).forEach { destination ->
                        val isSelected = selectedDestination?.name == destination.name
                        Box(
                            modifier = Modifier
                                .clip(PillShape)
                                .background(if (isSelected) RoyalBlue else SurfaceVariantLight)
                                .border(
                                    width = 1.dp,
                                    color = if (isSelected) RoyalBlue else BorderLight,
                                    shape = PillShape
                                )
                                .clickable {
                                    selectedDestination = destination
                                    searchQuery = destination.name
                                    activeMatchResult = null
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = destination.name.split(",")[0],
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) CardBackground else TextPrimary
                                )
                            )
                        }
                    }
                }

                // If destination is selected, display verified route details card
                if (selectedDestination != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFEFF6FF))
                            .border(1.dp, Color(0xFFBFDBFE), RoundedCornerShape(14.dp))
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.NearMe,
                                        contentDescription = null,
                                        tint = RoyalBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Route Configured",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = RoyalBlue,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                                StatusBadge(
                                    text = "${selectedDestination!!.distanceKm} km • ~${selectedDestination!!.estimatedDurationMinutes} mins",
                                    variant = BadgeVariant.ROYAL
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "From: IGI Airport Terminal 3\nTo: ${selectedDestination!!.name} (${selectedDestination!!.locality})",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF1E3A8A),
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Ride Options: Shared Pool vs Solo Ride (DESCRIPTIONS ONLY BEFORE SELECTION)
        Text(
            text = "Select Ride Category",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Option A: SHARED POOL CARD
            val isShared = ridePreferences.rideType == RideOptionType.SHARED_POOL
            RideMitraCard(
                modifier = Modifier.weight(1f),
                borderColor = if (isShared) RoyalBlue else BorderLight,
                backgroundColor = if (isShared) Color(0xFFF8FAFF) else CardBackground,
                elevation = if (isShared) 3.dp else 1.dp,
                onClick = {
                    ridePreferences = ridePreferences.copy(rideType = RideOptionType.SHARED_POOL)
                    activeMatchResult = null
                }
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AirportIconBadge(
                            icon = Icons.Default.Group,
                            size = 36.dp,
                            iconSize = 18.dp,
                            tint = if (isShared) RoyalBlue else TextSecondary,
                            background = if (isShared) Color(0xFFEBF2FE) else SurfaceVariantLight
                        )
                        if (isShared) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = RoyalBlue,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Shared Pool",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Share ride with verified co-travelers headed nearby. Fare split equally.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pricing condition: If no destination, display guidance text.
                    if (selectedDestination == null) {
                        Text(
                            text = "Enter destination for fare",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    } else {
                        val est = AirportPricingEngine.calculateFare(
                            distanceKm = selectedDestination!!.distanceKm,
                            durationMinutes = selectedDestination!!.estimatedDurationMinutes,
                            rideType = RideOptionType.SHARED_POOL,
                            passengerCount = ridePreferences.passengerCount
                        )
                        Column {
                            Text(
                                text = "Est. ₹${est.roundedFinalFare} / rider",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RoyalBlue
                                )
                            )
                            Text(
                                text = "Subject to route match",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                            )
                        }
                    }
                }
            }

            // Option B: SOLO RIDE CARD
            val isSolo = ridePreferences.rideType == RideOptionType.SOLO_RIDE
            RideMitraCard(
                modifier = Modifier.weight(1f),
                borderColor = if (isSolo) TealAccent else BorderLight,
                backgroundColor = if (isSolo) Color(0xFFF4FCFA) else CardBackground,
                elevation = if (isSolo) 3.dp else 1.dp,
                onClick = {
                    ridePreferences = ridePreferences.copy(rideType = RideOptionType.SOLO_RIDE)
                    activeMatchResult = null
                }
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AirportIconBadge(
                            icon = Icons.Default.DirectionsCar,
                            size = 36.dp,
                            iconSize = 18.dp,
                            tint = if (isSolo) TealAccent else TextSecondary,
                            background = if (isSolo) Color(0xFFE0F7F4) else SurfaceVariantLight
                        )
                        if (isSolo) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = TealAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Solo Ride",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Private dedicated airport cab with zero detour and direct highway transit.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pricing condition: If no destination, display guidance text.
                    if (selectedDestination == null) {
                        Text(
                            text = "Enter destination for fare",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    } else {
                        val est = AirportPricingEngine.calculateFare(
                            distanceKm = selectedDestination!!.distanceKm,
                            durationMinutes = selectedDestination!!.estimatedDurationMinutes,
                            rideType = RideOptionType.SOLO_RIDE,
                            passengerCount = 1
                        )
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Est. ₹${est.roundedFinalFare}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TealAccent
                                    )
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "₹${est.roundedOriginalFare}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted,
                                        fontSize = 11.sp,
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                                    )
                                )
                            }
                            Text(
                                text = "Includes 10% platform promo",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontSize = 10.sp,
                                    color = Color(0xFF0F8C7B),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 5. Trip Preferences (Passenger count, Luggage, Departure, Detour)
        if (selectedDestination != null) {
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Trip Preferences & Baggage",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Seat count selection (Rules: Capacity - 1)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Passengers Joining You",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Max RideMitra seats = Vehicle capacity - 1",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            listOf(1, 2).forEach { count ->
                                val isSelected = ridePreferences.passengerCount == count
                                Box(
                                    modifier = Modifier
                                        .padding(start = 6.dp)
                                        .clip(CircleShape)
                                        .background(if (isSelected) RoyalBlue else SurfaceVariantLight)
                                        .clickable {
                                            ridePreferences = ridePreferences.copy(passengerCount = count)
                                            activeMatchResult = null
                                        }
                                        .padding(horizontal = 14.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$count Seat${if (count > 1) "s" else ""}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) CardBackground else TextPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Luggage options
                    Text(
                        text = "Luggage Details",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        LuggageOption.entries.forEach { option ->
                            val isSelected = ridePreferences.luggage == option
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color(0xFFEFF6FF) else SurfaceVariantLight)
                                    .border(
                                        1.dp,
                                        if (isSelected) RoyalBlue else BorderLight,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        ridePreferences = ridePreferences.copy(luggage = option)
                                    }
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option.label,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isSelected) RoyalBlue else TextSecondary,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        fontSize = 11.sp
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    // Shared ride detour tolerance
                    if (ridePreferences.rideType == RideOptionType.SHARED_POOL) {
                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Maximum Detour Allowed",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "To drop off co-passengers",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                )
                            }

                            Row {
                                listOf(10, 15, 20).forEach { mins ->
                                    val isSelected = ridePreferences.maxDetourMinutes == mins
                                    Box(
                                        modifier = Modifier
                                            .padding(start = 6.dp)
                                            .clip(PillShape)
                                        .background(if (isSelected) TealAccent else SurfaceVariantLight)
                                        .clickable {
                                            ridePreferences = ridePreferences.copy(maxDetourMinutes = mins)
                                        }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Text(
                                            text = "$mins m",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = if (isSelected) PrimaryNavy else TextPrimary,
                                                fontWeight = FontWeight.Bold
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Action Button: "Find Matching Rides" (Calculates Match and Displays Results)
            RideMitraButton(
                text = if (activeMatchResult == null) "Find Matching Rides" else "Refresh Match",
                onClick = {
                    val currentFare = dynamicFare ?: return@RideMitraButton
                    if (ridePreferences.rideType == RideOptionType.SHARED_POOL) {
                        // Match with realistic airport pool co-passengers
                        activeMatchResult = RideMatchResult(
                            matchId = "RM-POOL-8821",
                            rideType = RideOptionType.SHARED_POOL,
                            destination = selectedDestination!!,
                            fareEstimate = currentFare,
                            detourMinutes = 8,
                            coPassengers = listOf(
                                CoPassenger(
                                    name = "Ananya Sharma",
                                    flightNumber = "6E-204",
                                    dropLocation = "Cyber Hub, DLF Phase 2",
                                    luggageSummary = "1 Trolley"
                                )
                            ),
                            driverVehicleInfo = DriverVehicleInfo(
                                driverName = "Suresh Verma",
                                rating = 4.88,
                                vehicleModel = "Maruti Suzuki Dzire (White)",
                                vehicleNumber = "DL 01 AB 9482",
                                vehicleTotalCapacity = 4,
                                allowedPassengerCapacity = 3 // Strictly capacity - 1
                            ),
                            departureTimeFormatted = "19:10",
                            pickupPillar = "Terminal 3, Pillar 14B"
                        )
                    } else {
                        // Solo ride with 10% platform-funded promotional discount
                        activeMatchResult = RideMatchResult(
                            matchId = "RM-SOLO-9143",
                            rideType = RideOptionType.SOLO_RIDE,
                            destination = selectedDestination!!,
                            fareEstimate = currentFare,
                            detourMinutes = 0,
                            coPassengers = emptyList(),
                            driverVehicleInfo = DriverVehicleInfo(
                                driverName = "Vikram Singh",
                                rating = 4.92,
                                vehicleModel = "Hyundai Aura (Silver)",
                                vehicleNumber = "DL 01 AZ 3301",
                                vehicleTotalCapacity = 4,
                                allowedPassengerCapacity = 3
                            ),
                            departureTimeFormatted = "19:05",
                            pickupPillar = "Terminal 3, Pillar 8A"
                        )
                    }
                    onActionClicked("Matching evaluated for ${selectedDestination!!.name}")
                },
                gradient = HeroTealGradient,
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
            )
        }

        // 7. MATCHING RESULTS & TRANSPARENT FARE BREAKDOWN (Appears ONLY after matching)
        AnimatedVisibility(
            visible = activeMatchResult != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            val result = activeMatchResult ?: return@AnimatedVisibility
            val fare = result.fareEstimate

            Column(modifier = Modifier.padding(top = 20.dp)) {
                // Section Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Match Results & Fare Receipt",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    StatusBadge(
                        text = if (result.rideType == RideOptionType.SHARED_POOL) "SHARED POOL MATCHED" else "SOLO RIDE CONFIRMED",
                        variant = if (result.rideType == RideOptionType.SHARED_POOL) BadgeVariant.ROYAL else BadgeVariant.TEAL
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Ride & Driver Details Card
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Co-passengers section (for Shared Pool)
                        if (result.rideType == RideOptionType.SHARED_POOL) {
                            Text(
                                text = "Matched Co-Passenger",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue
                                )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            result.coPassengers.forEach { coPassenger ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(SurfaceVariantLight)
                                        .padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(32.dp)
                                                .clip(CircleShape)
                                                .background(Color(0xFFEBF2FE)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Person,
                                                contentDescription = null,
                                                tint = RoyalBlue,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Column {
                                            Text(
                                                text = coPassenger.name,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextPrimary
                                                )
                                            )
                                            Text(
                                                text = "Drop: ${coPassenger.dropLocation}",
                                                style = MaterialTheme.typography.bodySmall.copy(
                                                    color = TextSecondary,
                                                    fontSize = 11.sp
                                                )
                                            )
                                        }
                                    }
                                    StatusBadge(
                                        text = "+${result.detourMinutes}m detour",
                                        variant = BadgeVariant.TEAL
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        // Driver & Vehicle Card
                        if (result.driverVehicleInfo != null) {
                            val driver = result.driverVehicleInfo
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
                                        tint = PrimaryNavy,
                                        background = SurfaceVariantLight
                                    )
                                    Spacer(modifier = Modifier.width(10.dp))
                                    Column {
                                        Text(
                                            text = driver.vehicleModel,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                        )
                                        Text(
                                            text = "Plate: ${driver.vehicleNumber} • Driver: ${driver.driverName} (${driver.rating}★)",
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondary
                                            )
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Strict Capacity Verification Tag
                            Text(
                                text = "Strict Capacity: Max ${driver.allowedPassengerCapacity} riders in ${driver.vehicleTotalCapacity}-seat cab (No overbooking)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = SuccessGreen,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Fare Itemized Breakdown
                        Text(
                            text = "Itemized Fare Breakdown (Estimate)",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        FareLineItem("Base Airport Flag-Down", "₹${fare.baseFare.toInt()}")
                        FareLineItem("Distance Charge (${fare.routeDistanceKm} km @ ₹16.5/km)", "₹${fare.distanceCharge.toInt()}")
                        FareLineItem("Time & Congestion (~${fare.routeDurationMinutes} mins)", "₹${fare.timeCharge.toInt()}")
                        FareLineItem("IGI Terminal Parking & Toll Fee", "₹${fare.airportTollFee.toInt()}")

                        if (result.rideType == RideOptionType.SOLO_RIDE && fare.discountAmount > 0) {
                            Spacer(modifier = Modifier.height(4.dp))
                            FareLineItem(
                                title = "10% Promotional Solo Discount (Platform Funded)",
                                amount = "- ₹${fare.roundedDiscountAmount}",
                                isDiscount = true
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(8.dp))

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
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = PrimaryNavy
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Mandatory 10% Advance Deposit Rule Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF0FDF4))
                                .border(1.dp, Color(0xFFBBF7D0), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "10% Advance Payment Required Now:",
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF166534)
                                        )
                                    )
                                    Text(
                                        text = "₹${fare.roundedAdvanceDeposit}",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF166534)
                                        )
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Remaining balance (₹${fare.roundedRemainingBalance}) payable directly to the driver after ride completion.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = Color(0xFF15803D),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Cancellation Policy Banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFFEF3C7))
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
                                        text = "Cancellation Refund Policy:",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF92400E)
                                        )
                                    )
                                    Text(
                                        text = "• Cancel > 1 hour before scheduled pickup: Eligible for 5% total ride refund (₹${fare.roundedCancellationRefund}).\n• Cancel within 1 hour of scheduled pickup: Non-refundable.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFFB45309),
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }

                        // Driver Payout Note for Solo Rides
                        if (result.rideType == RideOptionType.SOLO_RIDE) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "RideMitra funds your 10% promo discount entirely. Driver receives their full agreed payout of ₹${fare.roundedOriginalFare}.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = RoyalBlue,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Booking & Advance Payment CTA
                        if (!isBookingConfirmed) {
                            RideMitraButton(
                                text = "Pay 10% Advance (₹${fare.roundedAdvanceDeposit}) & Confirm",
                                onClick = {
                                    isBookingConfirmed = true
                                    onActionClicked("Booking Confirmed with 10% advance deposit paid (₹${fare.roundedAdvanceDeposit})")
                                },
                                gradient = PrimaryGradient
                            )
                        } else {
                            // Booking Confirmed Success Card
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFE6F9F2))
                                    .padding(14.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = SuccessGreen,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Booking Confirmed! (Trip #${result.matchId})",
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF0F766E)
                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "Please meet your driver at ${result.pickupPillar}. Trip start OTP will be shared upon vehicle arrival.",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = Color(0xFF0F766E),
                                            textAlign = TextAlign.Center
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 8. Platform Policy Card: Strict N-1 Capacity Rule
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
                        text = "RideMitra Fair Airport Rules",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "• Maximum passenger capacity = Vehicle capacity - 1 (Never overbooked).\n• 10% advance deposit required for booking; 90% balance payable at destination.\n• Solo discount is fully platform-funded and never penalizes driver earnings.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 9. Emergency SOS Access
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFFEF2F2))
                .clickable { onActionClicked("Emergency SOS triggered: Notifying Delhi Police & Airport Security") }
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
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
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "RideMitra Safety Shield",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = ErrorRed
                        )
                    )
                    Text(
                        text = "24/7 Police & Airport Authority Emergency SOS",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF991B1B)
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}

@Composable
private fun FareLineItem(
    title: String,
    amount: String,
    isDiscount: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
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
