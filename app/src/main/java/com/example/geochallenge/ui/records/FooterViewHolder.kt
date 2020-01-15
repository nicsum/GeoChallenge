package com.example.geochallenge.ui.records

import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.geochallenge.R
import com.example.geochallenge.data.records.RecordsDataSource
import kotlinx.android.synthetic.main.li_record_footer.view.*

class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(status: RecordsDataSource.State?) {
        itemView.progress_bar.visibility =
            if (status == RecordsDataSource.State.LOADING) VISIBLE else INVISIBLE
        itemView.errorText.visibility =
            if (status == RecordsDataSource.State.ERROR) VISIBLE else INVISIBLE
    }

    companion object {

        fun create(retry: () -> Unit, parent: ViewGroup): FooterViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.li_record_footer, parent, false)
            view.errorText.setOnClickListener { retry() }
            return FooterViewHolder(view)
        }
    }
}