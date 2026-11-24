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
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverApplication
import com.ridemitra.app.domain.validation.DocumentValidator
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.ErrorRed
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
import com.ridemitra.app.ui.theme.WarningAmber

/**
 * Step 6: Application Review Screen
 * Features:
 * - Comprehensive summary across all 5 onboarding steps.
 * - Inspects document expiry dates before submission.
 * - Enforces RideMitra N-1 seat capacity rule.
 * - Submits application for manual compliance review (NEVER auto-approves).
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
    val expiredDocuments = remember(application) { application.getExpiredDocuments() }
    val hasExpiredDocs = expiredDocuments.isNotEmpty()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Step 6: Review Application",
            subtitle = "Verify all details before submitting for Compliance Review",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Transparency notice: No automatic approval
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
                        text = "Submission sends your profile to the RideMitra compliance desk for background and document validation. Applications are reviewed manually and are never automatically approved.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF1E40AF),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            // Warning banner if any document is expired
            if (hasExpiredDocs) {
                Spacer(modifier = Modifier.height(14.dp))
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFFFEF2F2),
                    borderColor = Color(0xFFFECDD3),
                    elevation = 2.dp
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Action Required: Expired Document(s) Detected",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ErrorRed
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        expiredDocuments.forEach { err ->
                            Text(
                                text = "• $err",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF991B1B),
                                    fontSize = 11.sp
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Please update the expired document(s) by clicking Edit on the respective section below before submitting.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 10.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 1: Personal Details Review
            ReviewSectionCard(
                title = "1. Personal Information",
                icon = Icons.Default.Person,
                onEditClick = { onEditStep(1) }
            ) {
                ReviewItemRow("Full Name", application.personalInfo.fullName.ifBlank { "Not Provided" })
                ReviewItemRow("Mobile Number", application.personalInfo.mobileNumber.ifBlank { "Not Provided" })
                ReviewItemRow("Mobile Status", if (application.personalInfo.isMobileVerified) "OTP Verified" else "Unverified", isHighlight = application.personalInfo.isMobileVerified)
                ReviewItemRow("Email Address", application.personalInfo.email.ifBlank { "Not Provided" })
                ReviewItemRow("Date of Birth", application.personalInfo.dateOfBirth.ifBlank { "Not Provided" })
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
                val panText = if (application.identityDocs.panNumberMasked.isNotBlank())
                    application.identityDocs.panNumberMasked
                else if (application.identityDocs.isPanConfiguredMandatory)
                    "Required (Pending)"
                else
                    "Optional (Not Provided)"
                ReviewItemRow("PAN Number", panText)
                ReviewItemRow("Live Selfie Verification", if (application.identityDocs.selfiePhotoUri != null) "Captured & Attached" else "Pending")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 3: Driving Licence Review
            val dlExpiryCheck = DocumentValidator.validateExpiry(application.licenseInfo.expiryDate, "Driving Licence")
            ReviewSectionCard(
                title = "3. Driving Licence",
                icon = Icons.Default.Badge,
                onEditClick = { onEditStep(3) }
            ) {
                ReviewItemRow("Licence Number", application.licenseInfo.licenseNumber.ifBlank { "Not Provided" })
                ReviewItemRow("Category", application.licenseInfo.licenseCategory)
                ReviewItemRow("Issue Date", application.licenseInfo.issueDate.ifBlank { "Not Provided" })
                ReviewItemRow(
                    label = "Expiry Date",
                    value = application.licenseInfo.expiryDate.ifBlank { "Not Provided" } + if (dlExpiryCheck.isExpired) " (EXPIRED)" else " (Valid)",
                    isError = dlExpiryCheck.isExpired,
                    isHighlight = !dlExpiryCheck.isExpired
                )
                ReviewItemRow("Document Scans", if (application.licenseInfo.licenseFrontUri != null && application.licenseInfo.licenseBackUri != null) "Front & Back Attached" else "Incomplete")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 4: Vehicle Details Review (Strict N-1 Seat Confirmation)
            ReviewSectionCard(
                title = "4. Vehicle & Capacity",
                icon = Icons.Default.DirectionsCar,
                onEditClick = { onEditStep(4) }
            ) {
                ReviewItemRow("Registration Plate", application.vehicleDetails.registrationNumber.ifBlank { "Not Provided" })
                ReviewItemRow("Make & Model", "${application.vehicleDetails.manufacturer} ${application.vehicleDetails.model}".trim().ifBlank { "Not Provided" })
                ReviewItemRow("Manufacturing Year", application.vehicleDetails.manufacturingYear.ifBlank { "Not Provided" })
                ReviewItemRow("Registered Passenger Seats", "${application.vehicleDetails.passengerCapacityExclDriver} Seats (Excl. Driver)")
                ReviewItemRow(
                    label = "RideMitra Capacity (N-1 Rule)",
                    value = "Max ${application.vehicleDetails.allowedRideMitraCapacity} Riders (Guaranteed)",
                    isHighlight = true
                )
                ReviewItemRow("Luggage Capacity", "${application.vehicleDetails.luggageCapacityBags} Large Bags")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Section 5: Vehicle Regulatory Documents
            val insExpiryCheck = DocumentValidator.validateExpiry(application.vehicleDocs.insuranceExpiryDate, "Motor Insurance")
            val pucExpiryCheck = DocumentValidator.validateExpiry(application.vehicleDocs.pucExpiryDate, "PUC Certificate")
            ReviewSectionCard(
                title = "5. Regulatory Documents",
                icon = Icons.Default.Description,
                onEditClick = { onEditStep(5) }
            ) {
                ReviewItemRow("RC Number", application.vehicleDocs.rcNumber.ifBlank { "Not Provided" })
                ReviewItemRow("Insurance Policy", application.vehicleDocs.insurancePolicyNumber.ifBlank { "Not Provided" })
                ReviewItemRow(
                    label = "Insurance Expiry",
                    value = application.vehicleDocs.insuranceExpiryDate.ifBlank { "Not Provided" } + if (insExpiryCheck.isExpired) " (EXPIRED)" else " (Valid)",
                    isError = insExpiryCheck.isExpired,
                    isHighlight = !insExpiryCheck.isExpired
                )
                ReviewItemRow(
                    label = "PUC Expiry",
                    value = application.vehicleDocs.pucExpiryDate.ifBlank { "Not Provided" } + if (pucExpiryCheck.isExpired) " (EXPIRED)" else " (Valid)",
                    isError = pucExpiryCheck.isExpired,
                    isHighlight = !pucExpiryCheck.isExpired
                )
                if (application.vehicleDocs.commercialPermitExpiryDate.isNotBlank()) {
                    val permCheck = DocumentValidator.validateExpiry(application.vehicleDocs.commercialPermitExpiryDate, "Commercial Permit", isRequired = false)
                    ReviewItemRow(
                        label = "Commercial Permit",
                        value = "${application.vehicleDocs.commercialPermitExpiryDate} ${if (permCheck.isExpired) "(EXPIRED)" else "(Valid)"}",
                        isError = permCheck.isExpired
                    )
                }
                if (application.vehicleDocs.fitnessExpiryDate.isNotBlank()) {
                    val fitCheck = DocumentValidator.validateExpiry(application.vehicleDocs.fitnessExpiryDate, "Fitness Certificate", isRequired = false)
                    ReviewItemRow(
                        label = "Fitness Certificate",
                        value = "${application.vehicleDocs.fitnessExpiryDate} ${if (fitCheck.isExpired) "(EXPIRED)" else "(Valid)"}",
                        isError = fitCheck.isExpired
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            RideMitraButton(
                text = if (hasExpiredDocs) "Cannot Submit: Documents Expired" else "Submit Application for Review",
                onClick = onSubmitApplication,
                gradient = if (hasExpiredDocs) PrimaryGradient else HeroTealGradient,
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
    isHighlight: Boolean = false,
    isError: Boolean = false
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
                color = if (isError) ErrorRed else if (isHighlight) SuccessGreen else TextPrimary,
                fontWeight = if (isError || isHighlight) FontWeight.Bold else FontWeight.SemiBold,
                fontSize = 12.sp
            )
        )
    }
}
