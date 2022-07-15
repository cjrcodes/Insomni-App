package com.cjrcodes.insomniapp.core

import android.text.format.DateFormat.format
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
import com.cjrcodes.insomniapp.domain.models.Alarm.Alarm
import com.cjrcodes.insomniapp.ui.theme.WearAppTheme
import java.time.LocalTime

@Composable
fun CreateAlarmButton(
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
fun AlarmChip(
    modifier: Modifier = Modifier,
    alarm: Alarm,
    onClick: (Alarm) -> Unit
) {
    //val averageMeasurementTime: String = if (timeTask.hrMeasureType == HeartRateMeasurementType.AVERAGE) "Measure For: ${timeTask.averageMeasurementTime}" else ""
    Chip(
        modifier = modifier,
        enabled = true,
        label = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary,
                text = "${alarm.time}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        secondaryLabel = {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.onPrimary,
                text = "Max HR: ${alarm.maxHeartRate}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        onClick = { }

    )
}

@Preview
@Composable
fun CreateAlarmButtonPreview() {
    WearAppTheme {
        CreateAlarmButton(Modifier) {}


    }
}


@Preview
@Composable
fun AlarmChipPreview() {
    WearAppTheme {
        AlarmChip(Modifier, Alarm()) {}


    }

}





@Preview
@Composable
fun Alarm24ChipPreview() {
    WearAppTheme {
        AlarmChip(Modifier, Alarm()) {}
    }
}



