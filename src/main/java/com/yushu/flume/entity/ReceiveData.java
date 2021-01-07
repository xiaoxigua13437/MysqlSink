package com.yushu.flume.entity;


/**
 *
 * 终端日志实体类
 *
 * @author yushu
 * @create 2021-01-05 14:09
 */
public class ReceiveData {

    private String event_id;

    private String create_time;

    private String file_name;

    private String file_path;

    private String content_text;

    private String key_word;



    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getContent_text() {
        return content_text;
    }

    public void setContent_text(String content_text) {
        this.content_text = content_text;
    }

    public String getKey_word() {
        return key_word;
    }

    public void setKey_word(String key_word) {
        this.key_word = key_word;
    }


    @Override
    public String toString() {
        return super.toString();
    }
}
