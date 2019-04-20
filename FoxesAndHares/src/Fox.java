import javax.vecmath.Vector2d;
import java.awt.*;

public class Fox extends Animal{
    @Override
    protected void setup() {
        super.setup();
        System.out.println("The fox has been created!");
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.red);

        Vector2d screenPos = Camera.worldToScreenPoint(position);
        g.fillOval((int) screenPos.x, (int) screenPos.y, 10, 10);
    }
}