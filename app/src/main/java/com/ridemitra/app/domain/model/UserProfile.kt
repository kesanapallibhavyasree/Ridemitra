package com.ridemitra.app.domain.model

data class EmergencyContact(
    val name: String,
    val phone: String,
    val relation: String
)

data class SavedPlace(
    val id: String,
    val label: String, // Home, Work, Frequent Hotel
    val address: String,
    val locality: String
)

data class UserProfile(
    val name: String = "Keerthi Vardhan",
    val phone: String = "+91 98765 43210",
    val email: String = "keerthi.vardhan@ridemitra.com",
    val rating: Double = 4.95,
    val completedTripsCount: Int = 14,
    val carbonSavedKg: Double = 42.8,
    val totalSavedInRupees: Int = 3850,
    val isIdentityVerified: Boolean = true,
    val defaultAirportCode: String = "DEL",
    val emergencyContacts: List<EmergencyContact> = listOf(
        EmergencyContact("Saritha V. (Family)", "+91 98480 12345", "Mother"),
        EmergencyContact("Rohan M. (Colleague)", "+91 99123 67890", "Friend")
    ),
    val savedPlaces: List<SavedPlace> = listOf(
        SavedPlace("sp-1", "Home", "Flat 402, Signature Towers, DLF Phase 2", "Gurugram"),
        SavedPlace("sp-2", "Office", "Cyber Hub Building 10, Sector 24", "Gurugram"),
        SavedPlace("sp-3", "Hotel", "The Lalit, Barakhamba Road", "Central Delhi")
    )
)
