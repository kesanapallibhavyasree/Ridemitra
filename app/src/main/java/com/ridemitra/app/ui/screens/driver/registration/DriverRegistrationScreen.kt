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
 * Collects:
 * - Full name
 * - Mobile number + OTP verification
 * - Email + Email verification
 * - Date of birth
 * - Residential address
 * - Profile photograph
 * - Emergency contact name & number
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
                        text = "Driver Profile Photograph",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Text(
                        text = "Clear front-facing photo without sunglasses or cap",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Upload Driver Photo",
                        subtitle = "JPEG/PNG format from camera or gallery",
                        uploadedUri = profilePhotoUri,
                        onUploadClick = { profilePhotoUri = "driver_profile_pic.jpg" },
                        onRemoveClick = { profilePhotoUri = null }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Personal Fields Form
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Basic Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name (as on Driving Licence) *",
                        placeholder = "e.g. Rajesh Kumar Sharma",
                        leadingIcon = Icons.Default.Person
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = dateOfBirth,
                        onValueChange = { dateOfBirth = it },
                        label = "Date of Birth (DD/MM/YYYY) *",
                        placeholder = "e.g. 15/08/1988",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = residentialAddress,
                        onValueChange = { residentialAddress = it },
                        label = "Current Residential Address *",
                        placeholder = "Flat/House No, Street, Locality, City, PIN Code",
                        singleLine = false
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Mobile & Email Verification Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Contact & Authentication",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Mobile Field
                    RideMitraTextField(
                        value = mobileNumber,
                        onValueChange = {
                            mobileNumber = it
                            if (it.length < 10) isMobileVerified = false
                        },
                        label = "Mobile Number *",
                        placeholder = "+91 98765 43210",
                        leadingIcon = Icons.Default.Phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isMobileVerified) "Mobile number verified with OTP" else "OTP verification required",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isMobileVerified) SuccessGreen else TextSecondary,
                                fontSize = 11.sp
                            )
                        )

                        if (!isMobileVerified) {
                            RideMitraButton(
                                text = "Verify OTP",
                                onClick = {
                                    if (mobileNumber.length >= 10) isMobileVerified = true
                                },
                                modifier = Modifier.width(110.dp),
                                height = 36.dp,
                                gradient = PrimaryGradient
                            )
                        } else {
                            StatusBadge(text = "VERIFIED", variant = BadgeVariant.SUCCESS)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Email Field
                    RideMitraTextField(
                        value = email,
                        onValueChange = {
                            email = it
                            if (!it.contains("@")) isEmailVerified = false
                        },
                        label = "Email Address *",
                        placeholder = "driver.partner@ridemitra.com",
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isEmailVerified) "Email address verified" else "Confirmation link/OTP required",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = if (isEmailVerified) SuccessGreen else TextSecondary,
                                fontSize = 11.sp
                            )
                        )

                        if (!isEmailVerified) {
                            RideMitraButton(
                                text = "Verify Email",
                                onClick = {
                                    if (email.contains("@")) isEmailVerified = true
                                },
                                modifier = Modifier.width(110.dp),
                                height = 36.dp,
                                gradient = PrimaryGradient
                            )
                        } else {
                            StatusBadge(text = "VERIFIED", variant = BadgeVariant.SUCCESS)
                        }
                    }
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
                        text = "Emergency Contact Information",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Contacted in case of roadside or terminal emergencies",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = emergencyName,
                        onValueChange = { emergencyName = it },
                        label = "Emergency Contact Person Name *",
                        placeholder = "e.g. Suman Sharma (Spouse / Kin)"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = emergencyPhone,
                        onValueChange = { emergencyPhone = it },
                        label = "Emergency Contact Number *",
                        placeholder = "+91 98111 22334",
                        leadingIcon = Icons.Default.Phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
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
                text = "Next: Identity Verification",
                onClick = {
                    if (fullName.isBlank()) {
                        errorMessage = "Please enter your full name as shown on licence."
                        return@RideMitraButton
                    }
                    if (mobileNumber.isBlank() || !isMobileVerified) {
                        errorMessage = "Please enter and verify your mobile number with OTP."
                        return@RideMitraButton
                    }
                    if (email.isBlank() || !isEmailVerified) {
                        errorMessage = "Please enter and verify your email address."
                        return@RideMitraButton
                    }
                    if (dateOfBirth.isBlank()) {
                        errorMessage = "Please enter your date of birth."
                        return@RideMitraButton
                    }
                    if (residentialAddress.isBlank()) {
                        errorMessage = "Please enter your residential address."
                        return@RideMitraButton
                    }
                    if (emergencyName.isBlank() || emergencyPhone.isBlank()) {
                        errorMessage = "Please complete emergency contact details."
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
                            profilePhotoUri = profilePhotoUri ?: "profile_photo.jpg",
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
