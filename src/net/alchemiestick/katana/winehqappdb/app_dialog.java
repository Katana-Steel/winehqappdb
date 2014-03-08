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

import java.util.*;
import android.app.*;
import android.content.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.*;
import org.apache.http.*;
import org.apache.http.params.*;
import org.apache.http.message.*;
import org.apache.http.client.*;
import org.apache.http.client.entity.*;
import org.apache.http.client.methods.*;

public class app_dialog extends Dialog
{
    private Context cx;
 
    private VersionAdapter adapter;
    public TextView tv;
    
    public app_dialog(Context cx) {
        super(cx);
        this.cx = cx;
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        this.adapter = new VersionAdapter(cx);
        this.clear();
    }
    
    public HttpUriRequest getCall(String addr)
    {
        String url = addr.replaceAll("&amp;","&");
        try {

            return new HttpGet(url);
        }
        catch(Exception e)
        { }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedI)
    {
        super.onCreate(savedI);
        this.setContentView(R.layout.web_dlg);
        ListView lv = (ListView)this.findViewById(R.id.win_versions);
        tv = (TextView)this.findViewById(R.id.win_name);
        lv.setAdapter(this.adapter);
    }
    
    public void clear()
    {
        this.adapter.clear();
    }
    
    public void runWeb(str_link win)
    {
        this.clear();
        this.tv.setText(win.str);
        // this.tv.setText(win.addr);
        this.adapter.add("Getting app data;;;");
        if(win.addr.length() > 0) {
            WineApp que = new WineApp(this.adapter, this.tv);
            que.execute(this.getCall(win.addr));
        }
    }
}
