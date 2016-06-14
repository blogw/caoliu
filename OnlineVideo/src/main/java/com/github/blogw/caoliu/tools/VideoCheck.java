package com.github.blogw.caoliu.tools;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Box;
import com.coremedia.iso.boxes.MovieBox;
import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.SqliteUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by blogw on 2016/05/29.
 */
public class VideoCheck {
    private static Log log = LogFactory.getLog(VideoCheck.class);

    public static void main(String[] args) throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from video where videook='1'");
        List<PageLink> pllist = new ArrayList<>();

        for (HashMap<String, String> map : list) {
            PageLink pl = SqliteUtils.convert(map);

            File f=new File(WebConstants.SAVE_FOLDER+pl.getTxt(),pl.getVideo());

            try{
                IsoFile isoFile = new IsoFile(f.getAbsolutePath());
                MovieBox moov = isoFile.getMovieBox();
                log.info(pl.getTxt());
                for(Box b : moov.getBoxes()) {
                    log.info(b);
                    break;
                }
            }
            catch (IOException e){
                log.info("===>"+pl.getTxt());
            }
        }
    }
}
