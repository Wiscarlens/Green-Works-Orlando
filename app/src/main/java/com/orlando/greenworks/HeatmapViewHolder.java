package com.orlando.greenworks;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HeatmapViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public final TextView dayOfMonth;
    protected LinearLayout calendarCellLL;
    private final HeatmapAdapter.OnItemListener onItemListener;
    public HeatmapViewHolder(@NonNull View itemView, HeatmapAdapter.OnItemListener onItemListener)
    {
        super(itemView);

        calendarCellLL = itemView.findViewById(R.id.calendarCellLayout);
        dayOfMonth = itemView.findViewById(R.id.cellDayText);

        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        onItemListener.onItemClick(getAdapterPosition(), (String) dayOfMonth.getText());
    }
}
