/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.dao;

import atos.maggie.entity.Joueur;
import atos.maggie.entity.Partie;
import java.util.ArrayList;
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
        Query query = em.createQuery(" Select p from Partie p EXCEPT SELECT p from Partie p join p.joueurs j where j.etat=:etat_gagne or j.etat=:etat_alamain");
        query.setParameter("etat_gagne", Joueur.EtatJoueur.GAGNE);
        query.setParameter("etat_alamain", Joueur.EtatJoueur.A_LA_MAIN);
        return query.getResultList();
    }

    public boolean determineSiPlusQueUnJoueurDansPartie(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select j from Joueur j join j.partie p where p.id=:idPartie EXCEPT Select j from Joueur j join j.partie p where p.id=:idPartie AND j.etat=:etatPerdu");
        query.setParameter("idPartie", partieId);
        query.setParameter("etatPerdu", Joueur.EtatJoueur.PERDU);
        List res = query.getResultList();
//        if (res.size() != 1) {
//            return false;
//        }
//        return true;
        return res.size() == 1;

    }

    //ajouter au lieu de creer
    public void ajouterPartie(Partie p) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        em.getTransaction().begin();

        em.persist(p);

        em.getTransaction().commit();
    }

    public Partie recherchePartieParId(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        //Query query = em.createQuery("Select p from Partie p where p.id=: idPartie");
        //query.setParameter("idPartie", partieId);
        //return (Partie) query.getSingleResult();

        //the method find is simpler than make a query
        return em.find(Partie.class, partieId);
    }

    public long recupererNbJoueursParPartieId(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select COUNT(j.id) from Joueur j Join j.partie p where p.id=:idPartie");
        query.setParameter("idPartie", partieId);
        return (long) query.getSingleResult();
    }

}
