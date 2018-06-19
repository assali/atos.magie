/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.service;

import atos.maggie.dao.CarteDAO;
import atos.maggie.dao.JoueurDAO;
import atos.maggie.entity.Carte;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Administrateur
 */
public class CarteService {
    
    private CarteDAO dao = new CarteDAO();
    private JoueurDAO daoJ = new JoueurDAO();
    
    public Carte distribueCarteParJoueurId(long joeurId) {
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

        c.setJoueurProprietaire(daoJ.rechercherParId(joeurId));
        dao.ajouterNouvelleCarte(c);
        return c;
    }
    
    public void distribue7CartesParJoueurId(long joueurId) {
        for (int i = 0; i < 7; i++) {
            distribueCarteParJoueurId(joueurId);
        }
    }
    
    public List<Carte> listerCarteParJoueurId(long joueurId) {
        return dao.listerCarteParJoueurId(joueurId);
    }
    
    public void creerNouvelleCarte(Carte c) {
        dao.ajouterNouvelleCarte(c);
    }
    
}
