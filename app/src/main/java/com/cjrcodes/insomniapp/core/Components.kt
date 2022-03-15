package com.cjrcodes.insomniapp.core

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.*
import com.cjrcodes.insomniapp.models.HeartRateMeasurementType
import com.cjrcodes.insomniapp.models.TimeTask
import com.cjrcodes.insomniapp.theme.WearAppTheme

@Composable
fun CreateTimeTaskButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center
    ) {
        // Button
        Button(
            modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
            onClick = onClick
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Create Time Task",
                modifier = modifier
            )
        }
    }
}

@Composable
fun TimeTaskChip(
    modifier: Modifier = Modifier,
    timeTask: TimeTask,
    onClick: (TimeTask) -> Unit
) {
    val averageMeasurementTime: String
    averageMeasurementTime =
        if (timeTask.hrMeasureType == HeartRateMeasurementType.AVERAGE) "Measure For: ${timeTask.averageMeasurementTime}" else ""
    Chip(
        modifier = modifier,
        enabled = true,
        label = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary,
                text = timeTask.getTimeBasedOnAlarmType(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        secondaryLabel = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary,
                text = "Max HR: ${timeTask.maxHeartRate}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if(averageMeasurementTime != "") {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colors.onPrimary,
                    text = averageMeasurementTime,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        },
        onClick = { }

    )
}


@Preview
@Composable
fun TimeTaskChipPreview() {
    WearAppTheme {
        TimeTaskChip(Modifier, TimeTask.createTimeTaskList(1).get(0)) {}
    }

}