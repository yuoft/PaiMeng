package com.yuo.PaiMeng.WorldGen;

import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class TreeInit {

    public static final TreeSpawner APPLE_TREE = new TreeSpawner() {
        @Override
        protected Feature getFeature(Random random) {
            return FeatureInit.APPLE_TREE.get();
        }
    };
    public static final TreeSpawner SUN_APPLE_TREE = new TreeSpawner() {
        @Override
        protected Feature getFeature(Random random) {
            return FeatureInit.SUN_APPLE_TREE.get();
        }
    };
    public static final TreeSpawner PURPLE_APPLE_TREE = new TreeSpawner() {
        @Override
        protected Feature getFeature(Random random) {
            return FeatureInit.PURPLE_APPLE_TREE.get();
        }
    };
    public static final TreeSpawner ZAOYE_TREE = new TreeSpawner() {
        @Override
        protected Feature getFeature(Random random) {
            return FeatureInit.ZAOYE_TREE.get();
        }
    };
}
