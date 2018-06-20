/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.service;

import atos.maggie.dao.CarteDAO;
import atos.maggie.dao.JoueurDAO;
import atos.maggie.dao.PartieDAO;
import atos.maggie.entity.Joueur;
import atos.maggie.entity.Partie;

/**
 *
 * @author Administrateur
 */
public class JoueurService {

    private JoueurDAO daoJoueur = new JoueurDAO();
    private PartieDAO daoPartie = new PartieDAO();
    private CarteDAO daoCarte = new CarteDAO();
    // private CarteService servCarte = new CarteService();
    // private PartieService servPartie = new PartieService();

    public Joueur rejoindrePartie(String pseudo, String avatar, long idPartie) {
//recherche si le joueur existe deja
        //boolean isNew = false;
        Joueur joueur = daoJoueur.rechercherParPseudo(pseudo);

        if (joueur == null) {
//Le jouer n'existe pas encore
            joueur = new Joueur();
            joueur.setPseudo(pseudo);
            joueur.setNbPartiejouees(0L);
            joueur.setNbPartiesGagnees(0L);
        }
        joueur.setAvatar(avatar);
        joueur.setEtat(Joueur.EtatJoueur.N_A_PAS_LA_MAIN);
        joueur.setOrdre(daoJoueur.rechercheOrdre(idPartie));

        //Do Not Froget to associate in two directions
        Partie partie = daoPartie.recherchePartieParId(idPartie);
        joueur.setPartie(partie);
        partie.getJoueurs().add(joueur);

        if (joueur.getId() == null) {
            daoJoueur.ajoute(joueur);
        } else {
            daoJoueur.modifier(joueur);
        }
        return joueur;
    }

    //Passe Tour
//    public void passTour(long partieId, long joueurId) {
//        passeJoueurSuivant(partieId);
//        servCarte.distribueCarteParJoueurId(joueurId);
//    }
    //joueur suivant
    public void passeJoueurSuivant(long partieId) {

        //1- recuperer joueur qui a la main = joueurQuiALaMain
        Joueur joueurQuiALaMain = daoJoueur.rechercheJoueurQuiALaMainParPartieId(partieId);

        //2- Determine si tous autres joueurs ont perdu
        //et passe le joueur a l'etat gagné si c'est le cas 
        //puis quitte la fonction
        //une autre solution  
        //if (servPartie.finPartie(partieId))
        if (daoPartie.determineSiPlusQueUnJoueurDansPartie(partieId)) {
            joueurQuiALaMain.setEtat(Joueur.EtatJoueur.GAGNE);
            daoJoueur.modifier(joueurQuiALaMain);
            return;
        }
        //  else {
        //la partie n'est pas fini
        //3-recuperer ordre Max des joueurs de la partie
        long ordreMax = daoJoueur.rechercheMAXOrdre(partieId);
        //joueurEvalue= joueurQuiAlaMain
        Joueur joueurEvalue = joueurQuiALaMain;
        //long ordreProchain;
        // boolean prochainNonTrouve = true;
        while (true) {

            //si joueurEvalue est le dernier joueur alors on evalue
            if (joueurEvalue.getOrdre() >= ordreMax) {
                //ordreProchain = 1;
                daoJoueur.rechercheJoueurParPartieEtOrdre(partieId, 1L);
            } else {
                //ordreProchain = joueurQuiALaMain.getOrdre() + 1;
                daoJoueur.rechercheJoueurParPartieEtOrdre(partieId, joueurEvalue.getOrdre() + 1);
            }
            //Joueur prochain = daoJoueur.recupererJoueurProchain(partieId, ordreProchain);

            //Return si tout les joueurs non étiminés etaient en sommeil profond et q'on la  a juste réveillés
            if (joueurEvalue.getId() == joueurQuiALaMain.getId()) {
                return;
            }
            if (joueurEvalue.getEtat() == Joueur.EtatJoueur.SOMMEIL_PROFOND) {
                joueurEvalue.setEtat(Joueur.EtatJoueur.N_A_PAS_LA_MAIN);
                daoJoueur.modifier(joueurEvalue);
                //prochainNonTrouve = true;
                //si joueurEvalue pas la main alors c'est lui qui prend la main
            } else if (joueurEvalue.getEtat() == Joueur.EtatJoueur.N_A_PAS_LA_MAIN) {
                joueurEvalue.setEtat(Joueur.EtatJoueur.A_LA_MAIN);
                daoJoueur.modifier(joueurEvalue);
                joueurQuiALaMain.setEtat(Joueur.EtatJoueur.N_A_PAS_LA_MAIN);
                daoJoueur.modifier(joueurQuiALaMain);
                // prochainNonTrouve = false;
                return;
            }
        }
        // }
    }

//    public boolean isJoueurPerdu(long joueurId) {
//        Joueur j = daoJoueur.rechercherParId(joueurId);
//        if (j.getCartes().size() == 0) {
//            return true;
//        }
//        return false;
//
//    }
    void passeJoueurOrdre1EtatALaMain(long idPartie) {
        Joueur j = daoJoueur.rechercheJoueurParPartieEtOrdre(idPartie, 1L);
        j.setEtat(Joueur.EtatJoueur.A_LA_MAIN);
        daoJoueur.modifier(j);
    }
}
