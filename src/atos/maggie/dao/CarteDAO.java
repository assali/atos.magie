/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.dao;

import atos.maggie.entity.Carte;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Administrateur
 */
public class CarteDAO {

    public List<Carte> listerCarteParJoueurId(long joueurId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select c from Carte c where c.joueur.id= :idJoueur");
        query.setParameter("idJoueur", joueurId);

        return query.getResultList();
    }

    public void creerNouvelleCarte(Carte c) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        em.getTransaction().begin();

        em.persist(c);

        em.getTransaction().commit();
    }

}
