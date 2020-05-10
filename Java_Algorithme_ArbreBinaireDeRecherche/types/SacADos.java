package types;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Classe representant un sac a dos
 * Il contient la liste des objets disponible et la solution optimale du probleme d'optimisation.
 *
 * @author Oscar et Jeneifan
 * @version 5.0
 */
public class SacADos {

    private List<Objet> objetsPossibles;
    private List<Objet> meilleursObjets;
    private float poidsSolutionOpti;
    private float valeurOptimale;
    private float poidsMaximal;


    /**
     * Construit un sac a dos vide
     */
    public SacADos() {
        this.meilleursObjets = new ArrayList<>();
        this.objetsPossibles = new ArrayList<>();
        this.valeurOptimale = 0;
        this.poidsSolutionOpti = 0;
        this.poidsMaximal = 10; // valeur arbitraire par defaut
    }


    /**
     * Construit un sac a dos avec la liste des objets possibles et la capacite max. du sac
     *
     * @param chemin       : chemin du fichier contenant les objets possibles a mettre dans le sac
     * @param poidsMaximal : capacite maximal du sac a dos
     */
    public SacADos(String chemin, float poidsMaximal) {

        this.objetsPossibles = new ArrayList<>();
        this.meilleursObjets = new ArrayList<>();

        lireObjetsDansFichier(chemin);

        // On s'assure que le poidsMaximal donne soit > 0
        if (poidsMaximal > 0) {
            this.poidsMaximal = poidsMaximal;
        } else {
            this.poidsMaximal = 10;
            System.err.println("Valeur du poids maximal invalide! Valeur par defaut (10.0) va etre utilise.");
        }
    }


    /**
     * Lis un fichier .txt et remplit la liste des objets possibles
     *
     * @param path : le chemin du fichier contenant les objets a ajouter
     */
    private void lireObjetsDansFichier(String path) {
        String[] objsFichier;

        try {
            Scanner sc = new Scanner(new File(path));

            while (sc.hasNextLine()) {
                objsFichier = sc.nextLine().split(" ; ");


                // On s'assure que l'objet ne va pas faire planter les algorithmes
                if (!objsFichier[0].equals("") && objsFichier[0] != null && Float.parseFloat(objsFichier[1]) > 0 && objsFichier[1] != null && Float.parseFloat(objsFichier[2]) > 0 && objsFichier[2] != null) {
                    Objet o = new Objet(objsFichier[0], Float.parseFloat(objsFichier[1]), Float.parseFloat(objsFichier[2]));
                    objetsPossibles.add(o);
                } else {
                    System.err.println("Un objet n'a pas ete rajoute car il contient des valeurs interdites!");
                }

            }
            sc.close();
        } catch (FileNotFoundException exception) {
            System.err.println("Le fichier d'objets n'a pas ete trouve!");
        }
    }


    /**
     * Renvoie la solution optimale pour l'afficher
     *
     * @return string contenant la valeur optimale et la liste des objets a prendre
     */
    @Override
    public String toString() {
        calculerPoidsSolution();

        StringBuilder solution = new StringBuilder("Meilleure solution possible: " + valeurOptimale + "$   " + poidsSolutionOpti + "Kg\n\n");

        solution.append("Objets a mettre dans le sac: \n");

        for (Objet o : meilleursObjets) {
            solution.append("- ").append(o.toString());
            solution.append("\n");
        }

        return solution.toString();
    }


    /**
     * Lance l'algorithme de resolution precise en parametre
     *
     * @param methode : la methode de resolution souhaite (glouton, dynamique, pse)
     */
    public void resoudre(String methode) {
        String m = methode.trim();
        m = m.toLowerCase();


        switch (m) {
            case "glouton":
            case "gloutonne":
                methodeGloutonne();
                break;
            case "dynamique":
                methodeDynamique();
                break;
            case "pse":
                methodePSE();
                break;
            default:
                System.err.println("La methode precise n'est pas une methode valide. \nVeuillez en choisir une parmis: glouton, dynamique et pse");
                break;
        }

    }


    /**
     * Tri les objets de la liste objetsPossibles avec la methode gloutonne
     */
    private void triGlouton() {

        Collections.sort(objetsPossibles);     // Trie la liste d'objet avec la methode implementee compareTo
    }


    /**
     * Calcule la valeur totale optimale du sac
     */
    private void calculerValeurOptimale() {
        float v = 0;

        for (Objet o : meilleursObjets) {
            v += o.getValeur();
        }

        this.valeurOptimale = v;
    }


