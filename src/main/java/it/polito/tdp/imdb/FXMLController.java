/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	int anno= this.boxAnno.getValue();
    	this.model.creaGrafo(anno);
    	this.txtResult.appendText("GRAFO CREATO! \n");
    	this.txtResult.appendText("#VERTICI: "+model.getVertici()+"\n");
    	this.txtResult.appendText("#ARCHI: "+model.getArchi()+"\n");
    	List <Director> box= new ArrayList <Director>(model.getGrafo().vertexSet());
    	Collections.sort(box);
    	this.boxRegista.getItems().clear();
    	this.boxRegista.getItems().addAll(box);
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	this.txtResult.clear();
    	int anno= this.boxAnno.getValue();
    	Director selezionato= this.boxRegista.getValue();
    	List <String> result= new LinkedList <>(this.model.vicini(selezionato, this.model.getIdMap(), anno));
    	for(String s: result) {
    		this.txtResult.appendText(s+"\n");
    	}

    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	this.txtResult.clear();
    	int anno= this.boxAnno.getValue();
    	Director selezionato= this.boxRegista.getValue();
    	String max= this.txtAttoriCondivisi.getText();
    	int massimo;
    	try {
    		massimo=Integer.parseInt(max);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("INSERISCI UN NUMERO VALIDO");
    		return;
    	}
    	List<Director> result= new ArrayList <>(this.model.trovaPercorso(selezionato, massimo, anno));
    	for(Director d: result) {
    		this.txtResult.appendText(d.toString()+"\n");
    	}
    	this.txtResult.appendText("TOT CONDIVISI: "+this.model.calcolaCondivisi(result, anno,selezionato));
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	
    	this.model = model;
    	this.boxAnno.getItems().add(2004);
    	this.boxAnno.getItems().add(2005);
    	this.boxAnno.getItems().add(2006);
    	
    	
    }
    
}
