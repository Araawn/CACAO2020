package abstraction.eq7Distributeur2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import abstraction.eq8Romu.clients.ClientFinal;
import abstraction.eq8Romu.clients.IDistributeurChocolat;
import abstraction.eq8Romu.clients.IDistributeurChocolatDeMarque;
import abstraction.eq8Romu.produits.Chocolat;
//import abstraction.eq8Romu.produits.Gamme;
//import abstraction.fourni.Filiere;
import abstraction.eq8Romu.produits.ChocolatDeMarque;
import abstraction.eq8Romu.produits.Gamme;
import abstraction.fourni.Filiere;
import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Variable;

public class Vendeur extends AbsVendeur implements IDistributeurChocolatDeMarque, IActeur {

	public Vendeur(Distributeur2 ac) {
		super(ac);
		
	}
	
	public void initialiser() {
		this.coutUnitaire = new HashMap<ChocolatDeMarque, Double>();
		quantiteAVendreParDefaut = ac.stockInitial-ac.getStock().stockLimite;
		for (ChocolatDeMarque choco : ac.tousLesChocolatsDeMarquePossibles()) {
			ajouterProduitAuCatalogue(choco);
			prixChoco.put(choco, new Variable("Prix du " + choco.name(), ac, prixParDefaut.get(choco.getChocolat())));
			quantitesEnVente.put(choco, new Variable("Quantité en vente de " + choco.name(), ac, 0.));  //!!!!!!!!!!! Forcément 0 en stock initial ?
			quantitesACommander.put(choco, new Variable(getNom() + " : " + choco.name() + " [Commande Contrat-Cadre i-1]", ac, 0.));	
		}
	}
	
	public void next() {
		// Le vendeur met à jour ses indicateurs de vente
		majIndicateursDeVente();
		// Le vendeur détermine les quantités à vendre et à commander
		majAchatsEtVentes();
		// Le vendeur met à jour les prix de vente de chaque chocolat de marque
		majPrixDeVente();
		// Le vendeur choisit les campagnes de pub à mener lors de l'étape courante
		majPublicites();
	}
	
	public void majIndicateursDeVente() {
		//cette fonction met à jour les données de vente de la step actuelle
		for (Chocolat choco : ac.nosChoco) {
			chocoVendu.get(choco).setValeur(ac, ventesEtapeActuelle.get(choco));
			ventesEtapeActuelle.put(choco,0.);
		}
	}
	
	public void majPublicites() {
		//cette fonction actualise la liste des chocolats (selon gamme) dont le distributeur fait la publicité
		// IA : publicités sur les chocolats équitables uniquement
		int etape = Filiere.LA_FILIERE.getEtape();
		if (etape % 22 == 0 || etape % 7 == 0 || (panik && compteurPub < 3)) {
			// On est à la période de Noël, Paques ou en panik en ayant fait peu de pubs ! Pub sur tous nos chocolats !
			for (ChocolatDeMarque choco : produitsCatalogue) {
				if (choco.getChocolat().isEquitable()) {
					publicites.add(choco);
				}
			}
		} else if (etape % 24 == 0) {
			//Remise à 0 du compteur
			compteurPub = 0;
		}
	}
	
