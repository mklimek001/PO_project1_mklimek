public class Grass {
    Vector2d position;

    Grass(Vector2d pos){
        this.position = pos;
    }

    boolean isAt(Vector2d location){
        return (this.position.x == location.x && this.position.y == location.y);
    }

    public Vector2d getPosition(){
        return this.position;
    }

}
