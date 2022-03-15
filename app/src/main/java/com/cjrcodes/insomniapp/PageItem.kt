package com.cjrcodes.insomniapp

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import com.cjrcodes.insomniapp.core.TimeTaskChip
import com.google.accompanist.pager.PagerState

/**
 * Simple pager item which displays an image
 */
@OptIn(com.google.accompanist.pager.ExperimentalPagerApi::class)
@Composable
internal fun PageItem(
    page: Int,
    modifier: Modifier = Modifier,
) {

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { /*TODO*/ }) {
            Text("hello")
        }
    }
    }

