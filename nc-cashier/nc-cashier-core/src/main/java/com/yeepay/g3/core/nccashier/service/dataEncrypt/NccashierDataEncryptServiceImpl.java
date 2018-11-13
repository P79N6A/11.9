package com.yeepay.g3.core.nccashier.service.dataEncrypt;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.dao.PaymentRequestDao;
import com.yeepay.g3.core.nccashier.dao.RealPersonChooseTimesDao;
import com.yeepay.g3.core.nccashier.dao.UnbindCardDao;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.RealPersonChooseTimes;
import com.yeepay.g3.core.nccashier.entity.UnbindRecord;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * nc-cashier 数据库敏感信息清洗
 * Created by ruiyang.du on 2017/8/4.
 */
@Service("nccashierDataEncryptService")
public class NccashierDataEncryptServiceImpl implements NccashierDataEncryptService {

    private static final Logger logger = NcCashierLoggerFactory.getLogger(NccashierDataEncryptServiceImpl.class);


    @Autowired
    private PaymentRequestDao paymentRequestDao;
    @Autowired
    private UnbindCardDao unbindCardDao;
    @Autowired
    private RealPersonChooseTimesDao realPersonChooseTimesDao;

    /**
     * 线程池，需调用initThreadPool方法来完成初始化
     */
    private static ThreadPoolExecutor executor = null;

    /**
     * 记录总计已完成的数量
     */
    private static AtomicInteger totalCountFinished = new AtomicInteger(0);

    private static boolean taskStop = false;

