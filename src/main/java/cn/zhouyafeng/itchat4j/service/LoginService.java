package cn.zhouyafeng.itchat4j.service;

/**
 * 登陆服务接口
 * 
 * @author https://github.com/yaphone
 * @date 创建时间：2017年5月13日 上午12:07:21
 * @version 1.0
 *
 */
public interface LoginService {

	/**
	 * 登陆
	 */
	boolean login();

	/**
	 * 获取UUID
	 */
	String getUuid();

	/**
	 * 获取二维码图片
	 */
	boolean getQR(String qrPath);

	/**
	 * 初始化
	 */
	boolean webWxInit();

	/**
	 * 状态通知
	 */
	void wxStatusNotify();

	/**
	 * 接收消息
	 */
	void startReceiving();

	/**
	 * 获取联系人
	 */
	void webWxGetContact();

	/**
	 * 批量获取群聊联系人
	 */
	void WebWxBatchGetContact();

}
