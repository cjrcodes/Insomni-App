package com.cjrcodes.insomniapp

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.MoreTime
import androidx.compose.material.icons.filled.West
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.chargemap.compose.numberpicker.*

import com.cjrcodes.insomniapp.destinations.WearAppDestination
import com.cjrcodes.insomniapp.models.TimeTask
import com.cjrcodes.insomniapp.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch
import java.time.LocalTime

/**
 * Simple pager item which displays an image
 */
@OptIn(com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
internal fun PageItem(
    modifier: Modifier = Modifier,
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

    }
}

@OptIn(
    com.google.accompanist.pager.ExperimentalPagerApi::class,
)
@Composable
@ExperimentalWearMaterialApi
internal fun AlarmTypePage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    pickerState: PickerState,
    timeTaskState: MutableState<TimeTask>
) {
    val alarmTypeOptions = arrayListOf<String>("Elapsed Timer","Clock Time")
    val swipeState = rememberSwipeToDismissBoxState()

    // Alternatively, use SwipeDismissableNavHost from wear.compose.navigation.
    var showMainScreen by remember { mutableStateOf(true) }
    val saveableStateHolder = rememberSaveableStateHolder()


    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == SwipeDismissTarget.Dismissal) {
            swipeState.snapTo(SwipeDismissTarget.Original)
            showMainScreen = !showMainScreen
        }


    }
    WearAppTheme {

        SwipeToDismissBox(
            modifier = Modifier.fillMaxSize(),/*,
         contentAlignment = Alignment.Center*/
            state = swipeState, hasBackground = !showMainScreen,
            backgroundKey = if (!showMainScreen) "MainKey" else "Background",
            contentKey = if (showMainScreen) "MainKey" else "ItemKey",
        ) { isBackground ->
            if (isBackground || showMainScreen) {
                // Best practice would be to use State Hoisting and leave this composable stateless.
                // Here, we want to support MainScreen being shown from different destinations
                // (either in the foreground or in the background during swiping) - that can be achieved
                // using SaveableStateHolder and rememberSaveable as shown below.
                saveableStateHolder.SaveableStateProvider(
                    key = "MainKey"
                ) {
                    // Composable that maintains its own state
                    // and can be shown in foreground or background.
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalArrangement =
                        Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    ) {

                        AlarmTypeDisplayPage(
                            Modifier,
                            alarmTypeOptions,
                            pickerState,
                            navigator
                        ) { showMainScreen = false }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    AlarmTypePickerPage(Modifier, pickerState, alarmTypeOptions) {
                        showMainScreen = true
                    }

                }
            }

        }
    }

}

@Composable
fun AlarmTypeDisplayPage(
    modifier: Modifier,
    options: List<String>,
    pickerState: PickerState,
    navigator: DestinationsNavigator,
    onClick: () -> Unit
) {

    WearAppTheme {


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {

            Chip(

                modifier = modifier,


                enabled = true,
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Return To Main Menu",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                },

                icon = {
                    Icon(
                        Icons.Filled.West,
                        contentDescription = "Return To Main Menu"
                    )
                },
                onClick = { navigator.navigate(WearAppDestination) }
            )

            Chip(

                modifier = modifier,

                colors = ChipDefaults.chipColors(
                    backgroundColor = Color.Transparent
                ),
                enabled = true,
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Alarm Type",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                },
                secondaryLabel = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,

                        text = options[pickerState.selectedOption],
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )


                },
                icon = {
                    Icon(
                        Icons.Filled.ArrowForwardIos,
                        contentDescription = "Return To Main Menu Button"
                    )
                },
                onClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Destination
@Composable
fun AlarmTypePickerPage(
    modifier: Modifier = Modifier,
    pickerState: PickerState,
    options: List<String>,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()


    WearAppTheme {
        Picker(
            state = pickerState,
            modifier = Modifier,
            separation = 25.dp,
        ) {
            CompactChip(

                onClick = {
                    coroutineScope.launch {
                        pickerState.scrollToOption(it)
                        onClick()
                    }


                },

                label = {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = options[it]
                    )
                }
            )
        }
    }

}

@Composable
fun TimeSelectPage(
    modifier: Modifier = Modifier,
    alarmTypePickerState: PickerState,
    timePickerState: MutableState<Hours>
) {

    WearAppTheme {
        //Clock time Selected, display clock version of time


        //timePickerState.value = pickerValue

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (alarmTypePickerState.selectedOption == 0) {
                Text("Timer Length")

                var pickerValue by remember { mutableStateOf<Hours>(FullHours(0, 15)) }

                HoursNumberPicker(
                    leadingZero = true,
                    dividersColor = WearAppColorPalette.primary,
                    value = pickerValue,
                    onValueChange = {
                        pickerValue = it
                    },
                    hoursDivider = {
                        Text(
                            modifier = Modifier.size(24.dp),
                            textAlign = TextAlign.Center,
                            text = ":"
                        )
                    }, textStyle = TextStyle(Color.White)

                )


            }

            //Elapsed time display
            else {

                Text("Select Time")

                if (TimeTask.getIsTwelveHourFormat() == true) {

                    val timePickerState =
                        remember { mutableStateOf<Hours>(AMPMHours(12, 0, AMPMHours.DayTime.AM)) }
                    TimeSelect12HourView(modifier, timePickerState)


                } else {


                    TimeSelect24HourView(modifier, timePickerState)


                }

            }
        }
    }
}

