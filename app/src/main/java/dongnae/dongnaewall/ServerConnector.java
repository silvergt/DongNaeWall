package dongnae.dongnaewall;

import android.util.Log;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

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
    ArrayList<Poster> returningPosters=null;

    int id;

    OutputStream OS=null;
    ObjectOutputStream OOS=null;
    InputStream IS=null;
    ObjectInputStream OIS=null;

    public ArrayList<Poster> getPoster(){

        try{
            socket=new Socket(ServerConnector.server_ip,ServerConnector.server_port);

            sendCurrentStatus();
            returningPosters=getPosterFromServer();
            if(returningPosters==null) {
                Log.e("Log","returning poster is null!");
            }else if(returningPosters.size()==0){
                Log.v("Log","returning poster size is 0");
            }

            socket.close();
        }catch (Exception e){
            Log.e("Log","ERROR at getPoster");
            returningPosters=null;
            e.printStackTrace();
        }


        return returningPosters;
    }

    private void sendCurrentStatus(){
        /** pack
         *  1. Tempdata.status                  -int
         *  2. Tempdata.order                   -int
         *  3. TempData.search                  -String
         *  4. TempData.startNum                -int
         *  5. filter.getFilterCheckedData()    -boolean[]
         *  6. filter.getDayOfWeekCheckedData() -boolean[]
         */
        ArrayList<Object> pack=new ArrayList<>();
        pack.add(TempData.getStatus());
        pack.add(TempData.getOrder());
        if(TempData.getSearch()==null|| TempData.getSearch().equals("")){
            pack.add("`!`null");
        }else{
            pack.add(TempData.getSearch());
        }
        pack.add(TempData.getStartNum());
        pack.add(filter.getFilterCheckedData());
        pack.add(filter.getDayOfWeekCheckedData());
        try {
            OS = socket.getOutputStream();
            OOS=new ObjectOutputStream(OS);
            OOS.writeObject(pack);
        } catch (Exception e) {
            Log.e("Log","ERROR at ServerConnector.sendPosterStatus");
            e.printStackTrace();
        }

    }

    private ArrayList<Poster> getPosterFromServer(){
        ArrayList<HashMap<String,Object>> posters=null;
        ArrayList<Poster> returningPosterArray=null;
        try{
            IS=socket.getInputStream();
            OIS=new ObjectInputStream(IS);
            posters=(ArrayList<HashMap<String,Object>>)OIS.readObject();
            returningPosterArray=new ArrayList<>();
            for(int i=0;i<posters.size();i++){
                returningPosterArray.add(new Poster(posters.get(i)));
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


    public ArrayList<HashMap<String,Object>> getAdditionalPosterInfo(int id){
        this.id=id;
        ArrayList<HashMap<String,Object>> additionalInfo=null;
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

    private ArrayList<HashMap<String,Object>> getAdditionalPosterInfoFromServer(){
        ArrayList<HashMap<String,Object>> additional=null;
        try{
            InputStream IS=socket.getInputStream();
            ObjectInputStream OIS=new ObjectInputStream(IS);
            additional=(ArrayList<HashMap<String,Object>>)OIS.readObject();
        }catch (Exception e){
            Log.e("Log","ERROR at getAdditionalPosterInfoFromServer");
        }
        return additional;
    }

}
