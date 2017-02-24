package com.datfunc.basiclauncher;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class AppsListActivity extends AppCompatActivity {

    private PackageManager manager;
    private List<AppDetail> apps;
    private ListView list;
    private List<AppDetail> runningApps;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("M-d-yyyy HH:mm:ss");


    Context context = this;
    public static final String USAGE_STATS_SERVICE = "usagestats";
    private static final String TAG = "USAGE_STATS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps_list);

        loadApps();
        loadListView();
        addClickListener();

    }


    private void loadApps(){
        manager = getPackageManager();
        apps = new ArrayList<AppDetail>();
        runningApps = new ArrayList<AppDetail>();


        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);


        final UsageStatsManager usageStatsManager=(UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);// Context.USAGE_STATS_SERVICE);
        final int currentYear=Calendar.getInstance().get(Calendar.YEAR);


        Calendar beginCal = Calendar.getInstance();
        beginCal.set(Calendar.DATE, 1);
        beginCal.set(Calendar.MONTH, 0);
        beginCal.set(Calendar.YEAR, 2016);

        Calendar endCal = Calendar.getInstance();
        endCal.set(Calendar.DATE, 1);
        endCal.set(Calendar.MONTH, 0);
        endCal.set(Calendar.YEAR, 2018);

        final List<UsageStats> queryUsageStats=usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginCal.getTimeInMillis(), endCal.getTimeInMillis());


        long startTime = beginCal.getTimeInMillis();
        long endTime = endCal.getTimeInMillis();



        //Log.i(TAG, "Range start:" + dateFormat.format(startTime) );
        //Log.i(TAG, "Range end:" + dateFormat.format(endTime));

        UsageEvents uEvents = usageStatsManager.queryEvents(startTime,endTime);
       /* while (uEvents.hasNextEvent()) {
            UsageEvents.Event e = new UsageEvents.Event();
            uEvents.getNextEvent(e);

            if (e != null) {
                Log.d("USAGE_EVENT", "Event: " + e.getPackageName() + "\t" + e.getTimeStamp() + "\t" );
            }

        }*/

        for (UsageStats u : queryUsageStats){
            if (u.getTotalTimeInForeground() > 0)
            {
                AppDetail app = new AppDetail();
                try{
                    ApplicationInfo ai = manager.getApplicationInfo(u.getPackageName(), PackageManager.GET_META_DATA);
                    app.name = u.getPackageName();
                    app.label = manager.getApplicationLabel(ai);
                    apps.add(app);

                }
                catch(PackageManager.NameNotFoundException e){

                Log.i("PACKAGE_MANAGER", "FAILED TO FETCH Application Info");
            }

                Log.i("USAGE_STATS",  u.getPackageName());
            }
        }




        //printCurrentUsageStatus(AppsListActivity.this);
      //  printActiveApps(queryUsageStats);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);

//        for (ResolveInfo ri : availableActivities){
//
//            AppDetail app = new AppDetail();
//            app.label = ri.loadLabel(manager);
//            app.name = ri.activityInfo.packageName;
//            app.icon = ri.activityInfo.loadIcon(manager);
//
//            if(!usageStatsManager.isAppInactive((String)app.name)){
//                apps.add(app);
//            }
//
//        }

        /*Implemented Comapartor for class Apptetail , takes a charSequence and converst to string for compare*/
        Comparator<AppDetail> comparator = new Comparator<AppDetail>() {
            @Override
            public int compare(AppDetail app1, AppDetail app2) {
                return app1.getLabel().toString().compareTo(app2.getLabel().toString());
            }
        };

       Collections.sort(apps, comparator);


    }


    private void loadListView() {
        list = (ListView) findViewById(R.id.apps_list);


        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, apps){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }


                //ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                //appIcon.setImageDrawable(apps.get(position).icon);

                TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).label);

                //TextView appName = (TextView)convertView.findViewById(R.id.item_app_name);
                //appName.setText(apps.get(position).name);

                return convertView;
            }
        };

        list.setAdapter(adapter);



    }

    public static void printUsageStats(List<UsageStats> usageStatsList){
        for (UsageStats u : usageStatsList){
            Log.d("USAGE_STATS", "Pkg: " + u.getPackageName() +  "\t" + "ForegroundTime: "
                    + u.getTotalTimeInForeground()) ;
        }

    }

    public static void printActiveApps(List<UsageStats> usageStatsList){
        for (UsageStats u : usageStatsList){
            if (u.getTotalTimeInForeground() > 0)
            {
                Log.i("USAGE_STATS",  u.getPackageName());
            }
        }
    }

    public static List<UsageStats> getUsageStatsList(Context context){
        UsageStatsManager usm = getUsageStatsManager(context);
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.add(Calendar.YEAR, -1);
        long startTime = calendar.getTimeInMillis();

        Log.d(TAG, "Range start:" + dateFormat.format(startTime) );
        Log.d(TAG, "Range end:" + dateFormat.format(endTime));

        List<UsageStats> usageStatsList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,startTime,endTime);
        return usageStatsList;
    }


    public static void printCurrentUsageStatus(Context context){
        printUsageStats(getUsageStatsList(context));
    }
    @SuppressWarnings("ResourceType")
    private static UsageStatsManager getUsageStatsManager(Context context){
        UsageStatsManager usm = (UsageStatsManager) context.getSystemService("usagestats");
        return usm;
    }



    public void goHome(View v){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

    }

    private void addClickListener() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                Intent i = manager.getLaunchIntentForPackage(apps.get(position).name.toString());
                AppsListActivity.this.startActivity(i);

            }
        });

    }

}
