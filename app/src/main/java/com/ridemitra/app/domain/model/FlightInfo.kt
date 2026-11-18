package com.ridemitra.app.domain.model

/**
 * Flight Information domain model for live airport arrival sync.
 */
data class FlightInfo(
    val flightNumber: String = "AI-802",
    val airline: String = "Air India",
    val originAirport: String = "Chhatrapati Shivaji Maharaj Int'l (BOM)",
    val destinationAirport: String = "Indira Gandhi Int'l Airport (DEL)",
    val terminal: String = "Terminal 3",
    val arrivalTime: String = "18:45",
    val status: String = "On Time",
    val delayMinutes: Int = 0,
    val baggageBelt: String = "Belt 06"
)

