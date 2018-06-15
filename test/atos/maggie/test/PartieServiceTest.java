/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.test;

import atos.maggie.entity.Partie;
import atos.maggie.service.PartieService;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Administrateur 
 */
public class PartieServiceTest {

    private PartieService service = new PartieService();

    @Test
    public void creerNouvellePartieOk() {

        Partie p= service.creerNouvellePartie("blabla");
        assertNotNull(p.getId());
    }

}
