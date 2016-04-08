package ch.epfl.xblast.server.debug.boardDrawer;

import ch.epfl.xblast.*;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;
import ch.epfl.xblast.server.debug.Boards;
import ch.epfl.xblast.server.debug.geometry.Vector2D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

/**
 * @author Alexandre MICHEL
 * @version 1.0
 * @date 28 février 2016
 */
public final class GameStateDrawer extends Application {

    private final static GameState INITIAL_GAMESTATE = new GameState(Boards.STEP2_SAMPLE,
            Arrays.asList(
                    new Player(PlayerID.PLAYER_1, 5, new Cell(1, 1), 3, 5),
                    new Player(PlayerID.PLAYER_2, 5, new Cell(1, 11), 3, 3),
                    new Player(PlayerID.PLAYER_3, 5, new Cell(13, 11), 3, 3),
                    new Player(PlayerID.PLAYER_4, 5, new Cell(13, 1), 3, 3)));

    private final static int BLOCK_WIDTH = 64;
    private final static int BLOCK_HEIGHT = 48;
    private final static int LABEL_BAR = 40;
    private static Image[] BLOCKS_IMAGES;

    private final List<GameState> mGameStates = new ArrayList<>();
    private int mTicks = 0;
    private AnimationTimer timer;
    private boolean timerOn = true;

    private Map<PlayerID, Optional<Direction>> mMoveEvent = Collections.emptyMap();
    private Set<PlayerID> mBombDropEvent = Collections.emptySet();

    @Override
    public void start(Stage primaryStage) throws Exception {

        BLOCKS_IMAGES = initImages();
        primaryStage.setTitle("XBlast Debug Board Drawer");

        mGameStates.add(INITIAL_GAMESTATE);

        primaryStage.setScene(initScene());
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private Scene initScene() throws IOException {
        Parent root = FXMLLoader
                .load(getClass().getResource("/ch/epfl/xblast/server/debug/boardDrawer/BoardDrawer.fxml"));
        Scene scene = new Scene(root, Cell.COLUMNS * BLOCK_WIDTH, Cell.ROWS * BLOCK_HEIGHT + LABEL_BAR);

        AnchorPane canvasPane = (AnchorPane) scene.lookup("#canvasPane");
        // AnchorPane counterPane = (AnchorPane) scene.lookup("#counterPane");
        AnchorPane.setBottomAnchor(canvasPane, (double) LABEL_BAR);

        Label ticksCounter = (Label) scene.lookup("#ticksCounter");
        ticksCounter.setFont(new Font(LABEL_BAR));

        Canvas canvas = (Canvas) root.lookup("#canvas");
        canvas.setHeight(scene.getHeight());
        canvas.setWidth(scene.getWidth());

        drawGameState(canvas.getGraphicsContext2D(), ticksCounter, mGameStates.get(mTicks));

         timer = new AnimationTimer() {

            @Override
            public void handle(long now) {
                stepForward(mMoveEvent, mBombDropEvent, canvas.getGraphicsContext2D(), ticksCounter);
                mMoveEvent = Collections.emptyMap();
                mBombDropEvent = Collections.emptySet();
            }
        };
        timer.start();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            Map<PlayerID, Optional<Direction>> mMoveEvent = Collections.emptyMap();
            switch (e.getCode()) {

                case UP:
                    mMoveEvent = new HashMap<>();
                    mMoveEvent.put(PlayerID.PLAYER_1, Optional.of(Direction.N));
                    stepForward(mMoveEvent, Collections.emptySet(), canvas.getGraphicsContext2D(), ticksCounter);
                    break;

                case DOWN:
                    mMoveEvent = new HashMap<>();
                    mMoveEvent.put(PlayerID.PLAYER_1, Optional.of(Direction.S));
                    stepForward(mMoveEvent, Collections.emptySet(), canvas.getGraphicsContext2D(), ticksCounter);
                    break;

                case RIGHT:
                    mMoveEvent = new HashMap<>();
                    mMoveEvent.put(PlayerID.PLAYER_1, Optional.of(Direction.E));
                    stepForward(mMoveEvent, Collections.emptySet(), canvas.getGraphicsContext2D(), ticksCounter);
                    break;

                case LEFT:
                    mMoveEvent = new HashMap<>();
                    mMoveEvent.put(PlayerID.PLAYER_1, Optional.of(Direction.W));
                    stepForward(mMoveEvent, Collections.emptySet(), canvas.getGraphicsContext2D(), ticksCounter);
                    break;

                case SHIFT:
                    mMoveEvent = new HashMap<>();
                    mMoveEvent.put(PlayerID.PLAYER_1, Optional.empty());
                    stepForward(mMoveEvent, Collections.emptySet(), canvas.getGraphicsContext2D(), ticksCounter);
                    break;

                case SPACE:
                    if (timerOn) timer.stop();
                    else timer.start();
                    timerOn = !timerOn;
                    break;

                case B:
                    Set<PlayerID> s = new HashSet<>();
                    s.addAll(Collections.singletonList(PlayerID.PLAYER_1));
                    stepForward(Collections.emptyMap(), s, canvas.getGraphicsContext2D(), ticksCounter);
                    break;

                case A:
                    if (mTicks != 0)
                        --mTicks;
                    drawGameState(canvas.getGraphicsContext2D(), ticksCounter, mGameStates.get(mTicks));
                    break;
                case  D:
                    stepForward(Collections.emptyMap(), Collections.emptySet(), canvas.getGraphicsContext2D(), ticksCounter);
                    break;

                default:
                    break;
            }
        });

