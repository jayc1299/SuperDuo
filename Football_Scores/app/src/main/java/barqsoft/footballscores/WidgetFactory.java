package barqsoft.footballscores;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
	private Context mContext;
	Cursor mCursor;

	public WidgetFactory(Context context, Intent intent) {
		this.mContext = context;

		String[] projection = new String[]{DatabaseContract.scores_table.AWAY_COL,
				DatabaseContract.scores_table.HOME_COL, DatabaseContract.scores_table.HOME_GOALS_COL,
				DatabaseContract.scores_table.AWAY_GOALS_COL, DatabaseContract.scores_table.TIME_COL};

		String selection = DatabaseContract.scores_table.DATE_COL + " LIKE ?";

		SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
		String[] args = new String[]{String.valueOf(mformat.format(System.currentTimeMillis()))};

		ScoresDBHelper db = new ScoresDBHelper(mContext);
		mCursor = db.getReadableDatabase().query(DatabaseContract.SCORES_TABLE, projection, selection, args, null, null, null);
	}

	@Override
	public void onCreate() {
		// unused
	}

	@Override
	public void onDestroy() {
		// unused
	}

	@Override
	public int getCount() {
		return(mCursor.getCount());
	}

	@Override
	public RemoteViews getViewAt(int position) {
		if(mCursor != null && mCursor.moveToFirst()) {
			RemoteViews row = new RemoteViews(mContext.getPackageName(), R.layout.scores_list_item);
			row.setTextViewText(R.id.home_name, mCursor.getString(mCursor.getColumnIndex(DatabaseContract.scores_table.HOME_COL)));
			row.setTextViewText(R.id.away_name, mCursor.getString(mCursor.getColumnIndex(DatabaseContract.scores_table.AWAY_COL)));
			row.setTextViewText(R.id.data_textview, mCursor.getString(mCursor.getColumnIndex(DatabaseContract.scores_table.TIME_COL)));
			int homeGoals = mCursor.getInt(mCursor.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
			int awayGoals = mCursor.getInt(mCursor.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));
			row.setTextViewText(R.id.score_textview, Utilies.getScores(homeGoals, awayGoals));

			return (row);
		}else{
			return null;
		}
	}

	@Override
	public RemoteViews getLoadingView() {
		return(null);
	}

	@Override
	public int getViewTypeCount() {
		return(1);
	}

	@Override
	public long getItemId(int position) {
		return(position);
	}

	@Override
	public boolean hasStableIds() {
		return(true);
	}

	@Override
	public void onDataSetChanged() {
		// unused
	}
}