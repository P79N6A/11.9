package com.yeepay.g3.core.frontend.log;

import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.infra.Configuration;
import com.yeepay.infra.Tracer;
import io.opentracing.Span;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 监控埋点工具类
 * 一个应用对应一个Tracer，一个应用对应一个连接
 * 
 * @author：tao.liu
 * @since：2017年12月4日 下午5:04:24
 * @version:
 */
public class FETracerUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(FETracerUtils.class);

    private static final String SPAN_NAME = "frontend";

    /**
     * 这个没有测过。。。用的时候测试一下
     * 利用MessageFormat来格式化该消息，填充value，然后在分解
     * 用法如下：
     *       span("key1:{0}~key2:{1}~key3:{2}", value1, value2, value3);
     *       key:代表健，value:代表值  key1和value1为一对健值对
     *       先进行格式化，变成 key1:value1,key2:value2,key3:value3
     *       
     * @param arg1 格式为： key1:{0}~key2:{1}~key2:{2}
     * @param args value的数组，对应填充到arg1中的{n}中。
     */
    public static void spanFormat(String arg1, Object... args) {
        try {
            if (null == arg1 || arg1.length() == 0 || null == args || args.length == 0) {
                return;
            }
            Tracer tracer = NcTracer.getTracer();
            if (null == tracer) {
                return;
            }
            String[] entryStrs = MessageFormat.format(arg1, args).split("~");
            int len = entryStrs.length;
            
            Span span = tracer.buildSpan(SPAN_NAME).startManual();
           
            for (int i = 0; i < len; i++) {
                String entry = entryStrs[i];
                int index = entry.indexOf(":");
                if (index + 1 == entry.length()){
                    span.setTag(entry.substring(0, index), "");
                } else {
                    span.setTag(entry.substring(0, index), entry.substring(index+1));
                }
                
            }
            span.finish();
        } catch (Throwable t) {
            LOGGER.error("写入埋点日志异常：" + t.getMessage());
        }
    }

    /**
     * 可变参数，简便开发，适用于参数个数不确定情况下；
     * 入参的个数：参数的长度为偶数;
     * 入参的顺序：key1,value1,key2,value2 ...... keyN,valueN;
     * key:代表健，value:代表值 ，一对健值对要放在一起，key在前，value在后，顺序连续。
     * 比如：
     * NcTracerUtils.span("customerNum","10040026668","requestId","test201710260029","biz","11","bizOrder","20171204232");
     * 总共4对健值对：
     * key1:customerNum ，value1:10040026668
     * key2:requestId ，value2:test201710260029
     * key3:biz ，value3:11
     * key4:bizOrder ，value4:20171204232
     * 
     * @param args 参数的长度为偶数
     */
    public static void span(String... args) {

        try {
            if (null == args || args.length == 0) {
                return;
            }
            
            Tracer tracer = NcTracer.getTracer();
            if (null == tracer) {
                return;
            }
            
            NcSpan span = new NcSpan();
            span.initSpan(tracer);
            
            int len = args.length;
            for (int i = 0; i < len; i = i + 2) {
                if (i + 1 >= len) {
                    span.setTag(args[i], "");
                } else {
                    span.setTag(args[i], args[i + 1]);
                }
            }
            span.finish();
        } catch (Throwable t) {
            LOGGER.error("写入埋点日志异常：" + t.getMessage());
        }
    }

    public static void main(String args[]) {
        FETracerUtils.spanFormat("customerNum:{0}~requestId:{1}~biz:{2}~bizOrder:{3}","10040026668","test201710260029", "11" , "20171204232");

        FETracerUtils.span("customerNum", "10040026668", "requestId", "test201710260029", "biz", "11", "bizOrder", "20171204232");
        // NcTracerUtils.buildSpan().setTag("customerNum","10040026668").setTag("requestId","test201710260029").setTag("biz","11").setTag("bizOrder","20171204232").finish();
    }

    /**
     * 创建一个埋点Span记录
     * 主要用于链式调用，使代码更加简洁易懂，将多个埋点的信息放在一行代码里。
     *  
     * 用法如下：
     *     NcTracerUtils.buildSpan().setTag("customerNum","10040026668").setTag("requestId","test201710260029").setTag("biz","11").setTag("bizOrder","20171204232").finish();
     *     buildSpan ：创建一个埋点记录
     *     setTage   ：设置埋点的属性值；
     *     finish    : 完成一个埋点，将埋点记录的信息发送到ElasticSearch中
     *     
     * @return NcSpan
     */
    public static NcSpan buildSpan() {
        NcSpan ncSpan = new NcSpan();
        Tracer tracer = NcTracer.getTracer();
        if (null != tracer) {
            ncSpan.initSpan(tracer);
        }
        return ncSpan;
    }
    
    private static class NcTracer {

        private static final Lock LOCK = new ReentrantLock();

        private static Tracer tracer;

        public static Tracer getTracer() {
            if (null == tracer) {
                try {
                    initTracer();
                } catch (Throwable t) {
                    LOGGER.error("初始化一个Tracer异常 " + t);
                }
            }
            return tracer;
        }

        /**
         * Configuration 类为Tracer的配置类，构造方法中有三个参数
         * 第一个参数为serviceName(服务名)，是在应用中唯一区分Tracer的方式，体现到ElasticSearch中与appName(应用名)同构成了indexName(索引名)，
         * 如果为null则indexName为monitor_$(appName)，如果不为空则为monitor_$(appName)_$(serviceName)
         * 第二个参数为sampler(采样器)，顾名思义sampler决定了埋点的采样模式与采集频率，目前只有一种采样模式probabilistic(概率采样)，采样概率为0.0到1.0之间的一个double
         * 第三个参数为reporter(发布器)，reporter决定了数据发送的方式和远端collector地址，目前只能发送数据到fluentd
         * 觉得麻烦的话也可以使用以下方式，默认表示服务名为null，采样概率1，发布数据到域名monitor.logsync.yp
         * Tracer tracer = new Configuration().getTracer();
         * 
         * @throws InterruptedException
         */
        public static void initTracer() throws InterruptedException {

            if (LOCK.tryLock(10, TimeUnit.SECONDS)) {
                try {
                    LOGGER.info("初始化Tracer获取到锁");
                    if (null == tracer) {
                        tracer = new Configuration(null, new Configuration.SamplerConfiguration("probabilistic", 1),
                                new Configuration.ReporterConfiguration("monitor.logsync.yp", 1000, -1))
                                .getTracer();
                        LOGGER.info("初始化一个Tracer成功");
                    }
                } catch (Throwable t) {
                    LOGGER.error("初始化一个Tracer失败{} ", t);
                } finally {
                    LOCK.unlock();
                    LOGGER.info("初始化Tracer释放掉锁");
                }
            }

        }
    }

    private static class NcSpan {

        private Span span;

        private NcSpan() {

        }

        public void initSpan(Tracer tracer) {
            if (null != tracer) {
                try {
                    span = tracer.buildSpan(SPAN_NAME).startManual();
                } catch (Throwable t) {
                    LOGGER.error("初始化一个Span失败 " + t);
                }
            }
        }

        public NcSpan setTag(String key, String value) {
            if (null != span) {
                if (null == value){
                    value = "";
                }
                span.setTag(key, value);
            }
            return this;
        }

        @SuppressWarnings("unused")
        public NcSpan setTag(String key, boolean value) {
            if (null != span) {
                span.setTag(key, value);
            }
            return this;
        }

        @SuppressWarnings("unused")
        public NcSpan setTag(String key, Number value) {
            if (null != span) {
                span.setTag(key, value);
            }
            return this;
        }

        public void finish() {
            if (null != span) {
                span.finish();
            }
        }

    }

}
