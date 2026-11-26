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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.*
import com.ridemitra.app.ui.theme.*

/**
 * Complaint Management Screen - Phase 4
 */
@Composable
fun AdminComplaintManagementScreen(
    complaints: List<AdminComplaint>,
    onUpdateStatus: (String, ComplaintStatus) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<ComplaintStatus?>(null) }
    var selectedCategory by remember { mutableStateOf<ComplaintCategory?>(null) }

    val filtered = complaints.filter { c ->
        (selectedStatus == null || c.status == selectedStatus) &&
                (selectedCategory == null || c.category == selectedCategory) &&
                (searchQuery.isBlank() ||
                        c.complaintId.contains(searchQuery, ignoreCase = true) ||
                        c.reportedBy.contains(searchQuery, ignoreCase = true) ||
                        c.bookingId.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search complaint ID, user, booking…") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalBlue, unfocusedBorderColor = BorderLight)
        )

        // Status filter
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("All Statuses", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryNavy, selectedLabelColor = Color.White)
                )
            }
            items(ComplaintStatus.entries) { status ->
                FilterChip(
                    selected = selectedStatus == status,
                    onClick = { selectedStatus = status },
                    label = { Text(status.label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryNavy, selectedLabelColor = Color.White)
                )
            }
        }

        Text(
            "${filtered.size} complaints · DEMO DATA",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            if (filtered.isEmpty()) {
                item { EmptyState("No complaints found", "All clear! No active complaints.") }
            } else {
                items(filtered) { complaint ->
                    ComplaintCard(complaint = complaint, onUpdateStatus = onUpdateStatus)
                }
            }
        }
    }
}

