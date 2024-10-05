package id.example.yoshida.utils;

import freemarker.cache.TemplateLoader;
import id.example.yoshida.service.TemplateConfigService;
import id.example.yoshida.service.dto.TemplateConfig;
import lombok.AllArgsConstructor;

import java.io.Reader;
import java.io.StringReader;
import java.time.ZoneOffset;

@AllArgsConstructor
public class TemplateConfigServiceTemplateLoader implements TemplateLoader {

	private final TemplateConfigService templateConfigService;

	@Override
	public Object findTemplateSource(String s) {
		return templateConfigService.getSourceConfig(s);
	}

	@Override
	public long getLastModified(Object o) {
		return ((TemplateConfig) o).getUpdatedTs().toEpochSecond(ZoneOffset.UTC);
	}

	@Override
	public Reader getReader(Object o, String s) {
		return new StringReader(((TemplateConfig) o).getSource());
	}

	@Override
	public void closeTemplateSource(Object o) {
		// Noncompliance - method is empty
	}

}
