package com.ffx.fcalculator.calculation;

public class ReturnRate extends Calculation {

    /* ========== DATA FIELD ========== */

    private double presentValue;
    private double futureValue;
    private double years;
    private double returnRate;

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "RETURN_RATE";
    public static final String COLUMN_PRESENT_VALUE = "present_value";
    public static final String COLUMN_FUTURE_VALUE = "future_valie";
    public static final String COLUMN_YEARS = "years";
    public static final String CONSTRAINT_PKEY = "rr_pkey";
    public static final String CONSTRAINT_FKEY = "rr_fkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + Calculation.COLUMN_TITLE + " VARCHAR(200) NOT NULL, "
            + COLUMN_PRESENT_VALUE + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_FUTURE_VALUE + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_YEARS + " DECIMAL(5,2) NOT NULL, "
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + Calculation.COLUMN_TITLE + "),"
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY(" + Calculation.COLUMN_TITLE + ") REFERENCES "
            + Calculation.TABLE_NAME + "(" + Calculation.COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */

    public ReturnRate(double presentValue, double futureValue, double years) {
        super();
        this.presentValue = presentValue;
        this.futureValue = futureValue;
        this.years = years;
    }
    
    public ReturnRate(ReturnRate rr) {
        this(rr.presentValue, rr.futureValue, rr.years);
    }

    /* ======= ACCESSOR METHODS ======== */
    
    public double getPresentValue() {
        return this.presentValue;
    }
    
    public double getFutureValue() {
        return this.futureValue;
    }
    
    public double getYears() {
        return this.years;
    }
    
    public double getReturnRate() {
        this.returnRate = Math.pow(futureValue/presentValue,1.0/years) - 1.0;
        return this.returnRate;
    }

    public double getReturnRate(int years) {
        this.returnRate = Math.pow(futureValue/presentValue,1.0/years) - 1.0;
        return this.returnRate;
    }

    /* ======== MUTATOR METHODS ======== */
    
    public void setPresentValue(double presentValue) {
        this.presentValue = presentValue;
    }
    
    public void setFutureValue(double futureValue) {
        this.futureValue = futureValue;
    }
    
    public void setYears(double years) {
        this.years = years;
    }
            
}
