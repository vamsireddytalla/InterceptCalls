package com.example.callidentifier.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.callidentifier.databinding.RecyclerItemBinding
import com.example.callidentifier.pojo.SampleData
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MyAdapter(private val items: List<Triple<String,String,String>>,private val  onItmClick:(Triple<String,String,String>) -> Unit) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    inner class MyViewHolder(binding: RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        lateinit var binding: RecyclerItemBinding

        init {
            this.binding = binding
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerItemBinding.inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.name.text = items.get(position).second
        holder.binding.number.text = items.get(position).first
        holder.binding.date.text = convertTimestampToDateString(items.get(position).third.toLong())
        holder.binding.cv1.setOnClickListener{
            onItmClick(items.get(position))
        }
    }

    fun convertTimestampToDateString(timestamp: Long): String {
        val date = Date(timestamp)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.getDefault())
        return format.format(date)
    }

}

