package com.dailykorean.app;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.VideoOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import beautifiers.FontTextView;
import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

/**
 * Created by moshe on 02/05/2017.
 */

public class TitlesActivity extends Fragment {

    ArrayList<Object> titles;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RecyclerView rv_titles;

    String date = null;
    String today = "20170501";
    String testToday = "2017043-";

    boolean sbFilled = false;

    Calendar cal;
    private boolean loading;

    int sunDayMode = 0;
    private boolean noInternet = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.activity_main, container, false);

        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Download Data",0);
        editor = sharedPreferences.edit();
        editor.apply();

        rv_titles = (RecyclerView) view.findViewById(R.id.rv_titles);
        rv_titles.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        titles = new ArrayList<>();



        final RecyclerViewAdapter dataAdapter = new RecyclerViewAdapter(getActivity().getApplicationContext(),titles);
        rv_titles.setAdapter(dataAdapter);
        rv_titles.setItemAnimator(new ScaleInBottomAnimator());
        final LinearLayoutManager layoutManager = (LinearLayoutManager) rv_titles.getLayoutManager();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        cal = Calendar.getInstance();
        date = dateFormat.format(cal.getTime());

        sunDayMode = cal.get(Calendar.DAY_OF_WEEK)-1;

        today = date;


        if (!isNetworkAvailable() && sharedPreferences.getBoolean("HasSaved",false)){

            noInternet = true;
            String[] days = null;
            String[] titlesKor = null;
            String[] titlesEng = null;
            //tv_message.setVisibility(View.INVISIBLE);
            days = sharedPreferences.getString("SavedDays","").split("ъ");
            titlesEng = sharedPreferences.getString("SavedTitles","").split("ъ");
            titlesKor = sharedPreferences.getString("SavedTitlesKor","").split("ъ");

            if (days.length<=1){
                //tv_message.setVisibility(View.VISIBLE);
            }
            for (int j = days.length-1; j >=0 ; j--){
                titles.add(new Data(titlesEng[j],titlesKor[j],days[j],false));
            }



        }

        else {

            noInternet = false;

            editor.remove("SavedDays");
            editor.remove("SavedTitles");
            editor.remove("SavedTitlesKor");
            editor.putBoolean("HasSaved",false);
            editor.apply();


            new Download().execute("http://dict-channelgw.naver.com/endic/kr/enko/today/"+date+"/conversation.dict");


            loading = true;

            final int[] pastVisiblesItems = new int[1];
            final int[] visibleItemCount = new int[1];
            final int[] totalItemCount = new int[1];


            rv_titles.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy>=0){
                        visibleItemCount[0] = layoutManager.getChildCount();
                        totalItemCount[0] = layoutManager.getItemCount();
                        pastVisiblesItems[0] = layoutManager.findFirstVisibleItemPosition();



                        if (loading)
                        {
                            if ( (visibleItemCount[0] + pastVisiblesItems[0]) >= totalItemCount[0])
                            {
                                loading = false;
                                //Do pagination.. i.e. fetch new data
                                cal.add(Calendar.DAY_OF_MONTH,-1);
                                date = dateFormat.format(cal.getTime());
                                new Download().execute("http://dict-channelgw.naver.com/endic/kr/enko/today/" + date + "/conversation.dict");
                                loading = true;
                            }
                        }
                    }
                }
            });
        }



        return view;


    }
    private void addTitleItem(String title, String title_translation, String public_day) {
        titles.add(new Data(title,title_translation,public_day,false));
        rv_titles.getAdapter().notifyDataSetChanged();
    }

    private void addNativeExpressAds(){
        NativeExpressAdView adView = new NativeExpressAdView(getActivity().getApplicationContext());
        adView.setAdSize(new AdSize(320,250));
        adView.setAdUnitId(getResources().getString(R.string.NativeAdUnitID));
        adView.loadAd(new AdRequest.Builder().build());
        adView.setVideoOptions(new VideoOptions.Builder()
                .setStartMuted(true)
                .build());

        titles.add(adView);
        rv_titles.getAdapter().notifyDataSetChanged();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }


    public  class Download extends AsyncTask<String, Void, Boolean> {

        public String title = null;
        public String title_translation = null;
        public  String public_day = null;

        @Override
        protected Boolean doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.connect();

                InputStream in = new BufferedInputStream(connection.getInputStream());
                JSONObject result = new JSONObject(convertStreamToString(in));

                if (result.getJSONObject("meta").getInt("status")==0){
                    return true;
                }
                else {
                    JSONObject data = result.getJSONObject("data");
                    if (data.getInt("week_day")==0) {
                        title = data.getString("title");
                        title_translation = data.getString("title_translation");
                        public_day = data.getString("public_date");

                        return false;
                    }
                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            if (aVoid==null){
                return;
            }
            else if (aVoid){
                addNativeExpressAds();
            }
            else {
                addTitleItem(title,title_translation,public_day);


                editor.putString("SavedDays",public_day+"ъ"+sharedPreferences.getString("SavedDays",""));
                editor.putString("SavedTitles",title+"ъ"+sharedPreferences.getString("SavedTitles",""));
                editor.putString("SavedTitlesKor",title_translation+"ъ"+sharedPreferences.getString("SavedTitlesKor",""));
                editor.putBoolean("HasSaved",true);
                editor.apply();
            }


        }
    }



    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        public static final int TITLES_ITEM_VIEW_TYPE = 0;
        public static final int AD_ITEM_VIEW_TYPE = 1;



        private final Context mContext;
        private final ArrayList<Object> mRecyclerViewItems;

        public RecyclerViewAdapter(Context context, ArrayList<Object> recyclerViewItems){
            this.mContext = context;
            this.mRecyclerViewItems = recyclerViewItems;
        }

        public class TitleItemViewHolder extends RecyclerView.ViewHolder{

            private FontTextView sentence;
            private FontTextView day;
            private FontTextView date_ago;
            private FontTextView text;
            private ImageView favorite;
            private ImageView share;
            private RelativeLayout share_layout;

            private ImageView convo;
            private ImageView tran;

            public TitleItemViewHolder(View itemView) {
                super(itemView);

                sentence = (FontTextView)itemView.findViewById(R.id.tv_day_sentence);
                day = (FontTextView)itemView.findViewById(R.id.tv_day_number);
                date_ago = (FontTextView)itemView.findViewById(R.id.tv_date);
                text = (FontTextView)itemView.findViewById(R.id.tv_text);
                favorite = (ImageView)itemView.findViewById(R.id.favorite);
                share = (ImageView)itemView.findViewById(R.id.share);
                share_layout = (RelativeLayout)itemView.findViewById(R.id.rv_ad_holder);

                convo = (ImageView)itemView.findViewById(R.id.convo);
                tran  = (ImageView)itemView.findViewById(R.id.tran);
            }
        }

        public class NativeExpressAdViewHolder extends RecyclerView.ViewHolder{
            public NativeExpressAdViewHolder(View itemView) {
                super(itemView);
            }
        }

        @Override
        public int getItemViewType(int position) {
            if (noInternet)
                return TITLES_ITEM_VIEW_TYPE;
            return (position%7==sunDayMode)?AD_ITEM_VIEW_TYPE:TITLES_ITEM_VIEW_TYPE;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType){
                case AD_ITEM_VIEW_TYPE:
                    View adItemLayoutView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.native_ad_container,parent,false);
                    return new NativeExpressAdViewHolder(adItemLayoutView);
                case TITLES_ITEM_VIEW_TYPE:
                default:
                    View titleItemLayoutView = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.card_item,parent,false);
                    return new TitleItemViewHolder(titleItemLayoutView);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);

            switch (viewType){
                case AD_ITEM_VIEW_TYPE:
                    NativeExpressAdViewHolder nativeExpressAdViewHolder = (NativeExpressAdViewHolder)holder;
                    NativeExpressAdView adView = (NativeExpressAdView)mRecyclerViewItems.get(position);

                    ViewGroup adCardView = (ViewGroup)nativeExpressAdViewHolder.itemView;
                    RelativeLayout rv_ad = (RelativeLayout) adCardView.getChildAt(1);

                    if (adView.getParent() !=null){
                        ((ViewGroup)adView.getParent()).removeView(adView);
                    }
                    rv_ad.addView(adView);
                    break;

                case TITLES_ITEM_VIEW_TYPE:
                default:
                    final TitleItemViewHolder titleItemViewHolder = (TitleItemViewHolder)holder;
                    final Data category = (Data)mRecyclerViewItems.get(position);

                    // set item details
                    titleItemViewHolder.sentence.setText(category.getKorTitle()+"\n\n"+category.getEngTitle());
                    titleItemViewHolder.text.setText(category.getKorTitle()+"\n"+category.getEngTitle());
                    SimpleDateFormat printingDate = new SimpleDateFormat("dd MMM yyyy",Locale.KOREAN);
                    try {
                        Date current = new SimpleDateFormat("yyyyMMdd",Locale.KOREAN).parse(category.getDate());
                        titleItemViewHolder.day.setText(printingDate.format(current));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    titleItemViewHolder.date_ago.setText(category.getDate());

                    // Sharing Image
                    titleItemViewHolder.share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bitmap bitmap = screenShot(titleItemViewHolder.share_layout);
                            Uri uri = getImageUri(getActivity().getApplicationContext(), bitmap);
                            share_screen(uri,"com.instagram.android");
                        }
                    });


                    // convo and translations
                    titleItemViewHolder.convo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent details = new Intent(getActivity().getApplicationContext(),SimpleMainActivity.class);
                            details.putExtra("day",category.getDate());
                            details.putExtra("page",0);

                            editor.putString("selectedDate",category.getDate());
                            editor.apply();
                            startActivity(details);
                        }
                    });

                    titleItemViewHolder.tran.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent details = new Intent(getActivity().getApplicationContext(),SimpleMainActivity.class);
                            details.putExtra("day",category.getDate());
                            details.putExtra("page",2);

                            editor.putString("selectedDate",category.getDate());
                            editor.apply();
                            startActivity(details);
                        }
                    });

                    final ImageView favorite = titleItemViewHolder.favorite;

                    final boolean[] savedDay = {false};
                    String favorite_dates = sharedPreferences.getString("FavoriteDates",null);
                    if (favorite_dates!=null && favorite_dates.contains(category.getDate())){
                        favorite.setImageResource(R.drawable.icon_star_main);
                        savedDay[0] = true;
                    }

                    favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!savedDay[0]){
                                favorite.setImageResource(R.drawable.icon_star_main);
                                editor.putString("FavoriteDates",category.getDate()+"ъ"+sharedPreferences.getString("FavoriteDates",null));
                                editor.putString("FavoriteDatesTitles",category.getEngTitle()+"ъ"+sharedPreferences.getString("FavoriteDatesTitles",null));
                                editor.putString("FavoriteDatesTitlesKor",category.getKorTitle()+"ъ"+sharedPreferences.getString("FavoriteDatesTitlesKor",null));

                                editor.apply();
                                savedDay[0] = true;
                                Toast.makeText(getActivity().getApplicationContext(),"Expression is added to favorites",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                favorite.setImageResource(R.drawable.icon_star);
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
                                savedDay[0] = false;
                                Toast.makeText(getActivity().getApplicationContext(),"Expression is removed from favorites",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                    break;
            }

        }

        @Override
        public int getItemCount() {
            return mRecyclerViewItems.size();
        }

    }
    public void share_screen(Uri pngUri, final String sharingapp) {

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("image/png");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Today's Expression\n" +
                "Get our FREE App from our bio @mydailykorean\n" +
                "#koreanapp #koreanfashion #korea #korean #koreanlanguage #studykorean #todaysword #todaysexpression #kpop #kdrama #love #lifeinkorea #follow #like #learnlanguage #learnkorean");
        shareIntent.putExtra(android.content.Intent.EXTRA_STREAM, pngUri);
        if (hasFacebook()){
            shareIntent.setPackage(sharingapp);
        }
        ClipboardManager clipboardManager = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("Instagram text has been copied. Just past in the text box","Today's Expression\n" +
                "Get our FREE App from our bio @mydailykorean\n" +
                "#koreanapp #koreanfashion #korea #korean #koreanlanguage #studykorean #todaysword #todaysexpression #kpop #kdrama #love #lifeinkorea #follow #like #learnlanguage #learnkorean");
        clipboardManager.setPrimaryClip(clipData);
        startActivity(shareIntent);

        // change database so that users get notifications
        DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference ids = mRootRef.child("ids");
        ids.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long value = dataSnapshot.getValue(Long.class);
                if (value == 2){
                    ids.setValue(1);
                }
                else {
                    ids.setValue(2);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                    getApplicationInfo("com.instagram.android", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }
}