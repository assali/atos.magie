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
//    @Test
    public void creerNouvellePartieOk() {
        
        Partie p = serviceP.creerNouvellePartie("blabla");
        assertNotNull(p.getId());
    }

    //@Test
    public void demarrerPartieOk() {
        
        serviceP.demarrerPartie(1);
        
    }
    
    @Test
    public void passTour() {
        
        serviceJ.passTour(1, 3);
    }
    
}
