package com.xupu.locationmap.common.po;

import com.google.gson.reflect.TypeToken;
import com.xupu.locationmap.common.tools.RedisTool;
import com.xupu.locationmap.common.tools.ReflectTool;
import com.xupu.locationmap.common.tools.Tool;
import com.xupu.locationmap.exceptionmanager.MapException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 搜索框的历史记录
 */
public class HistorySearchText {
    /**
     * json 使用的key
     */
    public final static String JSON_TEXT = "text";
    /**
     * 搜索的文本
     */
    private String text;
    /**
     * sousuo 的时间
     */
    private long time;
    /**
     * 这个文本共搜索的次数
     */
    private int count;

    /**
     * 最后一次搜索结果的条目数
     */
    private int resultCount;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(int resultCount) {
        this.resultCount = resultCount;
    }

    public HistorySearchText(String text, int resultCount) {
        this.text = text;
        this.time = System.currentTimeMillis();
        this.count = 0;
        this.resultCount = resultCount;
    }


    private final static String REDIS_MARK = "HistorySearchText";
    private static List<HistorySearchText> all;
    private static Map<String, HistorySearchText> historySearchTextMap;

    public static List<HistorySearchText> getAllLog() {
        synchronized (HistorySearchText.class) {
            if (all == null) {
                try {
                    all = RedisTool.findRedis(REDIS_MARK,new TypeToken<List<HistorySearchText>>(){}.getType());
                    if (all == null) {
                        all = new ArrayList<>();
                    }
                    historySearchTextMap = ReflectTool.getIDMap("getText", all);
                } catch (Exception e) {
                    historySearchTextMap = new HashMap<>();
                    RedisTool.deleteRedisByMark(REDIS_MARK);
                    e.printStackTrace();
                }
            }
        }
        return all;
    }

    /**
     * 查询符合的记录 前20条
     *
     * @param text
     * @return
     */
    public static List<HistorySearchText> searchTexts(String text) {
        List<HistorySearchText> list = new ArrayList<>();
        for (HistorySearchText historySearchText : getAllLog()) {
            if (historySearchText.getText().contains(text)) {
                list.add(historySearchText);
                if (list.size() >= 20) {
                    return list;
                }
            }
        }

        //saveHistory(text,0);
        return list;
    }

    public static void saveHistory(String text, int resultCount) {

        if (Tool.isEmpty(text) || resultCount == 0) {
            return;
        }
        HistorySearchText historySearchText = historySearchTextMap.get(text);
        if (historySearchText != null) {
            all.remove(historySearchText);
            historySearchText.count++;
            all.add(0, historySearchText);
        } else {
            historySearchText = new HistorySearchText(text, resultCount);
            historySearchTextMap.put(text, historySearchText);
            all.add(0, historySearchText);
        }
    }

    /**
     * 记录保存数据库
     */
    public static void saveToRedis() {
        while (all.size() > 50) {
            all.remove(50);
        }
        RedisTool.updateRedis(REDIS_MARK, all);
    }
}
