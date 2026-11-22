package com.example.gharsa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF00253E)
val PrimaryContainer = Color(0xFF173B57)
val OnPrimary = Color(0xFFFFFFFF)
val OnPrimaryContainer = Color(0xFF84A5C6)

val Secondary = Color(0xFF006A63)
val SecondaryContainer = Color(0xFF80F6EA)
val OnSecondary = Color(0xFFFFFFFF)
val OnSecondaryContainer = Color(0xFF00716A)

val Background = Color(0xFFF9F9FF)
val OnBackground = Color(0xFF111C2D)
val Surface = Color(0xFFF9F9FF)
val SurfaceContainer = Color(0xFFE7EEFF)
val SurfaceContainerLow = Color(0xFFF0F3FF)
val SurfaceContainerLowest = Color(0xFFFFFFFF)
val OnSurface = Color(0xFF111C2D)
val OnSurfaceVariant = Color(0xFF42474D)
val Outline = Color(0xFF73777E)

val Error = Color(0xFFBA1A1A)
val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFDAD6)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceContainer = SurfaceContainer,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainerLowest = SurfaceContainerLowest,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer
)

@Composable
fun GharSaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
