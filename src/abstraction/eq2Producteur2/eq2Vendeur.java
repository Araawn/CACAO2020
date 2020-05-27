package abstraction.eq2Producteur2;

import java.util.ArrayList;
import java.util.List;

import abstraction.eq8Romu.cacaoCriee.IVendeurCacaoCriee;
import abstraction.eq8Romu.cacaoCriee.LotCacaoCriee;
import abstraction.eq8Romu.cacaoCriee.PropositionCriee;
import abstraction.fourni.IActeur;
import abstraction.fourni.Journal;
import abstraction.fourni.Variable;
import abstraction.eq8Romu.produits.Feve;
import abstraction.eq8Romu.produits.Pate;
import abstraction.eq8Romu.produits.Gamme;

public class eq2Vendeur extends eq2Stock implements IVendeurCacaoCriee {
	/*Lucas Y
	 *Kristof S
	 */
	private Variable prixTF;
	private Variable prixTT;
	private Variable prixTTE;
	private Variable prixTC;
	private Variable prixTCE;
	private double prixvente;
	private Variable propalsnonvendues;
	private Journal journal_des_ventes;
	

	
	public eq2Vendeur() {
		super();
		//pour l'instant tous les prix sont initialises a 0
		this.prixTF = new Variable("prixTF",this,1900);
		this.prixTT = new Variable("prixTT",this,2500);
		this.prixTTE = new Variable("prixTTE",this,2800);
		this.prixTC = new Variable("prixTC",this,2900);
		this.prixTCE = new Variable("prixTCE",this,3200);
		this.prixvente = 0;
		this.propalsnonvendues = new Variable("propalsnonvendues",this,999999999);//première valeur super haute pour permettre ventes
		this.journal_des_ventes = new Journal("Journal des ventes", this);
	}
	/*faudrait rajouter un truc qui set les prix en fonction de ce qu'il s'est passé au cycle d'avant et de notre rentabilité
	 * 
	 */

