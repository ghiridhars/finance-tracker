package app.personal.parser;

import java.util.Map;

public class ParserProfile {
    // map of region name -> [x,y,width,height]
    private Map<String, double[]> regions;

    public Map<String, double[]> getRegions() {
        return regions;
    }

    public void setRegions(Map<String, double[]> regions) {
        this.regions = regions;
    }
}
