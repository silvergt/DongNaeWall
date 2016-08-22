package dongnae.dongnaewall;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;




class contentAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<Poster> posterList;
    ArrayList<Poster> posters;
    ServerConnector SC;
    boolean reachedLastPoster=false;
    int count;

    ImageView posterImage;
    TextView likeNumber;
    TextView viewNumber;
    TextView name;
    TextView category;
    TextView price;
    TextView location;
    TextView date;

    public contentAdapter(Context context){
        this.context=context;
        count=0;
        SC=new ServerConnector();
        posterList=new ArrayList<>();
        getPosters();
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TempData.changeStatus(TempData.STATUS_RECOMMENDATION);
    }

    public void reloadPosterFromStart(int status){
        posterList=new ArrayList<>();
        main.scrollNumber=0;
        TempData.changeStatus(status);
        count=0;
        TempData.changeStartNum(0);
        reachedLastPoster=false;
        getPosters();

    }

    public boolean getPosters(){

        AsyncTask async=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                posters=SC.getPoster();
                if(posters==null){
                    Log.e("Log","NO SERVER RECEIVED POSTERS!");
                    return false;
                }else if(posters.size()!=0) {
                    Log.v("Log", "received");
                    posterList.addAll(posters);
                    TempData.changeStartNum(TempData.startNum + posters.size());
                    Log.v("total Loaded poster :", Integer.toString(TempData.startNum));
                    count=posterList.size();
                    publishProgress(0);
                    return true;
                }else if(posters.size()==0){
                    Log.v("Log","poster size is zero. client reached last poster!");
                    reachedLastPoster=true;
                    return true;
                }

                return true;
            }


            @Override
            protected void onProgressUpdate(Object[] values) {
                super.onProgressUpdate(values);
                Log.v("Log","progress updating");
                notifyDataSetChanged();
            }
        };
        async.execute(0);

        return false;

    }

    @Override
    public int getCount() {
        return count;
        //return posterList.size();
    }

    @Override
    public Poster getItem(int position) {
        return posterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(TempData.status==TempData.STATUS_POSTER_ABBREVIATED) {
            if (convertView == null||convertView.getId()!=R.id.content_main_listview_main) {
                convertView = inflater.inflate(R.layout.content_main_listview, null);
            }
            Poster poster = posterList.get(position);

            posterImage = (ImageView) convertView.findViewById(R.id.listview_background_poster);
            likeNumber = (TextView) convertView.findViewById(R.id.listview_bottom_like_number);
            viewNumber = (TextView) convertView.findViewById(R.id.listview_bottom_view_number);
            name = (TextView) convertView.findViewById(R.id.listview_second_layout_name);
            category = (TextView) convertView.findViewById(R.id.listview_second_layout_category);
            price = (TextView) convertView.findViewById(R.id.listview_second_layout_price);
            location = (TextView) convertView.findViewById(R.id.listview_second_layout_location);
            date = (TextView) convertView.findViewById(R.id.listview_second_layout_date);

            poster.main_picture_loaded.into(posterImage);
            likeNumber.setText(Integer.toString(poster.like));
            viewNumber.setText(Integer.toString(poster.view));
            date.setText(Integer.toString(poster.startDate[0]) + Integer.toString(poster.startDate[1]) + Integer.toString(poster.startDate[2]));
            name.setText(poster.title);
            category.setText(Integer.toString(poster.category[0]) + Integer.toString(poster.category[1]));
            price.setText(Integer.toString(poster.price));
            location.setText(Integer.toString(poster.place));

            if (position == posterList.size() - 1 && !reachedLastPoster) {
                getPosters();
            }
            main.scrollNumber = position;
        }else if(TempData.status==TempData.STATUS_RECOMMENDATION){
            if (convertView == null ||convertView.getId()!=R.id.recommendation_main) {
                convertView = inflater.inflate(R.layout.content_main_recommendation, null);
            }
            Poster poster = posterList.get(position);

            TextView title=(TextView)convertView.findViewById(R.id.recommendation_title);
            ImageView image=(ImageView)convertView.findViewById(R.id.recommendation_image);
            TextView description=(TextView)convertView.findViewById(R.id.recommendation_description);

            if(position==0){
                title.setText(context.getResources().getString(R.string.recommendation_position_0));
            }else if(position==1){
                title.setText(context.getResources().getString(R.string.recommendation_position_1));
            }else if(position==2){
                title.setText(context.getResources().getString(R.string.recommendation_position_2));
            }else{
                title.setText("");
            }
            title.setPadding(main.displayWidth/10,main.displayWidth/25,main.displayWidth/25,0);
            poster.main_picture_loaded.into(image);
            description.setText(poster.title);

            main.scrollNumber = position+1;
        }
        Log.v("Log from getView",Integer.toString(main.scrollNumber));
        return convertView;
    }

}


public class main extends AppCompatActivity {
    static int scrollNumber=0;
    static boolean scrollIsDownward=true;
    static boolean scrolledByTouch=false;
    static int displayHeight;
    static int displayWidth;

    ListView list;
    TextView listFooter;
    LinearLayout listHeader;
    RelativeLayout profileLayout;
    RelativeLayout mainProfileLayout;

    static boolean searchBarIsVisible=false;
    LinearLayout searchBar;
    RelativeLayout mainLayout;

