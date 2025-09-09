/*
 * Copyright 2023 Breens Mbaka
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.breens.beetablescompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.breens.beetablescompose.ui.theme.BeeTablesComposeTheme

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BeeTablesComposeTheme {
                TestUI()
            }
        }
    }
}

@Composable
fun TestUI() {
    ScrollingGrid()
}

@Composable
fun ScrollingGrid() {
    val itemsList = (0..15).toList()
    var editingIndex by remember { mutableStateOf(-1) } // -1表示没有编辑，16表示单独item
    var itemTexts by remember { mutableStateOf((0..15).associateWith { "Item is $it" }.toMutableMap()) }
    var singleItemText by remember { mutableStateOf("Single item") }
    val focusManager = LocalFocusManager.current

    val itemModifier = Modifier
        .border(1.dp, Color.Blue)
        .width(80.dp)
        .wrapContentSize()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    editingIndex = -1
                    focusManager.clearFocus()
                }
            },
    ) {
        LazyHorizontalGrid(
            rows = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(itemsList.size) { index ->
                EditableTextItem(
                    text = itemTexts[index] ?: "Item is $index",
                    isEditing = editingIndex == index,
                    modifier = itemModifier,
                    onEditStart = {
                        editingIndex = index
                    },
                    onTextChange = { newText ->
                        itemTexts[index] = newText
                    },
                    onEditEnd = {
                        editingIndex = -1
                    },
                )
            }

            item {
                EditableTextItem(
                    text = singleItemText,
                    isEditing = editingIndex == 16, // 特殊索引16表示单独item
                    modifier = itemModifier,
                    onEditStart = {
                        editingIndex = 16
                    },
                    onTextChange = { newText ->
                        singleItemText = newText
                    },
                    onEditEnd = {
                        editingIndex = -1
                    },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableTextItem(
    text: String,
    isEditing: Boolean,
    modifier: Modifier = Modifier,
    onEditStart: () -> Unit,
    onTextChange: (String) -> Unit,
    onEditEnd: () -> Unit,
) {
    var textFieldValue by remember(text) { mutableStateOf(TextFieldValue(text)) }

    Text(
        text = text,
        modifier = modifier.clickable { onEditStart() },
    )

    if (isEditing) {
        AlertDialog(
            onDismissRequest = { onEditEnd() },
            title = { Text("编辑文本") },
            text = {
                TextField(
                    value = textFieldValue,
                    onValueChange = {
                        textFieldValue = it
                        onTextChange(it.text)
                    },
                    singleLine = true,
                )
            },
            confirmButton = {
                TextButton(
                    onClick = { onEditEnd() },
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        textFieldValue = TextFieldValue(text)
                        onEditEnd()
                    },
                ) {
                    Text("取消")
                }
            },
        )
    }
}
