package id.example.yoshida.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;

import java.util.Map;
import java.util.Set;

import static java.util.Objects.requireNonNullElse;

@Slf4j
public abstract class AbstractDynamicConsumer implements AppDestroyHandler {

	private final ConcurrentKafkaListenerContainerFactory<String, String> factory;

	private ConcurrentMessageListenerContainer<String, String> container;

	protected AbstractDynamicConsumer(KafkaProperties kafkaProperties) {
		this(kafkaProperties, null);
	}

	protected AbstractDynamicConsumer(KafkaProperties kafkaProperties, Argument argument) {
		var arg = requireNonNullElse(argument, Argument.builder().build());

		Map<String, Object> consumerProps = kafkaProperties.buildConsumerProperties(arg.getSslBundles());

		String groupId = buildGroupId(kafkaProperties.getConsumer().getGroupId(), arg);
		consumerProps.replace(ConsumerConfig.GROUP_ID_CONFIG, groupId);

		factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerProps));
	}

	protected abstract void consume(ConsumerRecord<String, String> messageListener);

	public final void listen(Set<String> topic) {
		if (topic.isEmpty()) {
			throw new IllegalArgumentException("topic is empty");
		}
		container = factory.createContainer(topic.toArray(String[]::new));
		container.setBeanName(beanName());
		container.setupMessageListener((MessageListener<String, String>) this::consume);
		container.start();
		log.info("consumer listen topic: {}", topic);
	}

	public final void deafen() {
		if (container == null) {
			log.warn("deafen failed, no active container");
			return;
		}

		container.stop();
	}

	private String buildGroupId(String currentGroupId, Argument argument) {
		String subGroupId = requireNonNullElse(argument.getSubGroupId(),
				"#{GROUP_ID}--" + this.getClass().getSimpleName());
		return subGroupId.replace("#{GROUP_ID}", currentGroupId);
	}

	private String beanName() {
		return "kafka" + this.getClass().getSimpleName() + "DynamicConsumer";
	}

	@Override
	public void onAppDestroy() {
		deafen();
	}

	@Builder
	@Getter
	public static class Argument {

		private SslBundles sslBundles;

		private String subGroupId;

	}

}
