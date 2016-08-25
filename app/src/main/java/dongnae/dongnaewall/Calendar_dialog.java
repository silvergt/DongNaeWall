package dongnae.dongnaewall;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;


public class Calendar_dialog extends Dialog{
    Context context;
    LayoutInflater inflater;
    int selectedYear;
    int selectedMonth;
    int selectedDay;

    TextView leftButton;
    TextView yearAndMonth;
    TextView rightButton;


    LinearLayout calendar_calendar;
    Calendar today;
    LinearLayout calendarLine;
    TextView[] calendarLine_line;

    public Calendar_dialog(Context context) {
        super(context);
        this.context=context;
        onCreate();
    }

    private void onCreate(){
        setContentView(R.layout.calendar);

        WindowManager.LayoutParams par=getWindow().getAttributes();
        par.width=main.displayWidth*4/5;
        par.height=main.displayHeight/2;
        getWindow().setAttributes(par);

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        calendar_calendar=(LinearLayout)findViewById(R.id.calendar_calendar);
        leftButton=(TextView)findViewById(R.id.calendar_left);
        yearAndMonth=(TextView)findViewById(R.id.calendar_yymm);
        rightButton=(TextView)findViewById(R.id.calendar_right);

        today=Calendar.getInstance();

        createCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH),today.get(Calendar.DATE));

    }

    private void createCalendar(int year,int month,int day){
        if(month+1<10) {
            yearAndMonth.setText(Integer.toString(year) + ".0" + Integer.toString(month + 1));
        }else{
            yearAndMonth.setText(Integer.toString(year) + "." + Integer.toString(month + 1));
        }
        Calendar cal=Calendar.getInstance();
        cal.set(year,month,day);

        calendar_calendar.removeAllViews();
        boolean calendarLineIsFull=true;
        for(int i=1;i<=cal.getActualMaximum(Calendar.DATE);){
            if(calendarLineIsFull) {
                LinearLayout calendarLine = (LinearLayout) inflater.inflate(R.layout.calendar_line, null);
                LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
                params.weight=1;
                calendar_calendar.addView(calendarLine,params);

                calendarLine_line=new TextView[7];
                calendarLine_line[0]=(TextView)calendarLine.findViewById(R.id.calendar_line1);
                calendarLine_line[1]=(TextView)calendarLine.findViewById(R.id.calendar_line2);
                calendarLine_line[2]=(TextView)calendarLine.findViewById(R.id.calendar_line3);
                calendarLine_line[3]=(TextView)calendarLine.findViewById(R.id.calendar_line4);
                calendarLine_line[4]=(TextView)calendarLine.findViewById(R.id.calendar_line5);
                calendarLine_line[5]=(TextView)calendarLine.findViewById(R.id.calendar_line6);
                calendarLine_line[6]=(TextView)calendarLine.findViewById(R.id.calendar_line7);

                calendarLineIsFull=false;
            }
            /**
            //****************MODIFYHERE!!!!!!!
             *
             *
             *
             */
        }
    }


    public int[] getSelectedDate(){
        return new int[]{selectedYear,selectedMonth,selectedDay};
    }

}
