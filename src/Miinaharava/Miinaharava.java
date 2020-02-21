package Miinaharava;

/**
 * Kommentoi ja selkeyt�!
 *
 * kaikkien nollien poisto, jos osuu nollaan ja mahdollinen korvaus tyhj�ll� ruudulla
 * hienompi ulkoasu
 * Top-listan j�rjest�mien ja kentt�kohtaisuus
 * Pelin loppuminen
 *
 * mustat pohjat pommeille, v�rit eri numeroille
 *
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

public class Miinaharava implements MouseListener, ActionListener{
	public int koko;
	public int pommienMaara;
	
	public JButton pelaaNappi;
	public JFrame ikkuna;
	public JMenuBar menu;
	public JMenu valikko;
	public JMenuItem pieni;
	public JMenuItem keskikoko;
	public JMenuItem suuri;
	
	public PommiMatriisi pommit;
	public JButton[][] nappulat;
	public JPanel nappulaIkkuna;
	public JLabel[][] pohja;
	public JPanel pohjaIkkuna;
	
	public JLabel[][] ylanappulat; 
	public JPanel ylarivi;
	public boolean peliLoppu;
	public Tallennus file;
	
	public Timer aika;
	public int sekuntit;
	public Calendar kalenteri;
	public DateFormat paivaMuoto;
	
	Image kuva;
	ImageIcon hymio;
	
	public int tyhjienPaikkojenLkm;
	public int pommejaJaljella;
	public final Border rajat = BorderFactory.createLineBorder(Color.darkGray);
	public final Font fontti = new Font("Calibri", Font.PLAIN, 20);
	
	public static void main(String[] args) {
		Miinaharava peli = new Miinaharava();
	}
	
	public Miinaharava() {
		//luodaan uusi File tallennusta varten
		file = new Tallennus("Miinaharava.txt");
		
		//Luodaan uusi ikkuna
		ikkuna = new JFrame("Miinaharava");
		ikkuna.setFont(fontti);
		ikkuna.setSize(600,600);
		ikkuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//luodaan aluksi aina keskikokoinen kentt�
		luoMenu();
		luoYlaNappulat();
		luoKentta(40, 16, 22);
	}
	
	/**
	 * Luo kentt�n yl�osaan kolme nappulaa
	 * Vasemmanpuoleinen nappula kertoo j�ljell� olevien pommien m��r�n
	 * Seuraavasta napista voidaan aloittaa peli uudestaan (reset)
	 * Viimeisess� nappulassa n�kyy aika, joka l�htee k�yntii aina uuden pelikierroksen alkaessa, aluksi 0:0
	 */
	public void luoYlaNappulat() {
		kuva = Toolkit.getDefaultToolkit().getImage("hymio.jpg");
		hymio = new ImageIcon(kuva);

		//Tehd��n yl�riviin napit
		ylanappulat = new JLabel[1][3];
		JPanel ylarivi = new JPanel();
		ylarivi.setLayout(new GridLayout(1,3));
		
		//annetaan jokaiselle nimi yms.
		ylanappulat[0][0] = new JLabel(Integer.toString(pommejaJaljella), SwingConstants.CENTER);
		
		ylanappulat[0][1] = new JLabel(hymio);
		//ylanappulat[0][1] = new JLabel("PELAA", SwingConstants.CENTER);
		ylanappulat[0][1].addMouseListener(this);

		ylanappulat[0][2] = new JLabel("00:00", SwingConstants.CENTER);

		for(int i=0; i<3; i++) {
			ylanappulat[0][i].setFont(fontti);
			ylanappulat[0][i].setBorder(rajat);
			ylarivi.add(ylanappulat[0][i]);
		}
		
		ikkuna.add(ylarivi, BorderLayout.NORTH);
	}
	
	
	/**
	 * Luodaan valikko kent�n vasempaan yl�kulmaan
	 * Lis�t��n valikkoon kolme nappulaa: pieni, keskikoko ja suuri
	 * Nappuloilla luodaan uusi kentt�
	 */
	public void luoMenu() {
		//luodaan menu
		menu = new JMenuBar();
		valikko = new JMenu("valikko");
		valikko.setFont(fontti);
		menu.add(valikko);
		
		pieni = new JMenuItem("pieni");
		pieni.setFont(fontti);
		pieni.addActionListener(this);

		keskikoko = new JMenuItem("keskikoko");
		keskikoko.setFont(fontti);
		keskikoko.addActionListener(this);
		
		suuri = new JMenuItem("suuri");
		suuri.setFont(fontti);
		suuri.addActionListener(this);
		
		valikko.add(pieni);
		valikko.add(keskikoko);
		valikko.add(suuri);
		ikkuna.setJMenuBar(menu);
	}
	
	
	
	/**
	 * Luo uuden kent�n
	 * @param pommienMaara, pommien m��r� kent�ss�
	 * @param matriisinKoko, kent�n koko leveys tai pituus suunnassa (kent�t aina neli�it�)
	 * @param fontinKoko, haluttu fonttikoko kirjaimille, riippuen kent�n koosta
	 */
	public void luoKentta(int pommienMaara, int matriisinKoko, int fontinKoko) {

		aika = new Timer(1000, this);
		tyhjienPaikkojenLkm = (matriisinKoko * matriisinKoko) - pommienMaara;
		
		sekuntit = 0;
		ylanappulat[0][2].setText(annaMinSek(sekuntit));
		peliLoppu = false;
		
		this.pommienMaara = pommienMaara;
		this.koko = matriisinKoko;
		this.pommejaJaljella = pommienMaara;
		
		ylanappulat[0][0].setText(Integer.toString(pommejaJaljella));
		
		//luodaan uusi PommiMatriisi
		pommit = new PommiMatriisi(pommienMaara, matriisinKoko);
		
		//*****************************************************//
		
		//luodaan matriisi JLabeleit� varten
		pohja = new JLabel[koko][koko];
		pohjaIkkuna = new JPanel();
		
		//luodaan matriisiIkkunalle kehys joka on saman kokoinen kun matriisi itse
		pohjaIkkuna.setLayout(new GridLayout(koko, koko));
		
		//luodaan nappulat matriisiIkkunaan
		for(int i=0; i<koko; i++) {
			for(int j=0; j<koko; j++) {
				pohja[i][j] = new JLabel(pommit.annaMatriisi(i,j), SwingConstants.CENTER);
				pohjaIkkuna.add(pohja[i][j]);
				pohja[i][j].setFont(new Font("Calibri", Font.PLAIN, fontinKoko));
				pohja[i][j].setBorder(rajat);
			}
		}
		
		//asetetaan matriisiIkkuna p��ikkunan keskelle
		ikkuna.add(pohjaIkkuna, BorderLayout.CENTER);
		
		//*************************************************************************//
		
		//luodaan matriisi ja mariisiIkkuna nappuloita varten
		nappulat = new JButton[koko][koko];
		nappulaIkkuna = new JPanel();
		
		//luodaan matriisiIkkunalle kehys joka on saman kokoinen kun matriisi itse
		nappulaIkkuna.setLayout(new GridLayout(koko, koko));
		 
		//luodaan nappulat matriisiIkkunaan
		for(int i=0; i<koko; i++) {
			for(int j=0; j<koko; j++) {
				nappulat[i][j] = new JButton("");
				nappulat[i][j].addMouseListener(this);
				nappulaIkkuna.add(nappulat[i][j]);
				nappulat[i][j].setFont(new Font("Calibri", Font.PLAIN, fontinKoko));
				nappulat[i][j].setMargin(new Insets(9, 9, 9, 9));
			}
		}
		
		//asetetaan matriisiIkkuna p��ikkunan keskelle, labelIkkunan p��lle
		ikkuna.add(nappulaIkkuna, BorderLayout.CENTER);
		
		
		ikkuna.setVisible(true);
	}
	
	
	//************************************************************************//
	
	/**
	 * Peli loppuu h�vi��n
	 * N�ytt�� kent�n pommipaikat ja Top-listan
	 */
	public void havio() {
		aika.stop();
		naytaKentta();
		
		peliLoppu = true;
		JOptionPane.showMessageDialog(null, ("PELI LOPPUI, H�VI�!" + "\n" + annaTop()));
	}
	
	/**
	 * Peli loppuu voittoon
	 * N�ytt�� kent�n pommipaikat
	 * Tallentaa loppuneen pelin ajan Top-listaan ja n�ytt�� itse Top-listan
	 */
	public void voitto() {
		aika.stop();
		naytaKentta();
		
		paivaMuoto = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		kalenteri = Calendar.getInstance();
		
		file.kirjoitaAika(file.annaFile(), ylanappulat[0][2].getText(), paivaMuoto.format(kalenteri.getTime()));
		
		peliLoppu = true;
		JOptionPane.showMessageDialog(null, ("PELI LOPPUI, VOITTO!" + "\n" + annaTop()));
	}
	
	
	/**
	 * Poistaa kaikki nappulat ja asettaa kyseisiin nappulakohtiin pohjan kohdat
	 */
	public void naytaKentta() {
		//n�ytt�� kaikki pommien paikat
		for(int i=0; i<koko; i++) {
			for(int j=0; j<koko; j++) {
				nappulaIkkuna.remove(nappulat[i][j]);
				nappulaIkkuna.add(pohja[i][j], (i*koko)+j);
				ikkuna.setVisible(true);
			}
		}
	}
	
	/**
	 * Poistaa kent�n
	 */
	public void poistaKentta() {
		ikkuna.remove(nappulaIkkuna);
		ikkuna.remove(pohjaIkkuna);
	}
	
	/**
	 * Lukee tiedoston ja palauttaa sen
	 * @return top-lista tiedosto
	 */
	public String annaTop() {
		return file.lueTiedosto(file.annaFile());
	}
	
	/**
	 * Muuttaa parametrina saaneet sekuntit, sekunteksi ja minuuteiksi ja palauttaa n�m�
	 * @param s, sekuntit
	 * @return sekuntit ja minuutit tallennettuna Stringiksi
	 */
	public String annaMinSek(int s) {
		int minuutit = 0;
		String sek = "0";
		
		while (s > 0) {
			if(s >= 60) {
				minuutit ++;
				s = s - 60;
			}
			else if(s < 60) {
				if(s < 10) {
					sek = "0" + s;
				}
				else {
					sek = Integer.toString(s);
				}
				s = 0;
			}
		}
		return (Integer.toString(minuutit) + ":" + sek);
	}
	

	//*********************************************************************************//
	

	@Override
	public void mouseClicked(MouseEvent e) {
		
		//jos painettu nappula on yl�nappulan "reset" nappula
		//luodaan uusi samankokoinen kentt� kuin juuri pelattu kentt�
		if(e.getSource() == ylanappulat[0][1]) {
			aika.stop();
			if (pommienMaara == 9) {
				poistaKentta();
				luoKentta(9, 8, 40);
			}
			else if (pommienMaara == 40) {
				poistaKentta();
				luoKentta(40, 16, 22);
			}
			else if (pommienMaara == 99) {
				poistaKentta();
				luoKentta(99, 24, 15);
			}
		}
		
		//jos peli ei ole loppunut
		else if(peliLoppu == false){
			aika.start();
			
			//etsit��n painettu nappula nappulat matriisista
			for(int i=0; i<koko; i++) {
				for(int j=0; j<koko; j++) {
					
					//nappula l�ytyi
					if(e.getSource() == nappulat[i][j]) {
						
						/*
						 * jos painettiin hiiren vasenta n�pp�int�
						 * jos alla on pommi, peli loppuu
						 * jos ei, poistetaan p��limm�inen nappula nappulaIkkunasta
						 */
						if(e.getButton() == MouseEvent.BUTTON1) {
							if(nappulat[i][j].getText().equals("!")) {
								nappulat[i][j].setText("");
								pommejaJaljella = pommejaJaljella + 1;
								ylanappulat[0][0].setText(Integer.toString(pommejaJaljella));
								ikkuna.add(nappulaIkkuna, BorderLayout.CENTER);
								ikkuna.setVisible(true);
							}
							nappulaIkkuna.remove(nappulat[i][j]);
							nappulaIkkuna.add(pohja[i][j], (i*koko)+j);
							ikkuna.setVisible(true);
								
							if(pohja[i][j].getText().equals("*")) {
								havio();	
							}
								
							else {
								if(tyhjienPaikkojenLkm == 0) {
									voitto();
								}
								ikkuna.add(nappulaIkkuna, BorderLayout.CENTER);
							}
						}
						
						/*
						 * jos painettiin hiiren oikeaa n�pp�int�
						 * merkataan pommi, jos ei ole jo merkattu
						 * muuten poisetetaan merkki
						 */
						if(e.getButton() == MouseEvent.BUTTON3) {							
							if(nappulat[i][j].getText().equals("!")) {
								nappulat[i][j].setText("");
								pommejaJaljella = pommejaJaljella + 1;
							}
							else {
								nappulat[i][j].setText("!");
								pommejaJaljella = pommejaJaljella - 1;
							}
							ylanappulat[0][0].setText(Integer.toString(pommejaJaljella));
							ikkuna.add(nappulaIkkuna, BorderLayout.CENTER);
							ikkuna.setVisible(true);
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		/*jos painetaa jotain valikon n�pp�imist�, pieni, keskikoko tai suuri
		*poistetaan kyseinen kentt� ja luodaan uusi samaa kokoa oleva kentt�
		*/
		if(e.getSource() == pieni) {
			aika.stop();
			poistaKentta();
			luoKentta(9, 8, 40);
		}
		else if(e.getSource() == keskikoko) {
			aika.stop();
			poistaKentta();
			luoKentta(40, 16, 22);
		}
		else if(e.getSource() == suuri) {
			aika.stop();
			poistaKentta();
			luoKentta(99, 24, 15);
		}
		
		else if(e.getSource() == aika) {
			sekuntit += 1;
			ylanappulat[0][2].setText(annaMinSek(sekuntit));
		}
	}
}