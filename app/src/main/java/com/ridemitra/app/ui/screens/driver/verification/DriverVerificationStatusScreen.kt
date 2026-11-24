package com.ridemitra.app.ui.screens.driver.verification

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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverApplication
import com.ridemitra.app.domain.model.DriverVerificationStatus
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
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
 * Driver Verification Status Screen.
 * Strictly maintains and visualizes separate statuses:
 * - NOT_SUBMITTED: Registration form incomplete
 * - UPLOADED: Documents uploaded, ready for submission
 * - PENDING_REVIEW: Submitted, under compliance desk review
 * - APPROVED: Active & approved (blocked if documents expired)
 * - REJECTED: Changes required by compliance desk
 * - EXPIRED: Regulatory document(s) expired; booking eligibility blocked
 * - SUSPENDED: Temporarily suspended for audit
 */
@Composable
fun DriverVerificationStatusScreen(
    application: DriverApplication,
    onStartOrResumeRegistration: () -> Unit,
    onEnterDashboardIfApproved: () -> Unit,
    onReUploadDocuments: (step: Int) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val expiredDocs = remember(application) { application.getExpiredDocuments() }
    val effectiveStatus = if (application.hasExpiredDocuments && application.status == DriverVerificationStatus.APPROVED) {
        DriverVerificationStatus.EXPIRED
    } else {
        application.status
    }

    val (statusColor, statusBg, statusIcon, statusHeadline, statusExplanation) = when (effectiveStatus) {
        DriverVerificationStatus.NOT_SUBMITTED -> Quintuple(
            RoyalBlue,
            Color(0xFFEFF6FF),
            Icons.Default.Info,
            "Registration Incomplete",
            "You have not completed your commercial driver registration. Submit your personal details, driving licence, and vehicle RC to begin onboarding."
        )

        DriverVerificationStatus.UPLOADED -> Quintuple(
            RoyalBlue,
            Color(0xFFEFF6FF),
            Icons.Default.UploadFile,
            "Documents Uploaded",
            "Your registration details and document scans have been saved. Review your application and submit it for compliance review."
        )

        DriverVerificationStatus.PENDING_REVIEW -> Quintuple(
            WarningAmber,
            Color(0xFFFEF3C7),
            Icons.Default.HourglassTop,
            "Pending Compliance Review",
            "Your application (#${application.applicationId}) is currently being reviewed by the RideMitra compliance team. Review takes approximately 24–48 hours."
        )

        DriverVerificationStatus.APPROVED -> Quintuple(
            SuccessGreen,
            Color(0xFFE6F9F2),
            Icons.Default.CheckCircle,
            "Application Approved",
            "Your driving licence, vehicle documents, and identity details have been verified. You are authorized to accept airport passenger dispatches."
        )

        DriverVerificationStatus.REJECTED -> Quintuple(
            ErrorRed,
            Color(0xFFFEF2F2),
            Icons.Default.Error,
            "Action Required / Rejected",
            "One or more submitted documents did not meet verification criteria. Please review compliance remarks below and re-upload clear documents."
        )

        DriverVerificationStatus.EXPIRED -> Quintuple(
            ErrorRed,
            Color(0xFFFEF2F2),
            Icons.Default.Warning,
            "Regulatory Document(s) Expired",
            "One or more required documents have expired. Booking eligibility is strictly blocked until valid renewed documents are submitted and verified."
        )

        DriverVerificationStatus.SUSPENDED -> Quintuple(
            ErrorRed,
            Color(0xFFFEE2E2),
            Icons.Default.Warning,
            "Account Suspended",
            "Your driver partner account has been temporarily suspended due to a compliance or safety audit. Please contact RideMitra Driver Support."
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Verification Status",
            subtitle = "Application #${application.applicationId}",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Big Status Emblem
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(statusBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(46.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = statusHeadline,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryNavy
                ),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            StatusBadge(
                text = effectiveStatus.label.uppercase(),
                variant = when (effectiveStatus) {
                    DriverVerificationStatus.APPROVED -> BadgeVariant.SUCCESS
                    DriverVerificationStatus.REJECTED, DriverVerificationStatus.EXPIRED, DriverVerificationStatus.SUSPENDED -> BadgeVariant.WARNING
                    DriverVerificationStatus.PENDING_REVIEW, DriverVerificationStatus.UPLOADED -> BadgeVariant.ROYAL
                    else -> BadgeVariant.NEUTRAL
                }
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = statusExplanation,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondary,
                    lineHeight = 22.sp
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            // Expired Documents Alert Box
            if (expiredDocs.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFFFEF2F2),
                    borderColor = Color(0xFFFECDD3),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Error,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Dispatch & Booking Eligibility Blocked",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ErrorRed
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        expiredDocs.forEach { exp ->
                            Text(
                                text = "• $exp",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF991B1B),
                                    fontSize = 11.sp,
                                    lineHeight = 17.sp
                                )
                            )
                        }
                    }
                }
            }

            // Compliance Remarks Box
            if (application.adminFeedback != null && effectiveStatus == DriverVerificationStatus.REJECTED) {
                Spacer(modifier = Modifier.height(16.dp))
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFFFEF2F2),
                    borderColor = Color(0xFFFECDD3),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Compliance Desk Remarks",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = ErrorRed
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = application.adminFeedback,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF991B1B),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Application Summary Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Submission Details",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DetailLineRow("Application ID", application.applicationId)
                    DetailLineRow("Driver Name", application.personalInfo.fullName.ifBlank { "Not Submitted" })
                    DetailLineRow("Vehicle Plate", application.vehicleDetails.registrationNumber.ifBlank { "Not Submitted" })
                    DetailLineRow(
                        label = "Passenger Capacity",
                        value = "${application.vehicleDetails.passengerCapacityExclDriver} Seats (Max ${application.vehicleDetails.allowedRideMitraCapacity} Riders - N-1 Rule)"
                    )
                    DetailLineRow("Booking Eligibility", if (application.isEligibleForBookings) "ELIGIBLE" else "BLOCKED", isHighlight = application.isEligibleForBookings)
                    DetailLineRow("Submitted On", if (application.submissionDate.isNotBlank()) application.submissionDate else "Pending Submission")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Verification Policy Notice (No false authority/encryption claims)
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFF8FAFC),
                borderColor = BorderLight,
                elevation = 1.dp
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "RideMitra Safety & Document Verification",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Uploading documents does not mean automatic approval. Every vehicle and licence is audited by the RideMitra compliance team to guarantee passenger safety and regulatory adherence.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Actions based on status
            when (effectiveStatus) {
                DriverVerificationStatus.NOT_SUBMITTED -> {
                    RideMitraButton(
                        text = "Complete Driver Registration",
                        onClick = onStartOrResumeRegistration,
                        gradient = PrimaryGradient,
                        trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                    )
                }

                DriverVerificationStatus.UPLOADED -> {
                    RideMitraButton(
                        text = "Review & Submit Application",
                        onClick = onStartOrResumeRegistration,
                        gradient = PrimaryGradient,
                        trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                    )
                }

                DriverVerificationStatus.APPROVED -> {
                    if (application.isEligibleForBookings) {
                        RideMitraButton(
                            text = "Enter Driver Dashboard",
                            onClick = onEnterDashboardIfApproved,
                            gradient = HeroTealGradient,
                            trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                        )
                    } else {
                        RideMitraButton(
                            text = "Renew Expired Documents",
                            onClick = { onReUploadDocuments(application.reUploadRequiredStep ?: 5) },
                            gradient = PrimaryGradient,
                            trailingIcon = Icons.Default.Refresh
                        )
                    }
                }

                DriverVerificationStatus.EXPIRED -> {
                    RideMitraButton(
                        text = "Update Expired Documents",
                        onClick = { onReUploadDocuments(application.reUploadRequiredStep ?: 5) },
                        gradient = PrimaryGradient,
                        trailingIcon = Icons.Default.Refresh
                    )
                }

                DriverVerificationStatus.REJECTED -> {
                    RideMitraButton(
                        text = "Re-Upload Required Documents",
                        onClick = { onReUploadDocuments(application.reUploadRequiredStep ?: 3) },
                        gradient = PrimaryGradient,
                        trailingIcon = Icons.Default.Refresh
                    )
                }

                else -> {
                    RideMitraOutlinedButton(
                        text = "Review Submitted Documents",
                        onClick = onStartOrResumeRegistration,
                        borderColor = RoyalBlue,
                        textColor = RoyalBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Contact Support
            RideMitraOutlinedButton(
                text = "Contact Driver Help Desk",
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:18001024829"))
                        context.startActivity(intent)
                    } catch (_: Exception) {}
                },
                borderColor = BorderLight,
                textColor = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private data class Quintuple<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

@Composable
private fun DetailLineRow(
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
                fontSize = 11.sp
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
                color = if (isHighlight) SuccessGreen else TextPrimary,
                fontSize = 11.sp
            )
        )
    }
}
