package com.example.harshikesh.footballscores.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.harshikesh.footballscores.R;
import com.example.harshikesh.footballscores.database.DatabaseContract;
import com.example.harshikesh.footballscores.util.Utilities;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by harshikesh.kumar on 03/04/16.
 */
public class WidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {


  public static final int COL_HOME = 3;
  public static final int COL_AWAY = 4;
  public static final int COL_HOME_GOALS = 6;
  public static final int COL_AWAY_GOALS = 7;
  public static final int HOME_LOGO = 8;
  public static final int AWAY_LOGO = 9;
  public static final int COL_ID = 8;
  public static final int COL_MATCHTIME = 2;

  Context mContext = null;
  Cursor cursor;

  public WidgetDataProvider(Context context, Intent intent) {
    mContext = context;

  }

  @Override public void onCreate() {

  }

  @Override public void onDataSetChanged() {
    if (cursor != null) {
      cursor.close();
    }
    Date fragmentdate = new Date(System.currentTimeMillis());
    SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
    String[] fragmentDate = new String[1];
    fragmentDate[0] = mformat.format(fragmentdate);
    cursor = mContext.getContentResolver().query(DatabaseContract.scores_table.buildScoreWithDate(),
        null, null, fragmentDate, null);
    cursor.getCount();

  }

  @Override public void onDestroy() {

  }

  @Override public int getCount() {
    return cursor.getCount();
  }

  @Override public RemoteViews getViewAt(int position) {
    if (position == AdapterView.INVALID_POSITION || cursor == null) {
      return null;
    }
    cursor.moveToPosition(position);
    RemoteViews view = new RemoteViews(mContext.getPackageName(),
        R.layout.widget_list_item);

    view.setTextViewText(R.id.home_name,cursor.getString(COL_HOME));
    view.setTextViewText(R.id.away_name,cursor.getString(COL_AWAY));
    view.setTextViewText(R.id.data_textview, cursor.getString(COL_MATCHTIME));
    view.setTextViewText(R.id.score_textview,
        Utilities.getScores(cursor.getInt(COL_HOME_GOALS), cursor.getInt(COL_AWAY_GOALS)));
    //mHolder.match_id = cursor.getDouble(COL_ID);
    Bitmap b1;
    try {
      b1 = Glide.with(mContext)
          .load(cursor.getString(COL_HOME))
          .asBitmap()
          .error(R.drawable.no_icon)
          .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
          .get();
      view.setImageViewBitmap(R.id.home_crest,b1);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    Bitmap b2;
    try {
      b2 = Glide.with(mContext)
          .load(cursor.getString(COL_AWAY))
          .asBitmap()
          .error(R.drawable.no_icon)
          .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
          .get();
      view.setImageViewBitmap(R.id.away_crest,b2);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }



    // fill the intent from FootBall widget and add extra match id
    final Intent fillInIntent = new Intent();
    final Bundle extras = new Bundle();

    extras.putDouble("match_id",
        cursor.getDouble(COL_ID));
    fillInIntent.putExtras(extras);
    view.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

    return view;
  }

  @Override public RemoteViews getLoadingView() {
    return null;
  }

  @Override public int getViewTypeCount() {
    return 1;
  }

  @Override public long getItemId(int position) {
    return position;
  }

  @Override public boolean hasStableIds() {
    return true;
  }
}
