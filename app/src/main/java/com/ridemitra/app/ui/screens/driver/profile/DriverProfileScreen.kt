package com.ridemitra.app.ui.screens.driver.profile

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverApplication
import com.ridemitra.app.domain.model.DriverVerificationStatus
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.DividerLight
import com.ridemitra.app.ui.theme.ErrorRed
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary
import com.ridemitra.app.ui.theme.WarningAmber

@Composable
fun DriverProfileScreen(
    application: DriverApplication,
    onEditSection: (stepIndex: Int) -> Unit,
    onViewVerificationStatus: () -> Unit,
    onSwitchRole: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var showSupportDialog by remember { mutableStateOf(false) }
    var showPolicyDialog by remember { mutableStateOf<String?>(null) }

    val personal = application.personalInfo
    val vehicle = application.vehicleDetails
    val license = application.licenseInfo

    val displayName = if (personal.fullName.isNotBlank()) personal.fullName else "Rajesh Kumar"
    val displayPhone = if (personal.mobileNumber.isNotBlank()) personal.mobileNumber else "+91 98765 43210"
    val displayEmail = if (personal.email.isNotBlank()) personal.email else "rajesh.kumar@example.com"
    val displayVehiclePlate = if (vehicle.registrationNumber.isNotBlank()) vehicle.registrationNumber else "KA 01 MJ 4829"
    val displayVehicleModel = if (vehicle.manufacturer.isNotBlank()) "${vehicle.manufacturer} ${vehicle.model}" else "Toyota Etios (Sedan)"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Driver Identity Card Header
        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = PrimaryNavy
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(RoyalBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Photo",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = "Verified Driver",
                                tint = TealAccent,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Text(
                            text = displayPhone,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Rating",
                                tint = WarningAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "4.88 (312 rides)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.9f),
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Divider(color = Color.White.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Driver ID: ${application.applicationId}",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        )
                        Text(
                            text = "Status: ${application.status.label}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (application.status == DriverVerificationStatus.APPROVED) SuccessGreen else WarningAmber,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    RideMitraOutlinedButton(
                        text = "Check Status",
                        onClick = onViewVerificationStatus
                    )
                }
            }
        }

        // Vehicle & N-1 Seat Capacity Policy Card
        Text(
            text = "Registered Commercial Vehicle",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )
        )

        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CardBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(RoyalBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.DirectionsCar,
                                contentDescription = null,
                                tint = RoyalBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = displayVehiclePlate,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = displayVehicleModel,
                                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                            )
                        }
                    }

                    TextButton(onClick = { onEditSection(4) }) {
                        Text(text = "Edit Vehicle", color = RoyalBlue, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = DividerLight)
                Spacer(modifier = Modifier.height(12.dp))

                // Strict N-1 Seat Rule Enforcement Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(TealAccent.copy(alpha = 0.08f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = TealAccent,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "RideMitra N-1 Seat Rule Enforced",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "Registered passenger seats: ${vehicle.passengerCapacityExclDriver}  |  RideMitra max capacity: ${vehicle.allowedRideMitraCapacity} passengers (1 middle seat kept empty for passenger comfort and airport luggage).",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }
        }

        // Documents & Compliance
        Text(
            text = "Licence & Regulatory Documents",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )
        )

        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CardBackground
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                DocumentItemRow(
                    title = "Commercial Driving Licence",
                    subtitle = if (license.licenseNumber.isNotBlank()) license.licenseNumber else "DL-0420210084920",
                    isVerified = true,
                    onReplace = { onEditSection(3) }
                )

                Divider(color = DividerLight, modifier = Modifier.padding(vertical = 10.dp))

                DocumentItemRow(
                    title = "Identity Document (Aadhaar / PAN)",
                    subtitle = "Government ID Masked & Securely Stored",
                    isVerified = true,
                    onReplace = { onEditSection(2) }
                )

                Divider(color = DividerLight, modifier = Modifier.padding(vertical = 10.dp))

                DocumentItemRow(
                    title = "Vehicle RC & Commercial Insurance",
                    subtitle = "Active Policy Verified",
                    isVerified = true,
                    onReplace = { onEditSection(5) }
                )
            }
        }

        // Support & Legal Actions
        Text(
            text = "Driver Support & Settings",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = PrimaryNavy
            )
        )

        RideMitraCard(
            modifier = Modifier.fillMaxWidth(),
            backgroundColor = CardBackground
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                ActionRowItem(
                    icon = Icons.Default.Call,
                    title = "24x7 Driver Emergency Helpline",
                    subtitle = "Dial 1800-420-MITRA (Toll Free)",
                    iconColor = SuccessGreen,
                    onClick = { showSupportDialog = true }
                )

                Divider(color = DividerLight)

                ActionRowItem(
                    icon = Icons.Default.Description,
                    title = "Driver Partner Terms of Service",
                    subtitle = "Payout terms, solo subsidy rules & zero commission guarantee",
                    iconColor = RoyalBlue,
                    onClick = { showPolicyDialog = "TERMS" }
                )

                Divider(color = DividerLight)

                ActionRowItem(
                    icon = Icons.Default.PrivacyTip,
                    title = "Data Privacy & Location Policy",
                    subtitle = "Airport geo-fence tracking & secure Aadhaar masking",
                    iconColor = PrimaryNavy,
                    onClick = { showPolicyDialog = "PRIVACY" }
                )

                Divider(color = DividerLight)

                ActionRowItem(
                    icon = Icons.Default.SwapHoriz,
                    title = "Switch Role / Exit Driver Mode",
                    subtitle = "Return to Passenger or Gateway view",
                    iconColor = WarningAmber,
                    onClick = onSwitchRole
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

    // Support Dialog
    if (showSupportDialog) {
        AlertDialog(
            onDismissRequest = { showSupportDialog = false },
            title = {
                Text(
                    text = "24x7 Driver Priority Support",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            },
            text = {
                Column {
                    Text(
                        text = "Need assistance with airport pickup pillars, toll refunds, or passenger verification?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "• Helpline: 1800-420-MITRA\n• Direct WhatsApp: +91 80 4920 0000\n• Airport Desk: Terminal 1, Pillar 4B & Terminal 2, Pillar 8",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            },
            confirmButton = {
                RideMitraButton(
                    text = "Call Helpline",
                    onClick = {
                        showSupportDialog = false
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:180042064872"))
                        context.startActivity(intent)
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showSupportDialog = false }) {
                    Text("Close")
                }
            }
        )
    }

    // Policy Dialog
    showPolicyDialog?.let { type ->
        AlertDialog(
            onDismissRequest = { showPolicyDialog = null },
            title = {
                Text(
                    text = if (type == "TERMS") "Driver Partner Terms" else "Privacy & Data Protection",
                    fontWeight = FontWeight.Bold,
                    color = PrimaryNavy
                )
            },
            text = {
                Column {
                    Text(
                        text = if (type == "TERMS") {
                            "1. Fare Transparency: RideMitra maintains a transparent platform fee model with zero hidden cuts.\n\n" +
                            "2. 100% Solo Subsidy Guarantee: When RideMitra offers a 10% promo discount to solo passengers, 100% of the discount is absorbed by RideMitra. The driver's agreed payout is completely protected.\n\n" +
                            "3. N-1 Capacity Compliance: To ensure luggage safety and comfort, drivers must strictly respect the N-1 passenger ceiling.\n\n" +
                            "4. Advance & Cancellation: Advance payments are settled directly. Cancellations within one hour are non-refundable to the passenger."
                        } else {
                            "1. Document Security: Identity documents like Aadhaar and PAN cards are strictly encrypted and masked. Government identity numbers are never displayed in full to third parties.\n\n" +
                            "2. Location Privacy: High-accuracy location telemetry is only activated when you are set to 'ONLINE' and while an active airport trip is underway."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            },
            confirmButton = {
                RideMitraButton(
                    text = "I Understand",
                    onClick = { showPolicyDialog = null }
                )
            }
        )
    }
}

@Composable
private fun DocumentItemRow(
    title: String,
    subtitle: String,
    isVerified: Boolean,
    onReplace: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                )
                if (isVerified) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Verified",
                        tint = SuccessGreen,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
            )
        }

        TextButton(onClick = onReplace) {
            Text(text = "Re-upload", color = RoyalBlue, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ActionRowItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(iconColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}
