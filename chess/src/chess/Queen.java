package chess;

public class Queen extends Piece {
	
	public Queen(String color, int x, int y) {
		super(color, x, y);
		name="Queen";
	}

	public Square[] coupsPossibles(Board board) { //une reine se comporte comme une tour et un fou a la fois
		Rook rook = new Rook(color, ligne, colonne);
		Square[] coupsRook = rook.coupsPossibles(board);
		
		Bishop bishop = new Bishop(color, ligne, colonne);
		Square[] coupsBishop = bishop.coupsPossibles(board);
		
		return Board.fusion(coupsRook, coupsBishop);
	}
}
