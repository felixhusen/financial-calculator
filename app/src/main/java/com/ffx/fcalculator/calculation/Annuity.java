package com.ffx.fcalculator.calculation;

import java.util.ArrayList;

public class Annuity extends Calculation {
    /* ========== DATA FIELD ========== */

    protected double startingPrincipal;
    protected double growthRate;
    protected double yearsPayOut;
    protected boolean payoutOptions;
    protected double annuity;

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "ANNUITY";
    public static final String COLUMN_STARTING_PRINCIPAL = "starting_principal";
    public static final String COLUMN_GROWTH_RATE = "growth_rate";
    public static final String COLUMN_YEARS_PAY_OUT = "years_pay_out";
    public static final String COLUMN_PAYOUT_AT = "payout_at";
    public static final String CONSTRAINT_PKEY = "a_pkey";
    public static final String CONSTRAINT_FKEY = "a_fkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + Calculation.COLUMN_TITLE + " VARCHAR(200) NOT NULL, "
            + COLUMN_STARTING_PRINCIPAL + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_GROWTH_RATE + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_YEARS_PAY_OUT + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_PAYOUT_AT + " DECIMAL(1) NOT NULL,"
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + Calculation.COLUMN_TITLE + "),"
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY(" + Calculation.COLUMN_TITLE + ") REFERENCES "
            + Calculation.TABLE_NAME + "(" + Calculation.COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */

    public Annuity(double startingPrincipal, double growthRate, double yearsPayOut, boolean payoutOptions) {
        super();
        this.startingPrincipal = startingPrincipal;
        this.growthRate = growthRate / 100.0;
        this.yearsPayOut = yearsPayOut;
        this.payoutOptions = payoutOptions;
    }

    /* ======= ACCESSOR METHODS ======== */
    
    public double getStartingPrincipal() {
        return this.startingPrincipal;
    }
    
    public double getGrowthRate() {
        return this.growthRate;
    }
    
    public double getYearsPayOut() {
        return this.yearsPayOut;
    }
    
    public boolean getPayoutOptions() {
        return this.payoutOptions;
    }
    
    public double getAnnuity() {
        this.annuity = futureValue(startingPrincipal,growthRate,yearsPayOut-1)/geomSeries(1+growthRate,0,yearsPayOut-1);
        if (!this.payoutOptions) {
            this.annuity *= (1 + growthRate);
        }
        return this.annuity;
    }

    public double getAnnuity(int years) {
        this.annuity = futureValue(startingPrincipal,growthRate,years-1)/geomSeries(1+growthRate,0,years-1);
        if (!this.payoutOptions) {
            this.annuity *= (1 + growthRate);
        }
        return this.annuity;
    }

    public ArrayList<Double> getAnnuities(int years) {
        // create a new array
        ArrayList<Double> annuities = new ArrayList<>();
        // for each year add to the array
        for (int i = 1; i <= years; i++) {
            annuities.add(getAnnuity(i));
        }
        // return the array
        return annuities;
    }

    /* ======== MUTATOR METHODS ======== */

    public void setStartingPrincipal(double startingPrincipal) {
        this.startingPrincipal = startingPrincipal;
    }
    
    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate / 100.0;
    }
    
    public void setYearsPayOut(double yearsPayOut) {
        this.yearsPayOut = yearsPayOut;
    }
    
    public void setPayoutOptions(boolean payoutOptions) {
        this.payoutOptions = payoutOptions;       
    }
    
}
