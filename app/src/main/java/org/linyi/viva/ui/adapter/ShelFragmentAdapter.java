package org.linyi.viva.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.api.changdu.proto.BookApi;

import org.linyi.base.utils.SharePreUtil;
import org.linyi.ui.recycler.ItemClickListener;
import org.linyi.ui.recycler.RecyclerViewBaseHolder;
import org.linyi.viva.R;
import org.linyi.viva.ui.viewholder.ShlefViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShelFragmentAdapter extends RecyclerView.Adapter<RecyclerViewBaseHolder> {
    private List<BookApi.AckMyBookShelf.Data> list = new ArrayList<>();
    private ItemClickListener mListener;
    /**
     * 0  一般模式
     * 1  删除模式
     * 2  全选模式
     * */
    public static int commonType=0;
    public static int deleType=1;
    public static  int allSelect=2;
    public  static int showType=0;
    public static Map<String,BookApi.AckMyBookShelf.Data> idsMap=new ArrayMap<>();
    public void setItemListener(ItemClickListener mListener) {
        this.mListener = mListener;
    }
    @NonNull
    @Override
    public RecyclerViewBaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==0){
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_shelf_adapter_item, parent, false);
            return new ShlefViewHolder(view);
        }else {
            View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_shelf_adapter_end_item, parent, false);
            return new RecyclerViewBaseHolder(view);
        }

    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewBaseHolder holder, int position) {
        holder.setItemListener(mListener);
        int viewType = getItemViewType(position);
        switch (viewType){
            case 0:
                BookApi.AckMyBookShelf.Data data=list.get(position);
                ShlefViewHolder shlefViewHolder= (ShlefViewHolder) holder;
                shlefViewHolder.setData(data,showType);
                break;
            case 1:
                break;
        }
    }
    @Override
    public int getItemViewType(int position) {
        if (showType==deleType){
            return 0;
        }
        if (position!=list.size()){
               return 0;
        }
        return 1;
    }
    @Override
    public int getItemCount() {
        if (list==null||list.size()==0)
        return 0;
        if (showType==commonType){
            return list.size()+1;
        }else {
            return list.size();
        }
    }
    public void loadHead(BookApi.AckMyBookShelf data){
        if (data == null||data.getDataList()==null) return;
        for (int i=0;i<data.getDataCount();i++){
            String id=data.getData(i).getBookOverView().getBookID();
            SharePreUtil.removeKey(id);
            SharePreUtil.removeKey(id+"_position");
        }
        list.clear();
        list.addAll(data.getDataList());
        notifyDataSetChanged();
    }
    public void loadMore(BookApi.AckMyBookShelf data){
        if (data == null||data.getDataList()==null) return;
        list.addAll(data.getDataList());
        notifyDataSetChanged();
    }
    public BookApi.AckMyBookShelf.Data getDataOnPosition(int position){
        if (position==list.size()){
            return null;
        }
        if (position<0){
            return null;
        }
        BookApi.AckMyBookShelf.Data data=list.get(position);
        putMapData(data);
        return data;
    }
    public List<BookApi.AckMyBookShelf.Data> getDataList(){
        return list;
    }
    public void removeData(BookApi.AckBookShelf data){
        for (int i=0;i<data.getBookOverViewList().size();i++){
             String id=   data.getBookOverView(i).getBookID();
             Boolean hasBook=   data.getBookOverView(i).getHasBook();
            BookApi.AckMyBookShelf.Data shelf=null;
             if (!hasBook){
                  shelf=  idsMap.get(id);
                  if (shelf!=null){
                      list.remove(shelf);
                  }
             }
        }
        if (list.size()==0&&mEmptyListener!=null){
            mEmptyListener.onEmpty();
        }
        notifyDataSetChanged();
    }
    public void setShowType(int type){
        showType=type;
        if (showType!=commonType){
            idsMap.clear();
        }
        notifyDataSetChanged();
    }
    public static void putMapData(BookApi.AckMyBookShelf.Data data){
        idsMap.put(data.getBookOverView().getBookID(),data);
    }
    public static void removeMapData(BookApi.AckMyBookShelf.Data data){
        idsMap.remove(data);
    }

    public  List<String> getIds(){
        List<String> list=new ArrayList<>();

        for (Map.Entry<String, BookApi.AckMyBookShelf.Data> m : idsMap.entrySet()) {
            list.add(m.getKey());
        }
        return list;
    }

    public interface EmptyListener {
         void onEmpty();
    }
    private EmptyListener mEmptyListener;
    public void setEmptyListener(EmptyListener mListener) {
        this.mEmptyListener = mListener;
    }
    public void topDataChange(BookApi.AckTopBookShelf data){
        String changId=data.getBookID();
        int isTop=data.getTop();
        for (int i=0;i<list.size();i++) {
            BookApi.AckMyBookShelf.Data  shelf=list.get(i);
            if (shelf.getBookOverView().getBookID().equals(changId)){
                if (isTop==2){
                    SharePreUtil.putInt(changId+"_position",i);
                    SharePreUtil.putBoolean(changId,true);
                    list.remove(shelf);
                    list.add(0,shelf);
                    break;
                }else {
                    list.remove(shelf);
                    SharePreUtil.putBoolean(changId,false);
                    list.add(SharePreUtil.getInt(changId+"_position"),shelf);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }
}
