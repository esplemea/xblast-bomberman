package ch.epfl.xblast.server;

import java.util.Arrays;

import ch.epfl.xblast.Cell;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * @author Alexandre MICHEL
 * @date 28 févr. 2016
 *
 * @version 1.0
 */
public final class BoardDrawer extends Application{

	private final static int BLOCK_WIDTH = 64;
	private final static int BLOCK_HEIGHT = 48;

	private final static Image[] BLOCKS_IMAGES = initImages();
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("XBlast Debug Board Drawer");

		Group rootGroup = new Group();
		Canvas canvas = new Canvas(Cell.COLUMNS * BLOCK_WIDTH, Cell.ROWS * BLOCK_HEIGHT);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		Block __ = Block.FREE;
		Block XX = Block.INDESTRUCTIBLE_WALL;
		Block xx = Block.DESTRUCTIBLE_WALL;
		Board board = Board.ofQuadrantNWBlocksWalled(
		  Arrays.asList(
		    Arrays.asList(__, __, __, __, __, xx, __),
		    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
		    Arrays.asList(__, xx, __, __, __, xx, __),
		    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
		    Arrays.asList(__, xx, __, xx, __, __, __),
		    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
		
		drawBoard(gc, board);

		rootGroup.getChildren().add(canvas);
		Scene scene = new Scene(rootGroup);
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	/**
	 * <code>private void drawBoard(GraphicsContext gc)</code>
	 * 
	 * <p>This method creates and draw a Board on a specified Canvas</p>
	 * 
	 * @param gc 
	 */
	private void drawBoard(GraphicsContext gc, Board board) {
		
		int xCursor = 0, yCursor = 0;
		
		for (Cell c : Cell.ROW_MAJOR_ORDER) {
			switch (board.blocksAt(c).tail().head()) {
			case INDESTRUCTIBLE_WALL:
				gc.drawImage(BLOCKS_IMAGES[1], xCursor, yCursor);
				break;
			case DESTRUCTIBLE_WALL:
				gc.drawImage(BLOCKS_IMAGES[2], xCursor, yCursor);
				break;
			case CRUMBLING_WALL:
				gc.drawImage(BLOCKS_IMAGES[3], xCursor, yCursor);
				break;
			case FREE:
				gc.drawImage(BLOCKS_IMAGES[0], xCursor, yCursor);
				break;
			default:
				break;
			}
			xCursor += BLOCK_WIDTH;
			if (xCursor % Cell.COLUMNS * BLOCK_WIDTH == 0) {
				xCursor = 0;
				yCursor = yCursor += BLOCK_HEIGHT;
			}
		}
	}
	
	private static Image[] initImages(){
		Image[] images = new Image[Block.values().length];
		for(int i = 0; i < Block.values().length; ++i)
			images[i] = new Image(BoardDrawer.class.getResource("/blocks/"+Block.values()[i].toString()+".png").toString());
		return images;
	}
	
	public static void main(String[] args) throws Exception {
		launch(args);
	}
}
