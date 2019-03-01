package org.linyi.base.entity.db;

import android.support.annotation.IntDef;

import org.linyi.base.constant.SystemConstant;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rwz on 2017/7/19.
 */

@IntDef({SystemConstant.BookDetailType.NONE, SystemConstant.BookDetailType.HISTORY, SystemConstant.BookDetailType.BOOKSHELF})
@Retention(RetentionPolicy.SOURCE)
public @interface BookDetailType {
}