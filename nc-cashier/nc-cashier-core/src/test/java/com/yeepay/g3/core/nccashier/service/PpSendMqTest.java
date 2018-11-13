package com.yeepay.g3.core.nccashier.service;

import com.alibaba.fastjson.JSON;
import com.caucho.hessian.client.HessianProxyFactory;
import com.google.common.collect.Maps;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.yeepay.g3.component.open.dto.RechargeRequestDTO;
import com.yeepay.g3.component.yop.facade.MemberBasicFuncFacade;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.facade.member.param.MemberBaseResponse;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import com.yeepay.g3.utils.common.json.JSONUtils;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.junit.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

/**
 * Created by jimin.zhou on 18/1/2.
 */
public class PpSendMqTest extends BaseTest{


    public String QUEUE_NAME = "nccashier_receive_pp";

    @Test
    public void sendMqTest() throws IOException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();

        //设置RabbitMQ相关信息
        factory.setHost("rabbitmq.bass.3g");
        factory.setUsername("yjzf");
        factory.setPassword("1qaz2wsx");
        factory.setPort(5672);

        //创建一个新的连接
        Connection connection = factory.newConnection();

        //创建一个通道
        Channel channel = connection.createChannel();

        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //发送消息到队列中
//        String message = "Hello RabbitMQ";
        PayRecordResponseDTO payRecordResponseDTO = new PayRecordResponseDTO();

        channel.basicPublish("", QUEUE_NAME, null, JSON.toJSONBytes(payRecordResponseDTO));
        System.out.println("Producer Send +'" + JSON.toJSONString(payRecordResponseDTO) + "'");

        //关闭通道和连接
        channel.close();
        connection.close();
    }

    
    public static void main(String[] args) {
		String url = "http://10.151.30.98:8007/member-hessian/hessian/MemberBasicFuncFacade";
//		String url = "http://localhost:8007/member-hessian/hessian/MemberBasicFuncFacade";
		HessianProxyFactory factory = new HessianProxyFactory();
		MemberBasicFuncFacade memberBasicFuncFacade = null;
		try {
			memberBasicFuncFacade = (MemberBasicFuncFacade) factory
					.create(MemberBasicFuncFacade.class, url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		RechargeRequestDTO request = new RechargeRequestDTO();
		request.setPlatformId("10040020578");
		request.setPlatformUserNo("lili0101");
		request.setPlatformName("会员测试商户");
		request.setProductName("会员测试");
		request.setRequestId("20180227001");
		request.setAmount("0.01");
		request.setDirectPayType("WECHAT");
		request.setSaleProductCode("981291");
		request.setNotifyURL("http://10.151.30.98:8007/member-hessian/hessian/");
		request.setFrontCallBackUrl("https://m.jd.com");
		request.setOrderNo("AAA20180322A00zml3");//改这个就行其他放着
		Map<String, String> ext = Maps.newHashMap();
		ext.put("userPayIp", "172.18.162.230");
		ext.put("userUA", "User-Agent: Mozilla/5.0 ");
		ext.put("platForm", "Wap");
		ext.put("appName", "绝地求生");
		ext.put("appStatement", "https://m.jd.com");
		request.setExtendRiskInfo(JSONUtils.toJsonString(ext));
		request.setProductName("会员测试充值");
		System.out.println(ReflectionToStringBuilder.toString(request));
		MemberBaseResponse response = memberBasicFuncFacade.recharge(request);
		System.out.println(ReflectionToStringBuilder.toString(response));
	}
//  依赖
// 	<groupId>com.yeepay.g3.member</groupId>
// <artifactId>member-component-facade</artifactId>
// <version>1.0-SNAPSHOT</version>
}
