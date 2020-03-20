package com.ffx.fcalculator.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.ffx.fcalculator.calculation.Annuity;
import com.ffx.fcalculator.calculation.BondYield;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.calculation.CompoundInterest;
import com.ffx.fcalculator.calculation.Mortgage;
import com.ffx.fcalculator.calculation.PresentValOfAnnuity;
import com.ffx.fcalculator.calculation.PresentValue;
import com.ffx.fcalculator.calculation.Retirement;
import com.ffx.fcalculator.calculation.ReturnRate;
import com.ffx.fcalculator.DatabaseHelper;
import com.ffx.fcalculator.R;
import com.ffx.fcalculator.calculation.*;
import com.ffx.fcalculator.fragments.CalculationResultFragment;
import com.ffx.fcalculator.fragments.PropertyCashflowResultFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class CalculationResultActivity extends AppCompatActivity {

    private DatabaseHelper db;

    private Calculation calculation;

    private Bundle bundle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation_result);

        // set the back button to be displayed
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setElevation(0);

        // initialize all of the fields

        db = new DatabaseHelper(this);

        // get the bundle and data that have been transferred
        bundle = getIntent().getExtras();
        // set the onclicklistener for save button

        calculation = (Calculation)bundle.getSerializable("calculation");

        if (calculation.getClass() == PropertyCashflow.class) {
            // display property cashflow result fragment
            PropertyCashflowResultFragment propertyCashflowResult = new PropertyCashflowResultFragment(db, calculation);
            displayFragment(propertyCashflowResult);
        } else {
            // display regular result fragment
            CalculationResultFragment calculationResultFragment = new CalculationResultFragment(db, calculation);
            displayFragment(calculationResultFragment);
        }
    }

    private void displayFragment(Fragment fragment) {
        // Prepare to replace the fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.calculation_result_content, fragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }





}
