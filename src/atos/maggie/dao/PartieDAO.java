/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.dao;

import atos.maggie.entity.Joueur;
import atos.maggie.entity.Partie;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Administrateur
 */
public class PartieDAO {

    //we always start with the logic
    public List<Partie> listerPartiesNonDemarrees() {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select p from Partie p EXCEPT SELECT p from partie p join p.joueurs j where j.etat In(:etat_gagne, :etat_alamain)");
        query.setParameter("etat_gagne", Joueur.EtatJoueur.GAGNE);
        query.setParameter("etat_alamain", Joueur.EtatJoueur.A_LA_MAIN);
        return query.getResultList();
    }

    //ajouter au lieu de creer
    public void ajouterPartie(Partie p) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();
    }

    public Partie RecherchePartieParId(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        //Query query = em.createQuery("Select p from Partie p where p.id=: idPartie");
        //query.setParameter("idPartie", partieId);
        //return (Partie) query.getSingleResult();
        
        //the method find is simpler than make a query
        return em.find(Partie.class, partieId);
    }
}