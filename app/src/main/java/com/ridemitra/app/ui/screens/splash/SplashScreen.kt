package com.ridemitra.app.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.R
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.DarkNavy
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.TealAccent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Animation States
    val logoScale = remember { Animatable(0.5f) }
    val logoAlpha = remember { Animatable(0f) }
    val contentAlpha = remember { Animatable(0f) }
    val contentTranslationY = remember { Animatable(40f) }
    val pulseScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Logo entrance
        launch {
            logoAlpha.animateTo(1f, animationSpec = tween(600, easing = FastOutSlowInEasing))
        }
        launch {
            logoScale.animateTo(
                1f,
                animationSpec = tween(700, easing = FastOutSlowInEasing)
            )
        }

        // Subtitle & Tagline entrance
        delay(350)
        launch {
            contentAlpha.animateTo(1f, animationSpec = tween(500))
        }
        launch {
            contentTranslationY.animateTo(0f, animationSpec = tween(500, easing = FastOutSlowInEasing))
        }

        // Continuous subtle pulse on glow ring
        launch {
            pulseScale.animateTo(
                targetValue = 1.12f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
        }

        // Auto transition after 2.6 seconds
        delay(2300)
        onNavigateNext()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PrimaryNavy,
                        DarkNavy
                    )
                )
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onNavigateNext
            ),
        contentAlignment = Alignment.Center
    ) {
        // Background Decorative Ambient Glows
        Box(
            modifier = Modifier
                .size(280.dp)
                .scale(pulseScale.value)
                .clip(CircleShape)
                .background(RoyalBlue.copy(alpha = 0.12f))
        )
        Box(
            modifier = Modifier
                .size(190.dp)
                .clip(CircleShape)
                .background(TealAccent.copy(alpha = 0.10f))
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            // Animated Brand Logo Container
            Box(
                modifier = Modifier
                    .scale(logoScale.value)
                    .alpha(logoAlpha.value)
                    .size(120.dp),
                contentAlignment = Alignment.Center
            ) {
                // Outer subtle border ring
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0x1A276EF1))
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_ridemitra_logo),
                    contentDescription = "RideMitra Brand Emblem",
                    modifier = Modifier.size(108.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Brand Title & Tagline with staggered entry
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .alpha(contentAlpha.value)
                    .offset(y = contentTranslationY.value.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "RideMitra",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 36.sp,
                            color = CardBackground,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    // Teal accent dot
                    Box(
                        modifier = Modifier
                            .padding(start = 4.dp, bottom = 4.dp)
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(TealAccent)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Tagline
                Text(
                    text = "Land Together. Ride Together. Save Together.",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Color(0xFFD4E3F5),
                        letterSpacing = 0.2.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Sub-category pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color(0x2620BFA9))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "Intelligent Airport Carpooling",
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = TealAccent,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 0.5.sp
                        )
                    )
                }
            }
        }

        // Bottom Loading & Tap to Skip indicator
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .alpha(contentAlpha.value),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = TealAccent,
                strokeWidth = 2.5.dp
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tap anywhere to continue",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = Color(0x80FFFFFF),
                    fontSize = 12.sp
                )
            )
        }
    }
}
