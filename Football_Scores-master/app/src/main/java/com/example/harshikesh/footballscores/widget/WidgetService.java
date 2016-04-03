package com.example.harshikesh.footballscores.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Harshikesh on 03/04/16.
 */
public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetDataProvider(this, intent);
    }
}
