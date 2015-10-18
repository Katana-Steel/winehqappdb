/*
    Copyright 2015 Rene Kjellerup

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

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class Application
{
    private String title;
    private String url;
    private VersionAdapter versions;
    private View MyView;
    private Dialog MyDialog;
    private GetVersions verTask;

    public Application(String title) {
        this(title, null);
    }

    public Application(String title, String url)
    {
        this.title = title;
        if (url == null || url.isEmpty()) {
            this.url = null;
            this.versions = null;
            this.verTask = null;
        } else {
            this.url = url;
            this.versions = new VersionAdapter(SearchView.app_cx);
            this.verTask = new GetVersions(this.url, this.versions);
        }
    }

    public void getVersionsAsync()
    {
        if (this.verTask != null){
            if (this.verTask.getStatus() == AsyncTask.Status.PENDING) {
                this.verTask.execute();
            }
        }
    }
    public boolean hasVersions()
    {
        return !this.versions.isEmpty();
    }

    public View getMyView() {
        return MyView;
    }

    private View.OnClickListener showAppDialog = new View.OnClickListener() {
        public void onClick(View v) {
            getVersionsAsync();
            SearchView ax = (SearchView)SearchView.app_cx;
            try {
                ax.applist.FillInDialog(title, versions);
                ax.showDialog(SearchView.WINAPP_DLG);
            }
            catch(Exception e)
            { }
        }
    };

    public void setMyView(LayoutInflater inflater, ViewGroup parent) {
        if (MyView == null) {
            MyView = inflater.inflate(R.layout.list_item, parent, false);
            TextView textView = (TextView) MyView.findViewById(R.id.list_label);
            textView.setText(this.title);
            if (url != null) {
                textView.setOnClickListener(this.showAppDialog);
            }
        }
    }

}
