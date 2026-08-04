package ru.constructor.handbook.ui.theme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
object DarkCadColors { val Canvas=Color(0xFF0D1117); val Surface=Color(0xFF151B23); val Elevated=Color(0xFF1C2430); val LinePrimary=Color(0xFF344252); val Grid=Color(0xFF22303D); val TextPrimary=Color(0xFFE6EDF3); val TextSecondary=Color(0xFF9EABB8); val Blue=Color(0xFF42A5F5); val Cyan=Color(0xFF5BC0EB); val Warning=Color(0xFFFFB74D); val Error=Color(0xFFEF5350); val Success=Color(0xFF66BB6A) }
private val Scheme=darkColorScheme(primary=DarkCadColors.Blue, secondary=DarkCadColors.Cyan, background=DarkCadColors.Canvas, surface=DarkCadColors.Surface, surfaceVariant=DarkCadColors.Elevated, outline=DarkCadColors.LinePrimary, onPrimary=DarkCadColors.Canvas, onBackground=DarkCadColors.TextPrimary, onSurface=DarkCadColors.TextPrimary, onSurfaceVariant=DarkCadColors.TextSecondary, error=DarkCadColors.Error)
@Composable fun DarkCadTheme(content: @Composable () -> Unit) { MaterialTheme(colorScheme=Scheme, content=content) }