@Composable
private fun ComplaintCard(
    complaint: AdminComplaint,
    onUpdateStatus: (String, ComplaintStatus) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showStatusMenu by remember { mutableStateOf(false) }
    var showNotesDialog by remember { mutableStateOf(false) }
    var internalNoteText by remember { mutableStateOf(complaint.internalNotes) }
    var savedNotes by remember { mutableStateOf(complaint.internalNotes) }

    val priorityColor = when (complaint.priority) {
        ComplaintPriority.CRITICAL -> ErrorRed
        ComplaintPriority.HIGH -> Color(0xFFEA580C)
        ComplaintPriority.MEDIUM -> WarningAmber
        ComplaintPriority.LOW -> SuccessGreen
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Priority indicator
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(44.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(priorityColor)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(complaint.complaintId, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Text(
                        "${complaint.reportedBy} (${complaint.reportedByType}) · ${complaint.category.label}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp)
                    )
                    Text(complaint.bookingId, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
                }
                Column(horizontalAlignment = Alignment.End) {
                    ComplaintStatusChip(complaint.status)
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(priorityColor.copy(0.12f)).padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(complaint.priority.label, style = MaterialTheme.typography.labelSmall.copy(color = priorityColor, fontWeight = FontWeight.Bold, fontSize = 10.sp))
                    }
                }
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BorderLight)
                Spacer(modifier = Modifier.height(10.dp))

                Text("Description", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                Spacer(modifier = Modifier.height(4.dp))
                Text(complaint.description, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Submitted: ${complaint.submittedDate}", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))

                if (savedNotes.isNotBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFEF3C7))
                            .padding(8.dp)
                    ) {
                        Column {
                            Text("Internal Admin Note:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFF92400E)))
                            Text(savedNotes, style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF92400E), fontSize = 11.sp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Actions
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box {
                        OutlinedButton(
                            onClick = { showStatusMenu = true },
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.Edit, null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Status", fontSize = 11.sp)
                        }
                        DropdownMenu(
                            expanded = showStatusMenu,
                            onDismissRequest = { showStatusMenu = false }
                        ) {
                            ComplaintStatus.entries.forEach { status ->
                                DropdownMenuItem(
                                    text = { Text(status.label) },
                                    onClick = {
                                        onUpdateStatus(complaint.complaintId, status)
                                        showStatusMenu = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedButton(
                        onClick = { showNotesDialog = true },
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp)
                    ) {
                        Icon(Icons.Default.NoteAdd, null, modifier = Modifier.size(13.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Notes", fontSize = 11.sp)
                    }

                    if (complaint.status != ComplaintStatus.RESOLVED && complaint.status != ComplaintStatus.CLOSED) {
                        Button(
                            onClick = { onUpdateStatus(complaint.complaintId, ComplaintStatus.RESOLVED) },
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.Check, null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Resolve", fontSize = 11.sp)
                        }
                    }

                    if (complaint.category == ComplaintCategory.SAFETY_CONCERN && complaint.status != ComplaintStatus.ESCALATED) {
                        Button(
                            onClick = { onUpdateStatus(complaint.complaintId, ComplaintStatus.ESCALATED) },
                            modifier = Modifier.height(36.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                            contentPadding = PaddingValues(horizontal = 8.dp)
                        ) {
                            Icon(Icons.Default.PriorityHigh, null, modifier = Modifier.size(13.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Escalate Safety", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }

    if (showNotesDialog) {
        AlertDialog(
            onDismissRequest = { showNotesDialog = false },
            title = { Text("Internal Admin Note", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Add internal notes visible only to operations administrators.", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = internalNoteText,
                        onValueChange = { internalNoteText = it },
                        label = { Text("Note") },
                        minLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        savedNotes = internalNoteText
                        showNotesDialog = false
                    }
                ) { Text("Save Note") }
            },
            dismissButton = { TextButton(onClick = { showNotesDialog = false }) { Text("Cancel") } }
        )
    }
}
}

// ─── Analytics Screen ─────────────────────────────────────────────────────────

@Composable
fun AdminAnalyticsScreen(analytics: AdminAnalyticsSummary) {
    var selectedPeriod by remember { mutableStateOf(AnalyticsPeriod.WEEKLY) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF142B45)).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "⚠ DEMO DATA – All analytics are illustrative. Live backend integration required.",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.85f), fontSize = 11.sp)
                )
            }
        }

        // Period selector
        item {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AnalyticsPeriod.entries.forEach { period ->
                    FilterChip(
                        selected = selectedPeriod == period,
                        onClick = { selectedPeriod = period },
                        label = { Text(period.name.lowercase().replaceFirstChar { it.uppercase() }, fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryNavy, selectedLabelColor = Color.White)
                    )
                }
            }
        }

        // Key metrics
        item { SectionHeader("📈 Key Metrics", "DEMO DATA") }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { StatCard("Total Bookings", analytics.totalBookings.toString(), Icons.Default.BookOnline, RoyalBlue, "Total rides") }
                item { StatCard("Shared Rides", analytics.sharedRides.toString(), Icons.Default.People, TealAccent, "${analytics.sharedRides * 100 / analytics.totalBookings}% of total") }
                item { StatCard("Solo Rides", analytics.soloRides.toString(), Icons.Default.Person, PrimaryNavy, "${analytics.soloRides * 100 / analytics.totalBookings}% of total") }
                item { StatCard("Completed", analytics.completedRides.toString(), Icons.Default.CheckCircle, SuccessGreen, "${100 - analytics.cancellationRate.toInt()}% completion") }
            }
        }

        item { Spacer(modifier = Modifier.height(8.dp)) }

        item {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { StatCard("New Passengers", analytics.newPassengers.toString(), Icons.Default.PersonAdd, RoyalBlue, "New signups") }
                item { StatCard("Driver Approval Rate", "${"%.1f".format(analytics.driverApprovalRate)}%", Icons.Default.Verified, SuccessGreen, "Approval rate") }
                item { StatCard("Cancellation Rate", "${"%.1f".format(analytics.cancellationRate)}%", Icons.Default.Cancel, WarningAmber, "Of total bookings") }
            }
        }

        // Revenue section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader("💰 Revenue Overview", "DEMO DATA")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    RevenueRow("Platform Revenue", "₹${"%.2f".format(analytics.platformRevenue)}", RoyalBlue)
                    RevenueRow("Driver Payouts", "₹${"%.2f".format(analytics.driverPayouts)}", SuccessGreen)
                    RevenueRow("Net Platform Margin", "₹${"%.2f".format(analytics.platformRevenue - analytics.driverPayouts)}", TealAccent)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = BorderLight)
                    Text(
                        "Platform Commission: 10% of total fare per ride",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 11.sp)
                    )
                }
            }
        }

        // Simple chart visualization
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SectionHeader("📊 Daily Revenue Trend (This Week)", "DEMO DATA")
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val maxVal = analytics.chartPoints.maxOf { it.second }
                    analytics.chartPoints.forEach { (day, value) ->
                        ChartBar(day = day, value = value, maxValue = maxVal)
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "⚠ Chart values are DEMO DATA for UI demonstration purposes.",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ChartBar(day: String, value: Double, maxValue: Double) {
    val fraction = (value / maxValue).coerceIn(0.05, 1.0).toFloat()
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(day, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary), modifier = Modifier.width(36.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .height(22.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(BorderLight)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fraction)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(RoyalBlue)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            "₹${"%.0f".format(value)}",
            style = MaterialTheme.typography.labelSmall.copy(color = PrimaryNavy, fontSize = 10.sp),
            modifier = Modifier.width(60.dp)
        )
    }
}

@Composable
private fun RevenueRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
        Text(value, style = MaterialTheme.typography.bodySmall.copy(color = color, fontWeight = FontWeight.Bold))
    }
}

