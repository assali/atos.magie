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

/**
 *
 * @author Administrateur
 */
public class JoueurService {

    private JoueurDAO dao = new JoueurDAO();
    private PartieDAO daoPartie = new PartieDAO();

    public void rejoindrePartie(String pseudo, String avatar, long idPartie) {
//recherche si le joueur existe deja
        //boolean isNew = false;
        Joueur joueur = dao.rechercherParPseudo(pseudo);

        if (joueur == null) {
//Le jouer n'existe pas encore
            joueur = new Joueur();
            joueur.setPseudo(pseudo);
            joueur.setNbPartiejouees(0L);
            joueur.setNbPartiesGagnees(0L);
            //isNew=true;
        }
        joueur.setAvatar(avatar);
        joueur.setEtat(Joueur.EtatJoueur.N_A_PAS_LA_MAIN);
        joueur.setOrdre(dao.rechercheOrdre(idPartie));

        //Do Not Froget to associate in two directions
        Partie partie = daoPartie.RecherchePartieParId(idPartie);
        joueur.setPartie(partie);
        partie.getJoueurs().add(joueur);
//        if (isNew) {
//            
//        }

        if (joueur.getId() == null) {
            dao.ajoute(joueur);
        }
        dao.modifier(joueur);

    }
}
