package barqsoft.footballscores;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import barqsoft.footballscores.service.myFetchService;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainScreenFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
	public scoresAdapter mAdapter;
	public static final int SCORES_LOADER = 0;
	private String[] fragmentdate = new String[1];
	private int last_selected_item = -1;
	public static final String LOG_TAG = "MainScreenFragment";

	public MainScreenFragment(){
	}

	private void update_scores(){
		Intent service_start = new Intent(getActivity(), myFetchService.class);
		getActivity().startService(service_start);
	}

	public void setFragmentDate(String date){
		fragmentdate[0] = date;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
		Log.d(LOG_TAG, "onCreateView");
		update_scores();
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);
		final ListView score_list = (ListView) rootView.findViewById(R.id.scores_list);
		mAdapter = new scoresAdapter(getActivity(),null,0);
		score_list.setAdapter(mAdapter);
		getLoaderManager().initLoader(SCORES_LOADER, null, this);
		mAdapter.detail_match_id = MainActivity.selected_match_id;
		score_list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id){
				ViewHolder selected = (ViewHolder) view.getTag();
				mAdapter.detail_match_id = selected.match_id;
				MainActivity.selected_match_id = (int) selected.match_id;
				mAdapter.notifyDataSetChanged();
			}
		});
		return rootView;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int loaderId, Bundle bundle){
		Loader<Cursor> loader = null;
		if(loaderId == SCORES_LOADER) {
			Uri uri = DatabaseContract.scores_table.buildScoreWithDate();
			loader = new CursorLoader(getActivity(), uri, null, null, fragmentdate, null);
		}
		return loader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor){
		cursor.moveToFirst();
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> cursorLoader){
		mAdapter.swapCursor(null);
	}
}