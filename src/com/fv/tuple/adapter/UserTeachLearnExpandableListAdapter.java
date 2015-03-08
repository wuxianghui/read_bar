package com.fv.tuple.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.fv.tuple.R;
import com.fv.tuple.TupleApplication;

public class UserTeachLearnExpandableListAdapter extends BaseExpandableListAdapter{

	   // Sample data set.  children[i] contains the children (String[]) for groups[i].
    private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names" };
    private String[][] children = {
            { "Arnold", "Barry", "Chuck", "David" },
            { "Ace", "Bandit", "Cha-Cha", "Deuce" },
            { "Fluffy", "Snuggles" },
            { "Goldy", "Bubbles" }
    };
    
    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return children[groupPosition].length;
    }
    
    
    private View MakeGroupItem()  
    {  
		LayoutInflater inflater = 
		(LayoutInflater)TupleApplication.getContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflateLayout  = inflater.inflate(R.layout.item_list_user_teach_learn_group,null);   
        return  inflateLayout;  
    }  
    
    private View MakeChildItem()  
    {  
		LayoutInflater inflater = 
		(LayoutInflater) TupleApplication.getContext() .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View inflateLayout  = inflater.inflate(R.layout.item_list_user_teach_learn_child,null);   
        return  inflateLayout;  
    }  
    
    
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
    	View v = MakeChildItem();

    	TextView vv=(TextView)v.findViewById(R.id.item_name);
    	vv.setText(children[groupPosition][childPosition]);
    	
        return v;
    }

    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    public int getGroupCount() {
        return groups.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
    	View v = MakeGroupItem();
    	TextView vv=(TextView)v.findViewById(R.id.item_group_name);
    	vv.setText(groups[groupPosition]);
        return v;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}