    LayoutInflater MainInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP) {
            Window window =getWindow();
            //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.basicStatusBarColor));
        }

        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        displayWidth=metrics.widthPixels;
        displayHeight=metrics.heightPixels;
        Log.v("Display size : ", Integer.toString(displayWidth) + "," + Integer.toString(displayHeight));

        MainInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        Poster.context=this;
        createMainView();


    }



    public void createMainView(){
        setContentView(R.layout.content_main);


        mainLayout=(RelativeLayout)findViewById(R.id.main);
        mainProfileLayout=(RelativeLayout)findViewById(R.id.main_background_profilelayout);
        TextView filter=(TextView)findViewById(R.id.main_bottom_filter);
        TextView down=(TextView)findViewById(R.id.main_bottom_down);
        TextView up=(TextView)findViewById(R.id.main_bottom_up);
        ImageView logo=(ImageView)findViewById(R.id.main_logo);
        final TextView search=(TextView)findViewById(R.id.main_top_search);
        list=(ListView)findViewById(R.id.main_listview);
        TextView alarm=(TextView)findViewById(R.id.main_top_alarm);



        final contentAdapter adapter=new contentAdapter(this);
        list.setAdapter(adapter);
        setHeaderFooterViewToList();

        //******SEARCH METHOD
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!searchBarIsVisible) {
                    RelativeLayout.LayoutParams searchBarParams = new RelativeLayout.LayoutParams
                            (ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.topbar_size));
                    searchBarParams.addRule(RelativeLayout.BELOW, R.id.main_topbar);
                    searchBar = (LinearLayout) MainInflater.inflate(R.layout.searchbar, null);
                    mainLayout.addView(searchBar, searchBarParams);
                    searchBarIsVisible = true;
                }else{
                    mainLayout.removeView(searchBar);
                    searchBarIsVisible=false;
                }
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.reloadPosterFromStart(TempData.STATUS_POSTER_ABBREVIATED);
                setHeaderFooterViewToList();
                adapter.notifyDataSetChanged();
            }
        });



        //******SEARCH METHOD


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.reloadPosterFromStart(TempData.STATUS_RECOMMENDATION);
                setHeaderFooterViewToList();
                adapter.notifyDataSetChanged();
                if(searchBarIsVisible) {
                    mainLayout.removeView(searchBar);
                    searchBarIsVisible = false;
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(main.this, filter.class);
                startActivity(intent);
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrolledByTouch=false;
                if(scrollIsDownward){
                    scrollNumber-=2;
                    if(scrollNumber<0){
                        scrollNumber=0;
                    }
                }
                Log.v("Log scroll to",Integer.toString(scrollNumber));
                list.smoothScrollToPosition(scrollNumber--);
                scrollIsDownward=false;

                if(scrollNumber<0){
                    scrollNumber=0;
                }


            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrolledByTouch = false;
                if (!scrollIsDownward && scrollNumber>1) {
                    scrollNumber += 2;
                }else if(!scrollIsDownward && scrollNumber<=1){
                    scrollNumber+=1;
                }

                Log.v("Log scroll to",Integer.toString(scrollNumber));
                list.smoothScrollToPosition(scrollNumber++);
                if(scrollNumber>adapter.getCount()+1){
                    scrollNumber=adapter.getCount()+1;
                }
                scrollIsDownward = true;

            }

        });
    }

    @Override
    public void onBackPressed() {
        if(searchBarIsVisible){
            mainLayout.removeView(searchBar);
            searchBarIsVisible=false;
            return;
        }
        super.onBackPressed();


    }

    public void setHeaderFooterViewToList(){
        try{
            mainProfileLayout.removeAllViews();
        }catch (Exception e){
            Log.v("Log","profileLayout not detected");
        }
        try{
            list.removeHeaderView(listHeader);
        }catch (Exception e){
            Log.v("Log","no HeaderView detected");
        }
        try{
            list.removeFooterView(listFooter);
        }catch (Exception e){
            Log.v("Log","no FooterView detected");
        }

        if(TempData.status==TempData.STATUS_RECOMMENDATION){

            listHeader=(LinearLayout)MainInflater.inflate(R.layout.recommendation_headerview,null);
            ListView.LayoutParams LTHHparams=new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,main.displayHeight-(int)getResources().getDimension(R.dimen.topbar_size));
            TextView headerText1=(TextView)listHeader.findViewById(R.id.recommendation_headerview_text1);
            TextView headerText2=(TextView)listHeader.findViewById(R.id.recommendation_headerview_text2);
            headerText1.setPadding(main.displayWidth/10,main.displayHeight/30,0,0);
            headerText2.setPadding(main.displayWidth/10,5,0,0);
            listHeader.setLayoutParams(LTHHparams);
            list.addHeaderView(listHeader);

            profileLayout=(RelativeLayout)MainInflater.inflate(R.layout.profile_layout,null);
            mainProfileLayout.addView(profileLayout);
            main.scrollNumber=1;

        }else if(TempData.status==TempData.STATUS_POSTER_ABBREVIATED){
            //********FOOTER LOADING IMAGE SHOULD BE ADAPTED
            listFooter=new TextView(this);
            ListView.LayoutParams LTFFparams=new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
            listFooter.setLayoutParams(LTFFparams);
            listFooter.setText("LOADING...");
            listFooter.setTextColor(Color.BLUE);
            listFooter.setBackgroundColor(Color.WHITE);
            list.addFooterView(listFooter);
            //********
        }


    }
    

}
