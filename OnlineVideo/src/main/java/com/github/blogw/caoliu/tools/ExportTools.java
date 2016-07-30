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
 * export backup video to html
 * Created by blogw on 2016/06/04.
 */
public class ExportTools {
    private static Log log = LogFactory.getLog(ExportTools.class);

    public static void main(String[] args) throws Exception {
        List<HashMap<String, String>> list = SqliteUtils.getInstance().select("select * from backup order by id");
        StringBuilder sb = new StringBuilder();
        sb.append("<html>\n");
        sb.append("<head>\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("<pre>\n");

        for (HashMap<String, String> map : list) {
            PageLink pl = SqliteUtils.convert(map);
            sb.append("<a href='" + pl.getUrl() + "'>");
            sb.append(pl.getTxt());
            sb.append("</a>\n");
            log.info(pl.getTxt());
        }

        sb.append("</pre>\n");
        sb.append("</body>\n");
        sb.append("</html>\n");

        File f = new File(WebConstants.SAVE_FOLDER, "backup.html");
        FileUtils.writeStringToFile(f, sb.toString(), "UTF-8");
    }
}
