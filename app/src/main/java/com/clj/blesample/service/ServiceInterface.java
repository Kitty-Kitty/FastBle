package com.clj.blesample.service;

/**
 * 表示所有服务类的操作接口
 *
 * @author f
 * @version 1.0
 * @updated 22-7月-2020 18:29:26
 */
public interface ServiceInterface {

    /**
     * 表示初始化服务对象
     */
    public boolean initialize() throws ServiceException;

    /**
     * 表示启动服务对象
     */
    public boolean start() throws ServiceException;

    /**
     * 表示停止服务对象
     */
    public boolean stop() throws ServiceException;

}