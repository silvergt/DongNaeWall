package dongnae.dongnaewall;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

class filterContentView extends Button{
    boolean isChecked=false;
    boolean isBigCategory=false;
    int[] date=new int[3];

    int category=0;

    private void initialize(){
        filter.changeCheckedState(isChecked,this);
    }

    public filterContentView(Context context) {
        super(context);
        initialize();
    }

    public filterContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

}

public class filter extends AppCompatActivity {
    final static int CATEGORY_NONE=0;
    final static int CATEGORY_school=1;
    final static int CATEGORY_exhibition=2;
    final static int CATEGORY_performance=3;
    final static int CATEGORY_presentation=4;
    final static int CATEGORY_job=5;

    static ArrayList<filterContentView> categoryList;
    static ArrayList<filterContentView> dayOfWeek;
    Calendar_dialog calendar_dialog;

    static filterContentView school=null;
    static filterContentView inschool_activities;
    static filterContentView inschool_forums;
    static filterContentView inschool_circles;

    static filterContentView exhibition;
    static filterContentView exhibition_art;
    static filterContentView exhibition_sculpture;

    static filterContentView performance;
    static filterContentView performance_musical;
    static filterContentView performance_play;
    static filterContentView performance_family;
    static filterContentView performance_concert;

    static filterContentView presentation;
    static filterContentView presentation_company;
    static filterContentView presentation_contest;
    static filterContentView presentation_corporate;
    static filterContentView presentation_student;

    static filterContentView job;
    static filterContentView job_part_time;
    static filterContentView job_wanted;

    static filterContentView dateFrom;
    static filterContentView dateTo;

    static filterContentView day_sun;
    static filterContentView day_mon;
    static filterContentView day_tue;
    static filterContentView day_wed;
    static filterContentView day_thu;
    static filterContentView day_fri;
    static filterContentView day_sat;

    static TextView minimumPrice;   static int minimumPriceValue=0;
    static TextView maximumPrice;   static int maximumPriceValue=20;
    static RangeSeekBar priceSeekBar;



    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        ScrollView scrollView=(ScrollView)findViewById(R.id.filterview_scrollview);
        OverScrollDecoratorHelper.setUpOverScroll(scrollView);

        initialize();

