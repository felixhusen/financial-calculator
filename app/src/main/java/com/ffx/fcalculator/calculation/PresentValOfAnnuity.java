package com.ffx.fcalculator.calculation;

public class PresentValOfAnnuity extends Annuity {

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "PRES_VAL_OF_ANNUITY";
    public static final String COLUMN_ANNUITY = "annual_payout";
    public static final String COLUMN_GROWTH_RATE = "growth_rate";
    public static final String COLUMN_YEARS_PAY_OUT = "years_pay_out";
    public static final String COLUMN_PAYOUT_AT = "payout_at";
    public static final String CONSTRAINT_PKEY = "pa_pkey";
    public static final String CONSTRAINT_FKEY = "pa_fkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + COLUMN_TITLE + " VARCHAR(200) NOT NULL, "
            + COLUMN_ANNUITY + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_GROWTH_RATE + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_YEARS_PAY_OUT + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_PAYOUT_AT + " DECIMAL(1) NOT NULL, "
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + COLUMN_TITLE + "), "
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY(" + COLUMN_TITLE + ") REFERENCES "
            + Calculation.TABLE_NAME + "(" + COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */

    public PresentValOfAnnuity(double annuity, double growthRate, double yearsPayOut, boolean payoutOptions) {
        super(0, growthRate, yearsPayOut, payoutOptions);
        this.annuity = annuity;
    }

    /* ======== MUTATOR METHODS ======== */

    public void setAnnuity(double annuity) {
        this.annuity = annuity;
    }

    /* ======= ACCESSOR METHODS ======== */

    @Override
    public double getAnnuity() {
        return this.annuity;
    }

    @Override
    public double getStartingPrincipal() {
        this.startingPrincipal = annuity * (Math.pow(1+growthRate,yearsPayOut) - 1)/(Math.pow(1+growthRate,yearsPayOut-1) * growthRate);
        if (!this.payoutOptions) {
            this.startingPrincipal =  annuity * (Math.pow(1+growthRate,yearsPayOut) - 1)/(Math.pow(1+growthRate,yearsPayOut) * growthRate);
        }
        return this.startingPrincipal;
    }

    public double getStartingPrincipal(int years) {
        this.startingPrincipal = annuity * (Math.pow(1+growthRate,years) - 1)/(Math.pow(1+growthRate,years-1) * growthRate);
        if (!this.payoutOptions) {
            this.startingPrincipal =  annuity * (Math.pow(1+growthRate,years) - 1)/(Math.pow(1+growthRate,years) * growthRate);
        }
        return this.startingPrincipal;
    }
}
