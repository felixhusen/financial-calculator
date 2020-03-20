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
import com.ffx.fcalculator.calculation.PresentValOfAnnuity;
import com.ffx.fcalculator.R;
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout;

import es.dmoral.toasty.Toasty;

public class PresValOfAnnuityFragment extends Fragment {
    private EditText annualPayoutEd, growthRateEd, yearsPayOutEd;
    private ToggleButtonLayout presValToggle;
    private double annualPayout, growthRate, yearsPayOut;
    private Button calculateButton;
    private boolean payoutAt = true;

    public PresValOfAnnuityFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pres_val_of_annuity, container, false);
        // set nav title:
        getActivity().setTitle(R.string.nav_pres_value_of_annuity);
        annualPayoutEd = view.findViewById(R.id.pres_val_annuity_annual_payout);
        growthRateEd = view.findViewById(R.id.pres_val_annuity_growth_rate);
        yearsPayOutEd = view.findViewById(R.id.pres_val_annuity_years_pay_out);
        presValToggle = view.findViewById(R.id.pres_val_toggle);
        calculateButton = view.findViewById(R.id.pres_val_annuity_calculate_button);
        // set the default toggle
        presValToggle.setToggled(R.id.beginning_toggle, true);
        // add set on click listener to calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // get the fields that were input by user and assign them to the data fields
                    annualPayout = Double.parseDouble(annualPayoutEd.getText().toString());
                    growthRate = Double.parseDouble(growthRateEd.getText().toString());
                    yearsPayOut = Double.parseDouble(yearsPayOutEd.getText().toString());
                    // update the value of the variable
                    if (presValToggle.selectedToggles().get(0).getId() == R.id.beginning_toggle) {
                        payoutAt = true;
                    } else {
                        payoutAt = false;
                    }
                    if (yearsPayOut <= Calculation.YEARS_LIMIT) {
                        // make a new intent object to transfer to new activity
                        Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                        // make bundle and put the value that will be transferred to new activity
                        Bundle bundle = new Bundle();
                        PresentValOfAnnuity presentValOfAnnuity = new PresentValOfAnnuity(annualPayout, growthRate, yearsPayOut, payoutAt);
                        bundle.putSerializable("calculation", presentValOfAnnuity);
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
