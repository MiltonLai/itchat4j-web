package cn.zhouyafeng.itchat4j.utils.enums;

/**
 * 返回结构枚举类
 * <p>
 * Created by xiaoxiaomo on 2017/5/6.
 */
public enum ResultEnum {

    SUCCESS("200", "成功"),
    WAIT_CONFIRM("201", "Please confirm in the phone"),
    EXPIRED("400", "QRCode Expired"),
    WAITING("408", "Waiting scan or confirm");

    private String code;
    private String msg;

    ResultEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {return code;}
    public String getMsg() {return msg;}
}
