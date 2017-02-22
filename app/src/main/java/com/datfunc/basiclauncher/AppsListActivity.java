package com.datfunc.basiclauncher;

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

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);


        for (ResolveInfo ri : availableActivities){

            AppDetail app = new AppDetail();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);

        }

        /*Implemented Comapartor for class Apptetail , takes a charSequence and converst to string for compare*/
        Comparator<AppDetail> comparator = new Comparator<AppDetail>() {
            @Override
            public int compare(AppDetail app1, AppDetail app2) {
                return app1.getLabel().toString().compareTo(app2.getLabel().toString());
            }
        };

        Collections.sort(apps, comparator);

        /** CODE TO LOAD PROCESSES THAT ARE RUNNING ON Phone
         * Turns out this is deprcated as of API 21
        ActivityManager activityManager = (ActivityManager)
                this.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();
        Log.i("ListViewLogs", "Number of running apps= "+ processes.size());

         for (ActivityManager.RunningAppProcessInfo am : processes){

         AppDetail app = new AppDetail();
         try{
         app.label = (String) manager.getApplicationLabel((manager.getApplicationInfo(am.processName, PackageManager.GET_META_DATA)));
         } catch (PackageManager.NameNotFoundException e){
         return;
         }
         runningApps.add(app);

         }
         **/







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

    private void loadListViewRunning() {
        list = (ListView) findViewById(R.id.running_apps_list);


        ArrayAdapter<AppDetail> adapter = new ArrayAdapter<AppDetail>(this, R.layout.list_item, runningApps){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                }

                TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label);
                appLabel.setText(runningApps.get(position).label);

                return convertView;
            }
        };

        list.setAdapter(adapter);


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