        return scene;
    }

    private void stepForward(Map<PlayerID, Optional<Direction>> movementEvent, Set<PlayerID> bombDropEvent, GraphicsContext gc, Label ticksCounter) {
        if (mGameStates.size() == mTicks + 1)
            mGameStates.add(mGameStates.get(mTicks).next(movementEvent, bombDropEvent));
        ++mTicks;
        drawGameState(gc, ticksCounter, mGameStates.get(mTicks));
        //GameStatePrinter.printGameState(mGameStates.get(mTicks));
    }

    /**
     * <p>
     * This method creates and draw a Board on a specified Canvas
     * </p>
     *
     * @param gc Graphic context to draw on.
     */
    private void drawGameState(GraphicsContext gc, Label ticksCounter, GameState state) {

        ticksCounter.setText(String.valueOf(mTicks));

        // Draw Board
        Cell.ROW_MAJOR_ORDER.forEach(c -> gc.drawImage(imageForBlock(state.board().blockAt(c)),
                coordFromCell(c).x(), coordFromCell(c).y()));

        //Draw bombs
        for (Cell cell : state.bombedCells().keySet()) {
            Vector2D v = coordFromCell(cell);
            gc.setFill(Color.BLACK);
            gc.fillOval(v.x(), v.y(), BLOCK_WIDTH, BLOCK_HEIGHT);
        }

        // Draw Player
        for (Player player : state.alivePlayers()) {
            Vector2D vector2d = coordFromSubCell(player.position());
            int xAxis = vector2d.x() - (BLOCK_WIDTH / 2) + 2;

            gc.setFill(Color.WHITE);
            if(player.lifeState().state() == Player.LifeState.State.DYING)gc.setFill(Color.GRAY);
            if(player.lifeState().state() == Player.LifeState.State.INVULNERABLE)gc.setFill(Color.GREEN);
            gc.fillRect(xAxis, vector2d.y() - 13, 60, 20);
            gc.setFill(Color.RED);
            gc.setFont(new Font(20));
            gc.fillText(player.id() + "-" + player.direction(), xAxis, vector2d.y() + 3, 58);
        }

        // Draw blasts
        for (Cell cell : state.blastedCells()) {
            Vector2D v = coordFromCell(cell);
            gc.setFill(Color.YELLOW);
            gc.fillOval(v.x()+((BLOCK_WIDTH-BLOCK_HEIGHT)/2), v.y(), BLOCK_HEIGHT, BLOCK_HEIGHT);
        }
    }

    private Image imageForBlock(Block b) {
        switch (b) {
            case INDESTRUCTIBLE_WALL:
                return BLOCKS_IMAGES[1];
            case DESTRUCTIBLE_WALL:
                return BLOCKS_IMAGES[2];
            case CRUMBLING_WALL:
                return BLOCKS_IMAGES[3];
            case FREE:
                return BLOCKS_IMAGES[0];
            case BONUS_BOMB:
                return BLOCKS_IMAGES[4];
            case BONUS_RANGE:
                return BLOCKS_IMAGES[5];
            default:
                return null;
        }
    }

    private Vector2D coordFromCell(Cell c) {
        return new Vector2D(c.x() * BLOCK_WIDTH, c.y() * BLOCK_HEIGHT);
    }

    private Vector2D coordFromSubCell(SubCell sc) {
        return new Vector2D(sc.x() * (BLOCK_WIDTH / 16), sc.y() * (BLOCK_HEIGHT / 16));
    }

    private static Image[] initImages() {
        Image[] images = new Image[Block.values().length];
        for (int i = 0; i < Block.values().length; ++i)
            images[i] = new Image(GameStateDrawer.class.getResource("/blocks/" + Block.values()[i].toString() + ".png").toString());

        return images;
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}