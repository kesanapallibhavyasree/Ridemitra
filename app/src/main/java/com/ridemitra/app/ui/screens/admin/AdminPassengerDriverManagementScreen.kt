package com.ridemitra.app.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
 * Passenger Management Screen - Phase 4
 */
@Composable
fun AdminPassengerManagementScreen(
    passengers: List<AdminPassengerSummary>,
    onSuspend: (String) -> Unit,
    onRestore: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var filterStatus by remember { mutableStateOf<PassengerAccountStatus?>(null) }

    val filtered = passengers.filter { p ->
        (filterStatus == null || p.accountStatus == filterStatus) &&
                (searchQuery.isBlank() ||
                        p.name.contains(searchQuery, ignoreCase = true) ||
                        p.userId.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search passengers…") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalBlue, unfocusedBorderColor = BorderLight)
        )

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(null, PassengerAccountStatus.ACTIVE, PassengerAccountStatus.SUSPENDED, PassengerAccountStatus.UNDER_REVIEW)
                .forEach { status ->
                    FilterChip(
                        selected = filterStatus == status,
                        onClick = { filterStatus = status },
                        label = { Text(status?.label ?: "All", fontSize = 11.sp) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryNavy, selectedLabelColor = Color.White
                        )
                    )
                }
        }

        Text(
            "${filtered.size} passengers · DEMO DATA",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            if (filtered.isEmpty()) {
                item { EmptyState("No passengers found", "Try changing filters") }
            } else {
                items(filtered) { passenger ->
                    PassengerCard(passenger = passenger, onSuspend = onSuspend, onRestore = onRestore)
                }
            }
        }
    }
}

@Composable
private fun PassengerCard(
    passenger: AdminPassengerSummary,
    onSuspend: (String) -> Unit,
    onRestore: (String) -> Unit
) {
    var showProfileDialog by remember { mutableStateOf(false) }
    var showHistoryDialog by remember { mutableStateOf(false) }
    var showComplaintsDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { showProfileDialog = true }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp))
                        .background(RoyalBlue.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = RoyalBlue, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(passenger.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Text(passenger.userId, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                    Text(passenger.mobileNumberMasked, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 11.sp))
                }
                PassengerStatusChip(passenger.accountStatus)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LabelValue("Total Bookings", passenger.totalBookings.toString())
                LabelValue("Completed", passenger.completedRides.toString())
                LabelValue("Cancelled", passenger.cancelledRides.toString())
                LabelValue("Registered", passenger.registrationDate)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (passenger.accountStatus == PassengerAccountStatus.ACTIVE ||
                    passenger.accountStatus == PassengerAccountStatus.UNDER_REVIEW) {
                    OutlinedButton(
                        onClick = { onSuspend(passenger.userId) },
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
                    ) {
                        Icon(Icons.Default.Block, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Suspend", fontSize = 12.sp)
                    }
                } else if (passenger.accountStatus == PassengerAccountStatus.SUSPENDED) {
                    Button(
                        onClick = { onRestore(passenger.userId) },
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Icon(Icons.Default.Restore, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Restore", fontSize = 12.sp)
                    }
                }
                OutlinedButton(
                    onClick = { showHistoryDialog = true },
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.History, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("History", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = { showComplaintsDialog = true },
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Report, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Complaints", fontSize = 12.sp)
                }
            }
        }
    }

    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = { Text("Passenger Profile: ${passenger.name}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    DetailRow("User ID", passenger.userId)
                    DetailRow("Phone", passenger.mobileNumberMasked)
                    DetailRow("Email", passenger.email)
                    DetailRow("Registered", passenger.registrationDate)
                    DetailRow("Account Status", passenger.accountStatus.label)
                    DetailRow("Total Bookings", passenger.totalBookings.toString())
                    DetailRow("Completed Trips", passenger.completedRides.toString())
                    DetailRow("Cancelled Trips", passenger.cancelledRides.toString())
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("⚠ Demonstration Profile. Masked identifiers enforced for security.", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
                }
            },
            confirmButton = { TextButton(onClick = { showProfileDialog = false }) { Text("Close") } }
        )
    }

    if (showHistoryDialog) {
        AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            title = { Text("Booking History (${passenger.name})", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Recent Rides (DEMO DATA):", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• RM-POOL-9821: BLR T1 → Indiranagar (₹890, Confirmed)", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• RM-SOLO-9742: BLR T2 → Koramangala (₹640, Completed)", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• RM-POOL-9610: BLR T1 → Whitefield (₹720, Completed)", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            },
            confirmButton = { TextButton(onClick = { showHistoryDialog = false }) { Text("Close") } }
        )
    }

    if (showComplaintsDialog) {
        AlertDialog(
            onDismissRequest = { showComplaintsDialog = false },
            title = { Text("Complaints (${passenger.name})", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("• CMP-2026-0128: Driver Behaviour (Under Review)", style = MaterialTheme.typography.bodySmall.copy(color = PrimaryNavy, fontWeight = FontWeight.Medium))
                    Text("Reported: Driver was rude and took a longer route.", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                }
            },
            confirmButton = { TextButton(onClick = { showComplaintsDialog = false }) { Text("Close") } }
        )
    }
}

