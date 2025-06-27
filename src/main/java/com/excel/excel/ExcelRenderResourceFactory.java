package com.excel.excel;

import static com.excel.utils.SuperClassReflectionUtils.*;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.excel.annotations.ExcelColumn;
import com.excel.dto.BaseXlsx;

public class ExcelRenderResourceFactory {

	// 타입별로 render resource를 싱글턴처럼 캐싱
	private static final Map<Class<? extends BaseXlsx>, ExcelRenderResource> cachedResources = new HashMap<>();
	/**
	 *
	 * @param type Class type to be rendered
	 * @return ExcelRenderResource: 동일한 타입이면 재사용
	 */
	public static ExcelRenderResource prepareRenderResource(Class<? extends BaseXlsx> type) {

		if(cachedResources.containsKey(type)) {
			return cachedResources.get(type);
		}
		Map<String, String> headerNamesMap = new LinkedHashMap<>();
		List<String> fieldNames = new ArrayList<>();

		for(Field field : getAllFields(type)) {
			if (field.isAnnotationPresent(ExcelColumn.class)) {
				ExcelColumn annotation = field.getAnnotation(ExcelColumn.class);
				fieldNames.add(field.getName());
				headerNamesMap.put(field.getName(), annotation.headerName());
			}
		}

		ExcelRenderResource resource = new ExcelRenderResource(headerNamesMap, fieldNames);
		cachedResources.put(type, resource);
		return resource;
	}
}
