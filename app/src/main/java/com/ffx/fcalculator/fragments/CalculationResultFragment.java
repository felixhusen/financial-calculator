package com.ffx.fcalculator.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ffx.fcalculator.DatabaseHelper;
import com.ffx.fcalculator.R;
import com.ffx.fcalculator.activities.CalculationResultActivity;
import com.ffx.fcalculator.calculation.Annuity;
import com.ffx.fcalculator.calculation.BondYield;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.calculation.CompoundInterest;
import com.ffx.fcalculator.calculation.Mortgage;
import com.ffx.fcalculator.calculation.PresentValOfAnnuity;
import com.ffx.fcalculator.calculation.PresentValue;
import com.ffx.fcalculator.calculation.Retirement;
import com.ffx.fcalculator.calculation.ReturnRate;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CalculationResultFragment extends Fragment {

    private int type = 0;

    // Fields
    private TextView result1Desc;
    private TextView result1Text;

    private TextView result2Desc;
    private TextView result2Text;

    private TextView text1Desc, text1Text;

    private TextView text2Desc, text2Text;

    private TextView text3Desc, text3Text;

    private TextView text4Desc, text4Text;

    private TextView text5Desc, text5Text;

    private TextView text6Desc, text6Text;

    private TextView text7Desc, text7Text;

    private LinearLayout result1, result2;

    private Button saveCalculationButton;

    private DatabaseHelper db;

    private Calculation calculation;

    private Bundle bundle;

    private TableLayout annuityTable;

    private DecimalFormat formatter;

    private BarChart barChart;

    public CalculationResultFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CalculationResultFragment(DatabaseHelper db, Calculation calculation) {
        // Required empty public constructor
        this.db = db;
        this.calculation = calculation;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculation_result, container, false);
        // set nav title:
        getActivity().setTitle(R.string.nav_property_cashflow);


        // set the number format
        formatter = new DecimalFormat("#,###.00");

        // initialize all of the fields

        // declare new bar chart
        barChart = view.findViewById(R.id.chart_result);

        result1 = view.findViewById(R.id.result1);
        result2 = view.findViewById(R.id.result2);

        result1Desc = view.findViewById(R.id.result1_desc);
        result1Text = view.findViewById(R.id.result1_text);

        result2Desc = view.findViewById(R.id.result2_desc);
        result2Text = view.findViewById(R.id.result2_text);

        text1Desc = view.findViewById(R.id.text1_desc);
        text1Text = view.findViewById(R.id.text1_text);

        text2Desc = view.findViewById(R.id.text2_desc);
        text2Text = view.findViewById(R.id.text2_text);

        text3Desc = view.findViewById(R.id.text3_desc);
        text3Text = view.findViewById(R.id.text3_text);

        text4Desc = view.findViewById(R.id.text4_desc);
        text4Text = view.findViewById(R.id.text4_text);

        text5Desc = view.findViewById(R.id.text5_desc);
        text5Text = view.findViewById(R.id.text5_text);

        text6Desc = view.findViewById(R.id.text6_desc);
        text6Text = view.findViewById(R.id.text6_text);

        text7Desc = view.findViewById(R.id.text7_desc);
        text7Text = view.findViewById(R.id.text7_text);

        saveCalculationButton = view.findViewById(R.id.calculation_result_save_button);

        saveCalculationButton.setOnClickListener(showDialogOnClickListener);

        annuityTable = view.findViewById(R.id.annuity_table);

        // set the onclicklistener for save button
        saveCalculationButton.setOnClickListener(showDialogOnClickListener);

        if (calculation.getClass() == CompoundInterest.class) {
            // compound interest
            displayCompoundInterestResult((CompoundInterest)calculation);
        } else if (calculation.getClass() == PresentValue.class) {
            // present value
            displayPresentValueResult((PresentValue)calculation);
        } else if (calculation.getClass() == ReturnRate.class) {
            // return rate
            displayReturnRateResult((ReturnRate)calculation);
        } else if (calculation.getClass() == BondYield.class) {
            // bond yield
            displayBondYieldResult((BondYield)calculation);
        } else if (calculation.getClass() == Annuity.class) {
            // annuity
            displayAnnuityResult((Annuity)calculation);
        } else if (calculation.getClass() == PresentValOfAnnuity.class) {
            // present value of annuity
            displayPresValAnnuityResult((PresentValOfAnnuity)calculation);
        } else if (calculation.getClass() == Mortgage.class) {
            // mortgage
            displayMortgageResult((Mortgage)calculation);
        } else if (calculation.getClass() == Retirement.class) {
            // retirement
            displayRetirementResult((Retirement) calculation);
        }
        return view;
    }

    private void displayCompoundInterestResult(CompoundInterest ci) {
        // assign value to result1
        result1Desc.setText(R.string.future_value);
        result1Text.setText(String.format( "$%s", this.formatter.format(ci.getFutureValue())));

        // no result2
        result2.setVisibility(View.GONE);

        text1Desc.setText(R.string.current_principal);
        text1Text.setText(String.format( "$%s", this.formatter.format(ci.getCurrentPrincipal())));

        text2Desc.setText(R.string.annual_addition);
        text2Text.setText(String.format( "$%s", this.formatter.format(ci.getAnnualAddition())));

        text3Desc.setText(R.string.years_to_grow);
        text3Text.setText(String.format( "%.2f Years", ci.getYearsToGrow()));

        text4Desc.setText(R.string.interest_rate);
        text4Text.setText(String.format( "%.2f%s", ci.getInterestRate() * 100, "%"));

        text5Desc.setText(R.string.interest_time);
        text5Text.setText(String.format( "%.2f Times", ci.getInterestTime()));

        text6Desc.setText(R.string.make_addition_at);
        if (ci.getAdditionAt()) {
            text6Text.setText(R.string.beginning_year);
        } else {
            text6Text.setText(R.string.end_year);
        }

        text7Desc.setVisibility(View.GONE);
        text7Text.setVisibility(View.GONE);

        displayTable(ci.getYearsToGrow());
        displayChart(ci.getYearsToGrow());
    }

    private void displayPresentValueResult(PresentValue pv) {
        result1Desc.setText(R.string.present_value);

        result1Text.setText(String.format("$%s", this.formatter.format(pv.getPresentValue())));

        // no result2
        result2.setVisibility(View.GONE);

        text1Desc.setText(R.string.future_value);
        text1Text.setText(String.format( "$%s", this.formatter.format(pv.getFutureValue())));

        text2Desc.setText(R.string.years);
        text2Text.setText(String.format( "%.2f Years", pv.getYears()));

        text3Desc.setText(R.string.discount_rate);
        text3Text.setText(String.format( "%.2f%s", pv.getDiscountRate() * 100, "%"));

        text4Desc.setVisibility(View.GONE);
        text4Text.setVisibility(View.GONE);

        text5Desc.setVisibility(View.GONE);
        text5Text.setVisibility(View.GONE);

        text6Desc.setVisibility(View.GONE);
        text6Text.setVisibility(View.GONE);

        text7Desc.setVisibility(View.GONE);
        text7Text.setVisibility(View.GONE);

        displayTable(pv.getYears());
        displayChart(pv.getYears());
    }

    private void displayReturnRateResult(ReturnRate rr) {
        result1Desc.setText(R.string.return_rate);
        result1Text.setText(String.format( "%.2f%s", rr.getReturnRate() * 100, "%"));

        // no result2
        result2.setVisibility(View.GONE);

        text1Desc.setText(R.string.present_value);
        text1Text.setText(String.format( "$%s", this.formatter.format(rr.getPresentValue())));

        text2Desc.setText(R.string.future_value);
        text2Text.setText(String.format( "$%s", this.formatter.format(rr.getFutureValue())));

        text3Desc.setText(R.string.years);
        text3Text.setText(String.format( "%.2f Years", rr.getYears()));

        text4Desc.setVisibility(View.GONE);
        text4Text.setVisibility(View.GONE);

        text5Desc.setVisibility(View.GONE);
        text5Text.setVisibility(View.GONE);

        text6Desc.setVisibility(View.GONE);
        text6Text.setVisibility(View.GONE);

        text7Desc.setVisibility(View.GONE);
        text7Text.setVisibility(View.GONE);

        displayTable(rr.getYears());
        displayChart(rr.getYears());
    }

    @SuppressLint("ResourceAsColor")
    private void displayAnnuityResult(Annuity an) {
        result1Desc.setText(R.string.annual_payout);
        result1Text.setText(String.format( "$%s", this.formatter.format(an.getAnnuity())));

        // no result2
        result2.setVisibility(View.GONE);

        text1Desc.setText(R.string.starting_principal);
        text1Text.setText(String.format( "$%s", this.formatter.format(an.getStartingPrincipal())));

        text2Desc.setText(R.string.growth_rate);
        text2Text.setText(String.format( "%.2f%s", an.getGrowthRate() * 100, "%"));

        text3Desc.setText(R.string.years_pay_out);
        text3Text.setText(String.format( "%.2f Years", an.getYearsPayOut()));

        text4Desc.setText(R.string.payout_at);
        // if it is true then the annuity is on the beginning of each year
        if (an.getPayoutOptions()) {
            text4Text.setText(R.string.beginning_year);
        } else {
            text4Text.setText(R.string.end_year);
        }

        text5Desc.setVisibility(View.GONE);
        text5Text.setVisibility(View.GONE);

        text6Desc.setVisibility(View.GONE);
        text6Text.setVisibility(View.GONE);

        text7Desc.setVisibility(View.GONE);
        text7Text.setVisibility(View.GONE);

        displayTable(an.getYearsPayOut());
        displayChart(an.getYearsPayOut());

    }

    private void displayPresValAnnuityResult(PresentValOfAnnuity pva) {
        result1Desc.setText(R.string.starting_principal);
        result1Text.setText(String.format( "$%s", this.formatter.format(pva.getStartingPrincipal())));

        // no result2
        result2.setVisibility(View.GONE);

        text1Desc.setText(R.string.annual_payout);
        text1Text.setText(String.format( "$%s", this.formatter.format(pva.getAnnuity())));

        text2Desc.setText(R.string.growth_rate);
        text2Text.setText(String.format( "%.2f%s", pva.getGrowthRate() * 100, "%"));

        text3Desc.setText(R.string.years_pay_out);
        text3Text.setText(String.format( "%.2f Years", pva.getYearsPayOut()));

        text4Desc.setText(R.string.payout_at);
        // if it is true then the annuity is on the beginning of each year
        if (pva.getPayoutOptions()) {
            text4Text.setText(R.string.beginning_year);
        } else {
            text4Text.setText(R.string.end_year);
        }

        text5Desc.setVisibility(View.GONE);
        text5Text.setVisibility(View.GONE);

        text6Desc.setVisibility(View.GONE);
        text6Text.setVisibility(View.GONE);

        text7Desc.setVisibility(View.GONE);
        text7Text.setVisibility(View.GONE);

        displayTable(pva.getYearsPayOut());
        displayChart(pva.getYearsPayOut());
    }

    private void displayBondYieldResult(BondYield by) {
        result1Desc.setText(R.string.current_yield);
        result1Text.setText(String.format( "%.2f%s", by.getCurrentYield() * 100, "%"));

        result2Desc.setText(R.string.yield_to_maturity);
        result2Text.setText(String.format( "%.2f%s", by.getYield() * 100, "%"));

        text1Desc.setText(R.string.current_price);
        text1Text.setText(String.format( "$%s", this.formatter.format(by.getCurrentPrice())));

        text2Desc.setText(R.string.per_value);
        text2Text.setText(String.format( "$%s", this.formatter.format(by.getPerValue())));

        text3Desc.setText(R.string.coupon_rate);
        text3Text.setText(String.format( "%.2f%s", by.getCouponRate() * 100, "%"));

        text4Desc.setText(R.string.years_to_maturity);
        text4Text.setText(String.format( "%.2f Years", by.getYears()));

        text5Desc.setVisibility(View.GONE);
        text5Text.setVisibility(View.GONE);

        text6Desc.setVisibility(View.GONE);
        text6Text.setVisibility(View.GONE);

        text7Desc.setVisibility(View.GONE);
        text7Text.setVisibility(View.GONE);

        displayTable(by.getYears());
        displayChart(by.getYears());
    }

    private void displayMortgageResult(Mortgage mortgage) {
        result1Desc.setText(R.string.monthly_payment);
        result1Text.setText(String.format( "$%s", this.formatter.format(mortgage.getMonthlyPayment())));

        // hide the result2 as it is not used
        result2.setVisibility(View.GONE);

        text1Desc.setText(R.string.loan_amount);
        text1Text.setText(String.format( "$%s", this.formatter.format(mortgage.getLoanAmount())));

        text2Desc.setText(R.string.mortgage_rate);
        text2Text.setText(String.format( "%.2f%s", mortgage.getMorgageRate() * 100, "%"));

        text3Desc.setText(R.string.years_to_pay);
        text3Text.setText(String.format( "%.2f Years", mortgage.getYearsToPay()));

        text4Desc.setVisibility(View.GONE);
        text4Text.setVisibility(View.GONE);

        text5Desc.setVisibility(View.GONE);
        text5Text.setVisibility(View.GONE);

        text6Desc.setVisibility(View.GONE);
        text6Text.setVisibility(View.GONE);

        text7Desc.setVisibility(View.GONE);
        text7Text.setVisibility(View.GONE);

        displayTable(mortgage.getYearsToPay());
        displayChart(mortgage.getYearsToPay());
    }

    private void displayRetirementResult(Retirement retirement) {
        result1Desc.setText(R.string.annual_retirement_income);
        result1Text.setText(String.format( "$%s", this.formatter.format(retirement.getAnnualRetirementIncome())));

        // hide the result2 as it is not used
        result2.setVisibility(View.GONE);

        text1Desc.setText(R.string.current_principal);
        text1Text.setText(String.format( "$%s", this.formatter.format(retirement.getCurrentPrincipal())));

        text2Desc.setText(R.string.annual_addition);
        text2Text.setText(String.format( "$%s", this.formatter.format(retirement.getAnnualAddition())));

        text3Desc.setText(R.string.years_to_grow);
        text3Text.setText(String.format( "%.2f Years", retirement.getYearsToGrow()));

        text4Desc.setText(R.string.pre_growth_rate);
        text4Text.setText(String.format( "%.2f%s", retirement.getPreGrowthRate() * 100, "%"));

        text5Desc.setText(R.string.years_to_pay);
        text5Text.setText(String.format( "%.2f Years", retirement.getYearsToPayOut()));

        text6Desc.setText(R.string.in_growth_rate);
        text6Text.setText(String.format( "%.2f%s", retirement.getInGrowthRate() * 100, "%"));

        text7Desc.setVisibility(View.GONE);
        text7Text.setVisibility(View.GONE);

        displayTable(retirement.getYearsToGrow());
        displayChart(retirement.getYearsToGrow());
    }

    // display chart
    private void displayChart(double years) {

        // create new list
        ArrayList<BarEntry> entries = new ArrayList<>();
        // for each of the year
        for (int i = 1; i <= years; i++) {
            // create new entry
            BarEntry entry = new BarEntry(i, 0);
            // determine the calculation type
            if (this.calculation.getClass() == CompoundInterest.class) {
                CompoundInterest compoundInterest = (CompoundInterest)calculation;
                entry.setY((float)compoundInterest.getFutureValue(i));
            } else if (this.calculation.getClass() == PresentValue.class) {
                PresentValue presentValue = (PresentValue) calculation;
                entry.setY((float)presentValue.getPresentValue(i));
            } else if (this.calculation.getClass() == ReturnRate.class) {
                ReturnRate returnRate = (ReturnRate) calculation;
                entry.setY((float)returnRate.getReturnRate(i) * 100);
            } else if (this.calculation.getClass() == PresentValOfAnnuity.class) {
                PresentValOfAnnuity presentValOfAnnuity = (PresentValOfAnnuity) calculation;
                entry.setY((float)presentValOfAnnuity.getStartingPrincipal(i));
            } else if (this.calculation.getClass() == Annuity.class) {
                Annuity annuity = (Annuity) calculation;
                entry.setY((float)annuity.getAnnuity(i));
            } else if (this.calculation.getClass() == Mortgage.class) {
                Mortgage mortgage = (Mortgage) calculation;
                entry.setY((float)mortgage.getMonthlyPayment(i));
            } else if (this.calculation.getClass() == Retirement.class) {
                Retirement retirement = (Retirement) calculation;
                entry.setY((float)retirement.getAnnualRetirementIncome(i));
            } else if (this.calculation.getClass() == BondYield.class) {
                BondYield bondYield = (BondYield) calculation;
                entry.setY((float)bondYield.getYield(i) * 100);
            }
            // add the entry
            entries.add(entry);
        }
        // create new data set
        BarDataSet lineDataSet = new BarDataSet(entries, "Result");
        // set the color
        lineDataSet.setColor(getResources().getColor(R.color.colorPrimary));
        // new line data
        BarData lineData = new BarData(lineDataSet);
        // set the barchart to the line data
        barChart.setData(lineData);
        // clear the description
        Description description = new Description();
        description.setText("");
        // set the description
        barChart.setDescription(description);
        // refresh
        barChart.invalidate();

    }

    private boolean validateTitleName(String title) {
        // declaring a new validated boolean type variable
        boolean validated = true;
        // new arraylist title
        ArrayList<Calculation> calculations = new ArrayList<>();
        // add all of the title to the new arraylist
        calculations.addAll(db.getAllCalculation());
        // check for each of the title if it has existed
        for (Calculation calculation : calculations) {
            if (calculation.getTitle().equals(title)) {
                validated = false;
            }
        }
        return validated;
    }

    private DialogInterface.OnClickListener cancelButtonOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
        }
    };

    private View.OnClickListener showDialogOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // show the dialog first
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            // dialog view
            View dialogView = layoutInflater.inflate(R.layout.dialog_calculation_name, null);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            // set the view of the dialog builder
            alertDialogBuilder.setView(dialogView);
            // create new edittext and assign it to the edit text on the dialog view
            final EditText titleInput = dialogView.findViewById(R.id.calculation_name);
            // set the dialog cancellable
            alertDialogBuilder.setCancelable(false);
            // set the button
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    if (validateTitleName(titleInput.getText().toString()) && !titleInput.getText().toString().isEmpty()) {
                        calculation.setTitle(titleInput.getText().toString());
                        // if it is validated then put on the database
                        // insert the entry to the database
                        db.insertCalculation(calculation);
                        // inform the user it has been added
                        Toasty.success(getContext(), "Added!", Toast.LENGTH_SHORT, true).show();
                        // dismiss the dialog
                        dialog.dismiss();
                    } else {
                        // inform the user to use another name
                        Toasty.error(getContext(), "Use another name or enter a name! Error adding", Toast.LENGTH_SHORT, true).show();

                    }
                }
            });
            alertDialogBuilder.setNegativeButton("Cancel", cancelButtonOnClickListener);
            // create the alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show
            alertDialog.show();
        }
    };

    private void displayTable(double years) {
        // each of the year on the list
        for (int i = 1; i <= years; i++) {
            // create a new row
            TableRow tableRow = new TableRow(getContext());
            // set the layout parameters
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            // create new layout inflater to inflate
            LayoutInflater inflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // inflate the layout
            View view = inflater.inflate(R.layout.row_year_table_result, null);
            // assigning it to the textview
            TextView year = view.findViewById(R.id.year_text_result);
            // set the value
            year.setText(String.valueOf(i));
            // add to the row
            tableRow.addView(year);
            // create the new inflater
            LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // inflate it
            View anView = layoutInflater.inflate(R.layout.row_result_table_result, null);
            // the result textview
            TextView result = anView.findViewById(R.id.result_text_result);
            // assign the result for each of the year
            if (this.calculation.getClass() == CompoundInterest.class) {
                CompoundInterest compoundInterest = (CompoundInterest)calculation;
                result.setText(String.format("$%s", this.formatter.format(compoundInterest.getFutureValue(i))));
            } else if (this.calculation.getClass() == PresentValue.class) {
                PresentValue presentValue = (PresentValue) calculation;
                result.setText(String.format("$%s", this.formatter.format(presentValue.getPresentValue(i))));
            } else if (this.calculation.getClass() == ReturnRate.class) {
                ReturnRate returnRate = (ReturnRate) calculation;
                result.setText(String.format("%.2f%s", returnRate.getReturnRate(i) * 100, "%"));
            } else if (this.calculation.getClass() == PresentValOfAnnuity.class) {
                PresentValOfAnnuity presentValOfAnnuity = (PresentValOfAnnuity) calculation;
                result.setText(String.format("$%s", this.formatter.format(presentValOfAnnuity.getStartingPrincipal(i))));
            } else if (this.calculation.getClass() == Annuity.class) {
                Annuity annuity = (Annuity) calculation;
                result.setText(String.format("$%s", this.formatter.format(annuity.getAnnuity(i))));
            } else if (this.calculation.getClass() == Mortgage.class) {
                Mortgage mortgage = (Mortgage) calculation;
                result.setText(String.format("$%s", this.formatter.format(mortgage.getMonthlyPayment(i))));
            } else if (this.calculation.getClass() == Retirement.class) {
                Retirement retirement = (Retirement) calculation;
                result.setText(String.format("$%s", this.formatter.format(retirement.getAnnualRetirementIncome(i))));
            } else if (this.calculation.getClass() == BondYield.class) {
                BondYield bondYield = (BondYield) calculation;
                result.setText(String.format("%.2f%s", bondYield.getYield(i) * 100, "%"));
            }
            // add the result to the row
            tableRow.addView(result);
            // add the row to the table
            annuityTable.addView(tableRow);
        }
    }
}
