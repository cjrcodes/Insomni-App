package com.cjrcodes.insomniapp

import android.os.Bundle
import android.text.format.DateFormat
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
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


@Composable
fun MainMenuScreen(
    modifier: Modifier,
    timeTasks: List<TimeTask>,
    navigator: DestinationsNavigator
) {
    val listState = rememberScalingLazyListState()

    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),
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


@OptIn(ExperimentalWearMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
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

        // Display 10 items
        HorizontalPager(

            count = 4,
            state = pagerState,
            // Add 32.dp horizontal padding to 'center' the pages
            modifier = Modifier.fillMaxSize(),
        )
        { page ->

            when (page) {
                0 -> AlarmTypePage(Modifier, navigator)
                1 -> HeartRatePage(Modifier)
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
