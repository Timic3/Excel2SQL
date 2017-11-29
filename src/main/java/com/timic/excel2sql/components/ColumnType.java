package com.timic.excel2sql.components;

public enum ColumnType {
	VARCHAR("VARCHAR2", 32),
	NUMBER("NUMBER", 15),
	DATE("DATE");
	
	private final String oracleType;
	private final int oracleSize;
	
	private ColumnType(String oracleType) {
		this(oracleType, -1);
	}
	
	private ColumnType(String oracleType, int oracleSize) {
		this.oracleType = oracleType;
		this.oracleSize = oracleSize;
	}
	
	public String getOracleType() {
		return oracleType;
	}
	
	public int getOracleSize() {
		return oracleSize;
	}
}
