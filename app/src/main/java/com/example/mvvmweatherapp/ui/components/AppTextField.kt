package com.example.mvvmweatherapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppTextField(
    value: String,
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    onValueChange: (String) -> Unit,
    maxLines: Int = Int.MAX_VALUE,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    placeholder: String = "",
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    textStyle: TextStyle = MaterialTheme.typography.body1.copy(
        textAlign = TextAlign.Start,
    ),
    colors: TextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colors.onBackground,
        disabledTextColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
        disabledLeadingIconColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
        disabledTrailingIconColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
        disabledPlaceholderColor = MaterialTheme.colors.onBackground.copy(alpha = 0.2f),
        cursorColor = MaterialTheme.colors.onBackground,
        backgroundColor = MaterialTheme.colors.surface,
        focusedLabelColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        errorIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )
) {
    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = singleLine,
            enabled = enabled,
            readOnly = readOnly,
            isError = false,
            textStyle = textStyle,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground.copy(0.5f)
                )
            },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            modifier = Modifier
                .fillMaxWidth()
                .then(textFieldModifier),
            maxLines = maxLines,
            visualTransformation = visualTransformation,
            keyboardActions = keyboardActions,
            keyboardOptions = keyboardOptions,
            shape = shape,
            colors = colors,
        )

    }
}