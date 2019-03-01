package org.linyi.viva.ui;

import android.support.v4.app.Fragment;
import android.util.SparseArray;
import com.linyi.viva.extra.ui.fragment.MineFragment;
import org.linyi.viva.ui.fragment.BookShelfFragment;
import org.linyi.viva.ui.fragment.BookStackFragment;


/**
 * Created by Administrator on 2017/4/13.
 */

public class MainFragmentFactory {
  private  static SparseArray<Fragment> map=new SparseArray<Fragment>();
    public static Fragment getFragment(int posistion){
        Fragment currentFragment=map.get(posistion) ;
          if (currentFragment==null){
              switch (posistion){
                  case 0:
                    map.put(0,new BookShelfFragment());
                      break;
                  case 1:
                      map.put(1,new BookStackFragment());
                      break;
                  case 2:
                      map.put(2,new MineFragment());
                      break;

                  default:
                      break;
              }
              currentFragment=map.get(posistion);
          }
        return currentFragment;
    }
}
