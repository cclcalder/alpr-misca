import com.openalpr.jni.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws AlprException {

     Alpr alpr = new Alpr("eu", "/openalpr.conf", "/runtime_data");
     alpr.setTopN(10);
     alpr.setDefaultRegion("gb");

     Path path = Paths.get("/src","/double.JPG");
     byte[] imagedata = new byte[0];
     try {
      imagedata = Files.readAllBytes(path);
     } catch (IOException e) {
      e.printStackTrace();
     }
     AlprResults results = alpr.recognize(imagedata);

     System.out.format("  %-15s%-8s\n", "Plate Number", "Confidence");
     for (AlprPlateResult result : results.getPlates())
     {
      for (AlprPlate plate : result.getTopNPlates()) {
       if (plate.isMatchesTemplate())
        System.out.print("  * ");
       else
        System.out.print("  - ");
       System.out.format("%-15s%-8f\n", plate.getCharacters(), plate.getOverallConfidence());
      }
     }

     alpr.unload();
    }
}
