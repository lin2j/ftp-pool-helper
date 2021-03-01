package com.lin2j.ftp;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

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
     * @param ftpInfo      ftp 信息
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
        try {
            // 连接，测试连接
            client.setConnectTimeout(clientConfig.getTimeout());
            client.setDataTimeout(clientConfig.getTimeout());
            client.connect(ftpInfo.getIp(), ftpInfo.getPort());
            int reply = client.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                client.disconnect();
                String error = String.format("ftp server refused the connection: reply=%s, ip=%s, port=%s",
                        reply, ftpInfo.getIp(), ftpInfo.getPort());
                throw new RuntimeException(error);
            }

            // 登录，设置传输参数
            boolean result = client.login(ftpInfo.getUserName(), ftpInfo.getPassword());
            if (!result) {
                String error = String.format("failed to login ftp server: username=%s, password=%s",
                        ftpInfo.getUserName(), ftpInfo.getPassword());
                throw new RuntimeException(error);
            }
            client.setFileType(clientConfig.getFileType());
            // 文件传输模式
            client.setFileTransferMode(FTP.STREAM_TRANSFER_MODE);
            // 10M
            client.setBufferSize(1024 * 1024 * 10);

            // 客户端模式：主动模式或者被动模式
            if (clientConfig.getLocalActiveMode()) {
                client.enterLocalActiveMode();
            } else {
                client.enterLocalPassiveMode();
            }

            // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
            if (FTPReply.isPositiveCompletion(client.sendCommand(
                    "OPTS UTF8", "ON"))) {
                client.setControlEncoding("UTF-8");
            } else {
                client.setControlEncoding("GBK");
            }
        } catch (Exception e) {
            String error =
                    String.format("failed to connect ftp server, please check the configuration: %s", ftpInfo);
            client.logout();
            client.disconnect();
            throw new RuntimeException(error);
        }
        return client;
    }

    @Override
    public PooledObject<FTPClient> wrap(FTPClient ftpClient) {
        // 对 ftpClient 进行包装，使之可以方便连接池记录一些状态
        return new DefaultPooledObject<FTPClient>(ftpClient);
    }

    @Override
    public void destroyObject(PooledObject<FTPClient> p) throws Exception {
        if (p == null) {
            return;
        }
        FTPClient client = p.getObject();
        try {
            if(client != null && client.isConnected()) {
                client.logout();
            }
        } catch (Exception e) {
            //
        } finally {
            if (client != null) {
                client.disconnect();
            }
        }
    }

    @Override
    public boolean validateObject(PooledObject<FTPClient> p) {
        try {
            FTPClient client = p.getObject();
            if (client == null || !client.isConnected()) {
                return false;
            }
            return client.sendNoOp();
        } catch (Exception e) {
            return false;
        }

    }
}
