package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.api.changdu.proto.BookApi;

import org.linyi.base.entity.db.HistoryRecordDbEntity;
import org.linyi.base.entity.db.HistoryRecordEntity;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.HistoryHolder;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

public class HistoryRecordAapter extends RecyclerView.Adapter<HistoryHolder> {
    private List<HistoryRecordDbEntity> list = new ArrayList<>();
    private ItemClickListener mListener;

    public static Map<String,HistoryRecordDbEntity> idsMap=new ArrayMap<>();
    public void setItemListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public HistoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_history_adapter_item, parent, false);
        return new HistoryHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull HistoryHolder holder, int position) {
        holder.setItemListener(mListener);
          holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        if (list==null||list.size()==0)
        return 0;
        return list.size();
    }

    public void setData(List<HistoryRecordDbEntity> data){
        if (data == null||data.size()==0) return;
        list.addAll(data);
        notifyDataSetChanged();
    }
//    public HistoryRecordEntity getDataOnPosition(int position){
//        if (position==list.size()){
//            return null;
//        }
//        if (position<0){
//            return null;
//        }
//        HistoryRecordEntity data=list.get(position);
//        putMapData(data);
//        return data;
//    }
//    public List<HistoryRecordEntity> getDataList(){
//        return list;
//    }
    public void removeData(List<HistoryRecordEntity> data){
          list.removeAll(data);
        if (list.size()==0&&mEmptyListener!=null){
            mEmptyListener.onEmpty();
        }
        notifyDataSetChanged();
    }

//    public static void putMapData(HistoryRecordEntity data){
//        idsMap.put(data.getBookId(),data);
//    }
//    public static void removeMapData(BookApi.AckMyBookShelf.Data data){
//        idsMap.remove(data);
//    }
//    public String getIds(){
//       StringBuffer stringBuffer=new StringBuffer();
//        for (Map.Entry<String, HistoryRecordEntity> m : idsMap.entrySet()) {
//            System.out.println("key:" + m.getKey() + " value:" + m.getValue());
//            stringBuffer.append(m.getKey()+",");
//        }
//        return stringBuffer.toString();
//    }

    public interface EmptyListener {
         void onEmpty();
    }
    private EmptyListener mEmptyListener;
    public void setEmptyListener(EmptyListener mListener) {
        this.mEmptyListener = mListener;
    }

}
