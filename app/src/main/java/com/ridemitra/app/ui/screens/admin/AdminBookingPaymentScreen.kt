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
 * Booking Management Screen - Phase 4
 */
@Composable
fun AdminBookingManagementScreen(
    bookings: List<AdminBookingSummary>
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<AdminRideStatus?>(null) }

    val filtered = bookings.filter { b ->
        (selectedStatus == null || b.status == selectedStatus) &&
                (searchQuery.isBlank() ||
                        b.bookingId.contains(searchQuery, ignoreCase = true) ||
                        b.passengerName.contains(searchQuery, ignoreCase = true) ||
                        b.driverName.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search booking ID, passenger, driver…") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalBlue, unfocusedBorderColor = BorderLight)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("All", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryNavy, selectedLabelColor = Color.White)
                )
            }
            items(AdminRideStatus.entries) { status ->
                FilterChip(
                    selected = selectedStatus == status,
                    onClick = { selectedStatus = status },
                    label = { Text(status.label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryNavy, selectedLabelColor = Color.White)
                )
            }
        }

        Text(
            "${filtered.size} bookings · DEMO DATA",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            if (filtered.isEmpty()) {
                item { EmptyState("No bookings found", "Try changing filters") }
            } else {
                items(filtered) { booking ->
                    BookingDetailCard(booking = booking)
                }
            }
        }
    }
}

