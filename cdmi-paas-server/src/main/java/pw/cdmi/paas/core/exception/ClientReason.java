package pw.cdmi.paas.core.exception;

import org.springframework.http.HttpStatus;

import pw.cdmi.error.ErrorReason;

public enum ClientReason implements ErrorReason{

    NotPaaSManager(HttpStatus.UNAUTHORIZED, 1001, "当前访问者不是平台管理员"),
    NotDeveloper(HttpStatus.UNAUTHORIZED, 1002, "当前访问者没有开发者账号"),
    InvalidParameter(HttpStatus.BAD_REQUEST, 1003, "未按照接口要求正确传入参数");
    
    private final int code;
    private final String reason;
    private final HttpStatus status;
    
    private ClientReason(HttpStatus status, int code, String reason){
        this.code = code;
        this.reason = reason;
        this.status = status;
    }

    
    public String getReason() {
        return this.reason;
    }


    @Override
    public int getHttpStatus() {
        return this.status.value();
    }


    @Override
    public int getCode() {
        return this.code;
    }
}
