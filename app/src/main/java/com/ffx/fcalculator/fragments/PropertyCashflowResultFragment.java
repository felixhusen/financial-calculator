package com.ffx.fcalculator.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ffx.fcalculator.DatabaseHelper;
import com.ffx.fcalculator.R;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.calculation.PropertyCashflow;
import com.savvyapps.togglebuttonlayout.Toggle;
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class PropertyCashflowResultFragment extends Fragment {
    private DatabaseHelper db;
    private PropertyCashflow calculation;
    private TextView grossPropertyYield, propertyPrice, propertyDeposit, rentPerWeek, mortgageInterestRate;
    private TextView waterRateOptionText, waterRate, councilFeeOptionText, councilFee, additionalExpensesOptionText, additionalExpenses;
    private TextView landTaxOptionText, landTax, annualLandlordInsurance, annualPropertyFee;
    private TextView propertyCashflowResult;
    private ToggleButtonLayout propertyCashflowToggle;
    private DecimalFormat formatter;

    private Button saveCalculationButton;

    private static final String WATER_RATE_TEXT = "Water Rate";
    private static final String ANNUAL = "Annual";
    private static final String QUATERLY = "Quaterly";

    private static final String COUNCIL_FEE_TEXT = "Council Fee";
    private static final String ADDITIONAL_EXPENSES_TEXT = "Additional Expenses";
    private static final String LAND_TAX_TEXT = "Land Tax";
    public PropertyCashflowResultFragment() {

    }

    @SuppressLint("ValidFragment")
    public PropertyCashflowResultFragment(DatabaseHelper db, Calculation calculation) {
        this.db = db;
        this.calculation = (PropertyCashflow)calculation;
        // set the number format
        formatter = new DecimalFormat("#,###.00");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_property_cashflow_result, container, false);
        // set nav title:
        getActivity().setTitle(R.string.nav_property_cashflow);
        // initialize
        grossPropertyYield = view.findViewById(R.id.pcr_gross_pyield);
        propertyPrice = view.findViewById(R.id.pcr_property_price);
        propertyDeposit = view.findViewById(R.id.pcr_property_deposit);
        rentPerWeek = view.findViewById(R.id.pcr_rent_per_week);
        mortgageInterestRate = view.findViewById(R.id.pcr_mortgage_interest_rate);
        waterRateOptionText = view.findViewById(R.id.pcr_water_rate_text);
        waterRate = view.findViewById(R.id.pcr_water_rate);
        councilFeeOptionText = view.findViewById(R.id.pcr_council_fee_text);
        councilFee = view.findViewById(R.id.pcr_council_fee);
        additionalExpensesOptionText = view.findViewById(R.id.pcr_additional_fee_text);
        additionalExpenses = view.findViewById(R.id.pcr_additional_fee);
        landTaxOptionText = view.findViewById(R.id.pcr_land_tax_text);
        landTax = view.findViewById(R.id.pcr_land_tax);
        annualLandlordInsurance = view.findViewById(R.id.pcr_annual_li);
        annualPropertyFee = view.findViewById(R.id.pcr_annual_building_fee);
        propertyCashflowResult = view.findViewById(R.id.pcr_cashflow_result);
        propertyCashflowToggle = view.findViewById(R.id.pcr_cashflow_result_toggle);
        propertyCashflowToggle.setToggled(R.id.annual_toggle, true);
        propertyCashflowToggle.setOnToggledListener(new Function2<Toggle, Boolean, Unit>() {
            @Override
            public Unit invoke(Toggle toggle, Boolean aBoolean) {
                if (toggle.getId() == R.id.annual_toggle) {
                    propertyCashflowResult.setText(String.format( "$%s", formatter.format(calculation.getAnnualCashflow())));
                } else if (toggle.getId() == R.id.monthly_toggle) {
                    propertyCashflowResult.setText(String.format( "$%s", formatter.format(calculation.getMonthlyCashflow())));
                } else {
                    propertyCashflowResult.setText(String.format( "$%s", formatter.format(calculation.getWeeklyCashflow())));
                }
                return null;
            }
        });

        saveCalculationButton = view.findViewById(R.id.pcr_calculation_save_button);
        saveCalculationButton.setOnClickListener(showDialogOnClickListener);

        // display the result
        displayResult();
        return view;
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

    private void displayResult() {
        grossPropertyYield.setText(String.format( "%s%s", this.formatter.format(calculation.getGrossPropertyYield() * 100), "%"));
        propertyPrice.setText(String.format( "$%s", this.formatter.format(calculation.getPropertyPrice())));
        propertyDeposit.setText(String.format( "$%s", this.formatter.format(calculation.getPropertyDeposit())));
        rentPerWeek.setText(String.format( "$%s", this.formatter.format(calculation.getRentPerWeek())));
        mortgageInterestRate.setText(String.format( "%s%s", this.formatter.format(calculation.getMortgageInterestRate() * 100), "%"));

        if (calculation.isWaterRateOption()) {
            // annually
            waterRateOptionText.setText(ANNUAL + " " + WATER_RATE_TEXT);
            waterRate.setText(String.format( "$%s", this.formatter.format(calculation.getWaterRate())));
        } else {
            // quaterly
            waterRateOptionText.setText(QUATERLY + " " + WATER_RATE_TEXT);
            waterRate.setText(String.format( "$%s", this.formatter.format(calculation.getWaterRate() / 4)));
        }


        if (calculation.isCouncilOption()) {
            // annually
            councilFeeOptionText.setText(ANNUAL + " " + COUNCIL_FEE_TEXT);
            councilFee.setText(String.format( "$%s", this.formatter.format(calculation.getCouncil())));
        } else {
            // quaterly
            councilFeeOptionText.setText(QUATERLY + " " + COUNCIL_FEE_TEXT);
            councilFee.setText(String.format( "$%s", this.formatter.format(calculation.getCouncil() / 4)));

        }


        if (calculation.isExpensesOption()) {
            // annually
            additionalExpensesOptionText.setText(ANNUAL + " " + ADDITIONAL_EXPENSES_TEXT);
            additionalExpenses.setText(String.format( "$%s", this.formatter.format(calculation.getAdditionalExpenses())));
        } else {
            // quaterly
            additionalExpensesOptionText.setText(QUATERLY + " " + ADDITIONAL_EXPENSES_TEXT);
            additionalExpenses.setText(String.format( "$%s", this.formatter.format(calculation.getAdditionalExpenses() / 4)));
        }


        if (calculation.isLanTaxOption()) {
            // annually
            landTaxOptionText.setText(ANNUAL + " " + LAND_TAX_TEXT);
            landTax.setText(String.format( "$%s", this.formatter.format(calculation.getLandTax())));
        } else {
            // quaterly
            landTaxOptionText.setText(QUATERLY + " " + LAND_TAX_TEXT);
            landTax.setText(String.format( "$%s", this.formatter.format(calculation.getLandTax() / 4)));
        }

        annualLandlordInsurance.setText(String.format( "$%s", this.formatter.format(calculation.getAnnualInsuranceLandlord())));
        annualPropertyFee.setText(String.format( "%s%s", this.formatter.format(calculation.getAnnualPropertyFees() * 100), "%"));

        // set to annual by default
        propertyCashflowResult.setText(String.format( "$%s", this.formatter.format(calculation.getAnnualCashflow())));

    }
}