	/*On vend dès qu'on a du stock
	 * 
	 */
	public LotCacaoCriee getLotEnVente() { //le -500 est une valeur arbitraire
		List<Variable> Stock = this.getVariablesFeve(); 
	    double masseFora = 0;
	    double masseTrini = 0;
	    double masseTriniE = 0;
	    double masseCrio = 0;
	    double masseCrioE = 0;
	    double prixfora = 0;
	    double prixtrini = 0;
	    double prixtrinie = 0;
	    double prixcrio = 0;
	    double prixcrioe = 0;
	    for (int i = 0; i < Stock.size();i++) { //changer ça vu que c'est plus une liste de couples mais un dictionnaire (en fait y'a peut-être pas besoin de le changer)
	    	if (Stock.get(i).getNom() == "forastero") {
	    		masseFora = masseFora + Stock.get(i).getValeur();
	    	}
	    	if (Stock.get(i).getNom() == "trinitario") {
	    		masseTrini = masseTrini + Stock.get(i).getValeur();
	    	}
	    	if (Stock.get(i).getNom() == "trinitario_equitable") {
	    		masseTriniE = masseTriniE + Stock.get(i).getValeur();
	    	}
	    	if (Stock.get(i).getNom() == "criollo") {
	    		masseCrio = masseCrio + Stock.get(i).getValeur();
	    	}
	    	if (Stock.get(i).getNom() == "criollo_equitable") {
	    		masseCrioE = masseCrioE + Stock.get(i).getValeur();
	    	}
	    }
	    prixfora = masseFora*this.getPrixTF().getValeur();
	    prixtrini = masseTrini*this.getPrixTT().getValeur();
	    prixtrinie = masseTriniE*this.getPrixTTE().getValeur();
	    prixcrio = masseCrio*this.getPrixTC().getValeur();
	    prixcrioe = masseCrioE*this.getPrixTCE().getValeur();
	    if (prixfora > prixtrini && prixfora > prixcrio && masseFora > 0.5 && prixfora > prixtrinie && prixfora > prixcrioe) {
	    	if (this.getPropal()>prixfora) {
	    		this.setPrixVente(prixfora);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de forastero de " + masseFora + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_BASSE, masseFora, this.getPrixTF().getValeur());
	    	}
	    	else {
	    		this.setPrixVente(this.getPropal()-500);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de forastero de " + (this.getPropal()-500)/this.getPrixTF().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_BASSE, (this.getPropal()-500)/this.getPrixTF().getValeur(), this.getPrixTF().getValeur());
	    	}
	    		
	    }
	    else if (prixtrini > prixfora && prixtrini > prixcrio && masseTrini > 0.5 && prixtrini > prixtrinie && prixtrini > prixcrioe) {
	    	if (this.getPropal()>prixtrini) {
	    		this.setPrixVente(prixtrini);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de trinitario de " + masseTrini + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_MOYENNE, masseTrini, this.getPrixTT().getValeur());
	    	}
	    	else {
	    		this.setPrixVente(this.getPropal()-500);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de trinitario de " + (this.getPropal()-500)/this.getPrixTT().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_MOYENNE, (this.getPropal()-500)/this.getPrixTT().getValeur(), this.getPrixTT().getValeur());
	    	}
	    		
	    }
	    else if (prixtrinie > prixfora && prixtrinie > prixcrio && masseTriniE > 0.5 && prixtrinie > prixtrini && prixtrinie > prixcrioe) {
	    	if (this.getPropal()>prixtrinie) {
	    		this.setPrixVente(prixtrinie);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de trinitario de équitable " + masseTriniE + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_MOYENNE_EQUITABLE, masseTriniE, this.getPrixTTE().getValeur());
	    	}
	    	else {
	    		this.setPrixVente(this.getPropal()-500);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de trinitario équitable de " + (this.getPropal()-500)/this.getPrixTTE().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_MOYENNE_EQUITABLE, (this.getPropal()-500)/this.getPrixTTE().getValeur(), this.getPrixTTE().getValeur());
	    	}
	    		
	    }
	    else if (prixcrio > prixtrini && prixfora < prixcrio && masseCrio > 0.5 && prixcrio > prixtrinie && prixcrio > prixcrioe) {
	    	if (this.getPropal()>prixcrio) {
	    		this.setPrixVente(prixcrio);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de criollo de " + masseCrio + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_HAUTE, masseCrio, this.getPrixTC().getValeur());
	    	}
	    	else {
	    		this.setPrixVente(this.getPropal()-500);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de criollo de " + (this.getPropal()-500)/this.getPrixTC().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_HAUTE, (this.getPropal()-500)/this.getPrixTC().getValeur(), this.getPrixTC().getValeur());
	    	}
	    		
	    }
	    else if (prixcrioe > prixtrini && prixfora < prixcrioe && masseCrioE > 0.5 && prixcrioe > prixtrinie && prixcrioe > prixcrio) {
	    	if (this.getPropal()>prixcrioe) {
	    		this.setPrixVente(prixcrioe);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de criollo équitable de " + masseCrioE + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_HAUTE_EQUITABLE, masseCrioE, this.getPrixTCE().getValeur());
	    	}
	    	else {
	    		this.setPrixVente(this.getPropal()-500);
	    		this.journal_des_ventes.ajouter("Proposition de vente d'un lot de criollo équitable de " + (this.getPropal()-500)/this.getPrixTCE().getValeur() + "tonnes, pour "+this.getPrixVente()+"$");
	    		return new LotCacaoCriee(this, Feve.FEVE_HAUTE_EQUITABLE, (this.getPropal()-500)/this.getPrixTCE().getValeur(), this.getPrixTCE().getValeur());
	    	}
	    		
	    }
	    else if (masseFora <0.5) {
	    	this.journal_des_ventes.ajouter("masse fora trop faible");
	    	return null;
	    }
	    else if (masseTrini <0.5) {
	    	this.journal_des_ventes.ajouter("masse trini trop faible");
	    	return null;
	    }
	    else if (masseCrio <0.5) {
	    	this.journal_des_ventes.ajouter("masse crio trop faible");
	    	return null;
	    }
	    else if (masseTriniE <0.5) {
	    	this.journal_des_ventes.ajouter("masse triniE trop faible");
	    	return null;
	    }
	    else if (masseCrioE<0.5) {
	    	this.journal_des_ventes.ajouter("masse crioE trop faible");
	    	return null;
	    }
	    else {
	    	return null;
	    }
	}
	

