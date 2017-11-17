package com.timic.excel2sql.components;

public class Column {
	String name;
	ColumnType type;
	
	public Column(String name, ColumnType type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return type.getOracleType();
	}
	
	public int getSize() {
		return type.getOracleSize();
	}
	
	public boolean isNumeric() {
		return type == ColumnType.NUMERIC;
	}
}
