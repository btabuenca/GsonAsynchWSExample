package es.upm.miw.gsonasynchwsexample.pojo;

public class City {

    int id;
    String cityName;



    public City(int i, String cityName) {
        super();
        this.id = i;
        this.cityName = cityName;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCityName() {
        return cityName;
    }
    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

}