    /**
     * Calcule le poids total de la solution optimale
     */
    private void calculerPoidsSolution() {
        float p = 0;

        for (Objet o : meilleursObjets) {
            p += o.getPoids();
        }

        this.poidsSolutionOpti = p;
    }


    /**
     * Execute l'algorithme de resolution glouton
     */
    private void methodeGloutonne() {

        triGlouton();

        float poidsTotal = 0;
        int indexObjets = 0;

        // Recupere les objets a prendre tant que le poids est en dessous du poids maximal
        for (Objet obj : objetsPossibles) {
            poidsTotal += obj.getPoids();

            if (poidsTotal < poidsMaximal) {
                indexObjets++;
            } else {
                break;
            }
        }

        // On place les objets a mettre dans le sac dans la liste solution
        for (int i = 0; i < indexObjets; i++) {
            meilleursObjets.add(objetsPossibles.get(i));
        }

        calculerValeurOptimale();
    }


    /**
     * Execute l'algorithme de resolution par programmation dynamique
     */
    private void methodeDynamique() {

        // On multiplie les poids par dix pour ne pas avoir de problèmes avec les poids < 0 lors de la création du tableau
        poidsMaximal = poidsMaximal * 10;
        for (Objet o : objetsPossibles) {
            o.setPoids(o.getPoids() * 10);
        }


        // Toutes les cases sont  a zero par defaut (chaque ligne represente un objet tandis que chaque colonne represente le poids de 0 a poidsMaximal)
        double[][] matrice = new double[objetsPossibles.size()][(int) poidsMaximal + 1];

        // PHASE 1: On initialise la premiere ligne
        for (int i = 0; i < poidsMaximal + 1; i++) {

            // On initialise la case a zero si le poids de l'objet < "poids de la case" sinon on lui donne le poids de l'objet
            if (objetsPossibles.get(0).getPoids() > i) {
                matrice[0][i] = 0;
            } else {
                matrice[0][i] = objetsPossibles.get(0).getValeur();
            }
        }


        // PHASE 2: On remplit la matrice
        //Parcours les lignes de la matrice (on commence a la 2eme ligne)
        for (int i = 1; i < objetsPossibles.size(); i++) {

            //Parcours les colonnes de la matrice
            for (int j = 0; j < poidsMaximal + 1; j++) {

                // Si le poids < "poids de la colonne" de la matrice, on lui donne la valeur de la case d'au dessus
                if (j < objetsPossibles.get(i).getPoids()) {
                    matrice[i][j] = matrice[i - 1][j];
                } else {
                    // Sinon on lui donne la plus grande valeur qu'elle peut prendre entre la case d'au dessus et la somme de la valeur de l'objet actuel et de l'objet precedent si le poids l'autorise
                    matrice[i][j] = Math.max(matrice[i - 1][j], matrice[i - 1][(int) (j - objetsPossibles.get(i).getPoids())] + objetsPossibles.get(i).getValeur());
                }
            }
        }

        // PHASE 3: On récupère la solution
        int lastC = (int) poidsMaximal; // index de la derniere ligne de la matrice
        int lastL = objetsPossibles.size() - 1;   // index de la derniere colonne de la matrice
        valeurOptimale = (float) matrice[lastL][lastC];


        // On parcours la matrice en partant de la derniere case
        for (int i = lastL; i > 0; i--) {

            //Si la case au dessus a la meme valeur, il faut mettre l'objet dans le sac, sinon on ne le met pas
            if (matrice[i][lastC] != matrice[i - 1][lastC]) {

                meilleursObjets.add(objetsPossibles.get(i));

                lastC -= objetsPossibles.get(i).getPoids();
            }
        }

        // On multiplie les poids par dix pour ne pas avoir de problèmes avec les poids < 0 lors de la création du tableau
        poidsMaximal = poidsMaximal / 10f;
        for (Objet o : meilleursObjets) {
            o.setPoids(o.getPoids() / 10f);
        }

    }


    /**
     * Execute l'algorithme de resolution par procedure de separation et evaluation
     */
    private void methodePSE() {
        triGlouton();

        new Arbre(objetsPossibles, poidsMaximal);

        valeurOptimale = Arbre.meilleureValeur;
        meilleursObjets = new ArrayList<>(Arbre.meilleursObjets);
    }

}

