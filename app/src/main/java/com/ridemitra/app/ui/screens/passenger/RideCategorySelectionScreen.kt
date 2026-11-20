package com.ridemitra.app.ui.screens.passenger

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.Airport
import com.ridemitra.app.domain.model.Destination
import com.ridemitra.app.domain.model.RideOptionType
import com.ridemitra.app.domain.model.RidePreferences
import com.ridemitra.app.domain.pricing.AirportPricingEngine
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.HeroTealGradient
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Screen 10: Shared Pool & Solo Ride Selection Screen.
 * Displays calculated fares based on selected destination and enforces business rules:
 * - Shared Pool: split fare, capacity threshold N-1.
 * - Solo Ride: 10% platform promotional discount, driver receives 100% full payout.
 */
@Composable
fun RideCategorySelectionScreen(
    airport: Airport,
    destination: Destination,
    preferences: RidePreferences,
    onCategorySelected: (RideOptionType) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedType by remember { mutableStateOf(preferences.rideType) }
    val scrollState = rememberScrollState()

    val poolEstimate = remember(destination, preferences.passengerCount) {
        AirportPricingEngine.calculateFare(
            distanceKm = destination.distanceKm,
            durationMinutes = destination.estimatedDurationMinutes,
            rideType = RideOptionType.SHARED_POOL,
            passengerCount = preferences.passengerCount,
            airportTollFee = airport.baseTollFee
        )
    }

    val soloEstimate = remember(destination) {
        AirportPricingEngine.calculateFare(
            distanceKm = destination.distanceKm,
            durationMinutes = destination.estimatedDurationMinutes,
            rideType = RideOptionType.SOLO_RIDE,
            passengerCount = 1,
            airportTollFee = airport.baseTollFee
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "10. Select Ride Category",
            subtitle = "From ${airport.code} to ${destination.name}",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Journey Summary Pill
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE),
                elevation = 1.dp
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Destination: ${destination.name}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Text(
                            text = "${destination.distanceKm} km • ~${destination.estimatedDurationMinutes} mins • ${preferences.passengerCount} Seat(s)",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = RoyalBlue,
                                fontSize = 11.sp
                            )
                        )
                    }
                    StatusBadge(text = airport.code, variant = BadgeVariant.ROYAL)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CATEGORY 1: SHARED POOL
            val isPoolChosen = selectedType == RideOptionType.SHARED_POOL
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (isPoolChosen) RoyalBlue else BorderLight,
                backgroundColor = if (isPoolChosen) Color(0xFFF8FAFF) else CardBackground,
                elevation = if (isPoolChosen) 4.dp else 1.dp,
                onClick = { selectedType = RideOptionType.SHARED_POOL }
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AirportIconBadge(
                                icon = Icons.Default.Group,
                                size = 44.dp,
                                iconSize = 22.dp,
                                tint = if (isPoolChosen) RoyalBlue else TextSecondary,
                                background = if (isPoolChosen) Color(0xFFDBEAFE) else SurfaceVariantLight
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Shared Airport Pool",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Split taxi fare with verified flyers",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        if (isPoolChosen) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = RoyalBlue,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Estimated Fare:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted
                                )
                            )
                            Text(
                                text = "₹${poolEstimate.roundedFinalFare}",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = RoyalBlue
                                )
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            StatusBadge(
                                text = "10% Advance: ₹${poolEstimate.roundedAdvanceDeposit}",
                                variant = BadgeVariant.ROYAL
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Balance ₹${poolEstimate.roundedRemainingBalance} at drop",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "• Route similarity and luggage compatibility enforced.\n• Maximum passenger capacity strictly vehicle capacity - 1.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // CATEGORY 2: SOLO RIDE (WITH 10% PLATFORM PROMO)
            val isSoloChosen = selectedType == RideOptionType.SOLO_RIDE
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                borderColor = if (isSoloChosen) TealAccent else BorderLight,
                backgroundColor = if (isSoloChosen) Color(0xFFF4FCFA) else CardBackground,
                elevation = if (isSoloChosen) 4.dp else 1.dp,
                onClick = { selectedType = RideOptionType.SOLO_RIDE }
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
                                size = 44.dp,
                                iconSize = 22.dp,
                                tint = if (isSoloChosen) TealAccent else TextSecondary,
                                background = if (isSoloChosen) Color(0xFFE0F7F4) else SurfaceVariantLight
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Dedicated Solo Ride",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextPrimary
                                    )
                                )
                                Text(
                                    text = "Private dedicated airport cab with zero detour",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }

                        if (isSoloChosen) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Selected",
                                tint = TealAccent,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Estimated Fare:",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted
                                )
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "₹${soloEstimate.roundedFinalFare}",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TealAccent
                                    )
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "₹${soloEstimate.roundedOriginalFare}",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = TextMuted,
                                        textDecoration = TextDecoration.LineThrough
                                    )
                                )
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            StatusBadge(
                                text = "10% Platform Promo",
                                variant = BadgeVariant.TEAL
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Save ₹${soloEstimate.roundedDiscountAmount}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = Color(0xFF0F8C7B),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Driver payout protection assurance
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE0F7F4))
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = Color(0xFF0F8C7B),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Driver receives 100% agreed payout (₹${soloEstimate.roundedOriginalFare}). Promo is platform-funded.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF0F8C7B),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            RideMitraButton(
                text = if (selectedType == RideOptionType.SHARED_POOL) "Find Carpool Matches" else "Proceed with Solo Ride",
                onClick = { onCategorySelected(selectedType) },
                gradient = if (selectedType == RideOptionType.SHARED_POOL) PrimaryGradient else HeroTealGradient,
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
