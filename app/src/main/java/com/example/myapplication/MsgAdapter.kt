import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.*
import com.example.myapplication.R
import com.google.firebase.auth.FirebaseAuth
import java.util.*


class MsgAdapter(val context: Context, val msgList: ArrayList<Message>) : RecyclerView.Adapter<MsgAdapter.MsgRecyclerView>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgRecyclerView {
        val view: View = LayoutInflater.from(context).inflate(R.layout.signed_layout, parent, false)
        return MsgRecyclerView(view)
    }

    override fun onBindViewHolder(holder: MsgRecyclerView, position: Int) {
        val currentMsg = msgList[position]
        holder.textMsg.text = currentMsg.message
        holder.itemView.setOnClickListener{
            if (currentMsg.signature == true) {
                VerifySignature(currentMsg)
            } else Toast.makeText(context, "Это сообщение", Toast.LENGTH_SHORT).show()
            }
    }

    override fun getItemCount(): Int {
        return msgList.size
    }

    private fun VerifySignature(signedMsg : Message){
        if (signedMsg.senderId != FirebaseAuth.getInstance().currentUser?.uid) {
            val intent = Intent(context, Comunication::class.java)
            intent.putExtra("senderId", signedMsg.senderId)
            intent.putExtra("message", signedMsg.m)
            context.startActivity(intent)
        }else Toast.makeText(context, "Это ваша подпись", Toast.LENGTH_SHORT).show()
    }



    class MsgRecyclerView(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textMsg = itemView.findViewById<TextView>(R.id.message)
    }
}