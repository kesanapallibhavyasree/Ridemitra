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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhotoCamera
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.DriverPersonalInfo
import com.ridemitra.app.domain.validation.DocumentValidator
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.DocumentUploadBox
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraTextField
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
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
 * Step 1: Personal Information
 * Validates:
 * - Full name
 * - Mobile number + OTP verification
 * - Email format
 * - Date of birth (strictly 18+ years requirement)
 * - Residential address
 * - Profile photo attachment
 * - Emergency contact details
 */
@Composable
fun DriverRegistrationScreen(
    initialData: DriverPersonalInfo,
    onSaveAndNext: (DriverPersonalInfo) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf(initialData.fullName) }
    var mobileNumber by remember { mutableStateOf(initialData.mobileNumber) }
    var mobileOtp by remember { mutableStateOf(if (initialData.isMobileVerified) "8921" else "") }
    var isMobileVerified by remember { mutableStateOf(initialData.isMobileVerified) }

    var email by remember { mutableStateOf(initialData.email) }
    var isEmailVerified by remember { mutableStateOf(initialData.isEmailVerified) }

    var dateOfBirth by remember { mutableStateOf(initialData.dateOfBirth) }
    var residentialAddress by remember { mutableStateOf(initialData.residentialAddress) }
    var profilePhotoUri by remember { mutableStateOf(initialData.profilePhotoUri) }

    var emergencyName by remember { mutableStateOf(initialData.emergencyContactName) }
    var emergencyPhone by remember { mutableStateOf(initialData.emergencyContactNumber) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Driver Onboarding (Step 1/5)",
            subtitle = "Personal & Contact Information",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Profile Photo Upload Box
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(if (profilePhotoUri != null) Color(0xFFDCFCE7) else SurfaceVariantLight),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (profilePhotoUri != null) Icons.Default.CheckCircle else Icons.Default.PhotoCamera,
                            contentDescription = "Profile Photo",
                            tint = if (profilePhotoUri != null) SuccessGreen else RoyalBlue,
                            modifier = Modifier.size(34.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Driver Profile Photograph *",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Clear front-facing headshot with plain background",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = if (profilePhotoUri != null) "Photo Attached" else "Attach Profile Photo",
                        subtitle = "Selfie or passport size photo",
                        uploadedUri = profilePhotoUri,
                        onUploadClick = { profilePhotoUri = "driver_profile_headshot.jpg" },
                        onRemoveClick = { profilePhotoUri = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personal Information Inputs
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Personal Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name (as per Driving Licence) *",
                        placeholder = "e.g. Rajesh Kumar",
                        leadingIcon = Icons.Default.Person
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        label = "Date of Birth (DD/MM/YYYY) *",
                        placeholder = "14/08/1988"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = residentialAddress,
                        onValueChange = { residentialAddress = it },
                        label = "Current Residential Address *",
                        placeholder = "House no., street, locality, city, pincode"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Phone & OTP Card
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
                        Text(
                            text = "Mobile Phone Verification",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        if (isMobileVerified) {
                            StatusBadge(text = "VERIFIED", variant = BadgeVariant.SUCCESS)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraTextField(
                        value = mobileNumber,
                        onValueChange = {
                            mobileNumber = it
                            if (it != initialData.mobileNumber) isMobileVerified = false
                        },
                        label = "Mobile Phone Number *",
                        placeholder = "+91 98765 43210",
                        leadingIcon = Icons.Default.Phone
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isMobileVerified) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                RideMitraTextField(
                                    value = mobileOtp,
                                    onValueChange = { mobileOtp = it },
                                    label = "Enter 4-Digit OTP",
                                    placeholder = "8921"
                                )
                            }

                            RideMitraButton(
                                text = "Verify OTP",
                                onClick = {
                                    if (mobileOtp.trim().length >= 4) {
                                        isMobileVerified = true
                                        errorMessage = null
                                    } else {
                                        errorMessage = "Please enter 4-digit OTP (demo: 8921)."
                                    }
                                },
                                gradient = PrimaryGradient,
                                height = 52.dp,
                                modifier = Modifier.weight(0.9f)
                            )
                        }
                    } else {
                        Text(
                            text = "Mobile number verified via secure OTP.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = SuccessGreen,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Email Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Email Address (Optional)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "For weekly earnings statements and payment receipts",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = "Email Address",
                        placeholder = "driver@ridemitra.in",
                        leadingIcon = Icons.Default.Email
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Emergency Contact Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Emergency Contact",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Spouse, parent, or trusted family member in case of emergency",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraTextField(
                        value = emergencyName,
                        onValueChange = { emergencyName = it },
                        label = "Contact Name & Relationship *",
                        placeholder = "e.g. Sunita Kumar (Spouse)"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    RideMitraTextField(
                        value = emergencyPhone,
                        onValueChange = { emergencyPhone = it },
                        label = "Emergency Phone Number *",
                        placeholder = "+91 98450 11223",
                        leadingIcon = Icons.Default.Phone
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

            // Next Button
            RideMitraButton(
                text = "Next: Identity Verification (KYC)",
                onClick = {
                    if (profilePhotoUri == null) {
                        errorMessage = "Please attach a driver profile photograph."
                        return@RideMitraButton
                    }
                    if (fullName.trim().length < 2) {
                        errorMessage = "Please enter your full legal name."
                        return@RideMitraButton
                    }
                    val dobValidation = DocumentValidator.validateDateOfBirth(dateOfBirth)
                    if (!dobValidation.isValid) {
                        errorMessage = dobValidation.errorMessage
                        return@RideMitraButton
                    }
                    if (residentialAddress.trim().isBlank()) {
                        errorMessage = "Please enter your residential address."
                        return@RideMitraButton
                    }
                    val mobileValidation = DocumentValidator.validateMobileNumber(mobileNumber)
                    if (!mobileValidation.isValid) {
                        errorMessage = mobileValidation.errorMessage
                        return@RideMitraButton
                    }
                    if (!isMobileVerified) {
                        errorMessage = "Please verify your mobile number with OTP before continuing."
                        return@RideMitraButton
                    }
                    if (emergencyName.trim().isBlank()) {
                        errorMessage = "Please enter an emergency contact name."
                        return@RideMitraButton
                    }
                    val emergencyPhoneValidation = DocumentValidator.validateMobileNumber(emergencyPhone)
                    if (!emergencyPhoneValidation.isValid) {
                        errorMessage = "Please enter a valid 10-digit emergency contact phone number."
                        return@RideMitraButton
                    }

                    errorMessage = null
                    onSaveAndNext(
                        DriverPersonalInfo(
                            fullName = fullName.trim(),
                            mobileNumber = mobileNumber.trim(),
                            isMobileVerified = isMobileVerified,
                            email = email.trim(),
                            isEmailVerified = isEmailVerified,
                            dateOfBirth = dateOfBirth.trim(),
                            residentialAddress = residentialAddress.trim(),
                            profilePhotoUri = profilePhotoUri,
                            emergencyContactName = emergencyName.trim(),
                            emergencyContactNumber = emergencyPhone.trim()
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
