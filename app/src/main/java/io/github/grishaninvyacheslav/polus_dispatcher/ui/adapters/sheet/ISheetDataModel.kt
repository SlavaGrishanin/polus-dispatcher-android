package io.github.grishaninvyacheslav.polus_dispatcher.ui.adapters.sheet

interface ISheetDataModel {
    fun getCount(): Int
    fun bindView(view: ISheetItemView)
}