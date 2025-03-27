import java.util.Random;

public class Grid {

        private  int rowLength;
        private  int columnLength;

        public  int GetRowStart() { return 0;}
        public  int GetColumnStart() {return 0;}
        public  int GetColumnEnd() { return columnLength -1;}
        public int GetRowEnd() {return rowLength -1;}

    public Grid(int gridSize){
            rowLength = gridSize;
            columnLength = gridSize;
    }

    public GridLocation getRandomLocation(){
            Random rnd = new Random();
            return new GridLocation(rnd.nextInt(rowLength), rnd.nextInt(columnLength));
    }

}

