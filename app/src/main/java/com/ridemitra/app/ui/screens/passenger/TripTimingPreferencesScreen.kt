package com.ridemitra.app.ui.screens.passenger

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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.ridemitra.app.domain.model.RidePreferences
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
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Screens 7, 8, 9: Trip Timing & Detour Preferences Screen.
 * Covers:
 * - Screen 7: Departure time selection ("Ready Now" vs "+15m Baggage Buffer" vs "Schedule for Later")
 * - Screen 8: Maximum waiting time tolerance (5m, 10m, 15m, 20m)
 * - Screen 9: Maximum detour preference (5m, 10m, 15m, 20m)
 */
@Composable
fun TripTimingPreferencesScreen(
    currentPreferences: RidePreferences,
    onSavePreferences: (RidePreferences) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var departureTiming by remember { mutableStateOf(currentPreferences.departureTiming) }
    var maxWaitingTime by remember { mutableIntStateOf(currentPreferences.maxWaitingMinutes) }
    var maxDetour by remember { mutableIntStateOf(currentPreferences.maxDetourMinutes) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Timing & Detour Tolerance",
            subtitle = "Customise pickup wait and drop detour limits",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Screen 7: Departure Timing Section
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AirportIconBadge(
                            icon = Icons.Default.Schedule,
                            size = 32.dp,
                            iconSize = 16.dp,
                            tint = RoyalBlue,
                            background = Color(0xFFDBEAFE)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "7. Departure Time",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val timingOptions = listOf(
                        "Ready Now (At Terminal Curb)",
                        "+15 Mins (Collecting Luggage)",
                        "+30 Mins (Flight Just Landed)"
                    )

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        timingOptions.forEach { option ->
                            val isChosen = departureTiming == option
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isChosen) Color(0xFFEFF6FF) else SurfaceVariantLight)
                                    .border(
                                        1.dp,
                                        if (isChosen) RoyalBlue else BorderLight,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { departureTiming = option }
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isChosen) RoyalBlue else TextPrimary
                                    )
                                )
                                if (isChosen) {
                                    StatusBadge(text = "ACTIVE", variant = BadgeVariant.ROYAL)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Screen 8: Maximum Waiting Time Section
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AirportIconBadge(
                            icon = Icons.Default.HourglassTop,
                            size = 32.dp,
                            iconSize = 16.dp,
                            tint = TealAccent,
                            background = Color(0xFFE0F7F4)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "8. Maximum Waiting Time",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "How long are you willing to wait at terminal for co-riders?",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 10, 15, 20).forEach { mins ->
                            val isSelected = maxWaitingTime == mins
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) TealAccent else SurfaceVariantLight)
                                    .border(
                                        1.dp,
                                        if (isSelected) TealAccent else BorderLight,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { maxWaitingTime = mins }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$mins m",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) PrimaryNavy else TextPrimary
                                        )
                                    )
                                    Text(
                                        text = if (mins <= 10) "Fastest" else "Max Matches",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) PrimaryNavy.copy(alpha = 0.8f) else TextMuted,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Screen 9: Maximum Detour Preference Section
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AirportIconBadge(
                            icon = Icons.Default.NearMe,
                            size = 32.dp,
                            iconSize = 16.dp,
                            tint = RoyalBlue,
                            background = Color(0xFFDBEAFE)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "9. Maximum Detour Preference",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "Max extra transit minutes to drop co-riders along your highway route.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 10, 15, 20).forEach { mins ->
                            val isSelected = maxDetour == mins
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) RoyalBlue else SurfaceVariantLight)
                                    .border(
                                        1.dp,
                                        if (isSelected) RoyalBlue else BorderLight,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { maxDetour = mins }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "+$mins m",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) CardBackground else TextPrimary
                                        )
                                    )
                                    Text(
                                        text = if (mins <= 10) "Direct" else "Economy",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isSelected) Color(0xFFDBEAFE) else TextMuted,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            RideMitraButton(
                text = "Proceed to Ride Category",
                onClick = {
                    onSavePreferences(
                        currentPreferences.copy(
                            departureTiming = departureTiming,
                            maxWaitingMinutes = maxWaitingTime,
                            maxDetourMinutes = maxDetour
                        )
                    )
                },
                gradient = PrimaryGradient,
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
