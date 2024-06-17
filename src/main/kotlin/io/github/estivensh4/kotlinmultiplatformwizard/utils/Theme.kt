package io.github.estivensh4.kotlinmultiplatformwizard.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import io.github.estivensh4.kotlinmultiplatformwizard.utils.SwingColor

@Composable
fun Theme(content: @Composable () -> Unit) {
    val colors = if (isSystemInDarkTheme()) darkColors() else lightColors()
    val swingColor = SwingColor()
    MaterialTheme(
        colors = colors.copy(
            background = swingColor.background,
            onBackground = swingColor.onBackground,
            surface = swingColor.background,
            onSurface = swingColor.onBackground,
        ),
        content = content
    )
}