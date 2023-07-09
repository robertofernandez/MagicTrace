package studio.itpex.magictrace.calculations;

import ar.com.sodhium.commons.img.colors.map.ColorMap;

public class SetBaseMap implements FrameCalculation {
    private String originMapName;

    public SetBaseMap(String originMapName) {
        this.originMapName = originMapName;
    }
    
    @Override
    public void calculate(CalculatedMaps maps) throws Exception {
        if(!maps.getAllMaps().containsKey(originMapName)) {
            throw new Exception("No map with name " + originMapName);
        }
        ColorMap originMap = maps.getAllMaps().get(originMapName);
        maps.getAllMaps().put("base-map", originMap);
    }

}
