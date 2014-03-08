/*
    Copyright 2012 Rene Kjellerup
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.alchemiestick.katana.winehqappdb;

import net.alchemiestick.katana.winehqappdb.*;

import com.google.android.vending.licensing.*;

import java.io.*;
import java.util.*;
import android.accounts.*;
import android.app.*;
import android.content.*;
import android.content.DialogInterface.*;
import android.os.Bundle;
import android.os.Handler;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.os.AsyncTask;
import android.net.*;
import android.net.http.*;
import android.provider.Settings.Secure;
import android.webkit.*;
import org.apache.http.*;
import org.apache.http.params.*;
import org.apache.http.message.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;

public class SearchView extends Activity
{
    static public final int UNLICENSED  = 0x0004400;
    static public final int ABOUT_DLG   = 0x0004500;
    static public final int FILTERS_DLG = 0x0004600;
    static public final int WINAPP_DLG  = 0x0004700;
    static private final String myPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkdt/LgNoiCw9UwgawawbRv05QyfU1Bl+nrH64O1ErOZ5EDAJiP6golSEpvsPYfbXO/wlpOMzJeIiqUkO1m1ey4IeqYSJgYntO/yILJkthxZ8ry9/ipZXCZ4OKILycnooYmsTxDtjtcn+Ia/vTUbkiAhwT6l96pfsyHqf3YElSuApmgocza4E4rUPvOujjWbmetXJQjFCChLVs7KjLxGldpmn6d3s+Dp6odSYdoA0V+t+TMFReSO6zQAckmzXI0CQ1adyBcqp3htyK7M/qWoWFN4su92WvLZvcURpTAthFe7mqv/7dIvxmIN1owbDjC8z73/8ydqJq+vErrECWG/BCQIDAQAB";
    static private final byte[] pSalt = new byte[] {
      -127,  33, -77, 75,  38,   12, -111, 67, -12, 11,
       -45, 123,  27, 44, -38, -117,   69, 82,  -5, 8
     };
    
    public TextView  input;
    public List<NameValuePair> webData;

    public MyArrayAdapter tvlist;

    public ValidCheck lCheckerCallback;
    private LicenseChecker lChecker;
    private Handler uiHnd;
     
    private View.OnClickListener searchClick = new View.OnClickListener() {
        public void onClick(View v) {
//            try {
                Context cx = v.getContext();
                WineSearch appdb = new WineSearch((SearchView)cx);
                setAppNameData(input.getText().toString());
                appdb.execute(appdb.getCall("http://appdb.winehq.org/objectManager.php"));
/*            }
            catch(Exception e)
            { } */
        }
    };
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
 
        setContentView(R.layout.main);

        webData = new ArrayList<NameValuePair>();
        setDefaults();

        input = (EditText)findViewById(R.id.searchInput);

        Button s = (Button)findViewById(R.id.searchSubmit);
        s.setOnClickListener(this.searchClick);

        tvlist = new MyArrayAdapter(this);

        ListView lv = (ListView)findViewById(R.id.list);
        lv.setAdapter(tvlist);

        uiHnd = new Handler();
        
        // getting the "unique" deviceID
        String deviceId = Secure.getString(getContentResolver(), Secure.ANDROID_ID);        
        
        lCheckerCallback = new ValidCheck(this, uiHnd);
        
        lChecker = new LicenseChecker( this, 
            new ServerManagedPolicy( this,
                new AESObfuscator(pSalt, getPackageName(), deviceId)),
            myPubKey);
            
        makeCheck();
        Metrics mmc = new Metrics(this);
        mmc.execute(mmc.getCall("http://www.alchemiestick.net/store_user.php"));
    }
    
    public void makeCheck()
    {
        lChecker.checkAccess(lCheckerCallback);
    }
    
    /* showing / creating the menu */
    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.app_menu, m);
        return true;
    }

    /* dealing with the user's selection */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
    switch (item.getItemId()) {
            case R.id.about:
                showDialog(ABOUT_DLG);
                return true;
            case R.id.filters:
                // still to be done. based on setDefaults();
                showDialog(FILTERS_DLG);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private OnDismissListener appDismiss = new OnDismissListener() {
        public void onDismiss(DialogInterface di){
            app_dialog d = (app_dialog)di;
            d.clear();
        }
    };
    
    private DialogInterface.OnShowListener appShow = new DialogInterface.OnShowListener() {
        public void onShow(DialogInterface di){
            app_dialog d = (app_dialog)di;
            d.runWeb(tvlist.getCurrent());
        }
    };
    
    @Override
    protected Dialog onCreateDialog(int id)
    {
        Dialog diag;
        switch(id) {
        case UNLICENSED:
            diag = open_store();
            break;
        case ABOUT_DLG:
            diag = about_dialog();
            break;
        case FILTERS_DLG:
            // TODO: create the real filters dialog and have it modify webData accordingly.
            diag = new filter_dialog(this, this.webData);
            break;
        case WINAPP_DLG:
            diag= new app_dialog(this);
            diag.setOnDismissListener(appDismiss);
            diag.setOnShowListener(appShow);
            break;
        default:
            diag = null;
        }
        return diag;
    }

    private Dialog open_store()
    {
        String msg = "Couldn't verify that this account legally purchased this application ";
        msg += "from the google play store.\nSorry, it doesn't have any unlicensed features.";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unlicensed copy!!!")
        .setMessage(msg)
        .setCancelable(true)
        .setPositiveButton("Buy", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent goStore = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id="+getPackageName()));
                startActivity(goStore);
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        return builder.create();
    }

    private Dialog about_dialog()
    {
        String msg = "Copyright May 25th 2012 by Rene Kjellerup (aka Katana Steel) and Alchemiestick.\n\n";
        msg += "WineHQ Appdb Search is released under GPLv3 or later. It uses images from WINE project under LGPLv2 or later ";
        msg += "see license:\nhttp://www.gnu.org/licenses/\nfor more infomation about the licenses.\n\n";
        msg += "Souce code for the program can be obtained at\nhttp://www.alchemiestick.net/apps/?link=winehq\nand choose ";
        msg += "the link apropriate for the the version you are running.";
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About")
        .setMessage(msg)
        .setCancelable(true)
        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        return builder.create();
    }
    
    private void setNamedData(String name, String value) 
    {
        ListIterator<NameValuePair> itr = webData.listIterator();
        while(itr.hasNext())
        {
            NameValuePair t = itr.next();
            if(t.getName() == name) {
                itr.set(new BasicNameValuePair(name, value));
                return;
            }
        }
        webData.add(new BasicNameValuePair(name, value));
    }
    
    private void removeNamedData(String name) 
    {
        ListIterator<NameValuePair> itr = webData.listIterator();
        while(itr.hasNext())
        {
            NameValuePair t = itr.next();
            if(t.getName() == name) {
                itr.remove();
                return;
            }
        }
    }

    private void setAppNameData(String name) 
    {
        setNamedData("sappFamily-appNameData", name);
    }
    
    public void setDefaults()
    {
        webData.clear();
        /* the GET Data from the url */
        setNamedData("bIsQueue", "false");
        setNamedData("sClass", "application");  // this is what we are looking for
        setNamedData("sTitle", "Browse+Applications");
        setNamedData("iItemsPerPage", "30"); // get this many items at a time
        setNamedData("iPage", "1");  // first page
        setNamedData("sOrderBy", "appName");
        setNamedData("bAscending", "true");
        /* the default POST data from the web form */
        setNamedData("iappVersion-ratingOp", "5");
        setNamedData("iappCategoryOp", "11");
        setNamedData("iappVersion-licenseOp", "5");
        setNamedData("sappVersion-ratingData", ""); // Platinum, Gold, Silver, Bronze, Garbage 
        setNamedData("iversions-idOp", ""); // 5 =,6 <, 7 >
        setNamedData("sversions-idData", ""); // > 242 & < 131
        setNamedData("sappCategoryData", ""); // short int 1-999 (i think)
        setNamedData("sappVersion-licenseData", ""); // Retail, Open Source, Free to use, Free to use and share, Demo, Shareware
        setNamedData("iappFamily-appNameOp", "2"); // 2 = contains, 3 = starts with, 4 = ends with
        setNamedData("ionlyDownloadableOp", "10"); 
        setNamedData("sFilterSubmit", "Update Filter"); // the web submit button
        // setNamedData("sonlyDownloadableData", "true"); // unchecked by default
    }
    
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        lChecker.onDestroy();
    }
}