@Composable
private fun BookingDetailCard(booking: AdminBookingSummary) {
    var showDetailsDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showCancelDialog by remember { mutableStateOf(false) }
    var showComplaintDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { showDetailsDialog = true }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(RoyalBlue.copy(0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.DirectionsCar, null, tint = RoyalBlue, modifier = Modifier.size(24.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(booking.bookingId, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Text(booking.rideType, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                }
                RideStatusChip(booking.status)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            BookingInfoRow(Icons.Default.Person, "Passenger", booking.passengerName)
            BookingInfoRow(Icons.Default.DirectionsCar, "Driver", booking.driverName)
            BookingInfoRow(Icons.Default.FlightLand, "Pickup Airport", booking.pickupAirport)
            BookingInfoRow(Icons.Default.LocationOn, "Destination", booking.destination)
            BookingInfoRow(Icons.Default.People, "Passengers", "${booking.passengerCount} pax")
            BookingInfoRow(Icons.Default.Schedule, "Scheduled", booking.scheduledDeparture)
            BookingInfoRow(Icons.Default.AccountBalanceWallet, "Fare", "₹${"%.0f".format(booking.fare)}")

            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { showDetailsDialog = true },
                    modifier = Modifier.weight(1f).height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.Visibility, null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Details", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = { showPaymentDialog = true },
                    modifier = Modifier.weight(1f).height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.AccountBalanceWallet, null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Payment", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = { showCancelDialog = true },
                    modifier = Modifier.weight(1f).height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.Cancel, null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Policy", fontSize = 11.sp)
                }
                OutlinedButton(
                    onClick = { showComplaintDialog = true },
                    modifier = Modifier.weight(1f).height(34.dp),
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) {
                    Icon(Icons.Default.Report, null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Dispute", fontSize = 11.sp)
                }
            }
        }
    }

    if (showDetailsDialog) {
        AlertDialog(
            onDismissRequest = { showDetailsDialog = false },
            title = { Text("Booking: ${booking.bookingId}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    DetailRow("Passenger", booking.passengerName)
                    DetailRow("Driver", booking.driverName)
                    DetailRow("Pickup", booking.pickupAirport)
                    DetailRow("Destination", booking.destination)
                    DetailRow("Ride Option", booking.rideType)
                    DetailRow("Passenger Count", "${booking.passengerCount} pax")
                    DetailRow("Booking Time", booking.bookingTime)
                    DetailRow("Departure", booking.scheduledDeparture)
                    DetailRow("Estimated Fare", "₹${"%.0f".format(booking.fare)}")
                    DetailRow("Status", booking.status.label)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("ℹ No fake live tracking data generated. Real-time telemetry handled by backend dispatch.", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
                }
            },
            confirmButton = { TextButton(onClick = { showDetailsDialog = false }) { Text("Close") } }
        )
    }

    if (showPaymentDialog) {
        AlertDialog(
            onDismissRequest = { showPaymentDialog = false },
            title = { Text("Payment Breakdown: ${booking.bookingId}", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    DetailRow("Total Ride Fare", "₹${"%.2f".format(booking.fare)}")
                    DetailRow("Advance Payment (10%)", "₹${"%.2f".format(booking.fare * 0.10)}")
                    DetailRow("Remaining Due (90%)", "₹${"%.2f".format(booking.fare * 0.90)}")
                    DetailRow("Platform Commission (10%)", "₹${"%.2f".format(booking.fare * 0.10)}")
                    DetailRow("Driver Payout", "₹${"%.2f".format(booking.fare * 0.90)}")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("⚠ Payment status is for demonstration. No real transaction processed.", style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp))
                }
            },
            confirmButton = { TextButton(onClick = { showPaymentDialog = false }) { Text("Close") } }
        )
    }

    if (showCancelDialog) {
        AlertDialog(
            onDismissRequest = { showCancelDialog = false },
            title = { Text("Cancellation & Refund Rules", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("RideMitra Strict Cancellation Policy:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• More than 1 hour before departure: 5% of total ride fare refunded (₹${"%.2f".format(booking.fare * 0.05)}).", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• Within 1 hour before departure: No refund (0%).", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("• After scheduled departure: No refund (0%).", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            },
            confirmButton = { TextButton(onClick = { showCancelDialog = false }) { Text("Close") } }
        )
    }

    if (showComplaintDialog) {
        AlertDialog(
            onDismissRequest = { showComplaintDialog = false },
            title = { Text("Investigate Booking Dispute", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Booking Audit Record for ${booking.bookingId}:", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• Safety Status: Normal (No SOS trigger recorded)", style = MaterialTheme.typography.bodySmall.copy(color = SuccessGreen))
                    Text("• Driver Pillar Check-in: Verified on time", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                    Text("• OTP Handshake: Completed successfully", style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary))
                }
            },
            confirmButton = { TextButton(onClick = { showComplaintDialog = false }) { Text("Close") } }
        )
    }
}

@Composable
private fun BookingInfoRow(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = TextSecondary, modifier = Modifier.size(14.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(label, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary), modifier = Modifier.width(100.dp))
        Text(value, style = MaterialTheme.typography.labelSmall.copy(color = PrimaryNavy, fontWeight = FontWeight.Medium))
    }
}

// ─── Payment Management Screen ───────────────────────────────────────────────

@Composable
fun AdminPaymentManagementScreen(
    payments: List<AdminPaymentRecord>
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf<PaymentStatus?>(null) }

    val filtered = payments.filter { p ->
        (selectedStatus == null || p.paymentStatus == selectedStatus) &&
                (searchQuery.isBlank() ||
                        p.transactionId.contains(searchQuery, ignoreCase = true) ||
                        p.bookingId.contains(searchQuery, ignoreCase = true) ||
                        p.passengerName.contains(searchQuery, ignoreCase = true))
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Business Rules Card
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFEBF2FE)),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "RideMitra Payment Business Rules",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("• Advance Payment: 10% of estimated total fare", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                Text("• Cancellation >1 hour before departure: 5% refund", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                Text("• Cancellation within 1 hour or after departure: No refund", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                Text("• Platform Commission: 10% of total fare", style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search transaction ID, booking, passenger…") },
            leadingIcon = { Icon(Icons.Default.Search, null, tint = TextSecondary) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = RoyalBlue, unfocusedBorderColor = BorderLight)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedStatus == null,
                    onClick = { selectedStatus = null },
                    label = { Text("All", fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryNavy, selectedLabelColor = Color.White)
                )
            }
            items(PaymentStatus.entries) { status ->
                FilterChip(
                    selected = selectedStatus == status,
                    onClick = { selectedStatus = status },
                    label = { Text(status.label, fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryNavy, selectedLabelColor = Color.White)
                )
            }
        }

        Text(
            "${filtered.size} records · DEMO DATA",
            style = MaterialTheme.typography.labelSmall.copy(color = TextMuted),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        LazyColumn(contentPadding = PaddingValues(bottom = 16.dp)) {
            if (filtered.isEmpty()) {
                item { EmptyState("No payment records found", "Try changing filters") }
            } else {
                items(filtered) { payment ->
                    PaymentRecordCard(payment = payment)
                }
            }
        }
    }
}

@Composable
private fun PaymentRecordCard(payment: AdminPaymentRecord) {
    val statusColor = when (payment.paymentStatus) {
        PaymentStatus.PAID -> SuccessGreen
        PaymentStatus.PENDING -> WarningAmber
        PaymentStatus.REFUNDED -> RoyalBlue
        PaymentStatus.FAILED -> ErrorRed
        PaymentStatus.PARTIAL_REFUND -> TealAccent
    }

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Receipt, null, tint = RoyalBlue, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(payment.transactionId, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrimaryNavy))
                    Text(payment.bookingId, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary, fontSize = 11.sp))
                }
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(statusColor.copy(0.12f)).padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(payment.paymentStatus.label, style = MaterialTheme.typography.labelSmall.copy(color = statusColor, fontWeight = FontWeight.Bold, fontSize = 10.sp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(10.dp))

            // Payment breakdown
            PaymentRow("Passenger", payment.passengerName)
            PaymentRow("Driver", payment.driverName)
            PaymentRow("Total Fare", "₹${"%.2f".format(payment.totalFare)}", isBold = true)
            PaymentRow("Advance Paid (10%)", "₹${"%.2f".format(payment.advancePayment)}")
            PaymentRow("Remaining Balance", "₹${"%.2f".format(payment.remainingPayment)}")
            PaymentRow("Platform Commission (10%)", "₹${"%.2f".format(payment.platformCommission)}", valueColor = WarningAmber)
            PaymentRow("Driver Payout", "₹${"%.2f".format(payment.driverPayout)}", valueColor = SuccessGreen, isBold = true)
            PaymentRow("Payment Date", payment.paymentDate)

            if (payment.refundAmount > 0) {
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp)).background(Color(0xFFEBF2FE)).padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, null, tint = RoyalBlue, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Refund: ₹${"%.2f".format(payment.refundAmount)} (5% of total fare for cancellation >1 hr before departure)",
                        style = MaterialTheme.typography.labelSmall.copy(color = RoyalBlue, fontSize = 11.sp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "⚠ No refunds have been processed. Actual payment gateway integration pending backend development.",
                style = MaterialTheme.typography.labelSmall.copy(color = TextMuted, fontSize = 10.sp)
            )
        }
    }
}

@Composable
private fun PaymentRow(label: String, value: String, isBold: Boolean = false, valueColor: Color = PrimaryNavy) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall.copy(color = TextSecondary))
        Text(
            value,
            style = MaterialTheme.typography.labelSmall.copy(
                color = valueColor,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
            )
        )
    }
}

