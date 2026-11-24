package com.ridemitra.app.domain.validation

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Robust document and regulatory compliance validator for RideMitra Driver Module.
 * Validates expiration dates, identity documents, PAN, and vehicle capacity.
 */
object DocumentValidator {

    private val supportedDatePatterns = listOf(
        "dd/MM/yyyy",
        "dd-MM-yyyy",
        "yyyy-MM-dd"
    )

    /**
     * Parses a date string leniently across supported common date formats.
     */
    fun parseDate(dateStr: String): Date? {
        val trimmed = dateStr.trim()
        if (trimmed.isEmpty()) return null
        for (pattern in supportedDatePatterns) {
            try {
                val sdf = SimpleDateFormat(pattern, Locale.ENGLISH).apply {
                    isLenient = false
                }
                val date = sdf.parse(trimmed)
                if (date != null) return date
            } catch (_: Exception) {
            }
        }
        return null
    }

    /**
     * Formats a Date object to standard display string dd/MM/yyyy.
     */
    fun formatDate(date: Date): String {
        return SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).format(date)
    }

    /**
     * Checks if a document expiry date string is valid and not expired.
     * End of expiry day (23:59:59) is considered valid.
     */
    fun validateExpiry(
        dateStr: String,
        documentName: String,
        isRequired: Boolean = true
    ): ExpiryCheckResult {
        val trimmed = dateStr.trim()
        if (trimmed.isEmpty()) {
            return if (isRequired) {
                ExpiryCheckResult(
                    isValid = false,
                    isExpired = false,
                    errorMessage = "Please enter $documentName expiry date (DD/MM/YYYY)."
                )
            } else {
                ExpiryCheckResult(isValid = true, isExpired = false, errorMessage = null)
            }
        }

        val parsedDate = parseDate(trimmed) ?: return ExpiryCheckResult(
            isValid = false,
            isExpired = false,
            errorMessage = "Invalid date format for $documentName. Please enter date as DD/MM/YYYY."
        )

        val todayStart = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val expiryEndOfDay = Calendar.getInstance().apply {
            time = parsedDate
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
            set(Calendar.MILLISECOND, 999)
        }

        return if (expiryEndOfDay.before(todayStart)) {
            val formatted = formatDate(parsedDate)
            ExpiryCheckResult(
                isValid = false,
                isExpired = true,
                errorMessage = "$documentName expired on $formatted. Expired documents cannot be approved for operations."
            )
        } else {
            ExpiryCheckResult(
                isValid = true,
                isExpired = false,
                errorMessage = null
            )
        }
    }

    /**
     * Validates date of birth ensuring driver is at least 18 years old.
     */
    fun validateDateOfBirth(dobStr: String): ValidationResult {
        val trimmed = dobStr.trim()
        if (trimmed.isEmpty()) {
            return ValidationResult(isValid = false, errorMessage = "Date of Birth is required (DD/MM/YYYY).")
        }
        val parsed = parseDate(trimmed) ?: return ValidationResult(
            isValid = false,
            errorMessage = "Invalid Date of Birth format. Use DD/MM/YYYY."
        )

        val dobCal = Calendar.getInstance().apply { time = parsed }
        val minAgeCal = Calendar.getInstance().apply { add(Calendar.YEAR, -18) }

        return if (dobCal.after(minAgeCal)) {
            ValidationResult(
                isValid = false,
                errorMessage = "Driver must be at least 18 years of age to register for commercial operations."
            )
        } else {
            ValidationResult(isValid = true, errorMessage = null)
        }
    }

    /**
     * Validates PAN format (5 letters, 4 digits, 1 letter, e.g., ABCDE1234F).
     * Behavior depends on whether PAN collection is configured as mandatory.
     */
    fun validatePanNumber(panStr: String, isMandatory: Boolean): ValidationResult {
        val trimmed = panStr.trim().uppercase()
        if (trimmed.isEmpty()) {
            return if (isMandatory) {
                ValidationResult(isValid = false, errorMessage = "PAN number is required for commercial payout processing.")
            } else {
                ValidationResult(isValid = true, errorMessage = null)
            }
        }

        val panRegex = Regex("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")
        return if (!panRegex.matches(trimmed)) {
            ValidationResult(
                isValid = false,
                errorMessage = "Invalid PAN number format. Format should be 5 letters, 4 digits, 1 letter (e.g. ABCDE1234F)."
            )
        } else {
            ValidationResult(isValid = true, errorMessage = null)
        }
    }

    /**
     * Validates standard 10-digit Indian mobile number.
     */
    fun validateMobileNumber(phoneStr: String): ValidationResult {
        val cleaned = phoneStr.replace(Regex("[^0-9]"), "")
        val last10 = if (cleaned.length >= 10) cleaned.takeLast(10) else cleaned
        return if (last10.length == 10 && (last10.startsWith("6") || last10.startsWith("7") || last10.startsWith("8") || last10.startsWith("9"))) {
            ValidationResult(isValid = true, errorMessage = null)
        } else {
            ValidationResult(
                isValid = false,
                errorMessage = "Please enter a valid 10-digit Indian mobile number."
            )
        }
    }

    /**
     * Validates vehicle registration number (e.g., DL 01 AB 1234 or KA01MJ4829).
     */
    fun validateVehiclePlate(plateStr: String): ValidationResult {
        val cleaned = plateStr.trim().uppercase().replace(" ", "")
        if (cleaned.length < 6 || cleaned.length > 13) {
            return ValidationResult(
                isValid = false,
                errorMessage = "Please enter a valid vehicle registration number (e.g. KA01MJ4829)."
            )
        }
        return ValidationResult(isValid = true, errorMessage = null)
    }

    /**
     * Calculates RideMitra maximum passenger capacity strictly according to the rule:
     * Maximum passengers = registered passenger seats excluding driver - 1.
     */
    fun calculateRideMitraCapacity(registeredPassengerSeatsExclDriver: Int): Int {
        return (registeredPassengerSeatsExclDriver - 1).coerceAtLeast(1)
    }
}

data class ExpiryCheckResult(
    val isValid: Boolean,
    val isExpired: Boolean,
    val errorMessage: String?
)

data class ValidationResult(
    val isValid: Boolean,
    val errorMessage: String?
)
