package com.ffx.fcalculator.calculation;

import android.util.Log;

public class PropertyCashflow extends Calculation {

    // basic requirements
    private double propertyPrice, propertyDeposit, rentPerWeek, mortgageInterestRate, waterRate;
    private boolean waterRateOption;
    private double council;
    private boolean councilOption;
    private double additionalExpenses;
    private boolean expensesOption;
    private double landTax;
    private boolean landTaxOption;
    private double annualInsuranceLandlord;
    private double annualPropertyFees;

    // hidden information
    private double netRent = 0;
    private double loanAmount, annualMortgageInterest;

    // total variables
    private double totalExpenses = 0;
    private double totalIncomes = 0;

    // true == annual
    // false == quarterly

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "PROPERTY_CASHFLOW";
    public static final String COLUMN_PROPERTY_PRICE = "property_price";
    public static final String COLUMN_PROPERTY_DEPOSIT = "property_deposit";
    public static final String COLUMN_RENT_PER_WEEK = "rent_per_week";
    public static final String COLUMN_MORTGAGE_INTEREST_RATE = "mortgage_interest_rate";
    public static final String COLUMN_WATER_RATE = "water_rate";
    public static final String COLUMN_WATER_RATE_OPTION = "water_rate_option";
    public static final String COLUMN_COUNCIL = "council";
    public static final String COLUMN_COUNCIL_OPTION = "council_option";
    public static final String COLUMN_ADDITIONAL_EXPENSES = "additional_expenses";
    public static final String COLUMN_EXPENSES_OPTION = "additional_expenses_option";
    public static final String COLUMN_LAND_TAX = "land_tax";
    public static final String COLUMN_LAND_TAX_OPTION = "land_tax_option";
    public static final String COLUMN_ANNUAL_INSURANCE_LANDLORD = "annual_insurance_landlord";
    public static final String COLUMN_ANNUAL_PROPERTY_FEES = "annual_property_fees";
    public static final String CONSTRAINT_PKEY = "pc_pkey";
    public static final String CONSTRAINT_FKEY = "pc_fkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + Calculation.COLUMN_TITLE + " VARCHAR(200) " + " NOT NULL, "
            + COLUMN_PROPERTY_PRICE + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_PROPERTY_DEPOSIT + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_RENT_PER_WEEK + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_MORTGAGE_INTEREST_RATE + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_WATER_RATE + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_WATER_RATE_OPTION + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_COUNCIL + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_COUNCIL_OPTION + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_ADDITIONAL_EXPENSES + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_EXPENSES_OPTION + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_LAND_TAX + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_LAND_TAX_OPTION + " DECIMAL(5,2) NOT NULL, "
            + COLUMN_ANNUAL_INSURANCE_LANDLORD + " DECIMAL(20,2) NOT NULL, "
            + COLUMN_ANNUAL_PROPERTY_FEES + " DECIMAL(5,2) NOT NULL, "

            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + Calculation.COLUMN_TITLE + "), "
            + "CONSTRAINT " + CONSTRAINT_FKEY + " FOREIGN KEY(" + Calculation.COLUMN_TITLE + ") REFERENCES " +
            Calculation.TABLE_NAME  + "(" + Calculation.COLUMN_TITLE + "));";

    public PropertyCashflow() {

    }

    public PropertyCashflow(double propertyPrice, double propertyDeposit, double rentPerWeek,
                            double mortgageInterestRate, double waterRate, boolean
                                    waterRateOption, double council, boolean councilOption,
                            double additionalExpenses, boolean expensesOption, double landTax,
                            boolean landTaxOption, double annualInsuranceLandlord, double
                                    annualPropertyFees) {
        super();
        this.propertyPrice = propertyPrice;
        this.propertyDeposit = propertyDeposit;
        this.rentPerWeek = rentPerWeek;
        this.mortgageInterestRate = mortgageInterestRate;

        this.waterRateOption = waterRateOption;

        if (waterRateOption == true) {
            this.waterRate = waterRate;
        } else {
            this.waterRate = 4 * waterRate;
        }

        this.councilOption = councilOption;

        if (councilOption == true) {
            this.council = council;
        } else {
            this.council = 4 * council;
        }

        this.expensesOption = expensesOption;
        if (expensesOption == true) {
            this.additionalExpenses = additionalExpenses;
        } else {
            this.additionalExpenses = 4 * additionalExpenses;
        }

        this.landTaxOption = landTaxOption;
        if (landTaxOption == true) {
            this.landTax = landTax;
        } else {
            this.landTax = 4 * landTax;
        }

        this.annualInsuranceLandlord = annualInsuranceLandlord;
        this.annualPropertyFees = annualPropertyFees;
        // calling calculateRequiredData to calculate hidden information
        calculateRequiredData();
    }

