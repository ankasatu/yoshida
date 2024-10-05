package id.example.yoshida.converter;

import id.example.yoshida.service.dto.TemplateTopicConfig;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ModelTemplateTopicConfigToDtoTemplateTopicConfig
		implements Converter<id.example.yoshida.model.TemplateTopicConfig, TemplateTopicConfig> {

	@Override
	public TemplateTopicConfig convert(id.example.yoshida.model.TemplateTopicConfig modelSource) {
		return TemplateTopicConfig.builder()
			.id(modelSource.getId())
			.createdTs(modelSource.getCreatedTs())
			.category(modelSource.getCategory())
			.sourceTopic(modelSource.getSourceTopic())
			.targetTopic(modelSource.getTargetTopic())
			.build();
	}

}
