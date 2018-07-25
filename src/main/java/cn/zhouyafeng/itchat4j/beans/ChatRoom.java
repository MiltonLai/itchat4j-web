package cn.zhouyafeng.itchat4j.beans;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class ChatRoom {
    private int limit;
    private String name;
    private String nick;
    private JSONObject roomInfo;
    private Map<String, JSONObject> memberMap;
    private Queue<BaseMsg> msgQueue;
    private long lastUpdated;

    public ChatRoom(String name, int limit) {
        this.name = name;
        this.limit = limit;
        this.msgQueue = new LinkedList<>();
        this.memberMap = new LinkedHashMap<>();
    }

    public ChatRoom(JSONObject roomInfo, int limit) {
        this.roomInfo = roomInfo;
        this.name = roomInfo.getString("UserName");
        this.nick = roomInfo.getString("NickName");
        this.limit = limit;
        this.msgQueue = new LinkedList<>();
        this.memberMap = new LinkedHashMap<>();
        JSONArray memberList = roomInfo.getJSONArray("MemberList");
        rebuildMemberMap(memberList);
        this.lastUpdated = System.currentTimeMillis();
    }

    public void push(BaseMsg msg) {
        // fill the nick
        if (msg.isGroupMsg() && msg.getFromMemberName() != null) {
            JSONObject member = memberMap.get(msg.getFromMemberName());
            if (member != null) {
                if (member.getString("DisplayName").length() > 0) {
                    msg.setFromNick(member.getString("DisplayName"));
                } else {
                    msg.setFromNick(member.getString("NickName"));
                }
            }
        }
        msgQueue.offer(msg);
        if(msgQueue.size() >= limit){
            msgQueue.poll();
        }
    }

    public BaseMsg pop() {
        return msgQueue.poll();
    }

    public Collection<JSONObject> getMembers() {
        return memberMap.values();
    }


    public void setRoomInfo(JSONObject roomInfo) {
        this.roomInfo = roomInfo;
        this.name = roomInfo.getString("UserName");
        this.nick = roomInfo.getString("NickName");
        JSONArray memberList = roomInfo.getJSONArray("MemberList");
        rebuildMemberMap(memberList);
    }

    public String getNick() {
        if (nick == null || nick.length() == 0) {
            String output = "";
            Set<String> set = memberMap.keySet();
            int count = 0;
            for (String key : set) {
                if (count > 4) break;
                output += memberMap.get(key).getString("NickName") + ",";
                count++;
            }
            return output + "...";
        }
        return nick;
    }

    public int getLimit() {return limit;}
    public void setLimit(int limit) {this.limit = limit;}
    public String getName() {return name;}
    public void setName(String name) {this.name = name;}
    public void setNick(String nick) {this.nick = nick;}
    public JSONObject getRoomInfo() {return roomInfo;}
    public Map<String, JSONObject> getMemberMap() {return memberMap;}
    public Queue<BaseMsg> getMsgQueue() {return msgQueue;}
    public long getLastUpdated() {return lastUpdated;}

    private void rebuildMemberMap(JSONArray memberList) {
        if (memberList == null || memberList.size() == 0) {
            return;
        }
        memberMap.clear();
        for (int i = 0; i < memberList.size(); i++) {
            JSONObject member = memberList.getJSONObject(i);
            memberMap.put(member.getString("UserName"), member);
        }
    }
}
