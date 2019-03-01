package com.linyi.reader;

import com.linyi.reader.ui.ReaderActivity;

import org.linyi.base.utils.help.TurnHelp;

/**
 * @fun:
 * @developer: rwz
 * @date: 2019/2/14 0014 下午 6:39
 */
public class ReaderSDK {

    public static void init() {
        TurnHelp.register(ReaderActivity.class);
    }

}
