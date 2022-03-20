package com.cjrcodes.insomniapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import com.cjrcodes.insomniapp.core.TimeTaskChip
import com.cjrcodes.insomniapp.theme.*
import com.google.accompanist.pager.PagerState
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.navigation.EmptyDestinationsNavigator
import kotlinx.coroutines.launch

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
) {
    val alarmTypeOptions = arrayListOf<String>("Clock Time", "Elapsed Timer")
    val pickerState = rememberPickerState(initialNumberOfOptions = 2, repeatItems = false)

    val swipeState = rememberSwipeToDismissBoxState()
    val coroutineScope = rememberCoroutineScope()

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
                    key = "MainKey",
                    content = {
                        // Composable that maintains its own state
                        // and can be shown in foreground or background.
                        val checked = rememberSaveable { mutableStateOf(true) }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                            verticalArrangement =
                            Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
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
                                        text = "Alarm Type",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )

                                },
                                secondaryLabel = {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center,

                                        text = alarmTypeOptions[pickerState.selectedOption],
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )


                                },
                                icon = {
                                    Icon(
                                        Icons.Filled.ArrowForwardIos,
                                        contentDescription = "Localized description"
                                    )
                                },
                                onClick = { showMainScreen = false }
                            )
                        }
                    }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    AlarmTypePickerPage(Modifier, pickerState, alarmTypeOptions)

                }
            }

        }
    }

}

@Composable
fun AlarmTypePickerPage(
    modifier: Modifier = Modifier,
    pickerState: PickerState,
    options: List<String>
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
                    coroutineScope.launch { pickerState.scrollToOption(it) }
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
        AlarmTypePickerPage(Modifier, pickerState, alarmTypeOptions)
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
        AlarmTypePage(Modifier)
    }
}


@OptIn(ExperimentalWearMaterialApi::class)
@Composable
fun SwipeToDismiss(Modifier: Modifier) {
    var showMainScreen by remember { mutableStateOf(true) }
    val saveableStateHolder = rememberSaveableStateHolder()

// Swipe gesture dismisses ItemScreen to return to MainScreen.
    val state = rememberSwipeToDismissBoxState()
    LaunchedEffect(state.currentValue) {
        if (state.currentValue == SwipeDismissTarget.Dismissal) {
            state.snapTo(SwipeDismissTarget.Original)
            showMainScreen = !showMainScreen
        }
    }

// Hierarchy is ListScreen -> ItemScreen, so we show ListScreen as the background behind
// the ItemScreen, otherwise there's no background to show.
    SwipeToDismissBox(
        state = state,
        hasBackground = !showMainScreen,
        backgroundKey = if (!showMainScreen) "MainKey" else "Background",
        contentKey = if (showMainScreen) "MainKey" else "ItemKey",
    ) { isBackground ->
        if (isBackground || showMainScreen) {
            // Best practice would be to use State Hoisting and leave this composable stateless.
            // Here, we want to support MainScreen being shown from different destinations
            // (either in the foreground or in the background during swiping) - that can be achieved
            // using SaveableStateHolder and rememberSaveable as shown below.
            saveableStateHolder.SaveableStateProvider(
                key = "MainKey",
                content = {
                    // Composable that maintains its own state
                    // and can be shown in foreground or background.
                    val checked = rememberSaveable { mutableStateOf(true) }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        verticalArrangement =
                        Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
                    ) {
                        SplitToggleChip(
                            checked = checked.value,
                            label = { Text("Item details") },
                            modifier = Modifier.height(40.dp),
                            onCheckedChange = { v -> checked.value = v },
                            onClick = { showMainScreen = false }
                        )
                    }
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.primary),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Show details here...", color = MaterialTheme.colors.onPrimary)
                Text("Swipe right to dismiss", color = MaterialTheme.colors.onPrimary)
            }
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
fun SwipeToDismissPreview() {
    WearAppTheme {
        SwipeToDismiss(Modifier)
    }
}

