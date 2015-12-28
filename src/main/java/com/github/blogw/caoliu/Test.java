package com.github.blogw.caoliu;

import hu.ssh.progressbar.ProgressBar;
import hu.ssh.progressbar.console.ConsoleProgressBar;
import org.apache.commons.io.FileUtils;

import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.awt.*;
import java.io.File;
import java.io.PrintStream;
import java.net.URI;

/**
 * Created by blogw on 2015/09/24.
 */
public class Test {
    public static final char CARRIAGE_RETURN = '\r';
    public static void main(String[] args) throws Exception {
//        VideoLink vl=new VideoLink("poster")
//        SqliteUtils.getInstance().insert();

//        if(Desktop.isDesktopSupported()) {
//            Desktop.getDesktop().browse(new URI("http://www.baidu.com"));
//        }


        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine se = sem.getEngineByName("javascript");
        StringBuffer sb=new StringBuffer();
        se.put("sb", sb);
        se.eval(FileUtils.readFileToString(new File("c:/answer.txt")) + "sb.append(config.file);");
        System.out.println(sb.toString());

        PrintStream streamToUse=System.out;
        streamToUse.print("90%");
        streamToUse.print(CARRIAGE_RETURN);

        Thread.sleep(1000);

        streamToUse.print("95%");
        streamToUse.print(CARRIAGE_RETURN);

        Thread.sleep(1000);

        streamToUse.print("99%");
        streamToUse.print(CARRIAGE_RETURN);

        ProgressBar progressBar = ConsoleProgressBar.on(System.out)
                .withFormat("[:bar] :percent% :elapsed").withTotalSteps(50);
        progressBar.tick(40);
    }
}