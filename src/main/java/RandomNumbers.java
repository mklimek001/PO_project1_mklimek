public class RandomNumbers {
    static int randomEight(){
        return (int)Math.floor(Math.random()*7);
    }

    static int randomThirtyTwo(){
        return (int)Math.floor(Math.random()*31);
    }

    static int randomTwo(){
        return (int)Math.floor(Math.random()*2);
    }

    static MapDirection randomMapDirection(){
        MapDirection currDirection = MapDirection.NORTH;
        int randomNumber = randomEight();
        if (randomNumber <= 4){
            for(int i = 0; i<randomNumber; i++){
                currDirection = currDirection.next();
            }
        }else {
            randomNumber = 8 - randomNumber;
            for(int i = 0; i<randomNumber; i++){
                currDirection = currDirection.previous();
            }
        }
        return currDirection;
    }
}
