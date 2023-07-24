package com.example.multicoverinsurance.adapters


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.multicoverinsurance.ClaimsActivity
import com.example.multicoverinsurance.PolicyDetailsActivity
import com.example.multicoverinsurance.databinding.ItemClaimLayoutBinding
import com.example.multicoverinsurance.model.Claims
import com.example.multicoverinsurance.model.Insurance


class ClaimAdapter(
    private var list: MutableList<Claims>,
    private val context: Context,
    private val calling: String
) : RecyclerView.Adapter<ClaimAdapter.ViewHolder>()  {
    class ViewHolder(binding: ItemClaimLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        val name : TextView = binding.claimPolicyName
        val reason : TextView = binding.claimPolicyReason
        val date : TextView = binding.itemClaimDate
        val status : TextView = binding.itemCalimStatus
        val card : CardView = binding.policyCard
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = ItemClaimLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        val vh = ViewHolder(inflatedView)
        vh.card.setOnClickListener {
            val policy = list[vh.adapterPosition]
            val intent = Intent(context, PolicyDetailsActivity::class.java)
            intent.putExtra("policyName", policy)
            context.startActivity(intent)
        }


        return  vh
    }

    override fun onBindViewHolder(holder:  ViewHolder, position: Int) {
        holder.name.text = list[position].name
        holder.reason.text = list[position].reason
        holder.status.text = list[position].status
        holder.date.text = list[position].date
        /*Glide.with(context)
            .load(list[position].image)
            .into(holder.image)*/
    }


    override fun getItemCount(): Int {
        return list.size
    }
}