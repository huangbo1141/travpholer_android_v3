package com.denada.travpholer.util.http;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * Created by Hgc on 4/5/2015.
 */
public class UploadTask extends AsyncTask<String,Void,Boolean> {
    Context mContext;
    //List<UserObject> mApps;
    String mType;
    String mCaption;

    public static final int LOADER_RECENT   =   0;
    //String myurl	=		CGlobals.urlupload;

    Integer pagesize = 6;
    private Integer mTypeInteger,benId;
    private Handler mHandler;
    String mParams;
    public UploadTask(Context context, String params, Handler handler) {

        mContext    =   context;
        mHandler    =   handler;
        mParams     =   params;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean flag	=	false;


        /*HttpURLConnection conn    = null;

        OutputStream os   = null;
        InputStream is   = null;
        ByteArrayOutputStream baos = null;
        try{
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary =  "*****";
            FileInputStream fileInputStream = new FileInputStream(new File(upload_file_name) );

            URL url	=	new URL(myurl);
            conn = (HttpURLConnection)url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type","multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"image_request[image]\";filename=\"" + upload_file_name + "\"");
            dos.writeBytes(lineEnd);
            dos.writeBytes();
            os = conn.getOutputStream();
            os.write(mParams.getBytes());
            os.flush();

            String response;

            int responseCode = conn.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {

                is = conn.getInputStream();
                baos = new ByteArrayOutputStream();
                byte[] byteBuffer = new byte[1024];
                byte[] byteData = null;
                int nLength = 0;
                while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
                    baos.write(byteBuffer, 0, nLength);
                }
                byteData = baos.toByteArray();

                response = new String(byteData);

                JSONObject responseJSON = new JSONObject(response);
                Integer result = (Integer) responseJSON.get("result");
                if(result == 1)
                {
                    flag	=	true;
                }

            }

        }catch(Exception ex){

        }*/

        return flag;
    }
    @Override
    protected void onPostExecute(Boolean result) {
        if(result){
            //mHandler.sendEmptyMessage(CGlobals.MESSAGE_UPLOADSUCCESS);
        }
        else
        {
            //mHandler.sendEmptyMessage(CGlobals.MESSAGE_UPLOADFAIL);
        }
    }
}