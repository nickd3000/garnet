package com.physmo.garnet.renderer;

/**
 * Diagnostic counters for the buffered renderer migration.
 */
public class BatchRenderStats {
    private int queuedElements;
    private int renderRuns;
    private int bufferUploads;
    private int drawCalls;
    private int textureBinds;

    public BatchRenderStats() {
    }

    public BatchRenderStats(BatchRenderStats other) {
        queuedElements = other.queuedElements;
        renderRuns = other.renderRuns;
        bufferUploads = other.bufferUploads;
        drawCalls = other.drawCalls;
        textureBinds = other.textureBinds;
    }

    public void reset() {
        queuedElements = 0;
        renderRuns = 0;
        bufferUploads = 0;
        drawCalls = 0;
        textureBinds = 0;
    }

    public void incrementBufferUploads() {
        bufferUploads++;
    }

    public void incrementDrawCalls() {
        drawCalls++;
    }

    public void incrementTextureBinds() {
        textureBinds++;
    }

    public int getQueuedElements() {
        return queuedElements;
    }

    public void setQueuedElements(int queuedElements) {
        this.queuedElements = queuedElements;
    }

    public int getRenderRuns() {
        return renderRuns;
    }

    public void setRenderRuns(int renderRuns) {
        this.renderRuns = renderRuns;
    }

    public int getBufferUploads() {
        return bufferUploads;
    }

    public int getDrawCalls() {
        return drawCalls;
    }

    public int getTextureBinds() {
        return textureBinds;
    }

    public BatchRenderStats snapshot() {
        return new BatchRenderStats(this);
    }

    @Override
    public String toString() {
        return "BatchRenderStats{" +
                "queuedElements=" + queuedElements +
                ", renderRuns=" + renderRuns +
                ", bufferUploads=" + bufferUploads +
                ", drawCalls=" + drawCalls +
                ", textureBinds=" + textureBinds +
                '}';
    }
}
