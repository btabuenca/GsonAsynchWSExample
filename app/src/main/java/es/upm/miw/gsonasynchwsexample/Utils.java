package es.upm.miw.gsonasynchwsexample;

public class Utils {

    public static final String WELLBEING_GOOD = ":)";
    public static final String WELLBEING_MEDIUM = ":|";
    public static final String WELLBEING_BAD = ":(";

    // ECA 30% - 50%
    public String getHumidityMetaphor(int iPercentage){

        if((iPercentage>30) && (iPercentage<50)){
            return WELLBEING_GOOD;
        }else if (((iPercentage>20) && (iPercentage<30)) || ((iPercentage>50) && (iPercentage<60))){
            return WELLBEING_MEDIUM;
        }else{
            return WELLBEING_BAD;
        }

    }
}
