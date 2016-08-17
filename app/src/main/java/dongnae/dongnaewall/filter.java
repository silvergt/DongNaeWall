package dongnae.dongnaewall;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class filter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter);

        TextView school=(TextView)findViewById(R.id.filterview_first_event_category_school);
        TextView inschool_activities=(TextView)findViewById(R.id.filterview_first_event_category_school_inschool_activities);
        TextView inschool_forums=(TextView)findViewById(R.id.filterview_first_event_category_school_inschool_forums);
        TextView inschool_circles=(TextView)findViewById(R.id.filterview_first_event_category_school_inschool_circles);

        TextView exhibition=(TextView)findViewById(R.id.filterview_first_event_category_exhibition);
        TextView exhibition_art=(TextView)findViewById(R.id.filterview_first_event_category_exhibition_art);
        TextView exhibition_sculpture=(TextView)findViewById(R.id.filterview_first_event_category_exhibition_sculpture);

        TextView performance=(TextView)findViewById(R.id.filterview_first_event_category_performance);
        TextView performance_musical=(TextView)findViewById(R.id.filterview_first_event_category_performance_musical);
        TextView performance_play=(TextView)findViewById(R.id.filterview_first_event_category_performance_play);
        TextView performance_family=(TextView)findViewById(R.id.filterview_first_event_category_performance_family);
        TextView performance_concert=(TextView)findViewById(R.id.filterview_first_event_category_performance_concert);

        TextView presentation=(TextView)findViewById(R.id.filterview_first_event_category_presentation);
        TextView presentation_company=(TextView)findViewById(R.id.filterview_first_event_category_presentation_company);
        TextView presentation_contest=(TextView)findViewById(R.id.filterview_first_event_category_presentation_contest);
        TextView presentation_corporate=(TextView)findViewById(R.id.filterview_first_event_category_presentation_corporate);
        TextView presentation_student=(TextView)findViewById(R.id.filterview_first_event_category_presentation_student);

        TextView job=(TextView)findViewById(R.id.filterview_first_event_category_job);
        TextView job_part_time=(TextView)findViewById(R.id.filterview_first_event_category_job_part_time);
        TextView job_wanted=(TextView)findViewById(R.id.filterview_first_event_category_job_wanted);

        TextView dateFrom=(TextView)findViewById(R.id.filterview_second_primary_category_minimum_date);
        TextView dateTo=(TextView)findViewById(R.id.filterview_second_primary_category_maximum_date);

    }
}
