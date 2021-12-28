import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

abstract class AbstractWorldMap {
    int rangeY;
    int rangeX;
    int jungleStartY;
    int jungleStartX;
    int jungleRangeY;
    int jungleRangeX;
    List<Animal> animalsList;
    List<Grass> grassList;
    int magicFiveAppends = 3;

    AbstractWorldMap(int rangeX, int rangeY, float jungleRatio, int startAnimals){
        this.rangeX = rangeX;
        this.rangeY = rangeY;
        this.jungleRangeX = (int) Math.floor(jungleRatio * this.rangeX);
        this.jungleRangeY = (int) Math.floor(jungleRatio * this.rangeY);
        this.jungleStartX = (int) Math.floor((double) (this.rangeX - this.jungleRangeX)/2);
        this.jungleStartY = (int) Math.floor((double) (this.rangeY - this.jungleRangeY)/2);
        this.animalsList = new ArrayList<>();
        this.grassList = new ArrayList<>();

        int startAnimalsPlaceLen = (int) Math.ceil(Math.sqrt(startAnimals)) ;
        int startAnimalsX = (int) Math.floor((double) (this.rangeX - startAnimalsPlaceLen)/2);
        int startAnimalsY = (int) Math.floor((double) (this.rangeY - startAnimalsPlaceLen)/2);
        for(int i = 0; i<startAnimals; i++){
            int xpos = (int) Math.floor(Math.random()*startAnimalsPlaceLen) + startAnimalsX;
            int ypos = (int) Math.floor(Math.random()*startAnimalsPlaceLen) + startAnimalsY;
            Vector2d proposedPosition = new Vector2d(xpos,ypos);
            if(!this.isOccupied(proposedPosition)){
                animalsList.add(Animal.startingAnimal(proposedPosition));
            }else{
                i-=1;
            }
        }
    }


    public boolean isOccupied(Vector2d position) {
        for(Animal existing: this.animalsList){
            if (existing.isAt(position)) return true;
        }
        return false;
    }

    public boolean isOvergrown(Vector2d position) {
        for(Grass existing: this.grassList){
            if (existing.isAt(position)) return true;
        }
        return false;
    }

    public ArrayList<Animal> atPosition(Vector2d position){
        ArrayList<Animal> animalsAtPosition = new ArrayList<Animal>();
        for(Animal existing: this.animalsList){
            if (existing.isAt(position)){
                animalsAtPosition.add(existing);
            }
        }
        animalsAtPosition.sort(nrgyComparator);
        return animalsAtPosition;
    }


    void feeding(){
        ArrayList<Grass> eatenGrass = new ArrayList<>();
        for(Grass grass: this.grassList){
            if(this.isOccupied(grass.position)) {
                ArrayList<Animal> animsNearGrass = this.atPosition(grass.position);
                Animal strongestAnimal = animsNearGrass.get(0);
                int strongestAnimalNrgy = strongestAnimal.energy;
                ArrayList<Animal> feededAnimals = new ArrayList<Animal>();
                for(Animal animal: animsNearGrass){
                    if(animal.energy == strongestAnimalNrgy){
                        feededAnimals.add(animal);
                    }
                }

                int numOfGrassPieces = feededAnimals.size();
                int nrgyPortion = (int) Math.floor(App.plantEnergy/(double) numOfGrassPieces);

                for(Animal feedAnimal: feededAnimals){
                    feedAnimal.consumption(nrgyPortion);
                }
                eatenGrass.add(grass);
            }
        }
        this.grassList.removeAll(eatenGrass);
    }


    void procreation(){
        ArrayList<Vector2d> checkedPositions = new ArrayList<>();
        ArrayList<Animal> newlyBornAnimals = new ArrayList<>();

        for(Animal animal: this.animalsList) {
            Vector2d checkingPos = animal.position;
            if (!checkedPositions.contains(checkingPos)) {
                ArrayList<Animal> potentialParents = atPosition(checkingPos);

                if (potentialParents.size() > 1) {
                    Animal mother = potentialParents.get(0);
                    Animal father = potentialParents.get(1);
                    if (mother.energy > 0.5*App.startEnergy && father.energy > 0.5*App.startEnergy) {
                        mother.numOfChilds += 1;
                        father.numOfChilds += 1;
                        Animal child = mother.multiplication(father);
                        newlyBornAnimals.add(child);
                    }
                }

                checkedPositions.add(checkingPos);
            }
        }

        this.animalsList.addAll(newlyBornAnimals);
    }


