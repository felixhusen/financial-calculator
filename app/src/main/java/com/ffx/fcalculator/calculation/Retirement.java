package com.ffx.fcalculator.calculation;

public class Retirement extends Calculation {
    /* ========== DATA FIELD ========== */

    private double currentPrincipal;
    // pre-retirement:
    private double annualAddition;
    private double yearsToGrow;
    private double preGrowthRate;
    // in retirement:
    private double yearsToPayOut;
    private double inGrowthRate;
    // result:
    private double annualRetirementIncome;

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "RETIREMENT";
    public static final String COLUMN_CURRENT_PRINCIPAL = "current_principal";
    public static final String COLUMN_ANNUAL_ADDITION = "annual_addition";
    public static final String COLUMN_YEARS_TO_GROW = "years_to_grow";
    public static final String COLUMN_PRE_GROWTH_RATE = "pre_growth_rate";
    public static final String COLUMN_YEARS_TO_PAYOUT = "years_to_payout";
    public static final String COLUMN_IN_GROWTH_RATE = "in_growth_rate";
    public static final String CONSTRAINT_PKEY = "r_pkey";
    public static final String CONSTRAINT_FKEY = "r_fkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + COLUMN_TITLE + " VARCHAR(200) NOT NULL, "
            + COLUMN_CURRENT_PRINCIPAL + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_ANNUAL_ADDITION + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_YEARS_TO_GROW + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_PRE_GROWTH_RATE + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_YEARS_TO_PAYOUT + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_IN_GROWTH_RATE + " DECIMAL(5,2) NOT NULL, "
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + COLUMN_TITLE + "), "
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY(" + COLUMN_TITLE + ") REFERENCES "
            + Calculation.TABLE_NAME + "(" + COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */

    public Retirement(double currentPrincipal, double annualAddition, double yearsToGrow, double preGrowthRate, double yearsToPayOut, double inGrowthRate) {
        super();
        this.currentPrincipal = currentPrincipal;
        this.annualAddition = annualAddition;
        this.yearsToGrow = yearsToGrow;
        this.preGrowthRate = preGrowthRate / 100.0;
        this.yearsToPayOut = yearsToPayOut;
        this.inGrowthRate = inGrowthRate / 100.0;
    }

    /* ======= ACCESSOR METHODS ======== */
    
    public double getCurrentPrincipal() {
        return this.currentPrincipal;
    }
    
    public double getAnnualAddition() {
        return this.annualAddition;
    }
    
    public double getYearsToGrow() {
        return this.yearsToGrow;
    }
    
    public double getPreGrowthRate() {
        return this.preGrowthRate;
    }
    
    public double getYearsToPayOut() {
        return this.yearsToPayOut;
    }
    
    public double getInGrowthRate() {
        return this.inGrowthRate;
    }
    
    public double getAnnualRetirementIncome() {
        this.annualRetirementIncome = annuityPayout(basicInvestment(currentPrincipal, preGrowthRate, yearsToGrow, annualAddition), inGrowthRate, yearsToPayOut);
        return this.annualRetirementIncome;
    }

    public double getAnnualRetirementIncome(int years) {
        this.annualRetirementIncome = annuityPayout(basicInvestment(currentPrincipal, preGrowthRate, years, annualAddition), inGrowthRate, yearsToPayOut);
        return this.annualRetirementIncome;
    }

    /* ======== METHODS ======== */

    private double annuityPayout(double p, double r, double y) {
	    return futureValue(p,r,y-1)/geomSeries(1+r,0,y-1);
    }

    /* ======== MUTATOR METHODS ======== */
    
    public void setCurrentPrincipal(double currentPrincipal) {
        this.currentPrincipal = currentPrincipal;
    }
    
    public void setAnnualAddition(double annualAddition) {
        this.annualAddition = annualAddition;
    }
    
    public void setYearsToGrow(double yearsToGrow) {
        this.yearsToGrow = yearsToGrow;
    }
    
    public void setPreGrowthRate(double preGrowthRate) {
        this.preGrowthRate = preGrowthRate / 100.0;
    }
    
    public void setInGrowthRate(double inGrowthRate) {
        this.inGrowthRate = inGrowthRate / 100.0;
    }
    
    public void setYearsToPayOut(double yearsToPayOut) {
        this.yearsToPayOut = yearsToPayOut;
    }
}
