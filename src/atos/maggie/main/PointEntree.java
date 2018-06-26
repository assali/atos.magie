/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package atos.maggie.main;

import atos.maggie.entity.Carte;
import atos.maggie.entity.Joueur;
import atos.maggie.entity.Partie;
import atos.maggie.service.CarteService;
import atos.maggie.service.JoueurService;
import atos.maggie.service.PartieService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author Administrateur
 */
public class PointEntree {

    private PartieService serviceP = new PartieService();
    private JoueurService serviceJ = new JoueurService();
    private CarteService serviceC = new CarteService();
    Partie p;
    Joueur j;
    List<Partie> partiesNondemarrees;
    List<Partie> partiesDemarrees;
    List<Joueur> joueurs;
    List<Carte> cartes;
    List<Joueur> adversaires;
    List<String> sortsPossibles = new ArrayList<>();
    Map<String, List<Carte.Ingredient>> sorts = new HashMap<>();
    Carte c1;
    Carte c2;
    String partieRejoindr;
    String partieDemarrer;
    String partieinteresse;
    String choix;
    String choixS;
    String joueurId;
    String victimId;
    String carteId;
    StringBuffer sb = new StringBuffer();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws InterruptedException {
        PointEntree m = new PointEntree();
        m.menuPrincipal();

    }

