package id.example.yoshida.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerInterceptor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

@Slf4j
public class LogKafkaConsumerInterceptor implements ConsumerInterceptor<String, String> {

	@Override
	public ConsumerRecords<String, String> onConsume(ConsumerRecords<String, String> consumerRecords) {
		for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			log.info("begin consume from topic: {}. partition: {}, header: {}, value: {}", consumerRecord.topic(),
					consumerRecord.partition(), consumerRecord.headers(), consumerRecord.value());
		}
		return consumerRecords;
	}

	@Override
	public void onCommit(Map<TopicPartition, OffsetAndMetadata> map) {
		Set<Entry<TopicPartition, OffsetAndMetadata>> committedEntries = map.entrySet();

		for (Map.Entry<TopicPartition, OffsetAndMetadata> committedEntry : committedEntries) {
			log.info("success consume from topic: {}", committedEntry.getKey().topic());
		}
	}

	@Override
	public void close() {
		// Noncompliance - method is empty
	}

	@Override
	public void configure(Map<String, ?> map) {
		// Noncompliance - method is empty
	}

}
