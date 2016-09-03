package dongnae.dongnaewall;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class CalendarCustomView extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private int selectedYear;
    private int selectedMonth;
    private int selectedDay;

    boolean isRangedCalendar=false;

    private int[] startDate;
    private int[] endDate;

    private int selectedCell[];
    private int todayCell[];

    private LinearLayout calendar_main;

    private TextView leftButton;
    private TextView yearAndMonth;
    private TextView rightButton;

    private LinearLayout[] calendar_week;
    private calendarCell[][] calendar_day;

    private LinearLayout calendar_calendar;
    private GregorianCalendar calendar;
    private Calendar today;

    private LayoutInflater inflater;

    public CalendarCustomView(Context context) {
        super(context);
        this.context=context;
    }

    public CalendarCustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }

    public void initializeCalendar(int selectedYear,int selectedMonth,int selectedDay){
        this.selectedYear=selectedYear;
        this.selectedMonth=selectedMonth;
        this.selectedDay=selectedDay;
        isRangedCalendar=false;
        onCreate();
    }

    public void initializeCalendar(int[] startDate,int[] endDate){
        this.startDate=startDate;
        this.endDate=endDate;
        isRangedCalendar=true;
        onCreate();
    }


    private void onCreate(){
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        calendar_main=(LinearLayout)inflater.inflate(R.layout.calendar_base,null);
        LinearLayout.LayoutParams mainParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(calendar_main,mainParams);

        calendar_calendar=(LinearLayout)calendar_main.findViewById(R.id.basecalendar_calendar);
        leftButton=(TextView)calendar_main.findViewById(R.id.basecalendar_left);
        yearAndMonth=(TextView)calendar_main.findViewById(R.id.basecalendar_yymm);
        rightButton=(TextView)calendar_main.findViewById(R.id.basecalendar_right);

        today=Calendar.getInstance();
        calendar = new GregorianCalendar(today.get(Calendar.YEAR), today.get(Calendar.MONTH), 1);

        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) - 1,startDate,endDate);
            }
        });

        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,startDate,endDate);
            }
        });

        if (selectedYear > 0 && selectedMonth > 0) {
            createCalendar(selectedYear, selectedMonth - 1,startDate,endDate);
        } else {
            createCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),startDate,endDate);
        }

    }

    private Calendar createCalendar(int year, int month, int[] startDate,int[] endDate){
        Log.v("Log","creating new calendar");
        if(month>=12){year++;month=0;}
        if(month<0){year--;month=11;}

        calendar.set(year,month,1);
        String YYMM;
        if(month+1<10) {
            YYMM=Integer.toString(year) + ".0" + Integer.toString(month+1);
        }else{
            YYMM=Integer.toString(year) + "." + Integer.toString(month+1);
        }
        yearAndMonth.setText(YYMM);
        //Log.v("Log",Integer.toString(year)+"-"+Integer.toString(month+1)+"-"+Integer.toString(1));
        //Log.v("Log",calendar.getTime().toString());

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
        //Log.v("Log","number of weeks : "+Integer.toString(numberOfWeeks));
        //Log.v("Log","total day : "+Integer.toString(totalDays));

        calendar_week=new LinearLayout[numberOfWeeks];
        calendar_day=new calendarCell[numberOfWeeks][7];


        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,0);
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
                        calendar_day[j][k].setText(Integer.toString(i));
                        calendar_day[j][k].setCellNumber(j,k);
                        calendar_day[j][k].setOnClickListener(this);
                        if(!isRangedCalendar) {
                            if (year == selectedYear && month + 1 == selectedMonth && i == selectedDay) {
                                //calendar_day[j][k].setBackgroundColor(Color.MAGENTA);
                                selectCell(j, k);
                            }
                        }else if(isRangedCalendar&&startDate!=null){
                            if(endDate!=null) {
                                if (TempData.isBetweenDate(new int[]{year, month+1, i}, startDate, endDate)) {
                                    //Log.v("Log","is between!!");
                                    selectCell(j, k);
                                }
                            }else if(endDate==null&&year==startDate[0]&&month+1==startDate[1]&&i==startDate[2]){
                                //Log.v("Log","is NOT between!!");
                                selectCell(j, k);
                            }
                        }
                        i++;
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
        calendarCell cell = (calendarCell) v;
        if(isRangedCalendar) {
            if(startDate==null||endDate!=null){
                selectCellOfSchedule(cell.cellNumber[0], cell.cellNumber[1],true);
            }else{
                selectCellOfSchedule(cell.cellNumber[0], cell.cellNumber[1],false);
            }
        }else{
            selectCell(cell.cellNumber[0], cell.cellNumber[1]);
        }
    }

    public void selectCellOfSchedule(int column,int row,boolean clickedStartDate){
        /**MODIFY HERE
         * call this method if date is between startDate[] and endDate[]
         * **/
        if(clickedStartDate) {
            startDate=new int[3];
            endDate=null;
            startDate[0] = calendar.get(Calendar.YEAR);
            startDate[1] = calendar.get(Calendar.MONTH) + 1;
            startDate[2] = Integer.parseInt(calendar_day[column][row].getText().toString());
            createCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), startDate, endDate);
        }else{
            endDate=new int[3];
            endDate[0] = calendar.get(Calendar.YEAR);
            endDate[1] = calendar.get(Calendar.MONTH) + 1;
            endDate[2] = Integer.parseInt(calendar_day[column][row].getText().toString());
            if(TempData.isBetweenDate(endDate,new int[]{0,0,0},startDate)) {
                int[] temp=endDate;
                endDate=startDate;
                startDate=temp;
                createCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), startDate, endDate);
            }else{
                createCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), startDate, endDate);
            }
        }
    }

    public void selectCell(int column,int row){
        if(isRangedCalendar) {
            //Log.v("Log","coloring...");
            calendar_day[column][row].setBackgroundColor(Color.MAGENTA);
        }else{
            try{
                calendar_day[selectedCell[0]][selectedCell[1]].setBackgroundColor(Color.WHITE);
                if(calendar.get(Calendar.YEAR)==today.get(Calendar.YEAR)&&calendar.get(Calendar.MONTH)==today.get(Calendar.MONTH)){
                    calendar_day[todayCell[0]][todayCell[1]].setBackgroundColor(Color.CYAN);        //TODAY CELL
                }
            }catch (Exception e){
                e.printStackTrace();
                Log.v("Log","selected cell color failed to be changed!");
            }
            selectedYear = calendar.get(Calendar.YEAR);
            selectedMonth = calendar.get(Calendar.MONTH) + 1;
            selectedDay = Integer.parseInt(calendar_day[column][row].getText().toString());
            selectedCell = calendar_day[column][row].cellNumber;
            calendar_day[column][row].setBackgroundColor(Color.MAGENTA);                            //CLICKED CELL
        }
    }
}
