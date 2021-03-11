package com.wys.offer.other;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wangyasheng
 * @date 2021-03-11
 * @describe:剑指 Offer 50. 第一个只出现一次的字符
 */
public class Offer50_firstUniqChar {
    public char firstUniqChar(String s) {
        Map<Character, Boolean> dic = new LinkedHashMap<>();
        char[] sc = s.toCharArray();
        for(char c : sc)
            dic.put(c, !dic.containsKey(c));
        for(Map.Entry<Character, Boolean> d : dic.entrySet()){
            if(d.getValue()) return d.getKey();
        }
        return ' ';

    }
}
