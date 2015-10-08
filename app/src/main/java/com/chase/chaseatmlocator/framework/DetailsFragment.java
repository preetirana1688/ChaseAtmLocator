package com.chase.chaseatmlocator.framework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.chase.chaseatmlocator.R;

import org.w3c.dom.Text;

import java.util.List;


public class DetailsFragment extends Fragment {

    private TableLayout driveUphrs;
    private TableLayout lobbyLayout;
    private TableLayout languagesLayout;
    private TextView atm;
    private TextView bank;
    private TextView type;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.details_fragment,container,false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        driveUphrs = (TableLayout) view.findViewById(R.id.driveUphrs);
        lobbyLayout = (TableLayout) view.findViewById(R.id.lobby);
        languagesLayout =(TableLayout) view.findViewById(R.id.languages);
        atm = (TextView) view.findViewById(R.id.atm);
        bank = (TextView) view.findViewById(R.id.bank);
        type = (TextView) view.findViewById(R.id.type);

        atm.setText("ATM:" + " " +  getLocations().get(getPosition()).getAtms());
        bank.setText("Bank:" + " " +  getLocations().get(getPosition()).getBank());
        atm.setText("Type" + " " +  getLocations().get(getPosition()).getType());



       for (String driveUp : getLocations().get(getPosition()).getDriveUpHrs()) {


            TableRow row= new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            TextView tv = new TextView(getActivity());
            tv.setText(driveUp);
            row.addView(tv);
            driveUphrs.addView(row);
        }

        for(String lobby : getLocations().get(getPosition()).getLobbyHrs()) {
            TableRow row= new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            TextView tv = new TextView(getActivity());
            tv.setText(lobby);
            row.addView(tv);
            lobbyLayout.addView(row);
        }

        for(String languages : getLocations().get(getPosition()).getLanguages()) {
            TableRow row= new TableRow(getActivity());
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            TextView tv = new TextView(getActivity());
            tv.setText(languages);
            row.addView(tv);
            languagesLayout.addView(row);
        }


    }

    protected List<Location> getLocations() {
        return getResponse().getLocations();
    }

    protected int getPosition() {
        return Singelton.getInstance().getPosition();
    }

    protected Response getResponse() {
        return Singelton.getInstance().getResponse();
    }
}
