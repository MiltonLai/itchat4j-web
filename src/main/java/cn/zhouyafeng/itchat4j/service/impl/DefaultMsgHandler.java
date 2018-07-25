package cn.zhouyafeng.itchat4j.service.impl;

import cn.zhouyafeng.itchat4j.Constants;
import cn.zhouyafeng.itchat4j.api.MessageTools;
import cn.zhouyafeng.itchat4j.beans.BaseMsg;
import cn.zhouyafeng.itchat4j.beans.RecommendInfo;
import cn.zhouyafeng.itchat4j.service.MsgHandler;
import cn.zhouyafeng.itchat4j.utils.enums.MsgTypeEnum;
import cn.zhouyafeng.itchat4j.utils.tools.CommonTools;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 简单示例程序，收到文本信息自动回复原信息，收到图片、语音、小视频后根据路径自动保存
 * 
 * @author https://github.com/yaphone
 * @date 创建时间：2017年4月25日 上午12:18:09
 * @version 1.0
 *
 */
public class DefaultMsgHandler implements MsgHandler {
    Logger LOG = Logger.getLogger(DefaultMsgHandler.class);

    @Override
    public String textMsgHandle(BaseMsg msg) {
        // String docFilePath = "D:/itchat4j/pic/1.jpg"; // 这里是需要发送的文件的路径
        if (!msg.isGroupMsg()) { // 群消息不处理
            // String userId = msg.getString("FromUserName");
            // MessageTools.sendFileMsgByUserId(userId, docFilePath); // 发送文件
            // MessageTools.sendPicMsgByUserId(userId, docFilePath);
            String text = msg.getText(); // 发送文本消息，也可调用MessageTools.sendFileMsgByUserId(userId,text);
            LOG.info(text);
            return text;
        }
        return null;
    }

    @Override
    public String picMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());// 这里使用收到图片的时间作为文件名
        String picPath = File.separator + MsgTypeEnum.PIC.getCode() + File.separator + fileName + ".jpg"; // 调用此方法来保存图片
        if (CommonTools.getDownloadFn(msg, MsgTypeEnum.PIC.getCode(), Constants.BASE_PATH + picPath)) {
            msg.setFilePath(picPath);
            return "图片保存成功";
        } else {
            return "图片保存失败";
        }
    }

    @Override
    public String voiceMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String voicePath = File.separator + MsgTypeEnum.VOICE.getCode() + File.separator + fileName + ".mp3";
        if (CommonTools.getDownloadFn(msg, MsgTypeEnum.VOICE.getCode(), Constants.BASE_PATH + voicePath)) {
            msg.setFilePath(voicePath);
            return "声音保存成功";
        } else {
            return "声音保存失败";
        }
    }

    @Override
    public String videoMsgHandle(BaseMsg msg) {
        String fileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String videoPath = File.separator + MsgTypeEnum.VIDEO.getCode() + File.separator + fileName + ".mp4";
        if (CommonTools.getDownloadFn(msg, MsgTypeEnum.VIDEO.getCode(), Constants.BASE_PATH + videoPath)) {
            msg.setFilePath(videoPath);
            return "视频保存成功";
        } else {
            return "视频保存失败";
        }
    }

    @Override
    public String mediaMsgHandle(BaseMsg msg) {
        String fileName = msg.getFileName();
        String filePath = File.separator + MsgTypeEnum.MEDIA.getCode() + File.separator + fileName; // 这里是需要保存收到的文件路径，文件可以是任何格式如PDF，WORD，EXCEL等。
        if (CommonTools.getDownloadFn(msg, MsgTypeEnum.MEDIA.getCode(), Constants.BASE_PATH + filePath)) {
            msg.setFilePath(filePath);
            return "文件" + fileName + "保存成功";
        } else {
            return "文件" + fileName + "保存失败";
        }
    }

    @Override
    public String nameCardMsgHandle(BaseMsg msg) {
        return "收到名片消息";
    }

    @Override
    public void sysMsgHandle(BaseMsg msg) { // 收到系统消息
        String text = msg.getContent();
        LOG.info(text);
    }

    @Override
    public String verifyAddFriendMsgHandle(BaseMsg msg) {
        MessageTools.addFriend(msg, true); // 同意好友请求，false为不接受好友请求
        RecommendInfo recommendInfo = msg.getRecommendInfo();
        String nickName = recommendInfo.getNickName();
        String province = recommendInfo.getProvince();
        String city = recommendInfo.getCity();
        return "你好，来自" + province + city + "的" + nickName + "， 欢迎添加我为好友！";
    }

}
