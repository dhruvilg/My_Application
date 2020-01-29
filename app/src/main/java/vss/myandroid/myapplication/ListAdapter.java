package vss.myandroid.myapplication;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import vss.myandroid.myapplication.ui.main.PageViewModel;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private ArrayList<PageViewModel> dataList;
    private Context context;

    public ListAdapter(ArrayList<PageViewModel> data, Context context) {
        this.dataList = data;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, likes, comments, dates, current_price, original_price, off_percent;
        CardView cardview;
        ImageView profileview;

        public ViewHolder(View item) {
            super(item);
            this.title = (TextView) item.findViewById(R.id.title);
            this.likes = (TextView) item.findViewById(R.id.likes);
            this.comments = (TextView) item.findViewById(R.id.comments);
            this.dates = (TextView) item.findViewById(R.id.dates);
            this.current_price = (TextView) item.findViewById(R.id.current_price);
            this.original_price = (TextView) item.findViewById(R.id.original_price);
            this.off_percent = (TextView) item.findViewById(R.id.off_percent);
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
        final PageViewModel postValues = dataList.get(position);

        viewHolder.title.setText(postValues.getTitle());
        if (!postValues.getOriginal_price().equals("0.0")) {
            String ogprice = "Rs " + postValues.getOriginal_price();
            viewHolder.original_price.setText(ogprice);
            viewHolder.original_price.setPaintFlags(viewHolder.original_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (!postValues.getCurrent_price().equals("0.0")) {
            String ogprice = "Rs " + postValues.getCurrent_price();
            viewHolder.current_price.setText(ogprice);
        }

        if (!postValues.getOff_percent().equals("")) {
            String offper = postValues.getOff_percent() + "% Off";
            viewHolder.off_percent.setText(offper);
        }

        viewHolder.likes.setText(postValues.getVote_count());
        viewHolder.comments.setText(postValues.getAll_posts_count());

        long millisecond = Long.parseLong(postValues.getCreated_at());

        Date d = new Date(millisecond);
        SimpleDateFormat targetFormat = new SimpleDateFormat("dd MMM yyyy");
        String targetdatevalue = targetFormat.format(d);
        viewHolder.dates.setText(targetdatevalue);
        Glide.with(context).load(postValues.getImage()).into(viewHolder.profileview);
        //viewHolder.profileview.setImageResource(R.drawable.img);

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}

