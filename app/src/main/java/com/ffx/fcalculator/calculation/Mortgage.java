package com.ffx.fcalculator.calculation;

public class Mortgage extends Calculation {

    /* ========== DATA FIELD ========== */

    private double loanAmount;
    private double morgageRate;
    private double yearsToPay;
    private double monthlyPayment;

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "MORTGAGE";
    public static final String COLUMN_LOAN_AMOUNT = "loan_amount";
    public static final String COLUMN_MORTGAGE_RATE = "mortgage_rate";
    public static final String COLUMN_YEARS_TO_PAY = "years_to_pay";
    public static final String CONSTRAINT_PKEY = "m_pkey";
    public static final String CONSTRAINT_FKEY = "m_fkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + COLUMN_TITLE + " VARCHAR(200) NOT NULL, "
            + COLUMN_LOAN_AMOUNT + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_MORTGAGE_RATE + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_YEARS_TO_PAY + " DECIMAL(5,2) NOT NULL, "
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + COLUMN_TITLE + "), "
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY(" + COLUMN_TITLE + ") REFERENCES "
            + Calculation.TABLE_NAME + "(" + COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */

    public Mortgage(double loanAmount, double morgageRate, double yearsToPay) {
        super();
        this.loanAmount = loanAmount;
        this.morgageRate = morgageRate / 100.0;
        this.yearsToPay = yearsToPay;
    }

    /* ======= ACCESSOR METHODS ======== */
    
    public double getLoanAmount() {
        return this.loanAmount;
    }
    
    public double getMorgageRate() {
        return this.morgageRate;
    }
    
    public double getYearsToPay() {
        return this.yearsToPay;  
    }
    
    public double getMonthlyPayment() {
        this.monthlyPayment = morgagePayment(loanAmount, morgageRate/12, yearsToPay*12);
        return this.monthlyPayment;
    }

    public double getMonthlyPayment(int years) {
        this.monthlyPayment = morgagePayment(loanAmount, morgageRate/12, years*12);
        return this.monthlyPayment;
    }

    /* ======== METHODS ======== */
    
    private double morgagePayment(double loan, double rate, double years) {
        return futureValue(loan,rate,years)/geomSeries(1+rate,0,years-1);
    }

    /* ======== MUTATOR METHODS ======== */

    public void setLoanAmount(double loanAmount) {
        this.loanAmount = loanAmount;
    }
    
    public void setMorgageRate(double morgageRate) {
        this.morgageRate = morgageRate / 100.0;
    }
    
    public void setYearsToPay(double yearsToPay) {
        this.yearsToPay = yearsToPay;
    }

    
}
