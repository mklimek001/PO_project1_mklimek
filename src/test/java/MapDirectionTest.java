import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MapDirectionTest {
    @Test
    public void nextTest(){
        Assertions.assertTrue(MapDirection.NORTH.next() == MapDirection.NORTH_EAST);
        Assertions.assertTrue(MapDirection.SOUTH_EAST.next() == MapDirection.SOUTH);
        Assertions.assertTrue(MapDirection.WEST.next() == MapDirection.NORTH_WEST);
    }

    @Test
    public void previousTest(){
        Assertions.assertTrue(MapDirection.NORTH.previous() == MapDirection.NORTH_WEST);
        Assertions.assertTrue(MapDirection.SOUTH_EAST.previous() == MapDirection.EAST);
        Assertions.assertTrue(MapDirection.WEST.previous() == MapDirection.SOUTH_WEST);
    }
}
