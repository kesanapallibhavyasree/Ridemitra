package com.ridemitra.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.ui.theme.PillShape
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.WarningAmber

enum class BadgeVariant {
    TEAL,
    ROYAL,
    SUCCESS,
    WARNING,
    NEUTRAL
}

@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.TEAL,
    icon: ImageVector? = null
) {
    val (bgColor, contentColor) = when (variant) {
        BadgeVariant.TEAL -> Pair(Color(0xFFE0F7F4), Color(0xFF0F8C7B))
        BadgeVariant.ROYAL -> Pair(Color(0xFFEBF2FE), RoyalBlue)
        BadgeVariant.SUCCESS -> Pair(Color(0xFFE6F9F2), SuccessGreen)
        BadgeVariant.WARNING -> Pair(Color(0xFFFEF3C7), WarningAmber)
        BadgeVariant.NEUTRAL -> Pair(Color(0xFFF1F5F9), Color(0xFF475569))
    }

    Box(
        modifier = modifier
            .clip(PillShape)
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = contentColor
                )
            )
        }
    }
}
