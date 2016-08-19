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


    final static String server_ip="192.168.43.112";
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
                    Log.v("Log","LOGGING "+Integer.toString(i)+"th poster");
                    convertedPosters.add(returningPosters[i]);
                }
            }else{
                Log.v("Log","LOGGING2");
            }

            socket.close();
        }catch (Exception e){
            Log.e("Log","ERROR at getPoster");
            e.printStackTrace();
        }


        return convertedPosters;
    }

    private void sendCurrentStatus(){
        Object[] pack=new Object[4];
        pack[0]=status;
        pack[1]= TempData.order;
        if(TempData.search==null|| TempData.search.equals("")){
            pack[2]="`!`";
        }else{
            pack[2]= TempData.search;
        }
        pack[3]= TempData.startNum;
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
        Object[] pack=new Object[4];
        pack[0]=id;
        pack[1]=null;
        pack[2]=null;
        pack[3]=null;
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
