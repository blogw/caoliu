package com.github.blogw.caoliu;

import java.io.*;
import java.math.BigDecimal;

/**
 * Created by blogw on 2015/09/24.
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // system encoding
        String encode=System.getProperty("file.encoding");

        String source = "C:\\Users\\blogw\\Desktop\\ttss.mp4";
        String target="C:\\Users\\blogw\\Desktop\\tt.mp4";

        File f1=new File(source);
        File f2=new File(target);

        if(f2.exists()){
            long len=f2.length();

            InputStream is = new FileInputStream(f1);
            FileOutputStream baos = new FileOutputStream(f2,true);
            is.skip(len);

            byte[] buffer = new byte[1024];
            int read = 0;
            double total=f1.length();
            double readed=len;

            while ((read = is.read(buffer, 0, buffer.length)) != -1) {
                baos.write(buffer, 0, read);
                readed+=read;
                System.out.println(format(readed/total));
            }
            baos.flush();
            baos.close();
            is.close();
        }

    }

    public static String format(double ft){
        BigDecimal bd = new BigDecimal(ft * 100);
        bd = bd.setScale(2, 4);
        float f=bd.floatValue();
        String result=Float.toString(f);

        if(f<10){
            result=" "+f;
        }
        if(result.length()<5){
            result+="0";
        }
        return result+"%";
    }
}
