package com.ridemitra.app.ui.screens.driver.registration

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Error
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
import com.ridemitra.app.domain.model.VehicleRegulatoryDocs
import com.ridemitra.app.domain.validation.DocumentValidator
import com.ridemitra.app.ui.components.DocumentUploadBox
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraTextField
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.ErrorRed
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary
import com.ridemitra.app.ui.theme.WarningAmber

/**
 * Step 5: Vehicle Regulatory Document Submission
 * Features strict expiration date checking for:
 * - RC Book
 * - Commercial Motor Insurance
 * - PUC (Pollution Under Control)
 * - Commercial Cab Permit & Authorization
 * - Fitness Certificate
 */
@Composable
fun VehicleDocumentsScreen(
    initialData: VehicleRegulatoryDocs,
    onSaveAndNext: (VehicleRegulatoryDocs) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var rcNumber by remember { mutableStateOf(initialData.rcNumber) }
    var rcDocUri by remember { mutableStateOf(initialData.rcDocUri) }

    var insurancePolicy by remember { mutableStateOf(initialData.insurancePolicyNumber) }
    var insuranceExpiry by remember { mutableStateOf(initialData.insuranceExpiryDate) }
    var insuranceDocUri by remember { mutableStateOf(initialData.insuranceDocUri) }

    var pucExpiry by remember { mutableStateOf(initialData.pucExpiryDate) }
    var pucDocUri by remember { mutableStateOf(initialData.pucDocUri) }

    var fitnessExpiry by remember { mutableStateOf(initialData.fitnessExpiryDate) }
    var fitnessDocUri by remember { mutableStateOf(initialData.fitnessDocUri) }

    var commercialPermitUri by remember { mutableStateOf(initialData.commercialPermitUri) }
    var commercialPermitExpiry by remember { mutableStateOf(initialData.commercialPermitExpiryDate) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    // Real-time expiry feedback
    val insuranceCheck = remember(insuranceExpiry) {
        if (insuranceExpiry.isNotBlank()) DocumentValidator.validateExpiry(insuranceExpiry, "Motor Insurance")
        else null
    }

    val pucCheck = remember(pucExpiry) {
        if (pucExpiry.isNotBlank()) DocumentValidator.validateExpiry(pucExpiry, "PUC Certificate")
        else null
    }

    val fitnessCheck = remember(fitnessExpiry) {
        if (fitnessExpiry.isNotBlank()) DocumentValidator.validateExpiry(fitnessExpiry, "Fitness Certificate", isRequired = false)
        else null
    }

    val permitCheck = remember(commercialPermitExpiry) {
        if (commercialPermitExpiry.isNotBlank()) DocumentValidator.validateExpiry(commercialPermitExpiry, "Commercial Permit", isRequired = false)
        else null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Driver Onboarding (Step 5/5)",
            subtitle = "Vehicle Regulatory & Compliance Docs",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Airport Compliance Notice
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
                        text = "Commercial airport operations mandate active Motor Insurance and valid RTO Pollution (PUC) clearance. Expired certificates will prevent ride eligibility.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF92400E),
                            fontSize = 11.sp,
                            lineHeight = 16.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 1. RC Book Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "1. Registration Certificate (RC)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraTextField(
                        value = rcNumber,
                        onValueChange = { rcNumber = it.uppercase() },
                        label = "RC Number *",
                        placeholder = "DL01AB9482"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Upload RC Book / Smart Card",
                        subtitle = "Clear scan of both sides of Registration Card",
                        uploadedUri = rcDocUri,
                        onUploadClick = { rcDocUri = "vehicle_rc_smart_card.pdf" },
                        onRemoveClick = { rcDocUri = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Motor Insurance Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "2. Commercial Motor Insurance",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1.2f)) {
                            RideMitraTextField(
                                value = insurancePolicy,
                                onValueChange = { insurancePolicy = it },
                                label = "Policy Number *",
                                placeholder = "POL-992102-M"
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = insuranceExpiry,
                                onValueChange = { insuranceExpiry = it },
                                label = "Valid Till (DD/MM/YYYY) *",
                                placeholder = "DD/MM/YYYY"
                            )
                        }
                    }

                    if (insuranceCheck != null && insuranceCheck.isExpired) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = ErrorRed)
                            Spacer(modifier = Modifier.padding(end = 4.dp))
                            Text(
                                text = insuranceCheck.errorMessage ?: "Insurance is expired.",
                                style = MaterialTheme.typography.bodySmall.copy(color = ErrorRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Upload Insurance Policy Document",
                        subtitle = "Active commercial passenger carrying insurance policy",
                        uploadedUri = insuranceDocUri,
                        onUploadClick = { insuranceDocUri = "motor_insurance_policy.pdf" },
                        onRemoveClick = { insuranceDocUri = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. PUC Certificate Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "3. Pollution Under Control (PUC)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraTextField(
                        value = pucExpiry,
                        onValueChange = { pucExpiry = it },
                        label = "PUC Expiry Date (DD/MM/YYYY) *",
                        placeholder = "DD/MM/YYYY"
                    )

                    if (pucCheck != null && pucCheck.isExpired) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = ErrorRed)
                            Spacer(modifier = Modifier.padding(end = 4.dp))
                            Text(
                                text = pucCheck.errorMessage ?: "PUC is expired.",
                                style = MaterialTheme.typography.bodySmall.copy(color = ErrorRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Upload PUC Certificate",
                        subtitle = "Valid emission test certificate",
                        uploadedUri = pucDocUri,
                        onUploadClick = { pucDocUri = "puc_test_certificate.jpg" },
                        onRemoveClick = { pucDocUri = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Commercial Taxi Permit / Authorization
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "4. Commercial Taxi Permit / Authorization",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "All India Tourist Permit (AITP) or Local RTO Cab Authorization (if applicable)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraTextField(
                        value = commercialPermitExpiry,
                        onValueChange = { commercialPermitExpiry = it },
                        label = "Permit Expiry Date (Optional)",
                        placeholder = "DD/MM/YYYY"
                    )

                    if (permitCheck != null && permitCheck.isExpired) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = ErrorRed)
                            Spacer(modifier = Modifier.padding(end = 4.dp))
                            Text(
                                text = permitCheck.errorMessage ?: "Permit is expired.",
                                style = MaterialTheme.typography.bodySmall.copy(color = ErrorRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Upload Commercial Permit / Authorization",
                        subtitle = "Commercial permit scan / PDF",
                        uploadedUri = commercialPermitUri,
                        onUploadClick = { commercialPermitUri = "commercial_cab_permit.pdf" },
                        onRemoveClick = { commercialPermitUri = null },
                        isRequired = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 5. Fitness Certificate
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "5. Commercial Vehicle Fitness Certificate",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "RTO Form 38 fitness validity for commercial transport (if applicable)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraTextField(
                        value = fitnessExpiry,
                        onValueChange = { fitnessExpiry = it },
                        label = "Fitness Expiry Date (Optional)",
                        placeholder = "DD/MM/YYYY"
                    )

                    if (fitnessCheck != null && fitnessCheck.isExpired) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = ErrorRed)
                            Spacer(modifier = Modifier.padding(end = 4.dp))
                            Text(
                                text = fitnessCheck.errorMessage ?: "Fitness certificate is expired.",
                                style = MaterialTheme.typography.bodySmall.copy(color = ErrorRed, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Upload Fitness Certificate",
                        subtitle = "Commercial vehicle fitness certificate copy",
                        uploadedUri = fitnessDocUri,
                        onUploadClick = { fitnessDocUri = "vehicle_fitness_certificate.pdf" },
                        onRemoveClick = { fitnessDocUri = null },
                        isRequired = false
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
                text = "Next: Review Application",
                onClick = {
                    if (rcNumber.trim().isBlank() || rcDocUri == null) {
                        errorMessage = "Please enter RC number and upload RC book copy."
                        return@RideMitraButton
                    }
                    if (insurancePolicy.trim().isBlank() || insuranceDocUri == null) {
                        errorMessage = "Please enter insurance policy number and upload document."
                        return@RideMitraButton
                    }
                    val insValidation = DocumentValidator.validateExpiry(insuranceExpiry, "Motor Insurance")
                    if (!insValidation.isValid) {
                        errorMessage = insValidation.errorMessage
                        return@RideMitraButton
                    }

                    if (pucDocUri == null) {
                        errorMessage = "Please upload PUC certificate copy."
                        return@RideMitraButton
                    }
                    val pucValidation = DocumentValidator.validateExpiry(pucExpiry, "PUC Certificate")
                    if (!pucValidation.isValid) {
                        errorMessage = pucValidation.errorMessage
                        return@RideMitraButton
                    }

                    if (commercialPermitExpiry.isNotBlank()) {
                        val permValidation = DocumentValidator.validateExpiry(commercialPermitExpiry, "Commercial Permit", isRequired = false)
                        if (!permValidation.isValid) {
                            errorMessage = permValidation.errorMessage
                            return@RideMitraButton
                        }
                    }

                    if (fitnessExpiry.isNotBlank()) {
                        val fitValidation = DocumentValidator.validateExpiry(fitnessExpiry, "Fitness Certificate", isRequired = false)
                        if (!fitValidation.isValid) {
                            errorMessage = fitValidation.errorMessage
                            return@RideMitraButton
                        }
                    }

                    errorMessage = null
                    onSaveAndNext(
                        VehicleRegulatoryDocs(
                            rcNumber = rcNumber.trim(),
                            rcDocUri = rcDocUri,
                            insurancePolicyNumber = insurancePolicy.trim(),
                            insuranceExpiryDate = insuranceExpiry.trim(),
                            insuranceDocUri = insuranceDocUri,
                            pucExpiryDate = pucExpiry.trim(),
                            pucDocUri = pucDocUri,
                            fitnessExpiryDate = fitnessExpiry.trim(),
                            fitnessDocUri = fitnessDocUri,
                            commercialPermitUri = commercialPermitUri,
                            commercialPermitExpiryDate = commercialPermitExpiry.trim()
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
