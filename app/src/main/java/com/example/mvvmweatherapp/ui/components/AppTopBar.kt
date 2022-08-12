package com.example.mvvmweatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AppTopBar(
    leadingIcon: @Composable (() -> Unit)?,
    pageName: String?,
    trailingIcon: @Composable (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(64.dp)
            .clip(
                MaterialTheme.shapes.medium.copy(
                    topEnd = CornerSize(0.dp),
                    topStart = CornerSize(0.dp)
                )
            )
            .background(MaterialTheme.colors.primaryVariant),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.invoke()

        Spacer(modifier = Modifier.weight(1f))

        pageName?.let { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.h6.copy(
                    color = MaterialTheme.colors.onSurface
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        trailingIcon?.invoke()
    }

}