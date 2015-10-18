package net.alchemiestick.katana.winehqappdb;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import java.net.URL;
import java.net.HttpURLConnection;


/**
 * Created by rene on 5/7/15.
 */
class GetVersions extends AsyncTask<Void,Void,String[]> {
    final static int TIMEOUT_MS = 10000;
    private URL url;
    private VersionAdapter versionList;
    private HttpURLConnection request;

    public GetVersions(String addr, VersionAdapter versions) {
        this.request = null;
        this.versionList = versions;
        String url = addr.replaceAll("&amp;","&");
        if (url.startsWith ("//"))
            url = "https:" + url;
        try {
            this.url = new URL(url);
        } catch (Exception e)
        {
        }
    }

    private void setClient() {
        try {
            this.request = (HttpURLConnection)this.url.openConnection();
            if (this.request != null) {
                this.request.setRequestProperty("User-Agent", "WineHQ/1.1 App Browser");
                this.request.setConnectTimeout(TIMEOUT_MS);
            }
        } catch (Exception e) {
            this.request = null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        setClient();
        this.versionList.add(new Version({"Getting","From","versions","Server"}));
    }

    @Override
    protected void onPostExecute(String[] s) {
        super.onPostExecute(s);
        this.request.disconnect();
        if (s != null && s.length > 1) {
            this.versionList.clear();
            int len = s.length;
            for (int i = 0; i < len; ++i) {
                this.versionList.add(new Version(s[i].split(";")));
            }
        }
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        String table = "";
        String[] res = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(this.request.getInputStream()), (2*1024));
            String line;
            while((line = br.readLine()) != null) {
                if (line.contains("sClass=version")) {
                    table = line;
                    break;
                }
            }
            res = table.split("<a ");
            int len = res.length;
            String sep = ";";
            int istart = 0;
            if(len > 0)
                istart = res[0].indexOf("<td ");
            for (int i=0; i < len; ++i) {
                int iend = 0;
                String working = res[i];
                istart = working.indexOf(">",istart) + 1;
                iend = working.indexOf("</",istart);
                String end = working.substring(istart,iend); // adding the version
                end += sep;
                istart = working.indexOf("<td", istart) + 2; // skipping the description
                istart = working.indexOf("<td", istart) + 2;
                istart = working.indexOf(">",istart) + 1;
                iend = working.indexOf("</",istart);
                end += sep;
                end += working.substring(istart,iend); // adding the QA Class
                istart = working.indexOf("<td", iend) + 2;
                istart = working.indexOf(">",istart) + 1;
                iend = working.indexOf("</", istart);
                end += sep;
                end += working.substring(istart,iend); // version of Wine.
                end += sep;
                res[i] = end;
                working = null;
                istart=0;
            }
        }
        catch(Exception e){
            res = null;
        }
        return res;
    }
}
