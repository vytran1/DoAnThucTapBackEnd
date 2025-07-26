package com.thuctap.account.analysis;

public enum AnalysisPeriodType {
		WEEK(7),
	    MONTH(30),
	    YEAR(365);

	    private final int days;

	    AnalysisPeriodType(int days) {
	        this.days = days;
	    }

	    public int getDays() {
	        return days;
	    }
	    
	    
}
