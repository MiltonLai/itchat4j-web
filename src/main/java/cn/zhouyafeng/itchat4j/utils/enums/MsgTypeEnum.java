package cn.zhouyafeng.itchat4j.utils.enums;


/**
 * 消息类型枚举类
 * 
 * @author https://github.com/yaphone
 * @date 创建时间：2017年5月13日 下午11:53:00
 * @version 1.0
 *
 */
public enum MsgTypeEnum {
	TEXT("txt",		"文本消息"),
	PIC("pic",		"图片消息"),
	VOICE("voice",	"语音消息"),
	VIDEO("video",	"小视频消息"),
	NAMECARD("namecard",	"名片消息"),
	SYS("sys",		"系统消息"),
	VERIFYMSG("verifymsg",	"添加好友"),
	MEDIA("media",	"多媒体消息");

	private String code;
	private String name;

	MsgTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {return code;}
	public String getName() {return name;}
}
