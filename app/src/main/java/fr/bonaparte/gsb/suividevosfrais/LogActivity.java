package fr.bonaparte.gsb.suividevosfrais;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LogActivity extends AppCompatActivity {

    private static AccesDistant accesDistant;
    private static JSONArray loginJSON;
    private static JSONArray donneesFraisForfaitJSON;
    private static JSONArray donneesFraisHFJSON;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
        setTitle("GSB : Enregistrement des données");
        // chargement des méthodes événementielles
		cmdSync_clic() ;
	}

    /**
     * Sur le clic du bouton valider : sérialisation
     */
    private void cmdSync_clic() {
        findViewById(R.id.cmdLoginValider).setOnClickListener(new Button.OnClickListener() {
    		public void onClick(View v) {
                accesDistant = new AccesDistant();
                donnees_JSON();
                accesDistant.envoi(
                        "login", loginJSON,
                        "fraisHF", donneesFraisHFJSON,
                        "fraisForfait", donneesFraisForfaitJSON);
    			retourActivityPrincipale() ;
    		}
    	}) ;
    }

    /**
     * Encodage des données au format JSON
     */
    private void donnees_JSON(){
        // Variables
        //// pour connection
        List listDonneesLogin = new ArrayList();
        //// pour frais au forfait
        List listDonneesForfait = new ArrayList();
        ////// hors forfait
        ArrayList<FraisHf> lesFraisHF = new ArrayList<>();
        List listDonneesHF = new ArrayList();
        Object key;
        // enregistrement des informations
        //// de connection
        EditText utilisateur = (EditText) findViewById(R.id.txtUtilisateur);
        EditText mdp = (EditText) findViewById(R.id.txtMdp);
        listDonneesLogin.add(utilisateur.getText().toString());
        listDonneesLogin.add(mdp.getText().toString());
        loginJSON = new JSONArray(listDonneesLogin);

        for (Iterator itr=Global.listFraisMois.keySet().iterator(); itr.hasNext();){
            key = itr.next();
            lesFraisHF.clear();
            lesFraisHF.addAll(Global.listFraisMois.get(key).getLesFraisHf());
            listDonneesHF.add(key);
            listDonneesForfait.add(key);
            for (int i = 0; i<lesFraisHF.size(); i++) {
                //// de frais au forfait
                listDonneesForfait.add(Global.listFraisMois.get(key).getEtape());
                listDonneesForfait.add(Global.listFraisMois.get(key).getKm());
                listDonneesForfait.add(Global.listFraisMois.get(key).getNuitee());
                listDonneesForfait.add(Global.listFraisMois.get(key).getRepas());
                //// de frais hors forfait
                listDonneesHF.add(lesFraisHF.get(i).getJour().toString());
                listDonneesHF.add(lesFraisHF.get(i).getMontant().toString());
                listDonneesHF.add(lesFraisHF.get(i).getMotif().toString());
            }
        }
        donneesFraisForfaitJSON = new JSONArray(listDonneesForfait);
//        for (Iterator itr=Global.listFraisMois.keySet().iterator(); itr.hasNext();){
//            key = itr.next();
//            lesFrais.clear();
//            lesFrais.addAll(Global.listFraisMois.get(key).getLesFraisHf());
//            listDonneesHF.add(key);
//            for (int i = 0; i<lesFrais.size(); i++) {
//                listDonneesHF.add(lesFrais.get(i).getJour().toString());
//                listDonneesHF.add(lesFrais.get(i).getMontant().toString());
//                listDonneesHF.add(lesFrais.get(i).getMotif().toString());
//            }
//        }
        donneesFraisHFJSON = new JSONArray(listDonneesHF);
    }

	/**
	 * Retour à l'activité principale (le menu)
	 */
	private void retourActivityPrincipale() {
		Intent intent = new Intent(LogActivity.this, MainActivity.class) ;
		startActivity(intent) ;   					
	}
}
