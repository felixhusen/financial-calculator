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
import com.ffx.fcalculator.calculation.Annuity;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.R;
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout;

import es.dmoral.toasty.Toasty;

public class AnnuityFragment extends Fragment {
    // Fields:
    private EditText startingPrincipalEd, growthRateEd, yearsPayOutEd;
    private boolean payoutAt = true;
    private Button calculateButton;
    private double startingPrincipal, growthRate, yearsPayOut;

    private ToggleButtonLayout annuityToggle;

    public AnnuityFragment() {
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
        View view = inflater.inflate(R.layout.fragment_annuity, container, false);
        // set the title of the activity:
        getActivity().setTitle(R.string.nav_annuity);
        // initialize fields:
        startingPrincipalEd = view.findViewById(R.id.annuity_starting_principal);
        growthRateEd = view.findViewById(R.id.annuity_growth_rate);
        yearsPayOutEd = view.findViewById(R.id.annuity_years_pay_out);
        calculateButton = view.findViewById(R.id.annuity_calculate_button);
        annuityToggle = view.findViewById(R.id.an_toggle);
        // set the default toggle
        annuityToggle.setToggled(R.id.beginning_toggle, true);
        // add set on click listener to calculate button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    startingPrincipal = Double.parseDouble(startingPrincipalEd.getText().toString());
                    growthRate = Double.parseDouble(growthRateEd.getText().toString());
                    yearsPayOut = Double.parseDouble(yearsPayOutEd.getText().toString());
                    // update the value
                    if (annuityToggle.selectedToggles().get(0).getId() == R.id.beginning_toggle) {
                        payoutAt = true;
                    } else {
                        payoutAt = false;
                    }
                    if (yearsPayOut <= Calculation.YEARS_LIMIT) {
                        // make a new intent object to transfer to new activity
                        Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                        // make bundle and put the value that will be transferred to new activity
                        Bundle bundle = new Bundle();
                        Annuity annuity = new Annuity(startingPrincipal, growthRate, yearsPayOut, payoutAt);
                        bundle.putSerializable("calculation", annuity);
                        intent.putExtras(bundle);
                        // start the activity
                        startActivity(intent);
                    } else {
                        Toasty.error(getContext(), getString(R.string.year_limit), Toast.LENGTH_LONG, true).show();
                    }

                } catch (NumberFormatException exception) {
                    Toasty.error(getContext(), getString(R.string.fields_validation_failed), Toast.LENGTH_LONG, true).show();
                }

            }
        });
        return view;
    }
}
