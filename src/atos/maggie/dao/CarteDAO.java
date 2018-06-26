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

    public void ajouterNouvelleCarte(Carte c) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        em.getTransaction().begin();

        em.persist(c);

        em.getTransaction().commit();
    }

    public void modifierCarte(Carte c) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        em.getTransaction().begin();

        em.merge(c);

        em.getTransaction().commit();
    }

    public Carte rechercheParId(long carteId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select c from Carte c where c.id=:id");
        query.setParameter("id", carteId);

        return (Carte) query.getSingleResult();
    }

    public Carte recupererCarteParJouerIdEtIngredient(long joueurId, Carte.Ingredient ingredient) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select c from Carte c join c.joueur j where j.id=:idJoueur AND c.ingredient=:ing");
        query.setParameter("idJoueur", joueurId);
        query.setParameter("ing", ingredient);
        List<Carte> cs = query.getResultList();
        return cs.get(0);

    }

    public void supprimerCarte(long carteId) {

        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Delete From Carte c where c.id=:idCarte");
        query.setParameter("idCarte", carteId);
        em.getTransaction().begin();
        query.executeUpdate();

        em.getTransaction().commit();

    }

}
