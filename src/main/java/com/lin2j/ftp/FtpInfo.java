package com.lin2j.ftp;

import lombok.Data;

/**
 * @author linjinjia
 * @date 2021/2/26 11:50
 */
@Data
public class FtpInfo {
    /**
     * Ftp 服务Ip
     */
    private String ip;

    /**
     * Ftp 服务端口
     */
    private Integer port;

    /**
     * Ftp 服务账号
     */
    private String userName;

    /**
     * Ftp 服务密码
     */
    private String password;
}