    public void ecranJeu(long partieId) throws InterruptedException {

        Scanner s = new Scanner(System.in);
        Joueur joueurQuiALaMain;
        System.out.print("vous êtes Monsieur: Tapez votre ID >  ");
        joueurId = s.nextLine();
        j = serviceJ.rechercheJoueurParId(Long.parseLong(joueurId));
     
        int sortNb = 0;

        while (!serviceP.finPartie(partieId)) {
            if (serviceJ.joueurQuiALaMain(partieId).getId() == j.getId()) {
                System.out.println("lancer or passer");
                System.out.println("S- Lancer un sort");
                System.out.println("P- Passer votre tour");
                System.out.println("L- Lister mes Cartes");
                System.out.print("votre choix > ");
                s = new Scanner(System.in);
                String choix = s.nextLine();
                switch (choix) {
                    case "s":
                        //sort
                        sorts = serviceP.sortsCollection();

                        sb.append("Vos Sorts Possibles:\n");
                        if (serviceJ.isJoueurALesCartes(sorts.get("INVISIBILITE"), j.getId())) {
                            sb.append("i- INVISIBILITE\n");
                            sortNb++;

                        }
                        if (serviceJ.isJoueurALesCartes(sorts.get("PHILTRE DAMOUR"), j.getId())) {
                            sb.append("p- PHILTRE D’AMOUR\n");
                            sortNb++;
                        }
                        if ((serviceJ.isJoueurALesCartes(sorts.get("HYPNOSE"), j.getId())) && (serviceJ.recupererNbCartesParJoueurIdPartieId(j.getId(), p.getId()) >= 3)) {
                            sb.append("h- HYPNOSE\n");
                            sortNb++;
                        }
                        if (serviceJ.isJoueurALesCartes(sorts.get("DIVINATION"), j.getId())) {
                            sb.append("d- DIVINATION\n");
                            sortNb++;
                        }
                        if (serviceJ.isJoueurALesCartes(sorts.get("SOMMEIL PROFOND"), j.getId())) {
                            sb.append("s- SOMMEIL-PROFOND\n");
                            sortNb++;
                        }
                        if (sortNb == 0) {
                            sb.append("vous n'avez pas le droit de lancer un sort  ");
                            System.out.println(sb);
                            sb.delete(0, sb.length() - 1);
                        } else {
                            sb.append("choisissez lettre du sort que vous voulez lancer >  ");
                            System.out.println(sb);
                            sb.delete(0, sb.length() - 1);
                            sortNb = 0;
                            choixS = s.nextLine();
                            switch (choixS) {
                                case "i":
                                    c1 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.CORNE_DE_LICORNE);
                                    c2 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.BAVE_DE_CRAPAUD);
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c1.getId());
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c2.getId());
                                    serviceP.sortInvisibilite(p.getId(), j.getId());
                                    System.out.println("Done !");
                                    break;
                                case "p":
                                    System.out.println("Adversaires List");
                                    adversaires = serviceJ.recupererMesAdversaires(p.getId(), j.getId());
                                    for (Joueur joueur : adversaires) {
                                        System.out.println("Le Joueur  " + joueur.getId() + "  " + joueur.getPseudo());
                                    }
                                    System.out.print("votre victime: Tapez son ID >  ");
                                    victimId = s.nextLine();
                                    c1 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.CORNE_DE_LICORNE);
                                    c2 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.MANDRAGORE);
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c1.getId());
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c2.getId());
                                    serviceP.sortPhiltreAmour(p.getId(), j.getId(), Long.parseLong(victimId));
                                    System.out.println("Done !");
                                    break;
                                case "h":
                                    boolean victimDone = false;
                                    while (!victimDone) {
                                        System.out.println("Adversaires List");
                                        adversaires = serviceJ.recupererMesAdversaires(p.getId(), j.getId());
                                        if (adversaires.isEmpty()) {
                                            System.out.println("vous possédez pas d'adversaires");
                                        } else {
                                            for (Joueur joueur : adversaires) {
                                                System.out.println("Le Joueur  " + joueur.getId() + "  " + joueur.getPseudo());
                                            }
                                            System.out.print("votre victime: Tapez son ID >  ");
                                            victimId = s.nextLine();
                                            if (serviceJ.rechercheJoueurParId(Long.parseLong(victimId)).getCartes().size() < 3) {
                                                //TODO
                                                System.err.println("victim possède moins de 3 cartes");
                                                System.err.println("Choisissez un autre victime");
                                            } else {
                                                victimDone = true;
                                            }
                                        }
                                    }
                                    c1 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.LAPIS_LAZULI);
                                    c2 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.BAVE_DE_CRAPAUD);
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c1.getId());
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c2.getId());
                                    System.out.println("votre Cartes List");
                                    cartes = serviceC.listerCarteParJoueurId(j.getId());
                                    for (Carte carte : cartes) {
                                        System.out.println("Le carte " + carte.getId() + "  " + carte.getIngredient().name());
                                    }
                                    System.out.print("votre carte à echange: Tapez  ID >  ");
                                    carteId = s.nextLine();

                                    serviceP.sortHypnose(p.getId(), j.getId(), Long.parseLong(victimId), Long.parseLong(carteId));
                                    System.out.println("Done !");

                                    break;
                                case "d":
                                    c1 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.LAPIS_LAZULI);
                                    c2 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.AILE_DE_CHAUVE_SOURIS);
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c1.getId());
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c2.getId());
                                    adversaires = serviceP.sortDivination(p.getId(), j.getId());
                                    if (!adversaires.isEmpty()) {
                                        for (Joueur adversaire : adversaires) {
                                            System.out.println("Joueur : " + adversaire.getPseudo() + "  possède  " + adversaire.getCartes().size() + "  Cartes : ");
                                            for (Carte carte : adversaire.getCartes()) {
                                                System.out.println("Carte " + carte.getIngredient().name());
                                            }
                                        }
                                        System.out.println("Done !");
                                    } else {
                                        System.out.println("vous ne possédez pas adversaires ");
                                    }
                                    break;
                                case "s":
                                    System.out.println("Adversaires List");
                                    adversaires = serviceJ.recupererMesAdversaires(Long.parseLong(partieinteresse), Long.parseLong(joueurId));
                                    for (Joueur joueur : adversaires) {
                                        System.out.println("Le Joueur  " + joueur.getId() + "  " + joueur.getPseudo());
                                    }
                                    System.out.print("votre victime: Tapez son ID >  ");
                                    victimId = s.nextLine();
                                    c1 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.MANDRAGORE);
                                    c2 = serviceC.recupererCarteParJouerIdEtIngredient(j.getId(), Carte.Ingredient.AILE_DE_CHAUVE_SOURIS);
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c1.getId());
                                    serviceC.supprimerCarteParJoueurId(j.getId(), c2.getId());
                                    serviceP.sortSommeilProfond(p.getId(), j.getId(), Long.parseLong(victimId));
                                    System.out.println("Done !");
                                    break;
                                default:
                                    System.out.println("choix inconnu");
                                    break;
                            }
                            serviceJ.passeJoueurSuivant(p.getId());
                        }
                        break;
                    case "p":
                        serviceJ.passTour(Long.parseLong(partieinteresse), Long.parseLong(joueurId));
                        break;
                    case "l":
