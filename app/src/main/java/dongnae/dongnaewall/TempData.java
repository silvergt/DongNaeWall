package dongnae.dongnaewall;

public class TempData {
    /*

    ********* themeIsChecked
                        0           1           2           3           4           5
    1   학교행사 :		    교내행사		학회,동아리
    2   전시, 공연:	    전시,미술		뮤지컬		연극		    아동,가족		콘서트,공연	축제
    3   설명회, 공모전:	    기업설명회		공모전		기업행사	    학생 설명회
    4   구인구직:		    아르바이트		공채, 특채

    ********* regionIsChecked

                    0    1    2    3    4   5    6    7    8    9   10   11   12   13   14   15   16
    지역: 		    서울, 경기, 인천, 부산, 대구, 울산, 대전, 광주, 경남, 경북, 전남, 전북, 충남, 충북, 강원, 세종, 제주
    */

    final static int STATUS_FULL = -100;
    final static int STATUS_ABBREVIATED = -200;

    final static int ORDER_DATE=110;
    final static int ORDER_LIKE=120;
    final static int ORDER_VISIT=130;

    final static int CATEGORY_THEME_SCHOOL=1000;
    final static int CATEGORY_THEME_EXHIBIT=1001;
    final static int CATEGORY_THEME_CONTEST=1002;
    final static int CATEGORY_THEME_HIRING=1003;
    final static int CATEGORY_REGION=2000;

    static boolean[][] themeIsChecked=new boolean[4][6];
    static boolean[] regionIsChecked=new boolean[17];

    static int order;
    static String search;
    static int startNum;

    public static void initialization(){
        for(int i=0;i<themeIsChecked.length;i++){
            for(int j=0;j<themeIsChecked[0].length;j++){
                themeIsChecked[i][j]=false;
            }
        }
        for(int i=0;i<regionIsChecked.length;i++){
            regionIsChecked[i]=false;
        }
        order=ORDER_DATE;
        search="";
        startNum=0;
    }

    public void changeFilterCheckedEntity(int category,int location,boolean isChecked){
        try {
            if (category == CATEGORY_THEME_SCHOOL) themeIsChecked[0][location]=isChecked;
            else if (category == CATEGORY_THEME_EXHIBIT) themeIsChecked[1][location]=isChecked;
            else if (category == CATEGORY_THEME_CONTEST) themeIsChecked[2][location]=isChecked;
            else if (category == CATEGORY_THEME_HIRING) themeIsChecked[3][location]=isChecked;
            else if (category == CATEGORY_REGION) regionIsChecked[location]=isChecked;


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void changeAllEntity(int order,String search,int startNum){
        TempData.order=order;
        TempData.search=search;
        TempData.startNum=startNum;
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

    
}
