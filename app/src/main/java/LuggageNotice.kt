
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.hoxi.R


class LuggageNotice : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout._28_over_luggage_notice, null)

            // 여기서 Dialog를 직접 생성하고 설정
            val dialog = Dialog(it)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // 타이틀바 제거
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명하게
            dialog.window?.setLayout(380, 300) // 너비 400, 높이 300
            val density = resources.displayMetrics.density
            val widthPx = (320 * density).toInt()
            val heightPx = (217 * density).toInt()
            dialog.window?.setLayout(widthPx, heightPx)
            dialog.setContentView(view) // 뷰 설정

            // 이하 코드는 기존과 동일
            val done = view.findViewById<TextView>(R.id.done_btn)

            done.setOnClickListener{
                dialog.dismiss() // dialog를 dismiss 해야함
            }

            dialog // dialog 반환
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onResume() {
        super.onResume()
        val density = resources.displayMetrics.density
        val widthPx = (320 * density).toInt()
        val heightPx = (217 * density).toInt()
        dialog?.window?.setLayout(widthPx, heightPx)
    }
}


