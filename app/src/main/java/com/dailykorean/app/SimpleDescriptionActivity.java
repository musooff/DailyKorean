package com.dailykorean.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.victor.loading.book.BookLoading;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import beautifiers.FontTextView;


/**
 * Created by moshe on 02/05/2017.
 */


public class SimpleDescriptionActivity extends Fragment {

    ArrayList<DataSentence> dataSentences;
    RecyclerView rv_sentences;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    FontTextView tv_alert;

    BookLoading bookLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.activity_description, container, false);
        sharedPreferences = getActivity().getApplicationContext().getSharedPreferences("Download Data",0);
        editor = sharedPreferences.edit();
        editor.apply();

        rv_sentences = (RecyclerView)view.findViewById(R.id.rv_description);
        dataSentences = new ArrayList<>();

        DataSentenceAdapter dataAdapter = new DataSentenceAdapter(getActivity().getApplicationContext(),dataSentences);
        rv_sentences.setAdapter(dataAdapter);
        rv_sentences.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREAN);
        Calendar cal = Calendar.getInstance();
        //cal.add(Calendar.DAY_OF_MONTH,-2);

        bookLoading = (BookLoading)view.findViewById(R.id.bookloading);
        bookLoading.setVisibility(View.VISIBLE);
        bookLoading.start();
        new DownloadSentences().execute("http://dict-channelgw.naver.com/endic/kr/enko/today/"+sharedPreferences.getString("selectedDate","20170626")+"/conversation.dict");


        return view;


    }



    public class DataSentenceAdapter extends
            RecyclerView.Adapter<DataSentenceAdapter.ViewHolder> {

        private ArrayList<DataSentence> mCategory;
        private Context mContext;


        public DataSentenceAdapter(Context context, ArrayList<DataSentence> category) {
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
            View categoryView = inflater.inflate(R.layout.description_item, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(categoryView);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // Get the data model based on position
            final DataSentence category = mCategory.get(position);

            // Set item views based on your views and data model
            final FontTextView sentenceLeft = holder.sentenceLeft;
            final FontTextView sentenceRight = holder.sentenceRight;
            RelativeLayout personA = holder.personA;
            RelativeLayout personB = holder.personB;
            ImageView iv_a = holder.iv_a;
            ImageView iv_b = holder.iv_b;



            if (category.getSpeaker().equals("A")){

                iv_a.setImageResource(getImage(category.getGender()));
                sentenceLeft.setText(category.getOrgnc_sentence());
                category.setShowen("org");
                personB.setVisibility(View.GONE);
                personA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (category.getShowen().equals("org")){
                            sentenceLeft.setText(category.getTrsl_orgnc_sentence());
                            category.setShowen("tran");
                        }
                        else {
                            sentenceLeft.setText(category.getOrgnc_sentence());
                            category.setShowen("org");
                        }
                    }
                });
            }
            else {

                iv_b.setImageResource(getImage(category.getGender()));
                sentenceRight.setText(category.getOrgnc_sentence());
                category.setShowen("org");
                personA.setVisibility(View.GONE);
                personB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (category.getShowen().equals("org")){
                            sentenceRight.setText(category.getTrsl_orgnc_sentence());
                            category.setShowen("tran");
                        }
                        else {
                            sentenceRight.setText(category.getOrgnc_sentence());
                            category.setShowen("org");
                        }
                    }
                });
            }





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
            private FontTextView sentenceLeft;
            private FontTextView sentenceRight;
            private RelativeLayout personA;
            private RelativeLayout personB;
            private ImageView iv_a;
            private ImageView iv_b;

            // We also create a constructor that accepts the entire item row
            // and does the view lookups to find each subview
            public ViewHolder(View itemView) {
                // Stores the itemView in a public final member variable that can be used
                // to access the context from any ViewHolder instance.
                super(itemView);

                sentenceLeft = (FontTextView)itemView.findViewById(R.id.tv_left_sentence);
                sentenceRight = (FontTextView)itemView.findViewById(R.id.tv_right_sentence);
                personA = (RelativeLayout) itemView.findViewById(R.id.rl_left);
                personB = (RelativeLayout) itemView.findViewById(R.id.rl_right);
                iv_a = (ImageView)itemView.findViewById(R.id.tv_left_id);
                iv_b= (ImageView)itemView.findViewById(R.id.tv_right_id);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //rv_sentences.getAdapter().notifyDataSetChanged();
    }


    public  class DownloadSentences extends AsyncTask<String, Void, Void> {


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
                    JSONArray sentences = data.getJSONArray("sentences");
                    for (int i = 0; i<sentences.length();i++){
                        JSONObject single = sentences.getJSONObject(i);
                        dataSentences.add(new DataSentence(single.getString("trsl_orgnc_sentence"),removeHTML(single.getString("orgnc_sentence")),single.getString("speaker"),single.getString("gender")));

                    }

                }


            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            bookLoading.stop();
            bookLoading.setVisibility(View.INVISIBLE);
            rv_sentences.getAdapter().notifyDataSetChanged();

        }
    }


    public String removeHTML(String string){
        String result =string.replace("<b>","");
        result = result.replace("</b>","");
        return result;
    }


    public String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public Integer getImage(String gender){
        switch (gender){
            case "Male":
                return R.drawable.male_1;
            case "Female":
                return R.drawable.female_1;
            case "Boy":
                return R.drawable.boy_1;
            case "Girl":
                return R.drawable.girl_1;
            case "Male1":
                return R.drawable.male_1;
            case "Male2":
                return R.drawable.male_2;
            case "Male3":
                return R.drawable.male_3;
            case "Female1":
                return R.drawable.female_1;
            case "Female2":
                return R.drawable.female_2;
            case "Female3":
                return R.drawable.female_3;
            case "Girl1":
                return R.drawable.girl_1;
            case "Girl2":
                return R.drawable.girl_2;
            case "Boy2":
                return R.drawable.boy_2;
            case "Boy1":
            default:
                return R.drawable.boy_1;
        }
    }
}