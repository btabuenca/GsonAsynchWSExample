package es.upm.miw.gsonasynchwsexample;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import es.upm.miw.gsonasynchwsexample.pojo.Cities;

public class CustomCityListAdapter extends BaseAdapter {


    private Activity context;
    Cities cities;


    public CustomCityListAdapter(Activity context, Cities cities) {
        this.context = context;
        this.cities =cities;

    }

    public static class ViewHolder
    {
        TextView textViewId;
        TextView textViewCity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row=convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
        if(convertView==null) {
            vh=new ViewHolder();
            row = inflater.inflate(R.layout.row_item, null, true);
            vh.textViewId = (TextView) row.findViewById(R.id.textViewId);
            vh.textViewCity = (TextView) row.findViewById(R.id.textViewCity);

            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }

        // Prams : https://openweathermap.org/current#geo
        vh.textViewCity.setText(cities.getList().get(position).getName());

        // kelvin to celsius
        double dTemp = cities.getList().get(position).getMain().getTemp() - 273.15;

        // The java.lang.Math.round() is used round of the decimal numbers to the nearest value.
        // This method is used to return the closest long to the argument
        double dTempRoundOff = Math.round(dTemp*100)/100;

        // % humidity
        Integer oiHumidPerc = cities.getList().get(position).getMain().getHumidity();
        vh.textViewId.setText("[T:"+ dTempRoundOff+"][H:"+ oiHumidPerc.intValue()+"%]");

        return  row;
    }



    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {

        if (cities.getCount()!=null){
            Integer oI = cities.getCount();
            return oI.intValue();
        }else{
            return 0;
        }
         //if(cities.getCount()<=0)  return 1;
         //return cities.getCount();

    }

}