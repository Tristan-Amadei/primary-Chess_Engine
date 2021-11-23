package chess;

import java.util.Random;

import AI.Coup;
import AI.Move;

// cette classe sert surtout a tester les fonctions mises en place, le vrai main sera celui de gui

public class Main {

	public static void main(String[] args) throws DeplacementNonValableException, CheckmateException {
		long t0 = System.currentTimeMillis();
		
		Board board = new Board();
		board.newGameBoard();
		
		board.board[6][4].piece.deplacement(4, 4, board);
		board.board[1][4].piece.deplacement(3, 4, board);
		Board m = new Board();
		m = board.deepCopy();
		m.unmakeMove();
		
		board.board[6][3].piece.deplacement(4, 3, board);
		
		
		//pew pew
	
		
		
		System.out.println("done");
		board.display();
		long t1 = System.currentTimeMillis();
		System.out.println();
		System.out.println("time : " + (t1-t0)/1000);
		
	}
	

}
