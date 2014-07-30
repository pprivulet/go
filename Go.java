public class Go {
    int x;
    int y;
    int color;
    int status;

    public Go(int x, int y, int color){
        this.x = x;
        this.y = y;
        this.color = color;
        this.status = 1; //live
    }

    public void setStatus(int s){
        this.status = s;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public int getStatus() {
        return this.status;
    }
    
    public int getColor() {
        return this.color;
    }
}
