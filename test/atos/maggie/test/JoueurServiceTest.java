/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.test;

import atos.maggie.entity.Joueur;
import atos.maggie.entity.Partie;
import atos.maggie.service.JoueurService;
import atos.maggie.service.PartieService;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrateur
 */
public class JoueurServiceTest {

    private JoueurService service = new JoueurService();
    private PartieService partieService = new PartieService();

    @Test
    public void joueurSuivantOK() {
        service.passeJoueurSuivant(1);
    }
    
    //@Test
    public void ordreJoueursOk() {
        Partie nouvellePartie = partieService.creerNouvellePartie("ordreJoueursOk");

        service.rejoindrePartie("A", "A", nouvellePartie.getId());
        service.rejoindrePartie("B", "B", nouvellePartie.getId());
        Joueur j = service.rejoindrePartie("C", "C", nouvellePartie.getId());
        assertEquals(8L, j.getOrdre());
    }

    //@Test
    public void rejoindrePartieOk() {

        service.rejoindrePartie("j1", "blabla", 1);
        service.rejoindrePartie("j2", "blabla", 1);
        service.rejoindrePartie("j3", "blabla", 1);

    }

//    @Test
    public void passTourOk() {
        service.passTour(2, 25);
    }
}
