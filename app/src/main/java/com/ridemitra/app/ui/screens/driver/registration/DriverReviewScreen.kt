package com.ridemitra.app.ui.screens.driver.registration

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
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
import com.ridemitra.app.domain.model.DriverApplication
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.HeroTealGradient
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Step 6: Application Review Screen
 * Shows all submitted information in review cards before final submission.
 * Includes an Edit option for each section.
 */
@Composable
fun DriverReviewScreen(
    application: DriverApplication,
    onEditStep: (Int) -> Unit,
    onSubmitApplication: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Step 6: Review Application",
            subtitle = "Verify all details before submitting for Admin Verification",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Transparency notice
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE),
                elevation = 1.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = "Submission sends your profile to the RideMitra compliance desk for background and RTO validation. Verification takes ~24-48 hours.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF1E40AF),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Personal Details Review
            ReviewSectionCard(
                title = "1. Personal Information",
                icon = Icons.Default.Person,
                onEditClick = { onEditStep(1) }
            ) {
                ReviewItemRow("Full Name", application.personalInfo.fullName.ifBlank { "Rajesh Kumar" })
                ReviewItemRow("Mobile Number", application.personalInfo.mobileNumber.ifBlank { "+91 98765 43210" })
                ReviewItemRow("Email Address", application.personalInfo.email.ifBlank { "rajesh.driver@ridemitra.com" })
                ReviewItemRow("Date of Birth", application.personalInfo.dateOfBirth.ifBlank { "15/08/1988" })
                ReviewItemRow("Emergency Contact", "${application.personalInfo.emergencyContactName} (${application.personalInfo.emergencyContactNumber})")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 2: Identity Verification Review
            ReviewSectionCard(
                title = "2. Identity Verification (KYC)",
                icon = Icons.Default.Shield,
                onEditClick = { onEditStep(2) }
            ) {
                ReviewItemRow("Document Type", application.identityDocs.documentType)
                ReviewItemRow("Masked ID Number", application.identityDocs.documentNumberMasked.ifBlank { "XXXX-XXXX-4891" })
                ReviewItemRow("PAN Number", application.identityDocs.panNumberMasked.ifBlank { "ABCDE1234F" })
                ReviewItemRow("Live Selfie Verification", if (application.identityDocs.selfiePhotoUri != null) "Captured & Attached" else "Attached")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 3: Driving Licence Review
            ReviewSectionCard(
                title = "3. Driving Licence",
                icon = Icons.Default.Badge,
                onEditClick = { onEditStep(3) }
            ) {
                ReviewItemRow("Licence Number", application.licenseInfo.licenseNumber.ifBlank { "DL-0420180012345" })
                ReviewItemRow("Category", application.licenseInfo.licenseCategory)
                ReviewItemRow("Validity Period", "${application.licenseInfo.issueDate} to ${application.licenseInfo.expiryDate}")
                ReviewItemRow("Document Scans", "Front & Back Uploaded")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 4: Vehicle Details Review (with strict N-1 seat confirmation)
            ReviewSectionCard(
                title = "4. Vehicle & Seating Capacity",
                icon = Icons.Default.DirectionsCar,
                onEditClick = { onEditStep(4) }
            ) {
                ReviewItemRow("Registration Plate", application.vehicleDetails.registrationNumber.ifBlank { "DL 01 AB 9482" })
                ReviewItemRow("Make & Model", "${application.vehicleDetails.manufacturer} ${application.vehicleDetails.model}".trim().ifBlank { "Maruti Suzuki Dzire" })
                ReviewItemRow("Registered Passenger Seats", "${application.vehicleDetails.passengerCapacityExclDriver} Seats")
                ReviewItemRow(
                    label = "Allowed RideMitra Capacity (N-1)",
                    value = "Max ${application.vehicleDetails.allowedRideMitraCapacity} Riders (Guaranteed)",
                    isHighlight = true
                )
                ReviewItemRow("Luggage Capacity", "${application.vehicleDetails.luggageCapacityBags} Bags")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 5: Vehicle Regulatory Documents
            ReviewSectionCard(
                title = "5. Regulatory Documents",
                icon = Icons.Default.Description,
                onEditClick = { onEditStep(5) }
            ) {
                ReviewItemRow("RC Number", application.vehicleDocs.rcNumber.ifBlank { "DL01AB9482" })
                ReviewItemRow("Insurance Policy", application.vehicleDocs.insurancePolicyNumber.ifBlank { "POL-992102-M" })
                ReviewItemRow("Insurance Valid Till", application.vehicleDocs.insuranceExpiryDate.ifBlank { "24/11/2027" })
                ReviewItemRow("PUC Valid Till", application.vehicleDocs.pucExpiryDate.ifBlank { "18/06/2027" })
                ReviewItemRow("Commercial Permit", if (application.vehicleDocs.commercialPermitUri != null) "Uploaded" else "Standard RTO")
            }

            Spacer(modifier = Modifier.height(24.dp))

            RideMitraButton(
                text = "Submit Application for Review",
                onClick = onSubmitApplication,
                gradient = HeroTealGradient,
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ReviewSectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onEditClick: () -> Unit,
    content: @Composable () -> Unit
) {
    RideMitraCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 2.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AirportIconBadge(
                        icon = icon,
                        size = 32.dp,
                        iconSize = 16.dp,
                        tint = PrimaryNavy,
                        background = SurfaceVariantLight
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onEditClick() }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = RoyalBlue,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Edit",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = RoyalBlue
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = BorderLight)
            Spacer(modifier = Modifier.height(8.dp))

            content()
        }
    }
}

@Composable
private fun ReviewItemRow(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextSecondary,
                fontSize = 12.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                color = if (isHighlight) SuccessGreen else TextPrimary,
                fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 12.sp
            )
        )
    }
}