    void dethDelete(){
        this.animalsList.removeIf(animal -> animal.energy <= 0);
    }

    void magicFiveEvolution(){
        if(this.animalsList.size() == 5 && this.magicFiveAppends > 0){
            for(Animal animal: this.animalsList){
                Vector2d copyAnimPos = generateNewPosition();
                Animal copyAnim = new Animal(copyAnimPos, App.startEnergy, animal.gens);
                this.animalsList.add(copyAnim);
            }
            this.magicFiveAppends -= 1;
        }
    }

    void growGrass(){
        Vector2d potentialJungleGrassField = generateGrassInJunglePosition();
        if (!potentialJungleGrassField.equals(new Vector2d(-1,-1))) this.grassList.add(new Grass(potentialJungleGrassField));

        Vector2d potentialSteppeGrassField = generateGrassOutsideJunglePosition();
        if (!potentialSteppeGrassField.equals(new Vector2d(-1,-1))) this.grassList.add(new Grass(potentialSteppeGrassField));
    }

    Vector2d generateGrassInJunglePosition(){
        int xpos = (int) Math.floor(Math.random()*this.jungleRangeX);
        int ypos = (int) Math.floor(Math.random()*this.jungleRangeY);
        Vector2d potentialPosition = new Vector2d(xpos,ypos);
        ArrayList<Vector2d> checked = new ArrayList<>();
        checked.add(potentialPosition);
        while ((checked.contains(potentialPosition) || this.isOccupied(potentialPosition) || this.isOvergrown(potentialPosition)) && checked.size() < jungleRangeY*jungleRangeX){
            int alternativeXpos = (int) Math.floor(Math.random()*this.jungleRangeX) + this.jungleStartX;
            int alternativeYpos = (int) Math.floor(Math.random()*this.jungleRangeY) + this.jungleStartY;
            potentialPosition = new Vector2d(alternativeXpos,alternativeYpos);
            checked.add(potentialPosition);
        }
        if(checked.size() > jungleRangeY*jungleRangeX) potentialPosition = new Vector2d(-1,-1);
        return potentialPosition;
    }

    Vector2d generateGrassOutsideJunglePosition(){
        int xpos = (int) Math.floor(Math.random()*this.rangeX);
        int ypos = (int) Math.floor(Math.random()*this.rangeY);
        Vector2d potentialPosition = new Vector2d(xpos,ypos);
        ArrayList<Vector2d> checked = new ArrayList<>();
        checked.add(potentialPosition);
        int allPosibilities = this.rangeX * this.rangeY;
        while ((checked.contains(potentialPosition) || this.isOccupied(potentialPosition) || this.isOvergrown(potentialPosition)) && checked.size() < allPosibilities && this.outsideTheJungle(potentialPosition)){
            int alternativeXpos = (int) Math.floor(Math.random()*this.rangeX);
            int alternativeYpos = (int) Math.floor(Math.random()*this.rangeY);
            potentialPosition = new Vector2d(alternativeXpos,alternativeYpos);
            checked.add(potentialPosition);
        }
        if(checked.size() > allPosibilities) potentialPosition = new Vector2d(-1,-1);
        return potentialPosition;
    }

    boolean outsideTheJungle(Vector2d position){
        return (position.x < this.jungleRangeX && position.x > this.jungleStartX + this.jungleRangeX && position.y < this.jungleRangeY && position.y > this.jungleStartY + this.jungleRangeY);
    }


    Vector2d generateNewPosition(){
        int xpos = (int) Math.floor(Math.random()*this.rangeX);
        int ypos = (int) Math.floor(Math.random()*this.rangeY);
        Vector2d potentialPosition = new Vector2d(xpos,ypos);
        while (this.isOccupied(potentialPosition)){
            int alternativeXpos = (int) Math.floor(Math.random()*this.rangeX);
            int alternativeYpos = (int) Math.floor(Math.random()*this.rangeY);
            potentialPosition = new Vector2d(alternativeXpos,alternativeYpos);
        }
        return potentialPosition;
    }




    Comparator<Animal> nrgyComparator = new Comparator<Animal>() {
        @Override
        public int compare(Animal animal1, Animal animal2) {
            return animal2.energy.compareTo(animal1.energy);
            /*if (animal1.energy < animal2.energy) {
                return 1;
            } else if (animal1.energy > animal2.energy) {
                return -1;
            } else {
                return 0;
            }*/
        }
    };



}
