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
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class main extends AppCompatActivity {
    static int scrollNumber=0;
    static boolean scrollIsDownward=true;
    static int displayHeight=0;
    static int displayWidth=0;

    ListView list;
    static contentAdapter adapter;
    static TextView listFooter;
    static LinearLayout listHeader;
    static LinearLayout blankView;
    RelativeLayout profileLayout;
    RelativeLayout mainProfileLayout;

    static boolean searchBarIsVisible=false;
    LinearLayout searchBar;
    RelativeLayout mainLayout;
    InputMethodManager IMM;

    TextView filter;
    TextView down;
    TextView up;
    ImageView logo;
    TextView search;

    LayoutInflater MainInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP) {
            Window window =getWindow();
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.basicStatusBarColor));
        }

        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        displayWidth=metrics.widthPixels;
        displayHeight=metrics.heightPixels;
        Log.v("Display size : ", Integer.toString(displayWidth) + "," + Integer.toString(displayHeight));

        IMM = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);


        MainInflater=(LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        Poster.context=this;
        createMainView();


    }

    public void createMainView(){
        setContentView(R.layout.content_main);


        mainLayout=(RelativeLayout)findViewById(R.id.main);
        mainProfileLayout=(RelativeLayout)findViewById(R.id.main_background_profilelayout);
        filter=(TextView)findViewById(R.id.main_bottom_filter);
        down=(TextView)findViewById(R.id.main_bottom_down);
        up=(TextView)findViewById(R.id.main_bottom_up);
        logo=(ImageView)findViewById(R.id.main_logo);
        search=(TextView)findViewById(R.id.main_top_search);
        list=(ListView)findViewById(R.id.main_listview);
        TextView alarm=(TextView)findViewById(R.id.main_top_alarm);

        TempData.changeStatus(TempData.STATUS_RECOMMENDATION);

        if(adapter==null) {
            Log.v("Log","first setup for header & footer");
            setHeaderFooterViewToList();
        }
        adapter=new contentAdapter(this);
        list.setAdapter(adapter);
        OverScrollDecoratorHelper.setUpOverScroll(list);

        list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState==SCROLL_STATE_TOUCH_SCROLL){
                    setSearchBarStatus(false);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        TempData.changeStatus(TempData.STATUS_RECOMMENDATION);  //MANDATORY BECAUSE OF setHeaderFooterViewVisibility()


        //******SEARCH METHOD
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSearchBarStatus(!searchBarIsVisible);
            }
        });

        alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.reloadPosterFromStart(TempData.STATUS_POSTER_ABBREVIATED);
                adapter.notifyDataSetChanged();
                setSearchBarStatus(false);
            }
        });



        //******SEARCH METHOD


        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.reloadPosterFromStart(TempData.STATUS_RECOMMENDATION);
                adapter.notifyDataSetChanged();
                if(searchBarIsVisible) {
                    setSearchBarStatus(false);
                }
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBarIsVisible){
                    setSearchBarStatus(false);
                }
                Intent intent=new Intent(main.this, filter.class);
                startActivity(intent);
            }
        });

        up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBarIsVisible){
                    setSearchBarStatus(false);
                }
                if (scrollIsDownward) {
                    scrollNumber -= 2;
                    if (scrollNumber < 0) {
                        scrollNumber = 0;
                    }
                }
                //Log.v("Log scroll to",Integer.toString(scrollNumber));
                list.smoothScrollToPosition(scrollNumber--);
                scrollIsDownward = false;

                if (scrollNumber < 0) {
                    scrollNumber = 0;
                }



            }
        });
        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(searchBarIsVisible){
                    setSearchBarStatus(false);
                }
                if (!scrollIsDownward && scrollNumber > 1) {
                    scrollNumber += 2;
                } else if (!scrollIsDownward && scrollNumber <= 1) {
                    scrollNumber += 1;
                }

                //Log.v("Log scroll to",Integer.toString(scrollNumber));
                list.smoothScrollToPosition(scrollNumber++);
                if (scrollNumber > adapter.getCount() + 2) {
                    scrollNumber = adapter.getCount() + 2;
                }
                scrollIsDownward = true;

            }

        });
    }

    @Override
    public void onBackPressed() {
        if(searchBarIsVisible){
            setSearchBarStatus(false);
            return;
        }
        super.onBackPressed();


    }

    private void setHeaderFooterViewToList(){
        try{
            mainProfileLayout.removeAllViews();
        }catch (Exception e){
            Log.v("Log","profileLayout not detected");
        }

        listHeader=(LinearLayout) MainInflater.inflate(R.layout.recommendation_headerview,null);

        ListView.LayoutParams blankViewParams=new ListView.LayoutParams(
                1,main.displayHeight*3/5);
        ListView.LayoutParams listHeaderParams=new ListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,main.displayHeight*3/10);
        blankView=new LinearLayout(this);
        blankView.setLayoutParams(blankViewParams);
        listHeader.setLayoutParams(listHeaderParams);
        TextView headerText1=(TextView)listHeader.findViewById(R.id.recommendation_headerview_text1);
        TextView headerText2=(TextView)listHeader.findViewById(R.id.recommendation_headerview_text2);
        headerText1.setPadding(main.displayWidth/10,main.displayHeight/20,0,0);
        headerText2.setPadding(main.displayWidth/10,5,0,0);

        list.addHeaderView(blankView);
        list.addHeaderView(listHeader);

        profileLayout=(RelativeLayout)MainInflater.inflate(R.layout.profile_layout, null);
        mainProfileLayout.addView(profileLayout);

        main.scrollNumber=1;

        listFooter=new TextView(this);
        ListView.LayoutParams listFooterParams=new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100);
        listFooter.setLayoutParams(listFooterParams);
        listFooter.setText("LOADING...");
        listFooter.setTextColor(Color.BLUE);
        listFooter.setBackgroundColor(Color.WHITE);
        list.addFooterView(listFooter);

        Log.v("Log","header & footer view adapted");

    }

    public static void setHeaderFooterViewVisibility(){
        try {
            if (TempData.getStatus() == TempData.STATUS_RECOMMENDATION) {
                Log.v("Log", "setting visibility of header,footer view... RECOMM");
                ListView.LayoutParams blankViewParams = new ListView.LayoutParams(
                        1, main.displayHeight * 3 / 5);
                ListView.LayoutParams listHeaderParams = new ListView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, main.displayHeight * 3 / 10);
                ListView.LayoutParams invisibleParam = new AbsListView.LayoutParams(1, 1);
                blankView.setLayoutParams(blankViewParams);
                listHeader.setLayoutParams(listHeaderParams);
                listFooter.setLayoutParams(invisibleParam);
            } else if (TempData.getStatus() == TempData.STATUS_POSTER_ABBREVIATED) {
                ListView.LayoutParams listFooterParams = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
                ListView.LayoutParams invisibleParam = new AbsListView.LayoutParams(1, 1);
                Log.v("Log", "setting visibility of header,footer view... ABBR");
                blankView.setLayoutParams(invisibleParam);
                listHeader.setLayoutParams(invisibleParam);
                listFooter.setLayoutParams(listFooterParams);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.e("Log","setHeaderFooterViewVisibility ERROR");
        }

    }

    public void setSearchBarStatus(boolean turnOnSearchBar){
        try {
            if (turnOnSearchBar) {
                RelativeLayout.LayoutParams searchBarParams = new RelativeLayout.LayoutParams
                        (ViewGroup.LayoutParams.MATCH_PARENT, (int) getResources().getDimension(R.dimen.topbar_size));
                searchBarParams.addRule(RelativeLayout.BELOW, R.id.main_topbar);
                searchBar = (LinearLayout) MainInflater.inflate(R.layout.searchbar, null);
                mainLayout.addView(searchBar, searchBarParams);
                searchBarIsVisible = true;
            } else {
                IMM.hideSoftInputFromWindow(searchBar.getWindowToken(),0);
                mainLayout.removeView(searchBar);
                searchBarIsVisible = false;
            }
        }catch (Exception e){
            Log.e("Log","SearchBar Visibility failed to be changed!");
        }
    }
}

class contentAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    ArrayList<Poster> posterList;
    ArrayList<Poster> posters;
    ServerConnector SC;
    boolean reachedLastPoster=false;

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
        SC=new ServerConnector();
        posterList=new ArrayList<>();
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        getPosters();
    }

    public void reloadPosterFromStart(int status){
        posterList=new ArrayList<>();
        main.scrollNumber=0;
        TempData.changeStatus(status);
        TempData.changeStartNum(0);
        reachedLastPoster=false;
        getPosters();

    }

    public boolean getPosters(){

        AsyncTask<Integer, Integer, Boolean> posterThread=new AsyncTask<Integer, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Integer[] params) {
                posters=SC.getPoster();
                if(posters==null){
                    Log.e("Log","NO SERVER RECEIVED POSTERS!");
                    return false;
                }else if(posters.size()!=0) {
                    Log.v("Log", "received");
                    posterList.addAll(posters);
                    publishProgress(0);
                    TempData.changeStartNum(TempData.getStartNum() + posters.size());
                    Log.v("total Loaded poster :", Integer.toString(TempData.getStartNum()));
                    return true;
                }else if(posters.size()==0){
                    Log.v("Log","poster size is zero. client reached last poster!");
                    reachedLastPoster=true;
                    return true;
                }

                return true;
            }


            @Override
            protected void onProgressUpdate(Integer[] values) {
                super.onProgressUpdate(values);
                Log.v("Log","progress updating");
                notifyDataSetChanged();
            }
        };
        posterThread.execute(0);

        return false;

    }

    @Override
    public int getCount() {
        return posterList.size();
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
        if(TempData.getStatus()==TempData.STATUS_POSTER_ABBREVIATED) {
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
            main.scrollNumber = position+2;
        }else if(TempData.getStatus()==TempData.STATUS_RECOMMENDATION){
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

            main.scrollNumber = position+2;
        }
        //Log.v("Log from getView",Integer.toString(main.scrollNumber));
        return convertView;
    }

}


