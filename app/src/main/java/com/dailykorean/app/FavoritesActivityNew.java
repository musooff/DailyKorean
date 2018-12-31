package com.dailykorean.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import beautifiers.FontTextView;

/**
 * Created by moshe on 03/05/2017.
 */

public class FavoritesActivityNew extends AppCompatActivity {
    ArrayList<Data> dataFavorites;
    RecyclerView rv_favorites;
    FontTextView tv_message;

    SharedPreferences sharedPreferences;
    ImageView iv_home;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        dataFavorites = new ArrayList<>();

        sharedPreferences = getSharedPreferences("Download Data",0);
        editor = sharedPreferences.edit();

        tv_message = (FontTextView)findViewById(R.id.tv_alert);
        tv_message.setVisibility(View.VISIBLE);

        iv_home = (ImageView)findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        String[] days = null;
        String[] korTitles = null;
        String[] titles = null;
        if (sharedPreferences.getString("FavoriteDates",null)!=null){

            tv_message.setVisibility(View.INVISIBLE);
            days = sharedPreferences.getString("FavoriteDates",null).split("ъ");
            titles = sharedPreferences.getString("FavoriteDatesTitles",null).split("ъ");
            korTitles = sharedPreferences.getString("FavoriteDatesTitlesKor",null).split("ъ");

            if (days.length<=1){
                tv_message.setVisibility(View.VISIBLE);
            }
            for (int j = 0; j < days.length-1; j++){
                dataFavorites.add(new Data(titles[j],korTitles[j],days[j],false));
            }
        }

        rv_favorites = (RecyclerView)findViewById(R.id.rv_favorites);

        DataAdapter dataAdapter = new DataAdapter(this,dataFavorites);
        rv_favorites.setAdapter(dataAdapter);
        rv_favorites.setLayoutManager(new LinearLayoutManager(this));
        rv_favorites.setItemAnimator(new DefaultItemAnimator());
    }


    public class DataAdapter extends
            RecyclerView.Adapter<DataAdapter.ViewHolder> {

        private ArrayList<Data> mCategory;
        private Context mContext;


        public DataAdapter(Context context, ArrayList<Data> category) {
            mCategory = category;
            mContext = context;
        }

        // Easy access to the context object in the recyclerview
        private Context getContext() {
            return mContext;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View categoryView = inflater.inflate(R.layout.card_item, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(categoryView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // Get the data model based on position
            final Data category = mCategory.get(position);
            FontTextView day = holder.day;

            final ImageView favorite = holder.favorite;

            // Set item views based on your views and data model
            FontTextView tv_day_sentence = holder.sentence;
            tv_day_sentence.setText(category.getKorTitle()+"\n"+category.getEngTitle());
            day.setText("="+category.getDate()+"=");
            holder.days_ago.setText(category.getDate());
            favorite.setImageResource(R.drawable.icon_star_main);

            // Sharing Image
            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = screenShot(holder.share_layout);
                    Uri uri = getImageUri(getApplicationContext(), bitmap);
                    share_screen(uri,"com.facebook.katana");
                }
            });


            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tempFavDates = sharedPreferences.getString("FavoriteDates",null);
                    String tempFavDatesTitles = sharedPreferences.getString("FavoriteDatesTitles",null);
                    String tempFavDatesTitlesKor = sharedPreferences.getString("FavoriteDatesTitlesKor",null);

                    tempFavDates = tempFavDates.replace(category.getDate()+"ъ","");
                    tempFavDatesTitles = tempFavDatesTitles.replace(category.getEngTitle()+"ъ","");
                    tempFavDatesTitlesKor = tempFavDatesTitlesKor.replace(category.getKorTitle()+"ъ","");

                    editor.putString("FavoriteDates",tempFavDates);
                    editor.putString("FavoriteDatesTitles",tempFavDatesTitles);
                    editor.putString("FavoriteDatesTitlesKor",tempFavDatesTitlesKor);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Expression is removed from favorites",Toast.LENGTH_SHORT).show();
                    dataFavorites.remove(position);
                    rv_favorites.getAdapter().notifyItemRemoved(position);
                    rv_favorites.getAdapter().notifyItemRangeChanged(position,getItemCount());
                }
            });




        }

        @Override
        public int getItemCount() {
            return mCategory.size();
        }

        // Provide a direct reference to each of the views within a data item
        // Used to cache the views within the item layout for fast access
        public class ViewHolder extends RecyclerView.ViewHolder{
            // Your holder should contain a member variable
            // for any view that will be set as you render a row
            private FontTextView sentence;
            private FontTextView day;
            private ImageView favorite;
            private FontTextView days_ago;
            private ImageView share;
            private RelativeLayout share_layout;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                sentence = (FontTextView)itemView.findViewById(R.id.tv_day_sentence);
                day = (FontTextView)itemView.findViewById(R.id.tv_day_number);
                days_ago = (FontTextView)itemView.findViewById(R.id.tv_date);
                favorite = (ImageView)itemView.findViewById(R.id.favorite);
                share = (ImageView)itemView.findViewById(R.id.share);
                share_layout = (RelativeLayout)itemView.findViewById(R.id.rv_ad_holder);
            }

        }
    }


    public void share_screen(Uri pngUri, final String sharingapp) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "your sharing text");
        shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, pngUri);
        if (hasFacebook()){
            shareIntent.setPackage(sharingapp);
        }
        startActivity(shareIntent);

    }

    private Uri getImageUri(Context applicationContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(applicationContext.getContentResolver(),
                bitmap, "", "");
        return Uri.parse(path);
    }

    public Bitmap screenShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(),
                view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    public boolean hasFacebook(){
        try{
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }
}
