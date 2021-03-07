package main;

public class L3New_MpI_20_exception_automate_inexistant extends Exception {
    public L3New_MpI_20_exception_automate_inexistant(String s){
        super(s);
    }

    public void printmsg(){
        System.out.println("L'automate saisi ne fait pas partie de la liste");
    }
}
