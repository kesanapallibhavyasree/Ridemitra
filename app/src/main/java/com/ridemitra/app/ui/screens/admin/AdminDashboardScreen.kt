package com.ridemitra.app.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.*
import com.ridemitra.app.ui.theme.*

/**
 * Admin Dashboard Home Screen - Phase 4
 * Shows summary cards, activity feed, pending approvals, and quick actions.
 * All statistics are DEMO DATA until real backend integration.
 */
@Composable
fun AdminDashboardScreen(
    stats: AdminDashboardStats,
    notifications: List<AdminNotification>,
    recentBookings: List<AdminBookingSummary>,
    pendingApplications: List<AdminDriverApplicationSummary>,
    recentComplaints: List<AdminComplaint>,
    onNavigateToDriverApps: () -> Unit,
    onNavigateToPassengers: () -> Unit,
    onNavigateToBookings: () -> Unit,
    onNavigateToComplaints: () -> Unit,
    onNavigateToAnalytics: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // Demo Data Banner
        item {
            DemoBanner()
        }

        // Today's Snapshot
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader("📊 Platform Overview", "Live figures require backend integration")
        }

        // Summary Cards Row 1
        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    StatCard(
                        title = "Total Passengers",
                        value = stats.totalPassengers.toString(),
                        icon = Icons.Default.People,
                        color = RoyalBlue,
                        subText = "+${stats.newPassengersToday} today"
                    )
                }
                item {
                    StatCard(
                        title = "Registered Drivers",
                        value = stats.totalDrivers.toString(),
                        icon = Icons.Default.DirectionsCar,
                        color = TealAccent,
                        subText = "${stats.approvedDrivers} approved"
                    )
                }
                item {
                    StatCard(
                        title = "Pending Applications",
                        value = stats.pendingApplications.toString(),
                        icon = Icons.Default.PendingActions,
                        color = WarningAmber,
                        subText = "Requires review",
                        isAlert = stats.pendingApplications > 20
                    )
                }
                item {
                    StatCard(
                        title = "Approved Drivers",
                        value = stats.approvedDrivers.toString(),
                        icon = Icons.Default.Verified,
                        color = SuccessGreen,
                        subText = "Verified & eligible"
                    )
                }
                item {
                    StatCard(
                        title = "Active Rides",
                        value = stats.activeRides.toString(),
                        icon = Icons.Default.NearMe,
                        color = SuccessGreen,
                        subText = "Currently ongoing"
                    )
                }
            }
        }

        // Summary Cards Row 2
        item {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    StatCard(
                        title = "Completed Today",
                        value = stats.completedRidesToday.toString(),
                        icon = Icons.Default.CheckCircle,
                        color = SuccessGreen,
                        subText = "Rides completed"
                    )
                }
                item {
                    StatCard(
                        title = "Cancelled Today",
                        value = stats.cancelledRidesToday.toString(),
                        icon = Icons.Default.Cancel,
                        color = ErrorRed,
                        subText = "Rides cancelled"
                    )
                }
                item {
                    StatCard(
                        title = "Revenue Today",
                        value = "₹${"%,.0f".format(stats.platformRevenueToday)}",
                        icon = Icons.Default.AccountBalanceWallet,
                        color = PrimaryNavy,
                        subText = "Platform commission"
                    )
                }
                item {
                    StatCard(
                        title = "Month Revenue",
                        value = "₹${"%,.0f".format(stats.platformRevenueMonth)}",
                        icon = Icons.Default.BarChart,
                        color = RoyalBlue,
                        subText = "Total month"
                    )
                }
            }
        }

        // Quick Actions
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader("⚡ Quick Actions", null)
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionChip(
                    "Review Drivers",
                    Icons.Default.Assignment,
                    onClick = onNavigateToDriverApps,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    "Complaints",
                    Icons.Default.Report,
                    onClick = onNavigateToComplaints,
                    modifier = Modifier.weight(1f)
                )
                QuickActionChip(
                    "Analytics",
                    Icons.Default.BarChart,
                    onClick = onNavigateToAnalytics,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Today's Activity Summary
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader("📈 Today's Activity Summary", "Operations at a glance")
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        LabelValue("New Passengers", "+${stats.newPassengersToday}")
                        LabelValue("Driver Apps", "+${stats.newDriverApplicationsToday}")
                        LabelValue("Completed Trips", "${stats.completedRidesToday}")
                        LabelValue("Cancelled Trips", "${stats.cancelledRidesToday}")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Active airport dispatches: 18 ongoing", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                        Text("Fleet occupancy: 82%", style = MaterialTheme.typography.labelSmall.copy(color = SuccessGreen, fontWeight = FontWeight.Bold, fontSize = 11.sp))
                    }
                }
            }
        }

        // Pending Driver Approvals
        if (pendingApplications.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                SectionHeader("🕐 Pending Driver Approvals", "${pendingApplications.size} awaiting review")
            }

            items(pendingApplications.take(3)) { app ->
                PendingApplicationCard(app = app)
            }

            if (pendingApplications.size > 3) {
                item {
                    TextButton(
                        onClick = onNavigateToDriverApps,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        Text(
                            "View all ${pendingApplications.size} pending applications →",
                            color = RoyalBlue
                        )
                    }
                }
            }
        }

        // Recent Bookings
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("🚗 Recent Bookings", "Last 24 hours")
        }

        if (recentBookings.isEmpty()) {
            item { EmptyState("No bookings found", "Recent bookings will appear here") }
        } else {
            items(recentBookings.take(3)) { booking ->
                RecentBookingCard(booking = booking)
            }
            item {
                TextButton(
                    onClick = onNavigateToBookings,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("View all bookings →", color = RoyalBlue)
                }
            }
        }

        // Recent Complaints
        item {
            Spacer(modifier = Modifier.height(8.dp))
            SectionHeader("⚠️ Recent Complaints", "Requires attention")
        }

        if (recentComplaints.isEmpty()) {
            item { EmptyState("No active complaints", "Open complaints will appear here") }
        } else {
            items(recentComplaints.take(2)) { complaint ->
                ComplaintPreviewCard(complaint = complaint)
            }
            item {
                TextButton(
                    onClick = onNavigateToComplaints,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text("View all complaints →", color = RoyalBlue)
                }
            }
        }

        // Notifications Preview
        val unreadNotifs = notifications.filter { !it.isRead }
        if (unreadNotifs.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                SectionHeader("🔔 Unread Notifications", "${unreadNotifs.size} unread")
            }
            items(unreadNotifs.take(2)) { notif ->
                NotificationPreviewCard(notif = notif)
            }
        }

        item { Spacer(modifier = Modifier.height(80.dp)) }
    }
}

