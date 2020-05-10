package types;

/**
 * Represente un objet qui peut etre mis dans le sac a dos
 *
 * @author Oscar et Jeneifan
 * @version 2.0
 */
public class Objet implements Comparable<Objet> {

    private float poids;
    private float valeur;
    private String nom;


    /**
     * Construit un objet
     * @param nom : nom de l'objet
     * @param poids : poids de l'objet
     * @param valeur : valeur de l'objet
     */
    Objet(String nom, float poids, float valeur) {
        assert poids >= 0.0 && valeur >=0.0;

        this.poids = poids;
        this.valeur = valeur;
        this.nom = nom;
    }


    /**
     * Modifie le poids de l'objet avec le poids spécifié en paramètre
     * @param poids : le poids de l'objet
     */
    void setPoids(float poids) {
        this.poids = poids;
    }


    /**
     * Renvoie le poids de l'objet
     * @return le poids de l'objet
     */
    float getPoids() {
        return poids;
    }


    /**
     * Renvoie la valeur de l'objet
     * @return la valeur de l'objet
     */
    float getValeur() {
        return valeur;
    }


    /**
     * Calcule et renvoi le rapport de l'objet
     * @return le rapport (valeur/poids) de l'objet
     */
    private float getRapport() {
        return valeur / poids;
    }


    /**
     * Compare deux objets en fonction de leur rapport
     * @param o : types.Objet que l'on veut comparer
     * @return si l'objet est plus grand, plus petit ou egal
     */
    @Override
    public int compareTo(Objet o) {
        float resultatComparaison = getRapport() - o.getRapport();

        if (resultatComparaison > 0) {
            return -1;
        } else if (resultatComparaison < 0) {
            return 1;
        } else {
            return 0;
        }
    }


    /**
     * Affiche le nom, la valeur et le poids de l'objet
     * @return le nom et la valeur et le poids de l'objet
     */
    @Override
    public String toString() {
        return this.nom + ": " + this.valeur + "$ : " + this.poids + "Kg";
    }
}
