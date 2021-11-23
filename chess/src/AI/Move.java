package AI;
import chess.*;

public class Move {

	public static double maxi(int depth, Board board) {
       if (depth == 0) {
           return Evaluation.positionEvaluation(board);
       }
       Double max = Double.NEGATIVE_INFINITY;
       Coup[] moves = Coup.coupsPosition(board);
       for (Coup move : moves) {
           try {
        	   Board mock = new Board();
        	   mock = board.deepCopy();
               move.playMove(mock);
              
               System.out.println();
               double score = mini(depth-1, mock);
               if (score >= max) {
                   max = score;
               }
           }catch (Exception e) {

           }
       }
       return max;
   }

   public static double mini(int depth, Board board) {
       if (depth == 0) {
           return Evaluation.positionEvaluation(board);
       }
       Double min = Double.POSITIVE_INFINITY;
       Coup[] moves = Coup.coupsPosition(board);
       for (Coup move : moves) {
           try {
               Board mock = new Board();
               mock = board.deepCopy();
               move.playMove(mock);
               
               System.out.println();
               double score = maxi(depth-1, mock);
               if (score <= min) {
                   min = score;
               }
           } catch (Exception e) {

           }
       }
       return min;
   }
   
   
   
   public static Coup meilleur_coup_selon_mini_maxi(int depth, Board board, String color) {
	   long t0 = System.currentTimeMillis();
       Coup[] moves = Coup.coupsPosition(board);
       Coup best_move = moves[0];
       double score_max;
       if (color == "White") {
           score_max = Double.NEGATIVE_INFINITY;
       } else {
           score_max = Double.POSITIVE_INFINITY;
       }
       for (Coup move : moves) {
           try {
        	   Board mock = new Board();
        	   mock = board.deepCopy();
               move.playMove(mock);
               if (color == "White") {
                   double score = mini(depth, mock);
                 
                   if (score >= score_max) {
                       score_max = score;
                       best_move = move;
                   }
               } else {
                   double score = maxi(depth, mock);
                  
                   if (score <= score_max) {
                       score_max = score;
                       best_move = move;
                   }
               }
           } catch (Exception e) {

           }
       }
       long t1 = System.currentTimeMillis();
		System.out.println();
		System.out.println("time : " + (t1-t0)/1000);
       return best_move;
   }
   
   public static double alphaBetaMax(int depth, double alpha, double beta, Board board) {
	   if (depth == 0) {
		   return Evaluation.positionEvaluation(board);
	   }
	   
	   Coup[] moves = Coup.coupsPosition(board);
	   for (Coup move : moves) {
		   Board mock_board = new Board();
		   mock_board = board.deepCopy();
		   try{
			   move.playMove(mock_board);
			   double score = alphaBetaMin(depth-1, alpha, beta, mock_board);
			   mock_board = null; //supprimer l'echiquier non utilisé
			   if (score >= beta) {
				   return beta;
			   } 
			   if (score > alpha) {
				   alpha = score;
			   }
		   } catch (Exception e) { 
			   
		   }
	   }
	   return alpha;
   }
   
   public static double alphaBetaMin(int depth, double alpha, double beta, Board board) {
	   if (depth == 0) {
		   return Evaluation.positionEvaluation(board);
	   }
	   
	   Coup[] moves = Coup.coupsPosition(board);
	   for (Coup move : moves) {
		   Board mock_board = new Board();
		   mock_board = board.deepCopy();
		   try {
			   move.playMove(mock_board);
			   
			   double score = alphaBetaMax(depth-1, alpha, beta, mock_board);
			  mock_board = null;
			  
			   if (score <= alpha) {
				   return alpha;
			   }
			   if (score < beta) {
				   beta = score;
			   }
		   } catch (Exception e) {
			   
		   }
	   }
	   return beta;
   }
   
