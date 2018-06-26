/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.dao;

import atos.maggie.entity.Carte;
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

    public Joueur rechercheJoueurQuiALaMainParPartieId(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select j from Joueur j join j.partie p where j.etat=:etat AND p.id=:idPartie");
        query.setParameter("etat", Joueur.EtatJoueur.A_LA_MAIN);
        query.setParameter("idPartie", partieId);
        return (Joueur) query.getSingleResult();

    }

    public long recupereNbJoueursALaPartie(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select COUNT(j.id) From Joueur j join j.partie p where p.id= :idPartie");
        query.setParameter("idPartie", partieId);
        Object res = query.getSingleResult();
        if (res == null) {
            return 1;
        }
        return (long) res;
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

    public long rechercheMAXOrdre(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select MAX(j.ordre) From Joueur j join j.partie p where p.id=:idPartie");
        query.setParameter("idPartie", partieId);
        return (long) query.getSingleResult();
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

    public Joueur rechercherParId(long joueurId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select j From Joueur j where j.id=:id");
        query.setParameter("id", joueurId);
        List<Joueur> joueursTrouves = query.getResultList();
        if (joueursTrouves.isEmpty()) {
            return null;
        }
        return joueursTrouves.get(0);
    }

    public Joueur rechercherParJoueurIdEtPartieId(long joueurId, long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("select j From Joueur j join j.partie p where p.id=:partieId AND j.id=:id");
        query.setParameter("id", joueurId);
        query.setParameter("partieId", partieId);
        List<Joueur> joueursTrouves = query.getResultList();
        if (joueursTrouves.isEmpty()) {
            return null;
        }
        return joueursTrouves.get(0);
    }

    public Joueur rechercheJoueurParPartieEtOrdre(long partieId, long ordre) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select j From Joueur j join j.partie p where j.ordre=:ordre AND p.id=:idPartie");
        query.setParameter("idPartie", partieId);
        query.setParameter("ordre", ordre);
        return (Joueur) query.getSingleResult();
    }

    public boolean isJoueurALesCartes(List<Carte.Ingredient> cartes, long joueurId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query1 = em.createQuery("Select c from Joueur j join j.cartes c where c.ingredient=:firstCarte AND j.id=:idJoueur");
        query1.setParameter("firstCarte", cartes.get(0));
        query1.setParameter("idJoueur", joueurId);
        if (!query1.getResultList().isEmpty()) {
            Query query2 = em.createQuery("Select c from Joueur j join j.cartes c where c.ingredient=:secondCarte AND j.id=:idJoueur");
            query2.setParameter("secondCarte", cartes.get(1));
            query2.setParameter("idJoueur", joueurId);
            if (!query2.getResultList().isEmpty()) {

                return true;

            }
            return false;
        } else {
            return false;
        }

    }

    public long recupererNbCartesParJoueurIdPartieId(long joueurId, long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select COUNT(c.id) From Joueur j join j.cartes c join j.partie p where p.id=:idPartie And j.id=:idJoueur");
        query.setParameter("idPartie", partieId);
        query.setParameter("idJoueur", joueurId);
        Object res = query.getSingleResult();
        if (res == null) {
            return 0;
        }
        return (long) res;
    }

}
