package com.timic.excel2sql.components;

import java.util.ArrayList;

public class Table {
	private String name;
	public ArrayList<Column> columns = new ArrayList<Column>();
	public ArrayList<Data> rows = new ArrayList<Data>();
	
	public Table(String name) {
		this.name = name;
	}
	
	public void addColumn(String columnName, String columnType) {
		ColumnType columnTypeEnum = ColumnType.VARCHAR;
		if (columnType.equals("NUMERIC")) {
			columnTypeEnum = ColumnType.NUMBER;
		} else if(columnType.equals("DATE")) {
			columnTypeEnum = ColumnType.DATE;
		}
		this.columns.add(new Column(columnName, columnTypeEnum));
	}

	public String getName() {
		return name;
	}
}
