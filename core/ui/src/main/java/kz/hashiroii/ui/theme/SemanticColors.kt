package kz.hashiroii.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ColorScheme.incomeColor: Color
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFF81C784) else Color(0xFF2E7D32)

val ColorScheme.expenseColor: Color
    @Composable get() = if (isSystemInDarkTheme()) Color(0xFFEF9A9A) else Color(0xFFC62828)