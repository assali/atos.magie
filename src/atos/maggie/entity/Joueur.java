/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author Administrateur
 */
@Entity
public class Joueur implements Serializable {

    public enum EtatJoueur {
        N_A_PAS_LA_MAIN,
        A_LA_MAIN,
        SOMMEIL_PROFOND,
        PERDU

    }
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private EtatJoueur etat;

    @Column(unique = true)
    private String pseudo;

   // @Column
    private String avatar;

    //to avoid null value when we calculate
    @Column(nullable = false)
    private long nbPartiesGagnees;

    @Column(nullable = false)
    private long nbPartiejouees;

    @OneToMany(mappedBy = "joueur")
    private List<Carte> cartes = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private Partie partie;

    public EtatJoueur getEtat() {
        return etat;
    }

    public void setEtat(EtatJoueur etat) {
        this.etat = etat;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getNbPartiesGagnees() {
        return nbPartiesGagnees;
    }

    public void setNbPartiesGagnees(long nbPartiesGagnees) {
        this.nbPartiesGagnees = nbPartiesGagnees;
    }

    public long getNbPartiejouees() {
        return nbPartiejouees;
    }

    public void setNbPartiejouees(long nbPartiejouees) {
        this.nbPartiejouees = nbPartiejouees;
    }

    public List<Carte> getCartes() {
        return cartes;
    }

    public void setCartes(List<Carte> cartes) {
        this.cartes = cartes;
    }

    public Partie getPartieActuelle() {
        return partie;
    }

    public void setPartieActuelle(Partie partieActuelle) {
        this.partie = partieActuelle;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Joueur)) {
            return false;
        }
        Joueur other = (Joueur) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "atos.maggie.entity.Joueur[ id=" + id + " ]";
    }

}
