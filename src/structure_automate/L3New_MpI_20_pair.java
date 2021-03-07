package structure_automate;

import java.util.ArrayList;

public class L3New_MpI_20_pair {
    private boolean arc_existe;
    private ArrayList<String> symbole;

    public L3New_MpI_20_pair(boolean arc_existe, ArrayList<String> symbole)
    {
        this.arc_existe = arc_existe;
        this.symbole = symbole;
    }

    public boolean isArc_existe() {
        return arc_existe;
    }

    public ArrayList<String> getSymbole() {
        return symbole;
    }

    public void setArc_existe(boolean arc_existe) {
        this.arc_existe = arc_existe;
    }

    public void addSymbole(String s){
        this.symbole.add(s);
    }

    public String toString(){
        return "(" + this.arc_existe+"," + this.symbole + ")";
    }
}
