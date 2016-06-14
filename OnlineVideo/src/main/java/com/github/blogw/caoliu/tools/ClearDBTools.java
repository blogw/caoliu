package com.github.blogw.caoliu.tools;

import com.github.blogw.caoliu.beans.PageLink;
import com.github.blogw.caoliu.constant.WebConstants;
import com.github.blogw.caoliu.utils.SqliteUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * delete not downloaded
 * <p>
 * Created by blogw on 2016/05/29.
 */
public class ClearDBTools {
    private static Log log = LogFactory.getLog(ClearDBTools.class);

    public static void main(String[] args) throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from video where videook!='1'");

        for (HashMap<String, String> map : list) {
            PageLink pl = SqliteUtils.convert(map);
            File dir=new File(WebConstants.SAVE_FOLDER+pl.getTxt());
            FileUtils.deleteDirectory(dir);
            log.info(pl.getTxt()+" deleted");
        }
    }
}
