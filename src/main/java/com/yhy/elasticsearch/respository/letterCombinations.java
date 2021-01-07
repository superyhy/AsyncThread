package com.yhy.elasticsearch.respository;

import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class letterCombinations {

    public static List<String> letterCombinations(String digits) {
        List<String> combinations = new ArrayList<String>();
        if (digits.length() == 0) {
            return combinations;
        }
        Map<Character, String> phoneMap = new HashMap<Character, String>() {{
            put('2', "abc");
            put('3', "def");
            put('4', "ghi");
            put('5', "jkl");
            put('6', "mno");
            put('7', "pqrs");
            put('8', "tuv");
            put('9', "wxyz");
        }};
        backtrack(combinations, phoneMap, digits, 0, new StringBuffer());
        return combinations;
    }

    /**
     *
     * @param combinations 所有组合
     * @param phoneMap  电话键
     * @param digits  数字
     * @param index  数字下标
     * @param combination 字母字符串
     */
    public static void backtrack(List<String> combinations, Map<Character, String> phoneMap, String digits, int index, StringBuffer combination) {
        if (index == digits.length()) {
            //加入结果字符串
            combinations.add(combination.toString());
        } else {

            char digit = digits.charAt(index);
            String letters = phoneMap.get(digit);
            int lettersCount=letters.length();
            for(int i=0;i<lettersCount;i++){
                combination.append(letters.charAt(i));
                backtrack(combinations, phoneMap, digits, index+1, combination);
                combination.deleteCharAt(index);
            }
        }

    }

    public static void main(String[] args) {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append("ab");
        System.out.println(stringBuffer.deleteCharAt(1).toString());
        System.out.println(letterCombinations("23"));
        String imgUrl1="ht";
        String imgUrl="https://qr.encdata.cn/media/group1/M00/37/B0/ChOXjl_hsPKAUl_1AA7x5vqEvuw360_300x300.png";
        System.out.println(imgUrl1.lastIndexOf("."));
    }
}
