package com.github.blogw.caoliu;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.CaoliuParser;
import com.github.blogw.caoliu.utils.HtmlUtils;
import com.github.blogw.caoliu.utils.HttpUtils;
import com.github.blogw.download.Manager;
import com.github.blogw.download.Obj2DiskUtils;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

import static com.github.blogw.caoliu.constant.WebConstants.*;

/**
 * Created by blogw on 2015/09/24.
 */
public class Caoliu {
    private static Log log = LogFactory.getLog(Caoliu.class);
    private String url;
    private Stack<PageLink> stack = new Stack<>();
    String ymd=DateFormatUtils.format(Calendar.getInstance(),"yyyyMMdd");

    public Caoliu(String url) {
        this.url = url;
    }

    public void parse() throws Exception {
        //Runtime.getRuntime().addShutdownHook(xxx);

        // get referer from url
        String referer = url.replaceAll("(.*?)://(.*?)/.*", "$1://$2");

        // parse list page
        String html = HttpUtils.readUrl(url, referer, "GB2312");

        List<PageLink> links = HtmlUtils.parseLinks(html, referer);
        links = Lists.reverse(links);

        for (PageLink pl : links) {
            CaoliuParser.parse(pl);
            stack.push(pl);
        }

        Obj2DiskUtils.serialize("c:/caoliu/tasks-"+ymd+".obj", stack);


        //创建一个可重用固定线程数的线程池
//        ExecutorService pool = Executors.newFixedThreadPool(1);
        //创建实现了Runnable接口对象，Thread对象当然也实现了Runnable接口
//        CaoliuThread t1 = new CaoliuThread(referer,stack);
//        CaoliuThread t2 = new CaoliuThread(referer,stack);
//        CaoliuThread t3 = new CaoliuThread(referer,stack);

        //将线程放入池中进行执行
//        pool.execute(t1);
//        pool.execute(t2);
//        pool.execute(t3);

        //关闭线程池
//        pool.shutdown();

    }

    private static boolean isOver(String name) throws Exception {
        File folder = new File(OVER_FOLDER);
        List<String> names = Arrays.asList(folder.list());
        if(names.contains(name)){
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
//        String url = "http://cl.romcl.org/thread0806.php?fid=22";
//        Caoliu cl=new Caoliu(url);
//        cl.parse();

        Stack<PageLink> stack=(Stack<PageLink>)Obj2DiskUtils.deserialize("c:/caoliu/tasks-20151229.obj");
        Iterator<PageLink> it=stack.iterator();
        while(it.hasNext()){
            PageLink pl=it.next();
            if(pl.getType()!=null && !pl.getTxt().startsWith("!")) {

                File f=new File(WebConstants.SAVE_FOLDER,pl.getVideo());
                if(f.exists()){
                    Thread.sleep(1000);
                    continue;
                }
                if(!Manager.start(pl)){
                    Thread.sleep(1000);
                    continue;
                }
            }
        }
        Obj2DiskUtils.serialize("c:/caoliu/tasks-20151229.obj", stack);
    }
}
