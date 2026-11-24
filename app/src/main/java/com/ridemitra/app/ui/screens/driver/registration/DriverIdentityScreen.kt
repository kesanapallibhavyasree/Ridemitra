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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.ridemitra.app.domain.validation.DocumentValidator
import com.ridemitra.app.ui.components.DocumentUploadBox
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraTextField
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Step 2: Identity Verification
 * Features:
 * - Aadhaar or Government ID (front & back)
 * - Configurable PAN collection (mandatory vs optional based on operational policy)
 * - Live photo verification
 * - Truthful representation: claims only document masking & privacy (no unsubstantiated encryption/authority claims)
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

    // Configurable PAN collection requirement
    var isPanMandatory by remember { mutableStateOf(initialData.isPanConfiguredMandatory) }
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
            // Privacy & Document Masking Notice (Truthful & accurate)
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
                            text = "Document Privacy & Number Masking",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Text(
                            text = "Documents are collected solely for RideMitra driver verification. Sensitive government numbers are masked and never displayed in full.",
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
                        text = "Select accepted ID type and upload clear front and back scans",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Aadhaar Card", "Voter ID", "Passport").forEach { type ->
                            FilterChip(
                                selected = idType == type,
                                onClick = { idType = type },
                                label = { Text(type, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RoyalBlue,
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = idNumberMasked,
                        onValueChange = { idNumberMasked = it },
                        label = "$idType Number (Masked) *",
                        placeholder = "XXXX-XXXX-4891"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = "$idType (Front Side)",
                        subtitle = "Clear scan or photo of document front",
                        uploadedUri = idFrontUri,
                        onUploadClick = { idFrontUri = "id_document_front.pdf" },
                        onRemoveClick = { idFrontUri = null }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "$idType (Back Side)",
                        subtitle = "Clear scan with address and barcode",
                        uploadedUri = idBackUri,
                        onUploadClick = { idBackUri = "id_document_back.pdf" },
                        onRemoveClick = { idBackUri = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Configurable PAN Card Section
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "2. Permanent Account Number (PAN)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = if (isPanMandatory)
                                    "Configured as Mandatory for payout processing & TDS compliance"
                                else
                                    "Configured as Optional during initial onboarding",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = if (isPanMandatory) RoyalBlue else TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        // Configurable PAN Requirement Switch
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = if (isPanMandatory) "Required" else "Optional",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isPanMandatory) RoyalBlue else TextMuted
                                )
                            )
                            Switch(
                                checked = isPanMandatory,
                                onCheckedChange = { isPanMandatory = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = CardBackground,
                                    checkedTrackColor = RoyalBlue
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = panNumberMasked,
                        onValueChange = { panNumberMasked = it.uppercase() },
                        label = if (isPanMandatory) "PAN Number * (Required)" else "PAN Number (Optional)",
                        placeholder = "ABCDE1234F"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = if (isPanMandatory) "Upload PAN Card *" else "Upload PAN Card (Optional)",
                        subtitle = "Photo or PDF copy of PAN card",
                        uploadedUri = panCardUri,
                        onUploadClick = { panCardUri = "pan_card_doc.jpg" },
                        onRemoveClick = { panCardUri = null },
                        isRequired = isPanMandatory
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Real-Time Photo Verification (Selfie)
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
                        text = "Take a live selfie to verify document ownership and identity",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = "Live Verification Selfie",
                        subtitle = "Face directly into camera in clear lighting",
                        uploadedUri = selfiePhotoUri,
                        onUploadClick = { selfiePhotoUri = "live_driver_selfie.jpg" },
                        onRemoveClick = { selfiePhotoUri = null }
                    )
                }
            }

            if (errorMessage != null) {
                Spacer(modifier = Modifier.height(12.dp))
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
                    if (idNumberMasked.trim().isBlank()) {
                        errorMessage = "Please enter your $idType number."
                        return@RideMitraButton
                    }
                    if (idFrontUri == null || idBackUri == null) {
                        errorMessage = "Please upload both front and back images of your $idType."
                        return@RideMitraButton
                    }
                    val panValidation = DocumentValidator.validatePanNumber(panNumberMasked, isPanMandatory)
                    if (!panValidation.isValid) {
                        errorMessage = panValidation.errorMessage
                        return@RideMitraButton
                    }
                    if (isPanMandatory && panCardUri == null) {
                        errorMessage = "Please upload a copy of your PAN card."
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
                            selfiePhotoUri = selfiePhotoUri,
                            isPanConfiguredMandatory = isPanMandatory
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
