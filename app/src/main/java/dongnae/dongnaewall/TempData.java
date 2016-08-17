package dongnae.dongnaewall;

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

    final static int STATUS_FULL = -100;
    final static int STATUS_ABBREVIATED = -200;

    final static int ORDER_DATE=110;
    final static int ORDER_LIKE=120;
    final static int ORDER_VISIT=130;

    static boolean schoolIsChecked=false;
    static boolean inschool_activitiesIsChecked=false;
    static boolean inschool_forumsIsChecked=false;
    static boolean inschool_circlesIsChecked=false;

    static boolean exhibitionIsChecked=false;
    static boolean exhibition_artIsChecked=false;
    static boolean exhibition_sculptureIsChecked=false;

    static boolean performanceIsChecked=false;
    static boolean performance_musicalIsChecked=false;
    static boolean performance_playIsChecked=false;
    static boolean performance_familyIsChecked=false;
    static boolean performance_concertIsChecked=false;

    static boolean presentationIsChecked=false;
    static boolean presentation_companyIsChecked=false;
    static boolean presentation_contestIsChecked=false;
    static boolean presentation_corporateIsChecked=false;
    static boolean presentation_studentIsChecked=false;

    static boolean jobIsChecked=false;
    static boolean job_part_timeIsChecked=false;
    static boolean job_wantedIsChecked=false;

    static int[] dateFrom;
    static int[] dateTo;

    static int costFrom;
    static int costTo;

    static int order;
    static String search;
    static int startNum;



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
