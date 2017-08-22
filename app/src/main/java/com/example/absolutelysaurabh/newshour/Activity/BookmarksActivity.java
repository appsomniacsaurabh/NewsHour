package com.example.absolutelysaurabh.newshour.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.absolutelysaurabh.newshour.BookMarks.NewsDbHelper;
import com.example.absolutelysaurabh.newshour.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookmarksActivity extends AppCompatActivity {

    public static String channels[] = {"Guardian","TOI", "ESPN", "TechCrunch", "MTVnews", "HackerNews", "TheHindu",
            "TechRadar", "CNN", "FinancialTimes", "Mashable", "FoxSports"};

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

    /** Database helper that will provide us access to the database */
    private NewsDbHelper bookmarksDbHelper;

    Drawable[] channelsPictures;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);


        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        al_news_desc = new ArrayList<>();
        al_news_title = new ArrayList<>();
        al_news_publishedAt = new ArrayList<>();
        al_news_url = new ArrayList<>();
        al_news_urlToImage = new ArrayList<>();

        bookmarksDbHelper = new NewsDbHelper(getApplicationContext());

        Resources resources = getApplicationContext().getResources();
        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
        channelsPictures = new Drawable[a.length()];
        for (int i = 0; i < channelsPictures.length; i++) {

            channelsPictures[i] = a.getDrawable(i);
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        getBookmarkedNewsFromDatabase();

        if((new NewsDbHelper(getApplicationContext())).numberOfRowsInBookmarks()==0){

            View l = findViewById(R.id.empty_view);
            l.setVisibility(View.VISIBLE);
        }

        Log.e("Bookmarks: NEWS_URL: ", NEWS_URL);

        adapter = new ContentAdapter(recyclerView.getContext());


        ItemTouchHelper.SimpleCallback simpleCallback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                        final int position = viewHolder.getAdapterPosition(); //get position which is swipe

                        if (direction == ItemTouchHelper.RIGHT) {    //if swipe left

                            AlertDialog.Builder builder = new AlertDialog.Builder(BookmarksActivity.this); //alert for confirm to delete
                            builder.setMessage("Are you sure to delete?");    //set message
                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() { //when click on DELETE
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    bookmarksDbHelper.deleteBookmark(position);

                                    al_news_desc.remove(position);
                                    al_news_publishedAt.remove(position);
                                    al_news_title.remove(position);
                                    al_news_url.remove(position);
                                    al_news_urlToImage.remove(position);

                                    Intent intent = new Intent(getApplicationContext(), BookmarksActivity.class);
                                    finish();
                                    startActivity(intent);

                                    dialog.dismiss();
                                    return;

                                }
                            }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {  //not removing items if cancel is done
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    dialog.dismiss();
                                }
                            }).show();  //show alert dialog
                        }
                    }
                };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView); //set swipe to recylcerview

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView picture;
        public TextView title;
        public TextView description;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {

            super(inflater.inflate(R.layout.item_bookmark, parent, false));
            picture = (ImageView) itemView.findViewById(R.id.card_image);
            title = (TextView) itemView.findViewById(R.id.card_title);
            description = (TextView) itemView.findViewById(R.id.card_text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailsActivity.class);

                    Bundle bund = new Bundle();
                    bund.putInt("tab",4);
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

                    Context context = v.getContext();
                    Intent intent = new Intent(context, WebViewActivity.class);

                    intent.putExtra("articleUrl", al_news_url.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });

            ImageButton favoriteImageButton =
                    (ImageButton) itemView.findViewById(R.id.favorite_button);

            favoriteImageButton.setColorFilter(Color.rgb(30,144,255));

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
        private final int LENGTH = bookmarksDbHelper.numberOfRowsInBookmarks();
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

            try {

                Picasso.with(getApplicationContext()).load(al_news_urlToImage.get(position))
                        .error(channelsPictures[position % channelsPictures.length]).into(holder.picture);
                holder.title.setText(al_news_title.get(position));
                holder.description.setText(al_news_desc.get(position));

                Log.e("position:  AGAIN: ", String.valueOf(position));
            }catch(IndexOutOfBoundsException e){

                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }

    public void getBookmarkedNewsFromDatabase(){

        SQLiteDatabase db = bookmarksDbHelper.getReadableDatabase();
        Cursor rs = db.rawQuery("SELECT * FROM bookmarks",null);

        if (rs.moveToFirst()) {
            while (!rs.isAfterLast()) {

//                String item_index = rs.getString(rs.getColumnIndex(NewsDbHelper.ITEM_INDEX));
                String title = rs.getString(rs.getColumnIndex(NewsDbHelper.COLUMN_NEWS_TITLE));
                String desc = rs.getString(rs.getColumnIndex(NewsDbHelper.COLUMN_NEWS_DESC));
                String urlToImage = rs.getString(rs.getColumnIndex(NewsDbHelper.COLUMN_NEWS_URLTOIMAGE));
                String url = rs.getString(rs.getColumnIndex(NewsDbHelper.COLUMN_NEWS_URL));
                String publishedAt = rs.getString(rs.getColumnIndex(NewsDbHelper.COLUMN_NEWS_PUBLISHEDAT));

                Log.e("title: ", title);

                al_news_title.add(title);
                al_news_desc.add(desc);
                al_news_url.add(url);
                al_news_urlToImage.add(urlToImage);
                al_news_publishedAt.add(publishedAt);

                rs.moveToNext();
            }
        }
        rs.close();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setTitle("Delete All");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            //This is delete all bookmarks option.
            bookmarksDbHelper.deleteAllBookmarks();

            Intent intent = new Intent(getApplicationContext(), BookmarksActivity.class);
            finish();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp(){

        finish();
        return true;
    }
}