package id.example.yoshida.service;

import freemarker.template.Configuration;
import id.example.yoshida.exception.ParseException;
import id.example.yoshida.model.repository.TemplateConfigRepository;
import id.example.yoshida.service.dto.TemplateConfig;
import id.example.yoshida.service.dto.TemplateConfigItem;
import id.example.yoshida.service.dto.TemplateConfigRequest;
import id.example.yoshida.service.dto.TemplateTopicConfig;
import id.example.yoshida.utils.CommonUtils;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

@Service
public class TemplateConfigServiceImpl implements TemplateConfigService {

	private final TemplateConfigRepository templateConfigRepository;

	private final CommonUtils utils;

	private final Configuration freemarkerConfig;

	public TemplateConfigServiceImpl(TemplateConfigRepository templateConfigRepository, CommonUtils utils,
			@Lazy Configuration freemarkerConfig) {
		this.templateConfigRepository = templateConfigRepository;
		this.utils = utils;
		this.freemarkerConfig = freemarkerConfig;
	}

	@Override
	public List<TemplateConfigItem> getAll(String query) {
		return templateConfigRepository.findAll()
			.stream()
			.map(p -> utils.convert(p, TemplateConfigItem.class))
			.toList();
	}

	@Override
	public TemplateConfig getDetail(String id) {
		var templateConfig = templateConfigRepository.findById(id).orElseThrow();

		var result = utils.convert(templateConfig, TemplateConfig.class);

		var topicConfig = templateConfig.getTopicConfig();
		if (topicConfig != null) {
			var topicConfigResult = utils.convert(topicConfig, TemplateTopicConfig.class);
			result.setTopicConfig(topicConfigResult);
		}

		return result;
	}

	@Override
	public TemplateConfig getSourceConfig(String idOrName) {
		return templateConfigRepository.findByIdOrName(idOrName, idOrName)
			.map(p -> utils.convert(p, TemplateConfig.class))
			.orElseThrow();
	}

	@Override
	@Transactional
	public void save(TemplateConfigRequest request) {

		boolean updateData = request.getId() != null;

		id.example.yoshida.model.TemplateConfig modelConfig;
		if (updateData) {
			modelConfig = templateConfigRepository.findById(request.getId()).orElseThrow();

			modelConfig.setName(requireNonNullElse(request.getName(), modelConfig.getName()));
			modelConfig.setSource(requireNonNullElse(request.getSource(), modelConfig.getSource()));
		}
		else {
			modelConfig = id.example.yoshida.model.TemplateConfig.builder().build();

			modelConfig.setName(requireNonNull(request.getName()));
			modelConfig.setSource(requireNonNull(request.getSource()));
		}
		templateConfigRepository.save(modelConfig);
	}

	@Override
	@Transactional
	public void delete(String id) {
		var model = templateConfigRepository.findById(id).orElseThrow();

		boolean hasLinkedTopic = model.getTopicConfig() != null;
		if (hasLinkedTopic) {
			throw new IllegalStateException("template has linked topic item");
		}
		templateConfigRepository.deleteById(id);
	}

	@Override
	public String render(String name, Map<?, ?> model) {
		StringWriter writer = new StringWriter();

		try {
			freemarkerConfig.getTemplate(name).process(model, writer);
		}
		catch (Exception e) {
			throw new ParseException("error rendering template \"" + name + "\", reason: " + e.getMessage() + ".", e);
		}

		return writer.toString();

	}

}
