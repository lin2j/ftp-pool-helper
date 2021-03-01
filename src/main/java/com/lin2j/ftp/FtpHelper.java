package com.lin2j.ftp;

import org.apache.commons.net.ftp.FTPClient;
import sun.net.ftp.FtpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

/**
 * 针对同一个连接池的 ftp 操作方法的接口集合<br>
 * 一个 ftp 连接池中的连接，应该都是同一个 ftp 服务的
 *
 * @author linjinjia
 * @date 2021/2/26 11:48
 */
public interface FtpHelper {

    /**
     * 初始化连接池
     *
     * @return 成功初始化返回 true，否则返回false
     */
    boolean init();

    /**
     * 销毁一个连接池
     */
    void destroy();

    /**
     * ftp 客户端断开连接，实际上是将连接返回给连接池
     *
     * @param ftpClient 返还的客户端连接
     */
    void disconnect(FTPClient ftpClient);

    /**
     * 从连接池中取出一个连接
     *
     * @return ftp 客户端连接
     * @throws Exception 等待空闲连接超时时抛异常
     */
    FTPClient getFtpClient() throws Exception;

    /**
     * 判断指定路径的目录是否存在
     *
     * @param dir 目录路劲，相对路径或者绝对路径
     * @return
     */
    boolean isDirExist(String dir);

    /**
     * 判断指定路径下指定文件是否存在<br>
     * 以下情况返回false，否则返回true
     * <li>路径不存在</li>
     * <li>路径存在但是文件不存在</li>
     * <li>出现异常</li>
     *
     * @param dir  指定路径
     * @param file 文件名称
     * @return 存在返回true，否则返回false
     */
    boolean isFileExist(String dir, String file);

    /**
     * 当前目录下指定文件是否存在
     *
     * @param file 文件名称
     * @return 文件存在返回true，否则返回false
     */
    boolean isFileExist(String file);

    /**
     * 获取指定路径下的文件名列表
     *
     * @param dir 路径
     * @return 文件名集合
     * @throws IOException IO 异常
     */
    Set<String> listFiles(String dir) throws IOException;

    /**
     * 递归获取目录下的文件名列表
     *
     * @param dir   指定路径
     * @param depth 递归的深度
     * @return 文件名集合
     * @throws IOException IO 异常
     */
    Set<String> listFilesRecursive(String dir, int depth) throws IOException;

    /**
     * 获取指定文件的输入流
     *
     * @param client ftp 客户端连接
     * @param file   文件名
     * @return 文件对应的输出流
     */
    InputStream getInputStream(FtpClient client, String file);

    /**
     * 创建目录
     *
     * @param dir 待创建的目录
     * @return 成功创建返回true，否则返回false
     */
    boolean makeDir(String dir);

    /**
     * 递归创建目录
     *
     * @param dir 待创建的目录
     * @return 成功创建返回true，否则返回false
     */
    boolean makeDirRecursive(String dir);

    /**
     * 删除集合中对应的文件
     *
     * @param files 待删除文件名集合
     */
    void deleteFiles(Set<String> files);
}
