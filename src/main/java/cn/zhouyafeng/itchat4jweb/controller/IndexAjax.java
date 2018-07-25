package cn.zhouyafeng.itchat4jweb.controller;

import cn.zhouyafeng.itchat4j.Constants;
import cn.zhouyafeng.itchat4j.beans.ChatRoom;
import cn.zhouyafeng.itchat4jweb.service.WechatService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;


@Controller
public class IndexAjax {
    private static final Logger LOG = LoggerFactory.getLogger(IndexAjax.class);

    @Resource(name = "wechatService")
    WechatService wechatService;

    // Ajax methods

    @RequestMapping(value = "isAlive")
    @ResponseBody
    public String isAlive() {
        return JSON.toJSONString(wechatService.isAlive());
    }

    @RequestMapping(value = "conversation_msg")
    @ResponseBody
    public String doConversationMsg(HttpServletRequest request) {
        String name = request.getParameter("name");
        ChatRoom chatRoom = wechatService.getChatRoom(name);
        return JSON.toJSONString(chatRoom.getMsgQueue());
    }

    @RequestMapping("QR")
    @ResponseBody
    public void getQR(HttpServletResponse response) {
        try {
            File filePic = new File(Constants.BASE_PATH + "/QR.jpg");
            if (filePic.exists()) {
                FileInputStream is = new FileInputStream(filePic);
                int i = is.available(); // 得到文件大小
                byte data[] = new byte[i];
                is.read(data); // 读数据
                is.close();
                response.setContentType("image/*"); // 设置返回的文件类型
                OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
                toClient.write(data); // 输出数据
                toClient.close();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @RequestMapping("file")
    @ResponseBody
    public void getQR(HttpServletRequest request, HttpServletResponse response) {
        String path = request.getParameter("path");
        if (path == null || path.length() == 0) {
            return;
        }
        try {
            File file = new File(Constants.BASE_PATH + path);
            if (file.exists()) {
                FileInputStream is = new FileInputStream(file);
                int i = is.available(); // 得到文件大小
                byte data[] = new byte[i];
                is.read(data); // 读数据
                is.close();
                response.setContentType("image/*"); // 设置返回的文件类型
                OutputStream toClient = response.getOutputStream(); // 得到向客户端输出二进制数据的对象
                toClient.write(data); // 输出数据
                toClient.close();
            }
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
