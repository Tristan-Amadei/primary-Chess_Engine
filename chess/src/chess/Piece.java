package chess;

import AI.Coup;

public abstract class Piece {
	String name;
	String color;
	public int ligne;
	public int colonne;
	public boolean wasLastClicked; //utilise pour l'interface graphique uniquement
	
	Piece(String color, int i, int j) {
		this.color=color;
		this.ligne=i;
		this.colonne=j;
	}
	
	public abstract Square[] coupsPossibles(Board board); // chaque piece devra mettre en place cette methode, car chaque piece se deplace differemment
	
	public String getName() {
		return name;
	}
	
	public String getColor() {
		return color;
	}
	
	public void display() { //permet d'afficher la piece dans la console
		String col;
		if (color == "White") {
			col="W";
		} else {
			col="B";
		}
		System.out.print(col + " " + name);
	}
	
	public Board deplacement (int ligne_case_deplacement, int colonne_case_deplacement, Board board) throws DeplacementNonValableException, CheckmateException {
		
		int coordonnee_ligne_initial = ligne;
		int coordonnee_colonne_initial = colonne;
		
		// on fait une deepcopy de la piece passee en parametre pour la mettre dans le coup qu'on mettra dans la liste de coups coupsJoues dans le champ de board
		Piece piece_init = Coup.deepCopy(coordonnee_ligne_initial, coordonnee_colonne_initial, this);
		
		
		  if (color == board.colorToPlay) { //c'est bien au tour de cette piece de se deplacer
			Board former_board = new Board();
			former_board = board.deepCopy(); // on stocke la position actuelle avant le mouvement, car on la mettra dans previous_position apres le mouvement
			board.previous_position = former_board;
			
			Board justToCheck_board = new Board();
			justToCheck_board = board.deepCopy(); 
			/* On cree un nouvel echiquier avec la meme position que l'echiquier passe en parametre. Le but est de jouer le coup passe en 
			 * parametre sur ce "faux" echiquier et regarder si le coup est legal, i.e si apres le coup le roi de la couleur qui vient de jouer n'est pas en echec, 
			 * car sinon le coup est illegal */
			
			Square[] liste_coups = this.coupsPossibles(board);
			boolean deplacementValable=false;
			int compt=0;
			while (! deplacementValable && compt<liste_coups.length) { // on regarde tout d'abord si le coup qu'on veut jouer est bien possible d'apres les methodes coupsPossibles
				deplacementValable = liste_coups[compt].ligne==ligne_case_deplacement && liste_coups[compt].colonne==colonne_case_deplacement;
				compt++;
			}
			if (compt == liste_coups.length && ! deplacementValable) { //la case ou on veut se deplacer n'est pas dans la liste des coups possibles
				throw new DeplacementNonValableException();
			} else {
				if (board.board[ligne_case_deplacement][colonne_case_deplacement].piece == null) { // on differencie si le coup est un simple deplacement ou une capture, juste pour le cas ou on capturerait un roi
					
					Piece piece;
					// on regarde si la piece qu'on veut deplacer est un pion qui va etre promu ou non, et on remplace la piece si oui. 
					if (name == "Pawn" && color == "White" && ligne == 1) {
						piece = new Queen(color, ligne, colonne);
					} else if (name == "Pawn" && color == "Black" && ligne == 6) {
						piece = new Queen(color, ligne, colonne);
					} else {
						piece = this;
					}
					
					
					if (this instanceof Pawn && colonne_case_deplacement != colonne) { //on est dans le cas d'une prise en passant
						justToCheck_board.board[ligne][colonne].piece = null; //deplacement fictif, on joue le coup sur un autre echiquier pour verifier que le roi n'est plus en echec une fois le coup effectue, donc que le coup est legal
						justToCheck_board.board[ligne_case_deplacement][colonne_case_deplacement].piece = piece;
						justToCheck_board.board[ligne][colonne_case_deplacement].piece = null;
						piece.ligne=ligne_case_deplacement;
						piece.colonne=colonne_case_deplacement;
						
					} else {
						
						justToCheck_board.board[ligne][colonne].piece = null; //deplacement fictif, on joue le coup sur un autre echiquier pour verifier que le roi n'est plus en echec une fois le coup effectue, donc que le coup est legal
						justToCheck_board.board[ligne_case_deplacement][colonne_case_deplacement].piece = piece;
						piece.ligne=ligne_case_deplacement;
						piece.colonne=colonne_case_deplacement;
					}
					
					
					
					boolean deplacementValable_check;
					
					// il faut maintenant verifier que le coup etait possible, ie que le roi de la couleur qui a joue n'est pas en echec
					Square King_position = justToCheck_board.getKingPosition(board.colorToPlay); // renvoie la case ou se trouve le roi de la couleur qui doit jouer
					if (((King) King_position.piece).isInCheck(justToCheck_board)) { // le roi de la couleur qui a joue est encore en echec donc le coup n'est pas legal
						deplacementValable_check=false;
					} else {
						deplacementValable_check=true;
					}
					
					
					piece.ligne = coordonnee_ligne_initial; // on remet les coordonnees de la piece selectionnee comme avant le deplacement, car on n'a fait qu'un deplacement fictif pour verifier que le coup etait jouable
					piece.colonne = coordonnee_colonne_initial;
					Piece capture_stored; // dans cette variable on stockera la piece qui a ete capturee lors du deplacement
					if (deplacementValable_check) { // on refait exactement la meme chose que sur l'echiquier justToCheck_board, mais pour de "vrai" cette fois-ci
						
						if (this instanceof Pawn && colonne_case_deplacement != piece.colonne) { //on est dans le cas d'une prise en passant
							board.board[piece.ligne][piece.colonne].piece = null;
							board.board[ligne_case_deplacement][colonne_case_deplacement].piece = piece ;
							board.board[piece.ligne][colonne_case_deplacement].piece = null;
							piece.ligne=ligne_case_deplacement;
							piece.colonne=colonne_case_deplacement;
							
							
							int i_en_passant;
							String color_pawn_taken;
							if (board.colorToPlay == "White") {
								i_en_passant=3;
							} else {
								i_en_passant=4;
							}
							if (board.colorToPlay == "White") {
								color_pawn_taken = "Black";
							} else {
								color_pawn_taken = "White";
							}
							Piece captured_piece = board.board[ligne][colonne_case_deplacement].piece; 
							capture_stored = new Pawn(color_pawn_taken, i_en_passant, colonne_case_deplacement);
							
							
							
						} else {
							
							board.board[piece.ligne][piece.colonne].piece = null;
							board.board[ligne_case_deplacement][colonne_case_deplacement].piece = piece ;
							piece.ligne=ligne_case_deplacement;
							piece.colonne=colonne_case_deplacement;
							capture_stored = null;
						}

						// on regarde si la piece qui vient de se deplacer etait une tour ou un roi, et on adapte leur champ hasAlreadyMoved en consequence
						if (piece instanceof Rook) {
							((Rook) piece).hasAlreadyMoved=true;
						}
						if (piece instanceof King) {
							((King) piece).hasAlreadyMoved=true;
						}
						board.colorPlayed(); // on change la couleur du joueur dont c'est le tour car on vient de jouer
						Coup coup_a_jouer = new Coup(piece_init, coordonnee_ligne_initial, coordonnee_colonne_initial, ligne_case_deplacement, colonne_case_deplacement, capture_stored);
						board.coupsJoues.add(board.nb_coups_joues, coup_a_jouer);
						board.nb_coups_joues++;
						return board;
					} else {
		
						throw new DeplacementNonValableException();
					}
					
				} else { // il y a une capture a effectuer
					if (board.board[ligne_case_deplacement][colonne_case_deplacement].piece.name == "King") { // on capture un roi alors c'est mat
						throw new CheckmateException();
					} else { // on refait la meme chose que precedemment si le deplacement n'etait pas une capture
						
						
						Piece captured_piece = board.board[ligne_case_deplacement][colonne_case_deplacement].piece; 
						Piece capture_stored = Coup.deepCopy(ligne_case_deplacement, colonne_case_deplacement, captured_piece);

						
						Piece piece;
						// on regarde si la piece qu'on veut deplacer est un pion qui va etre promu ou non
						if (name == "Pawn" && color == "White" && ligne == 1) {
							piece = new Queen(color, ligne, colonne);
						} else if (name == "Pawn" && color == "Black" && ligne == 6) {
							piece = new Queen(color, ligne, colonne);
						} else {
							piece = this;
						}

						justToCheck_board.board[ligne][colonne].piece = null; //deplacement fictif, on joue le coup sur un autre echiquier pour verifier que le roi n'est plus en echec une fois le coup effectue, donc que le coup est legal
						justToCheck_board.board[ligne_case_deplacement][colonne_case_deplacement].piece = piece;
						piece.ligne=ligne_case_deplacement;
						piece.colonne=colonne_case_deplacement;
						boolean deplacementValable_check;
						
						// il faut maintenant verifier que le coup etait possible, ie que le roi de la couleur qui a joue n'est plus en echec
						Square King_position = justToCheck_board.getKingPosition(board.colorToPlay);
						if (((King) King_position.piece).isInCheck(justToCheck_board)) { // le roi de la couleur qui a joue est encore en echec donc le coup n'est pas legal
							deplacementValable_check=false;
						} else {
							deplacementValable_check=true;
						}
						
						
						piece.ligne = coordonnee_ligne_initial; // on remet les coordonnees de la piece selectionnee comme avant le deplacement, car on n'a fait qu'un deplacement fictif pour verifier que le coup etait jouable
						piece.colonne = coordonnee_colonne_initial;
						
						if (deplacementValable_check) {
							board.board[piece.ligne][piece.colonne].piece = null;
							board.board[ligne_case_deplacement][colonne_case_deplacement].piece = piece ;
							piece.ligne=ligne_case_deplacement;
							piece.colonne=colonne_case_deplacement;
							if (piece instanceof Rook) {
								((Rook) piece).hasAlreadyMoved=true;
							}
							if (piece instanceof King) {
								((King) piece).hasAlreadyMoved=true;
							}
							board.colorPlayed();
							
							
							Coup coup_a_jouer = new Coup(piece_init, coordonnee_ligne_initial, coordonnee_colonne_initial, ligne_case_deplacement, colonne_case_deplacement, capture_stored);
							board.coupsJoues.add(board.nb_coups_joues, coup_a_jouer);
							board.nb_coups_joues++;
							return board;
						} else {
							
							throw new DeplacementNonValableException();
						}
				
					}
				}
			}
		} else { // si la piece qu'on veut deplacer n'est pas de la couleur qui doit jouer
			throw new DeplacementNonValableException();
		}
	   }
	
