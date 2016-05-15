package ch.epfl.xblast.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class KeyboardEventHandler extends KeyAdapter {
    private final Map<Integer, PlayerAction> actions;
    private final Consumer<PlayerAction> consumer;
    

    public KeyboardEventHandler(Map<Integer, PlayerAction> actions, Consumer<PlayerAction> consumer) {
        this.actions = Collections.unmodifiableMap(new HashMap<>(actions));
        this.consumer = consumer;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if(actions.containsKey(event.getKeyCode()))
        consumer.accept(actions.get(event.getKeyCode()));
    }

}
