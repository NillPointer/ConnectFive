package model;

import model.interfaces.IGame;

/**
 * Implementation of the IGame interface for a
 * 5-in-a-Row connect game.
 * 
 * @author Oleksandr Kononov
 *
 */
public class ConnectFiveGame implements IGame{
	
	/** Game related constants */
	public static final int PLAYERS_AMOUNT = 2;
	public static final int WIN_AMOUNT = 5;
	public static final char[] PLAYER_SYMBOLS = {'X', 'O'};
	
	/** The sizes of the board for the current game instance */
	private int boardColumns, boardRows;
	
	/** The logical game board of 2 dimensional character arrays */
	private char[][] board;
	
	/** Array of String player names */
	private String[] players;
	
	/** Current active player index */
	private int currentPlayer;
	
	/** Current game session is over */
	private boolean gameWonOver;
	
	/**
	 * Constructor for initialising all default values.
	 */
	public ConnectFiveGame(){
		this.board = new char[9][6];
		this.boardColumns = 9;
		this.boardRows = 6;
		this.players = new String[]{"Player1", "Player2"};
		this.currentPlayer = 0;
		this.gameWonOver = false;
		initBoard();
	}
	
	/**
	 * Constructor for specifying specific values for the game instance.
	 * @param columns
	 * @param rows
	 * @param players
	 * @throws Exception
	 */
	public ConnectFiveGame(int columns, int rows, String[] players) throws Exception{
		/* Safety checks */
		if (columns < 5 || rows < 5) throw new Exception("Board sizes are not sufficient");
		if (players == null || players.length < PLAYERS_AMOUNT) {
			this.players = new String[PLAYERS_AMOUNT];
			for (int i=0; i<PLAYERS_AMOUNT; ++i) this.players[i] = "Player"+String.valueOf(i+1);
		} else this.players = players.clone();
		
		this.board = new char[columns][rows];
		this.boardColumns = columns;
		this.boardRows = rows;
		this.currentPlayer = 0;
		this.gameWonOver = false;
		initBoard();
	}
	
	/**
	 * Initialise the game board and reset the current player index.
	 */
	@Override
	public void initBoard(){
		for (int i=0; i<boardColumns; ++i)
			for (int j=0; j<boardRows; ++j)
				board[i][j] = ' ';
		currentPlayer = 0;
		gameWonOver = false;
	}
	
	/**
	 * Checks if the current player has won the game with the last move.
	 * @param column of the last move
	 * @param row of the last move
	 * @return boolean if the game is won
	 */
	@Override
	public boolean isGameWon(int column, int row){
		gameWonOver = isGameWon(column, row, getCurrentPlayerIndex());
		return gameWonOver;
	}

	/**
	 * Checks if the player at playerIndex has won the game with the last move.
	 * @param column of the last move
	 * @param row of the last move
	 * @param playerIndex
	 * @return boolean if the game is won by player at playerIndex
	 */
	@Override
	public boolean isGameWon(int column, int row, int playerIndex) {
		/* Safety checks */
		if (column < 0 || column > boardColumns) throw new IndexOutOfBoundsException();
		if (row < 0 || row > boardRows) throw new IndexOutOfBoundsException();
		if (playerIndex < 0 || playerIndex >= PLAYERS_AMOUNT) throw new IndexOutOfBoundsException();
		
		final char symbol = PLAYER_SYMBOLS[playerIndex];
		gameWonOver =  checkVerticalWin(column, row, symbol) || 
						checkHorizontalWin(column, row, symbol) ||
						checkDiagonalWinForward(column, row, symbol) ||
						checkDiagonalWinBackward(column, row, symbol);
		return gameWonOver;
	}
	
	/**
	 * Checks the symbols in a vertical line above and below the given
	 * column and row coordinates.
	 * @param column
	 * @param row
	 * @param symbol of the player to check
	 * @return boolean if won
	 */
	private boolean checkVerticalWin(int column, int row, char symbol){
		int amount = 1;
		for(int i=row+1; i<boardRows; ++i) {
			if (board[column][i] != symbol) break;
			amount++;
		}
		if (amount >= WIN_AMOUNT) return true;
		
		for(int i=row-1; i>=0; --i) {
			if (board[column][i] != symbol) break;
			amount++;
		}
		if (amount >= WIN_AMOUNT) return true;
		
		return false;
	}
	
	/**
	 * Checks the symbols in a horizontal line above and below the given
	 * column and row coordinates.
	 * @param column
	 * @param row
	 * @param symbol of the player to check
	 * @return boolean if won
	 */
	private boolean checkHorizontalWin(int column, int row, char symbol){
		int amount = 1;
		for (int i=column+1; i<boardColumns; ++i){
			if (board[i][row] != symbol) break;
			amount++;
		}
		if (amount >= WIN_AMOUNT) return true;
		
		for (int i=column-1; i>=0; --i){
			if (board[i][row] != symbol) break;
			amount++;
		}
		if (amount >= WIN_AMOUNT) return true;
		
		return false;
	}
	
