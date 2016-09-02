package dongnae.dongnaewall;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class test extends AppCompatActivity {
    RelativeLayout basic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        basic=new RelativeLayout(this);
        setContentView(basic);

        CalendarCustomView CCV=new CalendarCustomView(this);
        RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        basic.addView(CCV,params);
    }
}
