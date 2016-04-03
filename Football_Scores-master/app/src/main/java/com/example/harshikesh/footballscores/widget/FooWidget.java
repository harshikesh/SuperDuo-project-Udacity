package com.example.harshikesh.footballscores.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;
import com.example.harshikesh.footballscores.R;
import com.example.harshikesh.footballscores.service.MyFetchService;
import com.example.harshikesh.footballscores.ui.MainActivity;
import com.example.harshikesh.footballscores.util.Utilities;

/**
 * Created by harshikesh.kumar on 03/04/16.
 */
public class FooWidget extends AppWidgetProvider {

  static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.football_widget);

    setRemoteAdapter(context, views);
    Intent intent = new Intent(context, MainActivity.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    views.setPendingIntentTemplate(R.id.widget_list, pendingIntent);
    appWidgetManager.updateAppWidget(appWidgetId, views);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int appWidgetId : appWidgetIds) {
      updateAppWidget(context, appWidgetManager, appWidgetId);
    }
    Utilities.update_scores(context);
    super.onUpdate(context, appWidgetManager, appWidgetIds);
  }

  @Override public void onEnabled(Context context) {
  }

  @Override public void onDisabled(Context context) {
  }

  private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
    views.setRemoteAdapter(R.id.widget_list, new Intent(context, WidgetService.class));
  }

  @Override public void onReceive(@NonNull Context context, @NonNull Intent intent) {
    super.onReceive(context, intent);
    if (MyFetchService.ACTION.equals(intent.getAction())) {
      AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
      int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, getClass()));
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }
  }
}