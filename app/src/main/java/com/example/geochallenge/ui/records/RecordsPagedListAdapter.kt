package com.example.geochallenge.ui.records

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.data.records.RecordsDataSource
import com.example.geochallenge.game.Record

class RecordsPagedListAdapter(
    private val retry: () -> Unit,
    private val myRecordId: Int?
) :
    PagedListAdapter<Record, RecyclerView.ViewHolder>(RecordsDiffCallback) {

    companion object {
        const val DATA_VIEW_TYPE = 1
        const val FOOTER_VIEW_TYPE = 2
        val RecordsDiffCallback = object : DiffUtil.ItemCallback<Record>() {
            override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
                return oldItem == newItem
            }
        }
    }


    private var state = RecordsDataSource.State.LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DATA_VIEW_TYPE) RecordViewHolder.create(parent) else FooterViewHolder.create(
            retry,
            parent
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == DATA_VIEW_TYPE)
            (holder as RecordViewHolder).bind(
                getItem(position)!!,
                (getItem(position)!!.id == myRecordId && myRecordId != null)
            )
        else (holder as FooterViewHolder).bind(state)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < super.getItemCount()) DATA_VIEW_TYPE else FOOTER_VIEW_TYPE
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasFooter()) 1 else 0
    }

    private fun hasFooter(): Boolean {
        return super.getItemCount() != 0
                && (state == RecordsDataSource.State.LOADING ||
                state == RecordsDataSource.State.ERROR)
    }

    fun setState(state: RecordsDataSource.State) {
        this.state = state
        notifyItemChanged(super.getItemCount())
    }

//    private var records = ArrayList<Record>()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return RecordViewHolder(inflater.inflate(R.layout.li_record, parent, false))
//    }
//
//    override fun getItemCount(): Int {
//        return records.size
//    }
//
//    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
//        val record = records[position]
//        val isMyRecord = (record.id != null && record.id == gameInfo.recordId)
//        holder.bind(record, position + 1, isMyRecord)
//    }
//
//    fun add(records: List<Record>, refresh: Boolean) {
//        if(refresh){
//            this.records = ArrayList(records.size)
//        }
//        this.records.addAll(records)
//        this.records.sortDescending()
//        notifyDataSetChanged()
//    }
//
//    override fun getItemId(position: Int): Long {
//        return position.toLong()
//    }
//
//    override fun getItemViewType(position: Int): Int {
//        return position
//    }
}