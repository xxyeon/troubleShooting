package com.excel.excel;

import static com.excel.utils.SuperClassReflectionUtils.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.scheduling.annotation.Async;

import com.excel.dto.BaseXlsx;

public class SimpleExcelFile<T extends BaseXlsx> {
	//제네릭을 사용한 이유는 엑셀을 렌더링해야하는 대상의 객체가 달라지므로 이를 동적 타입으로 하기 위해서
	private static final SpreadsheetVersion supplyExcelVersion = SpreadsheetVersion.EXCEL2007;
	private static final int ROW_START_INDEX = 0;
	private static final int COLUM_START_INDEX = 0;

	private SXSSFWorkbook wb;
	private Sheet sheet;
	private ExcelRenderResource resource;

	//SimpleExcelMetaData는 엑셀을 그릴때 필요한 일종의 메타데이터를 보관하는 객체입니다. 엑셀에 그려져야 하는 필드들의 이름(필드명)과 엑셀에 보여져야 하는 헤더 이름을 맵으로 관리하고 있죠.
	public SimpleExcelFile(List<T> data, Class<? extends BaseXlsx> type) {
		validateMaxRow(data);
		this.wb = new SXSSFWorkbook();
		this.resource = ExcelRenderResourceFactory.prepareRenderResource(type);
		renderExcel(data);
	}

	private void validateMaxRow(List<T> data) {
		int maxRows = supplyExcelVersion.getMaxRows();
		if (data.size() > maxRows) {//최대 행 개수모다 크면 예외
			throw new IllegalArgumentException(
				String.format("This concrete ExcelFile does not support over %s rows", maxRows));
		}
	}

	//이 부분이
	private void renderExcel(List<T> data) {
		sheet = wb.createSheet();
		renderHeadersWithNewSheet(sheet, ROW_START_INDEX, COLUM_START_INDEX);

		if (data.isEmpty()) {
			return;
		}

		int rowIndex = ROW_START_INDEX + 1; //header가 ROW_START_INDEX부터  시작하므로
		for (Object renderData : data) {
			renderBody(renderData, rowIndex++);
		}
	}

	/**
	 *
	 * @param data : 엑셀로 생성할 정보가 담긴 객체
	 * @param rowIndex
	 */
	private void renderBody(Object data, int rowIndex) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = COLUM_START_INDEX;
		//리플렉션으로 body 생성

		for(String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			try {
				//.getClass() -> 현재 참조하고 있는 클래스를 확인할 수 있는 메서드
				Field field = getField(data.getClass(), (dataFieldName));
				field.setAccessible(true);
				Object cellValue = field.get(data);
				renderCellValue(cell, cellValue);
			}catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}
	protected void renderHeadersWithNewSheet(Sheet sheet, int rowIndex, int columnStartIndex) {
		Row row = sheet.createRow(rowIndex);
		int columnIndex = columnStartIndex;
		for (String dataFieldName : resource.getDataFieldNames()) {
			Cell cell = row.createCell(columnIndex++);
			cell.setCellValue(resource.getExcelHeaderNames(dataFieldName));
		}
	}

	protected void renderCellValue(Cell cell, Object cellValue) {
		//현재 엑셀파일에서는 모두 String으로 처리하고 있음
		cell.setCellValue(cellValue == null ? "" : cellValue.toString());
	}

	@Async
	public void write(OutputStream stream) throws IOException {
		wb.write(stream);
		wb.close();
		wb.dispose();
		stream.close();
	}

}
