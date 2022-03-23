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
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator

@Composable
fun CreateTimeTaskButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,

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
                text = timeTask.timeBasedOnAlarmType,
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
            if (averageMeasurementTime != "") {
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
fun CreateTimeTaskButtonPreview() {
    WearAppTheme {
        CreateTimeTaskButton(Modifier) {}


    }
}

val timeTasks: ArrayList<TimeTask> = TimeTask.createTimeTaskList(2)


@Preview
@Composable
fun TimeTaskChipPreview() {
    WearAppTheme {
        TimeTask.setIsTwelveHourFormat(true)
        TimeTaskChip(Modifier, timeTasks[0]) {}


    }

}

@Preview
@Composable
fun TimeTaskAverageMeasurementChipPreview() {
    WearAppTheme {
        TimeTask.setIsTwelveHourFormat(true)
        TimeTaskChip(Modifier, timeTasks[1]) {}


    }

}

val timeTasks24: ArrayList<TimeTask> = TimeTask.createTimeTaskList(2)


@Preview
@Composable
fun TimeTask24ChipPreview() {
    WearAppTheme {
        TimeTask.setIsTwelveHourFormat(false)
        TimeTaskChip(Modifier, timeTasks24[0]) {}
    }
}

@Preview
@Composable
fun TimeTask24AverageMeasurementChipPreview() {
    WearAppTheme {
        TimeTask.setIsTwelveHourFormat(false)
        TimeTaskChip(Modifier, timeTasks24[1]) {}
    }

}

