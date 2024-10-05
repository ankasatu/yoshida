package id.example.yoshida.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import id.example.yoshida.exception.ParseException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommonUtils {

	private final ConversionService conversionService;

	private final ObjectMapper mapper;

	public <T> T fromJson(String jsonStr, Class<T> valueType) {
		try {
			return mapper.readValue(jsonStr, valueType);
		}
		catch (JsonProcessingException e) {
			throw new ParseException("cannot parse json string: " + e.getMessage(), e);
		}
	}

	public String toJson(Object object) {
		try {
			return mapper.writeValueAsString(object);
		}
		catch (JsonProcessingException e) {
			throw new ParseException("cannot parse json string: " + e.getMessage(), e);
		}
	}

	public <T> T convert(@Nullable Object source, Class<T> targetType) {
		return conversionService.convert(source, targetType);
	}

}
