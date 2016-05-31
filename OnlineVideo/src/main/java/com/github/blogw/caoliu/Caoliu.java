package com.github.blogw.caoliu;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.utils.HtmlUtils;
import com.github.blogw.caoliu.utils.HttpUtils;
import com.github.blogw.download.Obj2DiskUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

import static com.github.blogw.caoliu.constant.WebConstants.OVER_FOLDER;

/**
 * Created by blogw on 2015/09/24.
 */
public class Caoliu {
    private static Log log = LogFactory.getLog(Caoliu.class);
    private String url;
    private Stack<PageLink> stack = new Stack<>();
    String ymd = DateFormatUtils.format(Calendar.getInstance(), "yyyyMMdd");

    public Caoliu(String url) {
        this.url = url;
    }

    public void parse() throws Exception {
        //Runtime.getRuntime().addShutdownHook(xxx);

        // get referer from url
        String referer = url.replaceAll("(.*?)://(.*?)/.*", "$1://$2");

        // parse list page
        // TODO: may be need gb2312 to utf-8 because some char is ?
        String html = HttpUtils.readUrl(url, referer, "GB2312");
        html = new String(html.getBytes("GB2312"), "UTF-8");

        List<PageLink> links = HtmlUtils.parseLinks(html, referer);
        links = Lists.reverse(links);

        for (PageLink pl : links) {
            if(pl.getTxt().indexOf("�")>=0) {
                log.info(pl.getTxt());
            }
            //CaoliuParser.parse(pl);
            //stack.push(pl);
        }

        Obj2DiskUtils.serialize("d:/caoliu/tasks-" + ymd + ".obj", stack);


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
        if (names.contains(name)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        // analyze all link of page, got poster and video url from this links and save to disk
        String url = "http://og.90cl.org/thread0806.php?fid=22";
        Caoliu cl = new Caoliu(url);
        cl.parse();

        // check video and poster url and get again if url is null
        String file = "d:/caoliu/tasks-20160528.obj";
        Stack<PageLink> stack = (Stack<PageLink>) Obj2DiskUtils.deserialize(file);
        Iterator<PageLink> it = stack.iterator();
//        while(it.hasNext()){
//            PageLink pl=it.next();
//            if(pl.getVideoUrl()==null || pl.getPosterUrl()==null){
//                // got video and poster url again
//                switch (pl.getType()){
//                    case NINEP91:
//                        Parser9P91 p=new Parser9P91();
//                        p.analyze(pl.getReferer2(), pl);
//                        break;
//                }
//            }
//        }
//        Obj2DiskUtils.serialize(file, stack);


        //TODO: add poster downloaded property and serialize to disk
        while (it.hasNext()) {
            PageLink pl = it.next();

            if ("137906.jpg".equals(pl.getPoster())) {
                log.info(pl.getTxt());
                break;
            }

            //HttpUtils.downloadPoster(pl);
        }


//        while(it.hasNext()){
//            PageLink pl=it.next();
//            if(pl.getType()!=null && !pl.getTxt().startsWith("!")) {
//
//                File f=new File(WebConstants.SAVE_FOLDER,pl.getVideo());
//                if(f.exists()){
//                    Thread.sleep(1000);
//                    continue;
//                }
//                if(!Manager.start(pl)){
//                    Thread.sleep(1000);
//                    continue;
//                }
//            }
//        }

    }
}
