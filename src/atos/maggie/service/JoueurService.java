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
    private CarteService servCarte = new CarteService();
    private PartieService servPartie = new PartieService();

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
        }
        daoJoueur.modifier(joueur);
        return joueur;
    }

    public long rechercheJoueurOrdre1(long partieId) {
        return daoJoueur.rechercheJoueurOrdre1(partieId);
    }

    public void passeJoueurOrdre1EtatALaMain(long partieId) {
        daoJoueur.passeJoueurOrdre1EtatALaMain(partieId);
    }

    //Passe Tour
    public void passTour(long partieId, long joueurId) {
        passerAProchain(partieId, joueurId);
        servCarte.distribueCarteParJoueurId(joueurId);

    }

    //joueur prochain
    public void passerAProchain(long partieId, long joueurId) {

        Joueur j = daoJoueur.rechercherParId(joueurId);
       daoJoueur.modifierJoueurEtat(partieId, joueurId, j.getOrdre(), Joueur.EtatJoueur.N_A_PAS_LA_MAIN);
        long ordreProchain;
        boolean prochainNonTrouve = true;
        while (!servPartie.finPartie(partieId)) {
            
            while (prochainNonTrouve) {
              
                if (j.getOrdre() + 1 > daoJoueur.recupereNbJoueursALaPartie(partieId)) {
                    ordreProchain = 1;
                } else {
                    ordreProchain = j.getOrdre() + 1;
                }
                Joueur prochain = daoJoueur.recupererJoueurProchain(partieId, ordreProchain);

                if ((prochain.getEtat() == Joueur.EtatJoueur.PERDU) || (prochain.getEtat() == Joueur.EtatJoueur.SOMMEIL_PROFOND)) {
                    ordreProchain++;

                }
               
            }
             daoJoueur.modifierJoueurEtat(partieId, prochain.getId(), ordreProchain, Joueur.EtatJoueur.A_LA_MAIN);
        }
    }

    public boolean isJoueurPerdu(long joueurId) {
        Joueur j = daoJoueur.rechercherParId(joueurId);
        if (j.getCartes().size() == 0) {
            return true;
        }
        return false;

    }

}
