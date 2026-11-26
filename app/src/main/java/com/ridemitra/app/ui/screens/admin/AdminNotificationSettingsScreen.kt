package com.ridemitra.app.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.*
import com.ridemitra.app.ui.theme.*

/**
 * Admin Notifications Screen - Phase 4
 */
@Composable
fun AdminNotificationsScreen(
    notifications: List<AdminNotification>,
    onMarkRead: (String) -> Unit,
    onMarkAllRead: () -> Unit
) {
    val unread = notifications.filter { !it.isRead }
    val read = notifications.filter { it.isRead }

    Column(modifier = Modifier.fillMaxSize()) {
        // Actions
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "${unread.size} unread · ${notifications.size} total",
                style = MaterialTheme.typography.labelMedium.copy(color = TextSecondary)
            )
            if (unread.isNotEmpty()) {
                TextButton(onClick = onMarkAllRead) {
                    Text("Mark All Read", color = RoyalBlue)
                }
            }
        }

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            if (unread.isNotEmpty()) {
                item {
                    SectionHeader("🔔 Unread (${unread.size})", null)
                }
                items(unread) { notif ->
                    NotificationCard(notif = notif, onMarkRead = onMarkRead)
                }
            }

            if (read.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    SectionHeader("✓ Read (${read.size})", null)
                }
                items(read) { notif ->
                    NotificationCard(notif = notif, onMarkRead = {})
                }
            }

            if (notifications.isEmpty()) {
                item { EmptyState("No notifications", "You're all caught up!") }
            }
        }
    }
}

@Composable
private fun NotificationCard(notif: AdminNotification, onMarkRead: (String) -> Unit) {
    val typeColor = when (notif.type) {
        AdminNotificationType.SAFETY_REPORT -> ErrorRed
        AdminNotificationType.NEW_COMPLAINT -> WarningAmber
        AdminNotificationType.DOCUMENT_EXPIRING -> Color(0xFFEA580C)
        AdminNotificationType.PAYMENT_ISSUE -> RoyalBlue
        AdminNotificationType.NEW_DRIVER_APPLICATION -> TealAccent
        AdminNotificationType.DOCUMENT_REVIEW_NEEDED -> PrimaryNavy
        AdminNotificationType.BOOKING_DISPUTE -> Color(0xFF7C3AED)
    }

    val typeIcon = when (notif.type) {
        AdminNotificationType.SAFETY_REPORT -> Icons.Default.Security
        AdminNotificationType.NEW_COMPLAINT -> Icons.Default.Report
        AdminNotificationType.DOCUMENT_EXPIRING -> Icons.Default.Warning
        AdminNotificationType.PAYMENT_ISSUE -> Icons.Default.AccountBalanceWallet
        AdminNotificationType.NEW_DRIVER_APPLICATION -> Icons.Default.Assignment
        AdminNotificationType.DOCUMENT_REVIEW_NEEDED -> Icons.Default.Description
        AdminNotificationType.BOOKING_DISPUTE -> Icons.Default.Gavel
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (!notif.isRead) Color(0xFFF0F7FF) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (!notif.isRead) 2.dp else 1.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Box(
                modifier = Modifier.size(38.dp).clip(RoundedCornerShape(10.dp)).background(typeColor.copy(0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(typeIcon, null, tint = typeColor, modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        notif.title,
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    if (!notif.isRead) {
                        Box(
                            modifier = Modifier.size(8.dp).clip(RoundedCornerShape(4.dp)).background(RoyalBlue)
                        )
                    }
                }
                Text(notif.message, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(notif.timestamp, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
                    if (!notif.isRead) {
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(
                            onClick = { onMarkRead(notif.notificationId) },
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                            modifier = Modifier.height(20.dp)
                        ) {
                            Text("Mark read", style = MaterialTheme.typography.labelSmall.copy(color = RoyalBlue, fontSize = 10.sp))
                        }
                    }
                }
            }
        }
    }
}

// ─── Admin Settings Screen ────────────────────────────────────────────────────

@Composable
fun AdminSettingsScreen(
    adminUser: AdminUser,
    onLogout: () -> Unit
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    var notifNewApplications by remember { mutableStateOf(true) }
    var notifComplaints by remember { mutableStateOf(true) }
    var notifDocExpiry by remember { mutableStateOf(true) }
    var notifSafety by remember { mutableStateOf(true) }
    var twoFactorEnabled by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        // Admin Profile Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(56.dp).clip(RoundedCornerShape(14.dp)).background(Color.White.copy(0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, null, tint = Color.White, modifier = Modifier.size(30.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(adminUser.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = Color.White))
                        Text(adminUser.role.label, style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(0.75f)))
                        Text(adminUser.email, style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.6f)))
                        Text("Last login: ${adminUser.lastLogin}", style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.5f), fontSize = 10.sp))
                    }
                }
            }
        }

        // Notification Preferences
        item { SettingsSectionTitle("Notification Preferences") }

        item {
            SettingsToggleItem("New Driver Applications", "Alert when new driver applications are submitted", notifNewApplications) { notifNewApplications = it }
        }
        item {
            SettingsToggleItem("Complaint Alerts", "Alert when new complaints are filed", notifComplaints) { notifComplaints = it }
        }
        item {
            SettingsToggleItem("Document Expiry Warnings", "Alert when driver documents are expiring", notifDocExpiry) { notifDocExpiry = it }
        }
        item {
            SettingsToggleItem("Safety Reports", "Immediate alert for safety concerns", notifSafety) { notifSafety = it }
        }

        // Security Settings
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { SettingsSectionTitle("Security Settings") }

        item {
            SettingsToggleItem("Two-Factor Authentication", "Require OTP verification for login (Backend Required)", twoFactorEnabled) { twoFactorEnabled = it }
        }
        item {
            SettingsNavigationItem("Change Password", "Update your admin portal password", Icons.Default.Lock) {}
        }
        item {
            SettingsNavigationItem("Login Activity", "Review recent admin login sessions", Icons.Default.History) {}
        }

        // Help & Support
        item { Spacer(modifier = Modifier.height(8.dp)) }
        item { SettingsSectionTitle("Help & Support") }
        item {
            SettingsNavigationItem("Admin Help Center", "Platform documentation and FAQs", Icons.Default.Help) {}
        }
        item {
            SettingsNavigationItem("Terms & Policies", "Terms of service and privacy policy", Icons.Default.Policy) {}
        }
        item {
            SettingsNavigationItem("Contact Support", "Get help from the RideMitra team", Icons.Default.ContactSupport) {}
        }

        // Logout
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
            ) {
                Icon(Icons.Default.Logout, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Logout from Admin Portal", fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "v4.0.0 · Phase 4 Admin Dashboard · RideMitra Platform",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp),
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to logout from the Admin Portal?") },
            confirmButton = {
                Button(
                    onClick = { showLogoutDialog = false; onLogout() },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed)
                ) { Text("Logout") }
            },
            dismissButton = { TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.labelMedium.copy(
            fontWeight = FontWeight.Bold,
            color = TextSecondary,
            fontSize = 11.sp
        ),
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    )
}

@Composable
private fun SettingsToggleItem(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(color = PrimaryNavy, fontWeight = FontWeight.Medium))
                Text(subtitle, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = RoyalBlue)
            )
        }
    }
}

@Composable
private fun SettingsNavigationItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 3.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, null, tint = PrimaryNavy, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(color = PrimaryNavy, fontWeight = FontWeight.Medium))
                Text(subtitle, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
            }
            Icon(Icons.Default.ChevronRight, null, tint = TextMuted, modifier = Modifier.size(18.dp))
        }
    }
}

