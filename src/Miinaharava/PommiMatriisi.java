package Miinaharava;

import java.util.ArrayList;
import java.util.Random;

public class PommiMatriisi {
	public String[][] matriisi;
	public int pommiMaara;
	private Random random;
	public int koko;
	public ArrayList<Integer> arvotutLuvut1;
	public ArrayList<Integer> arvotutLuvut2;

	
	/**
	 * Luo matriisin, johon tallennetaan m‰‰r‰n verran pommeja ja merkataan muut kohdat numeroilla
	 * Numerot kertovat ymp‰rill‰ olevien pommien m‰‰r‰n
	 * @param maara, pommien m‰‰r‰
	 * @param koko, matriisin koko leveys tai pituus suunnassa (koko on aina neliˆ)
	 */
	public PommiMatriisi(int maara, int koko) {
		random = new Random();
		
		this.pommiMaara = maara;
		matriisi = new String[koko][koko];
		
		arvotutLuvut1 = new ArrayList<>();
		arvotutLuvut2 = new ArrayList<>();

		//arvotaan random paikat pommeille
		while(pommiMaara > 0) {
			int apu = 0;
			
			int luku1 = random.nextInt(koko);
			int luku2 = random.nextInt(koko);
				
			for(int i=0; i<arvotutLuvut1.size(); i++) {
				if(arvotutLuvut1.get(i).equals(luku1)) {
					if(arvotutLuvut2.get(i).equals(luku2)) {
						//luku on jo olemassa
						apu = 1;
					}
				}
			}
			
			if(apu == 0) {
				matriisi[luku1][luku2] = "*";
				pommiMaara -= 1;
				arvotutLuvut1.add(luku1);
				arvotutLuvut2.add(luku2);
			}
		}
		
		//k‰yd‰‰n l‰pi lˆytyykˆ vierest‰ pommeja
		for(int i=0; i<matriisi.length; i++) {
			for(int j=0; j<matriisi.length; j++) {
				int summa = 0;
				
				if(matriisi[i][j] != "*") {
					//k‰yd‰‰n kaikki vaihtoehdot l‰pi
					//molemmat v‰lill‰ 1-14, eli keskell‰ matriisia
					if((i>0 && i<koko-1) && j>0 && j<koko-1) {
						if(matriisi[i-1][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j-1] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
					
					//yl‰ reuna, ei kulmat
					else if(i==0 && (j>0 && j<koko-1)) {
						if(matriisi[i+1][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i][j+1] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
					
					//vasen reuna, ei kulmat
					else if(j==0 && (i>0 && i<koko-1)) {
						if(matriisi[i+1][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
					
					//oikee reuna, ei kulmat
					else if(j==(koko-1) && (i>0 && i<koko-1)) {
						if(matriisi[i-1][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
					
					//ala reuna, ei kulmat
					else if(i==(koko-1) && (j>0 && j<koko-1)) {
						if(matriisi[i-1][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i][j-1] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
					
					//vasen yl‰kulma
					else if(i==0 && j==0) {
						if(matriisi[i+1][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i][j+1] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
					
					//oikea yl‰kulma
					else if(i==(koko-1) && j==0) {
						if(matriisi[i-1][j+1] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i][j+1] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
					
					//vasen alakulma
					else if(i==0 && j==(koko-1)) {
						if(matriisi[i+1][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i+1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i][j-1] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
					
					//oikea alakulmat
					else if(i==(koko-1) && j==(koko-1)) {
						if(matriisi[i-1][j-1] == "*") {
							summa += 1;
						}
						if(matriisi[i-1][j] == "*") {
							summa += 1;
						}
						if(matriisi[i][j-1] == "*") {
							summa += 1;
						}
						matriisi[i][j] = Integer.toString(summa);
					}
				}
			}
		}
		
		for(int i=0; i<koko; i++) {
			for(int j=0; j<koko; j++) {
				if(matriisi[i][j].equals("0")) {
					matriisi[i][j] = "";
				}
			}
		}
		
		/*
		//testataan ett‰ toimii
		for(int i=0; i<koko; i++) {
			System.out.println();
			for(int j=0; j<koko; j++) {
				System.out.print(matriisi[i][j] + " ");
			}
		}
		
		int a = 0;
		for(int i=0; i<koko; i++) {
			System.out.println();
			for(int j=0; j<koko; j++) {
				if(matriisi[i][j] == "*") {
					a ++;
				}
			}
		}
		*/
	}
	
	/**
	 * Saa parematreina kohdan koordinaatit ja palauttaa Stringin halutulta kohdalta
	 * @param i, koordinaatti x
	 * @param j, koordinaatti y
	 * @return, String koordinaattien kohdalta matriisista
	 */
	public String annaMatriisi(int i, int j) {
		return matriisi[i][j];
	}

	/**
	 * Palauttaa matriisin sis‰lt‰mien pommien m‰‰r‰n
	 * @return pommien m‰‰r‰
	 */
	public int annaPommiMaara() {
		return pommiMaara;
	}
}
