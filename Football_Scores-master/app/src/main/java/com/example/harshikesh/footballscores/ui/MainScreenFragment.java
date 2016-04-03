package com.example.harshikesh.footballscores.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.harshikesh.footballscores.R;
import com.example.harshikesh.footballscores.adapter.ViewHolder;
import com.example.harshikesh.footballscores.adapter.scoresAdapter;
import com.example.harshikesh.footballscores.database.DatabaseContract;
import com.example.harshikesh.footballscores.service.MyFetchService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
  public scoresAdapter mAdapter;
  public static final int SCORES_LOADER = 0;
  private String[] fragmentdate = new String[1];
  private int last_selected_item = -1;
  private ListView mScore_list;
  private int mPosition;

  public MainScreenFragment() {
  }

  private void update_scores() {
    Intent service_start = new Intent(getActivity(), MyFetchService.class);
    getActivity().startService(service_start);
  }

  public void setFragmentDate(String date,int position) {
    fragmentdate[0] = date;
    mPosition = position;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      final Bundle savedInstanceState) {
    update_scores();
    View rootView = inflater.inflate(R.layout.fragment_main, container, false);
    mScore_list = (ListView) rootView.findViewById(R.id.scores_list);
    mAdapter = new scoresAdapter(getActivity(), null, 0);
    mScore_list.setAdapter(mAdapter);
    getLoaderManager().initLoader(SCORES_LOADER, null, this);
    mAdapter.detail_match_id = MainActivity.selected_match_id;
    mScore_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ViewHolder selected = (ViewHolder) view.getTag();
        mAdapter.detail_match_id = selected.match_id;
        MainActivity.selected_match_id = (int) selected.match_id;
        mAdapter.notifyDataSetChanged();
      }
    });
    return rootView;
  }

  @Override public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
    return new CursorLoader(getActivity(), DatabaseContract.scores_table.buildScoreWithDate(), null,
        null, fragmentdate, null);
  }

  @Override public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
    mAdapter.swapCursor(cursor);
    if (cursor != null) {
      int i = 0;

      cursor.moveToFirst();
      if (MainActivity.selectedTabPosition != ListView.INVALID_POSITION) {


        while (!cursor.isAfterLast()) {
          i++;
          if (cursor.getInt(cursor.getColumnIndex(DatabaseContract.scores_table.MATCH_ID)) != MainActivity.selectedTabPosition) {
            cursor.moveToNext();
          } else {

            break;
          }
        }
        final int scrolltoInt = i;
        mScore_list.post(new Runnable() {

          @Override
          public void run() {
            if (mPosition == MainActivity.selected_match_id) {

              mScore_list.setSelection(scrolltoInt-1);
              mScore_list.smoothScrollToPosition(scrolltoInt-1);
            }
          }
        });
      }


    }
  }

  @Override public void onLoaderReset(Loader<Cursor> cursorLoader) {
    mAdapter.swapCursor(null);
  }

   public void update() {

    restartLoader();
  }

  private void restartLoader() {
    getLoaderManager().restartLoader(10, null, this);
  }
}
