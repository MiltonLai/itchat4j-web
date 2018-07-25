package cn.zhouyafeng.itchat4j.core;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.beans.ChatRoom;
import cn.zhouyafeng.itchat4j.utils.MyHttpClient;
import cn.zhouyafeng.itchat4j.utils.enums.parameters.BaseParaEnum;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 核心存储类，全局只保存一份，单例模式
 * 
 * @author https://github.com/yaphone
 * @date 创建时间：2017年4月23日 下午2:33:56
 * @version 1.0
 *
 */
public class Core {

    private static Core instance;

    private Core() {

    }

    public static Core getInstance() {
        if (instance == null) {
            synchronized (Core.class) {
                instance = new Core();
            }
        }
        return instance;
    }

    private boolean alive = false;
    private int memberCount = 0;

    private String indexUrl;

    private String userName;
    private String nickName;
    private List<BaseMsg> msgList = new ArrayList<BaseMsg>();
    private Map<String , JSONObject> contactMap = new HashMap<>();

    private JSONObject userSelf; // 登陆账号自身信息
    private List<JSONObject> memberList = new ArrayList<JSONObject>(); // 好友+群聊+公众号+特殊账号
    private List<JSONObject> contactList = new ArrayList<JSONObject>();// 好友
    private List<JSONObject> publicUsersList = new ArrayList<JSONObject>();;// 公众号／服务号
    private List<JSONObject> specialUsersList = new ArrayList<JSONObject>();;// 特殊账号

    private Set<String> groupChatRoomNameSet = new LinkedHashSet<>();
    private Map<String, ChatRoom> chatRoomMap = new HashMap<>();

    private Map<String, Object> loginInfo = new HashMap<String, Object>();
    // CloseableHttpClient httpClient = HttpClients.createDefault();
    private MyHttpClient myHttpClient = MyHttpClient.getInstance();
    private String uuid = null;

    private boolean useHotReload = false;
    private String hotReloadDir = "itchat.pkl";
    private int receivingRetryCount = 5;

    private long lastNormalRetcodeTime; // 最后一次收到正常retcode的时间，秒为单位

    /**
     * 请求参数
     */
    public Map<String, Object> getParamMap() {
        return new HashMap<String, Object>(1) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            {
                Map<String, String> map = new HashMap<String, String>();
                for (BaseParaEnum baseRequest : BaseParaEnum.values()) {
                    map.put(baseRequest.para(), getLoginInfo().get(baseRequest.value()).toString());
                }
                put("BaseRequest", map);
            }
        };
    }

    public void reset() {
        this.memberList.clear();
        this.memberCount = 0;
        this.contactList.clear();
        this.contactMap.clear();
        this.publicUsersList.clear();
        this.groupChatRoomNameSet.clear();
        this.chatRoomMap.clear();
        this.specialUsersList.clear();
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public List<JSONObject> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<JSONObject> memberList) {
        this.memberList = memberList;
    }

    public Map<String, Object> getLoginInfo() {
        return loginInfo;
    }

    public void setLoginInfo(Map<String, Object> loginInfo) {
        this.loginInfo = loginInfo;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }

    public boolean isUseHotReload() {
        return useHotReload;
    }

    public void setUseHotReload(boolean useHotReload) {
        this.useHotReload = useHotReload;
    }

    public String getHotReloadDir() {
        return hotReloadDir;
    }

    public void setHotReloadDir(String hotReloadDir) {
        this.hotReloadDir = hotReloadDir;
    }

    public int getReceivingRetryCount() {
        return receivingRetryCount;
    }

    public void setReceivingRetryCount(int receivingRetryCount) {
        this.receivingRetryCount = receivingRetryCount;
    }

    public MyHttpClient getMyHttpClient() {
        return myHttpClient;
    }

    public List<BaseMsg> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<BaseMsg> msgList) {
        this.msgList = msgList;
    }

    public ChatRoom getChatRoom(String name) {
        return chatRoomMap.get(name);
    }

    public Map<String, ChatRoom> getChatRoomMap() {return chatRoomMap;}

    public List<ChatRoom> getGroupChatRoomList() {
        List<ChatRoom> list = new ArrayList<>();
        for (String name : groupChatRoomNameSet) {
            ChatRoom room = chatRoomMap.get(name);
            if (room != null) {
                list.add(room);
            }
        }
        return list;
    }

    public void setMyHttpClient(MyHttpClient myHttpClient) {
        this.myHttpClient = myHttpClient;
    }

    public List<JSONObject> getContactList() {
        return contactList;
    }

    public void setContactList(List<JSONObject> contactList) {
        this.contactList = contactList;
    }

    public Set<String> getGroupChatRoomNameSet() {return groupChatRoomNameSet;}

    public List<JSONObject> getPublicUsersList() {
        return publicUsersList;
    }

    public void setPublicUsersList(List<JSONObject> publicUsersList) {
        this.publicUsersList = publicUsersList;
    }

    public List<JSONObject> getSpecialUsersList() {
        return specialUsersList;
    }

    public void setSpecialUsersList(List<JSONObject> specialUsersList) {
        this.specialUsersList = specialUsersList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public JSONObject getUserSelf() {
        return userSelf;
    }

    public void setUserSelf(JSONObject userSelf) {
        this.userSelf = userSelf;
    }

    public synchronized long getLastNormalRetcodeTime() {
        return lastNormalRetcodeTime;
    }

    public synchronized void setLastNormalRetcodeTime(long lastNormalRetcodeTime) {
        this.lastNormalRetcodeTime = lastNormalRetcodeTime;
    }

    public String getIndexUrl() {
        return indexUrl;
    }

    public void setIndexUrl(String indexUrl) {
        this.indexUrl = indexUrl;
    }

    public Map<String, JSONObject> getContactMap() {
        return contactMap;
    }
}
