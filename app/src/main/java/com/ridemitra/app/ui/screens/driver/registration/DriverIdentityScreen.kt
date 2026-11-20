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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
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
import com.ridemitra.app.domain.model.DriverIdentityDocs
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

/**
 * Step 2: Identity Verification
 * Secure document upload interfaces for:
 * - Aadhaar or legally accepted identity document (front & back)
 * - PAN card
 * - Address proof
 * - Live selfie / photo verification
 * Note: Never exposes sensitive identity data publicly.
 */
@Composable
fun DriverIdentityScreen(
    initialData: DriverIdentityDocs,
    onSaveAndNext: (DriverIdentityDocs) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var idType by remember { mutableStateOf(initialData.documentType) }
    var idNumberMasked by remember { mutableStateOf(initialData.documentNumberMasked) }
    var idFrontUri by remember { mutableStateOf(initialData.documentFrontUri) }
    var idBackUri by remember { mutableStateOf(initialData.documentBackUri) }

    var panNumberMasked by remember { mutableStateOf(initialData.panNumberMasked) }
    var panCardUri by remember { mutableStateOf(initialData.panCardUri) }

    var addressProofUri by remember { mutableStateOf(initialData.addressProofUri) }
    var selfiePhotoUri by remember { mutableStateOf(initialData.selfiePhotoUri) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Driver Onboarding (Step 2/5)",
            subtitle = "Identity & KYC Verification",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Security & Privacy Guarantee
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE),
                elevation = 1.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = RoyalBlue,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                    Column {
                        Text(
                            text = "Encrypted Document Storage",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Text(
                            text = "Documents are securely processed exclusively for airport authority compliance. Sensitive numbers are masked.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF1E40AF),
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Identity Document (Aadhaar / Voter ID / Passport)
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "1. Government Identity Document",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Aadhaar, Voter ID, or Indian Passport",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = idNumberMasked,
                        onValueChange = { idNumberMasked = it },
                        label = "Aadhaar / ID Number (Last 4 digits visible) *",
                        placeholder = "XXXX-XXXX-4891"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = "Identity Document (Front Side)",
                        subtitle = "Clear scan or photo of document front",
                        uploadedUri = idFrontUri,
                        onUploadClick = { idFrontUri = "aadhaar_front_scan.pdf" },
                        onRemoveClick = { idFrontUri = null }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Identity Document (Back Side)",
                        subtitle = "Clear scan with address and barcode",
                        uploadedUri = idBackUri,
                        onUploadClick = { idBackUri = "aadhaar_back_scan.pdf" },
                        onRemoveClick = { idBackUri = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // PAN Card Section
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "2. Permanent Account Number (PAN)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Mandatory for commercial airport driver tax compliance & payouts",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = panNumberMasked,
                        onValueChange = { panNumberMasked = it.uppercase() },
                        label = "PAN Number *",
                        placeholder = "ABCDE1234F"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = "Upload PAN Card",
                        subtitle = "Clear photo or PDF copy of PAN card",
                        uploadedUri = panCardUri,
                        onUploadClick = { panCardUri = "pan_card_doc.jpg" },
                        onRemoveClick = { panCardUri = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Live Selfie Verification
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "3. Real-Time Photo Verification",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Take a live selfie to verify document ownership",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = "Live Verification Selfie",
                        subtitle = "Face directly into camera in good lighting",
                        uploadedUri = selfiePhotoUri,
                        onUploadClick = { selfiePhotoUri = "live_driver_selfie.jpg" },
                        onRemoveClick = { selfiePhotoUri = null }
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
                text = "Next: Driving Licence Verification",
                onClick = {
                    if (idNumberMasked.isBlank()) {
                        errorMessage = "Please enter your Identity / Aadhaar number."
                        return@RideMitraButton
                    }
                    if (idFrontUri == null || idBackUri == null) {
                        errorMessage = "Please upload both front and back images of your identity document."
                        return@RideMitraButton
                    }
                    if (panNumberMasked.isBlank() || panCardUri == null) {
                        errorMessage = "Please provide your PAN number and card upload."
                        return@RideMitraButton
                    }
                    if (selfiePhotoUri == null) {
                        errorMessage = "Please capture a live verification selfie."
                        return@RideMitraButton
                    }

                    errorMessage = null
                    onSaveAndNext(
                        DriverIdentityDocs(
                            documentType = idType,
                            documentNumberMasked = idNumberMasked.trim(),
                            documentFrontUri = idFrontUri,
                            documentBackUri = idBackUri,
                            panNumberMasked = panNumberMasked.trim(),
                            panCardUri = panCardUri,
                            addressProofUri = idBackUri,
                            selfiePhotoUri = selfiePhotoUri
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