   public static Coup best_move_alpha_beta(int depth, Board board, String color) {
	   long t0 = System.currentTimeMillis();
	   
       Coup[] moves = Coup.coupsPosition(board);
       Coup best_move = moves[0];
       double score_max;
       if (color == "White") {
           score_max = Double.NEGATIVE_INFINITY;
       } else {
           score_max = Double.POSITIVE_INFINITY;
       }
       for (Coup move : moves) {
           try {
        	   Board mock = new Board();
        	   mock = board.deepCopy();
               move.playMove(mock);
               if (color == "White") {
                   double score = alphaBetaMin(depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, mock);
                 
                   if (score >= score_max) {
                       score_max = score;
                       best_move = move;
                   }
               } else {
                   double score = alphaBetaMax(depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, mock);
                  
                   if (score <= score_max) {
                       score_max = score;
                       best_move = move;
                   }
               }
              mock = null;
           } catch (Exception e) {

           }
       }
       
       try { // petit roque
    	   
    	   Board mock = new Board();
    	   mock = board.deepCopy();
    	   Square king_position = mock.getKingPosition(color);
    	   ((King) mock.board[king_position.ligne][king_position.colonne].piece).petitRoque(mock);
    	   board.resetSquaresCoordinates();
    	   if (color == "White") {
               double score = alphaBetaMin(depth, -Double.POSITIVE_INFINITY, -Double.NEGATIVE_INFINITY, mock);
             
               if (score >= score_max) {
                   score_max = score;
                   Coup move = new Coup(board.board[king_position.ligne][king_position.colonne].piece, king_position.ligne, king_position.colonne, king_position.ligne, king_position.colonne+2, null);
                   best_move = move;
               }
           } else {
               double score = alphaBetaMax(depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, mock);
              
               if (score <= score_max) {
                   score_max = score;
                   Coup move = new Coup(board.board[king_position.ligne][king_position.colonne].piece, king_position.ligne, king_position.colonne, king_position.ligne, king_position.colonne+2, null);
                   best_move = move;
               }
           }
          mock = null;
       } catch (Exception e) {
    	   
       }
       
       try { // grand roque
    	   
    	   Board mock = new Board();
    	   mock = board.deepCopy();
    	   Square king_position = mock.getKingPosition(color);
    	   ((King) mock.board[king_position.ligne][king_position.colonne].piece).grandRoque(mock);
    	   board.resetSquaresCoordinates();
    	   
    	   
    	   if (color == "White") {
               double score = alphaBetaMin(depth, -Double.POSITIVE_INFINITY, -Double.NEGATIVE_INFINITY, mock);
             
               if (score >= score_max) {
                   score_max = score;
                   Coup move = new Coup(board.board[king_position.ligne][king_position.colonne].piece, king_position.ligne, king_position.colonne, king_position.ligne, king_position.colonne-2, null);
                   best_move = move;
               }
           } else {
               double score = alphaBetaMax(depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, mock);
              
               if (score <= score_max) {
                   score_max = score;
                   Coup move = new Coup(board.board[king_position.ligne][king_position.colonne].piece, king_position.ligne, king_position.colonne, king_position.ligne, king_position.colonne-2, null);
                   best_move = move;
               }
           }
          mock = null;
       } catch (Exception e) {
    	   
       }
       
       long t1 = System.currentTimeMillis();
		System.out.println();
		int real_depth=depth+1;
		System.out.println("time for depth " + real_depth +" : " + (t1-t0)/1000);
		
       return best_move;
   }
   
   public static int[] nb_pieces_echiquier(Board board) { // renvoie une map contenant le nombre de chaque piece sur l'echiquier
	   int nb_reines = 0;
	   int nb_tours = 0;
	   int nb_fous = 0;
	   int nb_cavaliers = 0;
	   int nb_pions = 0;
	   
	   for (int k=0; k<8; k++) {
		   for (int l=0; l<8; l++) {
			   if (board.board[k][l].piece != null) {
				   if (board.board[k][l].piece instanceof Queen) {
					   nb_reines++;
				   }
				   else if (board.board[k][l].piece instanceof Rook) {
					   nb_tours++;
				   }
				   else if (board.board[k][l].piece instanceof Bishop) {
					   nb_fous++;
				   } 
				   else if (board.board[k][l].piece instanceof Knight) {
					   nb_cavaliers++;
				   } else if (board.board[k][l].piece instanceof Pawn) {
					   nb_pions++;
				   }
			   }
		   }
	   }
	   int[] liste_des_nombres_de_pieces = {nb_reines, nb_tours, nb_fous, nb_cavaliers, nb_pions};
	   return liste_des_nombres_de_pieces;
	   
   }
   
   public static void playTheBestMove(Board board) { //on se sert de la fonction precedente pour adapter la profondeur au nombre de pieces qui restent
	   int[] liste_des_nombres_de_pieces = nb_pieces_echiquier(board);
	   int nb_reines = liste_des_nombres_de_pieces[0];
	   int nb_tours = liste_des_nombres_de_pieces[1];
	   int nb_fous = liste_des_nombres_de_pieces[2];
	   int nb_cavaliers = liste_des_nombres_de_pieces[3];
	   int nb_pions = liste_des_nombres_de_pieces[4];


	   if (nb_reines > 0 || (nb_tours + nb_fous + nb_cavaliers) > 6) { // il y a encore beaucoup de pieces sur l'echiquier donc on reste a une profondeur egale a 2 (a 3 en vrai mais on l'appelle comme dans dans la fonction best_move_alpha_beta)
		   Coup coup_a_jouer = best_move_alpha_beta(2, board, board.colorToPlay);
		   try {
			   coup_a_jouer.playMove(board);
		   } catch (Exception e) {
			   
		   }
	   } else if ((nb_reines + nb_tours + nb_fous + nb_cavaliers) > 2) { // il n'y a plus de reine sur l'echiquier mais il reste encore trop de pieces autres que les reines pour vraiment pousser la profondeur loin
		   Coup coup_a_jouer = best_move_alpha_beta(3, board, board.colorToPlay);
		   try {
			   coup_a_jouer.playMove(board);
		   } catch (Exception e) {
			   
		   }
	   } else if (nb_tours >=2) {
		   Coup coup_a_jouer = best_move_alpha_beta(3, board, board.colorToPlay);
		   try {
			   coup_a_jouer.playMove(board);
		   } catch (Exception e) {
			   
		   }
	   }
	   else  { // il n'y a plus que des pions, les rois et maximum deux pieces donc le nombre de possibilités de coup est dimininué, on peut donc calculer plus de variantes
		   Coup coup_a_jouer = best_move_alpha_beta(4, board, board.colorToPlay);
		   try {
			   coup_a_jouer.playMove(board);
		   } catch (Exception e) {
			   
		   }
	   }
	   
   }
   
  
   
  

    
    
    
    
    
    
    
    
    
}
