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

import java.io.*;
import java.util.*;
import android.app.*;
import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import android.os.AsyncTask;
import android.net.*;
import android.net.http.*;
import android.webkit.*;
import org.apache.http.*;
import org.apache.http.params.*;
import org.apache.http.message.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;
import android.provider.Settings.Secure;
import android.accounts.*;

public class Metrics extends AsyncTask<HttpPost, Void, StringBuffer>
{
    SearchView main;
    final int TIMEOUT_MS = 10000;
    AndroidHttpClient httpClient;
    public List<NameValuePair> webData;

    public Metrics(SearchView app)
    {
        super();
        this.main = app;
        this.webData = app.webData;
    }

    private String getAcc()
    {
        Account[] accounts = AccountManager.get(this.main).getAccountsByType("com.google");
        String devID = Secure.getString(this.main.getContentResolver(), Secure.ANDROID_ID);
        String myEmailid=accounts[0].name + ":" + devID;
        return myEmailid;
    }

    public HttpPost getCall(String url)
    {
        try {
            return new HttpPost(url);
        }
        catch(Exception e)
        { }
        return null;
    }

    @Override
    protected void onPreExecute()
    {
        try {
            httpClient = AndroidHttpClient.newInstance("WineHQ/1.0 App Browser");
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), TIMEOUT_MS);
            HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT_MS);
        }
        catch(Exception e) {
        }
    }

    @Override
    protected StringBuffer doInBackground(HttpPost... url)
    {
        setNamedData("lic","2");
        setNamedData("googleAcc",getAcc());
        HttpPost httpPost = url[0];
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(webData));
        }
        catch(Exception e) { }
        StringBuffer sb = new StringBuffer("");
        HttpResponse res = null;
        while(res == null)
        try {
            SearchView.do_sleep(500);
            res = httpClient.execute(httpPost);
        }
        finally {
            continue;
        }

        while(res.getStatusLine() == null)
            SearchView.do_sleep(500);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(res.getEntity().getContent()), 4096);
            String line;
            String NL = System.getProperty("line.seperator");
            while((line = br.readLine()) != null) {
                sb.append(line + NL);
            }
        }
        catch(Exception ex)
        {
        }
        finally {
            httpClient.close();
        }
        return sb;
    }

    @Override
    protected void onPostExecute(StringBuffer res)
    {
        main.setDefaults();
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
};
