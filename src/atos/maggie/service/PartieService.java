/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.service;

import atos.maggie.dao.PartieDAO;
import atos.maggie.entity.Partie;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class PartieService {

    private PartieDAO dao = new PartieDAO();

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
        return dao.RecherchePartieParId(partieId);
    }

    public long recupererNbJoueursParPartieId(long partieId) {
        return (long) dao.recupererNbJoueursParPartieId(partieId);

    }

    public void demarrerPartie() {
        dao.demarrerPartie();
    }

}
