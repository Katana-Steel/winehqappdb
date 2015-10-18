package net.alchemiestick.katana.winehqappdb;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by rene on 5/7/15.
 */
class ApplicationList extends ArrayAdapter<Application> {

    private LayoutInflater inflater;
    private Context cx;
    private TextView tv;
    private ListView lv;
    private Dialog MyDialog;

    private int initialized = 0;

    public ApplicationList(Context cx)
    {
        super(cx, R.layout.list_item);
        this.cx = cx;
        this.inflater = (LayoutInflater) cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private View.OnClickListener dismissDlg = new View.OnClickListener() {
        public void onClick(View v) {
            Dialog d = makeDialog();
            d.dismiss();
        }
    };

    public Dialog makeDialog() {
        if (this.MyDialog == null){
            this.MyDialog = new Dialog(this.cx,android.R.style.Theme_Dialog);
            this.MyDialog.setContentView(R.layout.web_dlg);
            this.tv = (TextView)this.MyDialog.findViewById(R.id.win_name);
            this.tv.setOnClickListener(dismissDlg);
            this.lv = (ListView)this.MyDialog.findViewById(R.id.win_versions);
            this.lv.setOnClickListener(dismissDlg);
            this.MyDialog.setOnShowListener(null);
        }
        return this.MyDialog;
    }

    public void FillInDialog(String title, VersionAdapter va) {
        this.makeDialog();
        this.tv.setText(title);
        this.lv.setAdapter(va);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View ret = convertView;
        try {
            Application a = this.getItem(position);
            a.setMyView(this.inflater, parent);
            ret = a.getMyView();
        }
        catch(Exception e)
        {}
        return ret;
    }

    @Override
    public void add(Application a) {
        super.add(a);
        if (this.initialized < 4) {
            a.getVersionsAsync();
            this.initialized += 1;
        }
    }

    @Override
    public void clear() {

        super.clear();
        this.initialized = 0;
    }
}
