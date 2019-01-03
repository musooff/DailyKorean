package com.dailykorean.app.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.dailykorean.app.R
import com.dailykorean.app.main.discover.model.Expression
import java.util.*

class DatePickerFragment : DialogFragment() {

    private lateinit var repository: DialogRepository

    private lateinit var datePickerResult: DatePickerResult

    interface DatePickerResult {
        fun onFinish(expression: Expression)

        fun onError(date: Date)
    }

    fun setDatePickerResult(datePickerResult: DatePickerResult){
        this.datePickerResult = datePickerResult
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        repository = DialogRepository(context!!)

        val datePickerDialog =  DatePickerDialog(context, R.style.DialogTheme, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            val cal = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            repository.getExpression(cal.time)
                    .subscribe({
                        datePickerResult.onFinish(it)
                        this.dismiss()
                    }, {
                        datePickerResult.onError(cal.time)
                        this.dismiss()
                    })

        }, year, month, day)
        datePickerDialog.datePicker.maxDate = c.timeInMillis
        return datePickerDialog

    }


}