package dongnae.dongnaewall;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

class filterContentView extends ViewGroup{
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

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}

public class filter extends AppCompatActivity {
    final static int CATEGORY_school=1;
    final static int CATEGORY_exhibition=2;
    final static int CATEGORY_performance=3;
    final static int CATEGORY_presentation=4;
    final static int CATEGORY_job=5;


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

    private filterContentView findfilterContentViewById(int id,filterContentView v){
        boolean tempIsChecked=false;
        boolean tempIsBigCategory=false;
        int[] tempdate=null;
        if(v!=null){
            tempIsChecked=v.isChecked;
            tempIsBigCategory=v.isBigCategory;
            if(id==R.id.filterview_second_primary_category_minimum_date||id==R.id.filterview_second_primary_category_maximum_date){
                tempdate=v.date;
            }
        }
        filterContentView FCV=(filterContentView)findViewById(id);
        FCV.isChecked=tempIsChecked;
        FCV.isBigCategory=tempIsBigCategory;
        FCV.date=tempdate;
        changeCheckedState(FCV.isChecked, FCV);
        
        return FCV;
    }
    
    
    public void initialize(){

        school=findfilterContentViewById(R.id.filterview_first_event_category_school,school);
        school.isBigCategory=true;
        school.category=CATEGORY_school;
        inschool_activities=findfilterContentViewById(R.id.filterview_first_event_category_school_inschool_activities,inschool_activities);
        inschool_activities.category=CATEGORY_school;
        inschool_forums=findfilterContentViewById(R.id.filterview_first_event_category_school_inschool_forums,inschool_forums);
        inschool_forums.category=CATEGORY_school;
        inschool_circles=findfilterContentViewById(R.id.filterview_first_event_category_school_inschool_circles,inschool_circles);
        inschool_circles.category=CATEGORY_school;

        exhibition=findfilterContentViewById(R.id.filterview_first_event_category_exhibition,exhibition);
        exhibition.isBigCategory=true;
        exhibition.category=CATEGORY_exhibition;
        exhibition_art=findfilterContentViewById(R.id.filterview_first_event_category_exhibition_art,exhibition_art);
        exhibition_art.category=CATEGORY_exhibition;
        exhibition_sculpture=findfilterContentViewById(R.id.filterview_first_event_category_exhibition_sculpture,exhibition_sculpture);
        exhibition_sculpture.category=CATEGORY_exhibition;

        performance=findfilterContentViewById(R.id.filterview_first_event_category_performance,performance);
        performance.isBigCategory=true;
        performance.category=CATEGORY_performance;
        performance_musical=findfilterContentViewById(R.id.filterview_first_event_category_performance_musical,performance_musical);
        performance_musical.category=CATEGORY_performance;
        performance_play=findfilterContentViewById(R.id.filterview_first_event_category_performance_play,performance_play);
        performance_play.category=CATEGORY_performance;
        performance_family=findfilterContentViewById(R.id.filterview_first_event_category_performance_family,performance_family);
        performance_family.category=CATEGORY_performance;
        performance_concert=findfilterContentViewById(R.id.filterview_first_event_category_performance_concert,performance_concert);
        performance_concert.category=CATEGORY_performance;

        presentation=findfilterContentViewById(R.id.filterview_first_event_category_presentation,presentation);
        presentation.isBigCategory=true;
        presentation.category=CATEGORY_presentation;
        presentation_company=findfilterContentViewById(R.id.filterview_first_event_category_presentation_company,presentation_company);
        presentation_company.category=CATEGORY_presentation;
        presentation_contest=findfilterContentViewById(R.id.filterview_first_event_category_presentation_contest,presentation_contest);
        presentation_contest.category=CATEGORY_presentation;
        presentation_corporate=findfilterContentViewById(R.id.filterview_first_event_category_presentation_corporate,presentation_corporate);
        presentation_corporate.category=CATEGORY_presentation;
        presentation_student=findfilterContentViewById(R.id.filterview_first_event_category_presentation_student,presentation_student);
        presentation_student.category=CATEGORY_presentation;

        job=findfilterContentViewById(R.id.filterview_first_event_category_job,job);
        job.isBigCategory=true;
        job.category=CATEGORY_job;
        job_part_time=findfilterContentViewById(R.id.filterview_first_event_category_job_part_time,job_part_time);
        job_part_time.category=CATEGORY_job;
        job_wanted=findfilterContentViewById(R.id.filterview_first_event_category_job_wanted,job_wanted);
        job_wanted.category=CATEGORY_job;

        dateFrom=findfilterContentViewById(R.id.filterview_second_primary_category_minimum_date,dateFrom);
        dateTo=findfilterContentViewById(R.id.filterview_second_primary_category_maximum_date,dateTo);
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

}
