package org.linyi.base.utils.help;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import org.linyi.base.R;
import org.linyi.base.inf.CommonObserver;
import org.linyi.base.utils.UIUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;

/**
 * Created by rwz on 2018/7/26.
 */

public class SnackBarHelp {

    public static void showSnackBar(View view, @StringRes int text) {
        if(text != 0)
            showSnackBar(view, UIUtils.getString(text));
    }

    public static void showSnackBar(final View view, final String text) {
        if(view == null || TextUtils.isEmpty(text))
            return;
        Observable.just(view)
                .filter(new Predicate<View>() {
                    @Override
                    public boolean test(View view) throws Exception {
                        return view != null && !TextUtils.isEmpty(text);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CommonObserver<View>() {
                    @Override
                    public void onNext(View value) {
                        Snackbar.make(value, text, Snackbar.LENGTH_SHORT)
                                .setAction(R.string.has_know, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    }
                                }).show();
                    }
                });
    }


}
