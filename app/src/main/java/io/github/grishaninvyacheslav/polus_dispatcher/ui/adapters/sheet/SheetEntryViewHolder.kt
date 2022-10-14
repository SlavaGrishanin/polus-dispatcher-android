package io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet

import androidx.recyclerview.widget.RecyclerView
import io.github.grishaninvyacheslav.polus_dispatcher.databinding.ItemSheetEntryBinding

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

    override fun setTitle(title: String) {
        binding.title.text = title
    }
}