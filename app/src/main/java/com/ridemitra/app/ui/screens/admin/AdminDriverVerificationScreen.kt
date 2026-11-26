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
 * Driver Applications Management Screen - Phase 4
 * Shows all driver applications with filter tabs.
 */
@Composable
fun AdminDriverApplicationsScreen(
    applications: List<AdminDriverApplicationSummary>,
    onViewApplication: (String) -> Unit,
    onApprove: (String) -> Unit,
    onReject: (String) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf<AdminDriverStatus?>(null) }

    val filters = listOf(null) + AdminDriverStatus.entries
    val filtered = applications.filter { app ->
        (selectedFilter == null || app.status == selectedFilter) &&
                (searchQuery.isBlank() ||
                        app.driverName.contains(searchQuery, ignoreCase = true) ||
                        app.applicationId.contains(searchQuery, ignoreCase = true) ||
                        app.vehicleRegNumber.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Search bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search by name, app ID, vehicle…") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(Icons.Default.Clear, null, tint = TextSecondary)
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = RoyalBlue, unfocusedBorderColor = BorderLight
            )
        )

        // Filter chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                FilterChip(
                    selected = selectedFilter == filter,
                    onClick = { selectedFilter = filter },
                    label = {
                        Text(
                            text = filter?.label ?: "All",
                            fontSize = 11.sp
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryNavy,
                        selectedLabelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Count
        Text(
            "${filtered.size} application(s) · DEMO DATA",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        // List
        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            if (filtered.isEmpty()) {
                item { EmptyState("No applications found", "Try changing the filter or search term") }
            } else {
                items(filtered) { app ->
                    DriverApplicationCard(
                        app = app,
                        onView = { onViewApplication(app.applicationId) },
                        onApprove = { onApprove(app.applicationId) },
                        onReject = { onReject(app.applicationId) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DriverApplicationCard(
    app: AdminDriverApplicationSummary,
    onView: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    var showRejectDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = onView
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(PrimaryNavy.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = PrimaryNavy, modifier = Modifier.size(26.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        app.driverName,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold, color = PrimaryNavy
                        )
                    )
                    Text(
                        app.driverId,
                        style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp)
                    )
                }
                AdminStatusChip(app.status)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LabelValue("App ID", app.applicationId)
                LabelValue("Vehicle", app.vehicleRegNumber)
                LabelValue("Date", app.registrationDate)
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onView,
                    modifier = Modifier.weight(1f).height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.Visibility, null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Review", fontSize = 12.sp)
                }
                if (app.status == AdminDriverStatus.PENDING_VERIFICATION ||
                    app.status == AdminDriverStatus.UNDER_REVIEW
                ) {
                    Button(
                        onClick = onApprove,
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Icon(Icons.Default.Check, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Approve", fontSize = 12.sp)
                    }
                    OutlinedButton(
                        onClick = { showRejectDialog = true },
                        modifier = Modifier.weight(1f).height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
                    ) {
                        Icon(Icons.Default.Close, null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Reject", fontSize = 12.sp)
                    }
                }
            }
        }
    }

    if (showRejectDialog) {
        RejectApplicationDialog(
            onConfirm = { onReject(); showRejectDialog = false },
            onDismiss = { showRejectDialog = false }
        )
    }
}

@Composable
private fun RejectApplicationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    var reason by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Reject Application", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text(
                    "Please provide a reason for rejection. This will be communicated to the driver.",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Rejection Reason *") },
                    minLines = 3,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { if (reason.isNotBlank()) onConfirm() },
                colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                enabled = reason.isNotBlank()
            ) { Text("Reject Application") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun LabelValue(label: String, value: String) {
    Column {
        Text(label, style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
        Text(value, style = MaterialTheme.typography.labelSmall.copy(color = PrimaryNavy, fontWeight = FontWeight.Medium, fontSize = 11.sp))
    }
}

// ─── Driver App Detail Screen ─────────────────────────────────────────────────

@Composable
fun AdminDriverAppDetailScreen(
    application: AdminDriverApplicationSummary,
    onApprove: () -> Unit,
    onReject: (String) -> Unit,
    onRequestDocuments: (String) -> Unit,
    onSuspend: () -> Unit,
    onBack: () -> Unit
) {
    var identityChecked by remember { mutableStateOf(false) }
    var licenseChecked by remember { mutableStateOf(false) }
    var rcChecked by remember { mutableStateOf(false) }
    var insuranceChecked by remember { mutableStateOf(false) }
    var pucChecked by remember { mutableStateOf(false) }
    var permitsChecked by remember { mutableStateOf(false) }
    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }
    var showDocRequestDialog by remember { mutableStateOf(false) }

    val allChecked = identityChecked && licenseChecked && rcChecked && insuranceChecked && pucChecked

    LazyColumn(
        contentPadding = PaddingValues(bottom = 100.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Back + Status Header
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(PrimaryNavy)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .statusBarsPadding(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, null, tint = Color.White)
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        application.driverName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold, color = Color.White
                        )
                    )
                    Text(
                        application.applicationId,
                        style = MaterialTheme.typography.labelSmall.copy(color = Color.White.copy(0.7f))
                    )
                }
                AdminStatusChip(application.status)
            }
        }

        // Demo notice
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFFEF3C7))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "DEMO DATA – Document images are not available. Backend integration required for secure document retrieval.",
                    style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF92400E), fontSize = 11.sp)
                )
            }
        }

        // A. Personal Details
        item { DetailSectionHeader("A. Personal Details", Icons.Default.Person) }
        item {
            DetailCard {
                DetailRow("Full Name", application.driverName)
                DetailRow("Driver ID", application.driverId)
                DetailRow("Date of Birth", "14/08/1988")
                DetailRow("Mobile", application.mobileNumberMasked)
                DetailRow("Email", "driver.${application.driverId.lowercase()}@ridemitra.in")
                DetailRow("Residential Address", "No. 42, 3rd Cross, Airport Road, Kodihalli, Bengaluru 560008")
                DetailRow("City", application.city)
                DetailRow("Emergency Contact", "Sunita Kumar (Spouse) - +91 98XXX X1223")
                DetailRow("Registration Date", application.registrationDate)
            }
        }

        // B. Identity Documents
        item { DetailSectionHeader("B. Identity Documents", Icons.Default.Badge) }
        item {
            DetailCard {
                DetailRow("Document Type", "Aadhaar Card")
                DetailRow("Document Number", "XXXX-XXXX-4920 (Masked for security)")
                MaskedDocImageRow("Front Image", "identity_front")
                MaskedDocImageRow("Back Image", "identity_back")
                DetailRow("Verification Status", "Uploaded – Pending Admin Review")
            }
        }

        // C. Driving Licence
        item { DetailSectionHeader("C. Driving Licence", Icons.Default.CreditCard) }
        item {
            DetailCard {
                DetailRow("Licence Number", "KA01-20150048291 (Masked)")
                DetailRow("Category", "Commercial / LMV Transport")
                DetailRow("Issue Date", "15/03/2015")
                DetailRow("Expiry Date", if (application.status == AdminDriverStatus.EXPIRED) "10/01/2023 (EXPIRED)" else "14/03/2035 (Valid)")
                MaskedDocImageRow("Front Image", "dl_front")
                MaskedDocImageRow("Back Image", "dl_back")
            }
        }

        // D. Vehicle Details
        item { DetailSectionHeader("D. Vehicle Details", Icons.Default.DirectionsCar) }
        item {
            DetailCard {
                DetailRow("Registration No.", application.vehicleRegNumber)
                DetailRow("Manufacturer", "Toyota")
                DetailRow("Model", "Etios (Commercial Sedan)")
                DetailRow("Manufacturing Year", "2022")
                DetailRow("Vehicle Category", "Sedan")
                DetailRow("Passenger Capacity", "4 Registered Seats (Strictly Max 3 for RideMitra N-1 rule)")
                DetailRow("Luggage Capacity", "3 Large Trolleys + 2 Handbags")
                MaskedDocImageRow("Vehicle Front Photo", "vehicle_front")
                MaskedDocImageRow("Vehicle Rear Photo", "vehicle_rear")
            }
        }

        // E. Vehicle Documents
        item { DetailSectionHeader("E. Vehicle Documents", Icons.Default.Description) }
        item {
            DetailCard {
                DetailRow("RC Number", "KA01MJ4829")
                MaskedDocImageRow("RC Document", "rc_doc")
                DetailRow("Insurance Policy", "BAJAJ-ALLIANZ-COMM-4892")
                DetailRow("Insurance Expiry", if (application.status == AdminDriverStatus.EXPIRED) "15/04/2024 (EXPIRED)" else "28/11/2027 (Valid)")
                MaskedDocImageRow("Insurance Document", "insurance_doc")
                DetailRow("PUC Expiry", if (application.status == AdminDriverStatus.EXPIRED) "01/01/2024 (EXPIRED)" else "10/05/2027 (Valid)")
                MaskedDocImageRow("PUC Document", "puc_doc")
                DetailRow("Fitness Expiry", "14/03/2028 (Valid)")
                MaskedDocImageRow("Fitness Certificate", "fitness_doc")
                DetailRow("Commercial Permit Expiry", "15/06/2028 (Valid)")
                MaskedDocImageRow("Commercial Permit", "permit_doc")
            }
        }

        // F. Reviewing Admin & Decision Audit
        item { DetailSectionHeader("F. Admin Review Audit Record", Icons.Default.Security) }
        item {
            DetailCard {
                DetailRow("Reviewing Admin", "Platform Administrator (ADMIN-001)")
                DetailRow("Review Timestamp", "25 Nov 2026, 04:20 PM")
                DetailRow("Current Status", application.status.label)
                DetailRow("Authorization Mode", "Front-end Demonstration (Backend required for persistence)")
            }
        }

        // Expiry Warning Banner if status is EXPIRED
        if (application.status == AdminDriverStatus.EXPIRED) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFFFEE2E2))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Error, null, tint = ErrorRed, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            "CANNOT APPROVE: EXPIRED DOCUMENTS DETECTED",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = ErrorRed)
                        )
                        Text(
                            "One or more required documents (DL, Insurance, PUC) have expired. Ride dispatch and application approval are strictly blocked until valid documents are submitted.",
                            style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF7F1D1D), fontSize = 11.sp)
                        )
                    }
                }
            }
        }

        // Verification Checklist
        item { DetailSectionHeader("✓ Verification Checklist", Icons.Default.FactCheck) }
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
                    ChecklistItem("Identity document reviewed", identityChecked) { identityChecked = it }
                    ChecklistItem("Driving licence reviewed", licenseChecked) { licenseChecked = it }
                    ChecklistItem("Vehicle RC reviewed", rcChecked) { rcChecked = it }
                    ChecklistItem("Insurance validity checked", insuranceChecked) { insuranceChecked = it }
                    ChecklistItem("PUC validity checked", pucChecked) { pucChecked = it }
                    ChecklistItem("Other applicable permits reviewed", permitsChecked) { permitsChecked = it }
                }
            }
        }

        // Action Buttons
        if (application.status == AdminDriverStatus.PENDING_VERIFICATION ||
            application.status == AdminDriverStatus.UNDER_REVIEW ||
            application.status == AdminDriverStatus.EXPIRED
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    val canApprove = allChecked && application.status != AdminDriverStatus.EXPIRED
                    if (!allChecked && application.status != AdminDriverStatus.EXPIRED) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFEF3C7))
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, null, tint = WarningAmber, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Complete the verification checklist before approving.",
                                style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF92400E))
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Button(
                        onClick = { showApproveDialog = true },
                        enabled = canApprove,
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SuccessGreen,
                            disabledContainerColor = SuccessGreen.copy(0.4f)
                        )
                    ) {
                        Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (application.status == AdminDriverStatus.EXPIRED) "Approval Blocked (Documents Expired)"
                            else "Approve Application",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedButton(
                            onClick = { showDocRequestDialog = true },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Upload, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Request Docs", fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { showRejectDialog = true },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                            border = androidx.compose.foundation.BorderStroke(1.dp, ErrorRed)
                        ) {
                            Icon(Icons.Default.Cancel, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reject", fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = onSuspend,
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF7C3AED)),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF7C3AED))
                        ) {
                            Icon(Icons.Default.Block, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Suspend", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }

    if (showApproveDialog) {
        AlertDialog(
            onDismissRequest = { showApproveDialog = false },
            title = { Text("Approve Application?", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("You are about to approve the application for ${application.driverName}.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "⚠ NOTE: This action will be recorded. Backend authorization will verify and enforce this decision.",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showApproveDialog = false; onApprove() },
                    colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                ) { Text("Confirm Approval") }
            },
            dismissButton = { TextButton(onClick = { showApproveDialog = false }) { Text("Cancel") } }
        )
    }

    if (showRejectDialog) {
        var rejectionReason by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Reject Application", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Provide a detailed rejection reason. The driver will be notified.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = rejectionReason,
                        onValueChange = { rejectionReason = it },
                        label = { Text("Rejection Reason *") },
                        minLines = 3,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { if (rejectionReason.isNotBlank()) { showRejectDialog = false; onReject(rejectionReason) } },
                    colors = ButtonDefaults.buttonColors(containerColor = ErrorRed),
                    enabled = rejectionReason.isNotBlank()
                ) { Text("Reject Application") }
            },
            dismissButton = { TextButton(onClick = { showRejectDialog = false }) { Text("Cancel") } }
        )
    }

    if (showDocRequestDialog) {
        var docRequest by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showDocRequestDialog = false },
            title = { Text("Request Additional Documents", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Specify the documents needed from the driver.")
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = docRequest,
                        onValueChange = { docRequest = it },
                        label = { Text("Required Documents *") },
                        minLines = 3,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("e.g. Clear front photo of Driving Licence") }
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { if (docRequest.isNotBlank()) { showDocRequestDialog = false; onRequestDocuments(docRequest) } },
                    enabled = docRequest.isNotBlank()
                ) { Text("Send Request") }
            },
            dismissButton = { TextButton(onClick = { showDocRequestDialog = false }) { Text("Cancel") } }
        )
    }
}

@Composable
private fun DetailSectionHeader(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Row(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = RoyalBlue, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            title,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.Bold, color = PrimaryNavy
            )
        )
    }
}

@Composable
private fun DetailCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), content = content)
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary), modifier = Modifier.weight(0.4f))
        Text(value, style = MaterialTheme.typography.bodySmall.copy(color = PrimaryNavy, fontWeight = FontWeight.Medium), modifier = Modifier.weight(0.6f))
    }
    HorizontalDivider(color = BorderLight.copy(alpha = 0.5f), modifier = Modifier.padding(vertical = 2.dp))
}

@Composable
private fun ColumnScope.MaskedDocImageRow(label: String, docKey: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary), modifier = Modifier.weight(0.4f))
        Box(
            modifier = Modifier
                .weight(0.6f)
                .height(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF1F5F9)),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Lock, null, tint = TextMuted, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Secure · Backend Access Required", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
            }
        }
    }
}

@Composable
private fun ChecklistItem(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = SuccessGreen),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (checked) SuccessGreen else PrimaryNavy,
                fontWeight = if (checked) FontWeight.Medium else FontWeight.Normal
            )
        )
    }
}