    private void calculateRequiredData() {

        this.netRent = (rentPerWeek * 52) - ((rentPerWeek * 52) * annualPropertyFees);
        Log.i("Net Rent", String.valueOf(netRent));
        this.loanAmount = propertyPrice - propertyDeposit;
        this.annualMortgageInterest = mortgageInterestRate * loanAmount;

        // calculate total income and expenses
        this.totalIncomes = netRent;
        this.totalExpenses = annualMortgageInterest + council + waterRate + landTax + annualInsuranceLandlord;
    }

    public double getPropertyPrice() {
        return propertyPrice;
    }

    public void setPropertyPrice(double propertyPrice) {
        this.propertyPrice = propertyPrice;
    }

    public double getPropertyDeposit() {
        return propertyDeposit;
    }

    public void setPropertyDeposit(double propertyDeposit) {
        this.propertyDeposit = propertyDeposit;
    }

    public double getRentPerWeek() {
        return rentPerWeek;
    }

    public void setRentPerWeek(double rentPerWeek) {
        this.rentPerWeek = rentPerWeek;
    }

    public double getMortgageInterestRate() {
        return mortgageInterestRate;
    }

    public void setMortgageInterestRate(double mortgageInterestRate) {
        this.mortgageInterestRate = mortgageInterestRate;
    }

    public double getWaterRate() {
        return waterRate;
    }

    public void setWaterRate(double waterRate) {
        this.waterRate = waterRate;
    }

    public boolean isWaterRateOption() {
        return waterRateOption;
    }

    public void setWaterRateOption(boolean waterRateOption) {
        this.waterRateOption = waterRateOption;
    }

    public double getCouncil() {
        return council;
    }

    public void setCouncil(double council) {
        this.council = council;
    }

    public boolean isCouncilOption() {
        return councilOption;
    }

    public void setCouncilOption(boolean councilOption) {
        this.councilOption = councilOption;
    }

    public double getAdditionalExpenses() {
        return additionalExpenses;
    }

    public void setAdditionalExpenses(double additionalExpenses) {
        this.additionalExpenses = additionalExpenses;
    }

    public boolean isExpensesOption() {
        return expensesOption;
    }

    public void setExpensesOption(boolean expensesOption) {
        this.expensesOption = expensesOption;
    }

    public double getLandTax() {
        return landTax;
    }

    public void setLandTax(double landTax) {
        this.landTax = landTax;
    }

    public boolean isLanTaxOption() {
        return landTaxOption;
    }

    public void setLanTaxOption(boolean lanTaxOption) {
        this.landTaxOption = lanTaxOption;
    }

    public double getAnnualInsuranceLandlord() {
        return annualInsuranceLandlord;
    }

    public void setAnnualInsuranceLandlord(double annualInsuranceLandlord) {
        this.annualInsuranceLandlord = annualInsuranceLandlord;
    }

    public double getAnnualPropertyFees() {
        return annualPropertyFees;
    }

    public void setAnnualPropertyFees(double annualPropertyFees) {
        this.annualPropertyFees = annualPropertyFees;
    }

    public double getAnnualCashflow() {
        return totalIncomes - totalExpenses;
    }

    public double getMonthlyCashflow() {
        return (totalIncomes - totalExpenses) / 12;
    }

    public double getWeeklyCashflow() {
        return (totalIncomes - totalExpenses) / 52;
    }

    public double getGrossPropertyYield() {
        return (rentPerWeek * 52)/propertyPrice;
    }


}
