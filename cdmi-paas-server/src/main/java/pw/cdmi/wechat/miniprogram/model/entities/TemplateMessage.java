package pw.cdmi.wechat.miniprogram.model.entities;

import java.util.Map;

public class TemplateMessage {
    private String touser; //用户OpenID
    private String template_id; //模板消息ID
    private String page; //点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转
    private String form_id; //表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
    private Map<String, TemplateData> data; //模板详细信息

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getForm_id() {
        return form_id;
    }

    public void setForm_id(String form_id) {
        this.form_id = form_id;
    }

    public Map<String, TemplateData> getData() {
        return data;
    }

    public void setData(Map<String, TemplateData> data) {
        this.data = data;
    }
}
