package com.example.zero_fa

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zero_fa.databinding.ListItemBinding
import com.example.zero_fa.modules.Totp
import kotlin.collections.ArrayList
import kotlin.math.ceil

class RvAdapter() :
    RecyclerView.Adapter<RvAdapter.PassHolder>() {
    private val passList = ArrayList<Totp>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)

        return PassHolder(itemView)
    }

    override fun onBindViewHolder(holder: PassHolder, position: Int) {
        holder.bind(passList[position])
    }

    override fun getItemCount(): Int {
        return passList.size
    }


    @SuppressLint("NotifyDataSetChanged")
    fun updateAllPasswords(totps: ArrayList<Totp>) {
        passList.clear()
        passList.addAll(totps)
        notifyDataSetChanged()
    }


    class PassHolder(item: View) : RecyclerView.ViewHolder(item) {

        val binding = ListItemBinding.bind(item)

        fun bind(totp: Totp) = with(binding) {
            passTitle.text = totp.title
            passValue.text = totp.value
            progressBar.progress = countProgress(totp.timeLeft)

        }

        private fun countProgress(time: Int): Int {
            val step = 3.3
            return ceil(time * step).toInt()
        }

    }
}