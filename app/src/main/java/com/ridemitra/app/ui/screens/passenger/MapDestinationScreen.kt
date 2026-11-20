package com.ridemitra.app.ui.screens.passenger

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NearMe
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.Airport
import com.ridemitra.app.domain.model.Destination
import com.ridemitra.app.domain.model.getDestinationsForAirport
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
 * Screen 5: Map & Destination Selection Screen.
 * Visual vector route presentation from Airport terminal curb to drop zone.
 */
@Composable
fun MapDestinationScreen(
    airport: Airport,
    initialDestination: Destination?,
    onConfirmDestination: (Destination) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val airportDestinations = remember(airport) { getDestinationsForAirport(airport.code) }
    var selectedDest by remember {
        mutableStateOf(initialDestination ?: airportDestinations.first())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Airport Route Preview",
            subtitle = "From ${airport.code} Terminal to Drop Point",
            onBackClick = onBackClick
        )

        // Visual Map Canvas representation
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFFE2E8F0))
        ) {
            // Simulated stylized vector route canvas
            Canvas(modifier = Modifier.fillMaxSize()) {
                val startX = size.width * 0.25f
                val startY = size.height * 0.75f
                val endX = size.width * 0.75f
                val endY = size.height * 0.25f

                // Draw background grid lines
                val gridColor = Color(0xFFCBD5E1)
                for (i in 0..10) {
                    val y = size.height * (i / 10f)
                    drawLine(gridColor, Offset(0f, y), Offset(size.width, y), strokeWidth = 1f)
                }

                // Highway corridor curve
                val corridorPath = androidx.compose.ui.graphics.Path().apply {
                    moveTo(startX, startY)
                    cubicTo(
                        startX + 40f, startY - 120f,
                        endX - 40f, endY + 120f,
                        endX, endY
                    )
                }

                // Road outline
                drawPath(
                    path = corridorPath,
                    color = Color(0xFF94A3B8),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 14f)
                )

                // Active Transit Route
                drawPath(
                    path = corridorPath,
                    color = RoyalBlue,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 8f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(24f, 12f), 0f)
                    )
                )

                // Start Marker (Airport)
                drawCircle(color = PrimaryNavy, radius = 14f, center = Offset(startX, startY))
                drawCircle(color = TealAccent, radius = 8f, center = Offset(startX, startY))

                // End Marker (Destination)
                drawCircle(color = Color(0xFFEF4444), radius = 16f, center = Offset(endX, endY))
                drawCircle(color = Color.White, radius = 7f, center = Offset(endX, endY))
            }

            // Top Floating Route Overlay Tag
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color(0xEE142B45))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = TealAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Express Corridor: ${selectedDest.distanceKm} km • ~${selectedDest.estimatedDurationMinutes} mins",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = CardBackground,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Bottom Details Card
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            elevation = 6.dp
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Pickup Location",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = "${airport.name} (${airport.code})",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                    }
                    StatusBadge(text = airport.defaultTerminal, variant = BadgeVariant.TEAL)
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Drop Destination",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextMuted,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                        Text(
                            text = selectedDest.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue
                            )
                        )
                        Text(
                            text = selectedDest.locality,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }

                    StatusBadge(
                        text = "${selectedDest.distanceKm} km",
                        variant = BadgeVariant.ROYAL
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Alternative Nearby Hubs Chips
                Text(
                    text = "Quick Switch Drop Point:",
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextMuted,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    airportDestinations.take(3).forEach { d ->
                        val isChosen = selectedDest.name == d.name
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isChosen) RoyalBlue else SurfaceVariantLight)
                                .clickable { selectedDest = d }
                                .padding(vertical = 6.dp, horizontal = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = d.name.split(",")[0],
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isChosen) CardBackground else TextPrimary,
                                    fontSize = 10.sp
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                RideMitraButton(
                    text = "Confirm Destination (${selectedDest.distanceKm} km)",
                    onClick = { onConfirmDestination(selectedDest) },
                    gradient = HeroTealGradient,
                    trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                )
            }
        }
    }
}
