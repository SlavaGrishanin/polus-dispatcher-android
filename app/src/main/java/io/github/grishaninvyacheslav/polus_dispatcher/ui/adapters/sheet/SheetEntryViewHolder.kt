package io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet

import androidx.recyclerview.widget.RecyclerView
import com.yandex.mapkit.Animation
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.runtime.image.ImageProvider
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.ItemSheetEntryBinding
import io.github.grishaninvyacheslav.polus_dispatcher.utils.timestampToDate

class SheetEntryViewHolder(
    private val binding: ItemSheetEntryBinding,
    private var onItemClick: ((view: ISheetItemView) -> Unit)?
) :
    RecyclerView.ViewHolder(binding.root),
    ISheetItemView {
    init {
        itemView.setOnClickListener { onItemClick?.invoke(this) }
        binding.mapViewTouchBlock.setOnClickListener { onItemClick?.invoke(this) }
    }

    override var pos = -1

    override fun setLocation(lat: Double, lon: Double, imageProvider: ImageProvider) {
        binding.mapView.map.mapObjects.addPlacemark(
            Point(lat, lon),
            imageProvider,
            IconStyle().apply {
                scale = 0.2f
            }
        )

        binding.mapView.map.move(
            CameraPosition(Point(lat, lon), 11.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0F),
            null
        )
    }

    override fun setTitle(title: String) {
        binding.title.text = title
    }

    override fun setStartDate(timestamp: Long) {
        binding.date.text = "НАЧАЛО: ${timestamp.timestampToDate()}"
    }

    override fun setVehiclesDescription(description: String) {
        binding.requiredVehicleTitle.text = "ТЕХНИКА: $description"
    }

    override fun onStart() {
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
    }
}