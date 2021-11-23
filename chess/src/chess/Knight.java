package chess;

public class Knight extends Piece {
	
	public Knight(String color, int x, int y) {
		super(color, x, y);
		name="Knight";
	}

	public Square[] coupsPossibles(Board board) {
		
		Square[] liste_coups = new Square[8]; //max 8 coups possibles 
		int nb_coups=0;
		
		int[][] positions_atteignables = {{ligne-1, colonne-2}, {ligne-2, colonne-1}, {ligne-2, colonne+1}, {ligne-1, colonne+2},
				{ligne+1, colonne+2}, {ligne+2, colonne+1}, {ligne+2, colonne-1}, {ligne+1, colonne-2}};
		for (int k=0; k<positions_atteignables.length; k++) {
			int i_atteignable=positions_atteignables[k][0];
			int j_atteignable = positions_atteignables[k][1];
			if (i_atteignable >=0 && i_atteignable <8 && j_atteignable >=0 && j_atteignable <8) { // donc si la case est bien sur l'echiquier
				if (board.board[i_atteignable][j_atteignable].piece == null || board.board[i_atteignable][j_atteignable].piece.color != color) {
					liste_coups[nb_coups]=board.board[i_atteignable][j_atteignable];
					nb_coups++;
				}
			}
		}
		
		Square[] liste_courte_coups_possibles = new Square[nb_coups];
		for (int var=0; var<nb_coups; var++) {
			liste_courte_coups_possibles[var]=liste_coups[var];
		}
		return liste_courte_coups_possibles;
	}
}
