package vue.Command;

import modele.Cercle;
import modele.CollectionForme;

public class CreationCercleCommand implements OperationCommand {

        private int x;
        private int y;
        private int rayon;
        private CollectionForme collectionForme;

        private Cercle c;

        public CreationCercleCommand(int x, int y, int rayon, CollectionForme collectionForme) {
            this.x = x;
            this.y = y;
            this.rayon = rayon;
            this.collectionForme = collectionForme;
            this.c = null;
        }

        @Override
        public void operate() {
            Cercle c = new Cercle(x, y, rayon);
            this.c = c;
            this.collectionForme.add(c);
        }

        @Override
        public void compensate() {
            this.collectionForme.remove(this.c);
        }

        @Override
        public Object getObjet() {
            return this.c;
        }

}
