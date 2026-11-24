package com.ridemitra.app.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Core Brand Palette
val PrimaryNavy = Color(0xFF142B45)
val RoyalBlue = Color(0xFF276EF1)
val TealAccent = Color(0xFF20BFA9)
val BackgroundLight = Color(0xFFF5F7FA)
val CardBackground = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF172333)

// Secondary & Auxiliary Tones
val TextSecondary = Color(0xFF5A6E85)
val TextMuted = Color(0xFF8C9BAE)
val BorderLight = Color(0xFFE2E8F0)
val DividerLight = Color(0xFFE2E8F0)
val SurfaceVariantLight = Color(0xFFEEF2F6)

// Functional & State Tones
val SuccessGreen = Color(0xFF10B981)
val WarningAmber = Color(0xFFF59E0B)
val ErrorRed = Color(0xFFEF4444)
val InfoSky = Color(0xFF0EA5E9)

// Luxury Dark Accents (used in Splash & Highlights)
val DarkNavy = Color(0xFF0C1827)
val DeepNavySurface = Color(0xFF19324E)

// Gradients
val PrimaryGradient = Brush.linearGradient(
    colors = listOf(PrimaryNavy, RoyalBlue)
)

val HeroTealGradient = Brush.linearGradient(
    colors = listOf(RoyalBlue, TealAccent)
)

val CardGlowGradient = Brush.verticalGradient(
    colors = listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC))
)

val AccentGoldGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFFFB020), Color(0xFFF59E0B))
)
