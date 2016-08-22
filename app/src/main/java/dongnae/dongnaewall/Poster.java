package dongnae.dongnaewall;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.Serializable;

import pre_Poster.Pre_Poster;

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

    public Poster(Pre_Poster pre_poster){
        this.id=pre_poster.getId();
        this.title = pre_poster.getTitle();
        this.endDate = pre_poster.getEndDate();
        this.startDate = pre_poster.getStartDate();
        this.price =pre_poster.getPrice();
        this.place = pre_poster.getPlace();
        this.main_picture = pre_poster.getMain_picture();
        this.category = pre_poster.getCategory();
        this.like = pre_poster.getLike();
        this.view=pre_poster.getView();
        main_picture_loaded = Picasso.with(context).load(main_picture);
        setPlaceHolderForRecommendationPosters();
        //setHeightAndParams();
    }

    private void setPlaceHolderForRecommendationPosters(){
        if(TempData.status==TempData.STATUS_RECOMMENDATION){
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
    public void mergeAdditionalInfo(additionalPosterInfo additional){
        this.full_information = additional.full_information;
        this.pictures = additional.pictures;
        this.homepage = additional.homepage;
        this.phone = additional.phone;
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


class additionalPosterInfo{
    String[] pictures;
    String homepage;
    int phone;
    String full_information;

    public additionalPosterInfo(String[] pictures, String homepage, int phone, String full_information) {
        this.pictures = pictures;
        this.homepage = homepage;
        this.phone = phone;
        this.full_information = full_information;
    }
}