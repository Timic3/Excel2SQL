package com.timic.excel2sql.components;

import java.util.ArrayList;

public class Data {
	public ArrayList<String> data = new ArrayList<String>();
	
	public Data() {
		
	}
	
	public void addData(String data) {
		this.data.add(data);
	}
}
