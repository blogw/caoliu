package com.github.blogw.caoliu;

/**
 * Created by blogw on 2015/12/25.
 */
public enum VedioType {
    DEFAULT, UP2STREAM, PPT, NINEP91, AVTAOBAO, TADPOLES, P9P, QINGYULE, QQ2;

    public static VedioType getByName(String name) {
        switch (name) {
            case "UP2STREAM":
                return UP2STREAM;
            case "PPT":
                return PPT;
            case "NINEP91":
                return NINEP91;
            case "AVTAOBAO":
                return AVTAOBAO;
            case "TADPOLES":
                return TADPOLES;
            case "P9P":
                return P9P;
            case "QINGYULE":
                return QINGYULE;
            case "QQ2":
                return QQ2;
        }
        return DEFAULT;
    }
}
