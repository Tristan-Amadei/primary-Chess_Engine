package AI;


import chess.*;

public class Coup {
	public Piece piece_a_deplacer; // la piece qu'on veut deplacer
	public int ligne_position_initiale_piece_a_deplacer; //ligne sur laquelle se trouve la piece avant le deplacement
	public int colonne_position_initiale_piece_a_deplacer; // colonne sur laquelle se trouve la piece avant le deplacement
	public int ligne_case_deplacement_piece; //ligne de la case ou on deplace la piece
	public int colonne_case_deplacement_piece; // colonne de la case ou on deplace la piece
	public Piece piece_capturee; //la piece qui a ete capturee pendant le deplacement, peut etre null
	
	public Coup(Piece piece, int i, int j, int k, int l, Piece capture){
		this.piece_a_deplacer = piece;
		this.ligne_position_initiale_piece_a_deplacer=i;
		this.colonne_position_initiale_piece_a_deplacer=j;
		ligne_case_deplacement_piece=k;
		colonne_case_deplacement_piece=l;
		piece_capturee = capture;
	}
	
	public static Coup[] coupsPosition(Board board) { // renvoie la liste des coups possibles sur l'echiquier passe en parametre
		
		Coup[] listeCoups = new Coup[218];
		int compteur = 0;
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				
				if (board.board[k][l].piece != null && board.board[k][l].piece.getColor() == board.colorToPlay) {
					
					Square[] coupsPossibles = board.board[k][l].piece.CoupsLegaux(board);
					for (Square next_square : coupsPossibles) {
						Piece piece = deepCopy(k, l, board.board[k][l].piece);
						Piece captured_piece =  deepCopy(next_square.ligne, next_square.colonne, board.board[next_square.ligne][next_square.colonne].piece);
						Coup next_coup = new Coup(piece, k, l, next_square.ligne, next_square.colonne, captured_piece);
						listeCoups[compteur] = next_coup;
						compteur ++;
					}
				}
			}
		}
		Coup[] listeCourte = new Coup[compteur];
		for (int k=0; k<compteur; k++) {
			listeCourte[k] = listeCoups[k];
		}
		return listeCourte;
		
	}
	
	public void playMove(Board board) throws DeplacementNonValableException, CheckmateException { /*effectue le deplacement qui est contenu dans le coup this
	donc deplace la piece piece_a_deplacer sur la case (ligne_case_deplacement_piece, colonne_case_deplacement_piece) */
		if (piece_a_deplacer instanceof King && colonne_case_deplacement_piece == colonne_position_initiale_piece_a_deplacer+2) { // petit roque
			try {
				((King) piece_a_deplacer).petitRoque(board);
			} catch (DeplacementNonValableException e) {
				throw new DeplacementNonValableException();
			}
		}
		else if (piece_a_deplacer instanceof King && colonne_case_deplacement_piece == colonne_position_initiale_piece_a_deplacer-2) { // grand roque
			try {
				((King) piece_a_deplacer).grandRoque(board);
			} catch (DeplacementNonValableException e) {
				throw new DeplacementNonValableException();
			}
		}
		else {

			
			try {
				board.board[ligne_position_initiale_piece_a_deplacer][colonne_position_initiale_piece_a_deplacer].piece.deplacement(ligne_case_deplacement_piece, colonne_case_deplacement_piece, board);
			} catch (DeplacementNonValableException e) {
				throw new DeplacementNonValableException();
			} catch (CheckmateException e) {
				throw new CheckmateException();
			}
		}
		
	}
	
	public static Piece deepCopy(int i, int j, Piece piece) { // fait une deepcopy de la piece passee en parametre, permet de garder les coordonnees de la piece retournee inchangee meme si la piece this se deplace  
		Piece new_piece = null;
		
		if (piece instanceof Pawn) {
			new_piece = new Pawn(piece.getColor(), i, j);
		} else if (piece instanceof Rook) {
			new_piece = new Rook(piece.getColor(), i, j);
			((Rook) new_piece).hasAlreadyMoved = ((Rook) piece).hasAlreadyMoved;
		} else if (piece instanceof Bishop) {
			new_piece = new Bishop(piece.getColor(), i, j);
		} else if (piece instanceof Knight) {
			new_piece = new Knight(piece.getColor(), i, j);
		} else  if (piece instanceof Queen) {
			new_piece = new Queen(piece.getColor(), i, j);
		} else if (piece instanceof King) {
			new_piece = new King(piece.getColor(), i, j);
			((King) new_piece).hasAlreadyMoved = ((King) piece).hasAlreadyMoved;
		} 
		return new_piece;
	}

	
}