	public void majAchatsEtVentes() {
		int etapeActuelle = Filiere.LA_FILIERE.getEtape();
		double quantiteAVendre;
		double quantiteACommander;
		double stockActuel;
		double stockQuiVaPerimer;
		double stockQuiNeVaPasPerimer;
		double stockLimite = ac.getStock().stockLimite;
		for (ChocolatDeMarque choco : produitsCatalogue) {
			stockActuel = ac.getStock().getStockChocolatDeMarque(choco);
			if (etapeActuelle > 12) {
				stockQuiVaPerimer = ac.getStock().chocoEnStockParEtape.get(etapeActuelle-13).get(choco);
			} else {
				stockQuiVaPerimer = 0;
			}
			stockQuiNeVaPasPerimer = stockActuel - stockQuiVaPerimer;
			if (panik){
				// on vend tout et on n'achète rien en bourse !
				quantiteAVendre = stockActuel;
				quantiteACommander = 0.; //A voir quand même
			} else if (stockActuel <= stockLimite) {
				if (kalm) {
					quantiteAVendre = stockQuiVaPerimer + stockQuiNeVaPasPerimer/3; //On diminue un peu les quantités mises en vente (par rapport au else ci-dessous) pour augmenter les stocks
					quantiteACommander = 3*stockLimite ; //Arbitraire encore et toujours
				} else {
					quantiteAVendre = stockQuiVaPerimer + stockQuiNeVaPasPerimer/4;
					quantiteACommander = 2*stockLimite; //Il faut refaire les stocks !
				}				
			} else {
				if (kalm) {
					quantiteAVendre = stockQuiVaPerimer + stockQuiNeVaPasPerimer/3; //On diminue un peu les quantités mises en vente (par rapport au else ci-dessous) pour augmenter les stocks
					quantiteACommander = stockQuiNeVaPasPerimer/4 ; //Arbitraire encore et toujours
				} else {
					quantiteAVendre = stockQuiVaPerimer + stockQuiNeVaPasPerimer/2;
					quantiteACommander = stockActuel/5; //On ne commande que le stock limite en fait
				}		
				
			}
			quantitesEnVente.get(choco).setValeur(ac, quantiteAVendre);
			quantitesACommander.get(choco).setValeur(ac, quantiteACommander);
		}
	}
	/**
	public void majAchatsEtVentesChoco() {
		double quantiteAVendre;
		double quantiteACommander;
		double stockActuel;
		double stockLimite = ac.getStock().stockLimite;
		double surplus;
		ArrayList<Chocolat> liste_nos_chocos = new ArrayList<Chocolat>();
		
		for (ChocolatDeMarque choco : produitsCatalogue) {
			Chocolat ce_choco = choco.getChocolat();
			if ( !liste_nos_chocos.contains(ce_choco)) {
				liste_nos_chocos.add(ce_choco);
			}
			
		}
		for (Chocolat choco : liste_nos_chocos) {
			stockActuel = ac.getStock().getStockChocolat(choco);
			if (panik){
				// on vend tout, et on n'achète rien en bourse !
				quantitesEnVenteChoco.get(choco).setValeur(ac, stockActuel);
				quantiteACommander = 0.;
			} else if (stockActuel <= stockLimite) {
				surplus = stockLimite*0.5;
//				quantitesEnVente.get(choco).setValeur(ac, surplus/2); // Si le stock est nul, on met en vente ce que l'on a pas
				quantitesEnVenteChoco.get(choco).setValeur(ac, stockActuel/4);
				quantiteACommander = 2*stockLimite-stockActuel + surplus; //Il faut refaire les stocks !
			} else {
				surplus = stockLimite*0.5;
				quantiteAVendre = stockActuel - stockLimite + surplus;
				quantitesEnVenteChoco.get(choco).setValeur(ac, quantiteAVendre);
				quantiteACommander = Double.max(surplus*2, 0.); //On ne commande que le stock limite 
				
					}
			quantitesACommanderChoco.get(choco).setValeur(ac, quantiteACommander);
		}
	}*/
	
	public void majPrixDeVente() {
		// met à jour le prix de vente de chaque chocolat du catalogue selon la valeur du cours actuel
		// IA : prix vente = (1 + pourcentageMarge/100) * cours
		double cours;
		double pourcentageMarge;
		double prix;
		for (ChocolatDeMarque choco : produitsCatalogue) {
			if (this.coutUnitaire.containsKey(choco)) {
				double prixLastStep = this.coutUnitaire.get(choco);
			}
			
			pourcentageMarge = 10.;
			if (Filiere.LA_FILIERE.getEtape() > 1) {
	            cours = Filiere.LA_FILIERE.getIndicateur("BourseChoco cours " + choco.getChocolat().name()).getHistorique().get(Filiere.LA_FILIERE.getEtape()-1).getValeur();
	        } else {
	        	cours = prixParDefaut.get(choco.getChocolat());
	        }
			prix = (1. + pourcentageMarge/100.) * cours;
			
			if (panik) {
				//On a besoin de plus d'argent !
				prix *= 6;  //Valeur arbitraire, et un peu empirique surtout
			} else if (kalm) {
				prix *= 0.8; // Soyons attractifs !
			}
			prixChoco.get(choco).setValeur(ac, prix);
		}
	}
	
	public double getQuantiteACommander(ChocolatDeMarque choco) {
		//donne la valeur des quantités à commander pour tel chocolat de marque
		return quantitesACommander.get(choco).getValeur();
	}
	
	public double getQuantiteACommander(Chocolat choco) {
		//donne la quantité à commander de chocolat d'une gamme
		double res = 0.;
		for (ChocolatDeMarque produit : produitsCatalogue) {
			if (produit.getChocolat() == choco) {
				res += quantitesACommander.get(produit).getValeur();
			}
		}
		return res;
	}
	
	public double quantiteEnVente(Chocolat choco) {
		//donne quantité en vente de chocolat selon une gamme
		double res = 0.;
		for (ChocolatDeMarque produit : produitsCatalogue) {
			if (produit.getChocolat() == choco) {
				res += quantitesEnVente.get(produit).getValeur();
			}
		}
		return res;
	}
	
	public double quantiteEnVente(ChocolatDeMarque choco) {
		//donne quantité en vente de chocolat selon la marque
		if (!ac.debutEtape) {
			ac.debutEtape = true;
			ac.getStock().initialiserStocksEtape();
			adapterQuantitesEnVente();  // Appelée une seule fois à l'initialisation ?
			dessinerCatalogue();
		}
		if (Filiere.LA_FILIERE.getEtape() == 0) {
			return quantiteAVendreParDefaut;
		} else {
			System.out.println(choco.name() + " " + quantitesEnVente.get(choco).getValeur());
			return quantitesEnVente.get(choco).getValeur();
		}
	}
	
