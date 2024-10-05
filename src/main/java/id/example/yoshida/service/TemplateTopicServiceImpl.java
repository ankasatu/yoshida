package id.example.yoshida.service;

import id.example.yoshida.model.TemplateTopicConfig;
import id.example.yoshida.model.repository.TemplateConfigRepository;
import id.example.yoshida.model.repository.TemplateTopicConfigRepository;
import id.example.yoshida.service.dto.TemplateTopicConfigRequest;
import id.example.yoshida.utils.CommonUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class TemplateTopicServiceImpl implements TemplateTopicService {

	private final CommonUtils utils;

	private final KafkaTemplate<String, String> kafkaTemplate;

	private final TemplateConfigRepository templateConfigRepository;

	private final TemplateTopicConfigRepository templateTopicConfigRepository;

	@Override
	@Transactional
	public void attach(String templateConfigId, TemplateTopicConfigRequest request) {

		boolean loopSourceAndTarget = Objects.compare(request.getSourceTopic(), request.getTargetTopic(),
				String::compareTo) == 0;
		if (loopSourceAndTarget) {
			throw new IllegalArgumentException("source and target topic value should be different");
		}

		var template = templateConfigRepository.findById(templateConfigId).orElseThrow();

		if (template.getTopicConfig() != null) {
			throw new IllegalStateException("config is exist");
		}

		templateTopicConfigRepository.save(TemplateTopicConfig.builder()
			.category(request.getCategory())
			.sourceTopic(request.getSourceTopic())
			.targetTopic(request.getTargetTopic())
			.template(template)
			.build());
	}

	@Override
	@Transactional
	public void detach(String templateConfigId) {
		var template = templateConfigRepository.findById(templateConfigId).orElseThrow();
		var topic = template.getTopicConfig();

		if (topic == null) {
			throw new IllegalStateException("config not found");
		}
		template.setTopicConfig(null);
		templateConfigRepository.save(template);

		templateTopicConfigRepository.deleteById(topic.getId());
	}

	@Override
	public List<TemplateTopicConfig> getAllConfig() {
		return templateTopicConfigRepository.findAll();
	}

	@Override
	public CompletableFuture<SendResult<String, String>> send(Object data, String topicName) {
		var message = MessageBuilder.withPayload(utils.toJson(data)).setHeader(KafkaHeaders.TOPIC, topicName).build();
		return kafkaTemplate.send(message);
	}

}
