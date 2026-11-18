package com.ridemitra.app.domain.model

import kotlin.math.roundToInt

/**
 * Encapsulates dynamic fare breakdown for airport travel based on route metrics and business rules.
 * All fares are clearly identified as estimates until confirmed.
 */
data class FareEstimate(
    val originalFare: Double,
    val discountAmount: Double = 0.0,
    val finalEstimatedFare: Double,
    val advanceDepositRequired: Double,
    val remainingBalancePayable: Double,
    val eligibleCancellationRefund: Double,
    val routeDistanceKm: Double,
    val routeDurationMinutes: Int,
    val isSolo: Boolean,
    val baseFare: Double,
    val distanceCharge: Double,
    val timeCharge: Double,
    val airportTollFee: Double,
    val isConfirmed: Boolean = false
) {
    val roundedOriginalFare: Int get() = originalFare.roundToInt()
    val roundedDiscountAmount: Int get() = discountAmount.roundToInt()
    val roundedFinalFare: Int get() = finalEstimatedFare.roundToInt()
    val roundedAdvanceDeposit: Int get() = advanceDepositRequired.roundToInt()
    val roundedRemainingBalance: Int get() = remainingBalancePayable.roundToInt()
    val roundedCancellationRefund: Int get() = eligibleCancellationRefund.roundToInt()
}
