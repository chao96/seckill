package com.seckill.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 邮件dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto implements Serializable {
    //邮件主题
    private String subject;
    //邮件内容
    private String content;
    //接收人
    private String[] tos;
}