package com.example.absolutelysaurabh.newshour.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.absolutelysaurabh.newshour.BookMarks.BookmarksDbHelper;
import com.example.absolutelysaurabh.newshour.BookMarks.NewsDbHelper;
import com.example.absolutelysaurabh.newshour.Config.Config;
import com.example.absolutelysaurabh.newshour.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Provides UI for the Detail page with Collapsing Toolbar.
 */
public class ChannelActivity extends AppCompatActivity {

    public static String channels[] = {"Guardian","TOI", "ESPN", "TechCrunch", "MTVnews", "HackerNews", "TheHindu",
     "TechRadar", "CNN", "FinancialTimes", "Mashable", "FoxSports"};

    private BookmarksDbHelper bookmarksDbHelper;
    private NewsDbHelper simpleNewsDbHelper;

    public static ArrayList<String> al_news_title;
    public static ArrayList<String> al_news_desc;
    public static ArrayList<String> al_news_url;
    public static ArrayList<String> al_news_urlToImage;
    public static ArrayList<String> al_news_publishedAt;

    public static final String EXTRA_POSITION = "position";
    public static String NEWS_URL = "";
    ContentAdapter adapter;
    RecyclerView recyclerView;
    View listItemView;

    Drawable[] channelsPictures;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set Collapsing Toolbar layout to the screen

        al_news_desc = new ArrayList<>();
        al_news_title = new ArrayList<>();
        al_news_publishedAt = new ArrayList<>();
        al_news_url = new ArrayList<>();
        al_news_urlToImage = new ArrayList<>();

        Resources resources = getApplicationContext().getResources();
        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        channelsPictures = new Drawable[a.length()];
        for (int i = 0; i < channelsPictures.length; i++) {

            channelsPictures[i] = a.getDrawable(i);
        }

        bookmarksDbHelper = new BookmarksDbHelper(getApplicationContext());
        simpleNewsDbHelper = new NewsDbHelper(getApplicationContext());

        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        recyclerView.setNestedScrollingEnabled(false);

        int position = getIntent().getIntExtra("position", 0);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView newsImage = (ImageView) findViewById(R.id.image);

        if(position == 0){

            NEWS_URL = Config.THEGUARDIAN_URL + Config.API_KEY;
            collapsingToolbar.setTitle("The Guardian");
            Picasso.with(this).load(R.drawable.guardian).error(R.drawable.guardian).into(newsImage);



        }else
            if(position == 1){

                NEWS_URL = Config.TOI_URL + Config.API_KEY;
                collapsingToolbar.setTitle("The Times of India");
                Picasso.with(this).load(R.drawable.toi).error(R.drawable.guardian).into(newsImage);

            }else
            if(position == 2){

                NEWS_URL = Config.ESPN_URL + Config.API_KEY;
                collapsingToolbar.setTitle("ESPN");
                Picasso.with(this).load(R.drawable.espn).error(R.drawable.guardian).into(newsImage);


            }else
            if(position == 3){

                NEWS_URL = Config.TECHCRUNCH_URL + Config.API_KEY;
                collapsingToolbar.setTitle("TechCrunch");
                Picasso.with(this).load(R.drawable.tcrunch).error(R.drawable.guardian).into(newsImage);


            }else
            if(position == 4){

                NEWS_URL = Config.MTVNEWS_URL + Config.API_KEY;
                collapsingToolbar.setTitle("MTV News");
                Picasso.with(this).load(R.drawable.mtv_news).error(R.drawable.guardian).into(newsImage);


            }else
            if(position == 5){

                NEWS_URL = Config.HACKERNEWS_URL + Config.API_KEY;
                collapsingToolbar.setTitle("HackerNews");
                Picasso.with(this).load(R.drawable.hackernews).error(R.drawable.guardian).into(newsImage);

            }else
            if(position == 6){

                NEWS_URL = Config.HINDU_URL + Config.API_KEY;
                collapsingToolbar.setTitle("The Hindu");
                Picasso.with(this).load(R.drawable.hindu).error(R.drawable.guardian).into(newsImage);


            }else
            if(position == 7){

                NEWS_URL = Config.TECHRADAR_URL + Config.API_KEY;
                collapsingToolbar.setTitle("TechRadar");
                Picasso.with(this).load(R.drawable.techradar).error(R.drawable.guardian).into(newsImage);

            }else
            if(position == 8){

                NEWS_URL = Config.CNN_URL + Config.API_KEY;
                collapsingToolbar.setTitle("Hacker-News");
                Picasso.with(this).load(R.drawable.cnn).error(R.drawable.guardian).into(newsImage);

            }else
            if(position == 9){

                NEWS_URL = Config.FINANCIALTIMES_URL + Config.API_KEY;
                collapsingToolbar.setTitle("Financial Times");
                Picasso.with(this).load(R.drawable.financialtimes).error(R.drawable.guardian).into(newsImage);

            }else
            if(position == 10){

                NEWS_URL = Config.MASHABLE_URL + Config.API_KEY;
                collapsingToolbar.setTitle("Mashable");
                Picasso.with(this).load(R.drawable.mashable).error(R.drawable.guardian).into(newsImage);

            }else
            if(position == 11){

                NEWS_URL = Config.FOXSPORTS_URL + Config.API_KEY;
                collapsingToolbar.setTitle("Fox Sports");
                Picasso.with(this).load(R.drawable.foxsports).error(R.drawable.guardian).into(newsImage);

            }

