package com.ridemitra.app.ui.screens.passenger

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AirplanemodeActive
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Luggage
import androidx.compose.material.icons.filled.Schedule
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.domain.model.Airport
import com.ridemitra.app.domain.model.FlightInfo
import com.ridemitra.app.domain.model.FlightSyncStatus
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraOutlinedButton
import com.ridemitra.app.ui.components.RideMitraTextField
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary
import com.ridemitra.app.ui.theme.WarningAmber

/**
 * Screen 3: Flight Details Screen.
 * Allows passenger to enter arriving flight number, airline, and scheduled arrival time.
 * Clearly states flight synchronization policies without presenting fake live flight data.
 */
@Composable
fun FlightDetailsScreen(
    currentAirport: Airport,
    existingFlightInfo: FlightInfo,
    onSaveFlightInfo: (FlightInfo) -> Unit,
    onSkip: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var flightNumber by remember { mutableStateOf(existingFlightInfo.flightNumber) }
    var airline by remember { mutableStateOf(existingFlightInfo.airline) }
    var originCity by remember { mutableStateOf(existingFlightInfo.originCity) }
    var arrivalTime by remember { mutableStateOf(existingFlightInfo.arrivalTime) }
    var baggageBelt by remember { mutableStateOf(existingFlightInfo.baggageBelt ?: "") }
    var hasSaved by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Arriving Flight Details",
            subtitle = "Optional: Connect flight to auto-adjust pickup upon delays",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            // Airport Destination Banner
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFEFF6FF),
                borderColor = Color(0xFFBFDBFE),
                elevation = 1.dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AirportIconBadge(
                        icon = Icons.Default.AirplanemodeActive,
                        size = 36.dp,
                        iconSize = 18.dp,
                        tint = RoyalBlue,
                        background = Color(0xFFDBEAFE)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Destination Airport: ${currentAirport.name}",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                        Text(
                            text = "Pickup terminal: ${currentAirport.defaultTerminal}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Form inputs
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                elevation = 2.dp
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Enter Flight Details",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = PrimaryNavy
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = flightNumber,
                        onValueChange = { flightNumber = it.uppercase() },
                        label = "Flight Number",
                        placeholder = "e.g. 6E-204, AI-802, UK-955",
                        leadingIcon = Icons.Default.AirplanemodeActive
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = airline,
                        onValueChange = { airline = it },
                        label = "Airline Name",
                        placeholder = "e.g. IndiGo, Air India, Vistara, Akasa"
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = originCity,
                                onValueChange = { originCity = it },
                                label = "Origin City",
                                placeholder = "e.g. Mumbai, Goa"
                            )
                        }

                        Box(modifier = Modifier.weight(1f)) {
                            RideMitraTextField(
                                value = arrivalTime,
                                onValueChange = { arrivalTime = it },
                                label = "Arrival Time",
                                placeholder = "e.g. 19:45"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    RideMitraTextField(
                        value = baggageBelt,
                        onValueChange = { baggageBelt = it },
                        label = "Baggage Belt (Optional)",
                        placeholder = "e.g. Belt 06 (If already assigned)",
                        leadingIcon = Icons.Default.Luggage
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transparency Card (Never pretend fake live data)
            RideMitraCard(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = Color(0xFFF8FAFC),
                borderColor = BorderLight,
                elevation = 1.dp
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = RoyalBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "RideMitra Airport Sync Guarantee",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "• In commercial release, your driver's dispatch time adjusts automatically if your flight touches down late.\n• No cancellation fees apply when delays occur beyond your control.\n• Live API radar integration activates 3 hours prior to scheduled landing.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondary,
                            lineHeight = 18.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Actions
            RideMitraButton(
                text = "Save Flight & Continue",
                onClick = {
                    val info = FlightInfo(
                        airline = airline.ifBlank { "Domestic Carrier" },
                        flightNumber = flightNumber.ifBlank { "ARR-FLIGHT" },
                        originCity = originCity,
                        arrivalTime = arrivalTime.ifBlank { "On Schedule" },
                        terminal = currentAirport.defaultTerminal,
                        syncStatus = FlightSyncStatus.MANUALLY_ENTERED,
                        baggageBelt = baggageBelt.ifBlank { null }
                    )
                    hasSaved = true
                    onSaveFlightInfo(info)
                },
                gradient = PrimaryGradient
            )

            Spacer(modifier = Modifier.height(10.dp))

            RideMitraOutlinedButton(
                text = "Skip Flight Linking (Proceed with Booking)",
                onClick = onSkip,
                borderColor = BorderLight,
                textColor = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
