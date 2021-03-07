package structure_automate;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class L3New_MpI_20_automate {
    private int nom_automate;
    private int taille_alphabet;
    private int nbr_etat;
    private ArrayList<Integer> sommet_initial;
    private ArrayList<Integer> sommet_terminal;
    private L3New_MpI_20_pair[][] matrice;
    private ArrayList<ArrayList<Integer>> transition;
    private boolean minimal;
    private boolean complementaire;
    private PrintWriter writer;

    public int getNom(){
        return this.nom_automate;
    }

    private L3New_MpI_20_pair[][] crea_graph(int n) {
        L3New_MpI_20_pair[][] mat = new L3New_MpI_20_pair[n][n]; // creer un tableau de int a deux dimensions
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                mat[i][j] = new L3New_MpI_20_pair(false, new ArrayList<>()); // remplit le tableau
            }
        }
        return mat;
    } //fonction auxiliaire du constructeur

    private ArrayList<String> strsplit(String str, String regex) {
        ArrayList<String> re = new ArrayList<>();
        Scanner sub = new Scanner(str).useDelimiter(regex);
        while (sub.hasNext())
            re.add(sub.next());
        sub.close();
        return re;
    } //fonction auxiliaire de printautomate

    private int indexof(String str, String c) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c.charAt(0))
                return i;
        }
        return -1;
    } //fonction auxiliaire de printautomate

    private void agrandi_graph() {
        L3New_MpI_20_pair[][] re = crea_graph(this.nbr_etat + 1);
        for (int i = 0; i < this.nbr_etat; i++) {
            System.arraycopy(this.matrice[i], 0, re[i], 0, this.nbr_etat); // remplit le tableau avec l'ancien;
        }
        this.nbr_etat += 1;
        this.matrice = re;
    }

    private L3New_MpI_20_pair[][] size_down(int size){
        L3New_MpI_20_pair[][] mat = new L3New_MpI_20_pair[size][size]; // creer un tableau de int a deux dimensions
        for (int i = 0; i < size; i++) {
            System.arraycopy(this.matrice[i], 0, mat[i], 0, size);
        }
        return mat;
    }

    public L3New_MpI_20_automate(String path) throws FileNotFoundException {
        Scanner title = new Scanner(path);
        Scanner title_d = title.useDelimiter("[a-zA-Z\\-.\\\\_]*"); //L3New-MpI-<numero d’equipe>-#.txt
        title_d.next();title_d.next();title_d.next();
        int num = 0;
        while (title_d.hasNextInt()){
            num = num*10 + title_d.nextInt();
        }
        this.nom_automate = num;

        try {
            String trace_name;
            if (this.nom_automate < 10) {
                trace_name = "..\\Projet_math\\src\\traces\\L3New-MpI--trace20-0" + num + ".txt";
            }else{
                trace_name = "..\\Projet_math\\src\\traces\\L3New-MpI--trace20-" + num + ".txt";
            }
           this.writer = new PrintWriter(trace_name);
        } catch (IOException e) {
            System.out.println("    --Erreur pendant la creation des traces " + num);
        }

        Scanner sc = new Scanner(new File(path));
        this.sommet_initial = new ArrayList<>();
        this.sommet_terminal = new ArrayList<>();
        this.taille_alphabet = Integer.parseInt(sc.nextLine().trim());
        this.nbr_etat = Integer.parseInt(sc.nextLine().trim());
        this.matrice = this.crea_graph(this.nbr_etat);

        String line;
        Scanner sc_line;

        // remplit un array List avec le nom des sommets initiaux
        line = sc.nextLine();
        sc_line = new Scanner(line);
        int nb_initiaux = sc_line.nextInt();
        for (int i = 0; i < nb_initiaux; i++)
            this.sommet_initial.add(sc_line.nextInt());

        // remplit un array List avec le nom des sommets terminaux
        line = sc.nextLine();
        sc_line = new Scanner(line);
        nb_initiaux = sc_line.nextInt();
        for (int i = 0; i < nb_initiaux; i++)
            this.sommet_terminal.add(sc_line.nextInt());

        int nbr_transition = Integer.parseInt(sc.nextLine().trim()); // recupere le nombre de transitions

        for (int i = 0; i < nbr_transition; i++) {
            line = sc.nextLine();
            ArrayList<String> sommets = strsplit(line, "[a-z]|\\*"); //split la string sur les lettres ou *
            ArrayList<String> transitions = strsplit(line, "[0-9]*"); // split la string sur les chiffres
            int pere = Integer.parseInt(sommets.get(0).trim());
            int fils = Integer.parseInt(sommets.get(1).trim());
            matrice[pere][fils].setArc_existe(true);    //indique que l'arc existe
            matrice[pere][fils].addSymbole(transitions.get(0)); //rajoute les symboles pour passer du pere au fils
        }
        sc_line.close();
        sc.close();
        this.transition = new ArrayList<>();
        for (int i = 0; i < this.nbr_etat; i++){
            ArrayList<Integer> temp = new ArrayList<>();
            temp.add(i);
            this.transition.add(temp);
        }
        this.minimal = false;
        this.complementaire = false;
    } //constructeur

    public void automatePrint(boolean detmini) {
        String format = "|%-10s |";
        String alphabet;
        int taille_alphabet_affichage = this.taille_alphabet;
        if (this.isAsynchrone(false)) {
            alphabet = "*abcdefghijklmnopqrstuvwxyz";
            taille_alphabet_affichage++;    // il faut prendre en compte le symbole epsylon dans le cas des asynchrones
        } else
            alphabet = "abcdefghijklmnopqrstuvwxyz";

        System.out.println("==================================== Affichage ==========================================");
        this.writer.println("==================================== Affichage ==========================================");
        System.out.println("Automate numero: " + this.nom_automate);
        this.writer.println("Automate numero: " + this.nom_automate);


        if (this.sommet_initial.size() == 1) {
            System.out.println("Etat initial: " + this.sommet_initial.toString());
            this.writer.println("Etat initial: " + this.sommet_initial.toString());
        } else {
            System.out.println("Etats initiaux: " + this.sommet_initial.toString());
            this.writer.println("Etats initiaux: " + this.sommet_initial.toString());
        }
        if (this.sommet_terminal.size() == 1) {
            System.out.println("Etat terminal: " + this.sommet_terminal.toString());
            this.writer.println("Etat terminal: " + this.sommet_terminal.toString());
        } else{
            System.out.println("Etats terminaux: " + this.sommet_terminal.toString());
            this.writer.println("Etats terminaux: " + this.sommet_terminal.toString());
        }
        System.out.println();
        this.writer.println();

        if (detmini){
            System.out.println("Correspondance entre les etats de l'automate original et l'automate obtenu:");
            this.writer.println("Correspondance entre les etats de l'automate original et l'automate obtenu:");
            System.out.println("Ancien(s) etat(s) => etat actuel");
            this.writer.println("Ancien(s) etat(s) => etat actuel");
            for (int i = 0; i < this.transition.size(); i++){
                System.out.println( this.transition.get(i).toString() + " => " + i);
                this.writer.println(this.transition.get(i).toString() + " => " + i);
            }
            System.out.println();
            this.writer.println();
        }

        System.out.print("               |");
        this.writer.print("               |");
        for (int i = 0; i < taille_alphabet_affichage; i++){  // construit la premiere ligne du tableaux
            System.out.printf(format, alphabet.charAt(i));
            this.writer.printf(format, alphabet.charAt(i));
        }
        System.out.println();
        this.writer.println();

        for (int i = 0; i < this.nbr_etat; i++) {
            ArrayList<String> symbole_fils = new ArrayList<>(); // liste des fils a afficher vide par defaut
            for (int y = 0; y < taille_alphabet_affichage; y++) //remplit le tableau
                symbole_fils.add(" ");

            String ESR = ""; //String representant les entrees sorties d'un sommet
            if (this.sommet_initial.contains(i))
                ESR += "E";
            if (this.sommet_terminal.contains(i))
                ESR += "S";
            System.out.printf("%-3s", ESR);
            this.writer.printf("%-3s", ESR);
            System.out.printf(format, i);
            this.writer.printf(format, i);

            for (int j = 0; j < this.nbr_etat; j++) {
                if (!this.matrice[i][j].isArc_existe()) //on ignore les arcs qui n'existent pas
                    continue;
                ArrayList<String> sytemp = this.matrice[i][j].getSymbole(); //recupere la liste des symboles pour atteindre un fils
                for (String s : sytemp) {
                    int indice_sy = indexof(alphabet, s); //l'indice du symbole dans l'alphabet
                    String a_str = symbole_fils.get(indice_sy); //la string sur laquelle on veut concatener
                    a_str += "," + j; // concatenation
                    symbole_fils.set(indice_sy, a_str);
                }
            }
            for (String symbole_fil : symbole_fils) {
                System.out.printf(format, symbole_fil.replaceFirst(",", ""));
                this.writer.printf(format, symbole_fil.replaceFirst(",", ""));
            }
            System.out.println();
            this.writer.println();
        }
        this.writer.flush();
    }

    //=====================================================================================deter_comp

    private boolean isAsynchrone(boolean print) {
        if (print){
            System.out.println("======================= Verification automate synchrone =================================");
            this.writer.println("======================= Verification automate synchrone =================================");
        }
        boolean asynchrone = false;
        for (int i = 0; i < this.nbr_etat; i++) {
            for (int j = 0; j < this.nbr_etat; j++) {
                for (String s : this.matrice[i][j].getSymbole()) {
                    if (s.equals("*")) {
                        if (print) {
                            if (!asynchrone) {
                                System.out.println("L'automate numero " + this.nom_automate
                                        + " est asynchrone a cause des transitions:");
                                this.writer.println("L'automate numero " + this.nom_automate
                                        + " est asynchrone a cause des transitions:");
                            }
                            System.out.println("- " + i + "*" + j);
                            this.writer.println("- " + i + "*" + j);
                        }
                        asynchrone = true;
                    }
                }
            }
        }
        if (print && !asynchrone) {
            System.out.println("L'automate numero " + this.nom_automate + " est synchrone.");
            this.writer.println("L'automate numero " + this.nom_automate + " est synchrone.");
        }
        this.writer.flush();
        return asynchrone;
    }

    private boolean isDeterministe(boolean print) {
        if (print) {
            System.out.println("======================= Verification automate deterministe ===============================");
            this.writer.println("======================= Verification automate deterministe ===============================");
        }
        if (this.isAsynchrone(false)) {
            if (print) {
                System.out.println("L'automate numero " + this.nom_automate
                        + " est non deterministe puisqu'il est asynchrone");
                this.writer.println("L'automate numero " + this.nom_automate
                        + " est non deterministe puisqu'il est asynchrone");
            }
            this.writer.flush();
            return false;
        }
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        if (this.sommet_initial.size() > 1) { //on verifie qu'il n'y a qu'un etat entrant
            if (print) {
                System.out.println("L'automate numero " + this.nom_automate
                        + " est non deterministe puisqu'il a deux etats entrant");
                this.writer.println("L'automate numero " + this.nom_automate
                        + " est non deterministe puisqu'il a deux etats entrant");
            }
            this.writer.flush();
            return false;
        }

        boolean deterministe = true;
        for (int i = 0; i < this.nbr_etat; i++) {
            ArrayList<Integer> symbole_tot = new ArrayList<>(); // le total de fois où chaque symbole apparaît pour un sommet
            for (int j = 0; j < this.taille_alphabet; j++)
                symbole_tot.add(0);

            for (int j = 0; j < this.nbr_etat; j++) {
                boolean once = true; // permet de prendre en compte le cas où le même arc est present deux fois
                for (String s : this.matrice[i][j].getSymbole()) {
                    int index_s = indexof(alphabet, s);
                    int somme_actuel = symbole_tot.get(index_s);
                    if (once) {
                        symbole_tot.set(index_s, somme_actuel + 1); // on ajoute 1 dans la case correspondante lorsqu'on trouve un symbole
                        once = false;
                    }
                }
            }
            for (int sum : symbole_tot) {
                if (sum > 1) {
                    if (print) {
                        if (deterministe) {
                            System.out.println("L'automate numero " + this.nom_automate
                                    + " est non deterministe a cause des etats:");
                            this.writer.println("L'automate numero " + this.nom_automate
                                    + " est non deterministe a cause des etats:");
                        }
                        System.out.println("- " + i);
                        this.writer.println("- " + i);
                    }
                    deterministe = false;
                }
            }
        }
        if (print && deterministe) {
            System.out.println("L'automate numero " + this.nom_automate + " est deterministe.");
            this.writer.println("L'automate numero " + this.nom_automate + " est deterministe.");
        }
        this.writer.flush();
        return deterministe;
    }

    private boolean isComplet(boolean print) {
        if (print) {
            System.out.println("======================= Verification automate complet ===================================");
            this.writer.println("======================= Verification automate complet ===================================");
        }
        if (this.isAsynchrone(false)) {
            this.writer.flush();
            return false;
        }

        String alphabet = "abcdefghijklmnopqrst";
        boolean complet = true;

        for (int i = 0; i < this.nbr_etat; i++) {
            ArrayList<Boolean> presence_symbole = new ArrayList<>();
            for (int j = 0; j < this.taille_alphabet; j++) //list de bool pour check la presence de chaque symbole
                presence_symbole.add(false);
            for (int j = 0; j < this.nbr_etat; j++) {
                for (String s : this.matrice[i][j].getSymbole()) {
                    presence_symbole.set(this.indexof(alphabet, s), true);
                }
            }

            boolean loop1 = true;
            for (int j = 0; j < presence_symbole.size(); j++){
                boolean pre_sy = presence_symbole.get(j);
                if (!pre_sy){
                    if (print) {
                        if (complet) {
                            System.out.println("L'automate numero " + this.nom_automate + " est incomplet a cause des etats:");
                            this.writer.println("L'automate numero " + this.nom_automate + " est incomplet a cause des etats:");
                        }if (loop1) {
                            System.out.print("- " + i + " auquel il manque la transition sur: " + alphabet.charAt(j));
                            this.writer.println("- " + i + " auquel il manque la transition sur: " + alphabet.charAt(j));
                        } else {
                            System.out.print(", " + alphabet.charAt(j));
                            this.writer.println(", " + alphabet.charAt(j));
                        }
                        loop1 = false;
                    }
                    complet = false;
                }
            }
            if (print && !complet) {
                System.out.println();
                this.writer.println();
            }
        }
        if (print && complet) {
            System.out.println("L'automate numero " + this.nom_automate + " est complet.");
            this.writer.println("L'automate numero " + this.nom_automate + " est complet.");
        }
        this.writer.flush();
        return complet;
    }

    private void completion() {
        if (this.isComplet(false))
            return;
        int index_pui = this.nbr_etat;
        this.agrandi_graph(); // on fait de la place pour le puit

        String alphabet = "abcdefghijklmnopqrst";
        for (int i = 0; i < this.nbr_etat; i++) {

            ArrayList<Boolean> presence_symbole = new ArrayList<>(); // tableau comptant la presence de chaque lettre
            for (int j = 0; j < this.taille_alphabet; j++) // on remplit le tableau
                presence_symbole.add(false);

            for (int j = 0; j < this.nbr_etat; j++) {
                for (String s : this.matrice[i][j].getSymbole()) {
                    presence_symbole.set(this.indexof(alphabet, s), true);
                }
            }

            ArrayList<String> re = new ArrayList<>();
            for (int j = 0; j < presence_symbole.size(); j++) { //on regarde quel symbole manque pour chaque ligne et on ajoute un arc ver le puit
                if (!presence_symbole.get(j)) {
                    re.add("" + alphabet.charAt(j));
                }
            }
            this.matrice[i][index_pui] = new L3New_MpI_20_pair(true, re);
        }
        ArrayList<String> re = new ArrayList<>();
        for (int i = 0; i < this.taille_alphabet; i++)
            re.add("" + alphabet.charAt(i));
        this.matrice[index_pui][index_pui] = new L3New_MpI_20_pair(true, re);

        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(index_pui);
        this.transition.add(temp);
    }

    private void union(ArrayList<Integer> e1, ArrayList<Integer> e2){
        for (int e : e2){
            if(!e1.contains(e))
                e1.add(e);
        }
        Collections.sort(e1);
    } // fait l'union d'ensemble de int

    private ArrayList<Integer> rec_fermeture_e(int i){
        ArrayList<Integer> re = new ArrayList<>();
        re.add(i);
        for (int j = 0; j < this.nbr_etat; j++){
            if (this.matrice[i][j].isArc_existe() && this.matrice[i][j].getSymbole().contains("*")) {
                this.union(re, rec_fermeture_e(j));
            }
        }
        Collections.sort(re);
        return re;
    }

    private ArrayList<Integer> fermeture_tab(ArrayList<Integer> tab) {
        ArrayList<Integer> re = new ArrayList<>();
        for (int i : tab){
            this.union(re, this.rec_fermeture_e(i));
        }
        return re;
    }

    private void determinisation(){
        int d_matrice_size = this.nbr_etat;
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        ArrayList<ArrayList<Integer>> tab_ne = new ArrayList<>(); //tableau des nouveaux etats
        L3New_MpI_20_pair[][] d_matrice = crea_graph(d_matrice_size); //nouveau tableaux

        Collections.sort(this.sommet_initial);
        tab_ne.add(this.fermeture_tab(this.sommet_initial)); // on ajoute l'union des fermetures epsilons des sommets initiaux
        int index_det = 0; //index dernier etat traite dans le tableau des nouveaux etats

        while (index_det < tab_ne.size()) { //parcourt le tableau des unions de sommets a etudier
            ArrayList<ArrayList<Integer>> tab_sy = new ArrayList<>();
            for (int i = 0; i < this.taille_alphabet; i++)
                tab_sy.add(new ArrayList<>());

            ArrayList<Integer> line = tab_ne.get(index_det);
            for (int e_union : line) { //parcourt l'ensemble des etats
                for (int j = 0; j < this.nbr_etat; j++) { //parcourt la ligne en cours dans la matrice d'origine
                    if (this.matrice[e_union][j].isArc_existe()) {
                        for (int i_pair_sy = 0; i_pair_sy < this.matrice[e_union][j].getSymbole().size(); i_pair_sy++) {
                            int index_sy = indexof(alphabet, this.matrice[e_union][j].getSymbole().get(i_pair_sy)); //l'index du symbole etudie
                            if (index_sy != -1) { //si le symbole n'est pas une *
                                ArrayList<Integer> temp = tab_sy.get(index_sy);
                                Collections.sort(temp);
                                if (!temp.contains(j))
                                    temp.add(j);
                                Collections.sort(temp);
                                tab_sy.set(index_sy, temp);
                            }
                        }
                    }
                }
            }

            for (int y = 0; y < tab_sy.size(); y++) {
                ArrayList<Integer> union = tab_sy.get(y);
                union = this.fermeture_tab(union);
                Collections.sort(union);
                if (!union.isEmpty()) {
                    if (!tab_ne.contains(union))
                        tab_ne.add(union);

                    if (tab_ne.size() > d_matrice_size) { //si la matrice est devenue trop petite on l'agrandit
                        L3New_MpI_20_pair[][] re = crea_graph(d_matrice_size + 1);
                        for (int i = 0; i < d_matrice_size; i++) {
                            System.arraycopy(d_matrice[i], 0, re[i], 0, d_matrice_size);
                        }
                        d_matrice_size += 1;
                        d_matrice = re;
                    }

                    int i = tab_ne.indexOf(tab_ne.get(index_det));
                    int j = tab_ne.indexOf(union);
                    d_matrice[i][j].setArc_existe(true);
                    d_matrice[i][j].addSymbole("" + alphabet.charAt(y));
                }
            }
            index_det++;
        }
        this.sommet_initial.clear();
        this.sommet_initial.add(0); //le nouvel etat initial est l'etat 0

        ArrayList<Integer> re = new ArrayList<>();
        for (Integer integer : this.sommet_terminal) {
            for (int j = 0; j < tab_ne.size(); j++)
                if (tab_ne.get(j).contains(integer) && !re.contains(j))
                    re.add(j);
        }
        Collections.sort(re);
        this.sommet_terminal = re;
        this.nbr_etat = tab_ne.size();
        this.matrice = d_matrice;
        if (d_matrice_size > this.nbr_etat)
            this.matrice = this.size_down(this.nbr_etat);
        this.transition = tab_ne;
    }

    public void determinisation_et_completion(){
        if(this.isAsynchrone(true)){
            this.determinisation();
            this.completion();
        }else{
            if (this.isDeterministe(true)){
                if(!this.isComplet(true)) {
                    this.completion();
                }
            } else{
                this.determinisation();
                this.completion();
            }
        }
        this.automatePrint(true);
    }

    //=====================================================================================minimisation

    private int i_tab(ArrayList<ArrayList<Integer>>ttab, int elt){
        for (int i = 0; i < ttab.size(); i++){
            if(ttab.get(i).contains(elt))
                return i;
        }
        return -1; //erreur, non trouve
    }

    private int fils_transition(int id_pere, int id_lettre){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String c = "" + alphabet.charAt(id_lettre);

        for (int i = 0; i < this.nbr_etat; i++){
            if (this.matrice[id_pere][i].isArc_existe() && this.matrice[id_pere][i].getSymbole().contains(c))
                return i;
        }
        return -1; // erreur, non trouve
    }

    private ArrayList<ArrayList<Integer>> scinde_aux(ArrayList<ArrayList<Integer>> tab_ne, ArrayList<Integer> group){

        ArrayList<ArrayList<Integer>> re = new ArrayList<>();
        ArrayList<ArrayList<Integer>> comp_utilise = new ArrayList<>();

        for (int i = 0; i < tab_ne.size(); i++){
            ArrayList<Integer> comp_group = tab_ne.get(i);
            if (!comp_utilise.contains(comp_group)){
                ArrayList<Integer> temp = new ArrayList<>();
                temp.add(group.get(i));
                re.add(temp);
                comp_utilise.add(comp_group);
            }
            else{
                int id_compo = comp_utilise.indexOf(comp_group);
                ArrayList<Integer> temp = re.get(id_compo);
                temp.add(group.get(i));
                re.set(id_compo, temp);
            }
        }
        return re;
    }

    private boolean isMinimise(ArrayList<ArrayList<Integer>> old_tab,
                              ArrayList<ArrayList<Integer>> new_tab){
        boolean mini = true;
        if (old_tab.size() == new_tab.size()) {
            for (int i = 0; i < old_tab.size(); i++) {
                if (old_tab.get(i).size() == new_tab.get(i).size()) {
                    for (int j = 0; j < new_tab.get(i).size(); j++) {
                        if (!old_tab.get(i).get(j).equals(new_tab.get(i).get(j))) {
                            mini = false;
                            break;
                        }
                    }
                }else {
                    mini = false;
                    break;
                }
            }
            if (mini) {
                System.out.println("Les partitions sont toutes inchangees par rapport a l'etape precedente," +
                        " l'automate est donc minimal.");
                this.writer.println("Les partitions sont toutes inchangees par rapport a l'etape precedente," +
                        " l'automate est donc minimal.");
                this.minimal = true;
                this.writer.flush();
                return true;
            }
        }
        for (ArrayList<Integer> tab : new_tab){
            if (tab.size() > 1) {
                this.minimal = false;
                return false;
            }
        }
        System.out.println("Toutes les partitions etants des singletons, l'automate est donc minimal.");
        this.writer.println("Toutes les partitions etants des singletons, l'automate est donc minimal.");
        this.minimal = true;
        this.writer.flush();
        return true;
    }

    private void affiche_partition_mini(ArrayList<Integer> partition,
                                        ArrayList<ArrayList<Integer>> tab,
                                        ArrayList<ArrayList<Integer>> tab_partitions){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        String format = "|%-20s |";
        System.out.println("Partition: " + partition);
        this.writer.println("Partition: " + partition);
        System.out.print("                      |");
        this.writer.print("                      |");
        for (int i = 0; i < taille_alphabet; i++) {  // construit la premiere ligne du tableau
            System.out.printf(format, alphabet.charAt(i));
            this.writer.printf(format, alphabet.charAt(i));
        }
        System.out.println();
        this.writer.println();

        for (int i = 0; i < partition.size(); i++){
            System.out.printf(format,partition.get(i));
            this.writer.printf(format,partition.get(i));
            ArrayList<Integer> temp = tab.get(i);
            for (int j :temp){
                System.out.printf(format,tab_partitions.get(j));
                this.writer.printf(format,tab_partitions.get(j));
            }
            System.out.println();
            this.writer.println();
        }
        System.out.println();
        this.writer.println();
        this.writer.flush();
    }

    private ArrayList<ArrayList<Integer>> scinde(ArrayList<ArrayList<Integer>> tab_groupe, ArrayList<Integer> group){

        if (group.size() <= 1){ //Si le groupe est un singleton on le renvoie
            ArrayList<ArrayList<Integer>> re = new ArrayList<>();
            re.add(group);
            System.out.println("Partition: " + group + " est un singleton.\n");
            this.writer.println("Partition: " + group + " est un singleton.\n");
            this.writer.flush();
            return re;
        }

        ArrayList<ArrayList<Integer>> tab_ne = new ArrayList<>();
        for (int i = 0; i < group.size(); i++) {
            ArrayList<Integer> temp = new ArrayList<>();
            for (int j = 0; j < taille_alphabet; j++)
                temp.add(-2);
            tab_ne.add(temp);
        }

        for (int i = 0; i < group.size(); i++){
            int elt_groupe = group.get(i);
            for (int y = 0; y < this.taille_alphabet; y++){
                int fils = this.fils_transition(elt_groupe,y);
                int num_groupe = i_tab(tab_groupe, fils);
                ArrayList<Integer> temp = tab_ne.get(i);
                temp.set(y,num_groupe);
                tab_ne.set(i,temp);
            }
        }
        affiche_partition_mini(group, tab_ne, tab_groupe);
        ArrayList<ArrayList<Integer>> temp = this.scinde_aux(tab_ne, group);
        if (temp.size() != 1) {
            System.out.print("On scinde donc la partition: ");
            this.writer.print("On scinde donc la partition: ");
            for (int aff = 0; aff < temp.size(); aff++) {
                if (aff == (temp.size() - 1)) {
                    System.out.println(temp.get(aff).toString());
                    this.writer.println(temp.get(aff).toString());
                }else {
                    System.out.print(temp.get(aff).toString() + ", ");
                    this.writer.print(temp.get(aff).toString() + ", ");
                }
            }
        }else{
            System.out.println("La partition reste donc inchangee.");
            this.writer.println("La partition reste donc inchangee.");
        }
        System.out.println();
        this.writer.println();
        this.writer.flush();
        return temp;
    }

    private void reconstruction(ArrayList<ArrayList<Integer>> tab_groupe){
       L3New_MpI_20_pair[][] re = this.crea_graph(tab_groupe.size());
       String alphabet = "abcdefghijklmnopqrstuvwxyz";

       for (int i = 0; i < tab_groupe.size(); i++){
           int etat_ori = tab_groupe.get(i).get(0);
           for (int j = 0; j < this.taille_alphabet; j++) {
               int f = this.fils_transition(etat_ori, j);
               ArrayList<String> temp = re[i][this.i_tab(tab_groupe,f)].getSymbole();
               temp.add("" + alphabet.charAt(j));
               re[i][this.i_tab(tab_groupe,f)] = new L3New_MpI_20_pair(true, temp);
           }
       }

       this.matrice = re;
       this.nbr_etat = tab_groupe.size();
       this.sommet_initial.set(0,this.i_tab(tab_groupe,this.sommet_initial.get(0)));
        ArrayList<Integer> temp = new ArrayList<>();
        for (int etat : this.sommet_terminal) {
            int i_temp = this.i_tab(tab_groupe, etat);
            if(!temp.contains(i_temp))
                temp.add(i_temp);
        }
        Collections.sort(temp);
        this.sommet_terminal = temp;
        this.transition = tab_groupe;
    }

    public void minimisation(){
        ArrayList<ArrayList<Integer>> tab_groupe = new ArrayList<>();
        Collections.sort(this.sommet_terminal);
        tab_groupe.add(this.sommet_terminal);

        System.out.println("=================================== Minimisation ========================================");
        this.writer.println("=================================== Minimisation ========================================");

        ArrayList<Integer> non_terminaux = new ArrayList<>();
        for (int i = 0; i < this.nbr_etat; i++) {
            if (!this.sommet_terminal.contains(i))
                non_terminaux.add(i);
        }
        Collections.sort(non_terminaux);
        tab_groupe.add(non_terminaux);

        boolean mini = false;
        int j = 1;
        do{
            System.out.println("--Etape numero " + j + ":");
            this.writer.println("--Etape numero " + j + ":");
            System.out.print("Les partitons sont: " );
            this.writer.print("Les partitons sont: " );
            for (int aff = 0; aff< tab_groupe.size(); aff++) {
                if (aff == (tab_groupe.size() - 1)) {
                    System.out.println(tab_groupe.get(aff));
                    this.writer.println(tab_groupe.get(aff));
                }else {
                    System.out.print(tab_groupe.get(aff) + ", ");
                    this.writer.print(tab_groupe.get(aff) + ", ");
                }
            }
            ArrayList<ArrayList<Integer>> new_tab_groupe = new ArrayList<>();
            for (int i = 0; i < tab_groupe.size(); i++){
                new_tab_groupe.addAll(this.scinde(tab_groupe, tab_groupe.get(i)));
            }

            if (isMinimise(tab_groupe,new_tab_groupe))
                mini = true;
            tab_groupe = new_tab_groupe;
            j++;
        }while (!mini);
        System.out.println("--Fin minimisation");
        this.writer.println("--Fin minimisation");
        this.reconstruction(tab_groupe);
        this.minimal = true;
        this.writer.flush();
    }

    //=====================================================================================Reconnaissance de mot

    private void passage_mot(String mot){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int etat_etudie = this.sommet_initial.get(0);
        for (int i = 0; i < mot.length(); i++){
            if (mot.charAt(i) != '*') {
                int id_lettre = alphabet.indexOf(mot.charAt(i));
                if (id_lettre == -1) {
                    System.out.println("Le mot \"" + mot + "\" n'est pas reconnu par l'automate numero " + this.nom_automate
                        + " a cause de la lettre numero  " + i + ": " + mot.charAt(i));
                    this.writer.println("Le mot \"" + mot + "\" n'est pas reconnu par l'automate numero " + this.nom_automate
                            + " a cause de la lettre numero  " + i + ": " + mot.charAt(i));
                    this.writer.flush();
                    return;
                }
                int prochaine_etat = this.fils_transition(etat_etudie, id_lettre);
                if (prochaine_etat == -1) { // s'il n'existe pas de transition pour cette lettre on s'arrête
                    System.out.println("Le mot \"" + mot + "\" n'est pas reconnu par l'automate numero " + this.nom_automate
                            + " a cause de la lettre numero  " + i + ": " + mot.charAt(i));
                    this.writer.println("Le mot \"" + mot + "\" n'est pas reconnu par l'automate numero " + this.nom_automate
                            + " a cause de la lettre numero  " + i + ": " + mot.charAt(i));
                    this.writer.flush();
                    return;
                }
            etat_etudie = prochaine_etat;
            }
        }
        if (sommet_terminal.contains(etat_etudie)) {
            System.out.println("Le mot \"" + mot + "\" est reconnu par l'automate numero " + this.nom_automate);
            this.writer.println("Le mot \"" + mot + "\" est reconnu par l'automate numero " + this.nom_automate);
            this.writer.flush();
            return;
        }
        System.out.println("Le mot \"" + mot + "\" n'est pas reconnu par l'automate numero " + this.nom_automate
                + " puisqu'il ne permet pas d'atteindre un etat terminal");
        this.writer.println("Le mot \"" + mot + "\" n'est pas reconnu par l'automate numero " + this.nom_automate
                + " puisqu'il ne permet pas d'atteindre un etat terminal");
        this.writer.flush();
    }

    public void reconnaissance_mot(){
        System.out.println("============================== Reconnaissance de mot ====================================");
        this.writer.println("============================== Reconnaissance de mot ====================================");
        System.out.println("Entrez un mot que vous souhaitez reconnaitre."
                + "\n- * represente le mot vide"
                + "\n- $ lorsque vous souhaitez arreter");

        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()){
            line = sc.nextLine();
            if (line.equals("$"))
                break;
            this.passage_mot(line);
        }
        System.out.println();
        this.writer.println();
        this.writer.flush();
    }

    //=====================================================================================automate complementaire

    public void complementaire(){
        System.out.println("================================= Complementarisation =======================================");
        this.writer.println("================================= Complementarisation =======================================");

        if (this.isDeterministe(false))
            this.determinisation();
        if (this.isComplet(false))
            this.completion();

        ArrayList<Integer> temp = new ArrayList<>();
        for (int i = 0; i < this.nbr_etat; i++){
            if (!this.sommet_terminal.contains(i))
                temp.add(i);
        }
        this.sommet_terminal = temp;
        this.complementaire = !this.complementaire;
        System.out.print("L'automate fini deterministe complet ");
        this.writer.print("L'automate fini deterministe complet ");
        if (this.minimal) {
            System.out.print("minimal ");
            this.writer.print("minimal ");
        }
        System.out.println("numero " + this.nom_automate + " a ete transforme en son complementaire.\n");
        this.writer.println("numero " + this.nom_automate + " a ete transforme en son complementaire.\n");
        this.writer.flush();
    }

    //=====================================================================================Standardisation

    private boolean is_standard(){
        int etat_initial = this.sommet_initial.get(0);
        for (int i = 0; i < this.nbr_etat; i++){
            for(int j = 0; j < this.taille_alphabet; j++) {
                if (this.fils_transition(i, j) == etat_initial)
                    return false;
            }
        }
        System.out.println("L'automate numero " + this.nom_automate + " est deja standard.");
        this.writer.println("L'automate numero " + this.nom_automate + " est deja standard.");
        this.writer.flush();
        return true;
    }

    public void standardisation(){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        System.out.println("================================= Standardisation =======================================");
        this.writer.println("================================= Standardisation =======================================");
        if(!this.is_standard()){
            int etat_initial = this.sommet_initial.get(0);
            int id_i = this.nbr_etat;
            this.agrandi_graph();
            if (this.sommet_terminal.contains(etat_initial)) //si l'automate reconnaissait le mot vide on ajoute i aux etats terminaux
                this.sommet_terminal.add(id_i);
            this.sommet_initial.set(0, id_i); // on remplace l'etat initial actuel par i
            for (int j = 0; j < this.taille_alphabet; j++){
                ArrayList<String> temp = new ArrayList<>();
                temp.add("" + alphabet.charAt(j));
                this.matrice[id_i][this.fils_transition(etat_initial,j)] = new L3New_MpI_20_pair(true, temp);
            }
            System.out.println("L'automate numero " + this.nom_automate + " a ete standardise.");
            this.writer.println("L'automate numero " + this.nom_automate + " a ete standardise.");
        }
        this.writer.flush();
    }
}
