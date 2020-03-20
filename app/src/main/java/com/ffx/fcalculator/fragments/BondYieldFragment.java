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
import com.ffx.fcalculator.calculation.BondYield;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.R;

import es.dmoral.toasty.Toasty;

public class BondYieldFragment extends Fragment {
    private EditText currentPriceEd, perValueEd, couponRateEd, yearsToMaturityEd;
    private double currentPrice, perValue, couponRate, yearsToMaturity;
    private Button calculateButton;
    public BondYieldFragment() {
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
        View view = inflater.inflate(R.layout.fragment_bond_yield, container, false);
        // set title:
        getActivity().setTitle(R.string.nav_bond_yield);
        // initialize fields:
        currentPriceEd = view.findViewById(R.id.bond_yield_current_price);
        perValueEd = view.findViewById(R.id.bond_yield_per_value);
        couponRateEd = view.findViewById(R.id.bond_yield_coupon_rate);
        yearsToMaturityEd = view.findViewById(R.id.bond_yield_years);
        calculateButton = view.findViewById(R.id.bond_yield_calculate_button);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // get the fields that were input by user and assign them to the data fields
                    currentPrice = Double.parseDouble(currentPriceEd.getText().toString());
                    perValue = Double.parseDouble(perValueEd.getText().toString());
                    couponRate = Double.parseDouble(couponRateEd.getText().toString());
                    yearsToMaturity = Double.parseDouble(yearsToMaturityEd.getText().toString());
                    // if the year is below the limit or the same as the limit
                    if (yearsToMaturity <= Calculation.YEARS_LIMIT) {
                        // make a new intent object to transfer to new activity
                        Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                        // make bundle and put the value that will be transferred to new activity
                        Bundle bundle = new Bundle();
                        BondYield bondYield = new BondYield(currentPrice, perValue, couponRate, yearsToMaturity);
                        bundle.putSerializable("calculation", bondYield);
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
