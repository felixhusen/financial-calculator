package com.ffx.fcalculator.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ffx.fcalculator.calculation.Calculation;
import com.ffx.fcalculator.DatabaseHelper;
import com.ffx.fcalculator.R;
import com.ffx.fcalculator.SwipeableRecyclerViewTouchListener;
import com.ffx.fcalculator.adapter.SavedCalculationAdapter;

import java.util.ArrayList;
import java.util.Collections;

import es.dmoral.toasty.Toasty;

public class SavedCalculationFragment extends Fragment {

    private RecyclerView recyclerView;
    private SavedCalculationAdapter savedCalculationAdapter;
    private ArrayList<Calculation> savedCalculations = new ArrayList<>();
    private DatabaseHelper db;

    private LinearLayout noItemLayout;

    public SavedCalculationFragment() {
        // Required empty public constructor
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Change the title of the current activity to Saved Calculation
        getActivity().setTitle(R.string.nav_saved_calculation);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_saved_calculation, container, false);


        // initialising recyclerView
        noItemLayout = view.findViewById(R.id.no_item_layout);
        recyclerView = view.findViewById(R.id.rview_calculation);

        // set some param
        recyclerView.setHasFixedSize(true);
        // initialising layoutmanager
        RecyclerView.LayoutManager recyclerViewLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        savedCalculationAdapter = new SavedCalculationAdapter(getContext(), savedCalculations);
        recyclerView.setAdapter(savedCalculationAdapter);

        // .. coding for swipe to delete .. //
        SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(recyclerView,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipeRight(int position) {
                                return true;
                            }

                            @Override
                            public boolean canSwipeLeft(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                // search the index
                                for (int position : reverseSortedPositions) {
                                    // delete calculation by the index on the database
                                    db.deleteCalculation(savedCalculations.get(position));
                                    // remove from the list
                                    savedCalculations.remove(position);
                                }
                                // tell the adapter that the data has changed
                                savedCalculationAdapter.notifyDataSetChanged();
                                // if no data, display the no data information
                                if (savedCalculations.size() == 0) {
                                    noItemLayout.setVisibility(View.VISIBLE);
                                } else {
                                    noItemLayout.setVisibility(View.GONE);
                                }
                                // tell the user that it has been deleted
                                Snackbar.make(recyclerView, "Deleted",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    db.deleteCalculation(savedCalculations.get(position));
                                    savedCalculations.remove(position);
                                }
                                savedCalculationAdapter.notifyDataSetChanged();
                                if (savedCalculations.size() == 0) {
                                    noItemLayout.setVisibility(View.VISIBLE);
                                } else {
                                    noItemLayout.setVisibility(View.GONE);
                                }
                                Snackbar.make(recyclerView, "Deleted",
                                        Snackbar.LENGTH_SHORT)
                                        .show();
                            }

        });

        recyclerView.addOnItemTouchListener(swipeTouchListener);

        db = new DatabaseHelper(getContext());

        // check if first run
        if (isFirstRun()) {
            SharedPreferences.Editor editor = getContext().getSharedPreferences("first_run", Context.MODE_PRIVATE).edit();
            editor.putBoolean("first_run", false);
            editor.commit();
            Toasty.info(getContext(), "Swipe card to delete").show();
        }

        getAllSavedCalculation();
        return view;
    }

    private boolean isFirstRun() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("first_run", Context.MODE_PRIVATE);
        boolean firstRun = sharedPreferences.getBoolean("first_run", true);
        return firstRun;
    }

    private void getAllSavedCalculation() {
        savedCalculations.addAll(db.getAllCalculation());
        Collections.reverse(savedCalculations);
        savedCalculationAdapter.notifyDataSetChanged();
        if (savedCalculations.size() == 0) {
            noItemLayout.setVisibility(View.VISIBLE);
        } else {
            noItemLayout.setVisibility(View.GONE);
        }
    }

}