	public Square coupLegal (int k, int l, Board board) { 
		/*exactement comme la methode deplacement mais on renvoie true si on peut deplacer la piece au lieu d'effectuer le deplacement.
		 * Le but est d'utiliser cette fonction pour donner la liste des coups possibles lors d'une position donnee */
		
		
		int i_initial = ligne;
		int j_initial = colonne;
		  if (color == board.colorToPlay) { //c'est bien au tour de cette piece de se dÃ©placer
			Board former_board = board.deepCopy();
			board.previous_position = former_board;
			
			Board justToCheck_board = board.deepCopy();
			Square[] liste_coups = this.coupsPossibles(board);
			boolean deplacementValable=false;
			int compt=0;
			while (! deplacementValable && compt<liste_coups.length) {
				deplacementValable = liste_coups[compt].ligne==k && liste_coups[compt].colonne==l;
				compt++;
			}
			if (compt == liste_coups.length && ! deplacementValable) { //la case ou on veut se deplacer n'est pas dans la liste des coups possibles
				return null;
			} else {
				if (board.board[k][l].piece == null) {
					
					Piece piece;
					// on regarde si la piece qu'on veut deplacer est un pion qui va etre promu ou non
					if (name == "Pawn" && color == "White" && ligne == 1) {
						piece = new Queen(color, ligne, colonne);
					} else if (name == "Pawn" && color == "Black" && ligne == 6) {
						piece = new Queen(color, ligne, colonne);
					} else {
						piece = this;
					}
					

					
					
					justToCheck_board.board[ligne][colonne].piece = null; //deplacement fictif, on joue le coup sur un autre Ã©chiquier pour vÃ©rifier que le roi n'est plus en Ã©chec une fois le coup effectuÃ©, donc que le coup est legal
					justToCheck_board.board[k][l].piece = piece;
					piece.ligne=k;
					piece.colonne=l;
					boolean deplacementValable_check;
					
					// il faut maintenant vÃ©rifier que le coup Ã©tait possible, ie que le roi de la couleur qui a jouÃ© n'est plus en Ã©chec
					Square King_position = justToCheck_board.getKingPosition(board.colorToPlay);
					if (((King) King_position.piece).isInCheck(justToCheck_board)) { // le roi de la couleur qui a jouÃ© est encore en Ã©chec donc le coup n'est pas lÃ©gal
						deplacementValable_check=false;
					} else {
						deplacementValable_check=true;
					}
					
					
					piece.ligne = i_initial; // on remet les coordonnÃ©es de la piece selectionnÃ©e comme avant le deplacement, car on a faire qu'un deplacement fictif pour vÃ©rifier que le coup Ã©tait jouable
					piece.colonne = j_initial;
					
					if (deplacementValable_check) {
						return board.board[k][l];
					} else {
		
						return null;
					}
					
				} else { // il y a une capture Ã  effectuer
					if (board.board[k][l].piece.name == "King") {
						return null;
					} else { 
						
						Piece piece;
						// on regarde si la piece qu'on veut deplacer est un pion qui va etre promu ou non
						if (name == "Pawn" && color == "White" && ligne == 1) {
							piece = new Queen(color, ligne, colonne);
						} else if (name == "Pawn" && color == "Black" && ligne == 6) {
							piece = new Queen(color, ligne, colonne);
						} else {
							piece = this;
						}
						

						
						
						justToCheck_board.board[ligne][colonne].piece = null; //deplacement fictif, on joue le coup sur un autre Ã©chiquier pour vÃ©rifier que le roi n'est plus en Ã©chec une fois le coup effectuÃ©, donc que le coup est legal
						justToCheck_board.board[k][l].piece = piece;
						piece.ligne=k;
						piece.colonne=l;
						boolean deplacementValable_check;
						
						// il faut maintenant vÃ©rifier que le coup Ã©tait possible, ie que le roi de la couleur qui a jouÃ© n'est plus en Ã©chec
						Square King_position = justToCheck_board.getKingPosition(board.colorToPlay);
						if (((King) King_position.piece).isInCheck(justToCheck_board)) { // le roi de la couleur qui a jouÃ© est encore en Ã©chec donc le coup n'est pas lÃ©gal
							deplacementValable_check=false;
						} else {
							deplacementValable_check=true;
						}
						
						
						piece.ligne = i_initial; // on remet les coordonnees de la piece selectionnee comme avant le deplacement, car on n'a fait qu'un deplacement fictif pour verifier que le coup etait jouable
						piece.colonne = j_initial;
						
						if (deplacementValable_check) {
							return board.board[k][l];
						} else {
							
							return null;
						}
				
					}
				}
			}
		} else {
			return null;
		}
	   }
	
	public Square[] CoupsLegaux(Board board) { // on retourne la liste de tous les coups possibles et legaux de la piece en parametre
		Square[] coupsLegaux = new Square[0];
		
		Square[] coups = this.coupsPossibles(board);
		for (Square coup:coups) {
			if (this.coupLegal(coup.ligne, coup.colonne, board) != null) {
				Square[] liste_coup = {this.coupLegal(coup.ligne, coup.colonne, board)};
				coupsLegaux = Board.fusion(coupsLegaux, liste_coup);
			}
		}
		return coupsLegaux;
	}
	
	public int nbCoupsLegaux(Board board) { // on compte le nombre de coups legaux qu'une piece peut faire, ce qui revient a compter la taille de la liste CoupsLegaux + le nombre de roque possible s'il s'agit d'un roi
		if (this instanceof King) {
			int nb_roque = 0;
			if (((King) this).petitRoquePossible(board)) {
				nb_roque++;
			}
			if (((King) this).grandRoquePossible(board)) {
				nb_roque++;
			}
			return this.CoupsLegaux(board).length + nb_roque;
			
		} else {
			return this.CoupsLegaux(board).length;
		}
	}
}
