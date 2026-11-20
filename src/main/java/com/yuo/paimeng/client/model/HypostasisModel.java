package com.yuo.paimeng.client.model;

/**
 * 历史占位：无相之岩最初用 EntityModel + 立方体模型做外壳，无法满足“外壳使用原版方块纹理”的要求。
 * 现在的实现改为在 {@link com.yuo.paimeng.client.render.HypostasisRenderer} 里直接调用
 * 原版方块模型渲染（{@code BlockRenderDispatcher#renderSingleBlock}），
 * 因此不再需要本类的几何定义。下次大清理时可以直接删除本文件。
 */
final class HypostasisModel {
    private HypostasisModel() {
    }
}
