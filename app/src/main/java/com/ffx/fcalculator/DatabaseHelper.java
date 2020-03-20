package com.ffx.fcalculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ffx.fcalculator.calculation.Annuity;
import com.ffx.fcalculator.calculation.BondYield;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.calculation.CompoundInterest;
import com.ffx.fcalculator.calculation.Mortgage;
import com.ffx.fcalculator.calculation.PresentValOfAnnuity;
import com.ffx.fcalculator.calculation.PresentValue;
import com.ffx.fcalculator.calculation.PropertyCashflow;
import com.ffx.fcalculator.calculation.Retirement;
import com.ffx.fcalculator.calculation.ReturnRate;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    /* ========== DATA FIELD ========== */

    // Database version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "fcalculator_db";

    /* ======== CONSTRUCTOR ========= */

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* ======== METHODS ========= */

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Saved Calculation:
        db.execSQL(Calculation.CREATE_TABLE);
        // Create table for each calculation types
        db.execSQL(PropertyCashflow.CREATE_TABLE);
        db.execSQL(CompoundInterest.CREATE_TABLE);
        db.execSQL(PresentValue.CREATE_TABLE);
        db.execSQL(ReturnRate.CREATE_TABLE);
        db.execSQL(Annuity.CREATE_TABLE);
        db.execSQL(PresentValOfAnnuity.CREATE_TABLE);
        db.execSQL(BondYield.CREATE_TABLE);
        db.execSQL(Mortgage.CREATE_TABLE);
        db.execSQL(Retirement.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the older table
        db.execSQL("DROP TABLE IF EXISTS " + PropertyCashflow.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CompoundInterest.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PresentValue.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Annuity.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PresentValOfAnnuity.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BondYield.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Retirement.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Mortgage.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReturnRate.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Calculation.TABLE_NAME);
        // Create the tables again
        onCreate(db);
    }

    public void insertCalculation(Calculation calculation) {
        int type = 0;
        // insert entry to the subclass
        if (calculation.getClass() == CompoundInterest.class) {
            // call compound interest
            type = 0;
            CompoundInterest compoundInterest = (CompoundInterest)calculation;
            calculation.setResult(compoundInterest.getFutureValue());
            insertCompoundInterestCalculation(calculation.getTitle(), compoundInterest);
        } else if (calculation.getClass() == PresentValue.class) {
            // call present value
            type = 1;
            PresentValue presentValue = (PresentValue)calculation;
            calculation.setResult(presentValue.getPresentValue());
            insertPresentValueCalculation(calculation.getTitle(), presentValue);
        } else if (calculation.getClass() == ReturnRate.class) {
            // call return rate
            type = 2;
            ReturnRate returnRate = (ReturnRate)calculation;
            calculation.setResult(returnRate.getReturnRate());
            insertReturnRateCalculation(calculation.getTitle(), returnRate);
        } else if (calculation.getClass() == Annuity.class) {
            // call annuity
            type = 3;
            Annuity annuity = (Annuity)calculation;
            calculation.setResult(annuity.getAnnuity());
            insertAnnuityCalculation(calculation.getTitle(), annuity);
        } else if (calculation.getClass() == PresentValOfAnnuity.class) {
            // call pres val of annuity
            type = 4;
            PresentValOfAnnuity presentValOfAnnuity = (PresentValOfAnnuity) calculation;
            calculation.setResult(presentValOfAnnuity.getStartingPrincipal());
            insertPresentValOfAnnuityCalculation(calculation.getTitle(), presentValOfAnnuity);
        } else if (calculation.getClass() == BondYield.class) {
            // call bond yield
            type = 5;
            BondYield bondYield = (BondYield)calculation;
            calculation.setResult(bondYield.getCurrentPrice());
            insertBondYieldCalculation(calculation.getTitle(), bondYield);
        } else if (calculation.getClass() == Mortgage.class) {
            // call mortgage
            type = 6;
            Mortgage mortgage = (Mortgage)calculation;
            calculation.setResult(mortgage.getMonthlyPayment());
            insertMortgageCalculation(calculation.getTitle(), mortgage);
        } else if (calculation.getClass() == Retirement.class) {
            // call retirement
            type = 7;
            Retirement retirement = (Retirement) calculation;
            calculation.setResult(retirement.getAnnualRetirementIncome());
            insertRetirementCalculation(calculation.getTitle(), retirement);
        } else if (calculation.getClass() == PropertyCashflow.class) {
            type = 8;
            PropertyCashflow propertyCashflow = (PropertyCashflow) calculation;
            calculation.setResult(propertyCashflow.getAnnualCashflow());
            insertPropertyCashflowCalculation(calculation.getTitle(), propertyCashflow);
        }
        // get the writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // declare and initialize new contentValues
        ContentValues values = new ContentValues();
        // put each of the values to contentValues
        values.put(Calculation.COLUMN_TITLE, calculation.getTitle());
        values.put(Calculation.COLUMN_RESULT, calculation.getResult());
        values.put(Calculation.COLUMN_DATE, calculation.getDate());
        values.put(Calculation.COLUMN_TYPE, type);
        // insert the contentValues to the savedCalculation table
        db.insert(Calculation.TABLE_NAME, null, values);
        // close the connection
        db.close();
    }

    private void insertPropertyCashflowCalculation(String title, PropertyCashflow propertyCashflow) {
        // begin inserting to the compoundinterestTable
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);

        values.put(PropertyCashflow.COLUMN_PROPERTY_DEPOSIT, propertyCashflow.getPropertyDeposit());
        values.put(PropertyCashflow.COLUMN_RENT_PER_WEEK, propertyCashflow.getRentPerWeek());
        values.put(PropertyCashflow.COLUMN_MORTGAGE_INTEREST_RATE, propertyCashflow.getMortgageInterestRate());
        values.put(PropertyCashflow.COLUMN_PROPERTY_PRICE, propertyCashflow.getPropertyPrice());
        values.put(PropertyCashflow.COLUMN_WATER_RATE, propertyCashflow.getWaterRate());
        values.put(PropertyCashflow.COLUMN_WATER_RATE_OPTION, propertyCashflow.isWaterRateOption());
        values.put(PropertyCashflow.COLUMN_COUNCIL, propertyCashflow.getCouncil());
        values.put(PropertyCashflow.COLUMN_COUNCIL_OPTION, propertyCashflow.isCouncilOption());
        values.put(PropertyCashflow.COLUMN_ADDITIONAL_EXPENSES, propertyCashflow.getAdditionalExpenses());
        values.put(PropertyCashflow.COLUMN_EXPENSES_OPTION, propertyCashflow.isExpensesOption());
        values.put(PropertyCashflow.COLUMN_LAND_TAX, propertyCashflow.getLandTax());
        values.put(PropertyCashflow.COLUMN_LAND_TAX_OPTION, propertyCashflow.isLanTaxOption());
        values.put(PropertyCashflow.COLUMN_ANNUAL_INSURANCE_LANDLORD, propertyCashflow.getAnnualInsuranceLandlord());
        values.put(PropertyCashflow.COLUMN_ANNUAL_PROPERTY_FEES, propertyCashflow.getAnnualPropertyFees());
        // insert the contentValues to the savedCalculation table
        db.insert(PropertyCashflow.TABLE_NAME, null, values);
        // close the connection
        db.close();
    }

    private void insertCompoundInterestCalculation(String title,
                                                  CompoundInterest ci) {
        // begin inserting to the compoundinterestTable
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);
        values.put(CompoundInterest.COLUMN_CURRENT_PRINCIPAL, ci.getCurrentPrincipal());
        values.put(CompoundInterest.COLUMN_ANNUAL_ADDITION, ci.getAnnualAddition());
        values.put(CompoundInterest.COLUMN_YEARS_TO_GROW, ci.getYearsToGrow());
        values.put(CompoundInterest.COLUMN_INTEREST_RATE, ci.getInterestRate());
        values.put(CompoundInterest.COLUMN_INTEREST_TIME, ci.getInterestTime());
        values.put(CompoundInterest.COLUMN_ADDITION_AT, ci.getAdditionAt());
        // insert the contentValues to the savedCalculation table
        db.insert(CompoundInterest.TABLE_NAME, null, values);
        // close the connection
        db.close();
    }

    private void insertPresentValueCalculation(String title, PresentValue pv) {
        // begin inserting to the present value table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);
        values.put(PresentValue.COLUMN_FUTURE_VALUE, pv.getFutureValue());
        values.put(PresentValue.COLUMN_DISCOUNT_RATE, pv.getDiscountRate());
        values.put(PresentValue.COLUMN_YEARS, pv.getYears());
        // insert the contentValues to the savedCalculation table
        db.insert(PresentValue.TABLE_NAME, null, values);
        // close the connection
        db.close();
    }

    private void insertReturnRateCalculation(String title, ReturnRate rr) {
        // begin inserting to the present value table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);
        values.put(ReturnRate.COLUMN_PRESENT_VALUE, rr.getPresentValue());
        values.put(ReturnRate.COLUMN_FUTURE_VALUE, rr.getFutureValue());
        values.put(ReturnRate.COLUMN_YEARS, rr.getYears());
        // insert the contentValues to the savedCalculation table
        db.insert(ReturnRate.TABLE_NAME, null, values);
        // close the connection
        db.close();
    }

    private void insertAnnuityCalculation(String title, Annuity a) {
        // begin inserting to the present value table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);
        values.put(Annuity.COLUMN_STARTING_PRINCIPAL, a.getStartingPrincipal());
        values.put(Annuity.COLUMN_GROWTH_RATE, a.getGrowthRate());
        values.put(Annuity.COLUMN_YEARS_PAY_OUT, a.getYearsPayOut());
        values.put(Annuity.COLUMN_PAYOUT_AT, a.getPayoutOptions());
        db.insert(Annuity.TABLE_NAME, null, values);
        db.close();
    }

    private void insertPresentValOfAnnuityCalculation(String title, PresentValOfAnnuity pa) {
        // begin inserting to the present value table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);
        values.put(PresentValOfAnnuity.COLUMN_ANNUITY, pa.getAnnuity());
        values.put(PresentValOfAnnuity.COLUMN_GROWTH_RATE, pa.getGrowthRate());
        values.put(PresentValOfAnnuity.COLUMN_YEARS_PAY_OUT, pa.getYearsPayOut());
        values.put(PresentValOfAnnuity.COLUMN_PAYOUT_AT, pa.getPayoutOptions());
        db.insert(PresentValOfAnnuity.TABLE_NAME, null, values);
        db.close();
    }

    private void insertBondYieldCalculation(String title, BondYield by) {
        // begin inserting to the present value table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);
        values.put(BondYield.COLUMN_CURRENT_PRICE, by.getCurrentPrice());
        values.put(BondYield.COLUMN_PER_VALUE, by.getPerValue());
        values.put(BondYield.COLUMN_COUPON_RATE, by.getCouponRate());
        values.put(BondYield.COLUMN_YEARS_TO_MATURITY, by.getYears());
        db.insert(BondYield.TABLE_NAME, null, values);
        db.close();
    }

    private void insertMortgageCalculation(String title, Mortgage m) {
        // begin inserting to the present value table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);
        values.put(Mortgage.COLUMN_LOAN_AMOUNT, m.getLoanAmount());
        values.put(Mortgage.COLUMN_MORTGAGE_RATE, m.getMorgageRate());
        values.put(Mortgage.COLUMN_YEARS_TO_PAY, m.getYearsToPay());
        db.insert(Mortgage.TABLE_NAME, null, values);
        db.close();
    }

    private void insertRetirementCalculation(String title, Retirement r) {
        // begin inserting to the present value table
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Calculation.COLUMN_TITLE, title);
        values.put(Retirement.COLUMN_CURRENT_PRINCIPAL, r.getCurrentPrincipal());
        values.put(Retirement.COLUMN_ANNUAL_ADDITION, r.getAnnualAddition());
        values.put(Retirement.COLUMN_YEARS_TO_GROW, r.getYearsToGrow());
        values.put(Retirement.COLUMN_PRE_GROWTH_RATE, r.getPreGrowthRate());
        values.put(Retirement.COLUMN_YEARS_TO_PAYOUT, r.getYearsToPayOut());
        values.put(Retirement.COLUMN_IN_GROWTH_RATE, r.getInGrowthRate());
        db.insert(Retirement.TABLE_NAME, null, values);
        db.close();
    }



