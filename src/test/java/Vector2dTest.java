import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Vector2dTest {
    public Vector2d test1 = new Vector2d(1,2);
    public Vector2d test2 = new Vector2d(2,1);
    public Vector2d test3 = new Vector2d(2,3);

    @Test
    public void equalsTest(){
        Assertions.assertFalse(test1.equals(test2));
        Assertions.assertTrue(test1.equals(test1));
    }

    @Test
    public void toStringTest(){
        Assertions.assertEquals(test1.toString(),"(1, 2)");
        Assertions.assertEquals(test2.toString(),"(2, 1)");
    }

    @Test
    public void predcedesTest(){
        Assertions.assertFalse(test1.precedes(test2));
        Assertions.assertTrue(test1.precedes(test3));
        Assertions.assertTrue(test1.precedes(test1));
    }

    @Test
    public void upperRightTest(){
        Vector2d expresult = new Vector2d(2,2);
        Assertions.assertEquals(test1.upperRight(test2),expresult);
    }

    @Test
    public void lowerLeftTest(){
        Vector2d expresult = new Vector2d(1,1);
        Assertions.assertEquals(test1.lowerLeft(test2),expresult);
    }

    @Test
    public void addTest(){
        Vector2d expresult = new Vector2d(3,3);
        Assertions.assertEquals(test1.add(test2),expresult);
    }

    @Test
    public void substractTest(){
        Vector2d expresult = new Vector2d(-1,1);
        Assertions.assertEquals(test1.substract(test2),expresult);
    }

    @Test
    public void oppositeTest(){
        Vector2d expresult1 = new Vector2d(-1,-2);
        Assertions.assertEquals(test1.opposite(),expresult1);
        Vector2d expresult2 = new Vector2d(-2,-1);
        Assertions.assertEquals(test2.opposite(),expresult2);

    }

}
