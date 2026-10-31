package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (String) -> Unit
) {

    var taskTitle by remember {
        mutableStateOf("")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Add New Task")
        },

        text = {

            Column {

                OutlinedTextField(

                    value = taskTitle,

                    onValueChange = {
                        taskTitle = it
                    },

                    modifier = Modifier.fillMaxWidth(),

                    label = {
                        Text("Task Title")
                    },

                    singleLine = true,

                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )

                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter the task you want to complete today."
                )

            }

        },

        confirmButton = {

            Button(

                onClick = {

                    if (taskTitle.isNotBlank()) {

                        onAddTask(taskTitle.trim())

                        onDismiss()

                    }

                }

            ) {

                Text("Save")

            }

        },

        dismissButton = {

            TextButton(

                onClick = onDismiss

            ) {

                Text("Cancel")

            }

        }

    )

}