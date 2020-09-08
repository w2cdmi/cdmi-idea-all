package com.example.demo.exception;

public enum ErrorCode
{
    INVALID_PARAMTER("InvalidParameter", "The request parameter is invalid."),
    LOGIN_UNAUTHORIZED("Unauthorized", "Authentication fails, the user name or password is incorrect.");
    
    private String code;
    
    private String message;
    
    private ErrorCode(String code, String message)
    {
        this.code = code;
        this.message = message;
    }
    
    public String getCode()
    {
        return code;
    }
    
    public String getMessage()
    {
        return message;
    }
}
