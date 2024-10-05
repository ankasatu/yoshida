package id.example.yoshida.config;

import freemarker.template.Configuration;
import id.example.yoshida.service.TemplateConfigService;
import id.example.yoshida.utils.TemplateConfigServiceTemplateLoader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.time.Duration;

@org.springframework.context.annotation.Configuration
public class TemplateUtilsConfig {

	@Bean
	@Primary
	public Configuration defaultTemplateUtils(@Qualifier("freeMarkerConfiguration") Configuration defaultConfig,
			TemplateConfigService templateConfigService) {
		var config = (Configuration) defaultConfig.clone();

		// template source
		config.setTemplateLoader(new TemplateConfigServiceTemplateLoader(templateConfigService));

		// prevent trailed localized name when load template.
		config.setLocalizedLookup(false);

		// cache store
		// config.setCacheStorage(new freemarker.cache.MruCacheStorage(20, 250))

		// cache age
		config.setTemplateUpdateDelayMilliseconds(Duration.ofMinutes(1).toMillis());

		return config;
	}

}
