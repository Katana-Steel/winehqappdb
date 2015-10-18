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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.alchemiestick.katana.winehqappdb.*;
import net.alchemiestick.katana.winehqappdb.SearchView;

class Version
{
    String level; // from Platinum to Garbage
    String version;
    String date_tested;
    String wine_version;

    private View rowView;

    public Version(String[] vstr)
    {
        this.version = "";
        this.level = "";
        this.date_tested = "";
        this.wine_version = "";
        if (vstr.length == 4) {
            this.version = vstr[0];
            this.level = vstr[2];
            this.date_tested = vstr[1];
            this.wine_version = vstr[3];
        }
    }

    public View getRowView() {
        return rowView;
    }

    public void setRowView(LayoutInflater inflater, ViewGroup parent) {
        if (this.rowView == null) {
            this.rowView = inflater.inflate(R.layout.verlist_item, parent, false);
            TextView textView = (TextView) this.rowView.findViewById(R.id.tversion);
            textView.setText(this.version);
            textView = (TextView) this.rowView.findViewById(R.id.wine_version);
            textView.setText(this.wine_version);
            textView = (TextView) this.rowView.findViewById(R.id.tdate);
            textView.setText(this.date_tested);
            textView = (TextView) this.rowView.findViewById(R.id.tcls);
            textView.setText(this.level);
        }
    }
}
