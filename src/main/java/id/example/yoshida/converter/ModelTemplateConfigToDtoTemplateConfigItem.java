package id.example.yoshida.converter;

import id.example.yoshida.model.TemplateConfig;
import id.example.yoshida.service.dto.TemplateConfigItem;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ModelTemplateConfigToDtoTemplateConfigItem implements Converter<TemplateConfig, TemplateConfigItem> {

	@Override
	public TemplateConfigItem convert(TemplateConfig source) {
		return TemplateConfigItem.builder()
			.id(source.getId())
			.name(source.getName())
			.createdTs(source.getCreatedTs())
			.updatedTs(source.getUpdatedTs())
			.build();
	}

}
