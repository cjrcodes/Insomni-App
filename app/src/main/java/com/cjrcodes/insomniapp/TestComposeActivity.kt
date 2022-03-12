package com.cjrcodes.insomniapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.cjrcodes.insomniapp.theme.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController
import com.cjrcodes.insomniapp.destinations.ProfileScreenDestination
import com.cjrcodes.insomniapp.models.TimeTask
import com.cjrcodes.insomniapp.models.TimeTask.createTimeTaskList
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


class TestComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            WearAppTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}

@Destination(start = true)
@Composable
@OptIn(ExperimentalWearMaterialApi::class, com.google.accompanist.pager.ExperimentalPagerApi::class)
fun WearApp(
    navigator: DestinationsNavigator
) {
    WearAppTheme {

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


            val listState = rememberScalingLazyListState()
            val coroutineScope = rememberCoroutineScope()

            val scrollState = rememberScrollState()

            ScalingLazyColumn(
                modifier = Modifier
                    //.verticalScroll(scrollState)
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
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Button
                        Button(
                            modifier = Modifier.size(ButtonDefaults.LargeButtonSize),
                            onClick = {navigator.navigate(ProfileScreenDestination)}
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Add,
                                contentDescription = "Create Time Task",
                                modifier = iconModifier
                            )
                        }

                    }

                }

                /*itemsIndexed(items = ScalingLazyColumn.list, itemContent = { index, dataItem ->
                    Text(text = "Hello")// your row
                    if (scrollState.isScrollInProgress) {  LaunchedEffect(true) {
                            coroutineScope.launch {
                                val currentItem = index

                                listState.animateScrollToItem(index = currentItem)
                            }
                        }


                    }
                })*/

                /*items(timeTasks) { timeTask ->
                    Text(timeTask.time.toString())
                    // Text(text = timeTask.time.toString())
                    *//* if (scrollState.isScrollInProgress) {
                         DisposableEffect(Unit) {
                             onDispose {
                                 val currentItem = listState.centerItemIndex

                             }
                         }
                     }*//*


                }*/


            }
        }
    }
}


@Destination
@Composable
fun ProfileScreen(
    navigator: DestinationsNavigator,

    ) {
    Row(
        modifier = Modifier,
        horizontalArrangement = Arrangement.Center)
    {
        Text("Create Time Task Screen")

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
fun MainActivityPreview() {
    WearAppTheme {
        DestinationsNavHost(navGraph = NavGraphs.root)
    }
}