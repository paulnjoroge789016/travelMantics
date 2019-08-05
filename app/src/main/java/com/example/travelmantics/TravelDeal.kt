package com.example.travelmantics

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

/**
 * Created by ${paul} on 8/2/2019 at 10:23 AM.
 */
public class TravelDeal : Parcelable{
    lateinit var town: String
    lateinit var cost: String
    lateinit var description:String
    lateinit var imageUrl: String

    constructor(parcel: Parcel) : this() {
        town = parcel.readString()
        cost = parcel.readString()
        description = parcel.readString()
        imageUrl = parcel.readString()
    }

    constructor(){}
    constructor(town: String, cost: String, description: String, imageUrl: String) {
        this.town = town
        this.cost = cost
        this.description = description
        this.imageUrl = imageUrl
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(town)
        parcel.writeString(cost)
        parcel.writeString(description)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TravelDeal> {
        override fun createFromParcel(parcel: Parcel): TravelDeal {
            return TravelDeal(parcel)
        }

        override fun newArray(size: Int): Array<TravelDeal?> {
            return arrayOfNulls(size)
        }
    }

}