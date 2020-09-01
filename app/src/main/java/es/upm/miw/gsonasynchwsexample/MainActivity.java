package es.upm.miw.gsonasynchwsexample;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

import es.upm.miw.gsonasynchwsexample.pojo.Cities;

public class MainActivity extends AppCompatActivity {


    private static final String LOG_TAG = "MiW";

    // Vallecas
    //static final String URL_RECURSO = "http://api.openweathermap.org/data/2.5/find?lat=40.39354&lon=-3.662&cnt=20&APPID=add7afd148b08ad9e0c06da452f061d5";

    // Base Area de Torrejón
    static final String URL_RECURSO = "http://api.openweathermap.org/data/2.5/find?lat=40.475172&lon=-3.461757&cnt=10&APPID=add7afd148b08ad9e0c06da452f061d5";

    Button botonEnviar;
    ListView lvCitiesList;

    TareaCargarRecurso tarea;
    Cities citiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonEnviar = (Button) findViewById(R.id.btnSubmit);
        lvCitiesList = (ListView) findViewById(R.id.lvCities);

        Log.i(LOG_TAG, "URL=" + URL_RECURSO);

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
                buffer = citiesList.getCount()+" cities returned";


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
