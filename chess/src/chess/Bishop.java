package chess;

public class Bishop extends Piece {
	
	public Bishop(String color, int x, int y) {
		super(color, x, y);
		name="Bishop";
	}

	public Square[] coupsPossibles(Board board) {
		/* on regarde sur les diagonales si les cases sont vides, dans ce cas-la on l'ajoute aux cases possibles et on continue sur cette diagonale, ou si la 
		case est occupee par une piece adverse, dans ce cas-la on l'ajoute aux cases possibles mais on ne continue pas dans cette direction car cela correspond a  
		une capture */
		
		Square[] liste_coups = new Square[13]; //13 coups possibles au max
		int nb_coups=0;
		
		int k=ligne-1;
		int l=colonne-1;
		while (k>=0 && l>=0 && board.board[k][l].piece == null) { // on regarde les cases sur la diagonale en haut a gauche de la piece
			liste_coups[nb_coups]=board.board[k][l];
			nb_coups++;
			k--;
			l--;
		}
		if (k>=0 && l>=0) {
			if (board.board[k][l].piece.color != color) {
				liste_coups[nb_coups]=board.board[k][l];
				nb_coups++;
			}
		}
		
		k=ligne-1;
		l=colonne+1;
		while (k>=0 && l<8 && board.board[k][l].piece == null) { // on regarde les cases sur la diagonale en haut a droite de la piece
			liste_coups[nb_coups]=board.board[k][l];
			nb_coups++;
			k--;
			l++;
		}
		if (k>=0 && l<8) {
			if (board.board[k][l].piece.color != color) { // on a rencontre une case non vide, on regarde si on peut capturer la piece ou non
				liste_coups[nb_coups]=board.board[k][l];
				nb_coups++;
			}
		}	
		
		k=ligne+1;
		l=colonne-1;
		while (k<8 && l>=0 && board.board[k][l].piece == null) { // on regarde les cases sur la diagonale en bas a gauche de la piece
			liste_coups[nb_coups]=board.board[k][l];
			nb_coups++;
			k++;
			l--;
		}
		if (k<8 && l>=0) {
			if (board.board[k][l].piece.color != color) {
				liste_coups[nb_coups]=board.board[k][l];
				nb_coups++;
			}
		}
		
		k=ligne+1;
		l=colonne+1;
		while (k<8 && l<8 && board.board[k][l].piece == null) { // on regarde les cases sur la diagonale en bas a droite de la piece
			liste_coups[nb_coups]=board.board[k][l];
			nb_coups++;
			k++;
			l++;
		}
		if (k<8 && l<8) {
			if (board.board[k][l].piece.color != color) {
				liste_coups[nb_coups]=board.board[k][l];
				nb_coups++;
			}
		}
		// on reduit la liste des coups possibles de maniere a ne garder que ces coups la, par exemple si on n'a trouve que 9 coups possibles, on cree une liste de taille 9, pas 13
		Square[] liste_courte_coups_possibles = new Square[nb_coups];
		for (int var=0; var<nb_coups; var++) {
			liste_courte_coups_possibles[var]=liste_coups[var];
		}
		return liste_courte_coups_possibles;
	}

}
