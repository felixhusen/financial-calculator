package com.ffx.fcalculator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ffx.fcalculator.activities.CalculationResultActivity;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.calculation.Mortgage;
import com.ffx.fcalculator.R;

import es.dmoral.toasty.Toasty;

public class MortgageFragment extends Fragment {
    // fields:
    private EditText loanAmountEd, mortgageRateEd, yearsToPayEd;
    private double loanAmount, mortgageRate, yearsToPay;
    private Button calculateButton;
    private RadioGroup mortgageOptionRg;
    private boolean mortgageOption = true;

    public MortgageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mortgage, container, false);
        // set nav title
        getActivity().setTitle(R.string.nav_mortgage);
        // initialize every field
        loanAmountEd = view.findViewById(R.id.mortgage_loan_amount);
        mortgageRateEd = view.findViewById(R.id.mortgage_rate);
        yearsToPayEd = view.findViewById(R.id.mortgage_years);
        calculateButton = view.findViewById(R.id.mortgage_calculate_button);
        mortgageOptionRg = view.findViewById(R.id.mortgage_option);
        // set radio group listener
        mortgageOptionRg.setOnCheckedChangeListener((RadioGroup radioGroup, int i) -> {
            if (i == R.id.one_year) {
                mortgageOption = true;
                yearsToPayEd.setVisibility(View.GONE);
            } else {
                mortgageOption = false;
                yearsToPayEd.setVisibility(View.VISIBLE);
            }
        });
        // add set on click listener to calculate button
        calculateButton.setOnClickListener(contentView -> {
            if (mortgageOption == true) {
                yearsToPayEd.setText("1");
            }
            try {
                // get the fields that were input by user and assign them to the data fields
                loanAmount = Double.parseDouble(loanAmountEd.getText().toString());
                mortgageRate = Double.parseDouble(mortgageRateEd.getText().toString());
                yearsToPay = Double.parseDouble(yearsToPayEd.getText().toString());
                if (yearsToPay <= Calculation.YEARS_LIMIT) {
                    // make a new intent object to transfer to new activity
                    Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                    // make bundle and put the value that will be transferred to new activity
                    Bundle bundle = new Bundle();
                    if (mortgageOption == true) {
                        yearsToPay = 1;
                    }
                    Mortgage mortgage = new Mortgage(loanAmount, mortgageRate, yearsToPay);
                    bundle.putSerializable("calculation", mortgage);
                    intent.putExtras(bundle);
                    // start the activity
                    startActivity(intent);
                } else {
                    Toasty.error(getContext(), getString(R.string.year_limit), Toast
                            .LENGTH_LONG, true).show();
                }
            } catch (NumberFormatException exception){
                // inform the user that the fields are required!
                Toasty.error(getContext(), getString(R.string.fields_validation_failed), Toast.LENGTH_LONG, true).show();
            }
        });
        return view;
    }
}
