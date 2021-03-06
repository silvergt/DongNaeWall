package dongnae.dongnaewall;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TempData {
    /*

    ********* themeIsChecked
                        0           1           2           3           4           5
    1   학교행사 :		    교내행사		학회행사      동아리
    2
    3   전시, 공연:	    전시,미술		뮤지컬		연극		    아동,가족		콘서트,공연	축제
    4   설명회, 공모전:	    기업설명회		공모전		기업행사	    학생 설명회
    5   구인구직:		    아르바이트		공채, 특채

    ********* regionIsChecked

                    0    1    2    3    4   5    6    7    8    9   10   11   12   13   14   15   16
    지역: 		    서울, 경기, 인천, 부산, 대구, 울산, 대전, 광주, 경남, 경북, 전남, 전북, 충남, 충북, 강원, 세종, 제주
    */


    final static int STATUS_RECOMMENDATION=-100;
    final static int STATUS_POSTER_FULL = -200;
    final static int STATUS_POSTER_ABBREVIATED = -300;

    final static int CLIENT_CLICKED_LIKEBUTTON=-400;


    final static int ORDER_DATE=110;
    final static int ORDER_LIKE=120;
    final static int ORDER_VISIT=130;

    private static int status;
    private static int order;
    private static String search;
    private static int startNum;

    private static String savedID="NULL";
    private static String savedPassword="NULL";


    public static void saveLoginInfo(String ID,String password){
        savedID=ID; savedPassword=password;
        try {
            FileOutputStream FOS = main.context.openFileOutput("ID", Context.MODE_PRIVATE);
            ObjectOutputStream OOS =new ObjectOutputStream(FOS);
            OOS.writeObject(ID);
            OOS.close();
            FOS.close();
        }catch (Exception e){
            Log.e("Log","ID writing failed!!");
        }
        try{
            FileOutputStream FOS2=main.context.openFileOutput("PASSWORD",Context.MODE_PRIVATE);
            ObjectOutputStream OOS2 =new ObjectOutputStream(FOS2);
            OOS2.writeObject(password);
            OOS2.close();
            FOS2.close();
        }catch (Exception e){
            Log.e("Log","PASSWORD writing failed!!");
        }
    }

    public static String getSavedID(){
        if(!savedID.equals("NULL")){
            return savedID;
        }else{
            try {
                FileInputStream FIS = main.context.openFileInput("ID");
                ObjectInputStream OIS=new ObjectInputStream(FIS);
                OIS.close();
                FIS.close();
                savedID=OIS.readObject().toString();
            }catch (Exception e){
                Log.e("Log","retrieving ID information failed!");
            }
        }
        return savedID;
    }

    public static String getSavedPassword(){
        if(!savedPassword.equals("NULL")){
            return savedPassword;
        }else{
            try {
                FileInputStream FIS = main.context.openFileInput("PASSWORD");
                ObjectInputStream OIS=new ObjectInputStream(FIS);
                savedPassword=OIS.readObject().toString();
                OIS.close();
                FIS.close();
            }catch (Exception e){
                Log.e("Log","retrieving PASSWORD information failed!");
            }
        }
        return savedPassword;
    }

    public static void changeStatus(int status){
        TempData.status=status;
        switch (status){
            case TempData.STATUS_POSTER_ABBREVIATED:
            case TempData.STATUS_RECOMMENDATION:
                main.setHeaderFooterViewVisibility();
                break;
        }
    }

    public static void changeOrder(int order){
        TempData.order=order;
    }

    public static void changeSearch(String search){
        TempData.search=search;
    }

    public static void changeStartNum(int startNum){
        TempData.startNum=startNum;
    }

    public static int getStatus() {
        return status;
    }

    public static int getOrder() {
        return order;
    }

    public static String getSearch() {
        return search;
    }

    public static int getStartNum() {
        return startNum;
    }


    public static boolean isBetweenDate(int[] selected, int[] start, int[] end){
        if(selected[0]<start[0]||selected[0]>end[0]){
            return false;
        }else if(selected[0]>=start[0]&&selected[0]<=end[0]){   //year bound
            if(selected[0]==start[0]&&selected[0]<end[0]){      //same with start year
                if(selected[1]>start[1]){
                    return true;
                }else if(selected[1]==start[1]){    //same month
                    if(selected[2]>=start[2]){
                        return true;
                    }else{
                        return false;
                    }
                }else if(selected[1]<start[1]){
                    return false;
                }
            }else if(selected[0]>start[0]&&selected[0]<end[0]){ //between start & end year
                return true;
            }else if(selected[0]==end[0]&&selected[0]>start[0]){  //same with end year
                if(selected[1]<end[1]){
                    return true;
                }else if(selected[1]>end[1]) {
                    return false;
                }else if(selected[1]==end[1]){
                    if(selected[2]<=end[2]){
                        return true;
                    }else{
                        return false;
                    }
                }
            }else if(selected[0]==end[0]&&selected[0]==start[0]){
                if(selected[1]==start[1]&&selected[1]<end[1]){
                    if(selected[2]>=start[2]){
                        return true;
                    }else{
                        return false;
                    }
                }else if(selected[1]>start[1]&&selected[1]==end[1]){
                    if(selected[2]<=end[2]){
                        return true;
                    }else{
                        return false;
                    }
                }else if(selected[1]==start[1]&&selected[1]==end[1]){
                    if(selected[2]>=start[2]&&selected[2]<=end[2]){
                        return true;
                    }else
                        return false;
                }else if(selected[1]>start[1]&&selected[1]<end[1]){
                    return true;
                }else if(selected[1]<start[1]||selected[1]>end[1]){
                    return false;
                }
            }



        }

        Log.e("Log","missing part");
        return false;
    }

}
