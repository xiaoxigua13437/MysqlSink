package com.yushu.flume.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yushu.zhao
 * @create 2020-12-21 14:28
 */
public class FormatDataUtil {

    private static String arg = "";
    private static String arg1 = ";";
    private static String arg2 = ",";
    private static String arg3 = "=";
    private static String arg4 = "&&";
    private static String arg6 = "##";
    private static String arg7 = "DataTime";
    private static String arg8 = "<>";


    /**
     * 将接收到的数据转为map
     *
     * @param data
     * @return
     */
    public static Map<String, String> getMapByData(String data) {
        System.out.println("data:"+data);
        if (isEmpty(data)) {
            return null;
        }
        Map<String, String> map = new HashMap<String, String>();
        data = data.replaceAll(arg6, "");
        if(isContainsChars(data,arg1)){
            String[] temp = getArrayByChar(data, arg1);
            for (String s : temp) {
                // 如果有多个参数用逗号分割
                if (isContainsChars(s, arg2)) {
                    String[] temp1 = getArrayByChar(s, arg2);
                    for (String s1 : temp1) {
                        intoMap(map, s1);
                    }
                } else {
                    intoMap(map,s);
                }
            }
        }
        return map;
    }

    /**
     * 因为字段中存在特殊的（CP=&&DataTime=20040516020111）格式，不光是以（key=value）格式
     * 所以需要对其进行处理
     * @param map
     * @param var
     */
    public static void intoMap(Map<String, String> map, String var) {
        // 数据中是否存在CP=&&DataTime=20040516020111;格式  如果有按&&分割
        if (isContainsChars(var, arg4)) {
            String[] temp2 = getArrayByChar(var, arg4);
            String[] temp3;
            if(temp2.length == 1){
                temp3 = getArrayByChar(temp2[0], arg3);
            }else{
                temp3 = getArrayByChar(temp2[1], arg3);
            }
            if (temp3.length == 2) {
                if(arg7.equals(temp3[0])){
                    map.put(temp3[0],formatDateStr(temp3[1]));
                }else{
                    map.put(temp3[0], temp3[1]);
                }
            }
        } else {
            // 数据直接是key=value格式
            String[] temp4 = getArrayByChar(var, arg3);
            if (temp4.length == 2) {
                if(arg7.equals(temp4[0])){
                    map.put(temp4[0],formatDateStr(temp4[1]));
                }else{
                    map.put(temp4[0], temp4[1]);
                }
            }
        }
    }

    /**
     * 根据指定字符分割字符串
     *
     * @param datastr
     * @param s
     * @return
     */
    public static String[] getArrayByChar(String datastr, String s) {
        if (isEmpty(datastr) || !isContainsChars(datastr, s)) {
            return null;
        }
        return datastr.trim().replaceAll("\\s*", arg).split(s);
    }

    /**
     * 判断字符串是否包含指定字符串
     *
     * @param str
     * @return
     */
    public static boolean isContainsChars(String str, String arg) {
        if (isEmpty(str) || isEmpty(arg)) {
            return false;
        }
        return str.contains(arg);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || arg.equals(str);
    }



    /**
     * 格式化时间字符串
     * @param datestr
     * @return
     */
    public static String formatDateStr(String datestr){
        if(isEmpty(datestr)){
            return arg;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            Date date = format.parse(datestr);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return datestr;
    }

    /**
     * 新增Base64处理字符串
     *
     * @throws UnsupportedEncodingException
     */
    public static void test() throws UnsupportedEncodingException {


        String base64encodedString = Base64.getEncoder().encodeToString("runoob?java8".getBytes("utf-8"));
        System.out.println("Base64 编码字符串 (基本) :" + base64encodedString);
        // 解码
        byte[] base64decodedBytes = Base64.getDecoder().decode(base64encodedString);
        System.out.println("原始字符串: " + new String(base64decodedBytes, "utf-8"));


        base64encodedString = Base64.getUrlEncoder().encodeToString("runoob?java8".getBytes("utf-8"));
        System.out.println("Base64 编码字符串 (URL) :" + base64encodedString);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; ++i) {
            stringBuilder.append(UUID.randomUUID().toString());
        }

        byte[] mimeBytes = stringBuilder.toString().getBytes("utf-8");
        String mimeEncodedString = Base64.getMimeEncoder().encodeToString(mimeBytes);
        System.out.println("Base64 编码字符串 (MIME) :" + mimeEncodedString);
    }


    /*public static void main(String[] args) throws UnsupportedEncodingException {



        Map <String,String> map = null;

        map = FormatDataUtil.getMapByData("event_id=xxx<>file_path=base64(c:\\xxx;dsdf.txt)<>file_name=xxx<>key_word=base64(xxx;xxx;xxx)<>content_text=base64(xxxxxyyy)<>create_time=1609727577000");

        for (Map.Entry<String,String> entry : map.entrySet()){

            System.out.println(entry.getKey()+":"+entry.getValue()+"\n");

        }


        String str = "c:\\xxx;dsdf.txt";
        String base64encodedString = Base64.getEncoder().encodeToString(str.getBytes("utf-8"));

        String str1 = "event_id=xxx<>file_path="+base64encodedString+"<>";

        String str2 = "event_id=xxx<>file_path=base64(c:\\xxx;dsdf.txt)<>file_name=xxx<>key_word=base64(xxx;xxx;xxx)<>content_text=base64(xxxxxyyy)<>create_time=1609727577000";

        String[] temp = getArrayByChar(str2, arg8);
        System.out.println(temp.length);
        for (String s : temp) {
            // 如果有多个参数用逗号分割
            if (isContainsChars(s, arg2)) {
                String[] temp1 = getArrayByChar(s, arg2);
                for (String s1 : temp1) {
                    System.out.println(s1);
                }
            } else {
                System.out.println(s);
            }
        }



//        FormatDataUtil.test();


    }*/





}