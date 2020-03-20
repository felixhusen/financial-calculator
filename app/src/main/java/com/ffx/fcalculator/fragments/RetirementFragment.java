package com.ffx.fcalculator.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ffx.fcalculator.activities.CalculationResultActivity;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.calculation.Retirement;
import com.ffx.fcalculator.R;

import es.dmoral.toasty.Toasty;

public class RetirementFragment extends Fragment {
    // fields:
    private EditText currentPrincipalEd, annualAdditionEd, yearsToGrowEd, preGrowthRateEd, yearsPayOutEd, inGrowthRateEd;
    private double currentPrincipal, annualAddition, yearsToGrow, preGrowthRate, yearsPayOut, inGrowthRate;
    private Button calculateButton;

    public RetirementFragment() {
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
        View view = inflater.inflate(R.layout.fragment_retirement, container, false);
        // set title:
        getActivity().setTitle(R.string.nav_retirement);
        // initialize every field
        currentPrincipalEd = view.findViewById(R.id.retirement_current_principal);
        annualAdditionEd = view.findViewById(R.id.retirement_annual_addition);
        yearsToGrowEd = view.findViewById(R.id.retirement_years_to_grow);
        preGrowthRateEd = view.findViewById(R.id.retirement_pre_growth_rate);
        yearsPayOutEd = view.findViewById(R.id.retirement_years_pay_out);
        inGrowthRateEd = view.findViewById(R.id.retirement_in_growth_rate);
        calculateButton = view.findViewById(R.id.retirement_calculate_button);
        // add onclick listener to the button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // get the fields that were input by user and assign them to the data fields
                    currentPrincipal = Double.parseDouble(currentPrincipalEd.getText().toString());
                    annualAddition = Double.parseDouble(annualAdditionEd.getText().toString());
                    yearsToGrow = Double.parseDouble(yearsToGrowEd.getText().toString());
                    preGrowthRate = Double.parseDouble(preGrowthRateEd.getText().toString());
                    yearsPayOut = Double.parseDouble(yearsPayOutEd.getText().toString());
                    inGrowthRate = Double.parseDouble(inGrowthRateEd.getText().toString());
                    if (yearsPayOut <= Calculation.YEARS_LIMIT && yearsToGrow <= Calculation.YEARS_LIMIT) {
                        // make a new intent object to transfer to new activity
                        Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                        // make bundle and put the value that will be transferred to new activity
                        Bundle bundle = new Bundle();
                        Retirement retirement = new Retirement(currentPrincipal, annualAddition, yearsToGrow, preGrowthRate, yearsPayOut, inGrowthRate);
                        bundle.putSerializable("calculation", retirement);
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
            }
        });
        return view;
    }

}
