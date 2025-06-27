package com.demo.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ExcelParseResult {
    // Sheet 名 → 表头 + 数据
    private Map<String, SheetData> sheetDataMap;

    public ExcelParseResult(Map<String, SheetData> sheetDataMap) {
        this.sheetDataMap = sheetDataMap;
    }

    public Map<String, SheetData> getSheetDataMap() {
        return sheetDataMap;
    }

    public static class SheetData {
        private List<String> headers;
        private List<Map<String, Object>> data;

        public SheetData(List<String> headers, List<Map<String, Object>> data) {
            this.headers = headers;
            this.data = data;
        }

        public List<String> getHeaders() {
            return headers;
        }

        public List<Map<String, Object>> getData() {
            return data;
        }
    }
}
