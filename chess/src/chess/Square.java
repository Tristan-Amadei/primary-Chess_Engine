package chess;

public class Square {
	
	public int ligne;
	public int colonne;
	public Piece piece;
	
	public Square(int i, int j, Piece piece){
		this.ligne=i;
		this.colonne=j;
		this.piece=piece;
	}
	
	void addPiece(Piece piece) {
		this.piece=piece;
		piece.ligne = this.ligne;
		piece.colonne=this.colonne;
	}
	
	void removePiece() {
		this.piece=null;
	}
	
	public void display() {
		if (piece != null) {
			piece.display();
		} else {
			System.out.print("****");
		}
	}
}