//    public SavedCalculation getSavedCalculation(String title) {
//        // get the readable database
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(SavedCalculation.TABLE_NAME,
//                new String[]{SavedCalculation.COLUMN_TITLE, SavedCalculation.COLUMN_RESULT, SavedCalculation.COLUMN_DATE, SavedCalculation.COLUMN_TYPE},
//                SavedCalculation.COLUMN_TITLE + "=?",
//                new String[]{title}, null, null, null, null);
//        if (cursor != null)
//            cursor.moveToFirst();
//
//        // prepare calculation object
//        SavedCalculation savedCalculation = new SavedCalculation(
//                cursor.getString(cursor.getColumnIndex(SavedCalculation.COLUMN_TITLE)),
//                cursor.getInt(cursor.getColumnIndex(SavedCalculation.COLUMN_TYPE)),
//                cursor.getDouble(cursor.getColumnIndex(SavedCalculation.COLUMN_RESULT)),
//                cursor.getString(cursor.getColumnIndex(SavedCalculation.COLUMN_DATE))
//        );
//        // close the cursor
//        cursor.close();
//        // return the savedCalculation
//        return savedCalculation;
//    }

    // get calculation method for all the calculation type
    public Calculation getCalculation(String title, int type) {
        // get the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // switch the type
        // declare a curson object
        Cursor cursor = null;
        switch (type) {
            case 0:
                // compound interest
                cursor = db.query(CompoundInterest.TABLE_NAME,
                        new String[] {
                                Calculation.COLUMN_TITLE,
                                CompoundInterest.COLUMN_CURRENT_PRINCIPAL,
                                CompoundInterest.COLUMN_ANNUAL_ADDITION,
                                CompoundInterest.COLUMN_YEARS_TO_GROW,
                                CompoundInterest.COLUMN_INTEREST_RATE,
                                CompoundInterest.COLUMN_INTEREST_TIME,
                                CompoundInterest.COLUMN_ADDITION_AT
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
            case 1:
                // present value
                cursor = db.query(PresentValue.TABLE_NAME,
                        new String[]{
                                Calculation.COLUMN_TITLE,
                                PresentValue.COLUMN_FUTURE_VALUE,
                                PresentValue.COLUMN_YEARS,
                                PresentValue.COLUMN_DISCOUNT_RATE
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
            case 2:
                // return rate
                cursor = db.query(ReturnRate.TABLE_NAME,
                        new String[]{
                                Calculation.COLUMN_TITLE,
                                ReturnRate.COLUMN_FUTURE_VALUE,
                                ReturnRate.COLUMN_YEARS,
                                ReturnRate.COLUMN_PRESENT_VALUE
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
            case 3:
                // annuity
                cursor = db.query(Annuity.TABLE_NAME,
                        new String[]{
                                Calculation.COLUMN_TITLE,
                                Annuity.COLUMN_STARTING_PRINCIPAL,
                                Annuity.COLUMN_GROWTH_RATE,
                                Annuity.COLUMN_YEARS_PAY_OUT,
                                Annuity.COLUMN_PAYOUT_AT
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
            case 4:
                // present value of annuity
                cursor = db.query(PresentValOfAnnuity.TABLE_NAME,
                        new String[]{
                                Calculation.COLUMN_TITLE,
                                PresentValOfAnnuity.COLUMN_ANNUITY,
                                PresentValOfAnnuity.COLUMN_GROWTH_RATE,
                                PresentValOfAnnuity.COLUMN_YEARS_PAY_OUT,
                                PresentValOfAnnuity.COLUMN_PAYOUT_AT
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
            case 5:
                // bond yield
                cursor = db.query(BondYield.TABLE_NAME,
                        new String[]{
                                Calculation.COLUMN_TITLE,
                                BondYield.COLUMN_CURRENT_PRICE,
                                BondYield.COLUMN_PER_VALUE,
                                BondYield.COLUMN_COUPON_RATE,
                                BondYield.COLUMN_YEARS_TO_MATURITY
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
            case 6:
                // mortgage
                cursor = db.query(Mortgage.TABLE_NAME,
                        new String[]{
                                Calculation.COLUMN_TITLE,
                                Mortgage.COLUMN_LOAN_AMOUNT,
                                Mortgage.COLUMN_MORTGAGE_RATE,
                                Mortgage.COLUMN_YEARS_TO_PAY
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
            case 7:
                // retirement
                cursor = db.query(Retirement.TABLE_NAME,
                        new String[]{
                                Calculation.COLUMN_TITLE,
                                Retirement.COLUMN_CURRENT_PRINCIPAL,
                                Retirement.COLUMN_ANNUAL_ADDITION,
                                Retirement.COLUMN_YEARS_TO_GROW,
                                Retirement.COLUMN_PRE_GROWTH_RATE,
                                Retirement.COLUMN_YEARS_TO_PAYOUT,
                                Retirement.COLUMN_IN_GROWTH_RATE
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
            case 8:

                // cashflow NEW!
                cursor = db.query(PropertyCashflow.TABLE_NAME,
                        new String[]{
                                Calculation.COLUMN_TITLE,
                                PropertyCashflow.COLUMN_PROPERTY_PRICE,
                                PropertyCashflow.COLUMN_PROPERTY_DEPOSIT,
                                PropertyCashflow.COLUMN_RENT_PER_WEEK,
                                PropertyCashflow.COLUMN_MORTGAGE_INTEREST_RATE,
                                PropertyCashflow.COLUMN_MORTGAGE_INTEREST_RATE,
                                PropertyCashflow.COLUMN_WATER_RATE,
                                PropertyCashflow.COLUMN_WATER_RATE_OPTION,
                                PropertyCashflow.COLUMN_COUNCIL,
                                PropertyCashflow.COLUMN_COUNCIL_OPTION,
                                PropertyCashflow.COLUMN_ADDITIONAL_EXPENSES,
                                PropertyCashflow.COLUMN_EXPENSES_OPTION,
                                PropertyCashflow.COLUMN_LAND_TAX,
                                PropertyCashflow.COLUMN_LAND_TAX_OPTION,
                                PropertyCashflow.COLUMN_ANNUAL_INSURANCE_LANDLORD,
                                PropertyCashflow.COLUMN_ANNUAL_PROPERTY_FEES,
                        },Calculation.COLUMN_TITLE + "=?",
                        new String[]{title}, null, null, null, null);
                break;
        }
        if (cursor != null)
            cursor.moveToFirst();
        switch (type) {
            case 0:
                // addition at initialize the value
                boolean additionAt;
                if (cursor.getInt(cursor.getColumnIndex(CompoundInterest.COLUMN_ADDITION_AT)) == 0) {
                    additionAt = false;
                } else {
                    additionAt = true;
                }
                // declare a new compound interest
                CompoundInterest compoundInterest = new CompoundInterest(
                        cursor.getDouble(cursor.getColumnIndex(CompoundInterest.COLUMN_CURRENT_PRINCIPAL)),
                        cursor.getDouble(cursor.getColumnIndex(CompoundInterest.COLUMN_ANNUAL_ADDITION)),
                        cursor.getDouble(cursor.getColumnIndex(CompoundInterest.COLUMN_YEARS_TO_GROW)),
                        cursor.getDouble(cursor.getColumnIndex(CompoundInterest.COLUMN_INTEREST_RATE)) * 100,
                        cursor.getDouble(cursor.getColumnIndex(CompoundInterest.COLUMN_INTEREST_TIME)),
                        additionAt
                );
                return compoundInterest;
            case 1:
                // declare a new present value
                PresentValue presentValue = new PresentValue(
                        cursor.getDouble(cursor.getColumnIndex(PresentValue.COLUMN_FUTURE_VALUE)),
                        cursor.getDouble(cursor.getColumnIndex(PresentValue.COLUMN_YEARS)),
                        cursor.getDouble(cursor.getColumnIndex(PresentValue.COLUMN_DISCOUNT_RATE)) * 100
                );
                return presentValue;
            case 2:
                // declare a new return rate
                ReturnRate returnRate = new ReturnRate(
                        cursor.getDouble(cursor.getColumnIndex(ReturnRate.COLUMN_PRESENT_VALUE)),
                        cursor.getDouble(cursor.getColumnIndex(ReturnRate.COLUMN_FUTURE_VALUE)),
                        cursor.getDouble(cursor.getColumnIndex(ReturnRate.COLUMN_YEARS))
                );
                return returnRate;
            case 3:
                // declare a new annuity
                boolean payoutAt;
                if (cursor.getInt(cursor.getColumnIndex(Annuity.COLUMN_PAYOUT_AT)) == 0) {
                    payoutAt = false;
                } else {
                    payoutAt = true;
                }
                Annuity annuity = new Annuity(
                        cursor.getDouble(cursor.getColumnIndex(Annuity.COLUMN_STARTING_PRINCIPAL)),
                        cursor.getDouble(cursor.getColumnIndex(Annuity.COLUMN_GROWTH_RATE)) * 100,
                        cursor.getDouble(cursor.getColumnIndex(Annuity.COLUMN_YEARS_PAY_OUT)),
                        payoutAt
                );
                return annuity;
            case 4:
                // declare a new present value of annuity
                if (cursor.getInt(cursor.getColumnIndex(PresentValOfAnnuity.COLUMN_PAYOUT_AT)) == 0) {
                    payoutAt = false;
                } else {
                    payoutAt = true;
                }
                PresentValOfAnnuity presentValOfAnnuity = new PresentValOfAnnuity(
                        cursor.getDouble(cursor.getColumnIndex(PresentValOfAnnuity.COLUMN_ANNUITY)),
                        cursor.getDouble(cursor.getColumnIndex(PresentValOfAnnuity.COLUMN_GROWTH_RATE)) * 100,
                        cursor.getDouble(cursor.getColumnIndex(PresentValOfAnnuity.COLUMN_YEARS_PAY_OUT)),
                        payoutAt
                );
                return presentValOfAnnuity;
            case 5:
                BondYield bondYield = new BondYield(
                        cursor.getDouble(cursor.getColumnIndex(BondYield.COLUMN_CURRENT_PRICE)),
                        cursor.getDouble(cursor.getColumnIndex(BondYield.COLUMN_PER_VALUE)),
                        cursor.getDouble(cursor.getColumnIndex(BondYield.COLUMN_COUPON_RATE)) * 100,
                        cursor.getDouble(cursor.getColumnIndex(BondYield.COLUMN_YEARS_TO_MATURITY))
                );
                return bondYield;
            case 6:
                Mortgage mortgage = new Mortgage(
                        cursor.getDouble(cursor.getColumnIndex(Mortgage.COLUMN_LOAN_AMOUNT)),
                        cursor.getDouble(cursor.getColumnIndex(Mortgage.COLUMN_MORTGAGE_RATE)) * 100,
                        cursor.getDouble(cursor.getColumnIndex(Mortgage.COLUMN_YEARS_TO_PAY))
                );
                return mortgage;
            case 7:
                Retirement retirement = new Retirement(
                        cursor.getDouble(cursor.getColumnIndex(Retirement.COLUMN_CURRENT_PRINCIPAL)),
                        cursor.getDouble(cursor.getColumnIndex(Retirement.COLUMN_ANNUAL_ADDITION)),
                        cursor.getDouble(cursor.getColumnIndex(Retirement.COLUMN_YEARS_TO_GROW)),
                        cursor.getDouble(cursor.getColumnIndex(Retirement.COLUMN_PRE_GROWTH_RATE)) * 100,
                        cursor.getDouble(cursor.getColumnIndex(Retirement.COLUMN_YEARS_TO_PAYOUT)),
                        cursor.getDouble(cursor.getColumnIndex(Retirement.COLUMN_IN_GROWTH_RATE)) * 100
                );
                return retirement;
            case 8:
                boolean waterOption, councilOption, landTaxOption, additionalExpensesOption;
                double waterRate, councilRate, landTax, additionalExpenses;

                if (cursor.getInt(cursor.getColumnIndex(PropertyCashflow.COLUMN_WATER_RATE_OPTION)) == 0) {
                    // quaterly
                    waterOption = false;
                    // dividing by 4 as it will be times 4 when creating the object inside of the constructor
                    waterRate = cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_WATER_RATE)) / 4;
                } else {
                    waterOption = true;
                    waterRate = cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_WATER_RATE));
                }

                if (cursor.getInt(cursor.getColumnIndex(PropertyCashflow.COLUMN_COUNCIL_OPTION)) == 0) {
                    councilOption = false;
                    councilRate = cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_COUNCIL)) / 4;
                } else {
                    councilOption = true;
                    councilRate = cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_COUNCIL));
                }

                if (cursor.getInt(cursor.getColumnIndex(PropertyCashflow.COLUMN_LAND_TAX_OPTION)) == 0) {
                    landTax = cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_LAND_TAX)) / 4;
                    landTaxOption = false;
                } else {
                    landTax = cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_LAND_TAX));
                    landTaxOption = true;
                }

                if (cursor.getInt(cursor.getColumnIndex(PropertyCashflow.COLUMN_EXPENSES_OPTION)) == 0) {
                    additionalExpenses = cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_ADDITIONAL_EXPENSES)) / 4;
                    additionalExpensesOption = false;
                } else {
                    additionalExpenses = cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_ADDITIONAL_EXPENSES));
                    additionalExpensesOption = true;
                }

                // create new property cashflow object

                PropertyCashflow propertyCashflow = new PropertyCashflow(
                        cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_PROPERTY_PRICE)),
                        cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_PROPERTY_DEPOSIT)),
                        cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_RENT_PER_WEEK)),
                        cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_MORTGAGE_INTEREST_RATE)),
                        waterRate,
                        waterOption,
                        councilRate,
                        councilOption,
                        additionalExpenses,
                        additionalExpensesOption,
                        landTax,
                        landTaxOption,
                        cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_ANNUAL_INSURANCE_LANDLORD)),
                        cursor.getDouble(cursor.getColumnIndex(PropertyCashflow.COLUMN_ANNUAL_PROPERTY_FEES))
                );

                return propertyCashflow;
        }
        return null;
    }

    public void deleteCalculation(Calculation calculation) {
        // get the writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // delete the entry
        db.delete(Calculation.TABLE_NAME, Calculation.COLUMN_TITLE + "=?",
                new String[]{calculation.getTitle()});
        // declaring table name string
        String tableName = "";
        // determining the table name
        if (calculation.getClass() == CompoundInterest.class) {
            // compound interest
            CompoundInterest compoundInterest = (CompoundInterest)calculation;
            tableName = compoundInterest.TABLE_NAME;
        } else if (calculation.getClass() == PresentValue.class) {
            // present value
            PresentValue presentValue = (PresentValue) calculation;
            tableName = presentValue.TABLE_NAME;
        } else if (calculation.getClass() == ReturnRate.class) {
            // return rate
            ReturnRate returnRate = (ReturnRate) calculation;
            tableName = returnRate.TABLE_NAME;
        } else if (calculation.getClass() == BondYield.class) {
            // bond yield
            BondYield bondYield = (BondYield) calculation;
            tableName = bondYield.TABLE_NAME;
        } else if (calculation.getClass() == Annuity.class) {
            // annuity
            Annuity annuity = (Annuity) calculation;
            tableName = Annuity.TABLE_NAME;
        } else if (calculation.getClass() == PresentValOfAnnuity.class) {
            // present value of annuity
            PresentValOfAnnuity presentValOfAnnuity = (PresentValOfAnnuity) calculation;
            tableName = presentValOfAnnuity.TABLE_NAME;
        } else if (calculation.getClass() == Mortgage.class) {
            // mortgage
            Mortgage mortgage = (Mortgage) calculation;
            tableName = mortgage.TABLE_NAME;
        } else if (calculation.getClass() == Retirement.class) {
            // retirement
            Retirement retirement = (Retirement) calculation;
            tableName = retirement.TABLE_NAME;
        } else {
            // property cashflow
            PropertyCashflow propertyCashflow = (PropertyCashflow) calculation;
            tableName = propertyCashflow.TABLE_NAME;
        }
        // delete query
        db.delete(tableName, Calculation.COLUMN_TITLE + "=?", new String[]{calculation.getTitle()});
        // close the db
        db.close();
    }

    public List<Calculation> getAllCalculation() {
        // initialize new calculation
        List<Calculation> calculations = new ArrayList<>();
        // select query
        String selectQuery = "SELECT * FROM " + Calculation.TABLE_NAME;
        // get new writable database
        SQLiteDatabase db = this.getWritableDatabase();
        // new cursor
        Cursor cursor = db.rawQuery(selectQuery, null);
        // if the cursor move to first
        if (cursor.moveToFirst()) {
            // do save calculation and add it to the list while cursor is still there
            do {
                Log.i("Title", cursor.getString(cursor.getColumnIndex(Calculation.COLUMN_TITLE)));
                Calculation calculation;
                calculation = getCalculation(cursor.getString(cursor.getColumnIndex(Calculation.COLUMN_TITLE)), cursor.getInt(cursor.getColumnIndex(Calculation.COLUMN_TYPE)));
                calculation.setTitle(cursor.getString(cursor.getColumnIndex(Calculation.COLUMN_TITLE)));
                calculation.setResult(cursor.getDouble(cursor.getColumnIndex(Calculation.COLUMN_RESULT)));
                calculation.setDate(cursor.getString(cursor.getColumnIndex(Calculation.COLUMN_DATE)));
                calculations.add(calculation);

            } while(cursor.moveToNext());
        }
        // close
        db.close();
        // return
        return calculations;
    }

    // count method
    public int getCalculationsCount() {
        // query
        String countQuery = "SELECT * FROM " + Calculation.TABLE_NAME;
        // get the readable database
        SQLiteDatabase db = this.getReadableDatabase();
        // cursor
        Cursor cursor = db.rawQuery(countQuery, null);
        // count
        int count = cursor.getCount();
        // close
        cursor.close();
        // return
        return count;
    }
}
