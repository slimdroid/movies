package com.slimdroid.movies.presentation.screens.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.slimdroid.movies.theme.AppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchHistoryItem(
    itemValue: String,
    first: Boolean,
    last: Boolean,
    onItemClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable { onItemClick(itemValue) }
            .padding(4.dp),
        shape = RoundedCornerShape(
            topStart = if (first) 36.dp else 0.dp,
            topEnd = if (first) 36.dp else 0.dp,
            bottomStart = if (last) 36.dp else 0.dp,
            bottomEnd = if (last) 36.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.padding(end = 8.dp),
                imageVector = Icons.Outlined.AccessTime,
                contentDescription = null
            )
            Text(
                modifier = Modifier.weight(1f),
                text = itemValue
            )
            IconButton(onClick = { onDeleteClick(itemValue) }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
private fun SearchHistoryItemPreview() {
    AppTheme {
        Surface {
            SearchHistoryItem(
                itemValue = "Example",
                first = true,
                last = false,
                onItemClick = {},
                onDeleteClick = {}
            )
        }
    }
}
