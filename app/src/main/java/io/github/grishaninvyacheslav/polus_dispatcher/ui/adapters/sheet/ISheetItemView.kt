package io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet

import com.yandex.runtime.image.ImageProvider

interface ISheetItemView {
    var pos: Int
    fun setLocation(lat: Double, lon: Double, imageProvider: ImageProvider)
    fun setTitle(title: String)
    fun setStartDate(timestamp: Long)
    fun setVehiclesDescription(description: String)
    fun onStart()
    fun onStop()
}