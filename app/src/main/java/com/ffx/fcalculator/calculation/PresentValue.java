package com.ffx.fcalculator.calculation;

public class PresentValue extends Calculation {
    /* ========== DATA FIELD ========== */

    private double futureValue;
    private double years;
    private double discountRate;
    private double presentValue;

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "PRESENT_VALUE";
    public static final String COLUMN_FUTURE_VALUE = "future_value";
    public static final String COLUMN_YEARS = "years";
    public static final String COLUMN_DISCOUNT_RATE = "discount_rate";
    public static final String CONSTRAINT_PKEY = "pv_pkey";
    public static final String CONSTRAINT_FKEY = "pv_fkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + Calculation.COLUMN_TITLE + " VARCHAR(200) " + " NOT NULL, "
            + COLUMN_FUTURE_VALUE + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_YEARS + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_DISCOUNT_RATE + " DECIMAL(5,2) NOT NULL, "
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + Calculation.COLUMN_TITLE + "), "
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY(" + Calculation.COLUMN_TITLE + ") REFERENCES " +
            Calculation.TABLE_NAME  + "(" + Calculation.COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */

    public PresentValue(double futureValue, double years, double discountRate) {
        super();
        this.futureValue = futureValue;
        this.years = years;
        this.discountRate = discountRate / 100.0;
    }
    
    public PresentValue(PresentValue pv) {
        this(pv.futureValue, pv.years, pv.discountRate);
    }

    /* ======= ACCESSOR METHODS ======== */
    
    public double getFutureValue() {
        return this.futureValue;
    }
    
    public double getYears() {
        return this.years;
    }
    
    public double getDiscountRate() {
        return this.discountRate;
    }
    
    public double getPresentValue() {
        this.presentValue = this.futureValue/Math.pow(1+this.discountRate,this.years);
        return this.presentValue;
    }

    public double getPresentValue(int years) {
        this.presentValue = this.futureValue/Math.pow(1+this.discountRate,years);
        return this.presentValue;
    }

    /* ======== MUTATOR METHODS ======== */
    
    public void setFutureValue(double futureValue) {
        this.futureValue = futureValue;
    }
    
    public void setYears(double years) {
        this.years = years;
    }
    
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate / 100.0;
    }
    
}
