package com.org.great.world.data;

import java.io.Serializable;

public class CardType implements Serializable
{
	private static final long serialVersionUID = 1L;
	int cardTypeId;
	String typeName;
	public int getCardTypeId() {
		return cardTypeId;
	}
	public void setCardTypeId(int cardTypeId) {
		this.cardTypeId = cardTypeId;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
