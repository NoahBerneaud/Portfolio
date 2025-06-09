package vue.Command;

import modele.CollectionForme;
import modele.Rectangle;

public class CreationRectangleCommand implements OperationCommand {

    private int x;
    private int y;
    private int width;
    private int height;
    private CollectionForme collectionForme;

    private Rectangle r;

    public CreationRectangleCommand(int x, int y, int width, int height, CollectionForme collectionForme) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.collectionForme = collectionForme;
        this.r = null;
    }

    @Override
    public void operate() {
        Rectangle r = new Rectangle(x, y, width, height);
        this.r = r;
        this.collectionForme.add(r);
    }

    @Override
    public void compensate() {
        this.collectionForme.remove(this.r);
    }

    @Override
    public Object getObjet() {
        return this.r;
    }

}
