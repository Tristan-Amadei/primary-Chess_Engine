package AI;

import chess.*;

public class Evaluation {
	
	public static double rawEvaluation(Board board) { // aux echecs, chaque piece possede une valeur. Cette methode permet de renvoyer l'evaluation de la position en prenant seulement en compte la valeur des pieces
		double score=0;
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board.board[k][l].piece != null) {
					Piece piece = board.board[k][l].piece;
					if (piece.getName() == "Pawn") {
						if (piece.getColor() == "White") {
							score += 100;
						} else {
							score -= 100;
						}
					} else if (piece.getName() == "Rook") {
						if (piece.getColor() == "White") {
							score += 500;
						} else {
							score -= 500;
						}
					} else if (piece.getName() == "Knight") {
						if (piece.getColor() == "White") {
							score += 320;
						} else {
							score -= 320;
						}
					} else if (piece.getName() == "Bishop") {
						if (piece.getColor() == "White") {
							score += 330;
						} else {
							score -= 330;
						}
					} else if (piece.getName() == "Queen") {
						if (piece.getColor() == "White") {
							score += 900;
						} else {
							score -= 900;
						}
					}
				}
			}
		}
		return score;
	}
	
	public static double heuristicEvaluation(Board board) { /* implemente les regles avancees d'evaluation d'une position, 
															en fonction de la position d'une piece, mobilite des pieces, etc */
		double heuristic_score = 0;
		
		// Mobilite, ie nb de cases controlees
		Square[] casescontrolles_white = board.CasesControlleesParCapture("White");
		int mobilite_white = casescontrolles_white.length;
		Square[] casescontrolles_black = board.CasesControlleesParCapture("Black");
		int mobilite_black = casescontrolles_black.length;
		heuristic_score += 5*(mobilite_white-mobilite_black);
		
		// Pions doubles
		int nb_doubled_pawns_white = doubledPawns("White", board);
		int nb_doubled_pawns_black = doubledPawns("Black", board);
		heuristic_score -= 5*(nb_doubled_pawns_white - nb_doubled_pawns_black);
		
		// Pions isoles
		int nb_iso_pawns_white = isolatedPawns("White", board);
		int nb_iso_pawns_black = isolatedPawns("Black", board);
		heuristic_score -= 5*(nb_iso_pawns_white - nb_iso_pawns_black);
		
		// Evaluation bonusSquare
		int bonus_eval = 0;
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				bonus_eval += Evaluation.bonusSquare(k, l, board);
			}
		}
		heuristic_score += bonus_eval;
		
		return heuristic_score;
	}
	
	public static int doubledPawns(String color, Board board) { //on regarde sur toutes les colonnes s'il y a plusieurs pions doubles, ce qui est mauvais pour l'evaluation
		int nb_doubled_pawns = 0;
		for (int l=0; l<8; l++) {
			int pawns_l = 0; //on regarde le nb de pions de la couleur sur la colonne l
			for (int k=0; k<8; k++) {
				if (board.board[k][l].piece != null && board.board[k][l].piece.getColor() == color && board.board[k][l].piece instanceof Pawn) {
					pawns_l++;
				}
			}
			if (pawns_l >=2) { //alors il y a des pions doubles sur la colonne l
				nb_doubled_pawns += pawns_l -1; //on ne prend en compte que le deuxieme pion sur la colonne, pas les deux car il y en a 1 qui est bien place (et de meme s'il y en a plus de 2, mais rare)
			}
		}
		return nb_doubled_pawns;
	}
	
	public static int pawnOnRank(int l, String color, Board board) { //on regarde le nombre de pions sur la colonne l, sera utilisee dans la methode isolatedPawns
		int nb_pawns_on_rank=0;
		if (l>=0 && l<8) {
			for (int k=0; k<8; k++) {
				if (board.board[k][l].piece != null && board.board[k][l].piece.getColor() == color && board.board[k][l].piece instanceof Pawn) {
					nb_pawns_on_rank++;
				}
			}
		}
		return nb_pawns_on_rank;
	}
	
	public static int isolatedPawns(String color, Board board) { // on regarde le nombre de pions isoles pour la couleur color. Un pion est isole s'il n'y aucun pion de la meme couleur sur les colonnes sur sa droite et sa gauche
		int nb_iso_pawns = 0;
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board.board[k][l].piece != null && board.board[k][l].piece.getColor() == color && board.board[k][l].piece instanceof Pawn) {
					boolean iso = Evaluation.pawnOnRank(l-1, color, board) + Evaluation.pawnOnRank(l+1, color, board) == 0;
					if (iso) {
						nb_iso_pawns++;
					}
				}
			}
		}
		return nb_iso_pawns;
	}
	
	/* Pour toutes les fonctions bonusSquare qui suivent, la matrice affichee correspond a l'evaluation qu'on rajoute aux pieces blanches. 
	 * Pour les pieces noires, il suffit de renverser la matrice et de multiplier par -1 les bonus d'evaluatio  obtenus 
	 */
	public static int bonusSquareKnight(int i, int j, Board board) {
		/* le but de cette fonction est de mettre en place cette matrice de valeur a ajouter a l'evaluation d'une position donnee 
		 -50,-40,-30,-30,-30,-30,-40,-50,
		 -40,-20,  0,  0,  0,  0,-20,-40,
		 -30,  0, 10, 15, 15, 10,  0,-30,
		 -30,  5, 15, 20, 20, 15,  5,-30,
		 -30,  0, 15, 20, 20, 15,  0,-30,
		 -30,  5, 10, 15, 15, 10,  5,-30,
		 -40,-20,  0,  5,  5,  0,-20,-40,
		 -50,-40,-30,-30,-30,-30,-40,-50,*/
		int bonus = 0; // par defaut si la piece ne correspond pas a ce qu'on veut, on n'ajoute aucune evaluation
		if (board.board[i][j].piece != null && board.board[i][j].piece instanceof Knight) {
			
			switch(i) {
			case 0: switch(j) {
					case 0: bonus = -50;break;
					case 1: bonus = -40;break;
					case 2: bonus = -30;break;
					case 3: bonus = -30;break;
					case 4: bonus = -30;break;
					case 5: bonus = -30;break;
					case 6: bonus = -40;break;
					case 7: bonus = -50;break;
				}; break;
			case 1: switch(j) {
					case 0: bonus = -40;break;
					case 1: bonus = -20;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = -20;break;
					case 7: bonus = -40;break;
					}; break;
			case 2: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = 0;break;
					case 2: bonus = 10;break;
					case 3: bonus = 15;break;
					case 4: bonus = 15;break;
					case 5: bonus = 10;break;
					case 6: bonus = 0;break;
					case 7: bonus = -30;break;
					}; break;
			case 3: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = 5;break;
					case 2: bonus = 15;break;
					case 3: bonus = 20;break;
					case 4: bonus = 20;break;
					case 5: bonus = 15;break;
					case 6: bonus = 5;break;
					case 7: bonus = -30;break;
					}; break;
			default:
				bonus = bonusSquareKnight(7-i, j, board); // on utilise le fait que les parties hautes et basses de la matrice sont les memes
			}
			
		}
		return bonus;
			
	}
	
	public static int bonusSquareBishop(int i, int j, Board board) {
		/* le but de cette fonction est de mettre en place cette matrice de valeur a ajouter a l'evaluation d'une position donnee 
		 -20,-10,-10,-10,-10,-10,-10,-20,
		-10,  0,  0,  0,  0,  0,  0,-10,
		-10,  0,  5, 10, 10,  5,  0,-10,
		-10,  5,  5, 10, 10,  5,  5,-10,
		-10,  0, 10, 10, 10, 10,  0,-10,
		-10, 10, 10, 10, 10, 10, 10,-10,
		-10,  5,  0,  0,  0,  0,  5,-10,
		-20,-10,-10,-10,-10,-10,-10,-20,  */
		int bonus = 0; // par defaut si la piece ne correspond pas a ce qu'on veut, on n'ajoute aucune evaluation
		if (board.board[i][j].piece != null && board.board[i][j].piece instanceof Bishop) {
			
			switch(i) {
			case 0: switch(j) {
					case 0: bonus = -20;break;
					case 1: bonus = -10;break;
					case 2: bonus = -10;break;
					case 3: bonus = -10;break;
					case 4: bonus = -10;break;
					case 5: bonus = -10;break;
					case 6: bonus = -10;break;
					case 7: bonus = -20;break;
				}; break;
			case 1: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = -10;break;
					}; break;
			case 2: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 0;break;
					case 2: bonus = 5;break;
					case 3: bonus = 10;break;
					case 4: bonus = 10;break;
					case 5: bonus = 5;break;
					case 6: bonus = 0;break;
					case 7: bonus = -10;break;
					}; break;
			case 3: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 5;break;
					case 2: bonus = 5;break;
					case 3: bonus = 10;break;
					case 4: bonus = 10;break;
					case 5: bonus = 5;break;
					case 6: bonus = 5;break;
					case 7: bonus = -10;break;
					}; break;
			case 4: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 0;break;
					case 2: bonus = 10;break;
					case 3: bonus = 10;break;
					case 4: bonus = 10;break;
					case 5: bonus = 10;break;
					case 6: bonus = 0;break;
					case 7: bonus = -10;break;
					}; break;
			case 5: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 10;break;
					case 2: bonus = 10;break;
					case 3: bonus = 10;break;
					case 4: bonus = 10;break;
					case 5: bonus = 10;break;
					case 6: bonus = 10;break;
					case 7: bonus = -10;break;
					}; break;
			case 6: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 5;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 5;break;
					case 7: bonus = -10;break;
					}; break;
			case 7: switch(j) {
					case 0: bonus = -20;break;
					case 1: bonus = -10;break;
					case 2: bonus = -10;break;
					case 3: bonus = -10;break;
					case 4: bonus = -10;break;
					case 5: bonus = -10;break;
					case 6: bonus = -10;break;
					case 7: bonus = -20;break;
					}; break;
			}
			
		}
		return bonus;
			
	}
	
	public static int bonusSquareRook(int i, int j, Board board) {
		/* le but de cette fonction est de mettre en place cette matrice de valeur a ajouter a l'evaluation d'une position donnee 
		 0,  0,  0,  0,  0,  0,  0,  0,
		  5, 10, 10, 10, 10, 10, 10,  5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		 -5,  0,  0,  0,  0,  0,  0, -5,
		  0,  0,  0,  5,  5,  0,  0,  0  */
		int bonus = 0; // par defaut si la piece ne correspond pas a ce qu'on veut, on n'ajoute aucune evaluation
		if (board.board[i][j].piece != null && board.board[i][j].piece instanceof Rook) {
			
			switch(i) {
			case 0: bonus = 0;break;
			case 1: switch(j) {
					case 0: bonus = 5;break;
					case 1: bonus = 10;break;
					case 2: bonus = 10;break;
					case 3: bonus = 10;break;
					case 4: bonus = 10;break;
					case 5: bonus = 10;break;
					case 6: bonus = 10;break;
					case 7: bonus = 5;break;
					}; break;
			case 2: switch(j) {
					case 0: bonus = -5;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = -5;break;
					}; break;
			case 3: switch(j) {
					case 0: bonus = -5;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = -5;break;
					}; break;
			case 4: switch(j) {
					case 0: bonus = -5;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = -5;break;
					}; break;
			case 5: switch(j) {
					case 0: bonus = -5;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = -5;break;
					}; break;
			case 6: switch(j) {
					case 0: bonus = -5;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = -5;break;
					}; break;
			case 7: switch(j) {
					case 0: bonus = 0;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 5;break;
					case 4: bonus = 5;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = 0;break;
					}; break;
			}
			
		}
		return bonus;
			
	}
	
	public static int bonusSquareQueen(int i, int j, Board board) {
		/* le but de cette fonction est de mettre en place cette matrice de valeur a ajouter a l'evaluation d'une position donnee 
		 -20,-10,-10, -5, -5,-10,-10,-20,
		-10,  0,  0,  0,  0,  0,  0,-10,
		-10,  0,  5,  5,  5,  5,  0,-10,
		 -5,  0,  5,  5,  5,  5,  0, -5,
		  0,  0,  5,  5,  5,  5,  0, -5,
		-10,  5,  5,  5,  5,  5,  0,-10,
		-10,  0,  5,  0,  0,  0,  0,-10,
		-20,-10,-10, -5, -5,-10,-10,-20  */
		int bonus = 0; // par defaut si la piece ne correspond pas a ce qu'on veut, on n'ajoute aucune evaluation
		if (board.board[i][j].piece != null && board.board[i][j].piece instanceof Queen) {
			
			switch(i) {
			case 0: switch(j) {
					case 0: bonus = -20;break;
					case 1: bonus = -10;break;
					case 2: bonus = -10;break;
					case 3: bonus = -5;break;
					case 4: bonus = -5;break;
					case 5: bonus = -10;break;
					case 6: bonus = -10;break;
					case 7: bonus = -20;break;
					}; break;
			case 1: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = -10;break;
					}; break;
			case 2: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 0;break;
					case 2: bonus = 5;break;
					case 3: bonus = 5;break;
					case 4: bonus = 5;break;
					case 5: bonus = 5;break;
					case 6: bonus = 0;break;
					case 7: bonus = -10;break;
					}; break;
			case 3: switch(j) {
					case 0: bonus = -5;break;
					case 1: bonus = 0;break;
					case 2: bonus = 5;break;
					case 3: bonus = 5;break;
					case 4: bonus = 5;break;
					case 5: bonus = 5;break;
					case 6: bonus = 0;break;
					case 7: bonus = -5;break;
					}; break;
			case 4: switch(j) {
					case 0: bonus = 0;break;
					case 1: bonus = 0;break;
					case 2: bonus = 5;break;
					case 3: bonus = 5;break;
					case 4: bonus = 5;break;
					case 5: bonus = 5;break;
					case 6: bonus = 0;break;
					case 7: bonus = -5;break;
					}; break;
			case 5: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 5;break;
					case 2: bonus = 5;break;
					case 3: bonus = 5;break;
					case 4: bonus = 5;break;
					case 5: bonus = 5;break;
					case 6: bonus = 0;break;
					case 7: bonus = -10;break;
					}; break;
			case 6: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = 0;break;
					case 2: bonus = 5;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = -10;break;
					}; break;
			case 7: bonus = bonusSquareQueen(0, j, board);
			}
			
		}
		return bonus;
			
	}
	
	public static int bonusSquarePawn(int i, int j, Board board) {
		/* le but de cette fonction est de mettre en place cette matrice de valeur a ajouter a l'evaluation d'une position donnee 
		  0,  0,  0,  0,  0,  0,  0,  0,
		50, 50, 50, 50, 50, 50, 50, 50,
		10, 10, 20, 30, 30, 20, 10, 10,
		 5,  5, 10, 25, 25, 10,  5,  5,
		 0,  0,  0, 20, 20,  0,  0,  0,
		 5, -5,-10,  0,  0,-10, -5,  5,
		 5, 10, 10,-20,-20, 10, 10,  5,
		 0,  0,  0,  0,  0,  0,  0,  0  */
		int bonus = 0; // par defaut si la piece ne correspond pas a ce qu'on veut, on n'ajoute aucune evaluation
		if (board.board[i][j].piece != null && board.board[i][j].piece instanceof Pawn) {
			
			switch(i) {
			case 0: bonus = 0;break;
			case 1: bonus = 50; break;
			case 2: switch(j) {
					case 0: bonus = 10;break;
					case 1: bonus = 10;break;
					case 2: bonus = 20;break;
					case 3: bonus = 30;break;
					case 4: bonus = 30;break;
					case 5: bonus = 20;break;
					case 6: bonus = 10;break;
					case 7: bonus = 10;break;
					}; break;
			case 3: switch(j) {
					case 0: bonus = 5;break;
					case 1: bonus = 5;break;
					case 2: bonus = 10;break;
					case 3: bonus = 25;break;
					case 4: bonus = 25;break;
					case 5: bonus = 10;break;
					case 6: bonus = 5;break;
					case 7: bonus = 5;break;
					}; break;
			case 4: switch(j) {
					case 0: bonus = 0;break;
					case 1: bonus = 0;break;
					case 2: bonus = 0;break;
					case 3: bonus = 20;break;
					case 4: bonus = 20;break;
					case 5: bonus = 0;break;
					case 6: bonus = 0;break;
					case 7: bonus = 0;break;
					}; break;
			case 5: switch(j) {
					case 0: bonus = 5;break;
					case 1: bonus = -5;break;
					case 2: bonus = -10;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = -10;break;
					case 6: bonus = -5;break;
					case 7: bonus = 5;break;
					}; break;
			case 6: switch(j) {
					case 0: bonus = 5;break;
					case 1: bonus = 10;break;
					case 2: bonus = 10;break;
					case 3: bonus = -20;break;
					case 4: bonus = -20;break;
					case 5: bonus = 10;break;
					case 6: bonus = 10;break;
					case 7: bonus = 5;break;
					}; break;
			case 7: bonus = 0; break;
			}
			
		}
		return bonus;
			
	}
	
	public static int bonusSquareKingMiddleGame(int i, int j, Board board) {
		/* le but de cette fonction est de mettre en place cette matrice de valeur a ajouter a l'evaluation d'une position donnee 
		 	-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-30,-40,-40,-50,-50,-40,-40,-30,
			-20,-30,-30,-40,-40,-30,-30,-20,
			-10,-20,-20,-20,-20,-20,-20,-10,
			 10, 15,  0,  0,  0,  0, 15, 10,
			 20, 50, 10,  0,  0, 10, 50, 20  */
		int bonus = 0; // par defaut si la piece ne correspond pas a ce qu'on veut, on n'ajoute aucune evaluation
		if (board.board[i][j].piece != null && board.board[i][j].piece instanceof King) {
			
			switch(i) {
			case 0: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -40;break;
					case 2: bonus = -40;break;
					case 3: bonus = -50;break;
					case 4: bonus = -50;break;
					case 5: bonus = -40;break;
					case 6: bonus = -40;break;
					case 7: bonus = -30;break;
				}; break;
			case 1: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -40;break;
					case 2: bonus = -40;break;
					case 3: bonus = -50;break;
					case 4: bonus = -50;break;
					case 5: bonus = -40;break;
					case 6: bonus = -40;break;
					case 7: bonus = -30;break;
					}; break;
			case 2: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -40;break;
					case 2: bonus = -40;break;
					case 3: bonus = -50;break;
					case 4: bonus = -50;break;
					case 5: bonus = -40;break;
					case 6: bonus = -40;break;
					case 7: bonus = -30;break;
					}; break;
			case 3: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -40;break;
					case 2: bonus = -40;break;
					case 3: bonus = -50;break;
					case 4: bonus = -50;break;
					case 5: bonus = -40;break;
					case 6: bonus = -40;break;
					case 7: bonus = -30;break;
					}; break;
			case 4: switch(j) {
					case 0: bonus = -20;break;
					case 1: bonus = -30;break;
					case 2: bonus = -30;break;
					case 3: bonus = -40;break;
					case 4: bonus = -40;break;
					case 5: bonus = -30;break;
					case 6: bonus = -30;break;
					case 7: bonus = -20;break;
					}; break;
			case 5: switch(j) {
					case 0: bonus = -10;break;
					case 1: bonus = -20;break;
					case 2: bonus = -20;break;
					case 3: bonus = -20;break;
					case 4: bonus = -20;break;
					case 5: bonus = -20;break;
					case 6: bonus = -20;break;
					case 7: bonus = -10;break;
					}; break;
			case 6: switch(j) {
					case 0: bonus = 10;break;
					case 1: bonus = 15;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = 15;break;
					case 7: bonus = 10;break;
					}; break;
			case 7: switch(j) {
					case 0: bonus = 20;break;
					case 1: bonus = 50;break;
					case 2: bonus = 10;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 10;break;
					case 6: bonus = 50;break;
					case 7: bonus = 20;break;
					}; break;
			}
			
		}
		return bonus;
			
	}
	
	public static int bonusSquareKingEndGame(int i, int j, Board board) {
		/* le but de cette fonction est de mettre en place cette matrice de valeur a ajouter a l'evaluation d'une position donnee 
		 	-50,-40,-30,-20,-20,-30,-40,-50,
			-30,-20,-10,  0,  0,-10,-20,-30,
			-30,-10, 20, 30, 30, 20,-10,-30,
			-30,-10, 30, 40, 40, 30,-10,-30,
			-30,-10, 30, 40, 40, 30,-10,-30,
			-30,-10, 20, 30, 30, 20,-10,-30,
			-30,-30,  0,  0,  0,  0,-30,-30,
			-50,-30,-30,-30,-30,-30,-30,-50 */
		int bonus = 0; // par defaut si la piece ne correspond pas a ce qu'on veut, on n'ajoute aucune evaluation
		if (board.board[i][j].piece != null && board.board[i][j].piece instanceof King) {
			
			switch(i) {
			case 0: switch(j) {
					case 0: bonus = -50;break;
					case 1: bonus = -40;break;
					case 2: bonus = -30;break;
					case 3: bonus = -20;break;
					case 4: bonus = -20;break;
					case 5: bonus = -30;break;
					case 6: bonus = -40;break;
					case 7: bonus = -50;break;
				}; break;
			case 1: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -20;break;
					case 2: bonus = -10;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = -10;break;
					case 6: bonus = -30;break;
					case 7: bonus = -40;break;
					}; break;
			case 2: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = 10;break;
					case 2: bonus = 20;break;
					case 3: bonus = 30;break;
					case 4: bonus = 30;break;
					case 5: bonus = 20;break;
					case 6: bonus = -10;break;
					case 7: bonus = -30;break;
					}; break;
			case 3: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -10;break;
					case 2: bonus = 30;break;
					case 3: bonus = 40;break;
					case 4: bonus = 40;break;
					case 5: bonus = 30;break;
					case 6: bonus = -10;break;
					case 7: bonus = -30;break;
					}; break;
			case 4: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -10;break;
					case 2: bonus = 30;break;
					case 3: bonus = 40;break;
					case 4: bonus = 40;break;
					case 5: bonus = 30;break;
					case 6: bonus = -10;break;
					case 7: bonus = -30;break;
					}; break;
			case 5: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -10;break;
					case 2: bonus = 20;break;
					case 3: bonus = 30;break;
					case 4: bonus = 30;break;
					case 5: bonus = 20;break;
					case 6: bonus = -10;break;
					case 7: bonus = -30;break;
					}; break;
			case 6: switch(j) {
					case 0: bonus = -30;break;
					case 1: bonus = -30;break;
					case 2: bonus = 0;break;
					case 3: bonus = 0;break;
					case 4: bonus = 0;break;
					case 5: bonus = 0;break;
					case 6: bonus = -30;break;
					case 7: bonus = -30;break;
					}; break;
			case 7: switch(j) {
					case 0: bonus = -50;break;
					case 1: bonus = -30;break;
					case 2: bonus = -30;break;
					case 3: bonus = -30;break;
					case 4: bonus = -30;break;
					case 5: bonus = -30;break;
					case 6: bonus = -30;break;
					case 7: bonus = -50;break;
					}; break;
			}
			
		}
		return bonus;
			
	}
	
	public static int nb_white_queens(Board board) {
		int nb_white_queen = 0;
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board.board[k][l].piece != null && board.board[k][l].piece instanceof Queen && board.board[k][l].piece.getColor() == "White") { // c'est une dame blanche
					nb_white_queen++;
				}
			}
		}
		return nb_white_queen;
	}
	
	public static int nb_black_queens(Board board) {
		int nb_black_queen = 0;
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board.board[k][l].piece != null && board.board[k][l].piece instanceof Queen && board.board[k][l].piece.getColor() == "Black") { // c'est une dame blanche
					nb_black_queen++;
				}
			}
		}
		return nb_black_queen;
	}
	
	public static boolean isEndGameStage(Board board) {
		return nb_white_queens(board) == 0 && nb_black_queens(board) == 0; //on est dans une end game s'il n'y a plus de dame sur l'echiquier
	}
	
	public static int bonusSquare(int i, int j, Board board) { // on ajoute a chaque piece de l'echiquier son bonus d'evaluation par rapport a la case sur laquelle elle se trouve
		int score = 0;
		if (board.board[i][j].piece != null && board.board[i][j].piece.getColor() == "White") {
			if (isEndGameStage(board)) {
				score += bonusSquareKingEndGame(i, j, board);
			} else {
				score += bonusSquareKingMiddleGame(i, j, board);
			}
			score += bonusSquarePawn(i, j, board) + bonusSquareKnight(i, j, board) + bonusSquareBishop(i, j, board) 
			+ bonusSquareRook(i, j, board) + bonusSquareQueen(i, j, board);
			return score;
			
		} else if (board.board[i][j].piece != null && board.board[i][j].piece.getColor() == "Black") { // on renverse la matrice et on multiplie par -1 les bonus obtenus
			if (isEndGameStage(board)) {
				score -= bonusSquareKingEndGame(7-i, 7-j, board);
			} else {
				score -= bonusSquareKingMiddleGame(7-i, 7-j, board);
			}
			score += (-1)*(bonusSquarePawn(7-i, 7-j, board) + bonusSquareKnight(7-i, 7-j, board) + bonusSquareBishop(7-i, 7-j, board) 
			+ bonusSquareRook(7-i, 7-j, board) + bonusSquareQueen(7-i, 7-j, board) + bonusSquareKingMiddleGame(7-i, 7-j, board));
			return score;
		} else {
			return 0;
		}
		
		
	}
	
	public static double positionEvaluation(Board board) { // on renvoie l'evaluation reelle de la position, qui reprend les methodes rawEvaluation et heuristicEvaluation
		int evalSpeciale = board.checkmatedKing();
		if (evalSpeciale == 2) { // partie nulle 
			return 0;
		} else if (evalSpeciale == 1) { // echec et mat
			if (board.colorToPlay == "White") { // alors c'est les blancs qui sont en mat, donc les noirs ont gagne
				return Double.NEGATIVE_INFINITY;
			} else {
				return Double.POSITIVE_INFINITY;
			}
		} else { 
			return rawEvaluation(board) + heuristicEvaluation(board);
		}
		
	}
}

