package com.org.great.world.data;

import java.util.ArrayList;
import java.util.List;

public class BaseCatalogPojo {

	private String status;

	private ArrayList<CatalogPojo> message;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<CatalogPojo> getMessage() {
		return message;
	}

	public void setMessage(ArrayList<CatalogPojo> message) {
		this.message = message;
	}


}