//                        System.out.print("vous êtes Monsieur: Tapez votre ID >  ");
//                        joueurId = s.nextLine();
//                        j = serviceJ.rechercheJoueurParId(Long.parseLong(joueurId));
                        cartes = serviceC.listerCarteParJoueurId(j.getId());
                        sb.append("\nVous avez   " + cartes.size() + "   Cartes :\n");
                        for (Carte carte : cartes) {
                            sb.append("Carte " + carte.getIngredient().name() + "\n");
                        }
                        System.out.println(sb);
                        sb.delete(0, sb.length() - 1);
                        break;
                    default:
                        System.out.println("choix inconnu");
                        break;
                }
            } else {
                Thread.sleep(1000);
            }
        }
        System.out.println("Partie Fini!!!!");
    }

    public void menuPrincipal() throws InterruptedException {
        do {
            System.out.println("****************************");
            System.out.println("Maggie Maggie");
            System.out.println("****************************");
            System.out.println("Tapez le numero de votre choix");
            System.out.println("1- creer une Partie");
            System.out.println("2- Rejoindre une partie");
            System.out.println("3- Demarrer une partie");
            System.out.println("4- Lister les Parties non démarrées ");
            System.out.println("5- Lister Les Joueurs et leur nombre des cartes");
            System.out.println("6- Lister Mes Cartes");
            System.out.println("7- Ecran Jeu");
            System.out.println("Q- quitter ");
            System.out.println("****************************");
            System.out.print("votre choix > ");
            Scanner s = new Scanner(System.in);
            choix = s.nextLine();

            switch (choix) {
                case "1":
                    System.out.print("Tapez le nom de cette partie >  ");
                    String nomPartie = s.nextLine();
                    serviceP.creerNouvellePartie(nomPartie);
                    break;
                case "2":
                    System.out.print("Tapez votre Pseudo > ");
                    String pseudoJoueur = s.nextLine();
                    System.out.print("Tapez votre avatar > ");
                    String avatarJoueur = s.nextLine();
                    partiesNondemarrees = serviceP.listerPartiesNonDemarrees();
                    System.out.println("Parties NON Demarrees");
                    for (Partie partie : partiesNondemarrees) {
                        System.out.println(partie.getId().toString() + "  " + partie.getNom());
                    }
                    System.out.print("Tapez ID de partie que vous voulez rejoindre > ");
                    partieRejoindr = s.nextLine();
                    serviceJ.rejoindrePartie(pseudoJoueur, avatarJoueur, Long.parseLong(partieRejoindr));
                    break;
                case "3":
                    partiesNondemarrees = serviceP.listerPartiesNonDemarrees();
                    System.out.println("Parties NON Demarrees");
                    for (Partie partie : partiesNondemarrees) {
                        System.out.println(partie.getId().toString() + "  " + partie.getNom());
                    }
                    System.out.print("Tapez ID de partie que vous voulez Demarrer >  ");
                    partieDemarrer = s.nextLine();
                    serviceP.demarrerPartie(Long.parseLong(partieDemarrer));
                    break;
                case "4":
                    partiesNondemarrees = serviceP.listerPartiesNonDemarrees();
                    System.out.println("Parties NON Demarrees");
                    for (Partie partie : partiesNondemarrees) {
                        System.out.println(partie.getId().toString() + "  " + partie.getNom());
                    }
                    break;
                case "5":
                    partiesDemarrees = serviceP.listerPartiesDemarrees();
                    System.out.println("Parties Demarrees");
                    for (Partie partie : partiesDemarrees) {
                        System.out.println(partie.getId().toString() + "  " + partie.getNom());
                    }
                    System.out.print("Tapez ID de partie que vous voulez lister ses joueurs et leur nombre des cartes >  ");
                    partieinteresse = s.nextLine();
                    System.out.println("Joueurs List: ");
                    joueurs = serviceP.listerJoueursParPartieId(Long.parseLong(partieinteresse));
                    for (Joueur joueur : joueurs) {
                        System.out.println("Le Joueur  " + joueur.getPseudo() + "   possède  " + joueur.getCartes().size() + "  Cartes");
                    }
                    break;
                case "6":
                    System.out.print("vous êtes Monsieur: Tapez votre ID >  ");
                    joueurId = s.nextLine();
                    j = serviceJ.rechercheJoueurParId(Long.parseLong(joueurId));
                    cartes = serviceC.listerCarteParJoueurId(j.getId());
                    sb.append("\nVous avez   " + cartes.size() + "   Cartes :\n");
                    for (Carte carte : cartes) {
                        sb.append("Carte " + carte.getIngredient().name() + "\n");
                    }
                    System.out.println(sb);
                    sb.delete(0, sb.length() - 1);
                    break;
                case "7":
                    //partie Id 
                    partiesDemarrees = serviceP.listerPartiesDemarrees();
                    System.out.println("Parties Demarrees");
                    for (Partie partie : partiesDemarrees) {
                        System.out.println(partie.getId().toString() + "  " + partie.getNom());
                    }
                    System.out.print("Tapez ID de partie que vous voulez  > ");
                    partieinteresse = s.nextLine();
                    p = serviceP.recupererPartieparId(Long.parseLong(partieinteresse));
                    ecranJeu(p.getId());
                    break;
                case "Q":
                case "q":
                    return;
                //break;
                default:
                    System.out.println("choix inconnu");
                    break;

            }
            System.out.println("");
        } while (choix.equals(
                "Q") == false);

    }

}
