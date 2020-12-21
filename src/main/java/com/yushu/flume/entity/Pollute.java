package com.yushu.flume.entity;

import java.util.Date;

/**
 * 自定义实体
 *
 * @author yushu.zhao
 * @create 2020-12-21 14:15
 */
public class Pollute {
    private String key;
    private String value;
    private Date dateAdd;

    public Date getDateAdd() {
        return dateAdd;
    }

    public void setDateAdd(Date dateAdd) {
        this.dateAdd = dateAdd;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}