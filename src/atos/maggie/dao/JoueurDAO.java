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

    public long rechercheJoueurOrdre1(long partieId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select j.id From Joueur j join j.partie p where j.ordre=1 AND p.id=:idPartie");
        query.setParameter("idPartie", partieId);
        return (long) query.getSingleResult();
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
//Demarrer Partie

    public void passeJoueurOrdre1EtatALaMain(long partieId) {
        long id = rechercheJoueurOrdre1(partieId);
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        em.getTransaction().begin();
        Joueur j = rechercherParId(id);
        j.setEtat(Joueur.EtatJoueur.A_LA_MAIN);
        em.merge(j);
        em.getTransaction().commit();;

    }

    //Passe Tour
    public Joueur joueurSuivantModifierEtat(long ordreProchain) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Query query = em.createQuery("Select j.id from Joueur j where j.ordre=:ordreProchain");
        query.setParameter("ordreProchain", ordreProchain);
        List<Joueur> joueursTrouves = query.getResultList();
        if (joueursTrouves.isEmpty()) {
            return null;
        } else {
            Joueur j = joueursTrouves.get(0);
            j.setEtat(Joueur.EtatJoueur.A_LA_MAIN);
            modifier(j);

            return j;
        }

    }

    public void passeTour(long joueurId) {
        EntityManager em = Persistence.createEntityManagerFactory("PU").createEntityManager();
        Joueur j = rechercherParId(joueurId);

    }

}
