package com.example.mvvmweatherapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
        Column(
            modifier = Modifier.requiredSize(64.dp),
            verticalArrangement = Arrangement.Center
        ) {
            leadingIcon?.invoke()
        }

        Spacer(modifier = Modifier.weight(1f))

        pageName?.let { name ->
            Text(
                text = name,
                style = MaterialTheme.typography.h6.copy(
                    color = Color.White
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Column(
            modifier = Modifier.requiredSize(64.dp),
            verticalArrangement = Arrangement.Center
        ) {
            trailingIcon?.invoke()
        }
    }

}