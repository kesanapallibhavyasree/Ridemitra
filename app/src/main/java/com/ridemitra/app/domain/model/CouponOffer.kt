package com.ridemitra.app.domain.model

/**
 * Represents a promotional offer, airport discount voucher, or coupon code.
 */
data class CouponOffer(
    val code: String,
    val title: String,
    val description: String,
    val discountPercent: Int,
    val maxDiscountAmount: Double,
    val minTripFare: Double = 300.0,
    val category: CouponCategory = CouponCategory.GENERAL,
    val isApplicableForSolo: Boolean = true,
    val isApplicableForPool: Boolean = true,
    val expiryText: String = "Valid till month end"
)

enum class CouponCategory {
    SOLO_PROMO,
    AIRPORT_SPLIT,
    FIRST_RIDE,
    GENERAL
}

val sampleCouponOffers = listOf(
    CouponOffer(
        code = "SOLO10",
        title = "10% Platform Solo Discount",
        description = "Automatically applied when booking solo rides if no matching carpool is found. Fully platform funded without driver deduction.",
        discountPercent = 10,
        maxDiscountAmount = 250.0,
        category = CouponCategory.SOLO_PROMO,
        isApplicableForSolo = true,
        isApplicableForPool = false,
        expiryText = "Always Active"
    ),
    CouponOffer(
        code = "AIRPORTFIRST",
        title = "₹150 Off First Airport Ride",
        description = "Enjoy ₹150 flat discount on your very first RideMitra airport pool or solo booking.",
        discountPercent = 20,
        maxDiscountAmount = 150.0,
        minTripFare = 400.0,
        category = CouponCategory.FIRST_RIDE,
        isApplicableForSolo = true,
        isApplicableForPool = true,
        expiryText = "Valid for new riders"
    ),
    CouponOffer(
        code = "POOLTOGETHER",
        title = "Extra 15% Off Airport Pool",
        description = "Share your journey with 2+ verified co-travelers and save an extra 15% on your total split fare.",
        discountPercent = 15,
        maxDiscountAmount = 200.0,
        minTripFare = 350.0,
        category = CouponCategory.AIRPORT_SPLIT,
        isApplicableForSolo = false,
        isApplicableForPool = true,
        expiryText = "Valid on Shared Pool"
    ),
    CouponOffer(
        code = "NCRFLYER",
        title = "Flat 10% Off Airport Transfers",
        description = "Valid across Delhi, Mumbai, and Bengaluru airport journeys during peak hours.",
        discountPercent = 10,
        maxDiscountAmount = 180.0,
        minTripFare = 500.0,
        category = CouponCategory.GENERAL,
        isApplicableForSolo = true,
        isApplicableForPool = true,
        expiryText = "Valid for 30 days"
    )
)
