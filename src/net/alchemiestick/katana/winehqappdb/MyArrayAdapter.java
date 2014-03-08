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

import android.content.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;

    public class MyArrayAdapter extends ArrayAdapter<str_link>
    {
        private SearchView main;
        int cur;
        
        public MyArrayAdapter(SearchView app)
        {
            super(app, R.layout.list_item);
            this.main = app;
            this.cur = -1;
        }
        
        // getting data about the picked app
        private OnClickListener getAppData = new OnClickListener() {
            public void onClick(View v) {
                try {
                    TextView tv = (TextView)v;
                    setCurrent(tv.getText().toString());
                    main.showDialog(main.WINAPP_DLG);
                }
                catch(Exception e)
                { }
            }
        };
        
        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = (LayoutInflater) this.main.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_item, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.list_label);
            try {
                textView.setText(this.getItem(position).str);
                textView.setOnClickListener(this.getAppData);
            }
            catch (Exception e)
            {
                textView.setText("No more Elements");
            }
            return rowView;
        }
        
        public void setCurrent(String title)
        {
            str_link test = new str_link(title,"");
            str_link c;
            for(int i = 0; i < this.getCount(); i++) {
                c = this.getItem(i);
                if(c.equals(test)) {
                    this.cur = this.getPosition(c);
                    return;
                }
            }
            this.getItem(-1);
        }
        
        public str_link getCurrent()
        {
            try {
                return this.getItem(cur);
            }
            catch(Exception e)
            {
                return new str_link("Link Fail","");
            }
        }
        
    };
