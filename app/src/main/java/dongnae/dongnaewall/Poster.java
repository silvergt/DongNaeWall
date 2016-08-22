package dongnae.dongnaewall;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.Serializable;
import java.util.HashMap;

public class Poster implements Serializable{
    static Context context;

    int id;
    String title;
    int[] startDate;
    int[] endDate;
    int price;
    int place;
    String main_picture;
    RequestCreator main_picture_loaded;
    int[] category;
    int like;
    int view;


    String[] pictures;
    String homepage;
    int phone;
    String full_information;



    float height=0;
    RelativeLayout.LayoutParams params;
    static Bitmap bitmap;

    //**ABBREVIATED
    public Poster(int id, String title, int[] startDate, int[] endDate, int price,
                  int place, String main_picture, int[] category, int like,int view) {
        this.id=id;
        this.title = title;
        this.endDate = endDate;
        this.startDate = startDate;
        this.price = price;
        this.place = place;
        this.main_picture = main_picture;
        this.category = category;
        this.like = like;
        this.view=view;
        main_picture_loaded= Picasso.with(context).load(main_picture);
        setPlaceHolderForRecommendationPosters();
        //setHeightAndParams();
    }

    public Poster(HashMap<String,Object> map){
        this.id=(int)map.get("id");
        this.title = map.get("title").toString();
        this.endDate = (int[])map.get("endDate");
        this.startDate = (int[])map.get("startDate");
        this.price = (int)map.get("price");
        this.place =(int)map.get("place");
        this.main_picture = map.get("main_picture").toString();
        this.category = (int[])map.get("category");
        this.like = (int)map.get("like");
        this.view=(int)map.get("view");
        main_picture_loaded= Picasso.with(context).load(main_picture);
        setPlaceHolderForRecommendationPosters();
    }

    private void setPlaceHolderForRecommendationPosters(){
        if(TempData.getStatus()==TempData.STATUS_RECOMMENDATION){
            //main_picture_loaded.placeholder(R.drawable.logo);
        }
    }

    private void setHeightAndParams(){
        if(this.height==0) {
            try {
                /*
                if(bitmap!=null){
                    bitmap.recycle();
                }
                */
                bitmap = main_picture_loaded.get();
                float Temp1 = bitmap.getHeight();
                float Temp2 = bitmap.getWidth();
                float WHratio = Temp1 / Temp2;
                height = WHratio * main.displayWidth;
                params=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int)height);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    //**FULL
    public void mergeAdditionalInfo(HashMap<String,Object> additional){
        this.full_information = additional.get("full_information").toString();
        this.pictures = (String[])additional.get("pictures");
        this.homepage = additional.get("homepage").toString();
        this.phone = (int)additional.get("phone");
    }

    public static Context getContext() {
        return context;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int[] getStartDate() {
        return startDate;
    }

    public int[] getEndDate() {
        return endDate;
    }

    public int getPrice() {
        return price;
    }

    public int getPlace() {
        return place;
    }

    public String getMain_picture() {
        return main_picture;
    }

    public RequestCreator getMain_picture_loaded() {
        return main_picture_loaded;
    }

    public int[] getCategory() {
        return category;
    }

    public int getLike() {
        return like;
    }

    public int getView() {
        return view;
    }

    public String[] getPictures() {
        return pictures;
    }

    public String getHomepage() {
        return homepage;
    }

    public int getPhone() {
        return phone;
    }

    public String getFull_information() {
        return full_information;
    }
}
