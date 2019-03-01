package org.linyi.viva.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.api.changdu.proto.BookApi;

import org.linyi.viva.ui.fragment.Classify2ListFragment;


/**
 * Created by Administrator on 2016/9/10 0010.
 *
 */
public class Classify2PagerAdapter extends FragmentStatePagerAdapter {
    private BookApi.AckRankOption data;
    private String category;
    public Classify2PagerAdapter(FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        String title= (String) getPageTitle(position);
        return Classify2ListFragment.newInstance(category,title);
    }

    @Override
    public int getCount() {
        if (data==null)return  0;
       return data.getRankOptionCount();


    }

    @Override
    public CharSequence getPageTitle(int position) {
        return data.getRankOption(position);
    }

    public  void setData(BookApi.AckRankOption data){
       this.data=data;
       notifyDataSetChanged();
   }
   public void setCategory(String category){
        this.category=category;
   }
}
