package org.linyi.ui.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;



/**
 * Created by jyh on 2016/9/12.
 *
 */
public class RecyclerViewBaseHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    private ItemClickListener mListener=null;

    public RecyclerViewBaseHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onItemClick(view,  getAdapterPosition() );
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (mListener != null) {
            mListener.onItemLongClick(view, getAdapterPosition());
        }
        return true;
    }

    public void setItemListener(ItemClickListener mListener){
        this.mListener = mListener;
    }


    public ItemClickListener getItemListener(){
        return  this.mListener;
    }
}
