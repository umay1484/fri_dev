package test;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class HttpRequest {
    public static final boolean DEBUG = false;
    public static final Integer CONST_INT_ERRCODE = 500;
    public static final String CONST_STR_CANCELCODE = "50";
    public static final String CONST_STR_CONTENT_TYPE = "Content-Type";
    public static final String CONST_STR_CHAR_SET = "utf-8";//StandardCharsets.UTF_8
    public static final String CONST_STR_APP_JSON = "application/json";
    public static final String CONST_STR_APP_ZIP = "application/zip";
    public static final String CONST_STR_EMPTY = "";
    public static final String CONST_STR_SPACE = " ";
    public static final String CONST_STR_SEMICOLON = ";";
    public static final String CONST_STR_CHARSETEQ = "charset=";
    public static final String CONST_STR_COLON = ":";
    public static final String CONST_STR_POST = "POST";
    public static final String CONST_STR_PUT = "PUT";
    public static final String CONST_STR_GET = "GET";
    public static final String CONST_STR_RESULT = "result";
    public static final String CONST_STR_STATCODE = "statCode";
    public static final String CONST_STR_STATMSG = "infoMsg";
    public static final String CONST_STR_KAIGYO = "\r\n";
    public static final String CONST_STR_URI_ROOT = "http://eln.ie-lab.io:8080/api/"; // 開発用
    public static final String CONST_STR_URI_IIIAW = "InspectionItemInputAssistantWindows";
    
    public HttpRequest() {
    }
    
    //    public static void main(String args[]) {
    //        //new ExAPI()._saveFile(null, null, null);
    //        openIIIDialog(0);
    //        //new ExAPI().cmdExec("C:\\BarcodePrinter\\BarcodePrinter.exe");
    //    }
    public static ArrayList<HashMap<String, String>> openIIIDialog(int cCode) {
        return new ExAPI().openIIIDialog(cCode, downloadZip(CONST_STR_URI_IIIAW, CONST_STR_GET, CONST_STR_EMPTY, null));
    }
    
    public static InputStream downloadZip(String uri, String meth, String param, String content) {
        InputStream reader = null;
        try {
            URL url = new URL(CONST_STR_URI_ROOT + uri);
            reader = url.openStream();
            /*
            uc                              =   (HttpURLConnection)url.openConnection();
            uc.setUseCaches(false);
            uc.setDoOutput(CONST_STR_POST.equals(meth.toUpperCase()) || CONST_STR_PUT.equals(meth.toUpperCase()));
            uc.setRequestMethod(meth);
            uc.setInstanceFollowRedirects(false);
            if (uc.getDoOutput()) {
                uc.setRequestProperty(CONST_STR_CONTENT_TYPE, content == null ? CONST_STR_APP_ZIP + CONST_STR_SEMICOLON + CONST_STR_CHARSETEQ + CONST_STR_CHAR_SET : content);
                PrintStream ps    =    new PrintStream(uc.getOutputStream(), true, CONST_STR_CHAR_SET);
                ps.print(param);
                ps.close();
            }
            uc.connect();
            reader    =    uc.getInputStream();
            uc.disconnect();
            */
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return reader;
    }
}