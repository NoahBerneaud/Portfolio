package vue.Command;

import modele.Forme;
import vue.VueForme.VueForme;

import java.util.HashMap;

public class MoveCommand implements OperationCommand {

    private int x;
    private int y;

    private HashMap<Forme, VueForme> vueFormesMove;

    private boolean newMove;

    public MoveCommand(int x, int y, HashMap<Forme,VueForme> vueFormesMove, boolean newMove) {
        this.x = x;
        this.y = y;
        this.vueFormesMove = vueFormesMove;
        // On indique si c'est un nouveau mouvement ou non
        this.newMove = newMove;
    }


    @Override
    public void operate() {
        for(Forme f : this.vueFormesMove.keySet()){
            f.translate(x,y);
        }

    }

    @Override
    public void compensate() {
        for(Forme f : this.vueFormesMove.keySet()){
            f.translate(-x,-y);
        }
    }

    @Override
    public Object getObjet() {
        return this.vueFormesMove;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }

    public boolean isNewMove(){
        return this.newMove;
    }

}
