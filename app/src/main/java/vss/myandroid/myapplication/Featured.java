package vss.myandroid.myapplication;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import vss.myandroid.myapplication.ui.main.PageViewModel;

public class Featured extends Fragment {

    DBManager dbManager;
    ArrayList<PageViewModel> postValuesArrayList;
    ListAdapter mListadapter;
    private RecyclerView mRecyclerView;
    SwipeRefreshLayout swiperefresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_featured, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swiperefresh = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        dbManager = new DBManager(getContext());
        Cursor cursor = dbManager.getPersonaldetails();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "Error Occured", Toast.LENGTH_SHORT).show();
        }
        while (cursor.moveToNext()) {
            String datas = cursor.getString(0);
            if (!datas.equals(""))
                getData(datas);
            //Log.d("datas", datas);
        }

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataOnline();
            }
        });

        return view;
    }

    private void getDataOnline() {
        RequestQueue mRequestQueue;
        StringRequest mStringRequest;
        String url = "https://stagingapi.desidime.com/v3/deals/popular.json";
        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(getContext());
        //pd = ProgressDialog.show(MainActivity.this, "", "Loading", true, false);
        swiperefresh.setRefreshing(true);

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

                    DBHelper dbHelper = new DBHelper(getContext());
                    DBManager dbManager = new DBManager(getContext());

                    int personalcount = dbManager.getpersonalcount();
                    if (personalcount == 1) {
                        dbManager.DeletePersonaltable();
                        dbManager.insertpersonal(response);
                    } else {
                        dbManager.insertpersonal(response);
                    }

                    mListadapter = new ListAdapter(postValuesArrayList,getContext());
                    mRecyclerView.setAdapter(mListadapter);

                    swiperefresh.setRefreshing(false);
                    //pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", String.valueOf(error));
                swiperefresh.setRefreshing(false);
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

    private void getData(String response) {
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

            mListadapter = new ListAdapter(postValuesArrayList,getContext());
            mRecyclerView.setAdapter(mListadapter);

            //pd.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
        private ArrayList<PageViewModel> dataList;

        public ListAdapter(ArrayList<PageViewModel> data) {
            this.dataList = data;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, likes, comments, dates;
            CardView cardview;
            ImageView profileview;

            public ViewHolder(View item) {
                super(item);
                this.title = (TextView) item.findViewById(R.id.title);
                this.likes = (TextView) item.findViewById(R.id.likes);
                this.comments = (TextView) item.findViewById(R.id.comments);
                this.dates = (TextView) item.findViewById(R.id.dates);
                this.profileview = item.findViewById(R.id.imageview);

            }
        }

        @Override
        public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_row, parent, false);
            ListAdapter.ViewHolder viewHolder = new ListAdapter.ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ListAdapter.ViewHolder viewHolder, final int position) {
            final PageViewModel postValues = postValuesArrayList.get(position);

            viewHolder.title.setText(postValues.getTitle());
            viewHolder.likes.setText(postValues.getVote_count());
            viewHolder.comments.setText(postValues.getAll_posts_count());
            viewHolder.dates.setText(postValues.getCreated_at());
            Glide.with(getContext()).load(postValues.getImage()).into(viewHolder.profileview);
            //viewHolder.profileview.setImageResource(R.drawable.img);

        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }*/
}