	/**
	 * Checks the symbols in a forward slash diagonal line before and after
	 * the given column and row coordinate.
	 * @param column
	 * @param row
	 * @param symbol
	 * @return boolean if won
	 */
	private boolean checkDiagonalWinForward(int column, int row, char symbol){
		int amount = 1;
		for (int c=column+1, r=row-1; c<boardColumns && r>=0; ++c, --r){
			if (board[c][r] != symbol) break;
			amount++;
		}
		if (amount >= WIN_AMOUNT) return true;
		
		for (int c=column-1, r=row+1; c>=0 && r<boardRows; --c, ++r){
			if (board[c][r] != symbol) break;
			amount++;
		}
		if (amount >= WIN_AMOUNT) return true;
		
		return false;
	}
	
	/**
	 * Checks the symbols in a backward slash diagonal line before and after
	 * the given column and row coordinate.
	 * @param column
	 * @param row
	 * @param symbol
	 * @return boolean if won
	 */
	private boolean checkDiagonalWinBackward(int column, int row, char symbol){
		int amount = 1;
		for (int c=column+1, r=row+1; c<boardColumns && r<boardRows; ++c, ++r){
			if (board[c][r] != symbol) break;
			amount++;
		}
		if (amount >= WIN_AMOUNT) return true;
		
		for (int c=column-1, r=row-1; c>=0 && r>=0; --c, --r){
			if (board[c][r] != symbol) break;
			amount++;
		}
		if (amount >= WIN_AMOUNT) return true;
		
		return false;
	}

	/**
	 * Checks if the game is over by having no more valid moves.
	 * @return boolean is there are no more moves to be made
	 */
	@Override
	public boolean isGameOver() {
		for (int col=0; col<boardColumns; ++col)
			if (board[col][0] == ' ') return false || gameWonOver;
		return true;
	}

	/**
	 * Attempts to make a move on the board on behalf of the current player.
	 * @param column of the board to use
	 * @return row that was used
	 */
	@Override
	public int doMove(int column) {
		final char symbol = getCurrentPlayerSymbol();
		if (column < 0 || column >= boardColumns) return -1;
		if (board[column][0] != ' ') return -1;
		for (int row=1; row<boardRows; ++row){
			if (board[column][row] == ' ') continue;
			board[column][row-1] = symbol;
			return row-1;
		}
		board[column][boardRows-1] = symbol;
		return boardRows-1;
	}

	/**
	 * @return Primitive String array of all player names
	 */
	@Override
	public String[] getPlayers() {
		return players;
	}
	
	/**
	 * Sets player name.
	 * @param playerIndex
	 * @param name
	 */
	@Override
	public void setPlayerName(int playerIndex, String name) throws IndexOutOfBoundsException{
		if (name == null) return;
		name = name.trim();
		if (name.isEmpty()) return;
		
		players[playerIndex] = name;
	}
	
	/**
	 * Set the current player to the next player.
	 */
	@Override
	public int endPlayerTurn(){
		currentPlayer = ++currentPlayer % players.length;
		return currentPlayer;
	}
	
	/**
	 * @param playerIndex
	 * @return String name of the player at playerIndex
	 */
	@Override
	public String getPlayer(int playerIndex) throws IndexOutOfBoundsException {
		return players[playerIndex];
	}
	
	/**
	 * @return String name of the current player
	 */
	@Override
	public String getCurrentPlayer(){
		return players[currentPlayer];
	}
	
	/**
	 * @return Integer index of the current player
	 */
	@Override
	public int getCurrentPlayerIndex(){
		return currentPlayer;
	}
	
	/**
	 * @return Character symbol for the current player
	 */
	@Override
	public char getCurrentPlayerSymbol(){
		return PLAYER_SYMBOLS[currentPlayer];
	}
	
	@Override
	public char getPlayerSymbol(int playerIndex){
		if (playerIndex < 0 || playerIndex > PLAYER_SYMBOLS.length) return ' ';
		return PLAYER_SYMBOLS[playerIndex];
	}

	/**
	 * @return 2 dimensional primitive array of characters the game board
	 */
	@Override
	public char[][] getBoard() {
		return board;
	}
	
	/**
	 * Overrides toString method in order to display the board when
	 * the instance of the class is printed to screen.
	 * @return Formatted String of the board and player symbols
	 */
	@Override
	public String toString(){
		StringBuilder boardString = new StringBuilder();
		for (int col=0; col<boardColumns; ++col)boardString.append(" "+(col+1)+" ");
		boardString.append('\n');
		for (int col=0; col<boardColumns; ++col)boardString.append("---");
		boardString.append('\n');
		for (int row=0; row<boardRows; ++row){
			for (int col=0; col<boardColumns; ++col){
				boardString.append("["+board[col][row]+"]");
			}
			boardString.append("\n");
		}
		return boardString.toString();
		
	}
}