        Log.e("NEWS_URL: ", NEWS_URL);
        getChannelWiseNews(NEWS_URL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView title;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.item_tech, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            title = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailsActivity.class);

                    Bundle bund = new Bundle();
                    bund.putInt("tab",2);
                    bund.putInt(DetailsActivity.EXTRA_POSITION,getAdapterPosition());

                    intent.putExtra("bundle", bund);
                    context.startActivity(intent);
                }
            });

            // Adding Snackbar to Action Button inside card
            Button button = (Button)itemView.findViewById(R.id.read_full_story_button);
            button.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);

                    intent.putExtra("articleUrl", al_news_url.get(getAdapterPosition()));

                    startActivity(intent);
                }
            });

            final ImageButton favoriteImageButton = (ImageButton) itemView.findViewById(R.id.favorite_button);
            favoriteImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    SQLiteDatabase db = bookmarksDbHelper.getReadableDatabase();
                    Cursor rs = db.rawQuery("SELECT * FROM bookmarks",null);
                    int flag=0;
                    boolean isInserted = false;
                    if(rs.moveToFirst()){

                        while(!rs.isAfterLast()){

                            String title_in_db = rs.getString(rs.getColumnIndex(BookmarksDbHelper.COLUMN_NEWS_TITLE));
                            if(title_in_db.equals(al_news_title.get(getAdapterPosition()))){
                                flag=1;
                            }
                            rs.moveToNext();
                        }
                    }
                    if(flag==0){

                        isInserted =  bookmarksDbHelper.insertNews(al_news_title.get(getAdapterPosition()), al_news_desc.get(getAdapterPosition()),
                                al_news_url.get(getAdapterPosition()), al_news_urlToImage.get(getAdapterPosition()),
                                al_news_publishedAt.get(getAdapterPosition()));
                    }

                    if(isInserted){
                        boolean isBookmarked = simpleNewsDbHelper.updateNews(getAdapterPosition(),al_news_title.get(getAdapterPosition()),
                                al_news_desc.get(getAdapterPosition()), al_news_url.get(getAdapterPosition()),
                                al_news_urlToImage.get(getAdapterPosition()), al_news_publishedAt.get(getAdapterPosition()), 1);

                        if(isBookmarked){

                            favoriteImageButton.setClickable(false);
                            favoriteImageButton.setColorFilter(Color.rgb(30,144,255));
                            Snackbar.make(v, "Added to Favorites ", Snackbar.LENGTH_LONG).show();
                        }

                    }else {

                        if(flag==1){

                            favoriteImageButton.setClickable(false);
                            favoriteImageButton.setColorFilter(Color.rgb(30,144,255));
                            Snackbar.make(v, "Already added! ", Snackbar.LENGTH_LONG).show();
                        }else {

                            Snackbar.make(v, "Adding bookmark failed!! ", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
            });

            ImageButton shareImageButton = (ImageButton) itemView.findViewById(R.id.share_button);
            shareImageButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, al_news_url.get(getAdapterPosition()));
                    sendIntent.setType("text/plain");
                    startActivity(Intent.createChooser(sendIntent,("Share news via:")));
                    Snackbar.make(v, "Sharing....", Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Adapter to display recycler view.
     */
    public class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of Card in RecyclerView.
        private final int LENGTH = al_news_desc.size();

        private Context context;

        public ContentAdapter(Context context) {

            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            Log.e("OnBindViiewHolder: ", al_news_title.get(position));
            Picasso.with(getApplicationContext()).load(al_news_urlToImage.get(position))
                    .error(channelsPictures[position % channelsPictures.length]).into(holder.picture);

            holder.title.setText(al_news_title.get(position));
            holder.description.setText(al_news_desc.get(position));
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }


    public void getChannelWiseNews(String NEWS_REQUEST_URL){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(this, NEWS_REQUEST_URL , new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject responseObject) {
                try {

                    JSONArray responseArray = responseObject.getJSONArray("articles");
                    for(int i=0;i<responseArray.length();i++){

                        JSONObject currentNews = responseArray.getJSONObject(i);

                        String title = currentNews.getString("title");
                        String description = currentNews.getString("description");
                        String url = currentNews.getString("url");
                        String urlToImage = currentNews.getString("urlToImage");
                        String publishedAt = currentNews.getString("publishedAt");

                        al_news_desc.add(description);
                        al_news_urlToImage.add(urlToImage);
                        al_news_publishedAt.add(publishedAt);
                        al_news_title.add(title);
                        al_news_url.add(url);

                        Log.d("QUESTIONS: "+ String.valueOf(i), title);
                    }

                    adapter = new ContentAdapter(recyclerView.getContext());
                    recyclerView.setAdapter(adapter);

                } catch (JSONException e){
                    Log.e("QueryUtils", "Problem parsing the News JSON results", e);
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }

}