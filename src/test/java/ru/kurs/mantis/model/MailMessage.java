package ru.kurs.mantis.model;

/**
 * Created by yana on 4/9/2016.
 */
public class MailMessage {
    public String to;
    public String text;

    public MailMessage(String to, String text) {
        this.to = to;
        this.text = text;
    }
}
