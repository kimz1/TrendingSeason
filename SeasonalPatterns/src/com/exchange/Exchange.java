package com.exchange;

public abstract class Exchange {
	
	private String exchangeName;
	private String ticker;

	public String getMarketName() {
		return exchangeName;
	}

	public String getTicker() {
		return ticker;
	}

	protected void setMarketName(String exchangeName) {
		this.exchangeName = exchangeName;
	}
	
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	static public Exchange getExchange(String exchange) {
		String exchangeLowerCase = exchange.toUpperCase();
		
		switch(exchangeLowerCase) {
		case "CBOT":
			return CBOT.getInstance();
		default:
			return null;
		}
	}
	
	@Override
	public String toString() {
		return this.exchangeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((exchangeName == null) ? 0 : exchangeName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exchange other = (Exchange) obj;
		if (exchangeName == null) {
			if (other.exchangeName != null)
				return false;
		} else if (!exchangeName.equals(other.exchangeName))
			return false;
		return true;
	}
}
