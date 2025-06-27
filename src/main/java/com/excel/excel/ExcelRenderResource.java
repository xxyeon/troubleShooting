package com.excel.excel;

import java.util.List;
import java.util.Map;


//엑셀로 렌더링한 헤더와  필드 이름(row)
public class ExcelRenderResource {
	// TODO dataFieldName -> excelHeaderName Map Abstraction
	private Map<String, String> excelHeaderNames;
	private List<String> dataFieldNames;

	public ExcelRenderResource(Map<String, String> excelHeaderNames, List<String> dataFieldNames) {
		this.excelHeaderNames = excelHeaderNames;
		this.dataFieldNames = dataFieldNames;
	}

	public String getExcelHeaderNames(String dataFieldName) {
		return excelHeaderNames.get(dataFieldName);
	}
	public List<String> getDataFieldNames() {
		return dataFieldNames;
	}

}
