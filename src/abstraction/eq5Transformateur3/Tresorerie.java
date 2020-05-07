package abstraction.eq5Transformateur3;

import abstraction.fourni.Banque;
import abstraction.fourni.Filiere;
import abstraction.fourni.Variable;

/** @author Simon MORO  */
public class Tresorerie {
    /**
     * Gère l'argent de la société pour la répartir entre les filiaires et voir si toutes les décisions sont cohérentes en terme bugetaires.
     * Elle est agrégé dans la classe Transformateur3.
     * 
     * Infos utiles à récupérer : montant compte en banque ; stock ; historique des ventes / achats ; agios / découverts 
     * ; montant max d'investissement de chaque filière ;
     * 
     *structure d'une Variable = (String nom, IActeur createur,  double min, double max, double valInit)
     */
	private Transformateur3 Acteur;
	private Banque banque; //Filiere.LA_FILIERE.getBanque()
	private double MontantCompte;
	private double Decouvert;
	private double FacteurPrioriteGamme; // 100% haute gamme = 1, 100% bas de gamme = 0

	
	private Variable decouvertsConsecutifsAvantFaillite; //parametres fixes à priori
	private Variable decouvertAutorise;
	private Variable agiosDecouvertAutorise;
	private Variable agiosDecouvertAuDela;
	private Variable seuilOperationsRefusees;
	
	public Tresorerie(Transformateur3 acteur, Banque banque, double MontantCompte, double Decouvert, double Facteur, Variable decouvertsConsecutifsAvantFaillite,
			Variable decouvertAutorise, Variable agiosDecouvertAutorise, Variable agiosDecouvertAuDela, Variable seuilOperationsRefusees) {
		this.Acteur = acteur;
		this.banque = banque;
		this.MontantCompte=MontantCompte;
		this.Decouvert=Decouvert;
		this.FacteurPrioriteGamme=Facteur;
		
		this.decouvertsConsecutifsAvantFaillite = decouvertsConsecutifsAvantFaillite;
		this.decouvertAutorise = decouvertAutorise;
		this.agiosDecouvertAutorise = agiosDecouvertAutorise;
		this.agiosDecouvertAuDela = agiosDecouvertAuDela;
		this.seuilOperationsRefusees = seuilOperationsRefusees;
	}
	
	public Tresorerie(Transformateur3 acteur) {
		/**
	     * Initialise la trésorerie
	     */
		this(acteur,
				Filiere.LA_FILIERE.getBanque(),
				0,									//montantCompte
				0,									//decouvert actuel
				0,									//FacteurPriorite
				Filiere.LA_FILIERE.getBanque().getParametres().get(0),
				Filiere.LA_FILIERE.getBanque().getParametres().get(1),
				Filiere.LA_FILIERE.getBanque().getParametres().get(2),
				Filiere.LA_FILIERE.getBanque().getParametres().get(3),
				Filiere.LA_FILIERE.getBanque().getParametres().get(4));
	}
	
	public double getMontantCompte() {
		return this.MontantCompte;
	}
	
	public double getDecouvert() {
		return this.Decouvert;
	}
	
	public double getFacteurPrioriteGamme() {
		/**
	     * Le facteur qui décrit la priorité que nous mettons sur le bas de gamme par rapport au haut de gamme (évolue au cours de la
	     * simulation)
	     */
		return this.FacteurPrioriteGamme;
	}
	
	public void setMontantCompte (double MontantCompte) {
		this.MontantCompte=MontantCompte;
	}
	
	public void setDecouvert (double Decouvert) {
		this.Decouvert=Decouvert;
	}
	
	public void setFacteurPrioriteGamme (double Facteur) {
		/**
	     * Le facteur qui décrit la priorité que nous mettons sur le bas de gamme par rapport au haut de gamme (évolue au cours de la
	     * simulation)
	     */
		this.FacteurPrioriteGamme=Facteur;
	}
	
	public double InvestissementMaxBasDeGamme() {
		/**
	     * Renvoie l'investissement maximum possible à faire dans le bas de gamme en fonction du facteur de priorité que l'on s'impose
	     * ainsi que du montant de notre trésorerie
	     */
		return 0;
	}
	
	public double InvestissementMaxHautDeGamme() {
		/**
	     * Renvoie l'investissement maximum possible à faire dans le haut de gamme en fonction du facteur de priorité que l'on s'impose
	     * ainsi que du montant de notre trésorerie
	     */
		return 0;
	}
	
	public void DiminueTresorerie(double montant) {
		
	}
	
	public void AugmenteTresorerie(double montant) {
		
	}
}