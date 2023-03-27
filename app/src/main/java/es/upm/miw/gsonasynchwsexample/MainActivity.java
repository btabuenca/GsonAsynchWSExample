package es.upm.miw.gsonasynchwsexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import es.upm.miw.gsonasynchwsexample.pojo.Cities;

public class MainActivity extends AppCompatActivity {


    private static final String LOG_TAG = "btb";

    // Vallecas
    //static final String URL_RECURSO = "https://api.openweathermap.org/data/2.5/find?lat=40.39354&lon=-3.662&cnt=20&APPID=add7afd148b08ad9e0c06da452f061d5";

    // Cosuenda - Zaragoza 41.36342648251211, -1.2983547468037664
    static final String URL_RECURSO = "https://api.openweathermap.org/data/2.5/find?lat=41.36&lon=-1.298&cnt=5&APPID=add7afd148b08ad9e0c06da452f061d5";

    // Base Area de Torrejón
    //static final String URL_RECURSO = "http://api.openweathermap.org/data/2.5/find?lat=40.475172&lon=-3.461757&cnt=10&APPID=add7afd148b08ad9e0c06da452f061d5";

    ListView lvCitiesList;

    TareaCargarRecurso tarea;
    Cities citiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(LOG_TAG, "URL request : " + URL_RECURSO);

        lvCitiesList = (ListView) findViewById(R.id.lvCities);


        // Asignar url a la lista
        lvCitiesList.setTag(URL_RECURSO);
        tarea = new TareaCargarRecurso();


        try {
            citiesList = tarea.execute(lvCitiesList).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        CustomCityListAdapter adapter =  new CustomCityListAdapter(this, citiesList);
        lvCitiesList.setAdapter(adapter);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tarea != null) tarea.cancel(true);
    }

    /**
     * Carga un recurso en el TextView que recibe como parámetro: execute(TextView textView);
     */
    private class TareaCargarRecurso extends AsyncTask<ListView, Void, Cities> {

        //private TextView tvContenidoTarea = null;
        private ListView lvCitiesTarea = null;

        @Override
        protected void onPreExecute() {
            //tvContenido.setText("Loading...");

        }

        @Override
        protected Cities doInBackground(ListView... listViews) {
            Cities citiesList = new Cities();

            lvCitiesTarea = listViews[0];
            String buffer = null;
            HttpURLConnection con = null;

            try {
                @SuppressWarnings("WrongThread")
                URL mUrl = new URL(lvCitiesTarea.getTag().toString());
                Log.i(LOG_TAG, "URL=" + mUrl.toString());

                // Establecer conexión remota
                con = (HttpURLConnection) mUrl.openConnection();
                BufferedReader fin = new BufferedReader(
                        new InputStreamReader(
                                con.getInputStream()
                        )
                );


                // Deserializando el JSON via librería gson
                Gson gson = new Gson();
                citiesList = gson.fromJson(fin, Cities.class);


                int i = 0;
                float avg = 0;
                for ( i = 0; i < citiesList.getList().size(); i++) {
                    avg+=citiesList.getList().get(i).getMain().getHumidity();
                }

                Utils u = new Utils();
                avg = avg/i;
                int a = Math.round(avg);
                buffer = citiesList.getCount()+" cities returned with average humidity ["+avg+"] "+u.getHumidityMetaphor(a);
                Log.i(LOG_TAG,  buffer);

                fin.close();
                Log.i(LOG_TAG, "Data received");
            } catch (Exception e) {
                Log.e("ERROR", "Error loading data");
            } finally {
                if (con != null) con.disconnect();
            }

            return citiesList;
        }

        @Override
        protected void onPostExecute(Cities result) {
            super.onPostExecute(result);
        }

    } // TareaCargarRecurso


}
