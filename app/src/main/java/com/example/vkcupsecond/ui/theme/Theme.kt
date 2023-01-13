package com.example.vkcupsecond.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = MyColors(
    material = darkColors(
        primary = Purple200,
        primaryVariant = Purple700,
        secondary = Teal200,
    ),
    interviewHeaderColor = InterviewHeaderColorDark,
    answeredColor = AnsweredColorDark,
    correctColor = CorrectColorDark,
    incorrectColor = IncorrectColorDark,
    dzenColor = DzenColorDark,
)

private val LightColorPalette = MyColors(
    material = lightColors(
        primary = Purple500,
        primaryVariant = Purple700,
        secondary = Teal200,
    ),
    interviewHeaderColor = InterviewHeaderColor,
    answeredColor = AnsweredColorLight,
    correctColor = CorrectColorLight,
    incorrectColor = IncorrectColorLight,
    dzenColor = DzenColorLight,
)
val MaterialTheme.myColors: MyColors
    @Composable
    @ReadOnlyComposable
    get() = LocalColors.current
//@Composable
//fun VkCupSecondTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colorsMain = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
//
//    MaterialTheme(
//        colors = colorsMain,
//        typography = Typography,
//        shapes = Shapes,
//        content = content
//    )
//}

private val LocalColors = staticCompositionLocalOf { LightColorPalette }

@Composable
fun VkCupSecondTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    CompositionLocalProvider(LocalColors provides colors) {
        MaterialTheme(
            colors = colors.material,
            content = content,
        )
    }
}

data class MyColors(
    val material: Colors,
    val interviewHeaderColor: Color,
    val answeredColor:Color,
    val correctColor: Color,
    val incorrectColor:Color,
    val dzenColor: Color,
) {
    val primary: Color get() = material.primary
    val primaryVariant: Color get() = material.primaryVariant
    val secondary: Color get() = material.secondary
    val secondaryVariant: Color get() = material.secondaryVariant
    val background: Color get() = material.background
    val surface: Color get() = material.surface
    val error: Color get() = material.error
    val onPrimary: Color get() = material.onPrimary
    val onSecondary: Color get() = material.onSecondary
    val onBackground: Color get() = material.onBackground
    val onSurface: Color get() = material.onSurface
    val onError: Color get() = material.onError
    val isLight: Boolean get() = material.isLight
}