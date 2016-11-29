package com.packtpub.wflydevelopment.chapter6.jms;

import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.CompletionListener;
import javax.jms.Message;

@ApplicationScoped
public class BookingCompletionListener implements CompletionListener {

    @Inject
    private Logger logger;

    @Override
    public void onCompletion(Message message) {
        try {
            final String text = message.getBody(String.class);
            logger.info("Send was successful: " + text);
        } catch (Throwable e) {
            logger.severe("Problem with message format");
        }
    }

    @Override
    public void onException(Message message, Exception exception) {
        try {
            final String text = message.getBody(String.class);
            logger.info("Send failed..." + text);
        } catch (Throwable e) {
            logger.severe("Problem with message format");
        }
    }
}
