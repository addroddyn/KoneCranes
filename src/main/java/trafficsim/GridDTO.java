package trafficsim;

import java.util.ArrayList;

public class GridDTO {
    public int rowCount = 0;
    public int originRow = 0;
    public int originColumn = 0;

    public ArrayList<ArrayList<GridLocationDTO>> newLocations = new ArrayList<>();
    public ArrayList<ArrayList<GridLocationDTO>> oldLocations = new ArrayList<>();
}
