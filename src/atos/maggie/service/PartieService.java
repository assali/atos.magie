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
import java.util.Random;

/**
 *
 * @author Administrateur
 */
public class PartieService {

    private PartieDAO dao = new PartieDAO();
    private JoueurDAO daoJ = new JoueurDAO();
    private CarteDAO daoC = new CarteDAO();
    private JoueurService jService = new JoueurService();
    private CarteService cService = new CarteService();
    private Map<String, List<Carte.Ingredient>> sort = new HashMap<>();
    // private PartieService servPartie = new PartieService();

    public Map<String, List<Carte.Ingredient>> sortsCollection() {
        List<Carte.Ingredient> sort1 = new ArrayList<>();
        sort1.add(Carte.Ingredient.CORNE_DE_LICORNE);
        sort1.add(Carte.Ingredient.BAVE_DE_CRAPAUD);

        List<Carte.Ingredient> sort2 = new ArrayList<>();
        sort2.add(Carte.Ingredient.CORNE_DE_LICORNE);
        sort2.add(Carte.Ingredient.MANDRAGORE);

        List<Carte.Ingredient> sort3 = new ArrayList<>();
        sort3.add(Carte.Ingredient.BAVE_DE_CRAPAUD);
        sort3.add(Carte.Ingredient.LAPIS_LAZULI);

        List<Carte.Ingredient> sort4 = new ArrayList<>();
        sort4.add(Carte.Ingredient.LAPIS_LAZULI);
        sort4.add(Carte.Ingredient.AILE_DE_CHAUVE_SOURIS);

        List<Carte.Ingredient> sort5 = new ArrayList<>();
        sort5.add(Carte.Ingredient.MANDRAGORE);
        sort5.add(Carte.Ingredient.AILE_DE_CHAUVE_SOURIS);

        sort.put("INVISIBILITE", sort1);
        sort.put("PHILTRE DAMOUR", sort2);
        sort.put("HYPNOSE", sort3);
        sort.put("DIVINATION", sort4);
        sort.put("SOMMEIL PROFOND", sort5);

        return sort;

    }

    /**
     * Liste les parties dont aucun joueur n'est à l'etat a la main ou gagne
     *
     * @return
     */
    public List<Partie> listerPartiesNonDemarrees() {
        return dao.listerPartiesNonDemarrees();

    }

