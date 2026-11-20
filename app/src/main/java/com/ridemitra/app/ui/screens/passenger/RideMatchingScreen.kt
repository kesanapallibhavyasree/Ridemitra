package com.ridemitra.app.ui.screens.passenger

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.Airport
import com.ridemitra.app.domain.model.Destination
import com.ridemitra.app.domain.model.RideOptionType
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
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary
import kotlinx.coroutines.delay

/**
 * Screen 11: Ride Matching Screen.
 * Displays interactive matching radar scanning for compatible passengers based on:
 * - Route proximity
 * - Baggage volume
 * - Maximum detour tolerance
 * - Strict N-1 seat capacity
 */
@Composable
fun RideMatchingScreen(
    airport: Airport,
    destination: Destination,
    rideType: RideOptionType,
    onMatchFound: () -> Unit,
    onCancelMatching: () -> Unit,
    modifier: Modifier = Modifier
) {
    var matchProgressStage by remember { mutableStateOf("Scanning airport arrival terminal...") }
    val radarPulse = remember { Animatable(0.2f) }

    LaunchedEffect(Unit) {
        radarPulse.animateTo(
            targetValue = 1.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(1500, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )
    }

    LaunchedEffect(Unit) {
        delay(1200)
        matchProgressStage = "Evaluating route proximity to ${destination.name}..."
        delay(1200)
        matchProgressStage = "Verifying vehicle N-1 seat capacity..."
        delay(1100)
        matchProgressStage = "Match evaluated successfully!"
        delay(500)
        onMatchFound()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "11. Evaluating Pool Matches",
            subtitle = "Connecting compatible co-passengers at ${airport.code}",
            onBackClick = onCancelMatching
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Journey details mini card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE),
                elevation = 2.dp
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
                            text = "From ${airport.name} (${airport.defaultTerminal})",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                    StatusBadge(
                        text = if (rideType == RideOptionType.SHARED_POOL) "SHARED POOL" else "SOLO RIDE",
                        variant = if (rideType == RideOptionType.SHARED_POOL) BadgeVariant.ROYAL else BadgeVariant.TEAL
                    )
                }
            }

            // Central Animated Radar & Pulsing Rings
            Box(
                modifier = Modifier
                    .size(240.dp),
                contentAlignment = Alignment.Center
            ) {
                // Expanding radar pulses
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val radius = (size.minDimension / 2) * radarPulse.value
                    drawCircle(
                        color = RoyalBlue.copy(alpha = (1.2f - radarPulse.value).coerceIn(0f, 0.4f)),
                        radius = radius,
                        style = Stroke(width = 4f)
                    )
                    drawCircle(
                        color = TealAccent.copy(alpha = (1.2f - radarPulse.value).coerceIn(0f, 0.3f)),
                        radius = radius * 0.7f,
                        style = Stroke(width = 3f)
                    )
                }

                // Inner steady ambient ring
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFDBEAFE)),
                    contentAlignment = Alignment.Center
                ) {
                    AirportIconBadge(
                        icon = if (rideType == RideOptionType.SHARED_POOL) Icons.Default.Group else Icons.Default.DirectionsCar,
                        size = 72.dp,
                        iconSize = 36.dp,
                        tint = RoyalBlue,
                        background = CardBackground
                    )
                }
            }

            // Status feedback & cancellation
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(28.dp),
                    color = RoyalBlue,
                    strokeWidth = 3.dp
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = matchProgressStage,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryNavy
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "If no compatible shared ride is available, RideMitra automatically offers the solo option with a guaranteed 10% platform promotional discount.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = TextSecondary,
                        fontSize = 11.sp,
                        lineHeight = 16.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                RideMitraButton(
                    text = "Cancel Search",
                    onClick = onCancelMatching,
                    gradient = PrimaryGradient
                )
            }
        }
    }
}
