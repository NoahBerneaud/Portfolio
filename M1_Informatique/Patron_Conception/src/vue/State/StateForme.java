package vue.State;

import java.awt.*;

public interface StateForme {

    public void mouseClicked(int x, int y);

    public void mousePressed(int x, int y);

    public void mouseReleased(int x, int y);


    public void draw(Graphics g);

    public void mouseDragged(int x, int y);

    public void mouseMoved(int x, int y);

}
