package dongnae.dongnaewall;

import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import pre_Poster.Pre_Poster;

/**
 * sendCurrentStatus and sendRequestOfAdditionalPosterInfo both send array with length of 4.
 * if called sendCurrentStatus, Server needs all four information,
 * but if called sendRequestOfAdditionalPosterInfo, Server only needs first information.
 * this code was written to prevent error called by difference of int and int[].
 *
 * the information is consists of [status, order, search ,startNum] for Poster[] request,
 * and [id, null, null, null] for additionalPosterInfo request
 *
 * status   : (int) determine if client needs abbreviated info or full info
 * order    : (int) align contents by order - date, like number, etc
 * search   : (String) contains text that client wants to search
 * startNum : (int) request posters from index [startNum]
 *
 * id       : (int) assigned to every posters to retrieve poster information faster and precisely
 */
public class ServerConnector {


    final static String server_ip="203.170.122.31";
    final static int server_port=9491;

    Socket socket;
    Poster[] returningPosters=null;
    ArrayList<Poster> convertedPosters=null;
    int status;

    additionalPosterInfo additionalInfo=null;
    int id;

    OutputStream OS=null;
    ObjectOutputStream OOS=null;
    InputStream IS=null;
    ObjectInputStream OIS=null;

    public ArrayList<Poster> getPosterFromServer(int status){
        this.status=status;
        convertedPosters=new ArrayList<>();

        try{
            socket=new Socket(ServerConnector.server_ip,ServerConnector.server_port);

            sendCurrentStatus();

            returningPosters=getPosterFromServer();
            if(returningPosters!=null) {
                for (int i = 0; i < returningPosters.length; i++) {
                    Log.v("Log","logging "+Integer.toString(TempData.startNum+i)+"th poster");
                    convertedPosters.add(returningPosters[i]);
                }
            }else{
                Log.e("Log","returning poster is null!");
            }

            socket.close();
        }catch (Exception e){
            Log.e("Log","ERROR at getPoster");
            e.printStackTrace();
        }


        return convertedPosters;
    }

    private void sendCurrentStatus(){
        /** pack
         *  1. status                           -int
         *  2. Tempdata.order                   -int
         *  3. TempData.search                  -String
         *  4. TempData.startNum                -int
         *  5. filter.getFilterCheckedData()    -boolean[]
         */
        ArrayList<Object> pack=new ArrayList<>();
        pack.add(status);
        pack.add(TempData.order);
        if(TempData.search==null|| TempData.search.equals("")){
            pack.add("`!`");
        }else{
            pack.add(TempData.search);
        }
        pack.add(TempData.startNum);
        pack.add(filter.getFilterCheckedData());
        try {
            OS = socket.getOutputStream();
            OOS=new ObjectOutputStream(OS);
            OOS.writeObject(pack);
        } catch (Exception e) {
            Log.e("Log","ERROR at ServerConnector.sendPosterStatus");
            e.printStackTrace();
        }

    }

    private Poster[] getPosterFromServer(){
        Pre_Poster[] posters=null;
        Poster[] returningPosterArray=null;
        try{
            IS=socket.getInputStream();
            OIS=new ObjectInputStream(IS);
            posters=(Pre_Poster[])OIS.readObject();
            returningPosterArray=new Poster[posters.length];
            for(int i=0;i<returningPosterArray.length;i++){
                returningPosterArray[i]=new Poster(posters[i]);
            }
        }catch (Exception e){
            Log.v("Log","exception in getPrePosterFromServer");
            e.printStackTrace();
        }finally {
            try{
                IS.close();
                OIS.close();
                OS.close();
                OOS.close();
            }catch (Exception e2){
                Log.e("Log","ERROR at ServerConnector.getPrePosterFromServer closing");
                e2.printStackTrace();
            }
        }
        return returningPosterArray;
    }


    public additionalPosterInfo getAdditionalPosterInfo(int id){
        this.id=id;
        try{
            socket=new Socket(ServerConnector.server_ip,ServerConnector.server_port);

            sendRequestOfAdditionalPosterInfo();

            additionalInfo=getAdditionalPosterInfoFromServer();

            socket.close();
        }catch (Exception e){
            Log.e("Log","ERROR at getAdditionalPosterInfo");
            e.printStackTrace();
        }

        return additionalInfo;
    }

    private void sendRequestOfAdditionalPosterInfo(){
        OutputStream OS=null;
        ObjectOutputStream OOS=null;
        ArrayList<Object> pack=new ArrayList<>();
        pack.add(id);
        try {
            OS = socket.getOutputStream();
            OOS=new ObjectOutputStream(OS);
            OOS.writeObject(pack);
        } catch (Exception e) {
            Log.e("Log","ERROR at ServerConnector.sendRequestOfAdditionalPosterInfo");
            e.printStackTrace();
        }finally {
            try{
                OS.close();
                OOS.close();
            }catch (Exception e2){
                Log.e("Log","ERROR at ServerConnector.sendRequestOfAdditionalPosterInfo closing");
                e2.printStackTrace();
            }
        }
    }

    private additionalPosterInfo getAdditionalPosterInfoFromServer(){
        additionalPosterInfo additional=null;
        try{
            InputStream IS=socket.getInputStream();
            ObjectInputStream OIS=new ObjectInputStream(IS);
            additional=(additionalPosterInfo)OIS.readObject();
        }catch (Exception e){
            Log.e("Log","ERROR at getAdditionalPosterInfoFromServer");
        }
        return additional;
    }
}
