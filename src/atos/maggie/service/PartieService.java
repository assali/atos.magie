/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.service;

import atos.maggie.dao.JoueurDAO;
import atos.maggie.dao.PartieDAO;
import atos.maggie.entity.Joueur;
import atos.maggie.entity.Partie;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class PartieService {

    private PartieDAO dao = new PartieDAO();
    private JoueurDAO daoJ = new JoueurDAO();
    private JoueurService jService = new JoueurService();
    private CarteService cService = new CarteService();

    /**
     * Liste les parties dont aucun joueur n'est à l'etat a la main ou gagne
     *
     * @return
     */
    public List<Partie> listerPartiesNonDemarrees() {
        return dao.listerPartiesNonDemarrees();

    }

    //we pass the  paramters 
    public Partie creerNouvellePartie(String nom) {
        Partie p = new Partie();
        p.setNom(nom);
        dao.ajouterPartie(p);
        return p;
    }

    public Partie recupererPartieparId(long partieId) {
        return dao.recherchePartieParId(partieId);
    }

    public long recupererNbJoueursParPartieId(long partieId) {
        return (long) dao.recupererNbJoueursParPartieId(partieId);

    }

    public void demarrerPartie(long idPartie) {

        // recherche partie par son id en DB
        Partie p = dao.recherchePartieParId(idPartie);

        // Erreur si pas au moins 2 joueurs dans la partie
        if (recupererNbJoueursParPartieId(idPartie) < 2) {
            throw new RuntimeException("Erreur : nb joueurs moins 2");
        }

        // passe le joueur d'ordre 1 à etat a la main
        jService.passeJoueurOrdre1EtatALaMain(idPartie);

        // distribue 7 cartes d'ingrédients au hasard à chaque joueur de la partie
        for (Joueur j : p.getJoueurs()) {

            cService.distribue7CartesParJoueurId(j.getId());
        }

    }

    public boolean finPartie(long idPartie) {
        Partie p = dao.recherchePartieParId(idPartie);
        boolean partieEncours = true;
        int nbJoueursPerdus = 0;
        for (int i = 0; i < p.getJoueurs().size(); i++) {
            if (p.getJoueurs().get(i).getEtat() == Joueur.EtatJoueur.PERDU) {
                nbJoueursPerdus++;
            }

        }
        if (nbJoueursPerdus == p.getJoueurs().size() - 1) {
            partieEncours = false;
        }
        return partieEncours;
    }
    

}
