package vss.myandroid.myapplication.ui.main;

import androidx.lifecycle.ViewModel;

public class PageViewModel extends ViewModel {

    /*private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    public LiveData<String> getText() {
        return mText;
    }*/

    String title;
    String off_percent;
    String current_price;
    String original_price;
    String image;
    String all_posts_count;
    String created_at;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOff_percent() {
        return off_percent;
    }

    public void setOff_percent(String off_percent) {
        this.off_percent = off_percent;
    }

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getOriginal_price() {
        return original_price;
    }

    public void setOriginal_price(String original_price) {
        this.original_price = original_price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAll_posts_count() {
        return all_posts_count;
    }

    public void setAll_posts_count(String all_posts_count) {
        this.all_posts_count = all_posts_count;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    String vote_count;

}