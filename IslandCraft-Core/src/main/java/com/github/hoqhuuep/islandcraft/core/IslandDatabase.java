package com.github.hoqhuuep.islandcraft.core;

public interface IslandDatabase {
    static class Result {
        private final long islandSeed;
        private final String generator;

        public Result(final long islandSeed, final String generator) {
            this.islandSeed = islandSeed;
            this.generator = generator;
        }

        public long getIslandSeed() {
            return islandSeed;
        }

        public String getGenerator() {
            return generator;
        }
    }

    void save(String worldName, int centerX, int centerZ, long islandSeed, String generator);

    Result load(String worldName, int centerX, int centerZ);

    boolean isEmpty(String worldName);
}
