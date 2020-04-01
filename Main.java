package application;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.image.BufferedImage;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

import java.io.File;

import java.io.FileReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import javax.imageio.ImageIO;
public class Main extends Application  {

	Button bouton1;
	Button bouton2;
	Button bouton3;
	Stage window;
	Scene scene;
	ComboBox<String> comboBox1;
	ComboBox<String> comboBox2;
	ComboBox<String> comboBox3;
	ListView<Integer> listview;
	public static String afficheChemin(String d¨¦part, String arriv¨¦e,ArrayList<Integer> LignesInterdites, ArrayList<String> StationsInterdites) throws IOException {

		String texte1;
		String texte2;



		ArrayList<Arr¨ºte> arr¨ºtes = new  ArrayList<Arr¨ºte>();
		List<String> arr¨ºtesString = null;

		BufferedReader bf=new BufferedReader(new FileReader("./src/application/coordonnes1.txt"));
		String textLine;
		String str2 ="";
		while((textLine=bf.readLine())!=null){
			str2+=textLine;
		}
		String[] numbers=str2.split("	");
		float []cord=new float[numbers.length]; 
		for (int i = 0; i < numbers.length; i++) {
			cord[i]=Float.parseFloat(numbers[i]);
		}
		bf.close();
	
		
		
		arr¨ºtesString = Files.readAllLines(Paths.get("./src/application/Arr¨ºtes.txt"));

		for (String s: arr¨ºtesString) {
			String str[] = s.split(" ");
			ArrayList<Integer> b = new ArrayList<Integer>();
			for (String t: str) {
				int n = Integer.parseInt(t);
				b.add(n);
			}
			arr¨ºtes.add(new Arr¨ºte(b.get(0), b.get(1), b.get(2)+25));
		}
        
        BufferedReader br;
        String motLu;
        ArrayList<Integer> lignesbis=new ArrayList<Integer>();
        List<String> nombis = new ArrayList<String>();
        try{
            br = new BufferedReader(new FileReader(new File("./src/application/Stations1.txt")));
            while((motLu = br.readLine()) != null)
            {
            if (motLu.charAt(motLu.length()-2) ==';'){
            int monEntier = Integer.parseInt(motLu.charAt(motLu.length()-1)+"");
            lignesbis.add(monEntier);
            nombis.add(motLu.substring(0,motLu.length()-2 ));
            
            	
            }
            else {
            	int monEntier=Integer.parseInt(motLu.charAt(motLu.length()-1)+"") + (Integer.parseInt(motLu.charAt(motLu.length()-2)+"")*10) ;
                lignesbis.add(monEntier);
                nombis.add(motLu.substring(0,motLu.length()-3 ));
            }
            
          
            }
            br.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        List<String>nom= nombis;
 	   
        int [] lignes= new int [lignesbis.size()];
        for (int i=0;i<lignesbis.size();i++) {
        	lignes[i]=lignesbis.get(i);
        }
    
	    //on cr¨¦e le plan
        BufferedImage image = ImageIO.read(new File("./src/application/plan.png"));
		Graphics2D graphics = image.createGraphics();
		
		graphics.setColor(Color.blue);
		graphics.setStroke(new BasicStroke(10.0f));
		
		
		//on cr¨¦e le graphe:
		Graphe g = new Graphe(arr¨ºtes);
		
		g.donnenom(nom);
		g.donneligne(lignes);
		g.donnecord(cord);
	
		
		
		//on prend en compte les contraintes
		g.retirerligne2(LignesInterdites);
		g.retirerstation2(StationsInterdites);
		

		//calcul du plus court chemin
		int w=0;

		int z=0;
		int k=0;
		int u=100000000;

		while (w<g.estmultiligne(d¨¦part)) {

			int y=0;
			while (y<g.estmultiligne(arriv¨¦e)) {
				Graphe e = new Graphe(arr¨ºtes);

				e.donnenom(nom);
				e.donneligne(lignes);
				e.retirerligne2(LignesInterdites);
				e.retirerstation2(StationsInterdites);
				e.calculateShortestDistances(d¨¦part,arriv¨¦e,w,y);
				int m=e.sommets[e.StringtoInt(arriv¨¦e) + y].getDistance¨¤laSource();

				if (m<u) {
					u=m;
					k=w;
					z=y;
				}
				y=y+1;
			}
			w=w+1;
		}

		g.calculateShortestDistances(d¨¦part,arriv¨¦e,k,z);

		g.calculatePath();



		ArrayList<Sommet> path = g.getPath();
		ArrayList<String> h =g.transformation();
		Collections.reverse(h);
		Collections.reverse(path);
		
		//On dessine le chemin
		
		
		graphics.draw(new Line2D.Double(g.sommets[g.StringtoInt(d¨¦part)].x,g.sommets[g.StringtoInt(d¨¦part)].y , path.get(0).x,path.get(0).y));
		for (int i = 0; i < path.size()-1; i++) {
			graphics.draw(new Line2D.Double(path.get(i).x,path.get(i).y,path.get(i+1).x,path.get(i+1).y));
		}
		graphics.draw(new Line2D.Double(path.get(path.size()-1).x,path.get(path.size()-1).y,g.sommets[g.StringtoInt(arriv¨¦e)].x ,g.sommets[g.StringtoInt(arriv¨¦e)].y));
		graphics.dispose();
		ImageIO.write(image, "png", new File("./src/application/abc.png"));
		new App().setVisible(true);
		
		if (h.contains(arriv¨¦e)) {
			System.out.println(g.sommets[g.StringtoInt(d¨¦part)].ligne);
			h.set(0, h.get(0)+"(" + g.path.get(0+k).ligne +")");
			for (int m=1; m<h.size()-1;m++) {
				if (h.get(m).equals(h.get(m+1))){
					h.set(m+1, h.get(m+1)+"(" + g.path.get(m+1).ligne +")");
				}
			}
			texte1="Le plus court chemin est: " + h;

			texte2=g.printResult(d¨¦part, arriv¨¦e,z);

		}
		else {
			h.add(arriv¨¦e);

			h.set(0, h.get(0)+"(" + g.sommets[g.StringtoInt(d¨¦part)+k].ligne +")");
			for (int m=1; m<h.size()-1;m++) {
				if (h.get(m).equals(h.get(m+1))){
					h.set(m+1, h.get(m+1)+"(" + g.path.get(m+1).ligne +")");
				}
			}
			texte1=("Le plus court chemin est " + h);

			texte2=g.printResult(d¨¦part, arriv¨¦e,z);


		}

		return texte1 + " : " + texte2;

	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		window = primaryStage;
		window.setTitle("Menu itin¨¦raire m¨¦tro");
        BufferedReader br;
        String pr¨¦c="";
        String motLu;
        ArrayList<Integer> lignesbis=new ArrayList<Integer>();
        List<String> nombis = new ArrayList<String>();
        try{
            br = new BufferedReader(new FileReader(new File("./src/application/Stations1.txt")));
            while((motLu = br.readLine()) != null)
            {
            
            if (motLu.charAt(motLu.length()-2) ==';'){
            if (pr¨¦c.equals(motLu.substring(0,motLu.length()-2 ))){
            	
            }
            else {
            int monEntier = Integer.parseInt(motLu.charAt(motLu.length()-1)+"");
            lignesbis.add(monEntier);
            nombis.add(motLu.substring(0,motLu.length()-2 ));
            pr¨¦c=motLu.substring(0,motLu.length()-2 );
            }	
            }
            else {
            	if (pr¨¦c.equals(motLu.substring(0,motLu.length()-3 ))){
	            	
	            }
            	else {
            	int monEntier=Integer.parseInt(motLu.charAt(motLu.length()-1)+"") + (Integer.parseInt(motLu.charAt(motLu.length()-2)+"")*10) ;
                lignesbis.add(monEntier);
                nombis.add(motLu.substring(0,motLu.length()-3 ));
                pr¨¦c=motLu.substring(0,motLu.length()-3 );
            	}
            }
            
          
            }
            br.close();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }


		comboBox1 = new ComboBox<>();
		comboBox1.getItems().addAll(nombis);


		comboBox1.setPromptText("Station de d¨¦part");
		comboBox1.setEditable(true);



		comboBox2 = new ComboBox<>();
		comboBox2.getItems().addAll(nombis);


		comboBox2.setPromptText("Station d'arriv¨¦e");
		comboBox2.setEditable(true);



		comboBox3 = new ComboBox<>();
		comboBox3.getItems().addAll(nombis);


		comboBox3.setPromptText("Stations ¨¤ interdire");
		comboBox3.setEditable(true);





		listview = new ListView<>();
		listview.getItems().addAll(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16);
		listview.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);



		ArrayList<String> stationsinterdites = new ArrayList<String>();


		//afficheChemin(comboBox1.getValue(), comboBox2.getValue(), new int[] {9,8}, new String[] {})




		bouton2 = new Button();
		bouton2.setText("Ajouter");
		bouton2.setOnAction(e ->  stationsinterdites.add(comboBox3.getValue()) );





		bouton3 = new Button();
		bouton3.setText("Supprimer");
		bouton3.setOnAction(e ->  stationsinterdites.remove(comboBox3.getValue()) );




		bouton1 = new Button();
		bouton1.setText("Calcul itin¨¦raire");

		bouton1.setOnAction(e ->  {
			try {
				System.out.println(afficheChemin(comboBox1.getValue(), comboBox2.getValue(), tabint(),stationsinterdites ));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});




		VBox layout = new VBox(10);
		layout.setPadding(new Insets(20, 20, 20, 20));
		layout.getChildren().addAll(comboBox1,comboBox2,listview,comboBox3,bouton2, bouton3, bouton1);

		scene = new Scene(layout, 600, 500);
		window.setScene(scene);
		window.show();
	}

	ArrayList<Integer> tabint() {

		ObservableList<Integer> l;
		l = listview.getSelectionModel().getSelectedItems();
		ArrayList<Integer> tab = new ArrayList<Integer>(l); // ObservableList<String> --> Arraylist<String>

		return tab;

	}




	public static void main(String[] args) {
		launch(args);







	}


}