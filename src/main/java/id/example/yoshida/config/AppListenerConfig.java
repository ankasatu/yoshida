package id.example.yoshida.config;

import id.example.yoshida.utils.AppDestroyHandler;
import id.example.yoshida.utils.AppStartupHandler;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppListenerConfig {

	private final Set<AppStartupHandler> startupHandlerSet;

	private final Set<AppDestroyHandler> destroyHandlerSet;

	@PostConstruct
	public void onStartup() {
		if (startupHandlerSet.isEmpty()) {
			return;
		}

		int totalCount = startupHandlerSet.size();
		int errorCount = 0;

		log.info("found {} init item(s)", totalCount);

		for (var instance : startupHandlerSet) {
			var className = instance.getClass().getName();
			try {
				log.info("run - {}.onAppInit()", className);
				instance.onAppStartup();
			}
			catch (Exception e) {
				log.error("error run - {}.onAppInit() - reason: {}", className, e.getMessage(), e);
				errorCount++;
			}

		}

		log.info("init done. (success:{}, error:{})", totalCount - errorCount, errorCount);
	}

	@PreDestroy
	public void onDestroy() {
		if (startupHandlerSet.isEmpty()) {
			return;
		}

		int totalCount = startupHandlerSet.size();
		int errorCount = 0;

		log.info("destroying {} item(s)", totalCount);

		for (var instance : destroyHandlerSet) {
			var className = instance.getClass().getName();
			try {
				log.info("run - {}.onAppDestroy()", className);
				instance.onAppDestroy();
			}
			catch (Exception e) {
				log.error("error run - " + className + ".onAppDestroy() - reason: " + e.getMessage(), e);
				errorCount++;
			}

		}

		log.info("destroy done. (success:{}, error:{})", totalCount - errorCount, errorCount);

	}

}
