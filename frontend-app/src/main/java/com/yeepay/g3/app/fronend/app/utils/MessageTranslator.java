package com.yeepay.g3.app.fronend.app.utils;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 16/8/11.
 */
public class MessageTranslator {
    private static Map messages = Collections.EMPTY_MAP;

    public static Map getMessage(String key) {
        Map m = (Map) messages.get(key);
        if (m != null) {
            return m;
        }
        m = FrontEndConfigUtils.getSysConfigMap(key);
        messages.put(key,m);
        return m;
    }
    public Map getMessages() {
        return messages;
    }

    public String getKeyValueStr(String key){
        Map msg=getMessage(key);
        Iterator it=msg.keySet().iterator();
        String keyValus="";
        while(it.hasNext()){
            keyValus+="'"+it.next().toString()+"'"+",";
        }
        return keyValus.substring(0, keyValus.length()-1);
    }

    public String getContent(String key, String entryKey) {
        String content = entryKey;
        Map m = (Map) messages.get(key);
        if (m != null) {
            String message = (String) m.get(entryKey);
            if (message!= null && message .trim().length() > 0)
                content = message;
        }
        return content;
    }
}
