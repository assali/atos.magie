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
public class JoueurDAO {

    /**
     * Renvoie le joueur dont le pseudo existe en BD, ou renvoie null si pa
     * trouvé
     *
     * @param pseudo à recherche
     * @return
     */
    public Joueur rechercherParPseudo(String pseudo) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select j From Joueur j where j.pseudo= :lePseudo");
        query.setParameter("lePseudo", pseudo);
        List<Joueur> joueursTrouves = query.getResultList();
        if (joueursTrouves.isEmpty()) {
            return null;
        }
        return joueursTrouves.get(0);
    }

    public long rechercheOrdre(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select MAX(j.ordre)+1 From Joueur j join j.partie p where p.id= :idPartie");
        query.setParameter("idPartie", partieId);
        Object res = query.getSingleResult();
        if (res == null) {
            return 1;
        }
        return (long) res;
    }

    public void ajoute(Joueur joueur) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        em.getTransaction().begin();
        em.persist(joueur);
        em.getTransaction().commit();
    }

    public void modifier(Joueur joueur) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        em.getTransaction().begin();
        em.merge(joueur);
        em.getTransaction().commit();
    }

}