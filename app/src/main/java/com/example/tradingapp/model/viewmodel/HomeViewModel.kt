package com.example.tradingapp.model.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tradingapp.model.data.Address
import com.example.tradingapp.model.data.PreviewItemData

class HomeViewModel : ViewModel() {
    fun GetItemList() : List<PreviewItemData>{
        val result : MutableList<PreviewItemData> = mutableListOf()

        for (idx : Int in 1..20){
            result.add(
                PreviewItemData(
                    id = idx,
                    productImage = "test",
                    type = "type",
                    title = "title ${idx}",
                    modelYear = null,
                    mileage = null,
                    postingTime = "postingTime",
                    price = "price",
                    chattingCount = "0",
                    favoritesCount = "0",
                    address = Address(
                        city = null,
                        state = null,
                        town = null,
                        townShip = null,
                        village = null
                    )
                )
            )
        }

        return result.toList()
    }
}