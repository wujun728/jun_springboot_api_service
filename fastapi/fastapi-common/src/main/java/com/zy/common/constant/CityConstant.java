package com.zy.common.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author javachen
 * @description Constants
 */
public class CityConstant
{

    /**
     * 地市map
     */
    public static final Map<String,Integer> CITY_MAP = new HashMap<>();

    /**
     * 地市map(code为key)
     */
    public static final Map<Integer,String> CITY_MAP_CODE = new HashMap<>();

    static {
        CITY_MAP.put("济南市",370100);
        CITY_MAP.put("青岛市",370200);
        CITY_MAP.put("淄博市",370300);
        CITY_MAP.put("枣庄市",370400);
        CITY_MAP.put("东营市",370500);
        CITY_MAP.put("烟台市",370600);
        CITY_MAP.put("潍坊市",370700);
        CITY_MAP.put("济宁市",370800);
        CITY_MAP.put("泰安市",370900);
        CITY_MAP.put("威海市",371000);
        CITY_MAP.put("日照市",371100);
        CITY_MAP.put("临沂市",371300);
        CITY_MAP.put("德州市",371400);
        CITY_MAP.put("聊城市",371500);
        CITY_MAP.put("滨州市",371600);
        CITY_MAP.put("菏泽市",371700);

        CITY_MAP_CODE.put(370100,"济南市");
        CITY_MAP_CODE.put(370200,"青岛市");
        CITY_MAP_CODE.put(370300,"淄博市");
        CITY_MAP_CODE.put(370400,"枣庄市");
        CITY_MAP_CODE.put(370500,"东营市");
        CITY_MAP_CODE.put(370600,"烟台市");
        CITY_MAP_CODE.put(370700,"潍坊市");
        CITY_MAP_CODE.put(370800,"济宁市");
        CITY_MAP_CODE.put(370900,"泰安市");
        CITY_MAP_CODE.put(371000,"威海市");
        CITY_MAP_CODE.put(371100,"日照市");
        CITY_MAP_CODE.put(371300,"临沂市");
        CITY_MAP_CODE.put(371400,"德州市");
        CITY_MAP_CODE.put(371500,"聊城市");
        CITY_MAP_CODE.put(371600,"滨州市");
        CITY_MAP_CODE.put(371700,"菏泽市");
    }
}
