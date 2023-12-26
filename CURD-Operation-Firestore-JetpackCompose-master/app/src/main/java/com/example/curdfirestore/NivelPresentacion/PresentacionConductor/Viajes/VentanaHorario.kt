package com.example.curdfirestore.NivelPresentacion.PresentacionConductor.Viajes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController


@Composable
fun CustomNumberPicker(
    currentValue: Int,
    onValueChange: (Int) -> Unit,
    isHourPicker: Boolean = true
) {
    var adjustedValue by remember(currentValue) { mutableStateOf(currentValue) }
    val range = if (isHourPicker) 0 until 24 else 0 until 60
    val extendedRange = (range).toList()

    Box(modifier = Modifier.size(55.dp)) {
        val listState = rememberLazyListState()

        LazyColumn(state = listState) {
            items(extendedRange.size*3) { index ->
                val i = extendedRange[index % extendedRange.size]
                val formattedValue = String.format("%02d", i)
                Text(
                    text = formattedValue,
                    style = if (i == adjustedValue) MaterialTheme.typography.h6.copy(color = Color.Black)
                    else MaterialTheme.typography.h6.copy(color = Color(104, 104, 104)),
                    modifier = Modifier.clickable {
                        adjustedValue = i
                        onValueChange(adjustedValue)
                    }
                )
            }
        }



        // Centra el número seleccionado
        LaunchedEffect(currentValue) {
            listState.animateScrollToItem(adjustedValue)
        }
    }
}


@Composable
fun VentanaHora(
    navController: NavController,
    userId: String,
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var selectedHour by remember { mutableStateOf(12) }
    var selectedMinute by remember { mutableStateOf(0) }
    if (show) {
        Dialog(onDismissRequest = onDismiss) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(top=50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally

                ) {

                    //Spacer(modifier = Modifier.width(100.dp))
                    Row(
                        modifier = Modifier
                            .padding(80.dp,15.dp,15.dp,15.dp)
                            .align(
                                Alignment.CenterHorizontally
                            ),


                        ){

                        CustomNumberPicker(
                            currentValue = selectedHour,
                            onValueChange = { newValue -> selectedHour = newValue },
                            isHourPicker = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        CustomNumberPicker(
                            currentValue = selectedMinute,
                            onValueChange = { newValue -> selectedMinute = newValue },
                            isHourPicker = false
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text ="hrs",
                            style =  MaterialTheme.typography.h6.copy(color = Color.Black)
                        )

                    }


                }


                Spacer(modifier = Modifier.height(16.dp))

                Button(

                    onClick = {
                        // Do something with the selected time
                        val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                        onConfirm(selectedTime)
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(137, 13, 88)),
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Establecer")
                }
            }
        }
    }}

@Composable
fun NumberPicker(
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
    ) {
        items(range.toList()) { number ->
            Box(
                modifier = Modifier
                    .clickable { onValueChange(number) }
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = number.toString(), fontSize = 18.sp)
            }
        }
    }
}
