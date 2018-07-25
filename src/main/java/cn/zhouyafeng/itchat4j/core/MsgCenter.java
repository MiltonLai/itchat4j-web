package cn.zhouyafeng.itchat4j.core;

import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.beans.ChatRoom;
import cn.zhouyafeng.itchat4j.service.MsgHandler;
import cn.zhouyafeng.itchat4j.utils.SleepUtils;
import cn.zhouyafeng.itchat4j.utils.enums.MsgCodeEnum;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.CommonTools;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;

/**
 * 消息处理中心
 * 
 * @author https://github.com/yaphone
 * @date 创建时间：2017年5月14日 下午12:47:50
 * @version 1.0
 *
 */
public class MsgCenter {
    private static Logger LOG = LoggerFactory.getLogger(MsgCenter.class);

    private static Core core = Core.getInstance();

    /**
     * 接收消息，放入队列
     * 
     * @author https://github.com/yaphone
     * @date 2017年4月23日 下午2:30:48
     * @param msgList
     * @return
     */
    public static JSONArray produceMsg(JSONArray msgList) {
        JSONArray result = new JSONArray();
        for (int i = 0; i < msgList.size(); i++) {
            JSONObject msg = new JSONObject();
            JSONObject m = msgList.getJSONObject(i);
            m.put("groupMsg", false);// 是否是群消息
            if (m.getString("FromUserName").contains("@@") || m.getString("ToUserName").contains("@@")) { // 群聊消息
                if (m.getString("FromUserName").contains("@@")) {
                    core.getGroupChatRoomNameSet().add((m.getString("FromUserName")));
                } else if (m.getString("ToUserName").contains("@@")) {
                    core.getGroupChatRoomNameSet().add((m.getString("ToUserName")));
                }
                m.put("groupMsg", true);
                // 群消息与普通消息不同的是在其消息体（Content）中会包含发送者id及":<br/>"消息，这里需要处理一下，去掉多余信息，只保留消息内容
                String sep = ":<br/>";
                int pos = m.getString("Content").indexOf(sep);
                if (pos > 0) {
                    String fromMemberName = m.getString("Content").substring(0, pos);
                    m.put("fromMemberName", fromMemberName);
                    String content = m.getString("Content").substring(pos + sep.length());
                    m.put("Content", content);
                } else if (m.getString("FromUserName").equals(core.getUserName())) { // 自己在群聊里发的消息
                    m.put("fromMemberName", m.getString("FromUserName"));
                    m.put("Content", m.getString("Content"));
                }
            }
            if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_TEXT.getCode())) { // 文本消息
                if (m.getString("Url").length() != 0) {
                    String regEx = "(.+?\\(.+?\\))";
                    Matcher matcher = CommonTools.getMatcher(regEx, m.getString("Content"));
                    String data = "Map";
                    if (matcher.find()) {
                        data = matcher.group(1);
                    }
                    msg.put("Type", "Map");
                    msg.put("Text", data);
                } else {
                    msg.put("Type", MsgTypeEnum.TEXT.getCode());
                    msg.put("Text", m.getString("Content"));
                }
                m.put("Type", msg.getString("Type"));
                m.put("Text", msg.getString("Text"));
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_IMAGE.getCode())
                    || m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_EMOTICON.getCode())) { // 图片消息
                m.put("Type", MsgTypeEnum.PIC.getCode());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VOICE.getCode())) { // 语音消息
                m.put("Type", MsgTypeEnum.VOICE.getCode());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VERIFYMSG.getCode())) {// friends
                // 好友确认消息
                // MessageTools.addFriend(core, userName, 3, ticket); // 确认添加好友
                m.put("Type", MsgTypeEnum.VERIFYMSG.getCode());

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_SHARECARD.getCode())) { // 共享名片
                m.put("Type", MsgTypeEnum.NAMECARD.getCode());

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_VIDEO.getCode())
                    || m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_MICROVIDEO.getCode())) {// viedo
                m.put("Type", MsgTypeEnum.VIDEO.getCode());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_MEDIA.getCode())) { // 多媒体消息
                m.put("Type", MsgTypeEnum.MEDIA.getCode());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_STATUSNOTIFY.getCode())) {// phone
                // init
                // 微信初始化消息

            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_SYS.getCode())) {// 系统消息
                m.put("Type", MsgTypeEnum.SYS.getCode());
            } else if (m.getInteger("MsgType").equals(MsgCodeEnum.MSGTYPE_RECALLED.getCode())) { // 撤回消息

            } else {
                LOG.info("Useless msg");
            }
            LOG.info("Msg from:" + m.getString("FromUserName") + ", to:"  + m.getString("ToUserName") + ", " + m.getString("Content"));
            result.add(m);
        }
        return result;
    }

    /**
     * 消息处理
     * 
     * @author https://github.com/yaphone
     * @date 2017年5月14日 上午10:52:34
     * @param msgHandler
     */
    public static void handleMsg(MsgHandler msgHandler) {
        while (core.isAlive()) {
            if (core.getMsgList().size() > 0 && core.getMsgList().get(0).getContent() != null) {
                if (core.getMsgList().get(0).getContent().length() > 0) {
                    BaseMsg msg = core.getMsgList().get(0);
                    if (msg.getType() != null) {
                        // 添加到对应的 chat room
                        if (msg.getFromUserName().equals(core.getUserName())) { // 自己发的消息
                            ChatRoom chatRoom = core.getChatRoom(msg.getToUserName());
                            if (chatRoom == null) {
                                JSONObject toUser = core.getContactMap().get(msg.getToUserName());
                                if (toUser != null) {
                                    chatRoom = new ChatRoom(toUser, 100);
                                } else {
                                    chatRoom = new ChatRoom(msg.getToUserName(), 100);
                                }
                                core.getChatRoomMap().put(msg.getToUserName(), chatRoom);
                            }
                            chatRoom.push(msg);
                        } else {
                            ChatRoom chatRoom = core.getChatRoom(msg.getFromUserName());
                            if (chatRoom == null) {
                                JSONObject fromUser = core.getContactMap().get(msg.getFromUserName());
                                if (fromUser != null) {
                                    chatRoom = new ChatRoom(fromUser, 100);
                                } else {
                                    chatRoom = new ChatRoom(msg.getFromUserName(), 100);
                                }
                                core.getChatRoomMap().put(msg.getFromUserName(), chatRoom);
                            }
                            chatRoom.push(msg);
                        }

                        try {
                            if (msg.getType().equals(MsgTypeEnum.TEXT.getCode())) {
                                String result = msgHandler.textMsgHandle(msg);
                                //MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.PIC.getCode())) {

                                String result = msgHandler.picMsgHandle(msg);
                                //MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.VOICE.getCode())) {
                                String result = msgHandler.voiceMsgHandle(msg);
                                //MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.VIDEO.getCode())) {
                                String result = msgHandler.videoMsgHandle(msg);
                                //MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.NAMECARD.getCode())) {
                                String result = msgHandler.nameCardMsgHandle(msg);
                                //MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.SYS.getCode())) { // 系统消息
                                msgHandler.sysMsgHandle(msg);
                            } else if (msg.getType().equals(MsgTypeEnum.VERIFYMSG.getCode())) { // 确认添加好友消息
                                String result = msgHandler.verifyAddFriendMsgHandle(msg);
                                //MessageTools.sendMsgById(result, core.getMsgList().get(0).getRecommendInfo().getUserName());
                            } else if (msg.getType().equals(MsgTypeEnum.MEDIA.getCode())) { // 多媒体消息
                                String result = msgHandler.mediaMsgHandle(msg);
                                //MessageTools.sendMsgById(result, core.getMsgList().get(0).getFromUserName());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                core.getMsgList().remove(0);
            }
            SleepUtils.sleep(1000);
        }
        LOG.info("handleMsg exited");
    }

}