	@Override
	public void notifierAucuneProposition(LotCacaoCriee lot) { //500 encore arbitraire, surement trop élevée
		this.setPropal(this.getPrixVente()-500);
		this.journal_des_ventes.ajouter("Aucune proposition de lot n'a abouti à une vente");
		
	}

	@Override
	public PropositionCriee choisir(List<PropositionCriee> propositions) { // récupère l'offre d'achat la plus élevée et l'accepte si écart prix <= 5%
		int indice = 0;
		double max_actuel = 0;
		for (int i = 0; i < propositions.size(); i++) {
			if (max_actuel < propositions.get(i).getPrixPourLeLot()) {
				max_actuel = propositions.get(i).getPrixPourLeLot();
				indice = i;
			}
		}
		if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_BASSE) { //changer le abs pour qu'ils puissent nous l'acheter plus cher
			if ((Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTF().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("Nous acceptons la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		else if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_MOYENNE) {
			if ((Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTT().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("Nous acceptons la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		else if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_MOYENNE_EQUITABLE) {
			if ((Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTTE().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("Nous acceptons la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		else if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_HAUTE) {
			if ((Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTC().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("Nous acceptons la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		else if (propositions.get(indice).getLot().getFeve()==Feve.FEVE_HAUTE_EQUITABLE) {
			if ((Math.abs(propositions.get(indice).getPrixPourUneTonne() - this.getPrixTCE().getValeur()))/100 <= 0.05) {
				this.journal_des_ventes.ajouter("Nous acceptons la proposition d'achat de l'acteur "+propositions.get(indice).getAcheteur());
				return propositions.get(indice);
			}
		}
		return null;
	}

	@Override
	public void notifierVente(PropositionCriee proposition) { //enlève fèves vendues de notre stock
		removeQtFeve(proposition.getLot().getFeve(), proposition.getLot().getQuantiteEnTonnes());
		this.journal_des_ventes.ajouter("La vente a aboutit, le stock a été actualisé");
		
	}
	
	public double getPrixVente() {
		return this.prixvente;
	}
	
	public void setPrixVente(double prix) {
		this.prixvente = prix;
	}
	
	public double getPropal() {
		return this.propalsnonvendues.getValeur();
	}
	
	public void setPropal(double propal) {
		this.propalsnonvendues.setValeur(this, propal);
	}
	
	/**
	 * @return the prixTF
	 */
	public Variable getPrixTF() {
		return prixTF;
	}

	/**
	 * @param prixTF the prixTF to set
	 */
	public void setPrixTF(Variable prixTF) {
		this.prixTF = prixTF;
	}

	/**
	 * @return the prixTT
	 */
	public Variable getPrixTT() {
		return prixTT;
	}

	/**
	 * @param prixTT the prixTT to set
	 */
	public void setPrixTT(Variable prixTT) {
		this.prixTT = prixTT;
	}

	/**
	 * @return the prixTC
	 */
	public Variable getPrixTC() {
		return prixTC;
	}

	/**
	 * @param prixTC the prixTC to set
	 */
	public void setPrixTC(Variable prixTC) {
		this.prixTC = prixTC;
	}

	/**
	 * @return the prixTTE
	 */
	public Variable getPrixTTE() {
		return prixTTE;
	}

	/**
	 * @param prixTTE the prixTTE to set
	 */
	public void setPrixTTE(Variable prixTTE) {
		this.prixTTE = prixTTE;
	}

	/**
	 * @return the prixTCE
	 */
	public Variable getPrixTCE() {
		return prixTCE;
	}

	/**
	 * @param prixTCE the prixTCE to set
	 */
	public void setPrixTCE(Variable prixTCE) {
		this.prixTCE = prixTCE;
	}
	
	public List<Journal> getJournaux() {
		List<Journal> res=new ArrayList<Journal>();
		res.addAll(super.getJournaux());
		res.add(this.journal_des_ventes);
		return res;
	}

}
	


