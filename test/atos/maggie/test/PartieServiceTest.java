/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.test;

import atos.maggie.entity.Joueur;
import atos.maggie.entity.Partie;
import atos.maggie.service.CarteService;
import atos.maggie.service.JoueurService;
import atos.maggie.service.PartieService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Administrateur
 */
public class PartieServiceTest {

    private PartieService serviceP = new PartieService();
    private JoueurService serviceJ = new JoueurService();
    private CarteService serviceC = new CarteService();

//    @Before
//    public void setUp(){
//        
//    }
    //@Test
    public void creerNouvellePartieOk() {

        Partie p = serviceP.creerNouvellePartie("blabla");
        assertNotNull(p.getId());
    }

    @Test
    public void demarrerPartieOk() {
        //recherche partie par son id en DB
        long id = serviceP.creerNouvellePartie("partie777").getId();

        //Rejoindre 3 Joueurs
        List<Joueur> listJoueurs = new ArrayList<>();
        Joueur j1 = serviceJ.rejoindrePartie("AAA", "AAA", id);
        Joueur j2 = serviceJ.rejoindrePartie("BBBB", "BBBB", id);
        Joueur j3 = serviceJ.rejoindrePartie("CCC", "CCC", id);
        
        listJoueurs.add(j1);
        listJoueurs.add(j2);
        listJoueurs.add(j3);

        //Erreur si pas au moins 2 joueurs dans la partie
        if (serviceP.recupererNbJoueursParPartieId(id) < 2) {
            System.err.println("Erreur : nb joueurs moins 2");
        } 
        else
        {
            // passe le joueur d'ordre 1 à etat a la main
            serviceJ.passeJoueurOrdre1EtatALaMain(id);

            //distribue 7 cartes d'ingrédients au hasard à chaque joueur de la partie
            for (Joueur j : listJoueurs) {
                serviceC.distribue7CartesParJoueurId(j.getId());
            }

        }
        
    }

}