@Composable
private fun PassengerStatusChip(status: PassengerAccountStatus) {
    val (bg, fg) = when (status) {
        PassengerAccountStatus.ACTIVE -> Pair(Color(0xFFD1FAE5), SuccessGreen)
        PassengerAccountStatus.SUSPENDED -> Pair(Color(0xFFFEE2E2), ErrorRed)
        PassengerAccountStatus.UNDER_REVIEW -> Pair(Color(0xFFFEF3C7), WarningAmber)
    }
    Box(
        modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(bg).padding(horizontal = 8.dp, vertical = 3.dp)
    ) {
        Text(status.label, style = MaterialTheme.typography.labelSmall.copy(color = fg, fontWeight = FontWeight.Bold, fontSize = 10.sp))
    }
}

// ─── Driver Management Screen ─────────────────────────────────────────────────

@Composable
fun AdminDriverManagementScreen(
    drivers: List<AdminDriverApplicationSummary>,
    onSuspend: (String) -> Unit,
    onReactivate: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filtered = drivers.filter {
        searchQuery.isBlank() ||
                it.driverName.contains(searchQuery, ignoreCase = true) ||
                it.driverId.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search drivers…") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalBlue, unfocusedBorderColor = BorderLight)
        )

        Text(
            "${filtered.size} drivers · DEMO DATA",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            if (filtered.isEmpty()) {
                item { EmptyState("No drivers found", "Try changing search term") }
            } else {
                items(filtered) { driver ->
                    DriverManagementCard(
                        driver = driver,
                        onSuspend = { onSuspend(driver.driverId) },
                        onReactivate = { onReactivate(driver.driverId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DriverManagementCard(
    driver: AdminDriverApplicationSummary,
    onSuspend: () -> Unit,
    onReactivate: () -> Unit
) {
    var showProfileDialog by remember { mutableStateOf(false) }
    var showDocsDialog by remember { mutableStateOf(false) }
    var showExpiryDialog by remember { mutableStateOf(false) }
    var showComplaintsDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { showProfileDialog = true }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp))
                        .background(TealAccent.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DirectionsCar, null, tint = TealAccent, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(driver.driverName, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Text(driver.driverId, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                    Text("${driver.vehicleRegNumber} · Sedan", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 11.sp))
                }
                AdminStatusChip(driver.status)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                LabelValue("Completed", "312 rides")
                LabelValue("Rating", "4.88 ★")
                LabelValue("Earnings (Mo)", "₹34,200")
                LabelValue("City", driver.city)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Expiry quick-summary chip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF1F5F9))
                    .clickable { showExpiryDialog = true }
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Schedule, null, tint = PrimaryNavy, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (driver.status == AdminDriverStatus.EXPIRED) "DL: Expired 2023 · Insurance: Expired 2024"
                        else "DL: 2035 · Ins: 2027 · PUC: 2027",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = if (driver.status == AdminDriverStatus.EXPIRED) ErrorRed else PrimaryNavy,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Text("View Expiry →", style = MaterialTheme.typography.labelSmall.copy(color = RoyalBlue, fontSize = 10.sp))
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (driver.status != AdminDriverStatus.SUSPENDED) {
                    OutlinedButton(
                        onClick = onSuspend,
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
                    ) {
                        Icon(Icons.Default.Block, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Suspend", fontSize = 12.sp)
                    }
                } else {
                    Button(
                        onClick = onReactivate,
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Icon(Icons.Default.Restore, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reactivate", fontSize = 12.sp)
                    }
                }
                OutlinedButton(
                    onClick = { showDocsDialog = true },
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Description, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Docs", fontSize = 12.sp)
                }
                OutlinedButton(
                    onClick = { showComplaintsDialog = true },
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Report, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Feedback", fontSize = 12.sp)
                }
            }
        }
    }

    if (showProfileDialog) {
        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = { Text("Driver Profile: ${driver.driverName}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    DetailRow("Driver ID", driver.driverId)
                    DetailRow("Mobile", driver.mobileNumberMasked)
                    DetailRow("Vehicle Reg", driver.vehicleRegNumber)
                    DetailRow("Model", "Toyota Etios (Sedan)")
                    DetailRow("Passenger Capacity", "4 Seats (Max 3 RideMitra Passengers)")
                    DetailRow("Completed Trips", "312 trips")
                    DetailRow("Driver Rating", "4.88 / 5.0 (★)")
                    DetailRow("Monthly Payout", "₹34,200 (DEMO DATA)")
                    DetailRow("Compliance Status", driver.status.label)
                }
            },
            confirmButton = { TextButton(onClick = { showProfileDialog = false }) { Text("Close") } }
        )
    }

    if (showDocsDialog) {
        AlertDialog(
            onDismissRequest = { showDocsDialog = false },
            title = { Text("Driver Regulatory Documents", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    DetailRow("Driving Licence", if (driver.status == AdminDriverStatus.EXPIRED) "KA01-XXXXXXXX (Expired)" else "KA01-XXXXXXXX (Valid)")
                    DetailRow("Vehicle RC", "${driver.vehicleRegNumber} (Verified)")
                    DetailRow("Motor Insurance", if (driver.status == AdminDriverStatus.EXPIRED) "Expired 15/04/2024" else "Valid till 28/11/2027")
                    DetailRow("PUC Certificate", if (driver.status == AdminDriverStatus.EXPIRED) "Expired 01/01/2024" else "Valid till 10/05/2027")
                    DetailRow("Fitness Certificate", "Valid till 14/03/2028")
                    DetailRow("Commercial Permit", "Valid till 15/06/2028")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("🔒 Documents securely stored in compliance vault. Backend verification active.", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
                }
            },
            confirmButton = { TextButton(onClick = { showDocsDialog = false }) { Text("Close") } }
        )
    }

    if (showExpiryDialog) {
        AlertDialog(
            onDismissRequest = { showExpiryDialog = false },
            title = { Text("Document Expiry Review", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Regulatory Document Validity Schedule:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Driving Licence: ${if (driver.status == AdminDriverStatus.EXPIRED) "10/01/2023 (EXPIRED ⚠)" else "14/03/2035 (Valid ✓)"}", style = MaterialTheme.typography.bodySmall.copy(color = if (driver.status == AdminDriverStatus.EXPIRED) ErrorRed else PrimaryNavy))
                    Text("• Insurance Policy: ${if (driver.status == AdminDriverStatus.EXPIRED) "15/04/2024 (EXPIRED ⚠)" else "28/11/2027 (Valid ✓)"}", style = MaterialTheme.typography.bodySmall.copy(color = if (driver.status == AdminDriverStatus.EXPIRED) ErrorRed else PrimaryNavy))
                    Text("• PUC Certificate: ${if (driver.status == AdminDriverStatus.EXPIRED) "01/01/2024 (EXPIRED ⚠)" else "10/05/2027 (Valid ✓)"}", style = MaterialTheme.typography.bodySmall.copy(color = if (driver.status == AdminDriverStatus.EXPIRED) ErrorRed else PrimaryNavy))
                    Text("• Fitness Certificate: 14/03/2028 (Valid ✓)", style = MaterialTheme.typography.bodySmall.copy(color = PrimaryNavy))
                    Text("• Commercial Permit: 15/06/2028 (Valid ✓)", style = MaterialTheme.typography.bodySmall.copy(color = PrimaryNavy))
                }
            },
            confirmButton = { TextButton(onClick = { showExpiryDialog = false }) { Text("Close") } }
        )
    }

    if (showComplaintsDialog) {
        AlertDialog(
            onDismissRequest = { showComplaintsDialog = false },
            title = { Text("Driver Complaints & Feedback", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("• No active passenger complaints against ${driver.driverName}.", style = MaterialTheme.typography.bodySmall.copy(color = SuccessGreen))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Recent rating: 5.0 (42 trips), 4.0 (8 trips), 1.0 (0 trips)", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
                }
            },
            confirmButton = { TextButton(onClick = { showComplaintsDialog = false }) { Text("Close") } }
        )
    }
}

