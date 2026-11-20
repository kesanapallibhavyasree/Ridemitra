package com.ridemitra.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ridemitra.app.ui.theme.BorderLight
import com.ridemitra.app.ui.theme.CardBackground
import com.ridemitra.app.ui.theme.ErrorRed
import com.ridemitra.app.ui.theme.PrimaryNavy
import com.ridemitra.app.ui.theme.RoyalBlue
import com.ridemitra.app.ui.theme.SuccessGreen
import com.ridemitra.app.ui.theme.SurfaceVariantLight
import com.ridemitra.app.ui.theme.TealAccent
import com.ridemitra.app.ui.theme.TextMuted
import com.ridemitra.app.ui.theme.TextPrimary
import com.ridemitra.app.ui.theme.TextSecondary

/**
 * Reusable Commercial Document Upload Box.
 * Handles upload triggers, selected file preview indicators, and replacement.
 */
@Composable
fun DocumentUploadBox(
    title: String,
    subtitle: String,
    uploadedUri: String?,
    onUploadClick: () -> Unit,
    onRemoveClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    isRequired: Boolean = true
) {
    val isUploaded = !uploadedUri.isNullOrBlank()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isUploaded) Color(0xFFF0FDF4) else Color(0xFFF8FAFC))
            .border(
                1.dp,
                if (isUploaded) Color(0xFFBBF7D0) else BorderLight,
                RoundedCornerShape(12.dp)
            )
            .clickable { onUploadClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(if (isUploaded) Color(0xFFDCFCE7) else SurfaceVariantLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isUploaded) Icons.Default.CheckCircle else Icons.Default.CloudUpload,
                        contentDescription = null,
                        tint = if (isUploaded) SuccessGreen else RoyalBlue,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        )
                        if (isRequired && !isUploaded) {
                            Text(
                                text = " *",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = ErrorRed,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = if (isUploaded) "Document attached (${uploadedUri})" else subtitle,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = if (isUploaded) Color(0xFF15803D) else TextSecondary,
                            fontSize = 11.sp
                        ),
                        maxLines = 1
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isUploaded) {
                    StatusBadge(text = "ATTACHED", variant = BadgeVariant.SUCCESS)
                    if (onRemoveClick != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = onRemoveClick,
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Remove",
                                tint = ErrorRed,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFEFF6FF))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Upload",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = RoyalBlue
                            )
                        )
                    }
                }
            }
        }
    }
}
