package id.example.yoshida.service;

import id.example.yoshida.model.TemplateTopicConfig;
import id.example.yoshida.service.dto.TemplateResult;
import id.example.yoshida.utils.AppStartupHandler;
import id.example.yoshida.utils.CommonUtils;
import id.example.yoshida.utils.AbstractDynamicConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class YoshidaService extends AbstractDynamicConsumer implements AppStartupHandler {

	private final Map<String, TemplateTopicConfig> topicConfigMap = new HashMap<>();

	private final CommonUtils utils;

	private final TemplateConfigService templateConfigService;

	private final TemplateTopicService templateTopicService;

	public YoshidaService(KafkaProperties kafkaProperties, CommonUtils utils,
			TemplateConfigService templateConfigService, TemplateTopicService templateTopicService) {
		super(kafkaProperties);
		this.utils = utils;
		this.templateConfigService = templateConfigService;
		this.templateTopicService = templateTopicService;
	}

	@Override
	protected void consume(ConsumerRecord<String, String> messageListener) {
		var topicConfig = topicConfigMap.get(messageListener.topic());

		log.info("processing item from sourceTopic({}) with template(id:{}) into targetTopic({})",
				topicConfig.getSourceTopic(), topicConfig.getTemplateConfigId(), topicConfig.getTargetTopic());

		try {
			var parameter = utils.fromJson(messageListener.value(), LinkedHashMap.class);

			var templateId = topicConfig.getTemplateConfigId();
			var renderResult = templateConfigService.render(templateId, parameter);

			var targetTopic = topicConfig.getTargetTopic();

			// noinspection unchecked
			String sourceEventId = (String) parameter.getOrDefault("id", null);

			var templateResult = TemplateResult.builder()
				.result(renderResult)
				.sourceEventId(sourceEventId)
				.sourceTopicName(topicConfig.getSourceTopic())
				.templateId(templateId)
				.build();

			templateTopicService.send(templateResult, targetTopic);
		}
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	@Override
	public void onAppStartup() {
		var topicList = templateTopicService.getAllConfig();
		var ttnMap = topicList.stream().collect(Collectors.toMap(TemplateTopicConfig::getSourceTopic, p -> p));

		this.topicConfigMap.putAll(ttnMap);
		this.listen(ttnMap.keySet());
	}

}
