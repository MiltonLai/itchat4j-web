package cn.zhouyafeng.itchat4jweb.controller;

import cn.zhouyafeng.itchat4j.api.WechatTools;
import cn.zhouyafeng.itchat4jweb.service.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhouyong on 7/20/2018.
 */
@Controller
public class IndexController {
    private static final Logger LOG = LoggerFactory.getLogger(IndexController.class);

    @Resource(name = "wechatService")
    WechatService wechatService;

    @RequestMapping(value = "index")
    public ModelAndView doIndex(ModelAndView m) {
        if (!wechatService.isAlive()) {
            m.setViewName("redirect:login.html");
        } else {
            m.setViewName("index");
            m.addObject("contactList", wechatService.getContactList());
            m.addObject("chatRooms", wechatService.getGroupChatRoomList());
            m.addObject("publicUserList", wechatService.getPublicUsersList());
            m.addObject("skey", wechatService.getLoginInfo().get("skey"));
        }
        return m;
    }

    @RequestMapping(value = "login")
    public ModelAndView doLogin(ModelAndView m) {
        if (wechatService.isAlive()) {
            m.setViewName("redirect:index.html");
        } else {
            m.setViewName("login");
        }
        return m;
    }

    @RequestMapping(value = "chat")
    public ModelAndView doChat(ModelAndView m, HttpServletRequest request) {
        if (!wechatService.isAlive()) {
            m.setViewName("redirect:login.html");
            return m;
        } else {
            String group = request.getParameter("group");
            String name = request.getParameter("name");
            m.setViewName("chat");
            m.addObject("fromUserName", name);
            m.addObject("fromUser", wechatService.getContactMap().get(name));
            m.addObject("skey", wechatService.getLoginInfo().get("skey"));
            return m;
        }
    }

    @RequestMapping(value = "logout")
    public String doLogout() {
        WechatTools.logout();
        return "index";
    }
}