    /**
     * 初始化线程池
     *
     * @param corePoolSize
     * @param maxPoolSize
     */
    private String initThreadPool(int corePoolSize, int maxPoolSize, int queueSize) {
        if (executor != null) {
            executor.shutdown();
        }
        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(queueSize);
        executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, 60, TimeUnit.SECONDS, queue);
        taskStop = false;
        String result = "【nc-cashier敏感数据加密】初始化线程池结果 ==" + JSON.toJSONString(executor);
        logger.info(result);
        return result;
    }


    @Override
    public String stopWorkingTask() {
        taskStop = true;
        return "操作成功，任务将在下个分页查询终止";
    }

    @Override
    public String batchEncryptData(int corePoolSize, int maxPoolSize, int queueSize, long fromId, long toId, int queryPageSize, int partTaskSize, int threadTaskSize, String className) {
        //先查出querySize条数据
        long start = System.currentTimeMillis();
        try {
            initThreadPool(corePoolSize, maxPoolSize, queueSize);
            //本次调用接口，累计需处理的数据量
            long totalRowNum = countDataByIds(className,fromId,toId);
            //需分页查询次数
            long pageCount = totalRowNum % queryPageSize == 0 ? totalRowNum / queryPageSize : totalRowNum / queryPageSize + 1;
            for (int i = 0; i < pageCount; i++) {
                //将总数据量先进行拆分，进行分页查询，并分批处理
                long fromIndexPage = i * queryPageSize;
                long toIndexPage = (i + 1) * queryPageSize < totalRowNum ? (i + 1) * queryPageSize : totalRowNum;
                if(taskStop){
                    //任务需要终止
                    break;
                }
                pageOperate(fromIndexPage, toIndexPage, fromId, toId, partTaskSize, threadTaskSize, className);
            }

            StringBuilder result = new StringBuilder("【nc-cashier敏感数据加密】");
            result.append("执行完毕，总计执行数 = ").append(totalCountFinished.intValue());
            result.append("总计耗时秒数 = ").append((System.currentTimeMillis() - start) / 1000);
            logger.info(result.toString());
            totalCountFinished.set(0);
            return result.toString();
        } catch (Exception e) {
            logger.error("【nc-api敏感数据加密】 异常：", e);
            totalCountFinished.set(0);
            return "【nc-api敏感数据加密】 异常中断";
        }
    }

    /**
     * 分页查询后，针对每页数据的操作
     * @param fromIndexPage 分页行数起点
     * @param toIndexPage 分页行数终点
     * @param fromIdTotal 总ID范围起点
     * @param toIdTotal 总ID范围终点
     */
    private void pageOperate(long fromIndexPage, long toIndexPage,long fromIdTotal, long toIdTotal, int partTaskSize, int threadTaskSize, String className) {
        //logger.info("【nc-cashier敏感数据加密】准备分页查询并处理数据，本次查询ID起止为{}和{}的数据，查询的数据为{}", fromIndexPage, toIndexPage, className);

        //先查出querySize条数据
        long queryStart = System.currentTimeMillis();
        Map<String, Long> pageParam = new HashMap<String, Long>();
        pageParam.put("fromIndex", fromIndexPage);
        pageParam.put("toIndex", toIndexPage);
        pageParam.put("fromIdTotal", fromIdTotal);
        pageParam.put("toIdTotal", toIdTotal);
        if ("PaymentRequest".equals(className)) {
            List<PaymentRequest> pageTaskList = queryPaymentRequest(pageParam);
            logger.info("【nc-cashier敏感数据加密】本次批量查询耗时{}ms，查询数量为{}", (System.currentTimeMillis() - queryStart), pageTaskList.size());
            executeByType(pageTaskList, partTaskSize, threadTaskSize);
        } else if ("UnbindRecord".equals(className)) {
            List<UnbindRecord> pageTaskList = queryUnbindRecord(pageParam);
            logger.info("【nc-cashier敏感数据加密】本次批量查询耗时{}ms，查询数量为{}", (System.currentTimeMillis() - queryStart), pageTaskList.size());
            executeByType(pageTaskList, partTaskSize, threadTaskSize);
        } else if ("RealPersonChooseTimes".equals(className)) {
            List<RealPersonChooseTimes> pageTaskList = queryRealPersonChooseTimes(pageParam);
            logger.info("【nc-cashier敏感数据加密】本次批量查询耗时{}ms，查询数量为{}", (System.currentTimeMillis() - queryStart), pageTaskList.size());
            executeByType(pageTaskList, partTaskSize, threadTaskSize);
        } else {
            logger.error("【nc-cashier敏感数据加密】参数className错误");
        }
    }

    private <T> void executeByType(List<T> pageTaskList, int partTaskSize, int threadTaskSize) {
        int pageSize = pageTaskList.size();
        int partTaskNum = pageSize % partTaskSize == 0 ? pageSize / partTaskSize : pageSize / partTaskSize + 1;//子任务的数目
        for (int i = 0; i < partTaskNum; i++) {
            //把querySize条数据，拆成若干个最大partTaskSize条的子列表
            int fromIndexPart = i * partTaskSize;
            int toIndexPart = (i + 1) * partTaskSize < pageTaskList.size() ? (i + 1) * partTaskSize : pageTaskList.size();
            List<T> partTaskList = pageTaskList.subList(fromIndexPart, toIndexPart);
            logger.info("【nc-cashier敏感数据加密】分页内任务组开始处理----处理分页列表起止序号为[" + fromIndexPart + "," + toIndexPart + "]");
            //针对每个partTaskSize条，按每个线程处理最大threadTaskSize个记录的效率，使用线程池
            long taskGroupStart = System.currentTimeMillis();
            int threadNum = partTaskList.size() % threadTaskSize == 0 ? partTaskList.size() / threadTaskSize : partTaskList.size() / threadTaskSize + 1;//子任务中需开线程的数目
            final CountDownLatch countDownLatch = new CountDownLatch(threadNum);
            for (int j = 0; j < threadNum; j++) {
                int fromIndexThread = j * threadTaskSize;
                int toIndexThread = (j + 1) * threadTaskSize < partTaskList.size() ? (j + 1) * threadTaskSize : partTaskList.size();
                final List<T> threadTaskList = partTaskList.subList(fromIndexThread, toIndexThread);
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        threadOperate(threadTaskList);
                        countDownLatch.countDown();
                    }
                });
            }
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                logger.error("【nc-cashier敏感数据加密】线程调度异常，", e);
            }
            logger.info("【nc-cashier敏感数据加密】分页内任务组处理完毕----处理分页列表起止序号为({},{}]，耗时{}ms", fromIndexPart, toIndexPart, (System.currentTimeMillis() - taskGroupStart));
        }
    }

    /**
     * 批量查询payment_request表，获取id和identity_id
     *
     * @param pageParam
     */
    private List<PaymentRequest> queryPaymentRequest(Map<String, Long> pageParam) {
        List<PaymentRequest> list = paymentRequestDao.listPaymentRequestNotEnctypt(pageParam);
        return list;
    }

    /**
     * 批量查询payment_request表，获取id和identity_id
     */
    private List<UnbindRecord> queryUnbindRecord(Map<String, Long> pageParam) {
        List<UnbindRecord> list = unbindCardDao.listUnbindRecordNotEnctypt(pageParam);
        return list;
    }

    /**
     * 批量查询payment_request表，获取id和identity_id
     */
    private List<RealPersonChooseTimes> queryRealPersonChooseTimes(Map<String, Long> pageParam) {
        List<RealPersonChooseTimes> list = realPersonChooseTimesDao.listRealPersonChooseTimesNotEnctypt(pageParam);
        return list;
    }

    private int countDataByIds(String className,long fromIdTotal,long toIdTotal){
        int count = 0;
        Map<String,Long> map = new HashMap<String, Long>();
        map.put("fromIdTotal",fromIdTotal);
        map.put("toIdTotal",toIdTotal);
        if ("PaymentRequest".equals(className)) {
            count = paymentRequestDao.countPaymentRequest(map);
        } else if ("UnbindRecord".equals(className)) {
            count = unbindCardDao.countUnbindRecord(map);
        } else if ("RealPersonChooseTimes".equals(className)) {
            count = realPersonChooseTimesDao.countRealPersonChooseTimes(map);
        } else {
            logger.error("【nc-cashier敏感数据加密】参数className错误");
        }
        return count;
    }

    /**
     * 每个线程的操作-加密、更新
     *
     * @param threadTaskList
     */
    private <T> void threadOperate(List<T> threadTaskList) {
        int rows = 0;
        if(threadTaskList==null || threadTaskList.size()<1){
            return;
        }
        T object = threadTaskList.get(0);
        if (object instanceof PaymentRequest) {
            List<PaymentRequest> list = new ArrayList<PaymentRequest>();
            for (T t : threadTaskList) {
                if (StringUtils.isBlank(((PaymentRequest) t).getIdentityId())) {
                    continue;
                }
                list.add((PaymentRequest)t);
            }
            rows = paymentRequestDao.updateForBatchEncrypt(list);
        } else if (object instanceof UnbindRecord) {
            List<UnbindRecord> list = new ArrayList<UnbindRecord>();
            for (T t : threadTaskList) {
                if (StringUtils.isBlank(((UnbindRecord) t).getIdentityId())) {
                    continue;
                }
                list.add((UnbindRecord)t);
            }
            rows = unbindCardDao.updateForBatchEncrypt(list);
        } else if (object instanceof RealPersonChooseTimes) {
            List<RealPersonChooseTimes> list = new ArrayList<RealPersonChooseTimes>();
            for (T t : threadTaskList) {
                if (StringUtils.isBlank(((RealPersonChooseTimes) t).getIdentityId())) {
                    continue;
                }
                list.add((RealPersonChooseTimes)t);
            }
            rows = realPersonChooseTimesDao.updateForBatchEncrypt(list);
        }
        totalCountFinished.getAndAdd(rows);
        return;
    }


    /**
     * 加密后更新
     *
     * @param object
     * @return
     */
    /*private <T> int update(T object) {
        int rows = 0;
        if (object instanceof PaymentRequest) {
            if (StringUtils.isBlank(((PaymentRequest) object).getIdentityId())) {
                return 0;
            }
            rows = paymentRequestDao.updateForEncrypt((PaymentRequest) object);
        } else if (object instanceof UnbindRecord) {
            if (StringUtils.isBlank(((UnbindRecord) object).getIdentityId())) {
                return 0;
            }
            rows = unbindCardDao.updateForEncrypt((UnbindRecord) object);
        } else if (object instanceof RealPersonChooseTimes) {
            if (StringUtils.isBlank(((RealPersonChooseTimes) object).getIdentityId())) {
                return 0;
            }
            rows = realPersonChooseTimesDao.updateForEncrypt((RealPersonChooseTimes) object);
        }
        return rows;
    }*/

}
