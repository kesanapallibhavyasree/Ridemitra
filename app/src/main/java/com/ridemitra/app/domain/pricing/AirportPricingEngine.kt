package com.ridemitra.app.domain.pricing

import com.ridemitra.app.domain.model.FareEstimate
import com.ridemitra.app.domain.model.RideOptionType
import kotlin.math.roundToInt

/**
 * Dynamic Airport Pricing Engine for RideMitra.
 * Calculates transparent, non-random fares based on route distance, duration,
 * airport entry tolls, shared split rates, and solo promotional discounts.
 */
object AirportPricingEngine {

    // Standard Airport Mobility Rate Card
    private const val BASE_AIRPORT_FLAG_DOWN = 100.0   // Fixed base fare for airport terminal departure
    private const val RATE_PER_KM = 16.5              // Standard per kilometer rate
    private const val RATE_PER_MINUTE = 2.0           // Travel time & congestion rate
    private const val AIRPORT_TOLL_AND_PARKING = 120.0// Mandatory IGI Airport commercial access fee

    // Platform Policy Constants
    private const val SOLO_PROMOTIONAL_DISCOUNT_PERCENT = 0.10 // 10% Platform-funded solo discount
    private const val ADVANCE_PAYMENT_RATIO = 0.10             // 10% Advance deposit required
    private const val CANCELLATION_REFUND_RATIO = 0.05          // 5% of total ride fare refund if > 1 hr

    /**
     * Calculates dynamic fare based on route distance and duration.
     * Returns null if distance is invalid or route not yet selected.
     */
    fun calculateFare(
        distanceKm: Double,
        durationMinutes: Int,
        rideType: RideOptionType,
        passengerCount: Int = 1
    ): FareEstimate {
        val distanceCharge = distanceKm * RATE_PER_KM
        val timeCharge = durationMinutes * RATE_PER_MINUTE
        val fullTripVehicleFare = BASE_AIRPORT_FLAG_DOWN + distanceCharge + timeCharge + AIRPORT_TOLL_AND_PARKING

        return when (rideType) {
            RideOptionType.SHARED_POOL -> {
                // Shared rides divide the total trip vehicle fare among co-passengers.
                // In a typical 2-rider carpool with minimal detour factor (1.1x), each passenger pays ~55% of the full taxi cost.
                val sharedPoolFactor = if (passengerCount > 1) 0.85 else 0.55
                val perRiderFare = (fullTripVehicleFare * sharedPoolFactor)
                val roundedFinalFare = (perRiderFare / 5.0).roundToInt() * 5.0 // rounded to nearest ₹5

                val advanceDeposit = roundedFinalFare * ADVANCE_PAYMENT_RATIO
                val remainingBalance = roundedFinalFare - advanceDeposit
                val eligibleCancellationRefund = roundedFinalFare * CANCELLATION_REFUND_RATIO

                FareEstimate(
                    originalFare = roundedFinalFare,
                    discountAmount = 0.0,
                    finalEstimatedFare = roundedFinalFare,
                    advanceDepositRequired = advanceDeposit,
                    remainingBalancePayable = remainingBalance,
                    eligibleCancellationRefund = eligibleCancellationRefund,
                    routeDistanceKm = distanceKm,
                    routeDurationMinutes = durationMinutes,
                    isSolo = false,
                    baseFare = BASE_AIRPORT_FLAG_DOWN,
                    distanceCharge = distanceCharge,
                    timeCharge = timeCharge,
                    airportTollFee = AIRPORT_TOLL_AND_PARKING,
                    isConfirmed = false
                )
            }

            RideOptionType.SOLO_RIDE -> {
                // Solo private cab takes the full trip fare.
                // 10% promotional platform discount is deducted from the passenger fare.
                val roundedOriginalFare = (fullTripVehicleFare / 5.0).roundToInt() * 5.0
                val discountAmount = roundedOriginalFare * SOLO_PROMOTIONAL_DISCOUNT_PERCENT
                val finalFare = roundedOriginalFare - discountAmount

                val advanceDeposit = finalFare * ADVANCE_PAYMENT_RATIO
                val remainingBalance = finalFare - advanceDeposit
                val eligibleCancellationRefund = finalFare * CANCELLATION_REFUND_RATIO

                FareEstimate(
                    originalFare = roundedOriginalFare,
                    discountAmount = discountAmount,
                    finalEstimatedFare = finalFare,
                    advanceDepositRequired = advanceDeposit,
                    remainingBalancePayable = remainingBalance,
                    eligibleCancellationRefund = eligibleCancellationRefund,
                    routeDistanceKm = distanceKm,
                    routeDurationMinutes = durationMinutes,
                    isSolo = true,
                    baseFare = BASE_AIRPORT_FLAG_DOWN,
                    distanceCharge = distanceCharge,
                    timeCharge = timeCharge,
                    airportTollFee = AIRPORT_TOLL_AND_PARKING,
                    isConfirmed = false
                )
            }
        }
    }
}
