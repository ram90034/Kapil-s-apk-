package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MinimalPrimaryDark,
    onPrimary = Color(0xFF2D1600),
    secondary = MinimalSecondaryDark,
    onSecondary = Color(0xFF1E1A18),
    tertiary = MinimalTertiaryDark,
    background = MinimalBackgroundDark,
    onBackground = Color(0xFFFDF8F6),
    surface = MinimalSurfaceDark,
    onSurface = Color(0xFFFDF8F6),
    surfaceVariant = MinimalSurfaceVariantDark,
    onSurfaceVariant = Color(0xFFFFEFEB),
    outline = MinimalOutlineDark
)

private val LightColorScheme = lightColorScheme(
    primary = MinimalPrimaryLight,
    onPrimary = Color.White,
    secondary = MinimalSecondaryLight,
    onSecondary = Color.White,
    tertiary = MinimalTertiaryLight,
    background = MinimalBackgroundLight,
    onBackground = Color(0xFF1D1B1A),
    surface = MinimalSurfaceLight,
    onSurface = Color(0xFF1D1B1A),
    surfaceVariant = MinimalSurfaceVariantLight,
    onSurfaceVariant = Color(0xFF2D1600),
    outline = MinimalOutlineLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
