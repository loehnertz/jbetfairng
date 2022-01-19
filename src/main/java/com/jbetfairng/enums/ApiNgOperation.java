package com.jbetfairng.enums;

public enum ApiNgOperation {
	LISTEVENTTYPES("listEventTypes"),
	LISTCOMPETITIONS("listCompetitions"),
	LISTTIMERANGES("listTimeRanges"),
	LISTEVENTS("listEvents"),
	LISTMARKETTYPES("listMarketTypes"),
	LISTCOUNTRIES("listCountries"),
	LISTVENUES("listVenues"),
	LISTMARKETCATALOGUE("listMarketCatalogue"),
	LISTMARKETBOOK("listMarketBook"),
	PLACORDERS("placeOrders");

	private final String operationName;

	ApiNgOperation(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationName() {
		return operationName;
	}


}
