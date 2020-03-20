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
import com.ffx.fcalculator.calculation.ReturnRate;
import com.ffx.fcalculator.R;

import es.dmoral.toasty.Toasty;

public class ReturnRateFragment extends Fragment {
    // Fields:
    private EditText presentValueEd, futureValueEd, yearsEd;
    private Button calculateButton;
    private double presentValue, futureValue, years;
    public ReturnRateFragment() {
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
        View view = inflater.inflate(R.layout.fragment_return_rate, container, false);
        getActivity().setTitle(R.string.nav_return_rate);
        // initialize the fields
        futureValueEd = view.findViewById(R.id.return_rate_future_value);
        yearsEd = view.findViewById(R.id.return_rate_years);
        presentValueEd = view.findViewById(R.id.return_rate_present_value);
        calculateButton = view.findViewById(R.id.return_rate_calculate_button);
        // add onclick listener to the button
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // get the fields that were input by user and assign them to the data fields
                    futureValue = Double.parseDouble(futureValueEd.getText().toString());
                    years = Double.parseDouble(yearsEd.getText().toString());
                    presentValue = Double.parseDouble(presentValueEd.getText().toString());
                    if (years <= Calculation.YEARS_LIMIT) {
                        // make a new intent object to transfer to new activity
                        Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                        // make bundle and put the value that will be transferred to new activity
                        Bundle bundle = new Bundle();
                        ReturnRate returnRate = new ReturnRate(presentValue, futureValue, years);
                        bundle.putSerializable("calculation", returnRate);
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
