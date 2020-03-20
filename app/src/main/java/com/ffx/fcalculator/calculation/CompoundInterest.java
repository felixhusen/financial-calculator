package com.ffx.fcalculator.calculation;

/*
 * ======= DOCUMENTATION =========
 * p = current principal
 * c = annual addition
 * y = years to go
 * r = interest rate
 * n = interest time
 * additionAt = boolean that is obtained from user input
 * ================================ */


public class CompoundInterest extends Calculation {
    /* ========== DATA FIELD ========== */

    private double currentPrincipal, annualAddition, interestRate, totalFV, yearsToGrow, interestTime;
    private boolean additionAt;

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "COMPOUND_INTEREST";
    public static final String COLUMN_CURRENT_PRINCIPAL = "current_principal";
    public static final String COLUMN_ANNUAL_ADDITION = "annual_addition";
    public static final String COLUMN_YEARS_TO_GROW = "years_to_grow";
    public static final String COLUMN_INTEREST_RATE = "interest_rate";
    public static final String COLUMN_INTEREST_TIME = "interest_time";
    public static final String COLUMN_ADDITION_AT = "addition_at";
    public static final String CONSTRAINT_PKEY = "ci_pkey";
    public static final String CONSTRAINT_FKEY = "ci_fkey";
    // COMMANDS
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "( "
            + COLUMN_TITLE + " VARCHAR(200) NOT NULL, "
            + COLUMN_CURRENT_PRINCIPAL + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_ANNUAL_ADDITION + " DECIMAL(20,2) NULL, "
            + COLUMN_YEARS_TO_GROW + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_INTEREST_RATE + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_INTEREST_TIME + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_ADDITION_AT + " DECIMAL(1) NULL, "
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY("
            + COLUMN_TITLE + "),"
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY("
            + COLUMN_TITLE + ") REFERENCES " + Calculation.TABLE_NAME
            + "(" + COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */

    public CompoundInterest(
            double currentPrincipal,
            double annualAddition,
            double yearsToGrow,
            double interestRate,
            double interestTime,
            boolean additionAt
    ) {
        super();
        this.currentPrincipal = currentPrincipal;
        this.annualAddition = annualAddition;
        this.yearsToGrow = yearsToGrow;
        this.interestRate = interestRate / 100.0;
        this.interestTime = interestTime;
        this.additionAt = additionAt;
    }

    // COPY CONSTRUCTOR
    public CompoundInterest(CompoundInterest c) {
        this(c.getCurrentPrincipal(), c.getAnnualAddition(), c.getYearsToGrow(), c.getInterestRate(), c.getInterestTime(), c.getAdditionAt());
    }

    public CompoundInterest(
            double currentPrincipal,
            double yearsToGrow,
            double interestRate
    ) {
        super();
        this.currentPrincipal = currentPrincipal;
        this.yearsToGrow = yearsToGrow;
        this.interestRate = interestRate / 100.0;
    }

    /* ======== MUTATOR METHODS ======== */

    public void setCurrentPricipal(double currentPricipal) {
        this.currentPrincipal = currentPricipal;
    }

    public void setAnnualAddition(double annualAddition) {
        this.annualAddition = annualAddition;
    }

    public void setYearsToGrow(double yearsToGrow) {
        this.yearsToGrow = yearsToGrow;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate / 100.0;
    }

    public void setInterestTime(double interestTime) {
        this.interestTime = interestTime;
    }

    public void setAdditionAt(boolean additionAt) {
        this.additionAt = additionAt;
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

    public double getInterestRate() {
        return this.interestRate;
    }

    public double getInterestTime() {
        return this.interestTime;
    }

    public boolean getAdditionAt() {
        return this.additionAt;
    }

    // get future value method
    public double getFutureValue() {
        // check if the addition is at the beginning or end
        if (additionAt == true) {
            // the addition is at beginning
            totalFV = basicInvestment(currentPrincipal, interestRate / interestTime, yearsToGrow * interestTime, annualAddition / interestTime);
        } else {
            // the addition is at the end
            totalFV = bI2(currentPrincipal, interestRate / interestTime, yearsToGrow * interestTime, annualAddition / interestTime);
        }
        return totalFV;
    }

    public double getFutureValue(int years) {
        // check if the addition is at the beginning or end
        if (additionAt == true) {
            // the addition is at beginning
            totalFV = basicInvestment(currentPrincipal, interestRate / interestTime, years * interestTime, annualAddition / interestTime);
        } else {
            // the addition is at the end
            totalFV = bI2(currentPrincipal, interestRate / interestTime, years * interestTime, annualAddition / interestTime);
        }
        return totalFV;
    }

}
