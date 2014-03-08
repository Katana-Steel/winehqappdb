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

public class WineSearch extends AsyncTask<HttpUriRequest, Void, StringBuffer>
{
    SearchView main;
    final int TIMEOUT_MS = 10000;
    AndroidHttpClient httpClient;
    public List<NameValuePair> webData;

    MyArrayAdapter tvlist;

    public WineSearch(SearchView app)
    {
        super();
        this.main = app;
        this.tvlist = app.tvlist;
        this.webData = app.webData;
    }

    public HttpUriRequest getCall(String url)
    {
        try {

            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(webData));
            return httpPost;
        }
        catch(Exception e)
        { }
        return null;
    }

    @Override
    protected void onPreExecute()
    {
        String str = "Starting to get data from server";
        tvlist.clear();
        tvlist.add(new str_link(str,""));
        try {
            httpClient = AndroidHttpClient.newInstance("WineHQ/1.0 App Browser");
            HttpConnectionParams.setConnectionTimeout(httpClient.getParams(), TIMEOUT_MS);
            HttpConnectionParams.setSoTimeout(httpClient.getParams(), TIMEOUT_MS);
        }
        catch(Exception e) {
            str = "Couldn't set Params0";
            tvlist.add(new str_link(str,""));
        }
    }

    @Override
    protected StringBuffer doInBackground(HttpUriRequest... url)
    {
        StringBuffer sb = new StringBuffer("");
        Thread tt = Thread.currentThread(); // for thread sleep
        HttpResponse res = null;
        while(res == null)
        try {
            tt.sleep(500);
            res = httpClient.execute(url[0]);
        } 
        finally {
            continue;
        }

        while(res.getStatusLine() == null)
        try {
            tt.sleep(500);
        } 
        finally {
            continue;
        }

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
        return sb;
    }

    @Override
    protected void onPostExecute(StringBuffer res)
    {
        httpClient.close();

        String str = "Processing responce! (" + Long.valueOf(res.length()).toString() + " bytes)";
        tvlist.clear();
        // tvlist.add(str);

        int istart = res.indexOf("<table");
        int iend = res.indexOf("</table>", istart);
        String tab;
        try {
            tab = res.substring(istart, iend);
        }
        catch(StringIndexOutOfBoundsException e)
        {
            tvlist.add(new str_link("No Match Found",""));
            return;
        }
        iend = 0;
        String link;
        for(int i = 1;iend < tab.lastIndexOf("</td>");i++) {
            istart = tab.indexOf("<tr class=\"color"+ Long.valueOf(i%2).toString() +"\">", iend);
            istart = tab.indexOf("<td>", istart) + 6;
            istart = tab.indexOf("href=", istart) + 6;
            iend = tab.indexOf("\"", istart);
            link = tab.substring(istart, iend);
            istart = tab.indexOf(">", istart) + 1;
            iend = tab.indexOf("</a>", istart);
            str = tab.substring(istart, iend);
            iend = tab.indexOf("</tr>",istart);
            tvlist.add(new str_link(str,link));
        }

    }
};
