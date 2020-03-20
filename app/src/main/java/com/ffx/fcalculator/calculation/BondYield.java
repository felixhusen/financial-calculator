package com.ffx.fcalculator.calculation;

public class BondYield extends Calculation {

    /* ========== DATA FIELD ========== */

    private double currentPrice;
    private double perValue;
    private double couponRate;
    private double years;
    private double currentYield;
    private double yield;

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "BOND_YIELD";
    public static final String COLUMN_CURRENT_PRICE = "current_price";
    public static final String COLUMN_PER_VALUE = "per_value";
    public static final String COLUMN_COUPON_RATE = "coupon_rate";
    public static final String COLUMN_YEARS_TO_MATURITY = "years_to_maturity";
    public static final String CONSTRAINT_PKEY = "by_pkey";
    public static final String CONSTRAINT_FKEY = "by_fkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + COLUMN_TITLE + " VARCHAR(200) NOT NULL, "
            + COLUMN_CURRENT_PRICE + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_PER_VALUE + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_COUPON_RATE + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_YEARS_TO_MATURITY + " DECIMAL(5,2) NOT NULL, "
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + COLUMN_TITLE + "), "
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY(" + COLUMN_TITLE + ") REFERENCES "
            + Calculation.TABLE_NAME + "(" + COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */

    public BondYield(double currentPrice, double perValue, double couponRate, double years) {
        super();
        this.currentPrice = currentPrice;
        this.perValue = perValue;
        this.couponRate = couponRate / 100.0;
        this.years = years;
    }

    /* ======= ACCESSOR METHODS ======== */
    
    public double getCurrentPrice() {
        return this.currentPrice;
    }
    
    public double getPerValue() {
        return this.perValue;
    }
    
    public double getCouponRate() {
        return this.couponRate;
    }
    
    public double getYears() {
        return this.years;
    }
    
    public double getCurrentYield() {
        this.currentYield = couponRate * perValue * 1.0/currentPrice;
        return this.currentYield;
    }
    
    public double getYield() {
        this.yield = bondYTM(currentPrice, couponRate, perValue, years);
        return this.yield;
    }

    public double getYield(int years) {
        this.yield = bondYTM(currentPrice, couponRate, perValue, years);
        return this.yield;
    }

    /* ======== METHODS ======== */
    
    private double returnRate(double pv, double fv, double y) {
	return Math.pow(fv/pv,1.0/y) - 1.0;
    }
    
    private double fYTM(double z, double p, double c, double b, double y) {
	return (c + b)*Math.pow(z,y+1) - b*Math.pow(z,y) - (c+p)*z + p;
    }

    private double dfYTM(double z, double p, double c, double b, double y) {
	return (y+1)*(c + b)*Math.pow(z,y) - y*b*Math.pow(z,y - 1) - (c+p);
    }
    
    private double bondYTM(double p, double r, double b, double y) {
        double z = r;
        double c = r*b;
        double i;
        double E = .00001;

        if (r == 0)
        {
            return returnRate(p,b,y);
        }

        for (i = 0; i < 100; i++)
        {
            if (Math.abs(fYTM(z,p,c,b,y)) < E) break;

            while (Math.abs(dfYTM(z,p,c,b,y)) < E) z+= .1;
            z = z - (fYTM(z,p,c,b,y)/dfYTM(z,p,c,b,y));
        }
        if (Math.abs(fYTM(z,p,c,b,y)) >= E) return -1;  // error

        return (1/z) - 1;
    }

    /* ======== MUTATOR METHODS ======== */
    
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    
    public void setCouponRate(double couponRate) {
        this.couponRate = couponRate / 100.0;
    }
    
    public void setPerValue(double perValue) {
        this.perValue = perValue;
    }
    
    public void setYears(double years) {
        this.years = years;
    }
}
