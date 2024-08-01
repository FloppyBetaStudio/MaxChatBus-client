package com.iruanp;


import org.json.simple.JSONObject;

public class MsgTypes {
    public static String GroupMsgFormatter(String sender, String content){
        JSONObject obj = new JSONObject();
        obj.put("sender", sender);
        obj.put("content", content);
        obj.put("type", "text_group");
        return obj.toJSONString();
    }
}
