package cn.zhouyafeng.itchat4jweb.service;

import cn.zhouyafeng.itchat4j.Constants;
import cn.zhouyafeng.itchat4j.service.impl.DefaultMsgHandler;
import cn.zhouyafeng.itchat4j.beans.ChatRoom;
import cn.zhouyafeng.itchat4j.core.Core;
import cn.zhouyafeng.itchat4j.core.MsgCenter;
import cn.zhouyafeng.itchat4j.service.MsgHandler;
import cn.zhouyafeng.itchat4j.service.LoginService;
import cn.zhouyafeng.itchat4j.service.impl.LoginServiceImpl;
import cn.zhouyafeng.itchat4j.thread.CheckLoginStatusThread;
import cn.zhouyafeng.itchat4j.utils.SleepUtils;
import cn.zhouyafeng.itchat4j.utils.tools.CommonTools;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service("wechatService")
public class WechatService {
    private static Logger LOG = LoggerFactory.getLogger("WechatService");
    private LoginService loginService = new LoginServiceImpl();
    private static Core core = Core.getInstance();
    private MsgHandler msgHandler = new DefaultMsgHandler();

    @PostConstruct
    void init() {
        new Thread(()->{
            while(true) {
                // 检查登录状态
                if (!core.isAlive()) {
                    login(); // 未登录, 应当阻塞在这一步
                    afterLogin(); // 登录后初始化
                }
                if (core.isAlive()) { // 已登陆
                    // 阻塞, 开始接收消息, 直至登录状态失效
                    MsgCenter.handleMsg(msgHandler);
                }
            }
        }).start();
    }

    public boolean isAlive() {
        return core.isAlive();
    }

    public ChatRoom getChatRoom(String name) {
        return core.getChatRoom(name);
    }

    public Map<String, JSONObject> getContactMap() {
        return core.getContactMap();
    }

    public List<JSONObject> getContactList() {
        return core.getContactList();
    }

    public List<ChatRoom> getGroupChatRoomList() {
        return core.getGroupChatRoomList();
    }

    public Map<String, Object> getLoginInfo() {
        return core.getLoginInfo();
    }

    public List<JSONObject> getPublicUsersList() {
        return core.getPublicUsersList();
    }

    private void login() {
        while (true) {
            for (int count = 0; ; count++) {
                while (loginService.getUuid() == null) { // 阻塞直到获得UUID
                    LOG.info("1. Request UUID");
                    while (loginService.getUuid() == null) {
                        LOG.warn("1. Request UUID failed, sleep 2s");
                        SleepUtils.sleep(2000);
                    }
                }
                LOG.info("2. Request QRCode: {}th retry", count);
                if (loginService.getQR(Constants.BASE_PATH)) {
                    break;
                } else {
                    LOG.warn("2. Request QRCode failed, sleep 2s");
                    SleepUtils.sleep(2000);
                }
            }
            LOG.info("3. Waiting scan");
            if (!core.isAlive()) {
                if (loginService.login()) { // 阻塞直到登录
                    core.setAlive(true);
                    LOG.info(("3. Scan&login success"));
                    return;
                }
            }
            LOG.info("4. Timeout，please re-scan");
        }
    }

    private void afterLogin() {

        LOG.info("4. Core reset");
        core.reset();

        LOG.info("5. Wechat initializing");
        if (!loginService.webWxInit()) {
            LOG.info("5. Wechat initializing failed");
            return;
        }

        LOG.info("6. 开启微信状态通知");
        loginService.wxStatusNotify();

        LOG.info("7. 清除。。。。");
        CommonTools.clearScreen();
        LOG.info(String.format("欢迎回来， %s", core.getNickName()));

        LOG.info("9. 获取联系人信息");
        loginService.webWxGetContact();

        LOG.info("10. 获取群好友及群好友列表");
        loginService.WebWxBatchGetContact();

        LOG.info("11. 开始接收消息");
        loginService.startReceiving();

        LOG.info("12.开启微信状态检测线程");
        new CheckLoginStatusThread().start();
    }
}
