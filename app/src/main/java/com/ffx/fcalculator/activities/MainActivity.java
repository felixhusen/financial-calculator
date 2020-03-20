package com.ffx.fcalculator.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.ffx.fcalculator.R;
import com.ffx.fcalculator.fragments.AnnuityFragment;
import com.ffx.fcalculator.fragments.BondYieldFragment;
import com.ffx.fcalculator.fragments.CompoundInterestFragment;
import com.ffx.fcalculator.fragments.MortgageFragment;
import com.ffx.fcalculator.fragments.PresValOfAnnuityFragment;
import com.ffx.fcalculator.fragments.PresentValueFragment;
import com.ffx.fcalculator.fragments.PropertyCashflowFragment;
import com.ffx.fcalculator.fragments.RetirementFragment;
import com.ffx.fcalculator.fragments.ReturnRateFragment;
import com.ffx.fcalculator.fragments.SavedCalculationFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // displaying compoundinterest fragment
        CompoundInterestFragment compoundInterestFragment = new CompoundInterestFragment();
        displayFragment(compoundInterestFragment);

    }

    private void displayFragment(Fragment fragment) {
        // Prepare to replace the fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_content, fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_saved_calculation) {
            SavedCalculationFragment sa = new SavedCalculationFragment();
            displayFragment(sa);
        } else if (id == R.id.nav_comp_interest) {
            CompoundInterestFragment ci = new CompoundInterestFragment();
            displayFragment(ci);
        } else if (id == R.id.nav_present_value) {
            PresentValueFragment pv = new PresentValueFragment();
            displayFragment(pv);
        } else if (id == R.id.nav_return_rate) {
            ReturnRateFragment rr = new ReturnRateFragment();
            displayFragment(rr);
        } else if (id == R.id.nav_annuity) {
            AnnuityFragment an = new AnnuityFragment();
            displayFragment(an);
        } else if (id == R.id.nav_present_value_annuity) {
            PresValOfAnnuityFragment pvan = new PresValOfAnnuityFragment();
            displayFragment(pvan);
        } else if (id == R.id.nav_bond_yield) {
            BondYieldFragment by = new BondYieldFragment();
            displayFragment(by);
        }
        else if (id == R.id.nav_mortgage) {
            MortgageFragment mg = new MortgageFragment();
            displayFragment(mg);
        } else if (id == R.id.nav_retirement) {
            RetirementFragment re = new RetirementFragment();
            displayFragment(re);
        } else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
