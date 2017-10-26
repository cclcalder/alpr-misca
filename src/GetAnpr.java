import com.openalpr.jni.*;
import java.util.ArrayList;

public class GetAnpr {

    public static class Plate {
        public  ArrayList<Results> results;
    }

    public static class Results {
        public double confidence;
        public String code;
    }

    /**
     * Gets codes from openalpr
     * Currently returns top 3 confidence
     * @param imagedata
     * @return
     * @throws AlprException
     */
    public static ArrayList<Plate> Codes(byte[] imagedata)throws AlprException{

        Alpr alpr = new Alpr("eu", "/openalpr.conf", "/runtime_data");
        alpr.setTopN(3);
        alpr.setDefaultRegion("gb");

        ArrayList<Plate> alprResults = new ArrayList<>();
        AlprResults results = alpr.recognize(imagedata);

        for (AlprPlateResult result : results.getPlates())
        {
            Plate plateInfo = new Plate();
            for (AlprPlate plate : result.getTopNPlates()) {

                while(result.getBestPlate().getOverallConfidence() - plate.getOverallConfidence() < 10)
                {
                    Results newCode = new Results();
                    newCode.code = plate.getCharacters();
                    newCode.confidence = plate.getOverallConfidence();
                    plateInfo.results.add(newCode);
                }
                alprResults.add(plateInfo);
            }
        }
        alpr.unload();
        return alprResults;
    }
}
