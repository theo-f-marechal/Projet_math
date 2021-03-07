package main;

import java.util.Scanner;

public class L3New_MpI_20_main {
    public static void main(String[] args){
            L3New_MpI_20_stock st = new L3New_MpI_20_stock("..\\Projet_math\\src\\automate_file"); //remplit la classe contenant les automates

            while(true){
                Scanner scin = new Scanner(System.in);
                System.out.println("Tapez au choix :");
                System.out.println("-Le numero de l'automate sur lequel vous souhaitez travailler.");
                System.out.println("-\"liste\" pour afficher la liste des automates disponibles.");
                System.out.println("-\"fin\" pour arreter le programme.");

                boolean loop2 = true;
                while(loop2) {
                    String line = scin.next();
                    if (line.equals("liste")) {
                        st.print_automate_disponible();
                        System.out.println("Veuillez taper le numero d'un automate, \"liste\" ou \"fin\":");
                    } else if (line.equals("fin")) {
                        System.out.println("Le programme va se fermer.");
                        System.exit(0);
                    }else{
                        try {
                            int num = Integer.parseInt(line);
                            st.travail(num);
                            loop2 = false;
                        } catch (NumberFormatException e) {
                            System.out.println("Veuillez taper le numero d'un automate, \"liste\" ou \"fin\":");
                        } catch (L3New_MpI_20_exception_automate_inexistant L3New_MpI_20_exception_automate_inexistant) {
                            L3New_MpI_20_exception_automate_inexistant.printmsg();
                            System.out.println("Veuillez taper le numero d'un automate, \"liste\" ou \"fin\":");
                        }
                    }
                }
            }
    }
}
