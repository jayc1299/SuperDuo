package barqsoft.footballscores;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider{

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int count = appWidgetIds.length;

		//Intent service_start = new Intent(context, myFetchService.class);
		//context.startService(service_start);

		for (int i = 0; i < count; i++) {
			int widgetId = appWidgetIds[i];

			RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_today);

			Intent intent = new Intent(context, WidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

			if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
				remoteViews.setTextViewText(R.id.widget_today_title, context.getString(R.string.widget_text));
			}else{
				remoteViews.setRemoteAdapter(widgetId, R.id.widget_today_games, intent);
			}

			intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.widget_today, pendingIntent);
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}

		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
}