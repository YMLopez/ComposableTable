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
package com.breens.beetablescompose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.breens.beetablescompose.utils.lightColor
import com.breens.beetablescompose.utils.lightGray

@Composable
fun InlineEditableTableRowComponent(
    data: List<String>,
    rowIndex: Int,
    rowBorderColor: Color,
    rowTextStyle: TextStyle,
    rowBackGroundColor: Color,
    contentAlignment: Alignment,
    textAlign: TextAlign,
    tablePadding: Dp,
    columnToIndexIncreaseWidth: Int?,
    dividerThickness: Dp,
    disableVerticalDividers: Boolean,
    horizontalDividerColor: Color,
    onDataChange: ((rowIndex: Int, columnIndex: Int, newValue: String) -> Unit)? = null,
) {
    var editingCell by remember { mutableStateOf<Int?>(null) }
    var editingValue by remember { mutableStateOf(TextFieldValue("")) }
    var hasInitialFocus by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Row(
        Modifier
            .fillMaxWidth()
            .background(rowBackGroundColor)
            .padding(tablePadding),
    ) {
        data.forEachIndexed { columnIndex, cellValue ->
            val weight = if (columnIndex == columnToIndexIncreaseWidth) 8f else 2f
            val isEditing = editingCell == columnIndex

            Box(
                modifier = Modifier
                    .weight(weight)
                    .let { modifier ->
                        if (disableVerticalDividers) {
                            modifier.padding(bottom = dividerThickness)
                        } else {
                            modifier.border(
                                width = dividerThickness,
                                color = rowBorderColor,
                            )
                        }
                    }
                    .clickable {
                        if (!isEditing) {
                            editingCell = columnIndex
                            editingValue = TextFieldValue(
                                text = cellValue,
                                selection = TextRange(cellValue.length),
                            )
                            hasInitialFocus = false
                        }
                    },
                contentAlignment = contentAlignment,
            ) {
                if (isEditing) {
                    BasicTextField(
                        value = editingValue,
                        onValueChange = { editingValue = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp)
                            .wrapContentHeight()
                            .padding(horizontal = 4.dp, vertical = 8.dp)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    hasInitialFocus = true
                                } else if (hasInitialFocus && isEditing) {
                                    onDataChange?.invoke(rowIndex, columnIndex, editingValue.text)
                                    editingCell = null
                                    hasInitialFocus = false
                                }
                            },
                        textStyle = rowTextStyle.copy(
                            textAlign = textAlign,
                            color = MaterialTheme.colorScheme.onSurface,
                        ),
                        singleLine = true,
                        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                        ),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onDataChange?.invoke(rowIndex, columnIndex, editingValue.text)
                                editingCell = null
                                hasInitialFocus = false
                                focusManager.clearFocus()
                            },
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = contentAlignment,
                            ) {
                                innerTextField()
                            }
                        },
                    )

                    LaunchedEffect(columnIndex) {
                        focusRequester.requestFocus()
                    }
                } else {
                    Text(
                        text = cellValue.ifEmpty { "" },
                        style = rowTextStyle,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .height(38.dp)
                            .wrapContentHeight()
                            .padding(end = 8.dp),
                        textAlign = textAlign,
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun InlineEditableTableRowComponentPreview() {
    val data = listOf("Man Utd", "26", "7", "95")

    InlineEditableTableRowComponent(
        data = data,
        rowIndex = 0,
        rowBorderColor = lightGray(),
        rowTextStyle = MaterialTheme.typography.bodySmall,
        rowBackGroundColor = lightColor(),
        contentAlignment = Alignment.Center,
        textAlign = TextAlign.Center,
        tablePadding = 0.dp,
        columnToIndexIncreaseWidth = null,
        dividerThickness = 1.dp,
        disableVerticalDividers = false,
        horizontalDividerColor = lightGray(),
        onDataChange = { _, _, _ -> },
    )
}
