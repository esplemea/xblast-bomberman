package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 
 * @author Nicolas ZIMMERMANN Clara DI MARCO
 * @date Mai 25, 2016
 *
 */

public final class KeyboardEventHandler extends KeyAdapter {
    
    private final Map<Integer, PlayerAction> actions;
    private final Consumer<PlayerAction> consumer;

    /**
     * Constructor for KeyboardEventHandler
     * 
     * @param actions
     * @param consumer
     */
    public KeyboardEventHandler(Map<Integer, PlayerAction> actions,
            Consumer<PlayerAction> consumer) {
        this.actions = Collections.unmodifiableMap(new HashMap<>(actions));
        this.consumer = consumer;
    }

    
    /**
     * Invoked when a key has been pressed.
     */
    @Override
    public void keyPressed(KeyEvent event) {
        if (actions.containsKey(event.getKeyCode()))
            consumer.accept(actions.get(event.getKeyCode()));
    }

}
