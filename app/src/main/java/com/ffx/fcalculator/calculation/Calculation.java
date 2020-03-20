package com.ffx.fcalculator.calculation;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Calculation implements Serializable {
    /* ========== DATA FIELD ========== */
    private String title, date;
    private double result;

    public static final int YEARS_LIMIT = 100;

    /* ========== SQL DATABASE SCRIPT ========== */

    // ATTRIBUTES
    public static final String TABLE_NAME = "CALCULATION";
    public static final String COLUMN_TITLE = "c_title";
    public static final String COLUMN_RESULT = "c_result";
    public static final String COLUMN_DATE = "c_date";
    public static final String COLUMN_TYPE = "c_type";
    public static final String CONSTRAINT_PKEY = "c_pkey";
    // COMMANDS
    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + COLUMN_TITLE + " VARCHAR(200) " + " NOT NULL, "
            + COLUMN_RESULT + " DECIMAL(20,2) " + " NOT NULL, "
            + COLUMN_DATE + " DATE " + " NOT NULL, "
            + COLUMN_TYPE + " DECIMAL(1) " + " NOT NULL, "
            + "CONSTRAINT " + CONSTRAINT_PKEY + " PRIMARY KEY(" + COLUMN_TITLE + "));";

    /* ======== CONSTRUCTOR ========= */
    public Calculation() {
        this.title = "";
        this.result = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        this.date = dateFormat.format(date).toString();
    }

    public Calculation(String title, double result, String date) {
        this.title = title;
        this.result = result;
        this.date = date;
    }

    /* ======== ACCESSOR METHODS ======== */
    public String getTitle() {
        return this.title;
    }

    public double getResult() {
        return this.result;
    }

    public String getDate() {
        return this.date;
    }

    /* ======== METHODS ======== */

    // basic investment 2 for addition at the end
    protected double bI2(double p, double r, double y, double c) {
        // if the year is zero
        if (y == 0) {
            return p;
        }
        return futureValue(p, r, y) + c * geomSeries(1 + r, 0, y - 1);
    }

    // futureValue formula
    protected double futureValue(double p, double r, double y) {
        // current principal multiply by (1 + r)^y (y = years to go), r = rate
        return p * Math.pow(1 + r, y);
    }

    protected double geomSeries(double z, double m, double n) {
        double amt;
        if (z == 1.0) {
            amt = n + 1;
        } else {
            amt = (Math.pow(z, n + 1) - 1) / (z - 1);
        }
        if (m >= 1) {
            amt -= geomSeries(z, 0, m - 1);
        }
        return amt;
    }

    // basic investment for addition at the beginning
    protected double basicInvestment(double p, double r, double y, double c) {
        return futureValue(p, r, y) + c * geomSeries(1 + r, 1, y);
    }


    /* ======== MUTATOR METHODS ========= */

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setResult(double result) {
        this.result = result;
    }


}
