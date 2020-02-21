package Miinaharava;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Tallennus {
	File file;
	
	/**
	 * Luo uuden tiedoston parametrina saaneen polun avulla, mikäli tiedostoa ei ole jo olemassa.
	 * @param tallennuspaikka, tiedoston polku Stringina
	 */
	public Tallennus(String tallennuspaikka) {
		this.file = new File(tallennuspaikka);
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Paluttaa tiedoston
	 * @return palautettava tiedosto
	 */
	public File annaFile() {
		return file;
	}
	
	/**
	 * Lukee parametrina saaneen tiedoston ja palauuttaa tiedoston sisällön Stringina
	 * @param file, luettava tiedosto
	 * @return tiedoston sisältö Stringina
	 */
	public String lueTiedosto(File file) {
		StringBuilder kokoTiedosto = new StringBuilder();
		Scanner s;
		int luku = 1;
		
		try {
			s = new Scanner(new FileReader(file.getPath()));
			while(s.hasNextLine()) {
				kokoTiedosto.append(Integer.toString(luku) + ": ");
				kokoTiedosto.append(s.nextLine());
				kokoTiedosto.append(System.lineSeparator());
				luku++;
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String tiedosto = kokoTiedosto.toString();
		return tiedosto;
	}
	
	/**
	 * Lisää parametrina saaneen ajan ja päivän parametrina saaneeseen tiedostoon
	 * @param f, tiedosto, johon aika ja päivä tallennetaan
	 * @param aika, aika Stringina
	 * @param paiva, paiva Stingina
	 */
	public void kirjoitaAika(File f, String aika, String paiva){
		ArrayList<String> ajat = new ArrayList<>();
		Scanner s;
		try {
			s = new Scanner(new FileReader(f.getPath()));
			
			while(s.hasNextLine()) {
				ajat.add(s.nextLine() + System.lineSeparator());
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		ajat.add(aika + " " + paiva + System.lineSeparator());
		String sisalto = "";
		
		if(ajat.size() > 5) {
			for(int i=0; i<5; i++) {
				sisalto = sisalto + ajat.get(i);
			}
		}
		else {
			for(int i=0; i<ajat.size(); i++) {
				sisalto = sisalto + ajat.get(i);
			}
		}

		FileWriter fw;
		try {
			fw = new FileWriter(f);
			fw.write(sisalto);
			fw.flush();		
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
