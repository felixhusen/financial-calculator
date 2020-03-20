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
import com.ffx.fcalculator.calculation.PresentValue;
import com.ffx.fcalculator.R;

import es.dmoral.toasty.Toasty;

public class PresentValueFragment extends Fragment {
    // Fields:
    private EditText futureValueEd, yearsEd, discountRateEd;
    private Button calculateButton;
    private double futureValue, years, discountRate;
    public PresentValueFragment() {
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
        View view = inflater.inflate(R.layout.fragment_present_value, container, false);
        getActivity().setTitle(R.string.nav_pres_value);
        // initialize the fields
        futureValueEd = view.findViewById(R.id.pres_val_future_value);
        yearsEd = view.findViewById(R.id.pres_val_years);
        discountRateEd = view.findViewById(R.id.pres_val_discount_rate);
        calculateButton = view.findViewById(R.id.pres_val_calculate_button);
        // add onclick listener to the button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // get the fields that were input by user and assign them to the data fields
                    futureValue = Double.parseDouble(futureValueEd.getText().toString());
                    years = Double.parseDouble(yearsEd.getText().toString());
                    discountRate = Double.parseDouble(discountRateEd.getText().toString());
                    if (years <= Calculation.YEARS_LIMIT) {
                        // make a new intent object to transfer to new activity
                        Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                        // make bundle and put the value that will be transferred to new activity
                        Bundle bundle = new Bundle();
                        PresentValue presentValue = new PresentValue(futureValue, years, discountRate);
                        bundle.putSerializable("calculation", presentValue);
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
