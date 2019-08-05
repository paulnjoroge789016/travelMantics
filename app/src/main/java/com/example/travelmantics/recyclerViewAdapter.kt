package com.example.travelmantics

import android.content.Context
import android.content.Intent
import android.support.annotation.NonNull
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_admin.*

/**
 * Created by ${paul} on 8/2/2019 at 11:26 AM.
 */
class recyclerViewAdapter: RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {
    var travelDeals: ArrayList<TravelDeal>
    var context: Context
    lateinit var travelDeal: TravelDeal
    constructor(travelDeals: ArrayList<TravelDeal>, context: Context) : super() {
        this.travelDeals = travelDeals
        this.context = context
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view =  LayoutInflater.from(context).inflate(R.layout.single_item, p0, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return travelDeals.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.costTxtView.text = travelDeals[p1].cost
        p0.descTxtView.text = travelDeals[p1].description
        p0.townTxtView.text = travelDeals[p1].town
        Picasso.get().load(travelDeals[p1].imageUrl).into(p0.imageView)
        p0.mCurrentPosition = p1

    }

        inner class ViewHolder(@NonNull itemView:View):RecyclerView.ViewHolder(itemView) {
             val costTxtView: TextView
             val townTxtView: TextView
             val descTxtView: TextView
                val imageView: ImageView
            var mCurrentPosition:Int = 0
            init{
                costTxtView = itemView.findViewById(R.id.cost)
                townTxtView = itemView.findViewById(R.id.town)
                descTxtView = itemView.findViewById(R.id.description)
                imageView = itemView.findViewById(R.id.displayImageView)
                val onClickListener = object: View.OnClickListener {
                    override fun onClick(v:View) {
                        val intent = Intent(context.getApplicationContext(), AdminActivity::class.java)
                        val deal = travelDeals[mCurrentPosition]
                        intent.putExtra("deal", deal)
                        context.startActivity(intent)
                        Toast.makeText(context, "position Clicked $mCurrentPosition", Toast.LENGTH_SHORT).show()
                    }
                }
                itemView.setOnClickListener(onClickListener)
            }

    }
}