package gui;


import java.awt.Toolkit;

import AI.*;
import chess.*;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// cette classe sert d'interface graphique, affiche l'echiquier et permet de jouer une partie, en utilisant les methodes des autres packages

public class Main extends Application {
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start (Stage stage) throws DeplacementNonValableException, CheckmateException {
		double screen_height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double screen_width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double size_to_keep_for_canvas = Math.min(screen_width, screen_height)*0.9; // cela permet d'adapter la taille de l'echiquier affiche a la taille de l'ecran sur lequel il est affiche
		double canvas_width = size_to_keep_for_canvas;
		double canvas_height = size_to_keep_for_canvas; // un echiquier est carre
		
		Group group = new Group();
		Scene scene = new Scene(group);
		stage.setScene(scene);
		Canvas canvas = new Canvas(canvas_width + canvas_width/4, canvas_height);
	    ObservableList<Node> liste = group.getChildren();
	    liste.add(canvas);
		stage.setResizable(false);
		GraphicsContext graphics = canvas.getGraphicsContext2D();
		
		Board board = new Board(); // on initialise l'echiquier et on dispose les pieces comme au debut d'une partie
		board.newGameBoard();
		
		scene.setOnKeyPressed(new javafx.event.EventHandler<KeyEvent>() {
			  public void handle(KeyEvent e) {
				  KeyCode code = e.getCode();
				  switch(code) {

				  case ENTER: Move.playTheBestMove(board);break;

				  case LEFT:board.unmakeMove();break;
				  
				  case DOWN: Coup c = Move.meilleur_coup_selon_mini_maxi(2, board, board.colorToPlay);
			        try {
			            c.playMove(board);
			        } catch (Exception exp) {

			        } break;
			        
				  default : break;

				  }
			  }
		}); 
		
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e)  {
				double x_Mouse = e.getX();
				double y_Mouse = e.getY();
				int j = (int)Math.floor(8*x_Mouse/canvas_width);
				int i = (int)Math.floor(8*y_Mouse/canvas_height);
				
				Piece piece = whoWasLastClickedOn(board);
				if (piece != null) {
					if (piece.getName() == "King") { // on regarde si on veut roquer
						if (j == piece.colonne+2) { //petit roque
							try {
								((chess.King) piece).petitRoque(board);
							} catch (DeplacementNonValableException exp) {
								resetClick(board);
							} finally {
								resetClick(board);
							}
						} else if (j == piece.colonne-2) { // grand roque
							try {
								((chess.King) piece).grandRoque(board);
							} catch (DeplacementNonValableException exp) {
								resetClick(board);
							} finally {
								resetClick(board);
							}
						}
					} 
					
					try {
						piece.deplacement(i, j, board); // on deplace la piece selectionnee si cela est possible
						resetClick(board);
					} catch (DeplacementNonValableException exc) {
						resetClick(board);
					} catch (CheckmateException exp) {
						System.out.println("Checkmate !");
					} finally {
						resetClick(board); // on "declique" toutes les pieces
					}
				} else {
					if (board.board[i][j].piece != null) {
						board.board[i][j].piece.wasLastClicked=true; // cette piece n'etait pas deja selectionnee, alors on la selectionne et on affiche les cases sur lesquelles elle peut se deplacer
					}
					
				}
				
			}
		});
		
		
		AnimationTimer timer = new AnimationTimer() {
			  public void handle(long arg0) {
				  Image board_displayed = new Image("Echiquier.png", canvas_width, canvas_height, false, false);
				  graphics.drawImage(board_displayed, 0, 0); // affiche l'image de l'echiquier
					
				  render(graphics, board, canvas_width, canvas_height);
				  Piece piece_clicked = whoWasLastClickedOn(board);
				  if (piece_clicked != null) {
					  Square[] liste_coups = piece_clicked.CoupsLegaux(board);
					  for (Square cur_square:liste_coups) {
						  render_cases_coups(cur_square.ligne, cur_square.colonne, canvas_width, canvas_height, graphics, board);
					  }
				  }
				  
				  Image fond_noir = new Image("Black window.jpg", canvas_width/4, canvas_height, false, false);
				  graphics.drawImage(fond_noir, canvas_width, 0); // affiche le fond noir a droite de l'echiquier, permettant d'afficher le score et la couleur du joueur dont c'est le tour
				  
				  render_check(graphics, board, canvas_width, canvas_height);
				  
				  graphics.setFont(Font.font("Helvetica", FontWeight.BOLD, Math.min(canvas_width, canvas_height)/16)); // afficher la couleur a jouer
					graphics.setFill(Color.BLACK);
					graphics.setStroke(Color.WHITE);
					graphics.setLineWidth(1);
					graphics.fillText(board.colorToPlay, canvas_width + canvas_width/40, canvas_height/2+canvas_height/8);
					graphics.strokeText(board.colorToPlay, canvas_width + canvas_width/40, canvas_height/2 + canvas_height/8);
					
					
				  int stateOfTheGame = board.checkmatedKing(); // permet d'afficher si la partie est encore en cours, ou si c'est une nulle ou un echec et mat
				  if (stateOfTheGame == 1) {
					  graphics.setFont(Font.font("Helvetica", FontWeight.BOLD, Math.min(canvas_width, canvas_height)/16));
						graphics.setFill(Color.BLACK);
						graphics.setStroke(Color.WHITE);
						graphics.setLineWidth(1);
						graphics.fillText("Mate", canvas_width+canvas_width/40, canvas_height/2);
						graphics.strokeText("Mate", canvas_width+canvas_width/40, canvas_height/2);
						
				  }
				  if (stateOfTheGame == 2) {
					  graphics.setFont(Font.font("Helvetica", FontWeight.BOLD, Math.min(canvas_width, canvas_height)/32));
						graphics.setFill(Color.BLACK);
						graphics.setStroke(Color.WHITE);
						graphics.setLineWidth(1);
						graphics.fillText("Stalemate", canvas_width+canvas_width/50, canvas_height/2);
						graphics.strokeText("Stalemate", canvas_width+canvas_width/50, canvas_height/2);
						
				  }
				  if (stateOfTheGame == 0) {
					// on affiche l'evaluation de la position
						double score = AI.Evaluation.positionEvaluation(board)/100; // habituellement, l'evaluation est donnee sous cette forme, divisee par 100 car cela facilite la comprehension pour des humains
						String string_score = ""+score; //transforme score en string
						
						graphics.setFont(Font.font("Helvetica", FontWeight.BOLD, Math.min(canvas_width, canvas_height)/16));
						graphics.setFill(Color.BLACK);
						graphics.setStroke(Color.WHITE);
						graphics.setLineWidth(1);
						graphics.fillText(string_score, canvas_width+canvas_width/40, canvas_height/2);
						graphics.strokeText(string_score, canvas_width+canvas_width/40, canvas_height/2);
				  }
					
					
			  }
		};
		timer.start();
		stage.setTitle("Chess Board");
	    stage.show();
	}
	
	public static Piece whoWasLastClickedOn(Board board) { /* on regarde si on clique sur une piece ou sur une case vide. 
		Le but est de mettre en place le systeme de deplacement des pieces : on clique d'abord sur la piece qu'on veut deplacer
		puis on clique sur la case sur laquelle on veut deplacer la piece */
		
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board.board[k][l].piece != null && board.board[k][l].piece.wasLastClicked) {
					return board.board[k][l].piece;
				}
			}
		}
		return null;
	}
	
	public static void resetClick(Board board) { // on declique toutes les pieces
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board.board[k][l].piece != null) {
					board.board[k][l].piece.wasLastClicked=false;
					}
				}
			}
		}
	
	public static void render_cases_coups(int i, int j, double canvas_w, double canvas_h, GraphicsContext gc, Board board) { // methode pour afficher les coups possibles pour chaque piece
		if (board.board[i][j].piece == null) {
			Image image = new Image("Point gris.png", canvas_w/30, canvas_h/30, false, false);
			gc.drawImage(image, canvas_w/8*j + canvas_w/21, canvas_h/8*i + canvas_h/21);
		} else {
			Image image = new Image("Cercle.png", canvas_w/8, canvas_h/8, false, false);
			gc.drawImage(image, canvas_w/8*j , canvas_h/8*i);
		}
	}
	
	public static void render (GraphicsContext gc, Board board, double canvas_width, double canvas_height) { // methode pour afficher l'echiquier et ses pieces
		for (int k=0; k<8; k++) {
			for (int l=0; l<8; l++) {
				if (board.board[k][l].piece != null) {
					Image image = new Image(board.board[k][l].piece.getColor() + " " + board.board[k][l].piece.getName() + ".png", 
							canvas_width/8, canvas_height/8, false, false);
					gc.drawImage(image, canvas_width/8*board.board[k][l].piece.colonne , canvas_height/8*board.board[k][l].piece.ligne);
				}
			}
		}
	}
	
	public static void render_check(GraphicsContext gc, Board board, double canvas_width, double canvas_height) { // permet d'entourer le roi en echec si c'est le cas, pour attirer l'attention du joueur sur le fait que le roi est en echec, donc certains coups ne seront pas autorises
		King white_king = (King) (board.getKingPosition("White").piece); // 
		King black_king = (King) (board.getKingPosition("Black").piece);
		
		Image image = new Image("cercle rouge.png", canvas_width/8, canvas_height/8, false, false);
		if (white_king.isInCheck(board)) {
			gc.drawImage(image, canvas_width/8*white_king.colonne, canvas_height/8*white_king.ligne);
		}
		if (black_king.isInCheck(board)) {
			gc.drawImage(image, canvas_width/8*black_king.colonne, canvas_height/8*black_king.ligne);
		}
		
	}
}
