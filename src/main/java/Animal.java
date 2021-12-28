import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Animal {
    MapDirection orientation;
    Vector2d position;
    Integer energy;
    List<Integer> gens;
    int age;
    int numOfChilds;

    Animal(Vector2d position, Integer energy, List<Integer> gens){
        this.position = position;
        this.energy = energy;
        this.gens = gens;
        this.orientation = RandomNumbers.randomMapDirection();
        this.age = 0;
        this.numOfChilds = 0;
    }

    static Animal startingAnimal(Vector2d position){
        int energy = App.startEnergy;
        List<Integer> newGens = new ArrayList<>();
        for(int i = 0; i < 32; i++){
            int newRandomNum = RandomNumbers.randomEight();
            newGens.add(newRandomNum);
        }
        Collections.sort(newGens);
        return new Animal(position,energy,newGens);
    }


    boolean isAt(Vector2d location){
        return (this.position.x == location.x && this.position.y == location.y);
    }

    boolean isDirected(MapDirection direction){
        return (this.orientation == direction);
    }

    Vector2d forwardPosition(){
        return this.position.add(this.orientation.toUnitVector());
    }

    Vector2d backwardPosition(){
        return this.position.substract(this.orientation.toUnitVector());
    }

    void rotate(int rotation){
        MapDirection processedMapDirection = this.orientation;

        if(rotation != 4 && rotation != 0) {
            if (rotation <= 3) {
                for (int i = 0; i < rotation; i++) {
                    processedMapDirection = processedMapDirection.next();
                }
            } else {
                rotation = 8 - rotation;
                for (int i = 0; i < rotation; i++) {
                    processedMapDirection = processedMapDirection.previous();
                }
            }
        }
        this.orientation = processedMapDirection;

    }

    Vector2d proposedNewPosition(int moveNum){
        Vector2d proposedPosition = this.position;
        if(moveNum == 0){proposedPosition = forwardPosition();}
        if(moveNum == 4){proposedPosition = backwardPosition();}
        return proposedPosition;
    }

    void consumption(int nrgy){
        this.energy += nrgy;
    }

    void burning(int nrgy){
        this.energy -= nrgy;
    }

    Animal multiplication(Animal father) {
        List<Integer> childGens = new ArrayList<>();
        double proportion = (double)this.energy / (double) (father.energy + this.energy);
        int gensDivide = (int) Math.ceil(proportion * 32);
        int leftOrRight = RandomNumbers.randomTwo();
        if ((gensDivide >= 16 && leftOrRight == 0) || (gensDivide < 16 && leftOrRight == 1)) {
            for (int i = 0; i <= gensDivide; i++) {
                childGens.add(this.gens.get(i));
            }
            for (int i = gensDivide; i < 32; i++) {
                childGens.add(father.gens.get(i));
            }
        } else {
            for (int i = 0; i < gensDivide; i++) {
                childGens.add(father.gens.get(i));
            }
            for (int i = gensDivide; i < 32; i++) {
                childGens.add(this.gens.get(i));
            }
        }

        Collections.sort(childGens);
        int childNrgy = (int) (Math.floor(0.25 * this.energy) + Math.floor(0.25 * father.energy));
        this.energy -= (int) Math.floor(0.25 * this.energy);
        father.energy -= (int) Math.floor(0.25 * father.energy);

        return new Animal(this.position, childNrgy, childGens);
    }


}
