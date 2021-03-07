package main;

import structure_automate.L3New_MpI_20_automate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class L3New_MpI_20_stock {
    private ArrayList<L3New_MpI_20_automate> liste_L3NewMpI20_automate;

    public L3New_MpI_20_stock(String path) {
        this.liste_L3NewMpI20_automate = new ArrayList<>();
        File repertoire = new File(path);
        String[] list_file = repertoire.list();
        if (list_file != null && list_file.length != 0) {
            for (String s : list_file) {
                try {
                    this.liste_L3NewMpI20_automate.add(new L3New_MpI_20_automate(path + "\\" + s));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("Le dossier " + path + " est vide, le programme va se fermer. ");
            System.exit(0);
        }
    }

    public void print_automate_disponible() {
        System.out.println("Les automates disponibles sont:");
        for (L3New_MpI_20_automate a : this.liste_L3NewMpI20_automate) {
            System.out.println("Automate numero " + a.getNom());
        }
    }

    private L3New_MpI_20_automate get_automate(int num) throws L3New_MpI_20_exception_automate_inexistant {
        for (L3New_MpI_20_automate a : this.liste_L3NewMpI20_automate) {
            if (a.getNom() == num) {
                return a;
            }
        }
        throw new L3New_MpI_20_exception_automate_inexistant("L'automate saisi ne fait pas partie de la liste");
    }

    public void travail(int numero_automate) throws L3New_MpI_20_exception_automate_inexistant {
        L3New_MpI_20_automate a = this.get_automate(numero_automate);
        System.out.println("########################## Debut du travail sur l'automate numero " + a.getNom()
                + " ############################");
        a.automatePrint(false);
        a.determinisation_et_completion();
        boolean loop = true;
        while (loop) {
            Scanner scin = new Scanner(System.in);
            System.out.println("====================================== Menu ============================================");
            System.out.println("Tapez le numero de l'action que vous souhaitez entreprendre:");
            System.out.println("- 1 : Minimiser l'automate");
            System.out.println("- 2 : Standardiser l'automate");
            System.out.println("- 3 : Complementariser l'automate");
            System.out.println("- 4 : Reconnaissance de mot");
            System.out.println("- 5 : Changer d'automate");
            System.out.println("- 6 : Arreter le programme.");
            String line = scin.next();
            if (line.equals("fin")) {
                System.out.println("Le programme va se fermer.");
                System.exit(0);
            }
            try {
                int num = Integer.parseInt(line);
                switch (num) {
                    case 1:
                        a.minimisation();
                        a.automatePrint(true);
                        break;
                    case 2:
                        a.standardisation();
                        a.automatePrint(true);
                        break;
                    case 3:
                        a.complementaire();
                        a.automatePrint(true);
                        break;
                    case 4:
                        a.reconnaissance_mot();
                        break;
                    case 5:
                        loop = false;
                        break;
                    case 6:
                        System.out.println("Le programme va se fermer.");
                        System.exit(0);
                    default:
                        System.out.println("Veuillez taper le numero d'une action:");
                }
            } catch (NumberFormatException e) {
                System.out.println("Veuillez taper le numero d'une action:");
            }
        }
        System.out.println("########################### Fin du travail sur l'automate numero " + a.getNom()
                + " #############################");
    }
}
