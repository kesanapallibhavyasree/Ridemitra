package com.ridemitra.app.ui.screens.passenger

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.HorizontalDivider
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
import com.ridemitra.app.domain.model.CouponOffer
import com.ridemitra.app.domain.model.sampleCouponOffers
import com.ridemitra.app.ui.components.AirportIconBadge
import com.ridemitra.app.ui.components.BadgeVariant
import com.ridemitra.app.ui.components.RideMitraButton
import com.ridemitra.app.ui.components.RideMitraCard
import com.ridemitra.app.ui.components.RideMitraHeader
import com.ridemitra.app.ui.components.RideMitraTextField
import com.ridemitra.app.ui.components.StatusBadge
import com.ridemitra.app.ui.theme.BackgroundLight
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.HeroTealGradient
import com.ridemitra.app.ui.theme.PrimaryGradient
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Screen 20: Offers and Coupons Screen.
 * Displays available airport discount codes, including the platform-funded 10% solo promo discount,
 * with coupon redemption input and terms.
 */
@Composable
fun OffersCouponsScreen(
    coupons: List<CouponOffer> = sampleCouponOffers,
    onCouponApplied: (CouponOffer) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var promoInput by remember { mutableStateOf("") }
    var appliedCode by remember { mutableStateOf<String?>("SOLO10") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundLight)
    ) {
        RideMitraHeader(
            title = "Airport Offers & Promos",
            subtitle = "Guaranteed discounts & platform-funded savings"
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Promo Code Input Box
            item {
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Have a Promotional Airport Voucher?",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrimaryNavy
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.weight(1f)) {
                                RideMitraTextField(
                                    value = promoInput,
                                    onValueChange = { promoInput = it.uppercase() },
                                    placeholder = "Enter coupon code (e.g. SOLO10)",
                                    leadingIcon = Icons.Default.LocalOffer
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            RideMitraButton(
                                text = "Apply",
                                onClick = {
                                    if (promoInput.isNotBlank()) {
                                        appliedCode = promoInput
                                    }
                                },
                                modifier = Modifier.width(90.dp),
                                height = 48.dp,
                                gradient = PrimaryGradient
                            )
                        }

                        if (appliedCode != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Coupon $appliedCode is active and will apply to your next trip",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = SuccessGreen,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Platform Policy Assurance Card
            item {
                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = Color(0xFFEFF6FF),
                    borderColor = Color(0xFFBFDBFE),
                    elevation = 1.dp
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AirportIconBadge(
                            icon = Icons.Default.Shield,
                            size = 36.dp,
                            iconSize = 18.dp,
                            tint = RoyalBlue,
                            background = Color(0xFFDBEAFE)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Fair Payout Guarantee",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = RoyalBlue
                                )
                            )
                            Text(
                                text = "Promotional discounts (including the 10% solo promo) are 100% funded by RideMitra. Driver payouts are never reduced.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color(0xFF1E40AF),
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }
            }

            // Coupon Cards List
            items(coupons, key = { it.code }) { coupon ->
                val isCurrentlyApplied = appliedCode == coupon.code

                RideMitraCard(
                    modifier = Modifier.fillMaxWidth(),
                    borderColor = if (isCurrentlyApplied) TealAccent else BorderLight,
                    backgroundColor = if (isCurrentlyApplied) Color(0xFFF4FCFA) else CardBackground,
                    elevation = if (isCurrentlyApplied) 3.dp else 1.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFE0F7F4))
                                        .border(1.dp, TealAccent, RoundedCornerShape(6.dp))
                                        .padding(horizontal = 10.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = coupon.code,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color(0xFF0F8C7B),
                                            letterSpacing = 1.sp
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                StatusBadge(
                                    text = "${coupon.discountPercent}% OFF",
                                    variant = BadgeVariant.TEAL
                                )
                            }

                            if (isCurrentlyApplied) {
                                StatusBadge(text = "APPLIED", variant = BadgeVariant.SUCCESS)
                            } else {
                                Text(
                                    text = "Tap to Apply",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = RoyalBlue,
                                        fontWeight = FontWeight.Bold
                                    ),
                                    modifier = Modifier
                                        .clickable {
                                            appliedCode = coupon.code
                                            onCouponApplied(coupon)
                                        }
                                        .padding(4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = coupon.title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = coupon.description,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp,
                                lineHeight = 16.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = BorderLight)
                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Max Discount: ₹${coupon.maxDiscountAmount.toInt()}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                            Text(
                                text = coupon.expiryText,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextMuted,
                                    fontSize = 10.sp
                                )
                            )
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
