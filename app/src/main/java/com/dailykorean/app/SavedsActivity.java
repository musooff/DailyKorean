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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class SavedsActivity extends AppCompatActivity {
    ArrayList<DataCard> dataCards;
    RecyclerView rv_favorites;
    FontTextView tv_message;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageView iv_home;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_words);

        dataCards = new ArrayList<>();

        sharedPreferences = getSharedPreferences("Download Data",0);
        editor = sharedPreferences.edit();
        editor.apply();

        tv_message = (FontTextView)findViewById(R.id.tv_alert);
        tv_message.setVisibility(View.VISIBLE);
        iv_home = (ImageView)findViewById(R.id.iv_home);
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String[] eng_words = null;
        String[] kor_words = null;
        if (sharedPreferences.getString("FavoriteWordsEng",null)!=null){

            tv_message.setVisibility(View.INVISIBLE);
            eng_words = sharedPreferences.getString("FavoriteWordsEng",null).split("ъ");
            kor_words = sharedPreferences.getString("FavoriteWordsKor",null).split("ъ");
            if (eng_words.length<=1){
                tv_message.setVisibility(View.VISIBLE);

            }

            for (int j = 0; j < eng_words.length-1; j++){
                dataCards.add(new DataCard(eng_words[j],kor_words[j],null));
            }
        }

        rv_favorites = (RecyclerView)findViewById(R.id.rv_favorite_words);

        DataAdapter dataAdapter = new DataAdapter(this,dataCards);
        rv_favorites.setAdapter(dataAdapter);
        rv_favorites.setLayoutManager(new LinearLayoutManager(this));
        rv_favorites.setItemAnimator(new DefaultItemAnimator());
    }


    public class DataAdapter extends
            RecyclerView.Adapter<DataAdapter.ViewHolder> {

        private ArrayList<DataCard> mCategory;
        private Context mContext;


        public DataAdapter(Context context, ArrayList<DataCard> category) {
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
            View categoryView = inflater.inflate(R.layout.word_item, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(categoryView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            // Get the data model based on position
            final DataCard category = mCategory.get(position);

            // Set item views based on your views and data model
            FontTextView tv_word = holder.sentence;
            tv_word.setText(category.getOrgnc_word()+"\n\n"+category.getTrsl_orgnc_word());
            ImageView save = holder.save;
            save.setImageResource(R.drawable.icon_saved);

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = screenShot(holder.share_layout);
                    Uri uri = getImageUri(getApplicationContext(), bitmap);
                    share_screen(uri,"com.facebook.katana");
                }
            });

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String tempFavDatesTitles = sharedPreferences.getString("FavoriteWordsEng",null);
                    String tempFavDatesTitlesKor = sharedPreferences.getString("FavoriteWordsKor",null);

                    tempFavDatesTitles = tempFavDatesTitles.replace(category.getOrgnc_word()+"ъ","");
                    tempFavDatesTitlesKor = tempFavDatesTitlesKor.replace(category.getTrsl_orgnc_word()+"ъ","");

                    editor.putString("FavoriteWordsEng",tempFavDatesTitles);
                    editor.putString("FavoriteWordsKor",tempFavDatesTitlesKor);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Phrase is removed from favorites",Toast.LENGTH_SHORT).show();

                    dataCards.remove(position);
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
            private ImageView save;
            private ImageView share;
            private RelativeLayout share_layout;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                sentence = (FontTextView)itemView.findViewById(R.id.tv_word);
                save = (ImageView)itemView.findViewById(R.id.save);
                share = (ImageView)itemView.findViewById(R.id.share);
                share_layout = (RelativeLayout)itemView.findViewById(R.id.rv_main);
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
