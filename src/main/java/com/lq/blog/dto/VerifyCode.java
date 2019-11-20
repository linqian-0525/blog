package com.lq.blog.dto;

import lombok.Data;

@Data
public class VerifyCode {
    private String code;
    private byte[] imgBytes;
    private long expireTime;
}
