package com.yeepay.g3.core.nccashier.service.dataEncrypt;

/**
 * Created by ruiyang.du on 2017/8/7.
 */
public interface NccashierDataEncryptService {

    /**
     * 中断已在执行的任务
     * @return
     */
    String stopWorkingTask();

    /**
     * 执行数据批量清洗
     * @param corePoolSize 线程池核心大小
     * @param maxPoolSize 线程池最大大小
     * @param queueSize 线程池队列大小
     * @param fromId 总ID起点
     * @param toId 总ID终点
     * @param queryPageSize 分页查询大小
     * @param partTaskSize 分页内子任务处理数据大小
     * @param threadTaskSize 单线程处理任务大小
     * @param className 需清洗的数据对于的entity类名称
     * @return
     */
    String batchEncryptData(int corePoolSize, int maxPoolSize, int queueSize, long fromId, long toId,int queryPageSize,int partTaskSize,int threadTaskSize, String className);
}
