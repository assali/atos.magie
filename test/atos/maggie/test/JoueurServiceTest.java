/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.test;

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
    public void rejoindrePartieOk() {

        long id = partieService.creerNouvellePartie("nnn").getId();
        service.rejoindrePartie("thomas", "blabla", id);

    }

}
