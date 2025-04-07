package com.bornfire.taskmanagement.utils
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import java.util.Calendar
import com.bornfire.taskmanagement.R

@SuppressLint("DefaultLocale")
@Composable
fun DateTimePickerIcon(
    onDateTimeSelected: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    IconButton(onClick = {
        // Show Date Picker
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                // On Date Picked, show Time Picker
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val amPm = if (hour < 12) "AM" else "PM"
                        val hourFormatted = if (hour % 12 == 0) 12 else hour % 12

                        val selectedDateTime = String.format(
                            "%02d-%02d-%04d %02d:%02d %s",
                            dayOfMonth,
                            month + 1,
                            year,
                            hourFormatted,
                            minute,
                            amPm
                        )

                        onDateTimeSelected(selectedDateTime)

                        Toast.makeText(
                            context,
                            "Selected: $selectedDateTime",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    false // false for 12-hour format (AM/PM)
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.ic_time),
            contentDescription = "Timer",
            tint = Color.White
        )
    }
}
