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
import java.util.List;
import java.util.Random;

/**
 *
 * @author Administrateur
 */
public class CarteService {
    
    private CarteDAO dao = new CarteDAO();
    private JoueurDAO daoJ = new JoueurDAO();
    private PartieDAO daoP = new PartieDAO();
    
    public Carte distribueCarteParJoueurIdEtPartieId(long joeurId, long partieId) {
        Joueur j = daoJ.rechercherParId(joeurId);
        Partie p = daoP.recherchePartieParId(partieId);
        Carte c = new Carte();

        //autre solution
        // Carte.Ingredient[] tabIngredients = Carte.Ingredient.values();
        Random r = new Random();
        int num = r.nextInt(5);
        //autre solution
        //int num1 = r.nextInt(tabIngredients.length);
        switch (num) {
            case 0:
                c.setIngredient(Carte.Ingredient.MANDRAGORE);
                break;
            
            case 1:
                c.setIngredient(Carte.Ingredient.AILE_DE_CHAUVE_SOURIS);
                break;
            
            case 2:
                c.setIngredient(Carte.Ingredient.BAVE_DE_CRAPAUD);
                break;
            
            case 3:
                c.setIngredient(Carte.Ingredient.CORNE_DE_LICORNE);
                break;
            
            case 4:
                c.setIngredient(Carte.Ingredient.LAPIS_LAZULI);
                break;
        }
        //autre solution
        //  c.setIngredient(tabIngredients[num1]);

        c.setJoueur(daoJ.rechercherParId(joeurId));
        c.setIsChosen(0);
        dao.ajouterNouvelleCarte(c);

        //here
//        for (Joueur joueur : p.getJoueurs()) {
//            if (joueur.getId() == j.getId()) {
//                joueur.getCartes().add(c);
//            }
//        }
        j.getCartes().add(c);
        daoP.modifier(p);
        daoJ.modifier(j);
        
        return c;
    }
    
    public List<Carte> listerCarteParJoueurId(long joueurId) {
        return dao.listerCarteParJoueurId(joueurId);
    }
    
    public void creerNouvelleCarte(Carte c) {
        dao.ajouterNouvelleCarte(c);
    }
    
    void distribue7CartesParJoueurIdEtPartieId(Long id, long idPartie) {
        for (int i = 0; i < 7; i++) {
            distribueCarteParJoueurIdEtPartieId(id, idPartie);
        }
    }
    
    public Carte recupererCarteParJouerIdEtIngredient(long joueurId, Carte.Ingredient ingredient) {
        return dao.recupererCarteParJouerIdEtIngredient(joueurId, ingredient);
    }


    public void supprimerCarteParJoueurId(long joueurId, Long carteId) {
        dao.supprimerCarte(carteId);
    }
    
}
