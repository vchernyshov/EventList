package ua.insomnia.eventlist.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;

public class Utils {

	public static String convertStreamToString(InputStream is) throws IOException {
        InputStreamReader r = new InputStreamReader(is);
        StringWriter sw = new StringWriter();
        char[] buffer = new char[1024];
        try {
            for (int n; (n = r.read(buffer)) != -1;)
                sw.write(buffer, 0, n);
        }
        finally{
            try {
                is.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return sw.toString();
    }
    
    public static void closeStream(Object oin) {
        if(oin!=null)
            try {
                if(oin instanceof InputStream)
                    ((InputStream)oin).close();
                if(oin instanceof OutputStream)
                    ((OutputStream)oin).close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

}
