package com.ffx.fcalculator.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ffx.fcalculator.activities.CalculationResultActivity;
import com.ffx.fcalculator.calculation.Annuity;
import com.ffx.fcalculator.calculation.BondYield;
import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.calculation.CompoundInterest;
import com.ffx.fcalculator.calculation.Mortgage;
import com.ffx.fcalculator.calculation.PresentValOfAnnuity;
import com.ffx.fcalculator.calculation.PresentValue;
import com.ffx.fcalculator.calculation.Retirement;
import com.ffx.fcalculator.calculation.ReturnRate;
import com.ffx.fcalculator.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SavedCalculationAdapter extends RecyclerView.Adapter<SavedCalculationAdapter.ViewHolder> {

    /* ========== DATA FIELD ========== */

    private ArrayList<Calculation> savedCalculations;
    private Context context;
    private DecimalFormat formatter;

    /* ========== VIEW HOLDER CLASS ========== */

    public static class ViewHolder extends RecyclerView.ViewHolder {

        /* ========== DATA FIELD ========== */

        public TextView title, result, type, date;
        public RelativeLayout parentLayout;

        /* ========== CONSTRUCTOR ========== */

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.sc_title);
            result = view.findViewById(R.id.sc_result);
            type = view.findViewById(R.id.sc_type);
            date = view.findViewById(R.id.sc_date);
            parentLayout = view.findViewById(R.id.parent_layout);
        }
    }

    /* ========== CONSTRUCTOR ========== */

    public SavedCalculationAdapter(Context context, ArrayList<Calculation> savedCalculations) {
        this.savedCalculations = savedCalculations;
        this.context = context;
        formatter = new DecimalFormat("#,###.00");
    }

    /* ========== METHODS ========== */

    @Override
    public SavedCalculationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_saved_calculation, parent, false);
        // assign the viewholder to the view that has just been declared
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // setting the values for the informations
        holder.title.setText(savedCalculations.get(position).getTitle());
        holder.result.setText(String.format("$%s", this.formatter.format(savedCalculations.get(position).getResult())));
        if (savedCalculations.get(position).getClass() == CompoundInterest.class) {
            // compound interest
            holder.type.setText(R.string.nav_comp_interest);
        } else if (savedCalculations.get(position).getClass() == PresentValue.class) {
            // present value
            holder.type.setText(R.string.nav_pres_value);
        } else if (savedCalculations.get(position).getClass() == ReturnRate.class) {
            // return rate
            holder.type.setText(R.string.nav_return_rate);
            holder.result.setText(String.format("%.2f%s", savedCalculations.get(position).getResult() * 100, "%"));
        } else if (savedCalculations.get(position).getClass() == BondYield.class) {
            // bond yield
            holder.type.setText(R.string.nav_bond_yield);
        } else if (savedCalculations.get(position).getClass() == Annuity.class) {
            // annuity
            holder.type.setText(R.string.nav_annuity);
        } else if (savedCalculations.get(position).getClass() == PresentValOfAnnuity.class) {
            // present value of annuity
            holder.type.setText(R.string.nav_pres_value_of_annuity);
        } else if (savedCalculations.get(position).getClass() == Mortgage.class) {
            // mortgage
            holder.type.setText(R.string.nav_mortgage);
        } else if (savedCalculations.get(position).getClass() == Retirement.class) {
            // retirement
            holder.type.setText(R.string.nav_retirement);
        } else {
            // property cashflow
            holder.type.setText("Property Cashflow");
        }
        holder.date.setText(savedCalculations.get(position).getDate());
        // set the onclicklistener for touching the card
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // obtain the calculation
                Calculation calculation = savedCalculations.get(position);

                // new intent
                Intent intent = new Intent(context, CalculationResultActivity.class);
                // make bundle and put the value that will be transferred to new activity
                Bundle bundle = new Bundle();

                bundle.putSerializable("calculation", calculation);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
    }

    // get the number of entries
    @Override
    public int getItemCount() {
        return savedCalculations.size();
    }
}
