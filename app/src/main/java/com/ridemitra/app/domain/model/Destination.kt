package com.ridemitra.app.domain.model

/**
 * Represents an airport drop destination with calculated route distance and travel time.
 */
data class Destination(
    val name: String,
    val locality: String,
    val distanceKm: Double,
    val estimatedDurationMinutes: Int
)

/**
 * Standard pre-configured airport routes from Indira Gandhi International Airport (DEL) Terminal 3.
 */
val popularAirportDestinations = listOf(
    Destination(
        name = "DLF Cyber City, Phase 2",
        locality = "Gurugram, Haryana",
        distanceKm = 14.5,
        estimatedDurationMinutes = 24
    ),
    Destination(
        name = "Connaught Place (Inner Circle)",
        locality = "Central Delhi",
        distanceKm = 18.2,
        estimatedDurationMinutes = 35
    ),
    Destination(
        name = "South Extension Part II",
        locality = "South Delhi",
        distanceKm = 16.0,
        estimatedDurationMinutes = 30
    ),
    Destination(
        name = "Sector 62, Electronic City",
        locality = "Noida, Uttar Pradesh",
        distanceKm = 38.5,
        estimatedDurationMinutes = 55
    ),
    Destination(
        name = "Sector 21 Metro Station",
        locality = "Dwarka, New Delhi",
        distanceKm = 8.5,
        estimatedDurationMinutes = 16
    )
)
