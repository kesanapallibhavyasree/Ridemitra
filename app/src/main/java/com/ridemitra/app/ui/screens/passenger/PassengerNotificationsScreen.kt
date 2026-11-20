package com.ridemitra.app.ui.screens.passenger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.NotificationType
import com.ridemitra.app.domain.model.PassengerNotification
import com.ridemitra.app.domain.model.samplePassengerNotifications
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary
import com.ridemitra.app.ui.theme.WarningAmber

/**
 * Screen 19: Notifications Screen.
 * Real-time notifications for ride matching, terminal gate updates, and airport safety alerts.
 */
@Composable
fun PassengerNotificationsScreen(
    notifications: List<PassengerNotification> = samplePassengerNotifications,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Notifications & Alerts",
            subtitle = "Flight sync & airport ride updates",
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(notifications, key = { it.id }) { notif ->
                val (icon, tint, bg) = when (notif.type) {
                    NotificationType.RIDE_MATCH -> Triple(Icons.Default.DirectionsCar, RoyalBlue, Color(0xFFDBEAFE))
                    NotificationType.FLIGHT_UPDATE -> Triple(Icons.Default.AirplanemodeActive, TealAccent, Color(0xFFE0F7F4))
                    NotificationType.SAFETY_ALERT -> Triple(Icons.Default.Shield, SuccessGreen, Color(0xFFDCFCE7))
                    NotificationType.PROMO -> Triple(Icons.Default.LocalOffer, WarningAmber, Color(0xFFFEF3C7))
                    else -> Triple(Icons.Default.Notifications, PrimaryNavy, SurfaceVariantLight)
                }

                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (!notif.isRead) RoyalBlue.copy(alpha = 0.4f) else BorderLight,
                    backgroundColor = if (!notif.isRead) Color(0xFFF8FAFF) else CardBackground,
                    elevation = if (!notif.isRead) 2.dp else 1.dp
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        AirportIconBadge(
                            icon = icon,
                            size = 38.dp,
                            iconSize = 20.dp,
                            tint = tint,
                            background = bg
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = notif.title,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrimaryNavy
                                    )
                                )
                                Text(
                                    text = notif.timestampFormatted,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextMuted,
                                        fontSize = 10.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = notif.message,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    lineHeight = 17.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
