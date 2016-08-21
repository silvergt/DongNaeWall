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
    }

    public void reloadPosterFromStart(){
        posterList=new ArrayList<>();
        count=0;
        TempData.changeStartNum(0);
        reachedLastPoster=false;
        getPosters();
    }

    public boolean getPosters(){

        AsyncTask async=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                posters=SC.getPosterFromServer(TempData.STATUS_ABBREVIATED);
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
        if(convertView==null){
            convertView=inflater.inflate(R.layout.content_main_listview,null);
        }
        Poster poster=posterList.get(position);

        posterImage=(ImageView)convertView.findViewById(R.id.listview_background_poster);
        likeNumber=(TextView)convertView.findViewById(R.id.listview_bottom_like_number);
        viewNumber=(TextView)convertView.findViewById(R.id.listview_bottom_view_number);
        name=(TextView)convertView.findViewById(R.id.listview_second_layout_name);
        category=(TextView)convertView.findViewById(R.id.listview_second_layout_category);
        price=(TextView)convertView.findViewById(R.id.listview_second_layout_price);
        location=(TextView)convertView.findViewById(R.id.listview_second_layout_location);
        date=(TextView)convertView.findViewById(R.id.listview_second_layout_date);

        poster.main_picture_loaded.into(posterImage);
        likeNumber.setText(Integer.toString(poster.like));
        viewNumber.setText(Integer.toString(poster.view));
        date.setText(Integer.toString(poster.startDate[0])+Integer.toString(poster.startDate[1])+Integer.toString(poster.startDate[2]));
        name.setText(poster.title);
        category.setText(Integer.toString(poster.category[0])+Integer.toString(poster.category[1]));
        price.setText(Integer.toString(poster.price));
        location.setText(Integer.toString(poster.place));

        if(position==posterList.size()-1&&!reachedLastPoster){
            getPosters();
        }
        main.scrollNumber=position;

        return convertView;
    }

}


public class main extends AppCompatActivity {
    static int scrollNumber=0;
    static boolean scrollIsDownward=true;
    static boolean scrolledByTouch=false;
    static int displayHeight;
    static int displayWidth;

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
        Log.v("Display size : ",Integer.toString(displayWidth)+","+Integer.toString(displayHeight));

        MainInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        Poster.context=this;
        createMainView();


    }



    public void createMainView(){
        setContentView(R.layout.content_main);


        final ListView list=(ListView)findViewById(R.id.main_listview);
        final contentAdapter adapter=new contentAdapter(this);
        list.setAdapter(adapter);


        mainLayout=(RelativeLayout)findViewById(R.id.main);
        TextView filter=(TextView)findViewById(R.id.main_bottom_filter);
        TextView down=(TextView)findViewById(R.id.main_bottom_down);
        TextView up=(TextView)findViewById(R.id.main_bottom_up);
        ImageView logo=(ImageView)findViewById(R.id.main_logo);
        final TextView search=(TextView)findViewById(R.id.main_top_search);

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


        //******SEARCH METHOD


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.reloadPosterFromStart();
                adapter.notifyDataSetChanged();
            }
        });

        //********FOOTER LOADING IMAGE SHOULD BE ADAPTED
        TextView LoadingTextForFooter=new TextView(this);
        ListView.LayoutParams LTFFparams=new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        LoadingTextForFooter.setLayoutParams(LTFFparams);
        LoadingTextForFooter.setText("LOADING...");
        LoadingTextForFooter.setTextColor(Color.BLUE);
        LoadingTextForFooter.setBackgroundColor(Color.WHITE);
        list.addFooterView(LoadingTextForFooter);
        //********

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

                }
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
                scrolledByTouch=false;
                if(!scrollIsDownward&&scrollNumber!=0){
                    scrollNumber+=2;
                }
                list.smoothScrollToPosition(scrollNumber++);
                scrollIsDownward=true;
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

}
