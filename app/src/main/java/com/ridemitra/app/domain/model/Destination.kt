package com.ridemitra.app.domain.model

/**
 * Represents an airport drop destination with calculated route distance and travel time.
 */
data class Destination(
    val name: String,
    val locality: String,
    val distanceKm: Double,
    val estimatedDurationMinutes: Int,
    val airportCode: String = "DEL"
)

/**
 * Catalogue of popular destinations for key airports across India.
 */
val allAirportDestinations = listOf(
    // Delhi (DEL)
    Destination(
        name = "DLF Cyber City, Phase 2",
        locality = "Gurugram, Haryana",
        distanceKm = 14.5,
        estimatedDurationMinutes = 24,
        airportCode = "DEL"
    ),
    Destination(
        name = "Connaught Place (Inner Circle)",
        locality = "Central Delhi",
        distanceKm = 18.2,
        estimatedDurationMinutes = 35,
        airportCode = "DEL"
    ),
    Destination(
        name = "South Extension Part II",
        locality = "South Delhi",
        distanceKm = 16.0,
        estimatedDurationMinutes = 30,
        airportCode = "DEL"
    ),
    Destination(
        name = "Sector 62, Electronic City",
        locality = "Noida, Uttar Pradesh",
        distanceKm = 38.5,
        estimatedDurationMinutes = 55,
        airportCode = "DEL"
    ),
    Destination(
        name = "Sector 21 Metro Station",
        locality = "Dwarka, New Delhi",
        distanceKm = 8.5,
        estimatedDurationMinutes = 16,
        airportCode = "DEL"
    ),

    // Mumbai (BOM)
    Destination(
        name = "Bandra Kurla Complex (BKC)",
        locality = "Bandra East, Mumbai",
        distanceKm = 11.2,
        estimatedDurationMinutes = 28,
        airportCode = "BOM"
    ),
    Destination(
        name = "Nariman Point & Marine Drive",
        locality = "South Mumbai",
        distanceKm = 24.5,
        estimatedDurationMinutes = 48,
        airportCode = "BOM"
    ),
    Destination(
        name = "Powai Hiranandani Gardens",
        locality = "Eastern Suburbs, Mumbai",
        distanceKm = 9.8,
        estimatedDurationMinutes = 22,
        airportCode = "BOM"
    ),
    Destination(
        name = "Vashi Sector 17",
        locality = "Navi Mumbai",
        distanceKm = 28.0,
        estimatedDurationMinutes = 45,
        airportCode = "BOM"
    ),

    // Bengaluru (BLR)
    Destination(
        name = "Whitefield Tech Park",
        locality = "East Bengaluru",
        distanceKm = 39.0,
        estimatedDurationMinutes = 60,
        airportCode = "BLR"
    ),
    Destination(
        name = "Indiranagar 100 Feet Road",
        locality = "Central-East Bengaluru",
        distanceKm = 36.5,
        estimatedDurationMinutes = 52,
        airportCode = "BLR"
    ),
    Destination(
        name = "Koramangala 4th Block",
        locality = "South Bengaluru",
        distanceKm = 41.0,
        estimatedDurationMinutes = 65,
        airportCode = "BLR"
    ),
    Destination(
        name = "Electronic City Phase 1",
        locality = "South Bengaluru",
        distanceKm = 52.0,
        estimatedDurationMinutes = 75,
        airportCode = "BLR"
    ),

    // Hyderabad (HYD)
    Destination(
        name = "HITEC City & Cyber Towers",
        locality = "Madhapur, Hyderabad",
        distanceKm = 33.5,
        estimatedDurationMinutes = 42,
        airportCode = "HYD"
    ),
    Destination(
        name = "Gachibowli Financial District",
        locality = "West Hyderabad",
        distanceKm = 29.8,
        estimatedDurationMinutes = 35,
        airportCode = "HYD"
    ),
    Destination(
        name = "Banjara Hills Road No 12",
        locality = "Central Hyderabad",
        distanceKm = 28.0,
        estimatedDurationMinutes = 40,
        airportCode = "HYD"
    ),

    // Chennai (MAA)
    Destination(
        name = "OMR IT Corridor (Tidel Park)",
        locality = "Tharamani, Chennai",
        distanceKm = 14.2,
        estimatedDurationMinutes = 32,
        airportCode = "MAA"
    ),
    Destination(
        name = "T Nagar Commercial Hub",
        locality = "Central Chennai",
        distanceKm = 13.0,
        estimatedDurationMinutes = 30,
        airportCode = "MAA"
    ),

    // Kolkata (CCU)
    Destination(
        name = "Salt Lake Sector V",
        locality = "Salt Lake, Kolkata",
        distanceKm = 14.0,
        estimatedDurationMinutes = 28,
        airportCode = "CCU"
    ),
    Destination(
        name = "Park Street & Esplanade",
        locality = "Central Kolkata",
        distanceKm = 17.5,
        estimatedDurationMinutes = 40,
        airportCode = "CCU"
    )
)

/**
 * Returns popular destinations for a given airport code, falling back to DEL if none matched.
 */
fun getDestinationsForAirport(airportCode: String): List<Destination> {
    val matches = allAirportDestinations.filter { it.airportCode.equals(airportCode, ignoreCase = true) }
    return if (matches.isNotEmpty()) matches else allAirportDestinations.take(5)
}

/**
 * Backward compatibility alias for popularAirportDestinations.
 */
val popularAirportDestinations: List<Destination> get() = getDestinationsForAirport("DEL")