@Composable
fun TimeSelect12HourView(modifier: Modifier = Modifier, timePickerState: MutableState<Hours>) {

    var pickerValue by remember {
        mutableStateOf<Hours>(
            AMPMHours(
                12,
                0,
                AMPMHours.DayTime.AM
            )
        )
    }
    HoursNumberPicker(
        leadingZero = true,

        dividersColor = WearAppColorPalette.primary,
        value = pickerValue,
        onValueChange = {
            pickerValue = it
        },
        hoursDivider = {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                text = ":",
                color = Color.White

            )
        },
        minutesDivider = {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
                text = "",
                color = Color.White

            )
        },
        textStyle = TextStyle(Color.White)

    )

    timePickerState.value = pickerValue
}

@Composable
fun TimeSelect24HourView(modifier: Modifier = Modifier, timePickerState: MutableState<Hours>) {

    var pickerValue by remember { mutableStateOf<Hours>(FullHours(0, 0)) }


    HoursNumberPicker(
        leadingZero = true,
        dividersColor = WearAppColorPalette.primary,
        value = pickerValue,
        onValueChange = {
            pickerValue = it
        },
        hoursDivider = {
            Text(
                modifier = Modifier.size(24.dp),
                textAlign = TextAlign.Center,
                text = ":"

            )
        },

        textStyle = TextStyle(Color.White)

    )

    timePickerState.value = pickerValue

}

@Composable
fun HeartRatePage(
    modifier: Modifier = Modifier,
    heartRateState: MutableState<Int>
) {
    val minHeartRateValue = 40
    val maxHeartRateValue = 100
    val heartRateValuesMid = (maxHeartRateValue + minHeartRateValue) / 2
    var pickerValue by remember { mutableStateOf(heartRateValuesMid) }
    heartRateState.value = pickerValue
    WearAppTheme {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Max Heart Rate")
            NumberPicker(
                modifier,
                leadingZero = true,

                value = pickerValue,
                onValueChange = {
                    pickerValue = it
                },
                dividersColor = WearAppColorPalette.primary,
                range = minHeartRateValue..maxHeartRateValue,
                textStyle = TextStyle(Color.White)
            )
        }
    }

}

