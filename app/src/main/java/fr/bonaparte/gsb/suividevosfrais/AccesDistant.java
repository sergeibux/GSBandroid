package fr.bonaparte.gsb.suividevosfrais;

import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class AccesDistant implements AsyncResponse {

    // constante
    private static final String SERVERADDR = "http://semeva.fr/GSB/includes/android/synchronise.php";

    /**
     * Constructeur
     */
    public AccesDistant(){
        super();
    }

    /**
     * Retour du serveur HTTP
     * @param output
     */
    @Override
    public void processFinish(String output) {
        // pour vérification, affiche le contenu du retour dans la console
        Log.d("serveur", "************" + output);
        // découpage du message reçu
        String[] message = output.split("%");
        // contrôle si le retour est correct (au moins 2 cases)
        if(message.length>1){
            if(message[0].equals("OK")){
                Log.d("OK","**************** "+message[1] + " *** " + message[2]);
                try {
                    JSONArray lesInfos = new JSONArray(message[2]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                if(message[0].equals("ERREUR")) {
                    Log.d("Erreur ! ", "****************" + message[1]);
                }
            }
        }
    }

    /**
     * Envoi de données vers le serveur distant
     * @param operation les données transmises sont de ce type
     * @param lesDonneesJSON les données à envoyer en base de données
     */
    public void envoi(String operation, JSONArray lesDonneesJSON, String operation2, JSONArray lesDonneesJSON2, String operation3, JSONArray lesDonneesJSON3){
        AccesHTTP accesDonnees = new AccesHTTP();
        // lien avec AccesHTTP pour permettre à delegate d'appeler la méthode processFinish
        // au retour du serveur
        accesDonnees.delegate = this;
        // ajout de paramètres dans l'enveloppe HTTP
        accesDonnees.addParam(operation, lesDonneesJSON.toString());
        accesDonnees.addParam(operation2, lesDonneesJSON2.toString());
        accesDonnees.addParam(operation3, lesDonneesJSON3.toString());
        // envoi en post des paramètres, à l'adresse SERVERADDR
        accesDonnees.execute(SERVERADDR);
    }

}
