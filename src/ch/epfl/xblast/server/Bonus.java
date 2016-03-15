package ch.epfl.xblast.server;

public enum Bonus {
    
    INC_BOMB {
        @Override
        public Player applyTo(Player player) {
            if(player.maxBombs() < maxBomb){
                return player.withMaxBombs(player.maxBombs() + 1);
            }
            return player;
        }
    },

    INC_RANGE {
        @Override
        public Player applyTo(Player player) {
            if(player.bombRange() < maxRange){
                return player.withBombRange(player.bombRange() + 1);
            }
            return player;
        }
    };
    
    private static final int maxBomb = 9;
    private static final int maxRange = 9;

    abstract public Player applyTo(Player player);
}
