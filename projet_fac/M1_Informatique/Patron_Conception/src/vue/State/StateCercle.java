package vue.State;

import modele.Cercle;
import modele.CollectionForme;
import vue.Command.CommandHandler;
import vue.Command.CreationCercleCommand;
import vue.Command.OperationCommand;

import java.awt.*;

public class StateCercle implements StateForme {

    public int x1;
    public int y1;
    public int x2;
    public int y2;

    public Cercle c;

    public CollectionForme collectionForme;

    public CommandHandler commandHandler;


    public StateCercle(CollectionForme collectionForme, CommandHandler commandHandler){
        this.collectionForme = collectionForme;
        this.commandHandler = commandHandler;
    }

    public void mouseClicked(int x, int y) {

    }

    public void mousePressed(int x, int y) {
        this.x1 = x;
        this.y1 = y;
    }

    public void mouseReleased(int x, int y) {
        this.x2 = x;
        this.y2 = y;
        this.factory();
        // Cette ligne sert à indiquer à la méthode draw de ne pas dessiner la ligne après la création du cercle
        this.x1 = -1;
    }

    public void factory() {
        OperationCommand creationCercleCommand = new CreationCercleCommand(this.x1, this.y1,(int) (Math.sqrt(Math.pow(this.x2 - this.x1, 2) + Math.pow(this.y2 - this.y1, 2))), this.collectionForme);
        this.commandHandler.handle(creationCercleCommand);
    }

    public void draw(Graphics g) {
        if(this.x1>=0) {
            g.setColor(Color.RED);
            int r = (int) (Math.sqrt(Math.pow(this.x2 - this.x1, 2) + Math.pow(this.y2 - this.y1, 2)));
            g.drawOval(this.x1 - r, this.y1 - r, 2 * r, 2 * r);
        }
    }

    public void mouseDragged(int x, int y) {
        this.x2 = x;
        this.y2 = y;

    }

    public void mouseMoved(int x, int y) {
    }
}