@Composable
fun DemoBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF142B45))
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Info, null, tint = WarningAmber, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "⚠ DEMO DATA – All statistics are demonstration figures. Connect backend for live data.",
            style = MaterialTheme.typography.labelSmall.copy(
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun SectionHeader(title: String, subtitle: String?) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )
        )
        subtitle?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary)
            )
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    subText: String,
    isAlert: Boolean = false
) {
    Card(
        modifier = Modifier.width(160.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(color.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
                }
                if (isAlert) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(ErrorRed, CircleShape())
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            )
            Text(
                title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                subText,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = color,
                    fontWeight = FontWeight.Medium,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
private fun CircleShape() = androidx.compose.foundation.shape.CircleShape

@Composable
fun QuickActionChip(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(10.dp),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Icon(icon, null, modifier = Modifier.size(16.dp), tint = PrimaryNavy)
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text,
            style = MaterialTheme.typography.labelSmall.copy(
                color = PrimaryNavy,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
            )
        )
    }
}

@Composable
fun PendingApplicationCard(app: AdminDriverApplicationSummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(WarningAmber.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, null, tint = WarningAmber, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    app.driverName,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy)
                )
                Text(
                    "${app.applicationId} · ${app.registrationDate}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
                Text(
                    app.vehicleRegNumber,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                )
            }
            AdminStatusChip(app.status)
        }
    }
}

@Composable
fun RecentBookingCard(booking: AdminBookingSummary) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(RoyalBlue.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.DirectionsCar, null, tint = RoyalBlue, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    booking.bookingId,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy)
                )
                Text(
                    "${booking.passengerName} → ${booking.destination}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
                Text(
                    "₹${"%.0f".format(booking.fare)} · ${booking.rideType}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                )
            }
            RideStatusChip(booking.status)
        }
    }
}

