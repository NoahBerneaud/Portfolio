package vue.State;

import modele.CollectionForme;
import modele.Ligne;
import modele.Point;
import vue.Command.CommandHandler;
import vue.Command.CreationCercleCommand;
import vue.Command.CreationLigneCommand;
import vue.Command.OperationCommand;

import java.awt.*;

public class StateLigne implements StateForme {

        public int x1;
        public int y1;
        public int x2;
        public int y2;

        public CollectionForme collectionForme;

        public CommandHandler commandHandler;

        public StateLigne(CollectionForme collectionForme, CommandHandler commandHandler){
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
            OperationCommand creationLigneCommand = new CreationLigneCommand(this.x1,this.y1,this.x2,this.y2,this.collectionForme);
            this.commandHandler.handle(creationLigneCommand);
        }

        public void draw(Graphics g) {
            if(this.x1>=0) {
                g.setColor(Color.RED);
                g.drawLine(this.x1, this.y1, this.x2, this.y2);
            }
        }

        public void mouseDragged(int x, int y) {
            this.x2 = x;
            this.y2 = y;
        }

        public void mouseMoved(int x, int y) {
        }


}
