
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.NumberPicker
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.hoxi.R


interface TimePickerListener {
    fun onTimeSelected(hour: Int, minute: Int, amPm: String)
}

class TimePickerModal(private val listener: TimePickerListener) : DialogFragment() {

    private lateinit var alertDatepickerAmpm: NumberPicker
    private lateinit var alertDatepickerHour: NumberPicker
    private lateinit var alertDatepickerMinute: NumberPicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.activity_time_picker, null)

            // 여기서 Dialog를 직접 생성하고 설정
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게
            dialog.setContentView(view) // 뷰 설정

            // 이하 코드는 기존과 동일
            val done = view.findViewById<TextView>(R.id.period_done_tv)
            val cancel = view.findViewById<TextView>(R.id.cancel_btn)
            alertDatepickerAmpm = view.findViewById(R.id.alert_datepicker_ampm)
            alertDatepickerHour = view.findViewById(R.id.alert_datepicker_hour)
            alertDatepickerMinute = view.findViewById(R.id.alert_datepicker_minute)

            // NumberPicker에 값 설정
            alertDatepickerAmpm.minValue = 0
            alertDatepickerAmpm.maxValue = 1
            alertDatepickerAmpm.displayedValues = arrayOf("AM", "PM")

            alertDatepickerHour.minValue = 0
            alertDatepickerHour.maxValue = 23

            alertDatepickerMinute.minValue = 0
            alertDatepickerMinute.maxValue = 1
            alertDatepickerMinute.displayedValues = arrayOf("0", "30")

            done.setOnClickListener{
                val selectedHour = alertDatepickerHour.value
                val selectedMinute = alertDatepickerMinute.value * 30
                val selectedAmpm = if (alertDatepickerAmpm.value == 0) "AM" else "PM"

                listener.onTimeSelected(selectedHour, selectedMinute, selectedAmpm)
                dialog.dismiss() // dialog를 dismiss 해야함
            }

            cancel.setOnClickListener{
                dialog.dismiss() // dialog를 dismiss 해야함
            }

            dialog // dialog 반환
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}


