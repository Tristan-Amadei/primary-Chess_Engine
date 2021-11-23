package chess;

import java.util.List;

import AI.Coup;

public class Pawn extends Piece {
	
	public Pawn(String color, int x, int y) {
		super(color, x, y);
		name="Pawn";
	}
	
	public Square[] coupsPossibles(Board board) { // meme idee que la methode coupsPossibles des autres pieces, detaillee pour Bishop

		Square[] liste_coups = new Square[5]; //max 5 coups possibles
		int nb_coups=0;
		
		if (color == "White") {
			if (ligne-1>=0 && board.board[ligne-1][colonne].piece == null) { // le pion peut avancer
				liste_coups[nb_coups] = board.board[ligne-1][colonne];
				nb_coups++;
			}
			if (ligne-1>=0 && colonne-1 >=0 && board.board[ligne-1][colonne-1].piece != null && board.board[ligne-1][colonne-1].piece.color != color ) { //capture Ã  gauche
				liste_coups[nb_coups] = board.board[ligne-1][colonne-1];
				nb_coups++;
			}
			if (ligne-1>=0 && colonne+1 <8 && board.board[ligne-1][colonne+1].piece != null && board.board[ligne-1][colonne+1].piece.color != color) { //capture Ã  droite
				liste_coups[nb_coups] = board.board[ligne-1][colonne+1];
				nb_coups++;
			}
			if (ligne==6 && board.board[ligne-1][colonne].piece == null && board.board[ligne-2][colonne].piece == null) { //pion a sa position de depart, peut avancer de 2 cases
				liste_coups[nb_coups] = board.board[ligne-2][colonne];
				nb_coups++;
			}
		} else {
			if (ligne+1<8 && board.board[ligne+1][colonne].piece == null) {
				liste_coups[nb_coups] = board.board[ligne+1][colonne];
				nb_coups++;
			}
			if (ligne+1<8 && colonne-1>=0 && board.board[ligne+1][colonne-1].piece != null && board.board[ligne+1][colonne-1].piece.color != color) {
				liste_coups[nb_coups] = board.board[ligne+1][colonne-1];
				nb_coups++;
			}
			if (ligne+1<8 && colonne+1<8 && board.board[ligne+1][colonne+1].piece != null && board.board[ligne+1][colonne+1].piece.color != color) {
				liste_coups[nb_coups] = board.board[ligne+1][colonne+1];
				nb_coups++;
			}
			if (ligne==1 && board.board[ligne+1][colonne].piece == null && board.board[ligne+2][colonne].piece == null) {
				liste_coups[nb_coups] = board.board[ligne+2][colonne];
				nb_coups++;
			}
		}
		
		Square[] coupsEnPassant = this.coupsEnPassant(board);
		Square[] liste_courte_coups_possibles = new Square[nb_coups];
		for (int var=0; var<nb_coups; var++) {
			liste_courte_coups_possibles[var]=liste_coups[var];
		}
		Square[] liste_finale = Board.fusion(liste_courte_coups_possibles, coupsEnPassant);
		return liste_finale;
	}
	
	public Square[] coupsEnPassant(Board board) {
		if (board.nb_coups_joues>=3) {
			List<Coup> moves = board.coupsJoues;
			Coup lastMove = moves.get(board.nb_coups_joues-1);
			
			Square[] liste_coups = new Square[2];
			int nb_coups = 0;
			if (board.colorToPlay == "White" && ligne==3) {
				if (lastMove.piece_a_deplacer instanceof Pawn && lastMove.ligne_position_initiale_piece_a_deplacer == 1 && lastMove.colonne_position_initiale_piece_a_deplacer == colonne-1 && lastMove.ligne_case_deplacement_piece==3 && lastMove.colonne_case_deplacement_piece==colonne-1) {
					liste_coups[nb_coups] = board.board[2][colonne-1];
					nb_coups++;
				}
				if (lastMove.piece_a_deplacer instanceof Pawn && lastMove.ligne_position_initiale_piece_a_deplacer == 1 && lastMove.colonne_position_initiale_piece_a_deplacer == colonne+1 && lastMove.ligne_case_deplacement_piece==3 && lastMove.colonne_case_deplacement_piece==colonne+1) {
					liste_coups[nb_coups] = board.board[2][colonne+1];
					nb_coups++;
				}
			} 
			if (board.colorToPlay == "Black" && ligne == 4) {
				if (lastMove.piece_a_deplacer instanceof Pawn && lastMove.ligne_position_initiale_piece_a_deplacer == 6 && lastMove.colonne_position_initiale_piece_a_deplacer == colonne-1 && lastMove.ligne_case_deplacement_piece==4 && lastMove.colonne_case_deplacement_piece==colonne-1) {
					liste_coups[nb_coups] = board.board[5][colonne-1];
					nb_coups++;
				}
				if (lastMove.piece_a_deplacer instanceof Pawn && lastMove.ligne_position_initiale_piece_a_deplacer == 6 && lastMove.colonne_position_initiale_piece_a_deplacer == colonne+1 && lastMove.ligne_case_deplacement_piece == 4 && lastMove.colonne_case_deplacement_piece==colonne+1) {
					liste_coups[nb_coups] = board.board[5][colonne+1];
					nb_coups++;
				}
				
			}
			Square[] liste_courte_coups_possibles = new Square[nb_coups];
			for (int var=0; var<nb_coups; var++) {
				liste_courte_coups_possibles[var]=liste_coups[var];
			}
			return liste_courte_coups_possibles;
		} else {
			return new Square[0];
		}
		
}
	
	
	

	
	
	
}
