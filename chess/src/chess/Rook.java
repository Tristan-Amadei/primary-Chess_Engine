package chess;

public class Rook extends Piece {
	public boolean hasAlreadyMoved; // comme pour le roi
	
	public Rook(String color, int x, int y){
		super(color, x, y);
		hasAlreadyMoved=false;
		name="Rook";
	}
	
	public Square[] coupsPossibles(Board board) { // comme pour le fou, mais dans le cas d'une tour
		Square[] liste_coups = new Square[14]; //une tour a, au maximum, 14 coups possibles
		int nb_coups=0;
		
		int k=ligne-1;
		while (k>=0 && board.board[k][colonne].piece == null) { //on remonte selon la colonne
			liste_coups[nb_coups]=board.board[k][colonne];
			k--;
			nb_coups++;
		}
		if (k>=0) { /*on s'est arrete parce qu'on a rencontre une case non vide, et non pas parce qu'on est arrive au bord de l'echiquier. 
                      on regarde donc si la case est occupee par une piece qu'on peut capturer ou non */
			if (board.board[k][colonne].piece.color != color) { // la piece est de couleur differente de la piece en parametre donc on peut la capturer
				liste_coups[nb_coups]=board.board[k][colonne];
				nb_coups++;
			}
		}
		
		k=ligne+1;
		while (k<8 && board.board[k][colonne].piece == null) { //on descend selon la colonne
			liste_coups[nb_coups]=board.board[k][colonne];
			k++;
			nb_coups++;
		}
		if (k<8) { 
			if (board.board[k][colonne].piece.color != color) {
				liste_coups[nb_coups]=board.board[k][colonne];
				nb_coups++;
			}
		}
		
		int l=colonne-1;
		while (l>=0 && board.board[ligne][l].piece == null) { //on va a gauche sur la ligne
			liste_coups[nb_coups]=board.board[ligne][l];
			l--;
			nb_coups++;
		}
		if (l>=0) { 
			if (board.board[ligne][l].piece.color != color) {
				liste_coups[nb_coups]=board.board[ligne][l];
				nb_coups++;
			}
		}
		
		l=colonne+1;
		while (l<8 && board.board[ligne][l].piece == null) { //on va a droite sur la ligne
			liste_coups[nb_coups]=board.board[ligne][l];
			l++;
			nb_coups++;
		}
		if (l<8) { 
			if (board.board[ligne][l].piece.color != color) {
				liste_coups[nb_coups]=board.board[ligne][l];
				nb_coups++;
			}
		}
		// on reduit la liste pour ne garder que les cases sur lesquelles la tour peut effectivement se deplacer
		Square[] liste_courte_coups_possibles = new Square[nb_coups];
		for (int var=0; var<nb_coups; var++) {
			liste_courte_coups_possibles[var]=liste_coups[var];
		}
		return liste_courte_coups_possibles;
		
	}
}
