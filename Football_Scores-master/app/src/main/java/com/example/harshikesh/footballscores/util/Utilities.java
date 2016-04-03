package com.example.harshikesh.footballscores.util;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.text.format.Time;
import com.example.harshikesh.footballscores.R;
import com.example.harshikesh.footballscores.service.MyFetchService;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;
    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return "Seria A";
            case PREMIER_LEGAUE : return "Premier League";
            case CHAMPIONS_LEAGUE : return "UEFA Champions League";
            case PRIMERA_DIVISION : return "Primera Division";
            case BUNDESLIGA : return "Bundesliga";
            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return "Group Stages, Matchday : 6";
            }
            else if(match_day == 7 || match_day == 8)
            {
                return "First Knockout round";
            }
            else if(match_day == 9 || match_day == 10)
            {
                return "QuarterFinal";
            }
            else if(match_day == 11 || match_day == 12)
            {
                return "SemiFinal";
            }
            else
            {
                return "Final";
            }
        }
        else
        {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            default: return R.drawable.no_icon;
        }
    }

    public static String getPageTitle(Context context,int position)
    {
        return getDayName(context,System.currentTimeMillis()+((position-2)*86400000));
    }
    private static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        }
        else if ( julianDay == currentJulianDay -1)
        {
            return context.getString(R.string.yesterday);
        }
        else
        {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }

    public static String getApplicationDirectory(Context context) {
        boolean isSdPresent = android.os.Environment.getExternalStorageState().equals(
            android.os.Environment.MEDIA_MOUNTED);
        if(isSdPresent) {

            return context.getExternalFilesDir(null).toString();
        } else {
            return null;
        }
    }
    public static boolean isExternalStorageAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    public static boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    public static String fixUrlIfSvg(String UrlString) {
        String svgName = UrlString.substring(UrlString.lastIndexOf("/")+ 1, UrlString.length());
        int toEndWikipediaInt = UrlString.indexOf("wikipedia/") + 10;

        String toEndWikipedia = UrlString.substring(0,toEndWikipediaInt);
        String fromEndWikipedia = UrlString.substring(toEndWikipediaInt);

        String toStartThumb = fromEndWikipedia.substring(0, fromEndWikipedia.indexOf("/"));
        String partAfterThumb = fromEndWikipedia.substring(fromEndWikipedia.indexOf("/"), fromEndWikipedia.length());
        // 144px was the max size in our  samples,  we will use this
        String lastPart = "/144px-" + svgName + ".png";
        return toEndWikipedia + toStartThumb + "/thumb" + partAfterThumb + lastPart;

    }

    public static String downloadImageFile(Context context, String urlString, String teamName) {


        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlString));
        if (getApplicationDirectory(context) != null) {
            File pathDir = new File(getApplicationDirectory(context) + "/image_files");
            pathDir.mkdirs();
            int postionLastSlash = urlString.lastIndexOf('/');
            int postitionfileType = urlString.length() - 3;
            String fileExtension = urlString.substring(postitionfileType, urlString.length());
            String fileName = urlString.substring(postionLastSlash, urlString.length());
            fileName = fileName.replace(".svg","");
            String outputFile = "/" + "/image_files" + "/" + teamName;
            request.setDestinationInExternalFilesDir(context, "", outputFile);
            long enqueue = dm.enqueue(request);
            return getApplicationDirectory(context) + outputFile;
        }
        return "";
    }

    public static void update_scores(Context context){
        Intent service_start = new Intent(context, MyFetchService.class);
        context.startService(service_start);
    }
}
