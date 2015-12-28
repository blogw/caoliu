package com.github.blogw.caoliu;

import java.io.PrintStream;
import java.math.BigDecimal;

/**
 * Created by blogw on 2015/12/25.
 */
public class Progress {
    public static final char CARRIAGE_RETURN = '\r';
    private PrintStream streamToUse;
    private BigDecimal total;

    public Progress(PrintStream streamToUse,long t){
        this.streamToUse=streamToUse;
        this.total=new BigDecimal(Long.toString(t));
    }

    public static Progress on(final PrintStream streamToUse,long total) {
        return new Progress(streamToUse,total);
    }

    public void tick(long c){
        BigDecimal current=new BigDecimal(Long.toString(c));
        BigDecimal result=current.multiply(new BigDecimal(100)).divide(total, 2, BigDecimal.ROUND_HALF_UP);
        streamToUse.print(result.toString()+"%");
        streamToUse.print(CARRIAGE_RETURN);
    }

    public static void main(String[] args) {
        Progress.on(System.out,100).tick(49);
    }
}
