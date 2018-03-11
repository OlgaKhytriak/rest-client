package com.epam.utils;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.testng.Reporter;

import java.util.ArrayList;
import java.util.List;

public class ReportAppender extends AppenderSkeleton {

    private List<LoggingEvent> eventList = new ArrayList<>();
    @Override
    protected void append(LoggingEvent loggingEvent) {
        eventList.add(loggingEvent);
        Reporter.log(loggingEvent.getMessage().toString());
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }
}
