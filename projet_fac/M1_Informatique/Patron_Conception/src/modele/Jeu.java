package modele;

import java.util.ArrayList;

public class Jeu{

    CollectionForme collectionForme;

    public static int formeLimite = 4;
    ArrayList<Point> listPoint;

    public int width;
    public int height;

    public boolean isGame;

    public Jeu(CollectionForme collectionForme, ArrayList<Point> listPoint, int width, int height) {
        this.collectionForme = collectionForme;
        this.listPoint = listPoint;
        this.width = width;
        this.height = height;
        this.isGame = false;
    }

    public boolean isOver(){
        return this.collectionForme.getSize() >= formeLimite;
    }

    public Float getScore(){
        Double AireForme = 0.0;
       for(int i = 0; i< this.collectionForme.getSize(); i++){
              AireForme += this.collectionForme.getForme(i).getAire();
       }
         return (float)(1000/(AireForme/(width*height)));
    }

    public String result(){
        if (this.isOver()){
            for(Point p : listPoint){
                if(!this.collectionForme.contains(p)){
                    this.clearGame();
                    this.stopGame();
                    return "Game Over ";
                }
            }
            float score = this.getScore();
            this.clearGame();
            this.stopGame();
            return "Game Win ! Voici votre score " + Math.round(score*100.0)/100.0;
        }
        return null;
    }

    public void startGame(){
        this.clearGame();
        this.isGame = true;
    }

    public void stopGame(){
        this.isGame = false;
    }

    public void clearGame(){
        // On crée une liste de forme pour pouvoir supprimer les formes de la collection
        ArrayList<Forme> listDelete = new ArrayList<Forme>();
        for(int i = 0; i< this.collectionForme.getSize(); i++){
            listDelete.add(this.collectionForme.getForme(i));
        }
        // On supprime les formes de la collection
        for(Forme f : listDelete){
            this.collectionForme.remove(f);
        }
    }
    public void resetGame(){
        this.clearGame();
        this.isGame = false;
    }

    public boolean isGame() {
        return this.isGame;
    }
}
