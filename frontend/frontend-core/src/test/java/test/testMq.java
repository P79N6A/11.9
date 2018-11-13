package test;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;

import com.rabbitmq.client.Channel;

public class testMq implements ChannelAwareMessageListener{

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
		channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);
	}


}
