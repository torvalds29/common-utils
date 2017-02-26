package com.oracle.vo;

import org.springframework.mail.SimpleMailMessage;

/**
 * Created by oracle on 2017/2/26.
 */
public class MultiMailMessage extends SimpleMailMessage {
    /**
     * html为true时发送html邮件
     */
    boolean htmlText;

    public boolean isHtmlText() {
        return htmlText;
    }

    public void setHtmlText(boolean htmlText) {
        this.htmlText = htmlText;
    }



    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (htmlText ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        MultiMailMessage that = (MultiMailMessage) o;

        return htmlText == that.htmlText;

    }
}
