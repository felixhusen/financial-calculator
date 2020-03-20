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

import com.ffx.fcalculator.R;
import com.ffx.fcalculator.activities.CalculationResultActivity;
import com.ffx.fcalculator.calculation.PresentValOfAnnuity;
import com.ffx.fcalculator.calculation.PropertyCashflow;
import com.savvyapps.togglebuttonlayout.Toggle;
import com.savvyapps.togglebuttonlayout.ToggleButtonLayout;

import java.util.List;

import es.dmoral.toasty.Toasty;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class PropertyCashflowFragment extends Fragment {
    private EditText propertyPrice, propertyDeposit, rentPerWeek, mortgageInterestRate, waterRate, councilRate, additionalExpenses, landTax, annualLandlordInsurance, annualPropertyFees;
    private Button calculateButton;
    private ToggleButtonLayout waterToggle, councilToggle, expensesToggle, landTaxToggle;
    public PropertyCashflowFragment() {
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
        View view = inflater.inflate(R.layout.fragment_property_cashflow, container, false);
        // set nav title:
        getActivity().setTitle(R.string.nav_property_cashflow);
        // initializing
        propertyPrice = view.findViewById(R.id.pc_property_price);
        propertyDeposit = view.findViewById(R.id.pc_property_deposit);
        rentPerWeek = view.findViewById(R.id.pc_rent_per_week);
        mortgageInterestRate = view.findViewById(R.id.pc_mortgage_interest_rate);
        waterRate = view.findViewById(R.id.pc_water_rate);
        councilRate = view.findViewById(R.id.pc_council_rate);
        additionalExpenses = view.findViewById(R.id.pc_additional_expenses);
        landTax = view.findViewById(R.id.pc_land_tax);
        annualLandlordInsurance = view.findViewById(R.id.pc_annual_il);
        annualPropertyFees = view.findViewById(R.id.pc_annual_property_fee);
        calculateButton = view.findViewById(R.id.pc_calculate_button);
        waterToggle = view.findViewById(R.id.pc_water_rate_toggle);
        councilToggle = view.findViewById(R.id.pc_council_toggle);
        expensesToggle = view.findViewById(R.id.pc_additional_expenses_toggle);
        landTaxToggle = view.findViewById(R.id.pc_land_tax_toggle);
        // set default values
        waterToggle.setToggled(R.id.quaterly_toggle, true);
        councilToggle.setToggled(R.id.quaterly_toggle, true);
        expensesToggle.setToggled(R.id.quaterly_toggle, true);
        landTaxToggle.setToggled(R.id.quaterly_toggle, true);
        // set the onclick
        calculateButton.setOnClickListener(calculateButtonOnClickListener);
        return view;
    }


    private View.OnClickListener calculateButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                double pp = Double.parseDouble(propertyPrice.getText().toString());
                double pd = Double.parseDouble(propertyDeposit.getText().toString());
                double rpw = Double.parseDouble(rentPerWeek.getText().toString());
                double mir = Double.parseDouble(mortgageInterestRate.getText().toString()) / 100.0;
                double wr = Double.parseDouble(waterRate.getText().toString());
                double cr = Double.parseDouble(councilRate.getText().toString());
                double ae = Double.parseDouble(additionalExpenses.getText().toString());
                double lt = Double.parseDouble(landTax.getText().toString());
                double ail = Double.parseDouble(annualLandlordInsurance.getText().toString());
                double apf = Double.parseDouble(annualPropertyFees.getText().toString()) / 100.0;
                boolean waterOption = true;
                if (waterToggle.selectedToggles().get(0).getId() == R.id.quaterly_toggle) {
                    waterOption = false;
                } else {
                    waterOption = true;
                }
                boolean councilOption = true;
                if (councilToggle.selectedToggles().get(0).getId() == R.id.quaterly_toggle) {
                    councilOption = false;
                } else {
                    councilOption = true;
                }
                boolean expensesOption = true;
                if (expensesToggle.selectedToggles().get(0).getId() == R.id.quaterly_toggle) {
                    expensesOption = false;
                } else {
                    expensesOption = true;
                }
                boolean landTaxOption = true;
                if (landTaxToggle.selectedToggles().get(0).getId() == R.id.quaterly_toggle) {
                    landTaxOption = false;
                } else {
                    landTaxOption = true;
                }
                PropertyCashflow propertyCashflow = new PropertyCashflow(pp, pd, rpw, mir, wr, waterOption, cr, councilOption, ae, expensesOption, lt, landTaxOption, ail, apf);

                Intent intent = new Intent(getContext(), CalculationResultActivity.class);
                // make bundle and put the value that will be transferred to new activity
                Bundle bundle = new Bundle();
                bundle.putSerializable("calculation", propertyCashflow);
                intent.putExtras(bundle);
                // start the activity
                startActivity(intent);

            } catch (NumberFormatException exception) {
                Toasty.error(getContext(), getString(R.string.fields_validation_failed), Toast.LENGTH_LONG, true).show();
            }
        }
    };
}
