public class WallWorldMap extends AbstractWorldMap{
    WallWorldMap(int rangeX, int rangeY, float jungleRatio, int startAnimals) {
        super(rangeX, rangeY, jungleRatio, startAnimals);
    }


    void moving(){
        for (Animal animal: this.animalsList){
            animal.age += 1;
            int randomNum = animal.gens.get(RandomNumbers.randomThirtyTwo());
            if(randomNum == 0 || randomNum == 4){
                Vector2d newPosition = animal.proposedNewPosition(randomNum);
                if (newPosition.x < this.rangeX && newPosition.x >= 0 && newPosition.y < this.rangeY && newPosition.y >= 0){
                    animal.position = newPosition;
                    animal.burning(App.moveEnergy);
                }
            } else{
                animal.rotate(randomNum);
            }
        }
    }

}
