import java.util.Objects;

class Vector2d {
    public int x;
    public int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    boolean precedes(Vector2d other){
        return (other.x >= this.x && other.y >= this.y);
    }

    boolean follows(Vector2d other){
        return (other.x <= this.x && other.y <= this.y);
    }

    Vector2d upperRight(Vector2d other){
        return new Vector2d(Math.max(this.x, other.x),Math.max(this.y, other.y));
    }

    Vector2d lowerLeft(Vector2d other){
        return new Vector2d(Math.min(this.x, other.x),Math.min(this.y, other.y));
    }

    Vector2d add(Vector2d other){
        return new Vector2d(this.x + other.x,this.y + other.y);
    }

    Vector2d substract(Vector2d other){
        return new Vector2d(this.x - other.x,this.y - other.y);
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;
        return (this.x == that.x && this.y == that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }

    Vector2d opposite(){
        return new Vector2d(-this.x,-this.y);
    }
}



