package id.example.yoshida.service;

import id.example.yoshida.service.dto.TemplateConfig;
import id.example.yoshida.service.dto.TemplateConfigItem;
import id.example.yoshida.service.dto.TemplateConfigRequest;

import java.util.List;
import java.util.Map;

public interface TemplateConfigService {

	List<TemplateConfigItem> getAll(String query);

	TemplateConfig getDetail(String id);

	TemplateConfig getSourceConfig(String idOrName);

	void save(TemplateConfigRequest request);

	void delete(String id);

	String render(String name, Map<?, ?> model);

}
