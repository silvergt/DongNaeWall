package dongnae.dongnaewall;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;

class calendarCell extends Button{
    int cellNumber[];
    public calendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.WHITE);
    }

    public void setCellNumber(int column,int row){
        cellNumber=new int[]{column,row};
    }
}


public class Calendar_dialog extends Dialog implements View.OnClickListener{
    Context context;
    LayoutInflater inflater;
    int selectedYear;
    int selectedMonth;
    int selectedDay;
    int selectedCell[];
    int todayCell[];

    TextView leftButton;
    TextView yearAndMonth;
    TextView rightButton;
    TextView cancel;
    TextView apply;

    LinearLayout[] calendar_week;
    calendarCell[][] calendar_day;

    LinearLayout calendar_calendar;
    GregorianCalendar calendar;
    Calendar today;

    boolean isDismissedWithOrder=false;

    public Calendar_dialog(Context context) {
        super(context);
        this.context=context;
        onCreate();
    }

    private void onCreate(){
        setContentView(R.layout.calendar);

        WindowManager.LayoutParams par=getWindow().getAttributes();
        par.width=main.displayWidth*4/5>450?main.displayWidth*4/5:450;
        par.height=main.displayHeight*3/5>570?main.displayHeight*3/5:570;
        getWindow().setAttributes(par);

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        calendar_calendar=(LinearLayout)findViewById(R.id.calendar_calendar);
        leftButton=(TextView)findViewById(R.id.calendar_left);
        yearAndMonth=(TextView)findViewById(R.id.calendar_yymm);
        rightButton=(TextView)findViewById(R.id.calendar_right);
        cancel=(TextView)findViewById(R.id.calendar_cancel);
        apply=(TextView)findViewById(R.id.calendar_apply);

        today=Calendar.getInstance();
        calendar=new GregorianCalendar(today.get(Calendar.YEAR),today.get(Calendar.MONTH),1);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)-1);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDismissedWithOrder=true;
                selectedYear=0;
                selectedMonth=0;
                selectedDay=0;
                dismiss();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDismissedWithOrder=true;
                if(selectedYear==0&&selectedMonth==0&&selectedDay==0){
                    selectedYear=today.get(Calendar.YEAR);
                    selectedMonth=today.get(Calendar.MONTH)+1;
                    selectedDay=today.get(Calendar.DATE);
                }
                dismiss();
            }
        });

        createCalendar(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH));

    }

    private Calendar createCalendar(int year, int month){
        if(month>=12){year++;month=0;}
        if(month<0){year--;month=11;}

        calendar.set(year,month,1);

        if(month+1<10) {
            yearAndMonth.setText(Integer.toString(year) + ".0" + Integer.toString(month+1));
        }else{
            yearAndMonth.setText(Integer.toString(year) + "." + Integer.toString(month+1));
        }
        Log.v("Log",Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+Integer.toString(1));
        Log.v("Log",calendar.getTime().toString());

        calendar_calendar.removeAllViews();
        int dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);    //해당 월의 1일의 요일 - 1:일 2:월 3:화....
        int numberOfWeeks=0;                            //해당 월의 주 숫자
        int totalDays=calendar.getActualMaximum(Calendar.DATE);
        for(int i=0;i<totalDays;){
            numberOfWeeks++;
            for(int j=dayOfWeek;j<=7;j++){
                i++;
            }
            dayOfWeek=1;
        }
        Log.v("Log","number of weeks : "+Integer.toString(numberOfWeeks));
        Log.v("Log","total day : "+Integer.toString(totalDays));

        calendar_week=new LinearLayout[numberOfWeeks];
        calendar_day=new calendarCell[numberOfWeeks][7];


        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
        params.weight=1;
        dayOfWeek=calendar.get(Calendar.DAY_OF_WEEK);
        int i=1;
        for(int j=0;j<numberOfWeeks;j++){
            if(i<=totalDays) {
                calendar_week[j] = (LinearLayout) inflater.inflate(R.layout.calendar_line, null);
                calendar_day[j][0] = (calendarCell) calendar_week[j].findViewById(R.id.calendar_line1);
                calendar_day[j][1] = (calendarCell) calendar_week[j].findViewById(R.id.calendar_line2);
                calendar_day[j][2] = (calendarCell) calendar_week[j].findViewById(R.id.calendar_line3);
                calendar_day[j][3] = (calendarCell) calendar_week[j].findViewById(R.id.calendar_line4);
                calendar_day[j][4] = (calendarCell) calendar_week[j].findViewById(R.id.calendar_line5);
                calendar_day[j][5] = (calendarCell) calendar_week[j].findViewById(R.id.calendar_line6);
                calendar_day[j][6] = (calendarCell) calendar_week[j].findViewById(R.id.calendar_line7);

                calendar_calendar.addView(calendar_week[j], params);

                for (int k = dayOfWeek-1; k < 7; k++) {
                    if (i <= totalDays) {
                        if(i==today.get(Calendar.DATE)&&month==today.get(Calendar.MONTH)&&year==today.get(Calendar.YEAR)){
                            calendar_day[j][k].setBackgroundColor(Color.CYAN);
                            todayCell=new int[]{j,k};
                        }
                        calendar_day[j][k].setText(Integer.toString(i++));
                        calendar_day[j][k].setCellNumber(j,k);
                        calendar_day[j][k].setOnClickListener(this);
                    }
                }
                dayOfWeek = 1;
            }else{
                break;
            }
        }

        return calendar;
    }

    public int[] getSelectedDate(){
        return new int[]{selectedYear,selectedMonth,selectedDay};
    }


    @Override
    public void onClick(View v) {
        try{
            calendar_day[selectedCell[0]][selectedCell[1]].setBackgroundColor(Color.WHITE);
            if(calendar.get(Calendar.YEAR)==today.get(Calendar.YEAR)&&calendar.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
                calendar_day[todayCell[0]][todayCell[1]].setBackgroundColor(Color.CYAN);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.v("Log","calendar cell color change failed!");
        }
        calendarCell cell=(calendarCell)v;
        selectedYear=calendar.get(Calendar.YEAR);
        selectedMonth=calendar.get(Calendar.MONTH)+1;
        selectedDay= Integer.parseInt(cell.getText().toString());
        selectedCell=cell.cellNumber;
        v.setBackgroundColor(Color.MAGENTA);
    }
}
