package bishopMove;

//import static org.junit.jupiter.api.Assertions.*;

//import org.junit.jupiter.api.Test;

import chess.Board;
import chess.Pawn;
import chess.Square;
import chess.Rook;
import chess.Bishop;
import chess.Queen;
import chess.King;
import chess.Knight;
import chess.Piece;





class BishopMoveTest {

	//@Test
	void test() {
		
		
		Board echiquier = new Board();
		Board echiquier2 = new Board();
		//		https://www.google.com/search?q=fried+liver+chess&tbm=isch&ved=2ahUKEwiu1K_B8NfwAhXF9OAKHTmcD6gQ2-cCegQIABAA&oq=fried+liver+chess&gs_lcp=CgNpbWcQAzICCAAyBAgAEB4yBAgAEB4yBAgAEB4yBggAEAgQHjIGCAAQCBAeMgYIABAIEB46BAgAEENQ2gdYxRJgkRNoAHAAeACAAUeIAcsCkgEBNpgBAKABAaoBC2d3cy13aXotaW1nwAEB&sclient=img&ei=xyKmYO7UC8Xpgwe5uL7ACg&bih=525&biw=1163#imgrc=lTipZO5grQ1-MM
		
		echiquier.board[0][0].piece = new Rook("Black", 0, 0);
		echiquier.board[0][2].piece = new Bishop("Black", 0, 2);
		echiquier.board[0][3].piece = new Queen("Black", 0, 3);
		echiquier.board[0][5].piece = new Bishop("Black", 0, 5);
		echiquier.board[0][7].piece = new Rook("Black", 0, 7);
		
		echiquier.board[1][0].piece = new Pawn("Black", 1, 0);
		echiquier.board[1][1].piece = new Pawn("Black", 1, 1);
		echiquier.board[1][2].piece = new Pawn("Black", 1, 2);
		echiquier.board[1][6].piece = new Pawn("Black", 1, 6);
		echiquier.board[1][7].piece = new Pawn("Black", 1, 7);
		
		echiquier.board[2][2].piece = new Knight("Black", 2, 2);
		echiquier.board[2][4].piece = new King("Black", 2, 4);
		
		echiquier.board[3][3].piece = new Knight("Black", 3, 3);
		echiquier.board[3][4].piece = new Pawn("Black", 3, 4);
		
		echiquier.board[4][2].piece = new Bishop("White", 4, 2);
		
		echiquier.board[5][2].piece = new Knight("White", 5, 2);
		echiquier.board[5][5].piece = new Queen("White", 5, 5);
		
		echiquier.board[6][0].piece = new Pawn("White", 6, 0);
		echiquier.board[6][1].piece = new Pawn("White", 6, 1);
		echiquier.board[6][2].piece = new Pawn("White", 6, 2);
		echiquier.board[6][3].piece = new Pawn("White", 6, 3);
		echiquier.board[6][5].piece = new Pawn("White", 6, 5);
		echiquier.board[6][6].piece = new Pawn("White", 6, 6);
		echiquier.board[6][7].piece = new Pawn("White", 6, 7);
		
		echiquier.board[7][0].piece = new Rook("White", 7, 0);
		echiquier.board[7][2].piece = new Bishop("White", 7, 2);
		echiquier.board[7][4].piece = new King("White", 7, 4);
		echiquier.board[7][7].piece = new Rook("White", 7, 7);
		
		
//		echiquier.display();
		
		
		
//coups possibles B Knight 2,2
		
		// cest aux noirs de jouer (utilisé dans .CoupsLegaux())
		
		echiquier.colorToPlay="Black";
		
		// on crée une liste de type Square[] qui donne les coups legaux de la piece choisie
		//(ici le Black Knight en 2,2)
		
		Square[] liste = echiquier.board[2][2].piece.CoupsLegaux(echiquier);
		
		// ici on implemente une 2e liste qui contient les coups theoriquement legaux (observes par un joueur)
		
		Square[] Pliste = new Square[5];
		
		
		
		Pliste[0] = new Square (0,1, null); 		//format : Square(i,j,piece)
		Pliste[1] = new Square (1,4, null);
		Pliste[2] = new Square (4,3, null);
		Pliste[3] = new Square (4,1, null);
		Pliste[4] = new Square (3,0, null);
		
		
	
//coups possibles W Bishop 4,2
		echiquier.colorToPlay="White";
//		Square[] liste = echiquier.board[4][2].piece.CoupsLegaux(echiquier);
		
		
	
		
//coups possibles W Queen 5,5
		

//coups possibles W Pawn 6,0
		
		
	
		
		
		
		
		
		
		
		
		
		
		
		
		

		
//on compare ici les deux listes pour voir si elles sont equivalentes grace a la fct de junit "assertEquals"
		
		/*for (int var=0; var<liste.length; var++) {
			assertEquals(liste[var].i , Pliste[var].i);
			assertEquals(liste[var].j , Pliste[var].j);
			assertEquals(liste[var].piece , Pliste[var].piece); 
		
		} */

			
			
		}
		
		
		
	}