        priceSeekBar.setSelectedMinValue(minimumPriceValue);
        priceSeekBar.setSelectedMaxValue(maximumPriceValue);
        setPriceTextView();
        priceSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setPriceTextView();
                return false;
            }
        });

    }

    public static boolean[] getFilterCheckedData(){
        if(categoryList==null){
            Log.v("Log","categoryList is null. sending boolean[]==null");
            return null;
        }
        boolean[] returningArray=new boolean[categoryList.size()];
        Log.v("Log","making categoryList array... size :"+Integer.toString(returningArray.length));
        for(int i=0;i<returningArray.length;i++){
            returningArray[i]=categoryList.get(i).isChecked;
        }

        return returningArray;
    }

    public static boolean[] getDayOfWeekCheckedData(){
        if(dayOfWeek==null){
            Log.v("Log","dayOfWeek is null. sending boolean[]==null");
            return null;
        }
        boolean[] returningArray=new boolean[dayOfWeek.size()];
        Log.v("Log","making dayOfWeek array... size :"+Integer.toString(returningArray.length));
        for(int i=0;i<returningArray.length;i++){
            returningArray[i]=dayOfWeek.get(i).isChecked;
        }

        return returningArray;
    }

    private filterContentView findFilterContentViewById(int id,filterContentView v,boolean isBigCategory,int category){
        boolean tempIsChecked=false;
        int[] tempdate=null;
        if(v!=null){
            tempIsChecked=v.isChecked;
            tempdate=v.date;
        }
        filterContentView FCV=(filterContentView)findViewById(id);
        FCV.isChecked=tempIsChecked;
        FCV.isBigCategory=isBigCategory;
        FCV.date=tempdate;
        FCV.category=category;
        changeCheckedState(FCV.isChecked, FCV);

        if(category!=CATEGORY_NONE) {
            categoryList.add(FCV);
        }else{
            switch (FCV.getId()){
                case R.id.filterview_day_sun:case R.id.filterview_day_mon:case R.id.filterview_day_tue:case R.id.filterview_day_wed:
                case R.id.filterview_day_thu:case R.id.filterview_day_fri:case R.id.filterview_day_sat:
                    dayOfWeek.add(FCV);
                    break;
                case R.id.filterview_second_primary_category_minimum_date:case R.id.filterview_second_primary_category_maximum_date:
                    if(tempdate!=null) {
                        String str = Integer.toString(tempdate[0])+"." + Integer.toString(tempdate[1]) + "."
                                + Integer.toString(tempdate[2]);
                        FCV.setText(str);
                    }
            }
        }

        return FCV;
    }

    private void initialize(){

        categoryList=new ArrayList<>();
        dayOfWeek=new ArrayList<>();

        school=findFilterContentViewById(R.id.filterview_first_event_category_school, school, true, CATEGORY_school);
        inschool_activities=findFilterContentViewById(R.id.filterview_first_event_category_school_inschool_activities, inschool_activities, false, CATEGORY_school);
        inschool_forums=findFilterContentViewById(R.id.filterview_first_event_category_school_inschool_forums, inschool_forums, false, CATEGORY_school);
        inschool_circles=findFilterContentViewById(R.id.filterview_first_event_category_school_inschool_circles, inschool_circles, false, CATEGORY_school);

        exhibition=findFilterContentViewById(R.id.filterview_first_event_category_exhibition, exhibition, true, CATEGORY_exhibition);
        exhibition_art=findFilterContentViewById(R.id.filterview_first_event_category_exhibition_art, exhibition_art, false, CATEGORY_exhibition);
        exhibition_sculpture=findFilterContentViewById(R.id.filterview_first_event_category_exhibition_sculpture, exhibition_sculpture, false, CATEGORY_exhibition);

        performance=findFilterContentViewById(R.id.filterview_first_event_category_performance, performance, true, CATEGORY_performance);
        performance_musical=findFilterContentViewById(R.id.filterview_first_event_category_performance_musical, performance_musical, false, CATEGORY_performance);
        performance_play=findFilterContentViewById(R.id.filterview_first_event_category_performance_play, performance_play, false, CATEGORY_performance);
        performance_family=findFilterContentViewById(R.id.filterview_first_event_category_performance_family, performance_family, false, CATEGORY_performance);
        performance_concert=findFilterContentViewById(R.id.filterview_first_event_category_performance_concert, performance_concert, false, CATEGORY_performance);

        presentation=findFilterContentViewById(R.id.filterview_first_event_category_presentation, presentation, true, CATEGORY_presentation);
        presentation_company=findFilterContentViewById(R.id.filterview_first_event_category_presentation_company, presentation_company, false, CATEGORY_presentation);
        presentation_contest=findFilterContentViewById(R.id.filterview_first_event_category_presentation_contest, presentation_contest, false, CATEGORY_presentation);
        presentation_corporate=findFilterContentViewById(R.id.filterview_first_event_category_presentation_corporate, presentation_corporate, false, CATEGORY_presentation);
        presentation_student=findFilterContentViewById(R.id.filterview_first_event_category_presentation_student, presentation_student, false, CATEGORY_presentation);

        job=findFilterContentViewById(R.id.filterview_first_event_category_job, job, true, CATEGORY_job);
        job_part_time=findFilterContentViewById(R.id.filterview_first_event_category_job_part_time, job_part_time, false, CATEGORY_job);
        job_wanted=findFilterContentViewById(R.id.filterview_first_event_category_job_wanted, job_wanted, false, CATEGORY_job);

        dateFrom=findFilterContentViewById(R.id.filterview_second_primary_category_minimum_date, dateFrom, false, CATEGORY_NONE);
        dateTo=findFilterContentViewById(R.id.filterview_second_primary_category_maximum_date, dateTo, false, CATEGORY_NONE);

        day_sun=findFilterContentViewById(R.id.filterview_day_sun,day_sun,false,CATEGORY_NONE);
        day_mon=findFilterContentViewById(R.id.filterview_day_mon,day_mon,false,CATEGORY_NONE);
        day_tue=findFilterContentViewById(R.id.filterview_day_tue,day_tue,false,CATEGORY_NONE);
        day_wed=findFilterContentViewById(R.id.filterview_day_wed,day_wed,false,CATEGORY_NONE);
        day_thu=findFilterContentViewById(R.id.filterview_day_thu,day_thu,false,CATEGORY_NONE);
        day_fri=findFilterContentViewById(R.id.filterview_day_fri,day_fri,false,CATEGORY_NONE);
        day_sat=findFilterContentViewById(R.id.filterview_day_sat,day_sat,false,CATEGORY_NONE);

        minimumPrice=(TextView)findViewById(R.id.filterview_minimumprice);
        maximumPrice=(TextView)findViewById(R.id.filterview_maximumprice);
        priceSeekBar=(RangeSeekBar)findViewById(R.id.filterview_seekbar);

    }

    public void filterContentClicked(View view){
        filterContentView v=(filterContentView)view;
            if(v.category!=CATEGORY_NONE){
                if(v.isBigCategory) {
                    switch (v.category){
                        case CATEGORY_school:
                            changeCheckedState(!v.isChecked,v,inschool_forums,inschool_circles,inschool_activities);
                            break;
                        case CATEGORY_exhibition:
                            changeCheckedState(!v.isChecked,v,exhibition_art,exhibition_sculpture);
                            break;
                        case CATEGORY_performance:
                            changeCheckedState(!v.isChecked,v,performance_concert,performance_play,performance_musical,performance_family);
                            break;
                        case CATEGORY_presentation:
                            changeCheckedState(!v.isChecked,v,presentation_student,presentation_corporate,presentation_contest,presentation_company);
                            break;
                        case CATEGORY_job:
                            changeCheckedState(!v.isChecked,v,job_wanted,job_part_time);
                            break;
                    }
                }else{      //not bigCategory
                    if(v.isChecked){
                        switch (v.category){
                            case CATEGORY_school:
                                changeCheckedState(false,v,school);
                                break;
                            case CATEGORY_exhibition:
                                changeCheckedState(false,v,exhibition);
                                break;
                            case CATEGORY_performance:
                                changeCheckedState(false,v,performance);
                                break;
                            case CATEGORY_presentation:
                                changeCheckedState(false,v,presentation);
                                break;
                            case CATEGORY_job:
                                changeCheckedState(false,v,job);
                                break;
                        }
                    }else{
                        changeCheckedState(true,v);
                    }
                }
            }else if(v.category==CATEGORY_NONE){
                switch (v.getId()){
                    case R.id.filterview_second_primary_category_minimum_date:
                        startCalendarDialog(true);
                        return;
                    case R.id.filterview_second_primary_category_maximum_date:
                        startCalendarDialog(false);
                        return;
                }
                if(v.isChecked){
                    changeCheckedState(false,v);
                }else{
                    changeCheckedState(true,v);
                }



        }

    }

    public void startCalendarDialog(final boolean clickedStartDate){
        int year;int month;int date;
        if(clickedStartDate&&dateFrom.date!=null){
            year=dateFrom.date[0];month=dateFrom.date[1];date=dateFrom.date[2];
            calendar_dialog=new Calendar_dialog(this,year,month,date);
        }else if(!clickedStartDate&&dateTo.date!=null){
            year=dateTo.date[0];month=dateTo.date[1];date=dateTo.date[2];
            calendar_dialog=new Calendar_dialog(this,year,month,date);
        }else{
            calendar_dialog=new Calendar_dialog(this);
        }
        calendar_dialog.show();
        calendar_dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(calendar_dialog.isDismissedWithOrder){
                    int[] date=calendar_dialog.getSelectedDate();
                    if(date[0]==0&&date[1]==0&&date[2]==0){
                        if(clickedStartDate){
                            dateFrom.date=null;
                            dateFrom.setText(getResources().getText(R.string.dateFrom));
                            return;
                        }else{
                            dateTo.date=null;
                            dateTo.setText(getResources().getText(R.string.dateTo));
                            return;
                        }
                    }
                    String str=Integer.toString(date[0])+"."+Integer.toString(date[1])+"."
                            +Integer.toString(date[2]);
                    if(clickedStartDate){
                        if(dateTo.date!=null){
                            if(dateTo.date[0]<date[0]){
                                Toast.makeText(filter.this,"종료일보다 이후의 날짜입니다",Toast.LENGTH_SHORT).show();
                                return;
                            }else if(dateTo.date[0]==date[0]){
                                if(dateTo.date[1]<date[1]){
                                    Toast.makeText(filter.this,"종료일보다 이후의 날짜입니다",Toast.LENGTH_SHORT).show();
                                    return;
                                }else if (dateTo.date[1]==date[1]){
                                    if(dateTo.date[2]<date[2]){
                                        Toast.makeText(filter.this,"종료일보다 이후의 날짜입니다",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                        }
                        dateFrom.date=date;
                        dateFrom.setText(str);
                    }else{
                        if(dateFrom.date!=null){
                            if(dateFrom.date[0]>date[0]){
                                Toast.makeText(filter.this,"시작일보다 이전의 날짜입니다",Toast.LENGTH_SHORT).show();
                                return;
                            }else if(dateFrom.date[0]==date[0]){
                                if(dateFrom.date[1]>date[1]){
                                    Toast.makeText(filter.this,"시작일보다 이전의 날짜입니다",Toast.LENGTH_SHORT).show();
                                    return;
                                }else if (dateFrom.date[1]==date[1]){
                                    if(dateFrom.date[2]>date[2]){
                                        Toast.makeText(filter.this,"시작일보다 이전의 날짜입니다",Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                        }
                        dateTo.date=date;
                        dateTo.setText(str);
                    }
                }
            }
        });
    }

    public static void changeCheckedState(boolean changeStateToChecked,filterContentView... views){

        if(changeStateToChecked) {
            for (int i = 0; i < views.length; i++) {
                if(views[i].isBigCategory){
                    views[i].setBackgroundColor(Color.parseColor("#505050"));
                }else {
                    views[i].setBackgroundColor(Color.parseColor("#38F190"));
                }
                views[i].isChecked=true;
            }
        }else{
            for (int i = 0; i < views.length; i++) {
                if(views[i].isBigCategory) {
                    views[i].setBackgroundColor(Color.parseColor("#EFB3F0"));
                }else {
                    views[i].setBackgroundColor(Color.parseColor("#D8BFD8"));
                }
                views[i].isChecked=false;
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.v("Log","filterView closed");
        finish();
    }
    
    public void setPriceTextView(){
        switch (priceSeekBar.getSelectedMinValue().intValue()){
            case 0:
                minimumPrice.setText("무료");
                break;
            case 1:
                minimumPrice.setText("1000￦");
                break;
            case 2:
                minimumPrice.setText("2000￦");
                break;
            case 3:
                minimumPrice.setText("3000￦");
                break;
            case 4:
                minimumPrice.setText("4000￦");
                break;
            case 5:
                minimumPrice.setText("5000￦");
                break;
            case 6:
                minimumPrice.setText("6000￦");
                break;
            case 7:
                minimumPrice.setText("7000￦");
                break;
            case 8:
                minimumPrice.setText("8000￦");
                break;
            case 9:
                minimumPrice.setText("9000￦");
                break;
            case 10:
                minimumPrice.setText("10000￦");
                break;
            case 11:
                minimumPrice.setText("12000￦");
                break;
            case 12:
                minimumPrice.setText("15000￦");
                break;
            case 13:
                minimumPrice.setText("20000￦");
                break;
            case 14:
                minimumPrice.setText("25000￦");
                break;
            case 15:
                minimumPrice.setText("30000￦");
                break;
            case 16:
                minimumPrice.setText("35000￦");
                break;
            case 17:
                minimumPrice.setText("50000￦");
                break;
            case 18:
                minimumPrice.setText("75000￦");
                break;
            case 19:
                minimumPrice.setText("100000￦");
                break;
            case 20:
                minimumPrice.setText("100000￦ 이상");
                break;
        }
        
        switch (priceSeekBar.getSelectedMaxValue().intValue()){
            case 0:
                maximumPrice.setText("무료");
                break;
            case 1:
                maximumPrice.setText("1000￦");
                break;
            case 2:
                maximumPrice.setText("2000￦");
                break;
            case 3:
                maximumPrice.setText("3000￦");
                break;
            case 4:
                maximumPrice.setText("4000￦");
                break;
            case 5:
                maximumPrice.setText("5000￦");
                break;
            case 6:
                maximumPrice.setText("6000￦");
                break;
            case 7:
                maximumPrice.setText("7000￦");
                break;
            case 8:
                maximumPrice.setText("8000￦");
                break;
            case 9:
                maximumPrice.setText("9000￦");
                break;
            case 10:
                maximumPrice.setText("10000￦");
                break;
            case 11:
                maximumPrice.setText("12000￦");
                break;
            case 12:
                maximumPrice.setText("15000￦");
                break;
            case 13:
                maximumPrice.setText("20000￦");
                break;
            case 14:
                maximumPrice.setText("25000￦");
                break;
            case 15:
                maximumPrice.setText("30000￦");
                break;
            case 16:
                maximumPrice.setText("35000￦");
                break;
            case 17:
                maximumPrice.setText("50000￦");
                break;
            case 18:
                maximumPrice.setText("75000￦");
                break;
            case 19:
                maximumPrice.setText("100000￦");
                break;
            case 20:
                maximumPrice.setText("100000￦ 이상");
                break;
                
        }
        minimumPriceValue=priceSeekBar.getSelectedMinValue().intValue();
        maximumPriceValue=priceSeekBar.getSelectedMaxValue().intValue();
    }
}
