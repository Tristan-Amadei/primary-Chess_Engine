package chess;

import java.util.ArrayList;
import java.util.List;

import AI.Coup;

public class Board {
	
	public Square[][] board;
	public Board previous_position; // on garde en memoire la position preceedente, utilisee pour jouer les captures en passant des pions
	public String colorToPlay; // on garde en memoire la couleur qui a droit de jouer 
	int nb_coups_joues; //on garde en memoire le nombre de coups joues
	List<Coup> coupsJoues; //on garde en memoire une liste des coups joues depuis le debut de la partie, de manire a pouvoir annuler des coups joues et revenir en arriere
	
	
	public Board() { // l'echiquier est d'abord completement vide de pieces
		Square[][] tab = new Square[8][8];
		for (int i=0; i<8; i++) {
			for (int j=0; j<8; j++) {
				tab[i][j]=new Square(i, j, null);
			}
		}
		board=tab;
		previous_position = null;
		nb_coups_joues = 0;
		coupsJoues = new ArrayList<Coup>();
	}
	
	public void colorPlayed() { // on change la couleur qui doit jouer
		if (colorToPlay == "White") {
			colorToPlay = "Black";
		} else {
			colorToPlay = "White";
		}
	}
	
	public Board deepCopy() { // fait une copie de l'ehiquier passe en parametre
		Board new_board = new Board();
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				Piece piece = board[k][l].piece;
				if (piece instanceof Pawn) {
					new_board.board[k][l].piece = new Pawn(piece.getColor(), k, l);
				} else if (piece instanceof Rook) {
					new_board.board[k][l].piece = new Rook(piece.getColor(), k, l);
					((Rook) new_board.board[k][l].piece).hasAlreadyMoved = ((Rook) piece).hasAlreadyMoved;
				} else if (piece instanceof Bishop) {
					new_board.board[k][l].piece = new Bishop(piece.getColor(), k, l);
				} else if (piece instanceof Knight) {
					new_board.board[k][l].piece = new Knight(piece.getColor(), k, l);
				} else  if (piece instanceof Queen) {
					new_board.board[k][l].piece = new Queen(piece.getColor(), k, l);
				} else if (piece instanceof King) {
					new_board.board[k][l].piece = new King(piece.getColor(), k, l);
					((King) new_board.board[k][l].piece).hasAlreadyMoved = ((King) piece).hasAlreadyMoved;
				} else {
					new_board.board[k][l].piece = null;
				}				
			}
		}
		new_board.previous_position=this.previous_position;
		new_board.colorToPlay=colorToPlay;
		
		List<Coup> coupsJoues_deepcopy = new ArrayList<Coup>();
		
		for (Coup move : coupsJoues) {
			Piece piece_a_deplacer = Coup.deepCopy(move.ligne_position_initiale_piece_a_deplacer, move.colonne_position_initiale_piece_a_deplacer, move.piece_a_deplacer);
			Coup new_move = new Coup(piece_a_deplacer, move.ligne_position_initiale_piece_a_deplacer, move.colonne_position_initiale_piece_a_deplacer, move.ligne_position_initiale_piece_a_deplacer, move.colonne_case_deplacement_piece, move.piece_capturee);
			coupsJoues_deepcopy.add(new_move);
		}
		new_board.coupsJoues = coupsJoues_deepcopy;
		new_board.nb_coups_joues = nb_coups_joues;
		return new_board;
	}
	
	public void newGameBoard() { // on met en place l'echiquier de debut de partie 
		board[0][0].addPiece(new Rook("Black", 0, 0));
		board[0][7].addPiece(new Rook("Black", 0, 7));
		board[7][0].addPiece(new Rook("White", 7, 0));
		board[7][7].addPiece(new Rook("White", 7, 7));
		
		board[0][1].addPiece(new Knight("Black", 0, 1));
		board[0][6].addPiece(new Knight("Black", 0, 6));
		board[7][1].addPiece(new Knight("White", 7, 1));
		board[7][6].addPiece(new Knight("White", 7, 6));
		
		board[0][2].addPiece(new Bishop("Black", 0, 2));
		board[0][5].addPiece(new Bishop("Black", 0, 5));
		board[7][2].addPiece(new Bishop("White", 7, 2));
		board[7][5].addPiece(new Bishop("White", 7, 5));
		
		board[0][3].addPiece(new Queen("Black", 0, 3));
		board[7][3].addPiece(new Queen("White", 7, 3));
		
		board[0][4].addPiece(new King("Black", 0, 4));
		board[7][4].addPiece(new King("White", 7, 4));
		
		for (int i=0; i<8; i++) {
			board[1][i].addPiece(new Pawn("Black", 1, i));
			board[6][i].addPiece(new Pawn("White", 6, i));
		}
		colorToPlay = "White";
	}
	
	public void display() { // permet d'afficher l'echiquier dans la console
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				board[k][l].display();
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public Square getKingPosition(String color) { // sert a connaitre la position du roi "couleur" sur l'echiquier
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board[k][l].piece != null) {
					if (board[k][l].piece.name == "King" && board[k][l].piece.color == color) {
						return board[k][l];
					}
				}
			}
		}
		return null;
	}
	
	
	public int nbCoupsLegauxPosition() { // renvoie le nombre de coups legaux que la couleur colorToPlay peut jouer 
		int nb_coups = 0;
		String c = colorToPlay;
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				
				if (board[k][l].piece != null && board[k][l].piece.color == c) {
					nb_coups = nb_coups + board[k][l].piece.nbCoupsLegaux(this);
				}
				
			}
		}
		return nb_coups;
	}
	
	public int checkmatedKing() { //return 0 si la position est jouable, 1 s'il y a mat et 2 si c'est pat 
		int nb_coups_position = this.nbCoupsLegauxPosition();
		if (nb_coups_position == 0) {
			if (((King) this.getKingPosition(colorToPlay).piece).isInCheck(this)) { //le roi est en echec et il n'y a plus de coups possibles donc c'est mat
				return 1;
			} else { //plus de coups possibles mais roi pas en echec donc pat 
				return 2;
			}
		} else {
			return 0;
		}
	}
	
	public Square[] casesControllees(String color) { // on renvoie la liste de tous les coups possibles pour la personne qui doit jouer 
		String initial_color = colorToPlay;
		
		if (colorToPlay != color) { // si on ne change pas la couleur du joueur qui doit jouer, on se retrouve avec une liste vide car aucun coup n'est legal
			this.colorPlayed();
		}
		Square[] casesControllees = new Square[0];
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board[k][l].piece != null && board[k][l].piece.color == color) {
					Square[] coups_piece = board[k][l].piece.CoupsLegaux(this);
					casesControllees = Board.fusion(casesControllees, coups_piece);
				}
			}
		}
		colorToPlay = initial_color; //on remet la bonne couleur du prochain coup si on l'avait changee au debut de la methode
		return casesControllees;
	}
	
	
	public Square[] CasesControlleesParCapture(String color) { // comme la fonction precedente mais on s'interesse aux captures donc les pions controlent les cases en diagonale
		
		String initial_color = colorToPlay;
		
		if (colorToPlay != color) { // si on ne change pas la couleur du joueur qui doit jouer, on se retrouve avec une liste vide car aucun coup n'est legal
			this.colorPlayed();
		}
		Square[] casesControllees = new Square[0];
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board[k][l].piece != null && board[k][l].piece.color == color) {
					Square[] coups_piece;
					if (board[k][l].piece instanceof Pawn) {
						coups_piece = new Square[0];
						if (initial_color == "White") {
							if (l-1 >=0 && k-1>=0) {
								Square[] coup = {board[k-1][l-1]};
								coups_piece = Board.fusion(coups_piece, coup);
							}
							if (l+1 <8 && k-1>=0) {
								Square[] coup = {board[k-1][l+1]};
								coups_piece = Board.fusion(coups_piece, coup);
							}
							
						} else {
							if (l-1>=0 && k+1<8) {
								Square[] coup = {board[k+1][l-1]};
								coups_piece = Board.fusion(coups_piece, coup);
							} if (l+1 <8 && k+1<8) {
								Square[] coup = {board[k+1][l+1]};
								coups_piece = Board.fusion(coups_piece, coup);
							}
							
						}
						
					} else {
						coups_piece = board[k][l].piece.CoupsLegaux(this);
					}
					casesControllees = Board.fusion(casesControllees, coups_piece);
				}
			}
		}
		colorToPlay = initial_color; //on remet la bonne couleur du prochain coup si on l'avait potentiellement changÃ©e au debut de la methode
		return casesControllees;
		
	}
	
	public void resetSquaresCoordinates() { // la methode permet de faire correspondre les coordonnees des pieces avec les coordonees des cases sur lesquelles elles sont
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board[k][l].piece != null) {
					board[k][l].piece.ligne = k;
					board[k][l].piece.colonne=l;
				}
			}
		}
	}
	
	
	public void unmakeMove() { //on annule le dernier coup joue et on revient donc a la position precedente
		int nb = nb_coups_joues-1;
		Coup lastMove = coupsJoues.get(nb);
		
		if (lastMove.piece_capturee instanceof Pawn && lastMove.piece_capturee.ligne != lastMove.ligne_case_deplacement_piece) {
			// capture en passant
			board[lastMove.ligne_case_deplacement_piece][lastMove.colonne_case_deplacement_piece].piece = null;
			board[lastMove.ligne_position_initiale_piece_a_deplacer][lastMove.colonne_position_initiale_piece_a_deplacer].piece = new Pawn(lastMove.piece_a_deplacer.getColor(), lastMove.ligne_position_initiale_piece_a_deplacer, lastMove.colonne_position_initiale_piece_a_deplacer);
			board[lastMove.piece_capturee.ligne][lastMove.piece_capturee.colonne].piece = lastMove.piece_capturee;
			coupsJoues.remove(nb);
			nb_coups_joues--;
		} 
		
		else if(coupsJoues.get(nb_coups_joues-1).piece_a_deplacer.color == colorToPlay) { // la couleur de la derniere piece qui s'est deplace est egale à la couleur du joueur dont c'est le tour, donc il y a eu un roque entre les deux tours
			String color_to_unmake_move;
			if (colorToPlay == "White") {
				color_to_unmake_move = "Black";
			} else {
				color_to_unmake_move = "White";
			}
			Square king_position = this.getKingPosition(color_to_unmake_move);
			if (king_position.colonne == 6) { //le roque qui a ete effectue est un grand roque
				board[king_position.ligne][4].piece = king_position.piece;
				king_position.piece = null;
				board[king_position.ligne][7].piece = board[king_position.ligne][5].piece; //on remet la tour a sa position avant le roque
				board[king_position.ligne][5].piece = null;
				this.resetSquaresCoordinates();
				((King) board[king_position.ligne][4].piece).hasAlreadyMoved = false;
				((Rook) board[king_position.ligne][7].piece).hasAlreadyMoved = false;
			} else {
				board[king_position.ligne][4].piece = king_position.piece;
				king_position.piece = null;
				board[king_position.ligne][0].piece = board[king_position.ligne][3].piece; //on remet la tour a sa position avant le roque
				board[king_position.ligne][3].piece = null;
				this.resetSquaresCoordinates();
				((King) board[king_position.ligne][4].piece).hasAlreadyMoved = false;
				((Rook) board[king_position.ligne][0].piece).hasAlreadyMoved = false;
			}
		} 
		
		else {
			board[lastMove.ligne_position_initiale_piece_a_deplacer][lastMove.colonne_position_initiale_piece_a_deplacer].piece = lastMove.piece_a_deplacer;
			board[lastMove.ligne_case_deplacement_piece][lastMove.colonne_case_deplacement_piece].piece = lastMove.piece_capturee;
			coupsJoues.remove(nb);
			nb_coups_joues--;
		}
		

		this.resetSquaresCoordinates();
		this.colorPlayed();
		
		
	}

	public static Square[] fusion(Square[] l1, Square[] l2) { // fusionner deux listes, on la met en place simplement pour faciliter le code et eviter de la refaire a chaque fois qu'on en a besoin
		Square[] l = new Square[l1.length + l2.length];
		for (int k=0; k<l1.length; k++) {
			l[k]=l1[k];
		}
		for (int k=0; k<l2.length; k++) {
			l[l1.length+k]=l2[k];
		}
		return l;
	}
	
	

}
