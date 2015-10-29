package com.org.great.world.data;

import java.util.ArrayList;
import java.util.List;

public class BaseCatalogPojo extends BasePojo{
	private ArrayList<CatalogPojo> message;
	public ArrayList<CatalogPojo> getMessage() {
		return message;
	}
	public void setMessage(ArrayList<CatalogPojo> message) {
		this.message = message;
	}
}
