package display;

public class Camera2D {

    public double rotation = 0;

    public double xOffset = 0;
    public double yOffset = 0;

    public double zoom = 1;

    public double sens = 7;

    public double speed;

    public void update() {
        speed = sens / zoom;
    }

}
