package com.ridemitra.app.ui.screens.driver.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverLicenseInfo
import com.ridemitra.app.ui.components.DocumentUploadBox
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraTextField
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary
import com.ridemitra.app.ui.theme.WarningAmber

/**
 * Step 3: Driving Licence Verification
 * Collects:
 * - Driving licence number
 * - Licence front image
 * - Licence back image
 * - Issue date
 * - Expiry date
 * - Licence category
 * - Form & Expiry validation
 */
@Composable
fun DriverLicenseScreen(
    initialData: DriverLicenseInfo,
    onSaveAndNext: (DriverLicenseInfo) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var licenseNumber by remember { mutableStateOf(initialData.licenseNumber) }
    var licenseFrontUri by remember { mutableStateOf(initialData.licenseFrontUri) }
    var licenseBackUri by remember { mutableStateOf(initialData.licenseBackUri) }
    var issueDate by remember { mutableStateOf(initialData.issueDate) }
    var expiryDate by remember { mutableStateOf(initialData.expiryDate) }
    var licenseCategory by remember { mutableStateOf(initialData.licenseCategory) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Driver Onboarding (Step 3/5)",
            subtitle = "Driving Licence Verification",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Requirement banner
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFFEF3C7),
                borderColor = Color(0xFFFDE68A),
                elevation = 1.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = WarningAmber,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Text(
                        text = "Must possess a valid Indian Driving Licence authorised for Light Motor Vehicle (LMV) Transport or Commercial operations.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF92400E),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Licence Info Form
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Licence Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = licenseNumber,
                        onValueChange = { licenseNumber = it.uppercase() },
                        label = "Driving Licence Number *",
                        placeholder = "DL-0420180012345",
                        leadingIcon = Icons.Default.Badge
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = licenseCategory,
                        onValueChange = { licenseCategory = it },
                        label = "Licence Category *",
                        placeholder = "LMV Transport / Commercial Cab"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = issueDate,
                                onValueChange = { issueDate = it },
                                label = "Issue Date *",
                                placeholder = "DD/MM/YYYY"
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = expiryDate,
                                onValueChange = { expiryDate = it },
                                label = "Expiry Date *",
                                placeholder = "DD/MM/YYYY"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Licence Scans Upload Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Licence Document Scans",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Clear and unblurred photos showing validity and chip/hologram",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = "Licence Card Front",
                        subtitle = "Photo, licence number, validity, and name",
                        uploadedUri = licenseFrontUri,
                        onUploadClick = { licenseFrontUri = "driving_licence_front.jpg" },
                        onRemoveClick = { licenseFrontUri = null }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Licence Card Back",
                        subtitle = "Authorised vehicle classes and RTO badge",
                        uploadedUri = licenseBackUri,
                        onUploadClick = { licenseBackUri = "driving_licence_back.jpg" },
                        onRemoveClick = { licenseBackUri = null }
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = errorMessage!!,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color(0xFFEF4444),
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            RideMitraButton(
                text = "Next: Vehicle Registration",
                onClick = {
                    if (licenseNumber.isBlank()) {
                        errorMessage = "Please enter your driving licence number."
                        return@RideMitraButton
                    }
                    if (issueDate.isBlank() || expiryDate.isBlank()) {
                        errorMessage = "Please enter both issue date and expiry date."
                        return@RideMitraButton
                    }
                    if (licenseFrontUri == null || licenseBackUri == null) {
                        errorMessage = "Please upload both front and back images of your driving licence."
                        return@RideMitraButton
                    }

                    errorMessage = null
                    onSaveAndNext(
                        DriverLicenseInfo(
                            licenseNumber = licenseNumber.trim(),
                            licenseFrontUri = licenseFrontUri,
                            licenseBackUri = licenseBackUri,
                            issueDate = issueDate.trim(),
                            expiryDate = expiryDate.trim(),
                            licenseCategory = licenseCategory.trim(),
                            isExpiryValid = true
                        )
                    )
                },
                gradient = PrimaryGradient,
                trailingIcon = Icons.AutoMirrored.Filled.ArrowForward
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
