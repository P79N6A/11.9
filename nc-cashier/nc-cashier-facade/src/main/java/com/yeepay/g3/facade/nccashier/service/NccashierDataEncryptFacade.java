package com.yeepay.g3.facade.nccashier.service;

/**
 * Created by ruiyang.du on 2017/8/7.
 */
public interface NccashierDataEncryptFacade {

    String stopWorkingTask();

    /**
     * 执行数据批量清洗
     * @param corePoolSize 线程池核心大小
     * @param maxPoolSize 线程池最大大小
     * @param queueSize 线程池队列大小
     * @param fromIndex 总ID起点
     * @param toIndex 总ID终点
     * @param queryPageSize 分页查询大小
     * @param partTaskSize 分页内子任务处理数据大小
     * @param threadTaskSize 单线程处理任务大小
     * @param className
     * @return
     */
    String batchEncryptData(int corePoolSize, int maxPoolSize, int queueSize,long fromIndex, long toIndex, int queryPageSize,int partTaskSize,int threadTaskSize,String className);
}
