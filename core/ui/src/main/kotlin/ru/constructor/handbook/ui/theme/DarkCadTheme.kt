package ru.constructor.handbook.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Immutable
public object DarkCadColors {
    public val canvas: Color = Color(0xFF0D1117)
    public val surface: Color = Color(0xFF151B23)
    public val elevated: Color = Color(0xFF1C2430)
    public val linePrimary: Color = Color(0xFF344252)
    public val grid: Color = Color(0xFF22303D)
    public val textPrimary: Color = Color(0xFFE6EDF3)
    public val textSecondary: Color = Color(0xFF9EABB8)
    public val accentBlue: Color = Color(0xFF42A5F5)
    public val accentCyan: Color = Color(0xFF5BC0EB)
    public val warning: Color = Color(0xFFFFB74D)
    public val error: Color = Color(0xFFEF5350)
    public val success: Color = Color(0xFF66BB6A)
}

@Immutable
public object DarkCadSpacing {
    public val grid = 4.dp
    public val small = 8.dp
    public val medium = 12.dp
    public val standard = 16.dp
    public val large = 24.dp
    public val extraLarge = 32.dp
    public val minimumTouchTarget = 48.dp
}

private val DarkCadColorScheme = darkColorScheme(
    primary = DarkCadColors.accentBlue,
    secondary = DarkCadColors.accentCyan,
    background = DarkCadColors.canvas,
    surface = DarkCadColors.surface,
    surfaceVariant = DarkCadColors.elevated,
    outline = DarkCadColors.linePrimary,
    onPrimary = DarkCadColors.canvas,
    onBackground = DarkCadColors.textPrimary,
    onSurface = DarkCadColors.textPrimary,
    onSurfaceVariant = DarkCadColors.textSecondary,
    error = DarkCadColors.error,
)

private val DarkCadShapes = Shapes(
    extraSmall = RoundedCornerShape(6.dp),
    small = RoundedCornerShape(6.dp),
    medium = RoundedCornerShape(8.dp),
    large = RoundedCornerShape(8.dp),
)

@Composable
public fun DarkCadTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkCadColorScheme,
        typography = Typography(),
        shapes = DarkCadShapes,
        content = content,
    )
}
