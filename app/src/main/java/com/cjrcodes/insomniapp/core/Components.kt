package com.cjrcodes.insomniapp.core

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import com.cjrcodes.insomniapp.destinations.DirectionDestination
import com.cjrcodes.insomniapp.models.TimeTask
import com.cjrcodes.insomniapp.theme.WearAppTheme
import org.w3c.dom.Text




@Composable
fun TimeTaskChip(
    timeTask: TimeTask,
    text: String,
    modifier: Modifier = Modifier,
    onClick: (TimeTask) -> Unit
) {

}

@Preview
@Composable
fun TimeTaskChipPreview() {
    WearAppTheme {

    }

}