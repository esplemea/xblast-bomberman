package ch.epfl.xblast.client;

import java.awt.Image;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.SubCell;

public final class GameState {
	private final List<Player> players;
	private final List<Image> board;
	private final List<Image> bombs_blasts;
	private final List<Image> score;
	private final List<Image> time;

	public GameState(List<Player> players, List<Image> board, List<Image> bombs_blasts, List<Image> score,
			List<Image> time) {
		this.players = Collections.unmodifiableList(new ArrayList<>(players));
		this.board = Collections.unmodifiableList(new ArrayList<>(board));
		this.bombs_blasts = Collections.unmodifiableList(new ArrayList<>(bombs_blasts));
		this.score = Collections.unmodifiableList(new ArrayList<>(score));
		this.time = Collections.unmodifiableList(new ArrayList<>(time));
	}

	public static final class Player {
		private final PlayerID id;
		private final int lives;
		private final SubCell position;
		private final Image image;

		private Player(PlayerID id, int lives, SubCell position, Image image) {
			this.id = id;
			this.lives = lives;
			this.position = position;
			this.image = image;
		}
	}
}
