package vss.myandroid.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vss.myandroid.myapplication.ui.main.PageViewModel;
import vss.myandroid.myapplication.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<PageViewModel> postValuesArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
/*
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
*/
        /*NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);*/

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                /*if (downarrow.getTag().equals("downarrow")) {
                    ((ImageButton) views).setImageResource(R.drawable.ic_arrow_up);
                    downarrow.setTag("uparrow");
                    navigationView.getMenu().clear();
                    navigationView.inflateMenu(R.menu.add_account);
                } else */
                /*if (downarrow.getTag().equals("uparrow")) {
                    ((ImageButton) views).setImageResource(R.drawable.ic_arrow_down);
                    downarrow.setTag("downarrow");
                    if (hasStudentLoggedIn) {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.student);
                    } else if (hasStaffLoggedIn) {
                        navigationView.getMenu().clear();
                        //navigationView.inflateMenu(R.menu.staff);
                        expListView.setVisibility(View.VISIBLE);
                    } else {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.activity_main_drawer);
                    }
                }*/
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank

                super.onDrawerOpened(drawerView);
            }
        };
        drawer.setDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        getData();

    }

    private void getData() {
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        String url = "https://stagingapi.desidime.com/v3/deals/popular.json";
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);
        //pd = ProgressDialog.show(MainActivity.this, "", "Loading", true, false);

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                postValuesArrayList = new ArrayList<>();

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject DealsObject = parentObject.getJSONObject("deals");
                    Log.d("total count:", String.valueOf(DealsObject.get("total_count")));

                    JSONArray StatusArraymain = DealsObject.getJSONArray("data");

                    for (int i = 0; i < StatusArraymain.length(); i++) {
                        JSONObject StatusArray = StatusArraymain.getJSONObject(i);

                        PageViewModel model = new PageViewModel();
                        model.setTitle(StatusArray.getString("title"));
                        model.setOff_percent(StatusArray.getString("off_percent"));
                        model.setCurrent_price(StatusArray.getString("current_price"));
                        model.setOriginal_price(StatusArray.getString("original_price"));
                        model.setImage(StatusArray.getString("image"));
                        model.setAll_posts_count(StatusArray.getString("comments_count"));
                        model.setCreated_at(StatusArray.getString("created_at"));
                        model.setVote_count(StatusArray.getString("vote_count"));

                        postValuesArrayList.add(model);
                    }

                    DBHelper dbHelper = new DBHelper(MainActivity.this);
                    DBManager dbManager = new DBManager(MainActivity.this);

                    int personalcount = dbManager.getpersonalcount();
                    if (personalcount == 1) {
                        dbManager.DeletePersonaltable();
                        dbManager.insertpersonal(response);
                    } else {
                        dbManager.insertpersonal(response);
                    }

                    //pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", String.valueOf(error));
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-Desidime-Client", "68045fd226ab32029c98bf4533bfa98b3c50423094d292d70ca2702e61a9679b");

                return params;
            }
        };
        //pd.dismiss();

        mRequestQueue.add(mStringRequest);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        // Retrieve the SearchView and plug it into SearchManager
        /*final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));*/

        return true;
    }

}
