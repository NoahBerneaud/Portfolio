package vue.State;

import modele.CollectionForme;
import modele.Forme;
import vue.*;
import vue.Command.CommandHandler;
import vue.Command.DeleteCommand;
import vue.Command.OperationCommand;
import vue.VueForme.VueForme;

import java.awt.*;
import java.util.HashMap;

public class StateDelete implements StateForme {

    public CollectionForme collectionForme;
    public HashMap<Forme, VueForme> vueFormesDelete;

    public HashMap<Forme,VueForme> vueFormes;

    public PanelDessin panelDessin;

    public CommandHandler commandHandler;



    public StateDelete(CollectionForme collectionForme, PanelDessin panelDessin,CommandHandler commandHandler){
        this.collectionForme = collectionForme;
        this.vueFormes = panelDessin.getVueForme();
        this.vueFormesDelete = new HashMap<Forme,VueForme>();
        this.panelDessin = panelDessin;
        this.commandHandler = commandHandler;
    }

    @Override
    public void mouseClicked(int x, int y) {
        for(Forme f : this.vueFormesDelete.keySet()){
            OperationCommand suppressionFormeCommand = new DeleteCommand(f,this.panelDessin,this.collectionForme);
            this.commandHandler.handle(suppressionFormeCommand);
        }
        this.vueFormesDelete.clear();


    }

    @Override
    public void mousePressed(int x, int y) {

    }

    @Override
    public void mouseReleased(int x, int y) {

    }

    @Override
    public void draw(Graphics g) {
        for(VueForme vf : this.vueFormesDelete.values()){
            vf.draw(g);
        }

    }

    @Override
    public void mouseDragged(int x, int y) {
    }

    @Override
    public void mouseMoved(int x, int y) {
        this.vueFormesDelete.clear();
        for (Forme f : this.vueFormes.keySet()) {
            // On vérifie si la souris est dans la forme
            if (f.contains(x, y)) {
                // Si oui, on la sélectionne
                this.vueFormes.get(f).setSelected(true);
                this.vueFormesDelete.put(f,this.vueFormes.get(f));
            }
            else{
                // Sinon, on la déselectionne
                this.vueFormes.get(f).setSelected(false);
            }
        }



    }
}
