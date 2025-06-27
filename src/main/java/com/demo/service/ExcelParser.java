package com.demo.service;

import com.demo.vo.ExcelParseResult;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ExcelParser {

    public ExcelParseResult parseExcel(InputStream inputStream, String fileName) throws Exception {
        Workbook workbook;
        if (fileName.endsWith(".xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (fileName.endsWith(".xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileName);
        }

        var sheetDataMap = new LinkedHashMap<String, ExcelParseResult.SheetData>();

        for (var i = 0; i < workbook.getNumberOfSheets(); i++) {
            var sheet = workbook.getSheetAt(i);
            var sheetName = sheet.getSheetName();
            var rowIterator = sheet.iterator();

            var headers = new ArrayList<String>();
            var dataList = new ArrayList<Map<String, Object>>();

            if (!rowIterator.hasNext()) continue;

            //读取表头
            var headerRow = rowIterator.next();
            for (var cell : headerRow) {
                headers.add(getCellAsString(cell));
            }

            //读取数据行
            while (rowIterator.hasNext()) {
                var row = rowIterator.next();
                var rowData = new LinkedHashMap<String, Object>();
                var isRowEmpty = true;

                for (var j = 0; j < headers.size(); j++) {
                    var cell = row.getCell(j);
                    var value = getCellValue(cell);

                    if (value != null && !(value instanceof String && ((String) value).trim().isEmpty())) {
                        isRowEmpty = false;
                    }

                    rowData.put(headers.get(j), value);
                }

                if (!isRowEmpty) {
                    dataList.add(rowData);
                }
            }

            sheetDataMap.put(sheetName, new ExcelParseResult.SheetData(headers, dataList));
        }

        workbook.close();
        return new ExcelParseResult(sheetDataMap);
    }

    //保留类型地获取值
    private static Object getCellValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue();  //返回 Date 类型
                } else {
                    return cell.getNumericCellValue();  //返回 Double 类型
                }
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case FORMULA:
                try {
                    return cell.getNumericCellValue();
                } catch (Exception e) {
                    return cell.getStringCellValue();
                }
            case BLANK:
            default:
                return null;
        }
    }

    //用于读取表头，不强制保留类型
    private static String getCellAsString(Cell cell) {
        if (cell == null) return "";
        cell.setCellType(CellType.STRING);
        return cell.getStringCellValue().trim();
    }
}
