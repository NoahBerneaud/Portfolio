package vue.State;

import modele.CollectionForme;
import modele.Rectangle;
import modele.Point;
import vue.Command.CommandHandler;
import vue.Command.CreationRectangleCommand;
import vue.Command.OperationCommand;

import java.awt.*;

public class StateRectangle implements StateForme {

        public Point p1;
        public Point p2;

        public Rectangle r;

        public CollectionForme collectionForme;

        public CommandHandler commandHandler;

        public StateRectangle(CollectionForme collectionForme, CommandHandler commandHandler) {
            this.collectionForme = collectionForme;
            this.commandHandler = commandHandler;
            this.p1 = new Point(-1, -1);
            this.p2 = new Point(-1, -1);
        }

        public void mouseClicked(int x, int y) {

        }

        public void mousePressed(int x, int y) {
            this.p1 = new Point(x, y);
        }

        public void mouseReleased(int x, int y) {
            this.p2 = new Point(x, y);
            this.factory();
            // On remet les points à -1 pour ne pas dessiner le rectangle
            this.p1.setX(-1);
            this.p1.setY(-1);

        }

        public void factory() {
            int width = this.p2.getX() - this.p1.getX();
            int height = this.p2.getY() - this.p1.getY();
            // On vérifie que la largeur et la hauteur sont positives
            if(width<0) {
                // Si la largeur est négative, on inverse la coordonnée x du point de départ avec celle de la fin
                width = -width;
                this.p1.setX(this.p2.getX());
            }
            if(height<0) {
                // Si la hauteur est négative, on inverse la coordonnée y du point de départ avec celle de la fin
                height = -height;
                this.p1.setY(this.p2.getY());
            }
            OperationCommand command = new CreationRectangleCommand(this.p1.getX(), this.p1.getY(), width, height, this.collectionForme);
            this.commandHandler.handle(command);
            this.r = null;
        }

        public void draw(Graphics g) {
            int xDeb = this.p1.getX();
            int yDeb = this.p1.getY();
            // On vérifie que le point de départ est bien défini
            if(this.p1.getX() >= 0){
                g.setColor(Color.RED);
                int width = this.p2.getX() - this.p1.getX();
                int height = this.p2.getY() - this.p1.getY();
                // On vérifie que la largeur et la hauteur sont positives
                // Même traitement que pour la méthode factory()
                if(width<0) {
                    width = -width;
                    xDeb = this.p2.getX();
                }
                if(height<0) {
                    height = -height;
                    yDeb = this.p2.getY();
                }
                g.drawRect(xDeb, yDeb, width, height);
            }
        }

        public void mouseDragged(int x, int y) {
            this.p2 = new Point(x, y);
        }

        public void mouseMoved(int x, int y) {
        }



}
