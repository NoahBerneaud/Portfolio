package vue.Command;

import modele.CollectionForme;
import modele.Ligne;
import modele.Point;

public class CreationLigneCommand implements OperationCommand{

        private Point p1;
        private Point p2;
        private CollectionForme collectionForme;

        private Ligne l;

        public CreationLigneCommand(int x1, int y1, int x2, int y2, CollectionForme collectionForme) {
            this.p1 = new Point(x1,y1);
            this.p2 = new Point(x2,y2);
            this.collectionForme = collectionForme;
            this.l = null;
        }

        @Override
        public void operate() {
            Ligne l = new Ligne(this.p1,this.p2);
            this.l = l;
            this.collectionForme.add(l);
        }

        @Override
        public void compensate() {
            this.collectionForme.remove(this.l);
        }

        @Override
        public Object getObjet() {
            return this.l;
        }
}
