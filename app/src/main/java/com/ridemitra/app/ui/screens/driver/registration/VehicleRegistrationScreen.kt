package com.ridemitra.app.ui.screens.driver.registration

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.ridemitra.app.domain.model.VehicleDetails
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
 * Step 4: Vehicle Registration
 * Features:
 * - Vehicle registration plate number
 * - Vehicle make & model
 * - Manufacturing year
 * - Vehicle category (Sedan, SUV, EV, Compact)
 * - Strict RideMitra N-1 seat capacity rule:
 *   Maximum passengers = registered passenger seats excluding driver - 1.
 * - Luggage capacity (bags)
 * - Vehicle front and rear photos
 */
@Composable
fun VehicleRegistrationScreen(
    initialData: VehicleDetails,
    onSaveAndNext: (VehicleDetails) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var regNumber by remember { mutableStateOf(initialData.registrationNumber) }
    var manufacturer by remember { mutableStateOf(initialData.manufacturer) }
    var model by remember { mutableStateOf(initialData.model) }
    var manufacturingYear by remember { mutableStateOf(initialData.manufacturingYear) }
    var vehicleCategory by remember { mutableStateOf(initialData.vehicleCategory) }
    var passengerCapacity by remember { mutableIntStateOf(initialData.passengerCapacityExclDriver) }
    var luggageCapacityBags by remember { mutableIntStateOf(initialData.luggageCapacityBags) }

    var vehicleFrontUri by remember { mutableStateOf(initialData.vehicleFrontUri) }
    var vehicleRearUri by remember { mutableStateOf(initialData.vehicleRearUri) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    // Calculated allowed RideMitra seats strictly enforcing the N-1 rule
    val allowedCapacity = DocumentValidator.calculateRideMitraCapacity(passengerCapacity)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Driver Onboarding (Step 4/5)",
            subtitle = "Vehicle Registration & Capacity",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Strict N-1 Seat Rule Info Banner
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFF0FDF4),
                borderColor = Color(0xFFBBF7D0),
                elevation = 2.dp
            ) {
                Row(verticalAlignment = Alignment.Top) {
                    AirportIconBadge(
                        icon = Icons.Default.Shield,
                        size = 36.dp,
                        iconSize = 18.dp,
                        tint = SuccessGreen,
                        background = Color(0xFFDCFCE7)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Automatic N-1 Passenger Seat Rule",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF166534)
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Maximum passengers = registered passenger seats excluding driver - 1.\nYour $passengerCapacity-seat passenger cab will accept a maximum of $allowedCapacity riders, ensuring space for luggage and co-passenger comfort.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF15803D),
                                lineHeight = 17.sp,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vehicle Identity Form
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Vehicle Specifications",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = regNumber,
                        onValueChange = { regNumber = it.uppercase() },
                        label = "Vehicle Registration Plate Number *",
                        placeholder = "KA 01 MJ 4829",
                        leadingIcon = Icons.Default.DirectionsCar
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1.2f)) {
                            RideMitraTextField(
                                value = manufacturer,
                                onValueChange = { manufacturer = it },
                                label = "Manufacturer *",
                                placeholder = "Toyota / Maruti"
                            )
                        }

                        Box(modifier = Modifier.weight(1.2f)) {
                            RideMitraTextField(
                                value = model,
                                onValueChange = { model = it },
                                label = "Model Name *",
                                placeholder = "Etios / Dzire"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = manufacturingYear,
                        onValueChange = { manufacturingYear = it },
                        label = "Manufacturing Year *",
                        placeholder = "2022",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vehicle Category Selector
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Vehicle Category",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Sedan", "SUV", "EV", "Compact").forEach { cat ->
                            val isSelected = vehicleCategory.equals(cat, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) RoyalBlue else SurfaceVariantLight)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) RoyalBlue else BorderLight,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable { vehicleCategory = cat }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cat,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) Color.White else TextPrimary
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Seating & Luggage Capacity (Strict N-1 Rule Visualizer)
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Seating & Luggage Capacity",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Passenger seat stepper
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Registered Passenger Seats (Excl. Driver)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Total seats for passengers as registered on RC",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceVariantLight)
                                    .clickable {
                                        if (passengerCapacity > 3) passengerCapacity--
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryNavy)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "$passengerCapacity",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceVariantLight)
                                    .clickable {
                                        if (passengerCapacity < 8) passengerCapacity++
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryNavy)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Allowed RideMitra Capacity Display
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "RideMitra Booking Limit (N-1):",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )
                            Text(
                                text = "Platform will NEVER dispatch more riders",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 10.sp
                                )
                            )
                        }
                        StatusBadge(
                            text = "MAX $allowedCapacity RIDERS",
                            variant = BadgeVariant.ROYAL
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Luggage bags stepper
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Boot Luggage Capacity (Standard Trolleys)",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                            )
                            Text(
                                text = "Large check-in bags boot space",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    fontSize = 11.sp
                                )
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceVariantLight)
                                    .clickable {
                                        if (luggageCapacityBags > 1) luggageCapacityBags--
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryNavy)
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Text(
                                text = "$luggageCapacityBags",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryNavy
                                )
                            )

                            Spacer(modifier = Modifier.width(12.dp))

                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SurfaceVariantLight)
                                    .clickable {
                                        if (luggageCapacityBags < 6) luggageCapacityBags++
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = PrimaryNavy)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Vehicle Photos Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Vehicle Inspection Photographs",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Clear pictures showing the licence plate clearly visible",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = "Vehicle Front View",
                        subtitle = "Front angle showing headlights, windshield, and front number plate",
                        uploadedUri = vehicleFrontUri,
                        onUploadClick = { vehicleFrontUri = "car_front_angle.jpg" },
                        onRemoveClick = { vehicleFrontUri = null }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Vehicle Rear View",
                        subtitle = "Rear angle showing boot and rear number plate",
                        uploadedUri = vehicleRearUri,
                        onUploadClick = { vehicleRearUri = "car_rear_angle.jpg" },
                        onRemoveClick = { vehicleRearUri = null }
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
                text = "Next: Regulatory Documents",
                onClick = {
                    val plateCheck = DocumentValidator.validateVehiclePlate(regNumber)
                    if (!plateCheck.isValid) {
                        errorMessage = plateCheck.errorMessage
                        return@RideMitraButton
                    }
                    if (manufacturer.trim().isBlank()) {
                        errorMessage = "Please enter vehicle manufacturer."
                        return@RideMitraButton
                    }
                    if (model.trim().isBlank()) {
                        errorMessage = "Please enter vehicle model."
                        return@RideMitraButton
                    }
                    val yearInt = manufacturingYear.trim().toIntOrNull()
                    if (yearInt == null || yearInt < 2005 || yearInt > 2030) {
                        errorMessage = "Please enter a valid 4-digit manufacturing year (2005 or newer)."
                        return@RideMitraButton
                    }
                    if (vehicleFrontUri == null || vehicleRearUri == null) {
                        errorMessage = "Please upload both front and rear photos of your vehicle."
                        return@RideMitraButton
                    }

                    errorMessage = null
                    onSaveAndNext(
                        VehicleDetails(
                            registrationNumber = regNumber.trim(),
                            manufacturer = manufacturer.trim(),
                            model = model.trim(),
                            manufacturingYear = manufacturingYear.trim(),
                            vehicleCategory = vehicleCategory.trim(),
                            passengerCapacityExclDriver = passengerCapacity,
                            luggageCapacityBags = luggageCapacityBags,
                            vehicleFrontUri = vehicleFrontUri,
                            vehicleRearUri = vehicleRearUri
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
