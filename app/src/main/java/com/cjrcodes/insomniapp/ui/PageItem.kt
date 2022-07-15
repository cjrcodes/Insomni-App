package com.cjrcodes.insomniapp

import android.content.ContentProvider
import androidx.compose.foundation.layout.*
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
//import com.cjrcodes.insomniapp.core.vibrateDevice
import com.cjrcodes.insomniapp.destinations.WearAppDestination
import com.cjrcodes.insomniapp.domain.models.Alarm.AlarmType
import com.cjrcodes.insomniapp.domain.models.Alarm.Alarm
import com.cjrcodes.insomniapp.ui.theme.*
import com.cjrcodes.insomniapp.utils.Config
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter


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
) {
    val alarmTypeOptions = arrayListOf<String>("Elapsed Timer", "Clock Time")
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
                    AlarmTypePickerPage(Modifier, pickerState, alarmTypeOptions, alarmState, ) {
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
                        contentDescription = "Select Alarm Type"
                    )
                },
                onClick = onClick
            )
        }
    }
}

@OptIn(ExperimentalWearMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Destination
@Composable
fun AlarmTypePickerPage(
    modifier: Modifier = Modifier,
    pickerState: PickerState,
    options: List<String>,
    alarmState: MutableState<Alarm>,
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
                        alarmState.value.alarmType = if (pickerState.selectedOption == 1) AlarmType.CLOCK_TIME else AlarmType.ELAPSED_TIME
                        println(alarmState.value.alarmType)
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
    alarmState: MutableState<Alarm>,
    fullHoursPickerState: MutableState<FullHours>
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
                TimeSelect24HourView(modifier, alarmState)


            }

            //Elapsed time display
            else {

                Text("Select Time")

                if (Config.getTwelveHourFormat()) {

                    TimeSelect12HourView(modifier, alarmState)

                } else {


                    TimeSelect24HourView(modifier, alarmState)


                }

            }
        }
    }
}

@Composable
fun TimeSelect12HourView(modifier: Modifier = Modifier, alarmState: MutableState<Alarm>) {

    var pickerValue by remember {
        mutableStateOf<AMPMHours>(
            AMPMHours(
                12,
                0,
                AMPMHours.DayTime.AM
            )
        )
    }

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm a")

    AMPMHoursNumberPicker(
        leadingZero = true,
        dividersColor = WearAppColorPalette.primary,
        value = pickerValue,
        onValueChange = {
            pickerValue = it as AMPMHours
            val leadingZeroText = { i: Int -> if (i < 10) "0" else "" }
            alarmState.value.time =
                LocalTime.parse(
                    "${leadingZeroText(it.hours)}${it.hours}:${leadingZeroText(it.minutes)}${it.minutes} ${it.dayTime}",
                    formatter
                )
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
        textStyle = TextStyle(Color.White),
        hoursRange = 1..12
    )

    println("Time:" + alarmState.value.time)
}

@Composable
fun TimeSelect24HourView(
    modifier: Modifier = Modifier, alarmState: MutableState<Alarm>
) {

    var pickerValue by remember { mutableStateOf<Hours>(FullHours(0, 0)) }

    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    HoursNumberPicker(
        leadingZero = true,
        dividersColor = WearAppColorPalette.primary,
        value = pickerValue,
        onValueChange = {
            pickerValue = it
            val leadingZeroText = { i: Int -> if (i < 10) "0" else "" }
            alarmState.value.time =
                LocalTime.parse(
                    "${leadingZeroText(it.hours)}${it.hours}:${leadingZeroText(it.minutes)}${it.minutes}",
                    formatter
                )
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

    println(alarmState.value.time)
    //timeTaskState.value.time = LocalTime.parse(pickerValue.hours.toString())

}

@Composable
fun HeartRatePage(
    modifier: Modifier = Modifier,
    alarmState: MutableState<Alarm>,
    heartRateState: MutableState<Int>
) {
    val minHeartRateValue = 40
    val maxHeartRateValue = 100
    val heartRateValuesMid = (maxHeartRateValue + minHeartRateValue) / 2
    var pickerValue by remember { mutableStateOf(heartRateValuesMid) }
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
                    heartRateState.value = it
                    alarmState.value.maxHeartRate = pickerValue
                },
                dividersColor = WearAppColorPalette.primary,
                range = minHeartRateValue..maxHeartRateValue,
                textStyle = TextStyle(Color.White)
            )
        }
        //timeTaskState.value.maxHeartRate = pickerValue
        println("HR:" + alarmState.value.maxHeartRate)
    }

}


@Composable
fun TimeTaskDetailsPage(
    modifier: Modifier = Modifier,
    navigator: DestinationsNavigator,
    alarmState: MutableState<Alarm>,
    timeState: MutableState<FullHours>,
    heartRateState: MutableState<Int>
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
                        text = "Create Alarm",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                },

                icon = {
                    Icon(
                        Icons.Filled.MoreTime,
                        contentDescription = "Create Alarm Button"
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
        val alarmState: MutableState<Alarm> = remember { mutableStateOf(Alarm()) }

        AlarmTypePickerPage(Modifier, pickerState, alarmTypeOptions, alarmState) {}
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
        val alarmState: MutableState<Alarm> = remember { mutableStateOf(Alarm()) }

        AlarmTypePage(Modifier, EmptyDestinationsNavigator, pickerState, alarmState)
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
        // Display 10 items
        val alarmState: MutableState<Alarm> = remember { mutableStateOf(Alarm()) }
        val heartRateState: MutableState<Int> = remember { mutableStateOf(70) }

        HeartRatePage(Modifier, alarmState, heartRateState)
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
    val alarmState: MutableState<Alarm> = remember { mutableStateOf(Alarm()) }

    TimeSelect24HourView(Modifier, alarmState)
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
    val alarmState: MutableState<Alarm> = remember { mutableStateOf(Alarm()) }

    TimeSelect12HourView(Modifier, alarmState)
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
fun TimeTaskDetailsPagePreview() {
    WearAppTheme {

        val alarmState: MutableState<Alarm> = remember { mutableStateOf(Alarm()) }
        val fullHoursPickerState: MutableState<FullHours> =
            remember { mutableStateOf<FullHours>(FullHours(0, 0)) }
        val heartRateState: MutableState<Int> = remember { mutableStateOf(70) }

        TimeTaskDetailsPage(
            Modifier,
            EmptyDestinationsNavigator,
            alarmState,
            fullHoursPickerState,
            heartRateState
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
fun StateTextChangePreview() {
    val timePickerState = remember { mutableStateOf<Hours>(FullHours(0, 0)) }
    val alarmState: MutableState<Alarm> = remember { mutableStateOf(Alarm()) }

    LaunchedEffect(alarmState.value) {

    }

    WearAppTheme {
        TimeSelect24HourView(Modifier, alarmState)

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
fun ButtonVibrateTestPreview(){
    /*val mVibratePattern = longArrayOf(0, 400, 200, 400)
    val vibrator: Vibrator =  Vibrator()
    val vibrationEffect: VibrationEffect? = null*/
   /* val vibrator:Vibrator
    vibrator = getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    vibrator.vibrate(100)*/

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ){
        Button(
            content = {
                Text("Vibrate")
            },
            onClick = {
                /*if (vibrator != null) {
                    vibrator.vibrate(vibrationEffect)
                }*/
                val contentProvider: ContentProvider
                //vibrateDevice(requireContext())

            })
    }




}

