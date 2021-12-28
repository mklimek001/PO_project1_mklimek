import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AnimalTest {

    @Test
    public void consumptionTest() {
        ArrayList<Integer> testGens = new ArrayList<>();
        Animal testingAnimal = new Animal(new Vector2d(1, 1), 10, testGens);
        testingAnimal.consumption(10);
        Assertions.assertTrue(testingAnimal.energy == 20);
    }
}
