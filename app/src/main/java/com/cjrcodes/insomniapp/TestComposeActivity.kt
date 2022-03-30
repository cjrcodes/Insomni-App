package com.cjrcodes.insomniapp

import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.chargemap.compose.numberpicker.FullHours
import com.chargemap.compose.numberpicker.Hours
import com.cjrcodes.insomniapp.core.CreateTimeTaskButton
import com.cjrcodes.insomniapp.core.TimeTaskChip
import com.cjrcodes.insomniapp.destinations.CreateTimeTaskScreenDestination
import com.cjrcodes.insomniapp.models.TimeTask
import com.cjrcodes.insomniapp.models.TimeTask.createTimeTaskList
import com.cjrcodes.insomniapp.theme.*
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch
import java.time.LocalTime


class TestComposeActivity : ComponentActivity() {
    @OptIn(ExperimentalWearMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        TimeTask.setIsTwelveHourFormat(!DateFormat.is24HourFormat(applicationContext))

        setContent {
            WearAppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

@Destination(start = true)
@Composable
@OptIn(
    ExperimentalWearMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
fun WearApp(
    navigator: DestinationsNavigator
) {
    WearAppTheme {

        val listState = rememberScalingLazyListState()
        val focusRequester = remember { FocusRequester() }

        /* *************************** Part 4: Wear OS Scaffold *************************** */
        Scaffold(
            timeText = {
                if (!listState.isScrollInProgress) {
                    TimeText()
                }
            },
            vignette = {
                // Only show a Vignette for scrollable screens. This code lab only has one screen,
                // which is scrollable, so we show it all the time.
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(
                    scalingLazyListState = listState
                )
            }
        ) {
            // Modifiers used by our Wear composables.
            val contentModifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(bottom = 8.dp)
            val iconModifier = Modifier
                .size(24.dp)
                .wrapContentSize(align = Alignment.Center)
            /* *************************** Part 3: ScalingLazyColumn *************************** */
            var timeTasks: List<TimeTask> = createTimeTaskList(5);

            MainMenuScreen(contentModifier, timeTasks, navigator)


        }


    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainMenuScreen(
    modifier: Modifier,
    timeTasks: List<TimeTask>,
    navigator: DestinationsNavigator
) {
    val listState = rememberScalingLazyListState()
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth().onRotaryScrollEvent {
                coroutineScope.launch {
                    listState.scrollBy(it.verticalScrollPixels)
                }
                true
            }
            .focusRequester(focusRequester)
            .focusable(),
        contentPadding = PaddingValues(
            horizontal = 8.dp, vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState
    ) {
        item {
            CreateTimeTaskButton(Modifier) {
                navigator.navigate(
                    CreateTimeTaskScreenDestination
                )


            }
        }
        items(timeTasks) { timeTask ->
            TimeTaskChip(Modifier, timeTask) {}
        }
    }
}


@OptIn(ExperimentalWearMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Destination
@Composable
fun CreateTimeTaskScreen(
    navigator: DestinationsNavigator,
    ) {
    val listState = rememberScalingLazyListState()

    /* *************************** Part 4: Wear OS Scaffold *************************** */
    Scaffold(

        timeText = {
            if (!listState.isScrollInProgress) {
                TimeText()
            }
        },
        vignette = {
            // Only show a Vignette for scrollable screens. This code lab only has one screen,
            // which is scrollable, so we show it all the time.
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        },
        positionIndicator = {
            PositionIndicator(
                scalingLazyListState = listState
            )
        },
        modifier = Modifier


    ) {
        //Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        val pagerState = rememberPagerState()
        val alarmTypePickerState = rememberPickerState(2, repeatItems = false)
        val timePickerState = remember { mutableStateOf<Hours>(FullHours(0, 0)) }
        val heartRateState: MutableState<Int> = remember { mutableStateOf(70) }
        val heartRateMeasurementTypePickerState = rememberPickerState(2, repeatItems = false)
        val averageMeasurementTimeState: MutableState<LocalTime> = remember { mutableStateOf(LocalTime.MIN) }
        val timeTaskState: MutableState<TimeTask> = remember { mutableStateOf(TimeTask()) }
        val coroutineScope = rememberCoroutineScope()
        val focusRequester = remember { FocusRequester() }
        val configuration = LocalConfiguration.current

        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

// Display 10 items
        HorizontalPager(

            count = 4,
            state = pagerState,
            // Add 32.dp horizontal padding to 'center' the pages
            modifier = Modifier.fillMaxSize().onRotaryScrollEvent {
                coroutineScope.launch {
                        pagerState.scrollBy(it.verticalScrollPixels)

                }
                true
            }
                .focusRequester(focusRequester)
                .focusable()
        )
        { page ->

            when (page) {
                0 -> AlarmTypePage(Modifier, navigator, alarmTypePickerState, timeTaskState)
                1 -> TimeSelectPage(Modifier, alarmTypePickerState, timePickerState )
                2 -> HeartRatePage(Modifier, heartRateState)
                3 -> HeartRateMeasurementTypePage(Modifier, navigator, heartRateMeasurementTypePickerState, averageMeasurementTimeState, timePickerState)
                else -> PageItem(Modifier)
            }


            /*PageItem(
                page = 1,
                modifier = Modifier

            )*/


        }

        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.BottomCenter)
                .padding(20.dp),
            activeColor = White,
            inactiveColor = MaterialTheme.colors.secondary
        )
    }
}
//}


@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun MainActivityPreview() {
    WearAppTheme {
        WearApp(EmptyDestinationsNavigator)
    }
}


/*
@Preview(
    widthDp = WEAR_PREVIEW_DEVICE_WIDTH_DP,
    heightDp = WEAR_PREVIEW_DEVICE_HEIGHT_DP,
    apiLevel = WEAR_PREVIEW_API_LEVEL,
    uiMode = WEAR_PREVIEW_UI_MODE,
    backgroundColor = WEAR_PREVIEW_BACKGROUND_COLOR_BLACK,
    showBackground = WEAR_PREVIEW_SHOW_BACKGROUND
)
@Composable
fun ScrollingExample() {

    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth().rotaryEventHandler(listState),
        contentPadding = PaddingValues(
            horizontal = 8.dp, vertical = 8.dp
        ),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        state = listState
    ) {
        item {
            CreateTimeTaskButton(Modifier) {
                    EmptyDestinationsNavigator
            }
        }

        val timeTasks24: ArrayList<TimeTask> = TimeTask.createTimeTaskList(2)

        items(timeTasks24) { timeTask ->
            TimeTaskChip(Modifier, timeTask) {}
        }
    }
}*/
