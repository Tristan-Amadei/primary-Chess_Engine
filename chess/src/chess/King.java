package chess;

public class King extends Piece {
	public boolean hasAlreadyMoved; // faux tant que le roi n'a pas bouge, sera utilise pour permettre ou non le roque
	
	public King(String color, int x, int y) {
		super(color, x, y);
		hasAlreadyMoved=false;
		name="King";
	}
	
	public Square[] coupsPossibles(Board board) {
		Square[] liste_coups = new Square[8];
		int nb_coups=0;
		
		/* on ne s'interesse pas ici au fait de savoir si les coups indiquÃ©s sont jouables ou non (dans le sens oÃ¹ si 
		 * le roi est en Ã©chec ou non) , on regardera ca plus en dÃ©tail au moment de rÃ©aliser le coup */
		
		int[][] positions_atteignables = {{ligne-1, colonne-1}, {ligne-1, colonne}, {ligne-1, colonne+1}, {ligne, colonne+1}, {ligne+1, colonne+1}, {ligne+1, colonne}, {ligne+1, colonne-1}, {ligne, colonne-1}};
		for (int k=0; k<positions_atteignables.length; k++) {

			int i_att = positions_atteignables[k][0];
			int j_att = positions_atteignables[k][1];
			
			if (i_att >=0 && i_att <8 && j_att >=0 && j_att <8) { // on vÃ©rifie que la case visÃ©e est sur l'Ã©chiquier, si oui on l'ajoute a la liste
				if (board.board[i_att][j_att].piece == null) {
					liste_coups[nb_coups] = board.board[i_att][j_att];
					nb_coups++;
				} else if (board.board[i_att][j_att].piece.color != color) {
					liste_coups[nb_coups] = board.board[i_att][j_att];
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
	

	
	public boolean isInCheck(Board board) { // on regarde si la case du roi se trouve dans les coups possibles de l'adversaire pour savoir si le roi est en Ã©chec ou pas
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board.board[k][l].piece != null && board.board[k][l].piece.color != color) { //piece adverse sur cette case
					Square[] liste_coups_adv = board.board[k][l].piece.coupsPossibles(board);
					for (Square coups_adv:liste_coups_adv) {
						if (coups_adv.ligne==ligne && coups_adv.colonne==colonne) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public Board petitRoque(Board board) throws DeplacementNonValableException { // regarde si le petit roque est possible, et le joue si oui
		
		if (color != board.colorToPlay) {
			throw new DeplacementNonValableException();
		} else {
			
			
			if (hasAlreadyMoved) {
				throw new DeplacementNonValableException();
			}
			if (this.isInCheck(board)) {
				throw new DeplacementNonValableException();
			}
			if (color == "White") {
				if (!(board.board[7][5].piece == null && board.board[7][6].piece == null)) { //il y a au moins une piece entre la tour et le roi
					throw new DeplacementNonValableException();
				}
				if (board.board[7][7].piece instanceof Rook) {
					Rook rook = (Rook) board.board[7][7].piece;
					boolean caseslibres = true;
					Square[] casesControllees = board.casesControllees("Black");
					for (Square cases : casesControllees) {
						if ((cases.ligne == 7 && cases.colonne == 5) || (cases.ligne == 7 && cases.colonne == 6)) {
							caseslibres = false;
						}
					}
				
					
					if (rook.color == "White" && rook.hasAlreadyMoved==false && !(this.isInCheck(board)) && caseslibres) { //on peut alors roquer petit cote
						Board former_board = board.deepCopy();
						board.previous_position = former_board;
						board.board[7][4].piece = null;
						board.board[7][6].piece=this;
						colonne=6;
						hasAlreadyMoved=true;
						
						board.board[7][5].piece=rook;
						rook.colonne=5;
						rook.hasAlreadyMoved=true;
						board.board[7][7].piece=null;
						board.colorPlayed();
						return board;
					} else {
						throw new DeplacementNonValableException();
					}
				} else {
					throw new DeplacementNonValableException();
				}
			} else {
				if (!(board.board[0][5].piece == null && board.board[0][6].piece == null)) { //il y a au moins une piece entre la tour et le roi
					throw new DeplacementNonValableException();
				}
				if (board.board[0][7].piece instanceof Rook) {
					Rook rook = (Rook) board.board[0][7].piece;
					boolean caseslibres = true;
					Square[] casesControllees = board.casesControllees("White");
					for (Square cases : casesControllees) {
						if ((cases.ligne == 0 && cases.colonne == 5) || (cases.ligne == 0 && cases.colonne == 6)) {
							caseslibres = false;
						}
					}
					
					
					
					if (rook.color == "Black" && rook.hasAlreadyMoved==false && !(this.isInCheck(board)) && caseslibres) { //on peut alors roquer petit cote
						Board former_board = board.deepCopy();
						board.previous_position = former_board;
						board.board[0][4].piece = null;
						board.board[0][6].piece=this;
						colonne=6;
						hasAlreadyMoved=true;
						
						board.board[0][5].piece=rook;
						rook.colonne=5;
						rook.hasAlreadyMoved=true;
						board.board[0][7].piece=null;
						board.colorPlayed();
						return board;
					} else {
						throw new DeplacementNonValableException();
					}
				}else {
					throw new DeplacementNonValableException();
					}
				}
			}
			
		}
		
		
	
	public Board grandRoque(Board board) throws DeplacementNonValableException { // meme mÃ©thode que petitRoque, mais joue le grand roque
		if (color != board.colorToPlay) {
			throw new DeplacementNonValableException();
		} else {
			

			if (hasAlreadyMoved) {
				throw new DeplacementNonValableException();
			}
			if (this.isInCheck(board)) {
				throw new DeplacementNonValableException();
			}
			if (color == "White") {
				if (!(board.board[7][3].piece == null && board.board[7][2].piece == null && board.board[7][1].piece == null)) { //il y a au moins une piece entre la tour et le roi
					throw new DeplacementNonValableException();
				}
				if (board.board[7][0].piece instanceof Rook) {
					Rook rook = (Rook) board.board[7][0].piece;
					boolean caseslibres = true;
					Square[] casesControllees = board.casesControllees("Black");
					for (Square cases : casesControllees) {
						if ((cases.ligne == 7 && cases.colonne == 2) || (cases.ligne == 7 && cases.colonne == 3)) {
							caseslibres = false;
						}
					}
					
					
					
					
					
					if (rook.color == "White" && rook.hasAlreadyMoved==false && !(this.isInCheck(board)) && caseslibres) { //on peut alors roquer petit cote
						Board former_board = board.deepCopy();
						board.previous_position = former_board;
						board.board[7][4].piece = null;
						board.board[7][2].piece=this;
						colonne=2;
						hasAlreadyMoved=true;
						
						board.board[7][3].piece=rook;
						rook.colonne=3;
						rook.hasAlreadyMoved=true;
						board.board[7][0].piece=null;
						board.colorPlayed();
						return board;
					} else {
						throw new DeplacementNonValableException();
					}
				} else {
					throw new DeplacementNonValableException();
				}
			} else {
				if (!(board.board[0][3].piece == null && board.board[0][2].piece == null && board.board[0][1].piece == null)) { //il y a au moins une piece entre la tour et le roi
					throw new DeplacementNonValableException();
				}
				if (board.board[0][0].piece instanceof Rook) {
					Rook rook = (Rook) board.board[0][0].piece;
					boolean caseslibres = true;
					Square[] casesControllees = board.casesControllees("White");
					for (Square cases : casesControllees) {
						if ((cases.ligne == 0 && cases.colonne == 2) || (cases.ligne == 0 && cases.colonne == 3)) {
							caseslibres = false;
						}
					}
					
					
					
					
					if (rook.color == "Black" && rook.hasAlreadyMoved==false && !(this.isInCheck(board)) && caseslibres) { //on peut alors roquer petit cote
						Board former_board = board.deepCopy();
						board.previous_position = former_board;
						board.board[0][4].piece = null;
						board.board[0][2].piece=this;
						colonne=2;
						hasAlreadyMoved=true;
						
						board.board[0][3].piece=rook;
						rook.colonne=3;
						rook.hasAlreadyMoved=true;
						board.board[0][0].piece=null;
						board.colorPlayed();
						return board;
					} else {
						throw new DeplacementNonValableException();
					}
				}else {
					throw new DeplacementNonValableException();
					}
				}
			}
			
		}
	
	
	
	public boolean petitRoquePossible(Board board) { /* exactement la mÃªme fonction que petitRoque mais dans le cas ou le petit roque est possible, on renvoie true et false sinon.
		Le but est d'utiliser cette fonction pour donner la liste des coups possibles lors d'une position donnÃ©e */
		
		if (color != board.colorToPlay) {
			return false;
		} else {
			
			
			if (hasAlreadyMoved) {
				return false;
			}
			if (this.isInCheck(board)) {
				return false;
			}
			if (color == "White") {
				if (!(board.board[7][5].piece == null && board.board[7][6].piece == null)) { //il y a au moins une piece entre la tour et le roi
					return false;
				}
				if (board.board[7][7].piece instanceof Rook) {
					Rook rook = (Rook) board.board[7][7].piece;
					boolean caseslibres = true;
					Square[] casesControllees = board.casesControllees("Black");
					for (Square cases : casesControllees) {
						if ((cases.ligne == 7 && cases.colonne == 5) || (cases.ligne == 7 && cases.colonne == 6)) {
							caseslibres = false;
						}
					}
				
					
					if (rook.color == "White" && rook.hasAlreadyMoved==false && !(this.isInCheck(board)) && caseslibres) { //on peut alors roquer petit cote
						

						return true;
						
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (!(board.board[0][5].piece == null && board.board[0][6].piece == null)) { //il y a au moins une piece entre la tour et le roi
					return false;
				}
				if (board.board[0][7].piece instanceof Rook) {
					Rook rook = (Rook) board.board[0][7].piece;
					boolean caseslibres = true;
					Square[] casesControllees = board.casesControllees("White");
					for (Square cases : casesControllees) {
						if ((cases.ligne == 0 && cases.colonne == 5) || (cases.ligne == 0 && cases.colonne == 6)) {
							caseslibres = false;
						}
					}
					
					
					
					if (rook.color == "Black" && rook.hasAlreadyMoved==false && !(this.isInCheck(board)) && caseslibres) { //on peut alors roquer petit cote
						
						return true;
					} else {
						return false;
					}
				}else {
					return false;
					}
				}
			}
			
		}
	
	
	
	public boolean grandRoquePossible(Board board) { // meme chose que la methode petitRoquePossible
		if (color != board.colorToPlay) {
			return false;
		} else {
			

			if (hasAlreadyMoved) {
				return false;
			}
			if (this.isInCheck(board)) {
				return false;
			}
			if (color == "White") {
				if (!(board.board[7][3].piece == null && board.board[7][2].piece == null && board.board[7][1].piece == null)) { //il y a au moins une piece entre la tour et le roi
					return false;
				}
				if (board.board[7][0].piece instanceof Rook) {
					Rook rook = (Rook) board.board[7][0].piece;
					boolean caseslibres = true;
					Square[] casesControllees = board.casesControllees("Black");
					for (Square cases : casesControllees) {
						if ((cases.ligne == 7 && cases.colonne == 2) || (cases.ligne == 7 && cases.colonne == 3)) {
							caseslibres = false;
						}
					}
					
					
					
					
					
					if (rook.color == "White" && rook.hasAlreadyMoved==false && !(this.isInCheck(board)) && caseslibres) { //on peut alors roquer petit cote
						
						return true;
						
					} else {
						return false;
					}
				} else {
					return false;
				}
			} else {
				if (!(board.board[0][3].piece == null && board.board[0][2].piece == null && board.board[0][1].piece == null)) { //il y a au moins une piece entre la tour et le roi
					return false;
				}
				if (board.board[0][0].piece instanceof Rook) {
					Rook rook = (Rook) board.board[0][0].piece;
					boolean caseslibres = true;
					Square[] casesControllees = board.casesControllees("White");
					for (Square cases : casesControllees) {
						if ((cases.ligne == 0 && cases.colonne == 2) || (cases.ligne == 0 && cases.colonne == 3)) {
							caseslibres = false;
						}
					}
					
					
					
					
					if (rook.color == "Black" && rook.hasAlreadyMoved==false && !(this.isInCheck(board)) && caseslibres) { //on peut alors roquer petit cote
						
						return true;
						
					} else {
						return false;
					}
				}else {
					return false;
					}
				}
			}
			
		}
		
		
		
	
	
	
}
	


