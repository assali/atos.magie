/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.service;

import atos.maggie.dao.CarteDAO;
import atos.maggie.dao.JoueurDAO;
import atos.maggie.dao.PartieDAO;
import atos.maggie.entity.Carte;
import atos.maggie.entity.Joueur;
import atos.maggie.entity.Partie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Administrateur
 */
public class JoueurService {

    private JoueurDAO daoJoueur = new JoueurDAO();
    private PartieDAO daoPartie = new PartieDAO();
    private CarteDAO daoCarte = new CarteDAO();
    private CarteService servCarte = new CarteService();

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
        joueur.setNbPartiejouees(joueur.getNbPartiejouees() + 1);

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
    public void passTour(long partieId, long joueurId) {
        passeJoueurSuivant(partieId);
        Joueur j = daoJoueur.rechercherParId(joueurId);
        if (j.getEtat() == Joueur.EtatJoueur.GAGNE) {
            return;
        }
        if (j.getEtat() == Joueur.EtatJoueur.A_LA_MAIN) {
            return;
        }
        servCarte.distribueCarteParJoueurIdEtPartieId(joueurId, partieId);
    }

    //joueur suivant
    public void passeJoueurSuivant(long partieId) {

        //1- recuperer joueur qui a la main = joueurQuiALaMain
        Joueur joueurQuiALaMain = daoJoueur.rechercheJoueurQuiALaMainParPartieId(partieId);

        if (joueurQuiALaMain.getCartes().isEmpty()) {
            joueurQuiALaMain.setEtat(Joueur.EtatJoueur.PERDU);
            daoJoueur.modifier(joueurQuiALaMain);
        }
        //2- Determine si tous autres joueurs ont perdu
        //et passe le joueur a l'etat gagné si c'est le cas 
        //puis quitte la fonction
        //une autre solution  
        //if (servPartie.finPartie(partieId))
        if (daoPartie.determineSiPlusQueUnJoueurDansPartie(partieId)) {
            joueurQuiALaMain.setEtat(Joueur.EtatJoueur.GAGNE);
            joueurQuiALaMain.setNbPartiesGagnees(joueurQuiALaMain.getNbPartiesGagnees() + 1);
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
                joueurEvalue = daoJoueur.rechercheJoueurParPartieEtOrdre(partieId, 1L);
            } else {
                //ordreProchain = joueurQuiALaMain.getOrdre() + 1;
                joueurEvalue = daoJoueur.rechercheJoueurParPartieEtOrdre(partieId, joueurEvalue.getOrdre() + 1);
            }
            //Joueur prochain = daoJoueur.recupererJoueurProchain(partieId, ordreProchain);

            //Return si tout les joueurs non étiminés etaient en sommeil profond et q'on la  a juste réveillés
            if (joueurEvalue.getId() == joueurQuiALaMain.getId()) {
//                joueurEvalue.setEtat(Joueur.EtatJoueur.A_LA_MAIN);
//                daoJoueur.modifier(joueurEvalue);
                return;
            }

            if (joueurEvalue.getEtat() == Joueur.EtatJoueur.SOMMEIL_PROFOND) {
                joueurEvalue.setEtat(Joueur.EtatJoueur.N_A_PAS_LA_MAIN);
                daoJoueur.modifier(joueurEvalue);
                //prochainNonTrouve = true;
                //si joueurEvalue pas la main alors c'est lui qui prend la main
            } else if (joueurEvalue.getEtat() == Joueur.EtatJoueur.N_A_PAS_LA_MAIN) {
                if (joueurEvalue.getCartes().isEmpty()) {
                    joueurEvalue.setEtat(Joueur.EtatJoueur.PERDU);
                    daoJoueur.modifier(joueurEvalue);
                } else {
                    joueurEvalue.setEtat(Joueur.EtatJoueur.A_LA_MAIN);
                    daoJoueur.modifier(joueurEvalue);
                }
                //if the user doesnt have any cart
                if (joueurQuiALaMain.getCartes().isEmpty()) {
                    joueurQuiALaMain.setEtat(Joueur.EtatJoueur.PERDU);
                    daoJoueur.modifier(joueurQuiALaMain);
                } else if (daoPartie.determineSiPlusQueUnJoueurDansPartie(partieId)) {
                    joueurQuiALaMain.setEtat(Joueur.EtatJoueur.GAGNE);
                    joueurQuiALaMain.setNbPartiesGagnees(joueurQuiALaMain.getNbPartiesGagnees() + 1);
                    daoJoueur.modifier(joueurQuiALaMain);
                    //return;
                } else {
                    joueurQuiALaMain.setEtat(Joueur.EtatJoueur.N_A_PAS_LA_MAIN);
                    daoJoueur.modifier(joueurQuiALaMain);
                }
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
    public Joueur joueurQuiALaMain(long partieId) {
        return daoJoueur.rechercheJoueurQuiALaMainParPartieId(partieId);
    }

    void passeJoueurOrdre1EtatALaMain(long idPartie) {
        Joueur j = daoJoueur.rechercheJoueurParPartieEtOrdre(idPartie, 1L);
        j.setEtat(Joueur.EtatJoueur.A_LA_MAIN);
        daoJoueur.modifier(j);
    }

    public Joueur rechercheJoueurParId(long joueurId) {
        return daoJoueur.rechercherParId(joueurId);
    }

    public List<Joueur> recupererMesAdversaires(long partieId, long joueurId) {
        return daoPartie.listerAdversairesParPartieIdEtJoueurId(joueurId, partieId);
    }

    public boolean isJoueurALesCartes(List<Carte.Ingredient> cartes, long joueurId) {
        return daoJoueur.isJoueurALesCartes(cartes, joueurId);
    }

    public long recupererNbCartesParJoueurIdPartieId(long joueurId, long partieId) {
        return daoJoueur.recupererNbCartesParJoueurIdPartieId(joueurId, partieId);
    }

}
