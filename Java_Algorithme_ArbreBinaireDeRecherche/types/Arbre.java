package types;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe representant un arbre binaire
 *
 * @author Oscar et Jeneifan
 * @version 6.0
 */
class Arbre {

    static float meilleureValeur;
    static List<Objet> meilleursObjets;
    private static float capaciteMaxSac;

    private List<Objet> objsRestants;
    private List<Objet> objs;


    /**
     * Constructeur pour creer l'arbre racine
     *
     * @param objsPossibles : liste d'objets que l'ont peut mettre dans le sac
     * @param capaciteSac   : capacite maximale du sac (poids)
     */
    Arbre(List<Objet> objsPossibles, float capaciteSac) {

        this.objsRestants = new ArrayList<>(objsPossibles);
        this.objs = new ArrayList<>();
        capaciteMaxSac = capaciteSac;

        ConstruireSousBranches();
    }


    /**
     * Effectue des appels recursif au constructeur pour creer l'arbre et ses branches
     */
    private void ConstruireSousBranches() {

        Arbre filsGauche, filsDroit;

        if (!this.objsRestants.isEmpty() && peuxAjouterProchainObjet() && calculerBorneSuperieure() > meilleureValeur) {
            filsGauche = new Arbre(this.objsRestants, this.objs, true);
            filsDroit = new Arbre(this.objsRestants, this.objs, false);

        } else if (!this.objsRestants.isEmpty() && !peuxAjouterProchainObjet() && calculerBorneSuperieure() > meilleureValeur) {
            filsGauche = new Arbre(this.objsRestants, this.objs, true);
            filsDroit = null;

        } else {
            filsGauche = null;
            filsDroit = null;
        }
    }


    /**
     * Constructeur pour construire les sous-arbres (branches) de l'arbre initial de façon recursive
     *
     * @param objsRestants : liste d'objets restants (pour borne superieure) de l'arbre pere
     * @param objs         : liste d'objets contenus par l'arbre pere
     * @param arbreGauche : variable indiquant si l'arbre est un fils gauche ou un fils droit
     */
    private Arbre(List<Objet> objsRestants, List<Objet> objs, boolean arbreGauche) {

        // On lui donne les memes objets que l'arbre pere
        this.objsRestants = new ArrayList<>(objsRestants);
        this.objs = new ArrayList<>(objs);


        if (arbreGauche) {
            // On garde la meme liste que l'arbre pere pour le fils gauche
            this.objsRestants.remove(this.objsRestants.get(0));
        } else {


            // On ajoute l'objet d'après pour le fils droit
            this.objs.add(this.objsRestants.get(0));
            this.objsRestants.remove(this.objsRestants.get(0));
        }

        // Si la nouvelle branche est une meilleure solution, on mets a jour la solution optimale
        MajMeilleureSolution();

        // On fait des appels recursif au consructeur pour crer les branches de l'arbre initial
        ConstruireSousBranches();
    }


    /**
     * Calcule la borne superieure d'un arbre
     *
     * @return la valeur de la borne superieure d'un arbre
     */
    private float calculerBorneSuperieure() {
        float b = 0;

        // Somme des objets dans la branche
        for (Objet o : objs) {
            b += o.getValeur();
        }

        // Somme des objets que l'on peut encore mettre dans les branches fils
        for (Objet o : objsRestants) {
            b += o.getValeur();
        }

        return b;
    }


    /**
     * Calcule la valeur totale de la branche et mets a jour la meilleure solution actuelle si cette branche est la nouvelle meilleure solution
     * Cette methode mets a jour la borne inferieure
     */
    private void MajMeilleureSolution() {
        float bestValeur = 0;

        if (this.objs != null) {
            for (Objet o : this.objs) {
                bestValeur += o.getValeur();
            }
        }


        // Si la valeur de la branche actuelle est > la meilleure valeur, cette branche est la meilleure solution et on mets a jour les variables statiques
        if (bestValeur > meilleureValeur) {
            meilleureValeur = bestValeur;
            meilleursObjets = new ArrayList<>(objs);
        }
    }


    /**
     * Verifie si l'on peut ajouter le prochain objet, (si le poids de l'objet ne depasse pas le poids maximal du sac)
     * @return true si il reste de la place dans le sac pour le prochain objet
     */
    private boolean peuxAjouterProchainObjet() {
        float p = this.objsRestants.get(0).getPoids();  // Prochain objet a ajouter dans l'arbre

        if (!this.objs.isEmpty()) {
            for (Objet o : objs) {
                p += o.getPoids();
            }
        }
        return p <= capaciteMaxSac;
    }

}