@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun HeartRateMeasurementTypePage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    pickerState: PickerState,
    averageMeasurementTimeState: MutableState<LocalTime>,
    timePickerState: MutableState<Hours>
) {
    val heartRateMeasurementTypeOptions = arrayListOf<String>("Current", "Average Over Time")
    val swipeState = rememberSwipeToDismissBoxState()

    // Alternatively, use SwipeDismissableNavHost from wear.compose.navigation.
    var showMainScreen by remember { mutableStateOf(true) }
    val saveableStateHolder = rememberSaveableStateHolder()

    val hrMeasurementTypeChipSelectState: MutableState<Int> = remember { mutableStateOf(0) }

    LaunchedEffect(swipeState.currentValue) {
        if (swipeState.currentValue == SwipeDismissTarget.Dismissal) {
            swipeState.snapTo(SwipeDismissTarget.Original)
            showMainScreen = !showMainScreen
        }
    }

    WearAppTheme {

        SwipeToDismissBox(
            modifier = Modifier.fillMaxSize(),/*,
         contentAlignment = Alignment.Center*/
            state = swipeState, hasBackground = !showMainScreen,
            backgroundKey = if (!showMainScreen) "MainKey" else "Background",
            contentKey = if (showMainScreen) "MainKey" else "ItemKey",
        ) { isBackground ->
            if (isBackground || showMainScreen) {
                // Best practice would be to use State Hoisting and leave this composable stateless.
                // Here, we want to support MainScreen being shown from different destinations
                // (either in the foreground or in the background during swiping) - that can be achieved
                // using SaveableStateHolder and rememberSaveable as shown below.
                saveableStateHolder.SaveableStateProvider(
                    key = "MainKey"
                ) {
                    // Composable that maintains its own state
                    // and can be shown in foreground or background.
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalArrangement =
                        Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    ) {

                        HeartRateMeasurementTypeDisplayPage(
                            Modifier,
                            heartRateMeasurementTypeOptions,
                            pickerState,
                            navigator,
                            averageMeasurementTimeState,
                            hrMeasurementTypeChipSelectState
                        ) { showMainScreen = false }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    if (hrMeasurementTypeChipSelectState.value == 0) {
                        HeartRateMeasurementTypePickerView(
                            Modifier,
                            pickerState,
                            heartRateMeasurementTypeOptions
                        ) {
                            showMainScreen = true
                        }
                    } else {
                        AverageMeasurementTimePickerView(
                            modifier,
                            averageMeasurementTimeState,
                            timePickerState
                        )
                    }


                }
            }

        }
    }
}

@Composable
fun HeartRateMeasurementTypeDisplayPage(
    modifier: Modifier = Modifier,
    options: List<String>,
    pickerState: PickerState,
    navigator: DestinationsNavigator,
    averageMeasurementTimeState: MutableState<LocalTime>,
    hrMeasurementTypeChipSelectState: MutableState<Int>,
    onClick: () -> Unit
) {

    WearAppTheme {


        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {


            Chip(

                modifier = modifier,

                colors = ChipDefaults.chipColors(
                    backgroundColor = Color.Transparent
                ),
                enabled = true,
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "HR Measure Type",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                },
                secondaryLabel = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,

                        text = options[pickerState.selectedOption],
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )


                },
                icon = {
                    Icon(
                        Icons.Filled.ArrowForwardIos,
                        contentDescription = "Select Heart Rate Measurement Type"
                    )
                },
                onClick = {
                    hrMeasurementTypeChipSelectState.value = 0
                    onClick()
                }
            )

            Chip(

                modifier = modifier,

                colors = ChipDefaults.chipColors(
                    backgroundColor = Color.Transparent
                ),
                enabled = pickerState.selectedOption == 1,
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Time To Measure",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                },
                secondaryLabel = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,

                        text = averageMeasurementTimeState.value.toString(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )


                },
                icon = {
                    Icon(
                        Icons.Filled.ArrowForwardIos,
                        contentDescription = "Time To Measure"
                    )
                },
                onClick = {

                    hrMeasurementTypeChipSelectState.value = 1
                    onClick()

                }
            )

            Chip(

                modifier = modifier,


                enabled = true,
                label = {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "Create Time Task",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                },

                icon = {
                    Icon(
                        Icons.Filled.MoreTime,
                        contentDescription = "Create Time Task Button"
                    )
                }, onClick = { navigator.navigate(WearAppDestination) }
            )
        }
    }

}

@OptIn(ExperimentalWearMaterialApi::class)
@Destination
@Composable
fun HeartRateMeasurementTypePickerView(
    modifier: Modifier = Modifier,
    pickerState: PickerState,
    options: List<String>,
    onClick: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()


    WearAppTheme {
        Picker(
            state = pickerState,
            modifier = Modifier,
            separation = 25.dp,
        ) {
            CompactChip(

                onClick = {
                    coroutineScope.launch {
                        pickerState.scrollToOption(it)
                        onClick()

                    }

                },

                label = {

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = options[it]
                    )
                }
            )
        }
    }

}

