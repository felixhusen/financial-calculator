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
import com.ffx.fcalculator.calculation.CompoundInterest;
import com.ffx.fcalculator.R;
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout;

import es.dmoral.toasty.Toasty;

public class CompoundInterestFragment extends Fragment {

    // Data Fields:
    private EditText currentPrincipalEd, annualAdditionEd, yearsToGrowEd, interestRateEd, interestTimeEd;
    private Button calculateButton;
    private double currentPrincipal, annualAddition, yearsToGrow, interestRate, interestTime;
    private boolean additionAt = true;

    private ToggleButtonLayout compoundInterestToggle;

    public CompoundInterestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set the activity title to compound interest
        getActivity().setTitle(R.string.nav_comp_interest);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compound_interest, container, false);
        // initializing every datafields that have been declared
        currentPrincipalEd = view.findViewById(R.id.comp_current_principal);
        annualAdditionEd = view.findViewById(R.id.comp_annual_addition);
        yearsToGrowEd = view.findViewById(R.id.comp_years_to_grow);
        interestRateEd = view.findViewById(R.id.comp_interest_rate);
        interestTimeEd = view.findViewById(R.id.comp_interest_time);
        calculateButton = view.findViewById(R.id.comp_calculate_button);
        compoundInterestToggle = view.findViewById(R.id.cp_toggle);
        // set the toggle
        compoundInterestToggle.setToggled(R.id.beginning_toggle, true);
        // set what action is done when calculate button is pressed
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    // get the fields that were input by user and assign them to the data fields
                    currentPrincipal = Double.parseDouble(currentPrincipalEd.getText().toString());
                    annualAddition = Double.parseDouble(annualAdditionEd.getText().toString());
                    yearsToGrow = Double.parseDouble(yearsToGrowEd.getText().toString());
                    interestRate = Double.parseDouble(interestRateEd.getText().toString());
                    interestTime = Double.parseDouble(interestTimeEd.getText().toString());
                    // if the toggle is beginning then update the variable
                    if (compoundInterestToggle.selectedToggles().get(0).getId() == R.id.beginning_toggle) {
                        additionAt = true;
                    } else {
                        additionAt = false;
                    }
                    // years limit constraint
                    if (yearsToGrow <= Calculation.YEARS_LIMIT) {
                        // make a new intent object to transfer to new activity
                        Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                        // make bundle and put the value that will be transferred to new activity
                        Bundle bundle = new Bundle();

                        CompoundInterest compoundInterest = new CompoundInterest(currentPrincipal, annualAddition, yearsToGrow, interestRate, interestTime, additionAt);
                        bundle.putSerializable("calculation", compoundInterest);
                        intent.putExtras(bundle);
                        // start the activity
                        startActivity(intent);
                    } else {
                        Toasty.error(getContext(), getString(R.string.year_limit), Toast
                                .LENGTH_LONG, true).show();
                    }
                } catch (NumberFormatException exception){
                    // inform the user that the fields are required
                    Toasty.error(getContext(), getString(R.string.fields_validation_failed), Toast.LENGTH_LONG, true).show();
                }

            }
        });
        return view;
    }
}
