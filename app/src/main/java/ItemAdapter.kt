
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hoxi.R

// ItemAdapter.java
class ItemAdapter(private val items: List<String>?) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items!![position]
        holder.itemTextView.text = item
        holder.itemView.setOnClickListener {
            // 아이템 클릭시 처리할 내용을 여기에 작성합니다.
        }
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemTextView: TextView = itemView.findViewById<TextView>(R.id.item)
    }
}