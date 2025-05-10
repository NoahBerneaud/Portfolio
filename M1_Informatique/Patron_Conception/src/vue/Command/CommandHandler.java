package vue.Command;

import java.util.ArrayList;
import java.util.Stack;

public class CommandHandler {
    public Stack<OperationCommand> stack;
    public ArrayList<OperationCommand> stackRedu;

    public CommandHandler() {
        this.stack = new Stack<OperationCommand>();
        this.stackRedu = new ArrayList<OperationCommand>();
    }

    public void handle(OperationCommand command) {
        command.operate();
        // Si la commande est un move qui n'est pas un nouveau move et que la dernière commande est un move,
        // alors on ajoute les coordonnées du move à la dernière commande
        if(command instanceof MoveCommand){
            if(this.stack.lastElement() instanceof MoveCommand && !((MoveCommand) command).isNewMove()){
                MoveCommand lastCommand = (MoveCommand) this.stack.lastElement();
                if(lastCommand.getObjet().equals(command.getObjet())){
                    lastCommand.setX(lastCommand.getX() + ((MoveCommand) command).getX());
                    lastCommand.setY(lastCommand.getY() + ((MoveCommand) command).getY());
                }
            }
            else {
                stack.push(command);
            }
         }else{
            stack.push(command);
        }
        stackRedu.clear();
    }

    public void undo() {
        if (!stack.empty()) {
            OperationCommand command = stack.pop();
            command.compensate();
            stackRedu.add(command);
        }
    }

    public void redo(){
        if(!stackRedu.isEmpty()){
            OperationCommand command = stackRedu.remove(stackRedu.size()-1);
            command.operate();
            stack.push(command);
        }
    }
}
