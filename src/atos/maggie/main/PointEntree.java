/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.main;

import atos.maggie.entity.Partie;
import atos.maggie.service.JoueurService;
import atos.maggie.service.PartieService;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Administrateur
 */
public class PointEntree {

    private PartieService serviceP = new PartieService();
    private JoueurService serviceJ = new JoueurService();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PointEntree m = new PointEntree();
        m.menuPrincipal();

    }

    public void menuPrincipal() {
        Partie p;
        List<Partie> partiesNondemarrees;
        String partieRejoindr;
        String partieDemarrer;
        String choix;
        do {
            System.out.println("****************************");
            System.out.println("Maggie Maggie");
            System.out.println("****************************");
            System.out.println("Tapez le nombre de votre choix");
            System.out.println("1- creer une Partie");
            System.out.println("2- Rejoindre une partie");
            System.out.println("3- Demarrer une partie");
            System.out.println("4- Lister les Parties non démarrées ");
            System.out.println("Q- quitter ");
            System.out.println("****************************");
            System.out.print("votre choix > ");
            Scanner s = new Scanner(System.in);
            choix = s.nextLine();

            switch (choix) {
                case "1":
                    System.out.println("Tapez le nom de cette partie ");
                    String nomPartie = s.nextLine();
                    serviceP.creerNouvellePartie(nomPartie);
                    break;
                case "2":
                    System.out.println("Tapez votre Pseudo ");
                    String pseudoJoueur = s.nextLine();
                    System.out.println("Tapez votre avatar ");
                    String avatarJoueur = s.nextLine();
                    System.out.println("Tapez le nombre de partie que vous voulez rejoindre ");
                    //
                    partiesNondemarrees = serviceP.listerPartiesNonDemarrees();
                    for (Partie partie : partiesNondemarrees) {
                        System.err.println(partie.getId().toString() + "  " + partie.getNom());
                    }
                    partieRejoindr = s.nextLine();
                    serviceJ.rejoindrePartie(pseudoJoueur, avatarJoueur, Long.parseLong(partieRejoindr));
                    break;
                case "3":
                    System.out.println("Tapez le nombre de partie que vous voulez Demarrer ");
                    //
                    partiesNondemarrees = serviceP.listerPartiesNonDemarrees();
                    for (Partie partie : partiesNondemarrees) {
                        System.err.println(partie.getId().toString() + "  " + partie.getNom());
                    }
                    partieDemarrer = s.nextLine();
                    serviceP.demarrerPartie(Long.parseLong(partieDemarrer));
                    break;
                case "4":
                    partiesNondemarrees = serviceP.listerPartiesNonDemarrees();
                    for (Partie partie : partiesNondemarrees) {
                        System.err.println(partie.getId().toString() + "  " + partie.getNom());
                    }
                    break;
                case "Q":
                case "q":
                    break;
                default:
                    System.out.println("choix inconnu");
                    break;

            }
        } while (choix.equals("Q") == false);

    }

}
