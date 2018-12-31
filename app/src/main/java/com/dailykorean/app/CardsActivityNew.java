package com.dailykorean.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import beautifiers.FontTextView;

/**
 * Created by moshe on 02/05/2017.
 */


public class CardsActivityNew extends Fragment {

    ArrayList<DataCard> dataCards;
    RecyclerView rv_cards;

    StringBuilder kor_words = new StringBuilder();
    StringBuilder eng_words = new StringBuilder();

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    FontTextView tv_alert;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.activity_cards, container, false);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Download Data",0);
        editor = sharedPreferences.edit();
        editor.apply();

        rv_cards = (RecyclerView)view.findViewById(R.id.rv_cards);
        dataCards = new ArrayList<>();

        DataCardAdapter dataAdapter = new DataCardAdapter(getActivity().getApplicationContext(),dataCards);
        rv_cards.setAdapter(dataAdapter);
        rv_cards.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DAY_OF_MONTH,-3);
        if (sharedPreferences.getBoolean("TodayWordsAvailable",false) && dateFormat.format(cal.getTime())
                .equals(sharedPreferences.getString("TodayDateWord",null))){
            String[] todayKorWords = sharedPreferences.getString("TodayKorWords",null).split("ъ");
            String[] todayEngWords = sharedPreferences.getString("TodayEngWords",null).split("ъ");
            //Log.e("KorWords", String.valueOf(Arrays.asList(todayKorWords)));
            //Log.e("EngWords", String.valueOf(Arrays.asList(todayKorWords)));
            for (int j = 0; j < todayKorWords.length;j++){
                dataCards.add(new DataCard(todayKorWords[j], todayEngWords[j], null));
            }
        }

        else if (cal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            tv_alert = (FontTextView)view.findViewById(R.id.tv_alert);
            tv_alert.setVisibility(View.VISIBLE);
        }

        else {
            new DownloadWords().execute("http://dict-channelgw.naver.com/endic/kr/enko/today/" + dateFormat.format(cal.getTime()) + "/conversation.dict");
            editor.putString("TodayDateWord",dateFormat.format(cal.getTime()));
            editor.apply();
        }

        return view;


    }


    public class DataCardAdapter extends
            RecyclerView.Adapter<DataCardAdapter.ViewHolder> {

        private ArrayList<DataCard> mCategory;
        private Context mContext;


        public DataCardAdapter(Context context, ArrayList<DataCard> category) {
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
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // Get the data model based on position
            final DataCard category = mCategory.get(position);

            // Set item views based on your views and data model
            final FontTextView word = holder.word;
            final ImageView favorite = holder.save;

            word.setText(category.getOrgnc_word());
            category.setShowen("org");
            word.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("getShowen",category.getShowen());
                    //Log.d("org",category.getOrgnc_word());
                    //Log.d("tran",category.getTrsl_orgnc_word());
                    if (category.getShowen().equals("org")){
                        word.setText(category.getTrsl_orgnc_word());
                        category.setShowen("tran");
                    }
                    else {
                        word.setText(category.getOrgnc_word());
                        category.setShowen("org");
                    }
                }
            });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bitmap = screenShot(holder.share_layout);
                    Uri uri = getImageUri(getActivity().getApplicationContext(), bitmap);
                    share_screen(uri,"com.facebook.katana");
                }
            });


            final boolean[] savedWord = {false};
            String favorite_dates = sharedPreferences.getString("FavoriteWordsEng",null);
            if (favorite_dates!=null && favorite_dates.contains(category.getOrgnc_word())){
                favorite.setImageResource(R.drawable.icon_saved);
                savedWord[0] = true;
            }

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!savedWord[0]){
                        favorite.setImageResource(R.drawable.icon_saved);  // ы барои датаи рузхо, я барои елементи рузхо, ъ барои чумлахо
                        editor.putString("FavoriteWordsEng",category.getOrgnc_word()+"ъ"+sharedPreferences.getString("FavoriteWordsEng",null));
                        editor.putString("FavoriteWordsKor",category.getTrsl_orgnc_word()+"ъ"+sharedPreferences.getString("FavoriteWordsKor",null));
                        editor.apply();
                        savedWord[0] = true;
                        Toast.makeText(getActivity().getApplicationContext(),"Word is added to favorites",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        favorite.setImageResource(R.drawable.icon_bookmark);
                        String tempFavDates = sharedPreferences.getString("FavoriteWordsEng",null);
                        String tempFavDatesTitles = sharedPreferences.getString("FavoriteWordsKor",null);

                        tempFavDates = tempFavDates.replace(category.getOrgnc_word()+"ъ","");
                        tempFavDatesTitles = tempFavDatesTitles.replace(category.getTrsl_orgnc_word()+"ъ","");

                        editor.putString("FavoriteWordsEng",tempFavDates);
                        editor.putString("FavoriteWordsKor",tempFavDatesTitles);
                        editor.apply();
                        savedWord[0] = false;
                        Toast.makeText(getActivity().getApplicationContext(),"Word is removed from favorites",Toast.LENGTH_SHORT).show();
                    }

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
            private FontTextView word;
            private ImageView save;
            private ImageView share;
            private RelativeLayout share_layout;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                word = (FontTextView)itemView.findViewById(R.id.tv_word);
                save = (ImageView)itemView.findViewById(R.id.save);
                share = (ImageView)itemView.findViewById(R.id.share);
                share_layout = (RelativeLayout)itemView.findViewById(R.id.rv_main);
            }

        }
    }


    public  class DownloadWords extends AsyncTask<String, Void, Void> {

        public String title = null;
        public String title_translation = null;

        public String[] korWords;
        @Override
        protected Void doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream in = new BufferedInputStream(connection.getInputStream());
                JSONObject result = new JSONObject(convertStreamToString(in));

                if (result.getJSONObject("meta").getInt("status")==0){
                    //titles.add(new Data("Advertisement","Google or Unity","SUNDAY"));
                }
                else {
                    JSONObject data = result.getJSONObject("data");
                    //Log.e("title",data.getString("title"));
                    //Log.e("title_translation",data.getString("title_translation"));
                    if (data.getInt("week_day")==0) {
                        //title = data.getString("title");
                        //title_translation = data.getString("title_translation");
                        //korWords = title_translation.split(" ");
                        //wordCount = korWords.length;

                        JSONArray entrys = data.getJSONArray("entrys");

                        for (int i = 0;i<entrys.length();i++) {

                            JSONObject single = entrys.getJSONObject(i);
                            dataCards.add(new DataCard(removeOtherMeanings(single.getString("mean")), single.getString("orgnc_entry_name"), null));
                            kor_words.append(removeOtherMeanings(single.getString("mean"))).append("ъ");
                            eng_words.append(single.getString("orgnc_entry_name") + "ъ");
                        }
                        kor_words.deleteCharAt(kor_words.length()-1);
                        eng_words.deleteCharAt(eng_words.length()-1);

                        editor.putString("TodayKorWords",kor_words.toString());
                        editor.putString("TodayEngWords",eng_words.toString());
                        editor.putBoolean("TodayWordsAvailable",true);
                        editor.apply();


                    }
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            rv_cards.getAdapter().notifyDataSetChanged();

        }
    }



    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
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
            ApplicationInfo info = getActivity().getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }

    public String removeOtherMeanings(String translation){
        if (translation.contains(",")){
            translation = translation.substring(0,translation.indexOf(","));
        }
        if (translation.contains("(")){
            translation = translation.substring(0,translation.indexOf("("));
        }
        return translation;
    }

}