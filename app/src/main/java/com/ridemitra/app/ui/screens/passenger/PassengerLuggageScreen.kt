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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.LuggageOption
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
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Screen 6: Passenger & Luggage Details Screen.
 * Strictly enforces RideMitra's N-1 seat capacity rule:
 * Actual passenger threshold = actual passenger capacity excluding driver - 1.
 * 4 passenger seats = max 3 RideMitra passengers.
 * 7 passenger seats = max 6 RideMitra passengers.
 */
@Composable
fun PassengerLuggageScreen(
    currentPreferences: RidePreferences,
    onSavePreferences: (RidePreferences) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var passengerCount by remember { mutableIntStateOf(currentPreferences.passengerCount) }
    var luggageOption by remember { mutableStateOf(currentPreferences.luggage) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Passengers & Luggage",
            subtitle = "Strict comfort rules: Guaranteed seat reservation",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Strict N-1 Capacity Rule Banner
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0),
                elevation = 2.dp
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    AirportIconBadge(
                        icon = Icons.Default.Shield,
                        size = 36.dp,
                        iconSize = 18.dp,
                        tint = SuccessGreen,
                        background = Color(0xFFDCFCE7)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Guaranteed N-1 Seat Policy",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "One passenger seat always remains empty to ensure airport comfort.\n• 4-seat cab = Maximum 3 passengers\n• 7-seat cab = Maximum 6 passengers\nOverbooking is strictly prohibited.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF15803D),
                                lineHeight = 17.sp,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Passenger count selection
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Number of Passengers Traveling",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Include yourself and any accompanying travelers in your party.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(1, 2, 3).forEach { count ->
                            val isSelected = passengerCount == count
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) RoyalBlue else SurfaceVariantLight)
                                    .border(
                                        1.dp,
                                        if (isSelected) RoyalBlue else BorderLight,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { passengerCount = count }
                                    .padding(vertical = 12.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        imageVector = if (count == 1) Icons.Default.Person else Icons.Default.Group,
                                        contentDescription = null,
                                        tint = if (isSelected) CardBackground else PrimaryNavy,
                                        modifier = Modifier.size(24.dp)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = "$count ${if (count == 1) "Rider" else "Riders"}",
                                        style = MaterialTheme.typography.labelLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) CardBackground else TextPrimary
                                        )
                                    )
                                    Text(
                                        text = if (count <= 2) "Pool Eligible" else "Max Pool Cap",
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

            Spacer(modifier = Modifier.height(16.dp))

            // Luggage Selection
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Luggage & Baggage Size",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Helps match with vehicles having appropriate boot space capacity.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        LuggageOption.entries.forEach { option ->
                            val isSelected = luggageOption == option
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSelected) Color(0xFFEFF6FF) else CardBackground)
                                    .border(
                                        1.dp,
                                        if (isSelected) RoyalBlue else BorderLight,
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { luggageOption = option }
                                    .padding(horizontal = 14.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    AirportIconBadge(
                                        icon = Icons.Default.Luggage,
                                        size = 36.dp,
                                        iconSize = 18.dp,
                                        tint = if (isSelected) RoyalBlue else TextSecondary,
                                        background = if (isSelected) Color(0xFFDBEAFE) else SurfaceVariantLight
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = option.label,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) RoyalBlue else TextPrimary
                                            )
                                        )
                                        Text(
                                            text = when (option) {
                                                LuggageOption.HANDBAG_ONLY -> "Cabin backpack or small laptop bag"
                                                LuggageOption.ONE_TROLLEY -> "1 medium check-in or standard cabin bag"
                                                LuggageOption.TWO_OR_MORE -> "2+ large suitcases (requires full boot)"
                                            },
                                            style = MaterialTheme.typography.bodySmall.copy(
                                                color = TextSecondary,
                                                fontSize = 11.sp
                                            )
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Selected",
                                        tint = RoyalBlue,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            RideMitraButton(
                text = "Continue to Timing Preferences",
                onClick = {
                    onSavePreferences(
                        currentPreferences.copy(
                            passengerCount = passengerCount,
                            luggage = luggageOption
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
