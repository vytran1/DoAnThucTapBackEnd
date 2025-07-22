package com.thuctap.utility;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteData {
	@JsonProperty("quote_information")
	private QuoteInformation quoteInformation;
	@JsonProperty("quote_items")
    private List<QuoteItem> quoteItems;
    
    
    
	public QuoteData(QuoteInformation quoteInformation, List<QuoteItem> quoteItems) {
		super();
		this.quoteInformation = quoteInformation;
		this.quoteItems = quoteItems;
	}
	public QuoteInformation getQuoteInformation() {
		return quoteInformation;
	}
	public void setQuoteInformation(QuoteInformation quoteInformation) {
		this.quoteInformation = quoteInformation;
	}
	public List<QuoteItem> getQuoteItems() {
		return quoteItems;
	}
	public void setQuoteItems(List<QuoteItem> quoteItems) {
		this.quoteItems = quoteItems;
	}
    
    
}
