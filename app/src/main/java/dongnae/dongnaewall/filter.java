package dongnae.dongnaewall;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

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
    
    
    @Override 
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);
        initialize();

    }

    public static boolean[] getFilterCheckedData(){
        if(categoryList==null){
            Log.v("Log","categoryList is null. sending boolean[]==null");
            return null;
        }
        boolean[] returningArray=new boolean[categoryList.size()];
        Log.v("Log","making array... size :"+Integer.toString(returningArray.length));
        for(int i=0;i<returningArray.length;i++){
            returningArray[i]=categoryList.get(i).isChecked;
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

        categoryList.add(FCV);
        
        return FCV;
    }

    private void initialize(){

        categoryList=new ArrayList<>();

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
    }
    
    public void filterContentClicked(View view){
        filterContentView v=(filterContentView)view;
            if(v.category!=0){
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
            }

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
}
