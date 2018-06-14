/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.service;

import atos.maggie.dao.PartieDAO;
import atos.maggie.entity.Partie;
import java.util.List;

/**
 *
 * @author Administrateur
 */
public class PartieService {

    private PartieDAO dao = new PartieDAO();

    public List<Partie> listerPartiesNonDemarrees() {
        return dao.listerPartiesNonDemarrees();

    }

}
