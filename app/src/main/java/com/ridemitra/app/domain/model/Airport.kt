package com.ridemitra.app.domain.model

/**
 * Represents a commercial airport in India supported by RideMitra.
 */
data class Airport(
    val code: String,              // e.g., "DEL"
    val name: String,              // e.g., "Indira Gandhi International Airport"
    val city: String,              // e.g., "New Delhi"
    val state: String,             // e.g., "Delhi NCR"
    val terminals: List<String>,   // e.g., ["Terminal 1", "Terminal 2", "Terminal 3"]
    val pickupPillars: List<String>,// e.g., ["Pillar 4A", "Pillar 8B", "Pillar 14"]
    val defaultTerminal: String = terminals.firstOrNull() ?: "Terminal 1",
    val baseTollFee: Double = 120.0
)

/**
 * Catalogue of major Indian airports supported across the platform.
 */
val indianAirportsList = listOf(
    Airport(
        code = "DEL",
        name = "Indira Gandhi International Airport",
        city = "New Delhi",
        state = "Delhi NCR",
        terminals = listOf("Terminal 1 (Domestic)", "Terminal 2 (Low Cost)", "Terminal 3 (Int'l & Domestic)"),
        pickupPillars = listOf("T3 Pillar 4A", "T3 Pillar 8B", "T3 Pillar 14B", "T1 Pillar 2", "T2 Pillar 6"),
        defaultTerminal = "Terminal 3 (Int'l & Domestic)",
        baseTollFee = 120.0
    ),
    Airport(
        code = "BOM",
        name = "Chhatrapati Shivaji Maharaj Int'l Airport",
        city = "Mumbai",
        state = "Maharashtra",
        terminals = listOf("Terminal 1 (Santacruz)", "Terminal 2 (Sahar - Int'l)"),
        pickupPillars = listOf("T2 Level P4 Pillar P2", "T2 Level P7", "T1 Gate 2 Ola/Uber Hub"),
        defaultTerminal = "Terminal 2 (Sahar - Int'l)",
        baseTollFee = 140.0
    ),
    Airport(
        code = "BLR",
        name = "Kempegowda International Airport",
        city = "Bengaluru",
        state = "Karnataka",
        terminals = listOf("Terminal 1", "Terminal 2 (Garden Terminal)"),
        pickupPillars = listOf("T1 Zone 2 Pickup", "T2 Level 0 Cab Hub Pillar 5", "T2 Gate 3"),
        defaultTerminal = "Terminal 2 (Garden Terminal)",
        baseTollFee = 130.0
    ),
    Airport(
        code = "HYD",
        name = "Rajiv Gandhi International Airport",
        city = "Hyderabad",
        state = "Telangana",
        terminals = listOf("Main Passenger Terminal"),
        pickupPillars = listOf("Aeroplaza Level C", "Carpark Zone C Pillar 9", "Arrival Gate 3"),
        defaultTerminal = "Main Passenger Terminal",
        baseTollFee = 110.0
    ),
    Airport(
        code = "MAA",
        name = "Chennai International Airport",
        city = "Chennai",
        state = "Tamil Nadu",
        terminals = listOf("Terminal 1 (Domestic)", "Terminal 2 (New Int'l)"),
        pickupPillars = listOf("MLCP Level 2 Pillar D", "Domestic Arrival Curb Pillar 3"),
        defaultTerminal = "Terminal 1 (Domestic)",
        baseTollFee = 100.0
    ),
    Airport(
        code = "CCU",
        name = "Netaji Subhash Chandra Bose Int'l Airport",
        city = "Kolkata",
        state = "West Bengal",
        terminals = listOf("Integrated Terminal"),
        pickupPillars = listOf("Arrival Gate 4 Pillar 12", "Zone B App Cab Pickup"),
        defaultTerminal = "Integrated Terminal",
        baseTollFee = 90.0
    ),
    Airport(
        code = "AMD",
        name = "Sardar Vallabhbhai Patel Int'l Airport",
        city = "Ahmedabad",
        state = "Gujarat",
        terminals = listOf("Terminal 1 (Domestic)", "Terminal 2 (Int'l)"),
        pickupPillars = listOf("T1 Arrival Canopy Pillar 4", "T2 Pickup Zone A"),
        defaultTerminal = "Terminal 1 (Domestic)",
        baseTollFee = 90.0
    ),
    Airport(
        code = "PNQ",
        name = "Pune International Airport",
        city = "Pune",
        state = "Maharashtra",
        terminals = listOf("New Integrated Terminal"),
        pickupPillars = listOf("AeroMall Level 2", "Arrival Curb Pillar 6"),
        defaultTerminal = "New Integrated Terminal",
        baseTollFee = 80.0
    ),
    Airport(
        code = "COK",
        name = "Cochin International Airport",
        city = "Kochi",
        state = "Kerala",
        terminals = listOf("Terminal 1 (Domestic)", "Terminal 3 (Int'l)"),
        pickupPillars = listOf("T3 Canopy Pillar 8", "T1 Parking Bay C"),
        defaultTerminal = "Terminal 3 (Int'l)",
        baseTollFee = 85.0
    ),
    Airport(
        code = "GOI",
        name = "Manohar International Airport (MOPA)",
        city = "Goa",
        state = "Goa",
        terminals = listOf("Integrated Terminal"),
        pickupPillars = listOf("Arrival Bay Pillar 3", "North Goa App Taxi Bay"),
        defaultTerminal = "Integrated Terminal",
        baseTollFee = 100.0
    )
)