@Composable
fun AverageMeasurementTimePickerView(
    modifier: Modifier = Modifier,
    averageTimePickerState: MutableState<LocalTime>,
    timePickerState: MutableState<Hours>
) {

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {


        Row(
            modifier = Modifier
                .fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            var hourPickerValue by remember { mutableStateOf(0) }

            var minutePickerValue by remember { mutableStateOf(0) }

            NumberPicker(
                leadingZero = true,
                value = minutePickerValue,
                range = 0..10/*timePickerState.value.hours*/,
                onValueChange = {
                    minutePickerValue = it
                },
                textStyle = TextStyle(Color.White)

            )
            Text(
                modifier = Modifier.size(24.dp),
                textAlign = TextAlign.Center,
                text = ":"

            )

            NumberPicker(
                leadingZero = true,
                value = minutePickerValue,
                range = 0..timePickerState.value.minutes,
                onValueChange = {
                    minutePickerValue = it
                },
                textStyle = TextStyle(Color.White)

            )

            averageTimePickerState.value = LocalTime.of(hourPickerValue, minutePickerValue)
        }


    }
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun AlarmTypePickerPagePreview() {
    WearAppTheme {
        val alarmTypeOptions = arrayListOf<String>("Clock Time", "Elapsed Timer")
        val pickerState = rememberPickerState(initialNumberOfOptions = 2, repeatItems = false)
        AlarmTypePickerPage(Modifier, pickerState, alarmTypeOptions) {}
    }
}


@OptIn(ExperimentalWearMaterialApi::class)
@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun AlarmTypePagePreview() {
    WearAppTheme {
        val pickerState = rememberPickerState(initialNumberOfOptions = 2, repeatItems = false)
        val timeTaskState: MutableState<TimeTask> = remember { mutableStateOf(TimeTask()) }

        AlarmTypePage(Modifier, EmptyDestinationsNavigator, pickerState, timeTaskState)
    }
}

@OptIn(ExperimentalWearMaterialApi::class)
@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun HeartRatePagePreview() {
    WearAppTheme {
        val heartRateState: MutableState<Int> =
            remember { mutableStateOf(70) }        // Display 10 items

        HeartRatePage(Modifier, heartRateState)
    }
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)

//Clock time, time select preview, 24 hour format
@Composable
fun TimeSelect24HourViewPreview() {
    val timePickerState = remember { mutableStateOf<Hours>(FullHours(0, 0)) }

    TimeSelect24HourView(Modifier, timePickerState)
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
//Clock time, time select preview, 24 hour format
@Composable
fun TimeSelect12HourViewPreview() {
    val timePickerState = remember { mutableStateOf<Hours>(AMPMHours(12, 0, AMPMHours.DayTime.AM)) }

    TimeSelect12HourView(Modifier, timePickerState)
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun HeartRateMeasurementTypePagePreview() {
    WearAppTheme {
        val pickerState = rememberPickerState(initialNumberOfOptions = 2, repeatItems = false)
        val averageMeasurementTimeState: MutableState<LocalTime> =
            remember { mutableStateOf(LocalTime.MIN) }
        val timePickerState =
            remember { mutableStateOf<Hours>(AMPMHours(12, 0, AMPMHours.DayTime.AM)) }

        HeartRateMeasurementTypePage(
            Modifier,
            EmptyDestinationsNavigator,
            pickerState,
            averageMeasurementTimeState,
            timePickerState
        )
    }
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
//Clock time, time select preview, 24 hour format
@Composable
fun HeartRateMeasurementTypePickerPagePreview() {
    val heartRateMeasurementTypeOptions = arrayListOf<String>("Current", "Average Over Time")
    val pickerState = rememberPickerState(initialNumberOfOptions = 2, repeatItems = false)
    HeartRateMeasurementTypePickerView(Modifier, pickerState, heartRateMeasurementTypeOptions) {}
}

@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
//Clock time, time select preview, 24 hour format
@Composable
fun AverageMeasurementTimePickerViewPreview() {
    val averageMeasurementTimeState: MutableState<LocalTime> =
        remember { mutableStateOf(LocalTime.MIN) }
    val timePickerState =
        remember { mutableStateOf<Hours>(AMPMHours(12, 0, AMPMHours.DayTime.AM)) }
    AverageMeasurementTimePickerView(Modifier, averageMeasurementTimeState, timePickerState)
}