package com.lin2j.ftp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;

/**
 * @author linjinjia
 * @date 2021/2/26 16:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FtpClientFactory extends BasePooledObjectFactory<FTPClient> {

    /**
     * ftp 的信息，服务器ip、端口，账号密码等等
     */
    private FtpInfo ftpInfo;

    /**
     * ftp 连接的配置信息
     */
    private FtpClientConfig clientConfig;

    /**
     * 构造函数
     *
     * @param ftpInfo         ftp 信息
     * @param clientConfig ftp 连接配置
     */
    public FtpClientFactory(FtpInfo ftpInfo, FtpClientConfig clientConfig) {
        super();
        this.ftpInfo = ftpInfo;
        this.clientConfig = clientConfig;
    }


    @Override
    public FTPClient create() throws Exception {
        FTPClient client = new FTPClient();
        // 连接，测试连接
        client.setConnectTimeout(clientConfig.getTimeout());
        client.setDataTimeout(clientConfig.getTimeout());
        client.connect(ftpInfo.getIp(), ftpInfo.getPort());
        int reply = client.getReplyCode();
        if(!FTPReply.isPositiveCompletion(reply)) {
            client.disconnect();
            String error = String.format("ftp server refused the connection: reply=%s, ip=%s, port=%s",
                    reply, ftpInfo.getIp(), ftpInfo.getPort());
            throw new RuntimeException(error);
        }

        // 登录，设置传输参数
        boolean result = client.login(ftpInfo.getUserName(), ftpInfo.getPassword());
        if (!result){
            String error = String.format("failed to login ftp server: username=%s, password=%s",
                    ftpInfo.getUserName(), ftpInfo.getPassword());
            throw new RuntimeException(error);
        }
        client.setFileType(clientConfig.getFileType());
        // 文件传输模式
        client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);


        return null;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        return null;
    }
}
