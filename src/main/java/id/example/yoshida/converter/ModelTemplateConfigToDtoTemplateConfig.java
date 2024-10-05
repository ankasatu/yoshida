package id.example.yoshida.converter;

import id.example.yoshida.service.dto.TemplateConfig;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ModelTemplateConfigToDtoTemplateConfig
		implements Converter<id.example.yoshida.model.TemplateConfig, TemplateConfig> {

	@Override
	public TemplateConfig convert(id.example.yoshida.model.TemplateConfig modelSource) {
		return TemplateConfig.builder()
			.id(modelSource.getId())
			.name(modelSource.getName())
			.createdTs(modelSource.getCreatedTs())
			.updatedTs(modelSource.getUpdatedTs())
			.source(modelSource.getSource())
			.build();
	}

}
