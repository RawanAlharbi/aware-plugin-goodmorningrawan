package com.aware.plugin.goodmorningrawan;

import android.util.Log;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.ESM;
import com.aware.utils.Aware_Plugin;
import com.aware.utils.Scheduler;

import org.json.JSONException;

public class Plugin extends Aware_Plugin {

    public void onCreate() {
        super.onCreate();

        TAG = "GOODMORNINGRAWAN";
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

        if( DEBUG ) Log.d(TAG, "Good Morning plugin running");
        Aware.setSetting(this, Aware_Preferences.STATUS_ESM, true); //we will need the ESMs
        Aware.startSensor(this, Aware_Preferences.STATUS_ESM); //ask AWARE to start ESM

        try {
            scheduleMorningQuestionnaire(); //see further below
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Aware.setSetting(this, Settings.STATUS_PLUGIN_GOODMORNINGRAWAN, true);
        Aware.startPlugin(this, "com.aware.plugin.goodmorningrawan");
    }

    public void onDestroy() {
        super.onDestroy();
        if( DEBUG ) Log.d(TAG, "Good Morning plugin terminating.");
        Aware.stopSensor(this, Aware_Preferences.STATUS_ESM); //turn off ESM for our plugin
        Aware.setSetting(this, Settings.STATUS_PLUGIN_GOODMORNINGRAWAN, false);
        Aware.stopPlugin(this, "com.aware.plugin.goodmorningrawan");
    }

    private void scheduleMorningQuestionnaire() throws JSONException {
        Scheduler.Schedule schedule = new Scheduler.Schedule("morning_question");
        schedule.addHour(8); //we want this schedule every day at 8
        schedule.setActionType(Scheduler.ACTION_TYPE_BROADCAST); //we are doing a broadcast
        schedule.setActionClass(ESM.ACTION_AWARE_QUEUE_ESM); //with this action
        schedule.addActionExtra(ESM.EXTRA_ESM, MORNINGJSON); //and this extra
        Scheduler.saveSchedule(getApplicationContext(), schedule);
    }

    private static final String MORNINGJSON = "[{'esm':{" +
            "'esm_type':" + ESM.TYPE_ESM_TEXT + "," +
            "'esm_title': 'How did you sleep?'," +
            "'esm_instructions': 'How did you sleep last night? Please provide an estimate on a scale from 1 (worst) to 10 (best) as well as a written description of your night!'," +
            "'esm_submit': 'Submit.'," +
            "'esm_expiration_threshold': 300," + //the user has 5 minutes to respond. Set to 0 to disable
            "'esm_trigger': 'com.aware.plugin.goodmorningrawan'" +
            "}}]";

//    @Override
//    public void onCreate() {
//        super.onCreate();
//
//        TAG = "AWARE::"+getResources().getString(R.string.app_name);
//        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");
//
//        //Initialize our plugin's settings
//        if( Aware.getSetting(this, Settings.STATUS_PLUGIN_TEMPLATE).length() == 0 ) {
//            Aware.setSetting(this, Settings.STATUS_PLUGIN_TEMPLATE, true);
//        }
//
//        //Activate programmatically any sensors/plugins you need here
//        //e.g., Aware.setSetting(this, Aware_Preferences.STATUS_ACCELEROMETER,true);
//        //NOTE: if using plugin with dashboard, you can specify the sensors you'll use there.
//
//        //Any active plugin/sensor shares its overall context using broadcasts
//        CONTEXT_PRODUCER = new ContextProducer() {
//            @Override
//            public void onContext() {
//                //Broadcast your context here
//            }
//        };
//
//        //Add permissions you need (Support for Android M) e.g.,
//        //REQUIRED_PERMISSIONS.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        //To sync data to the server, you'll need to set this variables from your ContentProvider
//        //DATABASE_TABLES = Provider.DATABASE_TABLES
//        //TABLES_FIELDS = Provider.TABLES_FIELDS
//        //CONTEXT_URIS = new Uri[]{ Provider.Table_Data.CONTENT_URI }
//
//        //Activate plugin
//        Aware.startPlugin(this, "com.aware.plugin.template");
//    }

    //This function gets called every 5 minutes by AWARE to make sure this plugin is still running.
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        //Check if the user has toggled the debug messages
//        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");
//
//        return super.onStartCommand(intent, flags, startId);
//    }


}
