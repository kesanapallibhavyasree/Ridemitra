package com.ridemitra.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = RoyalBlue,
    onPrimary = CardBackground,
    primaryContainer = SurfaceVariantLight,
    onPrimaryContainer = PrimaryNavy,
    secondary = TealAccent,
    onSecondary = PrimaryNavy,
    secondaryContainer = Color(0xFFD7F8F3),
    onSecondaryContainer = PrimaryNavy,
    tertiary = WarningAmber,
    onTertiary = CardBackground,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = CardBackground,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderLight,
    outlineVariant = BorderLight,
    error = ErrorRed,
    onError = CardBackground
)

private val DarkColorScheme = darkColorScheme(
    primary = RoyalBlue,
    onPrimary = CardBackground,
    primaryContainer = DeepNavySurface,
    onPrimaryContainer = BackgroundLight,
    secondary = TealAccent,
    onSecondary = DarkNavy,
    tertiary = WarningAmber,
    onTertiary = DarkNavy,
    background = DarkNavy,
    onBackground = BackgroundLight,
    surface = DeepNavySurface,
    onSurface = BackgroundLight,
    surfaceVariant = PrimaryNavy,
    onSurfaceVariant = TextMuted,
    outline = Color(0xFF28486E),
    error = ErrorRed,
    onError = CardBackground
)

@Composable
fun RideMitraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = PrimaryNavy.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
