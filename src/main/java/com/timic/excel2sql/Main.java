package com.timic.excel2sql;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.timic.excel2sql.components.Column;
import com.timic.excel2sql.components.Data;
import com.timic.excel2sql.components.Table;

public class Main {

	public static void main(String[] args) {
		InputStream excelFile;
		try {
			excelFile = new FileInputStream("E:\\School\\L4\\NRP\\test.xlsx");
			Workbook excel = new XSSFWorkbook(excelFile);
			ArrayList<Table> tables = new ArrayList<Table>();

			for (int i = 0; i < excel.getNumberOfSheets(); i++) {
				Sheet sheet = excel.getSheetAt(i);
				String sheetName = sheet.getSheetName();
				
				Table table = new Table(sheetName);
				tables.add(table);
				if (!sheetName.isEmpty()) {
					Iterator<Row> rows = sheet.rowIterator();
					
					boolean canGo = false;
					int realRowIndex = 0;
					while (rows.hasNext()) {
						Row row = rows.next();
						Iterator<Cell> cells = row.cellIterator();

						Data data = new Data();
						
						while (cells.hasNext()) {
							canGo = true;
							Cell cell = cells.next();
							String cellName;
							if (cell.getCellTypeEnum() == CellType.NUMERIC) {
								// Decimal support
								double numericCell = cell.getNumericCellValue();
								if (numericCell == Math.floor(numericCell)) {
									cellName = String.valueOf((int) cell.getNumericCellValue());
								} else {
									cellName = String.valueOf(cell.getNumericCellValue());
								}
							} else if (cell.getCellTypeEnum() == CellType.STRING) {
								cellName = cell.getStringCellValue();
							} else if (cell.getCellTypeEnum() == CellType.BOOLEAN) {
								cellName = String.valueOf(cell.getBooleanCellValue());
							} else {
								cellName = cell.getStringCellValue();
							}
							if (!cellName.isEmpty()) {
								if (realRowIndex == 0) {
									// Column names
									String columnType = sheet.getRow(row.getRowNum() + 1)
											.getCell(cell.getColumnIndex())
											.getStringCellValue();
									table.addColumn(cellName, columnType);
								} else if (realRowIndex == 1) {
									// Column types
									continue;
								} else {
									// Data
									data.addData(cellName);
								}
							}
						}
						if (canGo) {
							realRowIndex++;
							if (data.data.size() != 0) {
								table.rows.add(data);
							}
						}
					}
				}
			}
			excel.close();
			
			// Generate SQL code
			for (int tableIndex = 0; tableIndex < tables.size(); tableIndex++) {
				Table table = tables.get(tableIndex);
				String tableQuery = "";
				
				tableQuery += "CREATE TABLE " + table.getName() + " (\n";
				for (int columnIndex = 0; columnIndex < table.columns.size(); columnIndex++) {
					Column column = table.columns.get(columnIndex);
					tableQuery += "\t" + column.getName() + " " + column.getType();
					
					if (column.getSize() != -1) {
						tableQuery += "(" + column.getSize() + ")";
					}
					if (columnIndex < table.columns.size() - 1) {
						tableQuery += ",\n";
					}
				}
				tableQuery += "\n);";
				System.out.println(tableQuery);
				
				
				System.out.println();
				
				for (int rowIndex = 0; rowIndex < table.rows.size(); rowIndex++) {
					String insertQuery = "INSERT INTO " + table.getName() + "\n(";
					for (int columnIndex = 0; columnIndex < table.columns.size(); columnIndex++) {
						Column column = table.columns.get(columnIndex);
						insertQuery += column.getName();
						
						if (columnIndex < table.columns.size() - 1) {
							insertQuery += ", ";
						}
					}
					insertQuery += ")";
					insertQuery += " VALUES\n(";
					Data data = table.rows.get(rowIndex);
					for (int dataIndex = 0; dataIndex < data.data.size(); dataIndex++) {
						String rowData = data.data.get(dataIndex);
						
						if (table.columns.get(dataIndex).isNumeric()) {
							insertQuery += rowData;
						} else {
							insertQuery += "'" + rowData + "'";
						}
						if (dataIndex < table.columns.size() - 1) {
							insertQuery += ", ";
						}
					}
					insertQuery += ");";
					System.out.println(insertQuery);
					System.out.println();
				}
				
				System.out.println();
				System.out.println();
				System.out.println();
			}
		} catch (FileNotFoundException e) {
			System.err.println("File not found");
		} catch (IOException e) {
			System.err.println("Unknown IO error");
		}
	}

}
