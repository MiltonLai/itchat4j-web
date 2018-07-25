package cn.zhouyafeng.itchat4j.beans;

public class ChatMsg {
    /** 消息发送者ID **/
    private String fromUserName;
    /** 消息发送者ID **/
    private String fromMemberName;
    /** 文本消息内容 **/
    private String text;
    private String url;
    /** 消息类型 **/
    private int msgType;
    /** 文本消息内容 **/
    private String filePath;
    private int imgWidth;
    private int imgHeight;

    public ChatMsg(
            String fromUserName,
            String fromMemberName,
            String text,
            String url,
            int msgType,
            String filePath, int imgWidth, int imgHeight) {
        this.fromUserName = fromUserName;
        this.fromMemberName = fromMemberName;
        this.text = text;
        this.url = url;
        this.msgType = msgType;
        this.filePath = filePath;
        this.imgWidth = imgWidth;
        this.imgHeight = imgHeight;
    }

    public String getFromUserName() {return fromUserName;}
    public void setFromUserName(String fromUserName) {this.fromUserName = fromUserName;}
    public String getFromMemberName() {return fromMemberName;}
    public void setFromMemberName(String fromMemberName) {this.fromMemberName = fromMemberName;}
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}
    public String getUrl() {return url;}
    public void setUrl(String url) {this.url = url;}
    public int getMsgType() {return msgType;}
    public void setMsgType(int msgType) {this.msgType = msgType;}
    public String getFilePath() {return filePath;}
    public void setFilePath(String filePath) {this.filePath = filePath;}
    public int getImgWidth() {return imgWidth;}
    public void setImgWidth(int imgWidth) {this.imgWidth = imgWidth;}
    public int getImgHeight() {return imgHeight;}
    public void setImgHeight(int imgHeight) {this.imgHeight = imgHeight;}
}