	public void ajouterProduitAuCatalogue(ChocolatDeMarque choco) {
		//ajoute au catalogue un chocolat (selon sa marque) en indiquant son prix et sa quantité en vente
		if (!produitsCatalogue.contains(choco)) {
			produitsCatalogue.add(choco);
			prixChoco.put(choco, new Variable("Prix du " + choco.name(), ac, prixParDefaut.get(choco.getChocolat())));
			quantitesEnVente.put(choco, new Variable("Quantité de " + choco.name() + " en vente", ac, 0.));
			journalCatalogue.ajouter(Journal.texteColore(positiveColor, Color.BLACK, "[AJOUT] Le chocolat " + choco.name() + " a été ajouté au catalogue."));
		}
	}
	
	public void dessinerCatalogue() {
		journalCatalogue.ajouter(Journal.texteColore(metaColor, Color.BLACK, "[ETAPE " + Filiere.LA_FILIERE.getEtape() + "] Catalogue du distributeur"));
		journalCatalogue.ajouter(Journal.texteSurUneLargeurDe("Chocolat", 40) + Journal.texteSurUneLargeurDe("Quantité (tonnes)", 20) + Journal.texteSurUneLargeurDe("Prix", 20) + Journal.texteSurUneLargeurDe("", 30));
		for (ChocolatDeMarque choco : produitsCatalogue) {
			journalCatalogue.ajouter(Journal.texteSurUneLargeurDe(choco.name(), 40) + Journal.texteSurUneLargeurDe("" + Journal.doubleSur(quantitesEnVente.get(choco).getValeur(),2), 20) + Journal.texteSurUneLargeurDe("" + Journal.doubleSur(prixChoco.get(choco).getValeur(),2), 20) + Journal.texteSurUneLargeurDe("", 30));
		}
	}
	
	public List<ChocolatDeMarque> getCatalogue() {
		//renvoie la liste des chocolats de marque de notre catalogue
		return produitsCatalogue;
	}

	public double prix(ChocolatDeMarque choco) {
		//renvoie le prix d'un chocolat (selon sa marque) 
		return prixChoco.get(choco).getValeur();
	}

	public void adapterQuantitesEnVente() {
		//met à jour les quantité de chocolat de chaque marque en vente selon le stock qu'on a
		
		for (ChocolatDeMarque produit : produitsCatalogue) {
			double stockActuel = ac.getStock().getStockChocolatDeMarque(produit);
			double quantitesEnVentePrevue = quantitesEnVente.get(produit).getValeur();
			quantitesEnVente.get(produit).setValeur(ac, Double.min(stockActuel, quantitesEnVentePrevue));
			quantitesVendues = new ArrayList<Double>();
		}
		
	}
 
	public void vendre(ClientFinal client, ChocolatDeMarque choco, double quantite, double montant) {
		//vend le chocolat de telle marque à tel prix et telle quantité au client final
		if (client!=null) {
			this.coutUnitaire.put(choco, montant/quantite); 
			ac.getStock().retirerStockChocolat(choco, quantite);
			journal.ajouter(Journal.texteColore(positiveColor, Color.BLACK, "[VENTE] Vente de " + Journal.doubleSur(quantite,2) + " tonnes de " + choco.name() + " à " + client.getNom() + " pour " + Journal.doubleSur(montant,2) + " (" + Journal.doubleSur(montant/quantite,2) + "/tonne)."));
			ventesEtapeActuelle.put(choco.getChocolat(), ventesEtapeActuelle.get(choco.getChocolat()) + quantite);
			quantitesVendues.add(quantite);
		}
	}

	public void notificationRayonVide(ChocolatDeMarque choco) {
		//notifie quand le rayon est vide
		journal.ajouter(Journal.texteColore(warningColor, Color.BLACK, "[RAYON] Le rayon de " + choco.name() + " est vide."));
	}

	public List<ChocolatDeMarque> pubSouhaitee() {
		return publicites;
	}
	
	public double calculSoldeMini() {
		double coursActuel;
		double moyenneCours = 0;
		double soldeMini;
		
		// On calcule d'abord le cours moyen de la bourse pour nos chocolats
		for (ChocolatDeMarque choco : produitsCatalogue) {
			coursActuel = Filiere.LA_FILIERE.getIndicateur("BourseChoco cours " + choco.getChocolat().name()).getHistorique().get(Filiere.LA_FILIERE.getEtape()).getValeur();
			moyenneCours += coursActuel;
		}
		moyenneCours /= produitsCatalogue.size();
		soldeMini = coeffCoursMoyen*moyenneCours; //Valeur complètement arbitraire
		return soldeMini;
	}
}
