package com.thuctap.utility;

import java.io.InputStream;

import io.jsonwebtoken.io.IOException;

public interface QuoteFileReader {
//	QuoteData readQuoteData(InputStream inputStream) throws IOException, IllegalArgumentException;
	public QuoteData readQuoteAcceptData(InputStream inputStream);
	public QuoteData readQuoteRejectData(InputStream inputStream);
}
