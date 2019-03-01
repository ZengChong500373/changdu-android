package org.linyi.viva.ui.adapter;

import com.api.changdu.proto.BookApi;

import java.util.HashMap;
import java.util.Map;

public class RankAdapterFactory {
    private static Map<String, RankListAdapter> map = new HashMap<>();

    public static RankListAdapter getAdapter(BookApi.AckIndexRankingDetail data) {
        String site = data.getSiteName();
        String rank = data.getRankName();
        String key = site + "," + rank;
        RankListAdapter adapter = map.get(key);
        if (adapter == null) {
            map.put(key, creatAdapter());
        }
        return map.get(key);
    }

    private static RankListAdapter creatAdapter() {
        return new RankListAdapter();
    }

    public static RankListAdapter getAdapter(String site, String rank) {
        String key = site + "," + rank;
        return map.get(key);
    }
}
