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
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
 * Implements professional UI states for all 7 statuses:
 * - NOT_SUBMITTED
 * - PENDING_VERIFICATION
 * - UNDER_REVIEW
 * - APPROVED
 * - REJECTED
 * - SUSPENDED
 * - EXPIRED
 * Note: Clearly displays that the application is awaiting real admin review
 * and never generates fake verification successes.
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
    val status = application.status

    val (statusColor, statusBg, statusIcon, statusHeadline, statusExplanation) = when (status) {
        DriverVerificationStatus.NOT_SUBMITTED -> Quintuple(
            RoyalBlue,
            Color(0xFFEFF6FF),
            Icons.Default.Info,
            "Onboarding Incomplete",
            "You have not completed your commercial driver registration. Submit your driving licence and vehicle RC to begin airport operations."
        )

        DriverVerificationStatus.PENDING_VERIFICATION -> Quintuple(
            WarningAmber,
            Color(0xFFFEF3C7),
            Icons.Default.Schedule,
            "Verification Documents Queued",
            "Your registration documents have been uploaded and are queued in the verification pipeline. Government database cross-checks are pending."
        )

        DriverVerificationStatus.UNDER_REVIEW -> Quintuple(
            RoyalBlue,
            Color(0xFFDBEAFE),
            Icons.Default.HourglassTop,
            "Under Admin Review",
            "Your application (#${application.applicationId}) is currently being reviewed by the RideMitra Airport Operations Compliance Desk. Expected turnaround: 24–48 hours."
        )

        DriverVerificationStatus.APPROVED -> Quintuple(
            SuccessGreen,
            Color(0xFFE6F9F2),
            Icons.Default.CheckCircle,
            "Application Approved & Active",
            "Your driving licence, background check, and commercial vehicle papers have been verified. You are eligible to receive airport ride dispatches."
        )

        DriverVerificationStatus.REJECTED -> Quintuple(
            ErrorRed,
            Color(0xFFFEF2F2),
            Icons.Default.Error,
            "Action Required on Application",
            "One or more submitted documents could not be verified by our compliance team. Please review admin remarks and re-upload clear copies."
        )

        DriverVerificationStatus.SUSPENDED -> Quintuple(
            ErrorRed,
            Color(0xFFFEE2E2),
            Icons.Default.Warning,
            "Account Suspended",
            "Your driver partner account has been temporarily suspended due to a compliance or safety audit. Please contact RideMitra Driver Support immediately."
        )

        DriverVerificationStatus.EXPIRED -> Quintuple(
            WarningAmber,
            Color(0xFFFEF3C7),
            Icons.Default.Warning,
            "Regulatory Documents Expired",
            "Your commercial insurance, fitness certificate, or driving licence has passed its expiration date. Re-upload updated valid documents to resume accepting airport trips."
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
                text = status.label.uppercase(),
                variant = when (status) {
                    DriverVerificationStatus.APPROVED -> BadgeVariant.SUCCESS
                    DriverVerificationStatus.REJECTED, DriverVerificationStatus.SUSPENDED -> BadgeVariant.WARNING
                    DriverVerificationStatus.UNDER_REVIEW, DriverVerificationStatus.PENDING_VERIFICATION -> BadgeVariant.ROYAL
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

            Spacer(modifier = Modifier.height(20.dp))

            // Admin Feedback Box (If rejected, expired, or remarks available)
            if (status == DriverVerificationStatus.REJECTED || application.adminFeedback != null) {
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
                            text = application.adminFeedback ?: "Driving Licence scan was blurry or unreadable. Please re-upload a high-resolution front and back photo.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF991B1B),
                                lineHeight = 18.sp
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

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
                    DetailLineRow("Driver Name", application.personalInfo.fullName.ifBlank { "Rajesh Kumar" })
                    DetailLineRow("Registered Vehicle", "${application.vehicleDetails.manufacturer} ${application.vehicleDetails.model}".trim().ifBlank { "Maruti Dzire" })
                    DetailLineRow("Vehicle Plate", application.vehicleDetails.registrationNumber.ifBlank { "DL 01 AB 9482" })
                    DetailLineRow("Seating Rule", "Max ${application.vehicleDetails.allowedRideMitraCapacity} Riders (N-1 Enforced)")
                    DetailLineRow("Submitted On", if (application.submissionDate.isNotBlank()) application.submissionDate else "Pending Submission")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Background Verification Policy Assurance
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
                            text = "Authentic Airport Compliance",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Uploading documents does not mean instant automated approval. Every vehicle and licence is audited by airport ground operators to guarantee passenger safety.",
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

            // Primary contextual actions based on status
            when (status) {
                DriverVerificationStatus.NOT_SUBMITTED -> {
                    RideMitraButton(
                        text = "Complete Driver Registration",
                        onClick = onStartOrResumeRegistration,
                        gradient = PrimaryGradient,
                        trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                    )
                }

                DriverVerificationStatus.APPROVED -> {
                    RideMitraButton(
                        text = "Enter Driver Dashboard",
                        onClick = onEnterDashboardIfApproved,
                        gradient = HeroTealGradient,
                        trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
                    )
                }

                DriverVerificationStatus.REJECTED, DriverVerificationStatus.EXPIRED -> {
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

            // Contact Support action
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
private fun DetailLineRow(label: String, value: String) {
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
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                fontSize = 11.sp
            )
        )
    }
}
