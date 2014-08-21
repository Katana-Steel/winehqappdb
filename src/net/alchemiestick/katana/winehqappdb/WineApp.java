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

// the need to see all the other classes in the packege; espesially R
import net.alchemiestick.katana.winehqappdb.*;

import android.content.*;
import android.os.AsyncTask;
import android.net.*;
import android.net.http.*;
import android.text.*;
import android.webkit.*;
import android.widget.*;
import java.io.*;
import javax.xml.parsers.*;
import org.apache.http.*;
import org.apache.http.params.*;
import org.apache.http.message.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;
import org.w3c.dom.*;
import org.xml.sax.*;

public class WineApp extends AsyncTask<HttpUriRequest,Void, String>
{
    final int TIMEOUT_MS = 10000;
    AndroidHttpClient client;
    VersionAdapter adapter;
    TextView tv;
    String str;
    String body;
    
    public WineApp(VersionAdapter a, TextView tv)
    {
        super();
        this.adapter = a;
        this.tv = tv;
        this.str = new String("");
        this.body = new String("");
    }

    @Override
    protected void onPreExecute()
    {
        String str = "Getting;App data;from server;";
        this.adapter.clear();
        this.adapter.add(str);
        try {
            this.client = AndroidHttpClient.newInstance("WineHQ/1.0 App Browser");
            HttpConnectionParams.setConnectionTimeout(this.client.getParams(), TIMEOUT_MS);
            HttpConnectionParams.setSoTimeout(this.client.getParams(), TIMEOUT_MS);
        }
        catch(Exception e) {
            str = "Couldn't set Params0;;;";
            this.adapter.add(str);
        }
    }

    @Override
    protected String doInBackground(HttpUriRequest... url)
    {
        if(url[0] == null)
            return this.body;
        HttpResponse res = null;
        while(res == null)
        try {
            SearchView.do_sleep(500);
            res = this.client.execute(url[0]);
        } 
        finally {
            continue;
        }
            
        while(res.getStatusLine() == null) {
            SearchView.do_sleep(500);
        } 

        StringBuffer sb = new StringBuffer("");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(res.getEntity().getContent()), (2*1024));
            String line;
            boolean first = true;
            while((line = br.readLine()) != null) {
                if(first) { first = false; continue; }
                sb.append(line + "\n");
            }
            
            this.body = sb.substring(sb.indexOf("<body"),(sb.lastIndexOf("</body>") + 7)); 
        }
        catch(IOException ioe)
        {
            this.str = "IO Failed!!!:; ; ;";
            this.str += ioe.toString();            
        }
        catch(Exception ex)
        {
        }
        finally {
            this.client.close();
        }
        return this.body;
    }
    
    private void noVTable()
    {
        this.adapter.add("No;Version;Table;found");
    }
    
    @Override
    protected void onPostExecute(String res)
    {
        if(this.str.length() > 1)
            this.tv.setText(this.str);
                
        this.str = "";
        
        this.adapter.clear();
        String test = "sClass=version";
        if(!res.contains(test)) {
            tv.setText("Version Lines not found");
        }
        try {
            
            BufferedReader is = new BufferedReader(new StringReader(res),(4*1024));
            String table;
            do
            {
                table = is.readLine();
                if(table == null && this.str == "" )
                {
                    this.noVTable();
                    this.adapter.add(res);
                    return;
                }
                if(table.contains(test)) {
                    this.str = table;
                }
            }
            while(table != null);
            is = null;
            this.body = "";
        }
        catch(Exception e)
        {
            if(!this.str.contains(test)) {
                this.noVTable();
            }
        }
        
        int iend = 0;
        int istart = 0;
        int max = this.str.lastIndexOf("</table>");
        this.adapter.add("Version;Class;Wine\nVersion;");
        while(iend < max) {
            istart = this.str.indexOf("<a ", iend);
            istart = this.str.indexOf(">", istart) + 1;
            iend = this.str.indexOf("</", istart);
            if(istart < iend)
                this.body = this.str.substring(istart,iend);
            istart = this.str.indexOf("<td", istart) + 2; // skipping the description
            istart = this.str.indexOf("<td", istart) + 2;
            istart = this.str.indexOf(">", istart) + 1;
            iend = this.str.indexOf("</td>", istart);
            this.body += ";";
            this.body += this.str.substring(istart,iend);
            istart = this.str.indexOf("<td", iend) + 2; 
            istart = this.str.indexOf(">", istart) + 1;
            iend = this.str.indexOf("</td>", istart);
            this.body += ";";
            this.body += this.str.substring(istart,iend);            
            this.body += ";";
            iend = this.str.indexOf("</tr>",istart) + 6;
            this.adapter.add(this.body);
        }
    }
    
}
