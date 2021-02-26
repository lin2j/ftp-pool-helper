package com.lin2j.ftp;

import lombok.Data;

/**
 * @author linjinjia
 * @date 2021/2/26 16:14
 */
@Data
public class FtpClientConfig {

    /**
     * 连接超时时间，数据连接超时时间
     */
    private Integer timeout;

    /**
     * 主动模式，为 false 表示被动模式
     */
    private Boolean localActiveMode;

    /**
     * 文件传输模式：
     * <li>二进制传输 {@link org.apache.commons.net.ftp.FTP#BINARY_FILE_TYPE}</li>
     * <li>ASCII 传输 {@link org.apache.commons.net.ftp.FTP#ASCII_FILE_TYPE}</li>
     * <p>
     *     一般来讲，二进制传输模式比ASCII的快。二进制模式可以传输所有的ASCII值
     *     图片和执行文件必须用二进制模式，脚本和普通的文本文件可以用ASCII模式。
     * </p>
     */
    private Integer fileType;

    /**
     * ftp 连接池的核心连接数
     */
    private Integer corePoolSize;

    /**
     * ftp 连接池的最大连接数
     */
    private Integer maxPoolSize;

    /**
     * 向 ftp 连接池取连接时，测试一下连接是否可用
     */
    private Boolean testOnBorrow;

    /**
     * 定时测试空闲连接是否可用
     */
    private Boolean testWhileIdle;

    /**
     * 连接使用完之后，返回 ftp 连接池时，测试一下连接是否可用
     */
    private Boolean testOnReturn;

    /**
     * 间隔多少毫秒进行检测，检测那些空闲连接需要关闭
     */
    private Long timeBetweenEvictionRuns;

    /**
     * 空闲连接的最小生存时间，单位：毫秒
     */
    private Long minEvictableIdleTimes;

    /**
     * 获取空闲连接的等待超时时间
     */
    private Integer timeoutForWaitingIdleObject;
}
