package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.AccentViolet
import com.example.ui.theme.DarkCanvas
import com.example.ui.theme.GlassCardBorder
import com.example.ui.theme.GlassCardSurface
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextFaded
import com.example.ui.theme.TextPure

@Composable
fun EditNameDialog(
    currentName: String,
    onSave: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var nameText by remember { mutableStateOf(currentName) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(GlassCardSurface)
                .border(1.dp, GlassCardBorder, RoundedCornerShape(20.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "Edit Callsign",
                    color = TextPure,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Set your personal focus identity",
                    color = TextDim,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp, bottom = 14.dp)
                )

                OutlinedTextField(
                    value = nameText,
                    onValueChange = { if (it.length <= 20) nameText = it },
                    label = { Text("Name", color = TextFaded) },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = TextPure,
                        unfocusedTextColor = TextPure,
                        focusedContainerColor = DarkCanvas,
                        unfocusedContainerColor = DarkCanvas,
                        focusedIndicatorColor = AccentViolet,
                        unfocusedIndicatorColor = GlassCardBorder
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("hero_name_input")
                )

                Spacer(modifier = Modifier.height(18.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Cancel", color = TextDim)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = {
                            if (nameText.isNotBlank()) {
                                onSave(nameText.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AccentViolet,
                            contentColor = Color.White
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("save_hero_name_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = "Save", color = Color.White, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
