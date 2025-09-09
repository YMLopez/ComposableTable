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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.breens.beetablescompose.utils.lightColor
import com.breens.beetablescompose.utils.lightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditableTableRowComponent(
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

    Row(
        Modifier
            .fillMaxWidth()
            .background(rowBackGroundColor)
            .padding(tablePadding),
    ) {
        data.forEachIndexed { columnIndex, cellValue ->
            val weight = if (columnIndex == columnToIndexIncreaseWidth) 8f else 2f

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
                        editingCell = columnIndex
                        editingValue = TextFieldValue(cellValue)
                    },
                contentAlignment = contentAlignment,
            ) {
                Text(
                    text = cellValue,
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

    if (editingCell != null) {
        AlertDialog(
            onDismissRequest = {
                editingCell = null
            },
            title = { Text("编辑单元格") },
            text = {
                TextField(
                    value = editingValue,
                    onValueChange = { editingValue = it },
                    singleLine = true,
                    label = { Text("输入新值") },
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDataChange?.invoke(rowIndex, editingCell!!, editingValue.text)
                        editingCell = null
                    },
                ) {
                    Text("确定")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        editingCell = null
                    },
                ) {
                    Text("取消")
                }
            },
        )
    }
}

@Composable
@Preview(showBackground = true)
fun EditableTableRowComponentPreview() {
    val data = listOf("Man Utd", "26", "7", "95")

    EditableTableRowComponent(
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
