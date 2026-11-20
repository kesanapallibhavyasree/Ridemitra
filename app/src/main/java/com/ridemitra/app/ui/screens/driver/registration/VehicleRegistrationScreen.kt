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
 * Collects:
 * - Vehicle registration number
 * - Vehicle manufacturer
 * - Vehicle model
 * - Manufacturing year
 * - Vehicle category
 * - Passenger capacity excluding driver
 * - Luggage capacity
 * - Vehicle photographs (Front & Rear)
 * Note: Enforces strict RideMitra passenger threshold = capacity excluding driver - 1.
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

    // Calculated allowed RideMitra seats (N-1 rule)
    val allowedCapacity = (passengerCapacity - 1).coerceAtLeast(1)

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
                            text = "RideMitra passenger threshold = Registered capacity - 1.\nYour $passengerCapacity-passenger cab will accept a maximum of $allowedCapacity riders, guaranteeing luggage room and comfort.",
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

            // Vehicle Specifications Card
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
                        label = "Commercial Vehicle Registration Number *",
                        placeholder = "DL 01 AB 9482 (Yellow Board)",
                        leadingIcon = Icons.Default.DirectionsCar
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = manufacturer,
                                onValueChange = { manufacturer = it },
                                label = "Manufacturer *",
                                placeholder = "e.g. Maruti Suzuki"
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = model,
                                onValueChange = { model = it },
                                label = "Model Name *",
                                placeholder = "e.g. Dzire, Aura, Ertiga"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = manufacturingYear,
                                onValueChange = { manufacturingYear = it },
                                label = "Manufacturing Year *",
                                placeholder = "e.g. 2022",
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = vehicleCategory,
                                onValueChange = { vehicleCategory = it },
                                label = "Category *",
                                placeholder = "Sedan, SUV, EV"
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Capacity Selector Card
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Seating & Boot Capacity",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Registered Capacity Pills
                    Text(
                        text = "Registered Passenger Seats (Excluding Driver):",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(4, 6, 7).forEach { cap ->
                            val isChosen = passengerCapacity == cap
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isChosen) RoyalBlue else SurfaceVariantLight)
                                    .border(
                                        1.dp,
                                        if (isChosen) RoyalBlue else BorderLight,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { passengerCapacity = cap }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$cap Seats",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isChosen) CardBackground else TextPrimary
                                        )
                                    )
                                    Text(
                                        text = "Max ${cap - 1} Riders",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = if (isChosen) Color(0xFFDBEAFE) else SuccessGreen,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 9.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = BorderLight)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Luggage boot capacity
                    Text(
                        text = "Boot Luggage Capacity (Standard Medium Bags):",
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        listOf(2, 3, 4, 5).forEach { bags ->
                            val isChosen = luggageCapacityBags == bags
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isChosen) TealAccent else SurfaceVariantLight)
                                    .border(
                                        1.dp,
                                        if (isChosen) TealAccent else BorderLight,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { luggageCapacityBags = bags }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$bags Bags",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isChosen) PrimaryNavy else TextPrimary
                                    )
                                )
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
                        text = "Vehicle Photographs",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )
                    Text(
                        text = "Clear pictures showing license number plate and clean interior/exterior",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DocumentUploadBox(
                        title = "Vehicle Exterior Front",
                        subtitle = "Clear view with front number plate visible",
                        uploadedUri = vehicleFrontUri,
                        onUploadClick = { vehicleFrontUri = "cab_front_exterior.jpg" },
                        onRemoveClick = { vehicleFrontUri = null }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    DocumentUploadBox(
                        title = "Vehicle Exterior Rear / Boot",
                        subtitle = "Showing rear number plate and open boot space",
                        uploadedUri = vehicleRearUri,
                        onUploadClick = { vehicleRearUri = "cab_rear_boot.jpg" },
                        onRemoveClick = { vehicleRearUri = null }
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
                text = "Next: Vehicle Document Submission",
                onClick = {
                    if (regNumber.isBlank()) {
                        errorMessage = "Please enter vehicle registration number."
                        return@RideMitraButton
                    }
                    if (manufacturer.isBlank() || model.isBlank()) {
                        errorMessage = "Please enter vehicle manufacturer and model."
                        return@RideMitraButton
                    }
                    if (manufacturingYear.isBlank()) {
                        errorMessage = "Please enter manufacturing year."
                        return@RideMitraButton
                    }
                    if (vehicleFrontUri == null || vehicleRearUri == null) {
                        errorMessage = "Please upload both front and rear vehicle photos."
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