@Composable
fun ComplaintPreviewCard(complaint: AdminComplaint) {
    val priorityColor = when (complaint.priority) {
        ComplaintPriority.CRITICAL -> ErrorRed
        ComplaintPriority.HIGH -> Color(0xFFEA580C)
        ComplaintPriority.MEDIUM -> WarningAmber
        ComplaintPriority.LOW -> SuccessGreen
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(priorityColor)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    complaint.complaintId,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy)
                )
                Text(
                    "${complaint.category.label} · ${complaint.reportedBy}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
                Text(
                    complaint.description.take(60) + if (complaint.description.length > 60) "…" else "",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                )
            }
            ComplaintStatusChip(complaint.status)
        }
    }
}

@Composable
fun NotificationPreviewCard(notif: AdminNotification) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
            Icon(
                Icons.Default.Notifications,
                null,
                tint = WarningAmber,
                modifier = Modifier.size(18.dp).padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    notif.title,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy)
                )
                Text(
                    notif.message,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp)
                )
                Text(
                    notif.timestamp,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                )
            }
        }
    }
}

@Composable
fun EmptyState(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Inbox,
            null,
            tint = TextMuted,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, style = MaterialTheme.typography.bodyMedium.copy(color = TextSecondary, fontWeight = FontWeight.Medium))
        Text(subtitle, style = MaterialTheme.typography.bodySmall.copy(color = TextMuted))
    }
}

// ─── Status Chip Helpers ──────────────────────────────────────────────────────

@Composable
fun AdminStatusChip(status: AdminDriverStatus) {
    val (bg, fg) = when (status) {
        AdminDriverStatus.APPROVED -> Pair(Color(0xFFD1FAE5), SuccessGreen)
        AdminDriverStatus.PENDING_VERIFICATION -> Pair(Color(0xFFFEF3C7), WarningAmber)
        AdminDriverStatus.UNDER_REVIEW -> Pair(Color(0xFFDBEAFE), RoyalBlue)
        AdminDriverStatus.REJECTED -> Pair(Color(0xFFFEE2E2), ErrorRed)
        AdminDriverStatus.SUSPENDED -> Pair(Color(0xFFFEE2E2), Color(0xFFB91C1C))
        AdminDriverStatus.EXPIRED -> Pair(Color(0xFFF3F4F6), Color(0xFF6B7280))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            status.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = fg, fontWeight = FontWeight.Bold, fontSize = 10.sp
            )
        )
    }
}

@Composable
fun RideStatusChip(status: AdminRideStatus) {
    val (bg, fg) = when (status) {
        AdminRideStatus.COMPLETED -> Pair(Color(0xFFD1FAE5), SuccessGreen)
        AdminRideStatus.IN_PROGRESS -> Pair(Color(0xFFDBEAFE), RoyalBlue)
        AdminRideStatus.CONFIRMED -> Pair(Color(0xFFE0F2FE), TealAccent)
        AdminRideStatus.CANCELLED -> Pair(Color(0xFFFEE2E2), ErrorRed)
        AdminRideStatus.REQUESTED -> Pair(Color(0xFFFEF3C7), WarningAmber)
        else -> Pair(Color(0xFFF3F4F6), Color(0xFF6B7280))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            status.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = fg, fontWeight = FontWeight.Bold, fontSize = 10.sp
            )
        )
    }
}

@Composable
fun ComplaintStatusChip(status: ComplaintStatus) {
    val (bg, fg) = when (status) {
        ComplaintStatus.OPEN -> Pair(Color(0xFFFEF3C7), WarningAmber)
        ComplaintStatus.UNDER_REVIEW -> Pair(Color(0xFFDBEAFE), RoyalBlue)
        ComplaintStatus.ESCALATED -> Pair(Color(0xFFFEE2E2), ErrorRed)
        ComplaintStatus.RESOLVED -> Pair(Color(0xFFD1FAE5), SuccessGreen)
        ComplaintStatus.CLOSED -> Pair(Color(0xFFF3F4F6), Color(0xFF6B7280))
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bg)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(
            status.label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = fg, fontWeight = FontWeight.Bold, fontSize = 10.sp
            )
        )
    }
}

