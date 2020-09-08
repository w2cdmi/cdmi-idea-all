package pw.cdmi.wishlist.model.wechat;

public enum MessageEnum {
    LUCK_USER_MESSAGE(0,"SB7VyKAReVbAkopeaW8eXuXquFTN7ozO6u6VuiTmIOU", "中奖消息"),
    USER_PAYED_SUCCESS(1, "x8shWebI1Kq_Y0Qgvf7OtIgmi1p15sH2W7AUG410A4k","众筹支付成功");

    private int code;
    private String msgNumber;
    private String label;

    MessageEnum(int code, String msgNumber,String label) {
        this.code = code;
        this.msgNumber = msgNumber;
        this.label = label;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsgNumber() {
        return msgNumber;
    }

    public void setMsgNumber(String msgNumber) {
        this.msgNumber = msgNumber;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