    public List<Partie> listerPartiesDemarrees() {
        return dao.listerPartiesDemarres();

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

            cService.distribue7CartesParJoueurIdEtPartieId(j.getId(), idPartie);
        }

    }

    public boolean finPartie(long idPartie) {
        Partie p = dao.recherchePartieParId(idPartie);
        boolean partieFini = false;
        int nbJoueursPerdus = 0;
        boolean someonewon = false;
        for (int i = 0; i < p.getJoueurs().size(); i++) {
            if (p.getJoueurs().get(i).getEtat() == Joueur.EtatJoueur.PERDU) {
                nbJoueursPerdus++;
            }
            if (p.getJoueurs().get(i).getEtat() == Joueur.EtatJoueur.GAGNE) {
                someonewon = true;
            }

        }
        if ((nbJoueursPerdus == p.getJoueurs().size() - 1) && (someonewon == true)) {
            partieFini = true;
        }
        return partieFini;
    }

    public List<Joueur> listerJoueursParPartieId(long partieId) {
        return dao.listerJoueursParPartieId(partieId);
    }

    public void sortSommeilProfond(long partieId, long joueurId, long victimId) {
        // Joueur joueurQuiLance = daoJ.rechercherParJoueurIdEtPartieId(joueurId, partieId);
        Joueur joueurVictim = daoJ.rechercherParJoueurIdEtPartieId(victimId, partieId);
        joueurVictim.setEtat(Joueur.EtatJoueur.SOMMEIL_PROFOND);
        daoJ.modifier(joueurVictim);
    }

    public List<Joueur> sortDivination(long partieId, long joueurId) {
        //Joueur joueurQuiLance = daoJ.rechercherParJoueurIdEtPartieId(joueurId, partieId);

        //List<Joueur> res = new ArrayList<>();
        //res = 
        return dao.listerAdversairesParPartieIdEtJoueurId(joueurId, partieId);

        //return res;
    }

    public void sortHypnose(long partieId, long joueurId, long victimId, long carteId) {

        Joueur joueurQuiLance = daoJ.rechercherParJoueurIdEtPartieId(joueurId, partieId);

        Joueur joueurVictim = daoJ.rechercherParJoueurIdEtPartieId(victimId, partieId);
        List<Carte> victimCartes = cService.listerCarteParJoueurId(victimId);
        List<Carte> CartesAuHasard = new ArrayList<>();
        Carte cartePourEchange = daoC.rechercheParId(carteId);

        if (victimCartes.size() < 3) {
            throw new RuntimeException("Erreur : victim possède moins de 3 cartes");
        } else {
            int arriveAMax = 0;
            Random r = new Random();
            int num;
            while (arriveAMax != 3) {
                num = r.nextInt(victimCartes.size());
                if (victimCartes.get(num).getIsChosen() != 1) {
                    victimCartes.get(num).setIsChosen(1);
                    daoC.modifierCarte(victimCartes.get(num));
                    CartesAuHasard.add(victimCartes.get(num));
                    arriveAMax++;
                }
            }
            num = r.nextInt(3);
            Carte c = CartesAuHasard.get(num);
            cartePourEchange.setJoueur(joueurVictim);
            daoC.modifierCarte(cartePourEchange);

            c.setJoueur(joueurQuiLance);
            c.setIsChosen(0);
            daoC.modifierCarte(c);

            for (Carte carte : CartesAuHasard) {
                carte.setIsChosen(0);
                daoC.modifierCarte(carte);
            }

        }
    }

    public void sortPhiltreAmour(long partieId, long joueurId, long victimId) {

        Joueur joueurQuiLance = daoJ.rechercherParJoueurIdEtPartieId(joueurId, partieId);
        Joueur joueurVictim = daoJ.rechercherParJoueurIdEtPartieId(victimId, partieId);
        List<Carte> victimCartes = cService.listerCarteParJoueurId(victimId);
        List<Carte> CartesAuHasard = new ArrayList<>();
        double moitie = 0;

        if (victimCartes.size() == 1) {
            joueurVictim.setEtat(Joueur.EtatJoueur.PERDU);
            Carte c = joueurVictim.getCartes().get(0);
            c.setJoueur(joueurQuiLance);
            joueurQuiLance.getCartes().add(joueurVictim.getCartes().get(0));
            daoC.modifierCarte(c);
            daoJ.modifier(joueurVictim);
            return;
        }
        if (victimCartes.size() % 2 == 0) {
            moitie = victimCartes.size() / 2;
        } else {
            moitie = Math.ceil((double) victimCartes.size() / 2);
        }
        int arriveAMax = 0;
        Random r = new Random();
        int num;
        while (arriveAMax != moitie) {
            num = r.nextInt(victimCartes.size());
            if (victimCartes.get(num).getIsChosen() != 1) {
                victimCartes.get(num).setIsChosen(1);

                daoC.modifierCarte(victimCartes.get(num));
                CartesAuHasard.add(victimCartes.get(num));
                arriveAMax++;
            }
        }
        for (Carte carte : CartesAuHasard) {

            carte.setJoueur(joueurQuiLance);
            carte.setIsChosen(0);
            joueurQuiLance.getCartes().add(carte);
            daoC.modifierCarte(carte);

        }
    }

    public void sortInvisibilite(long partieId, long joueurId) {
        Joueur joueurQuiLance = daoJ.rechercherParJoueurIdEtPartieId(joueurId, partieId);

        List<Joueur> adversaires = dao.listerAdversairesParPartieIdEtJoueurId(joueurId, partieId);
        List<Carte> CartesAuHasard = new ArrayList<>();
        Random r = new Random();
        int num;
        for (Joueur adversaire : adversaires) {
            num = r.nextInt(adversaire.getCartes().size());
            CartesAuHasard.add(adversaire.getCartes().get(num));

        }
        for (Carte carte : CartesAuHasard) {
            carte.setJoueur(joueurQuiLance);
            joueurQuiLance.getCartes().add(carte);
            daoC.modifierCarte(carte);

        }

    }

}
