package com.physmo.garnet.renderer;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

/**
 * Owns the GL buffers used to upload and draw one CPU-generated batch mesh.
 */
public class DynamicMeshBuffer {
    public static final int POSITION_COMPONENTS = 2;
    public static final int TEX_COORD_COMPONENTS = 2;
    public static final int COLOR_COMPONENTS = 4;
    public static final int MATERIAL_FLAG_COMPONENTS = 1;
    public static final int FLOATS_PER_VERTEX = POSITION_COMPONENTS + TEX_COORD_COMPONENTS + COLOR_COMPONENTS + MATERIAL_FLAG_COMPONENTS;
    public static final int VERTEX_STRIDE_BYTES = FLOATS_PER_VERTEX * BYTES_PER_FLOAT;
    public static final int BYTES_PER_FLOAT = Float.BYTES;
    public static final int POSITION_OFFSET_BYTES = 0;
    public static final int TEX_COORD_OFFSET_BYTES = POSITION_COMPONENTS * BYTES_PER_FLOAT;
    public static final int COLOR_OFFSET_BYTES = (POSITION_COMPONENTS + TEX_COORD_COMPONENTS) * BYTES_PER_FLOAT;
    public static final int MATERIAL_FLAGS_OFFSET_BYTES = (POSITION_COMPONENTS + TEX_COORD_COMPONENTS + COLOR_COMPONENTS) * BYTES_PER_FLOAT;

    public static final int POSITION_ATTRIBUTE_INDEX = 0;
    public static final int TEX_COORD_ATTRIBUTE_INDEX = 1;
    public static final int COLOR_ATTRIBUTE_INDEX = 2;
    public static final int MATERIAL_FLAGS_ATTRIBUTE_INDEX = 3;

    private int vboId;
    private int eboId;
    private int uploadedVertexCapacity;
    private int uploadedIndexCapacity;
    private boolean disposed;

    public void upload(BatchMesh mesh) {
        ensureCreated();
        float[] vertexData = toFloatArray(mesh);
        int[] indexData = toIntArray(mesh);

        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexData.length);
        vertexBuffer.put(vertexData).flip();
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_DYNAMIC_DRAW);
        uploadedVertexCapacity = Math.max(uploadedVertexCapacity, mesh.vertexCount());

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(indexData.length);
        indexBuffer.put(indexData).flip();
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_DYNAMIC_DRAW);
        uploadedIndexCapacity = Math.max(uploadedIndexCapacity, mesh.indexCount());

        configureAttributes();
    }

    private void ensureCreated() {
        if (disposed) throw new IllegalStateException("DynamicMeshBuffer has been disposed");
        if (vboId != 0) return;
        vboId = glGenBuffers();
        eboId = glGenBuffers();
    }

    static float[] toFloatArray(BatchMesh mesh) {
        float[] data = new float[mesh.vertexCount() * FLOATS_PER_VERTEX];
        int offset = 0;
        for (BatchVertex vertex : mesh.vertices()) {
            data[offset++] = vertex.x();
            data[offset++] = vertex.y();
            data[offset++] = vertex.u();
            data[offset++] = vertex.v();
            data[offset++] = vertex.r();
            data[offset++] = vertex.g();
            data[offset++] = vertex.b();
            data[offset++] = vertex.a();
            data[offset++] = vertex.materialFlags();
        }
        return data;
    }

    static int[] toIntArray(BatchMesh mesh) {
        int[] data = new int[mesh.indexCount()];
        for (int i = 0; i < mesh.indexCount(); i++) {
            data[i] = mesh.indices().get(i);
        }
        return data;
    }

    private void configureAttributes() {
        glEnableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
        glVertexAttribPointer(POSITION_ATTRIBUTE_INDEX, POSITION_COMPONENTS, GL_FLOAT, false, VERTEX_STRIDE_BYTES, POSITION_OFFSET_BYTES);

        glEnableVertexAttribArray(TEX_COORD_ATTRIBUTE_INDEX);
        glVertexAttribPointer(TEX_COORD_ATTRIBUTE_INDEX, TEX_COORD_COMPONENTS, GL_FLOAT, false, VERTEX_STRIDE_BYTES, TEX_COORD_OFFSET_BYTES);

        glEnableVertexAttribArray(COLOR_ATTRIBUTE_INDEX);
        glVertexAttribPointer(COLOR_ATTRIBUTE_INDEX, COLOR_COMPONENTS, GL_FLOAT, false, VERTEX_STRIDE_BYTES, COLOR_OFFSET_BYTES);

        glEnableVertexAttribArray(MATERIAL_FLAGS_ATTRIBUTE_INDEX);
        glVertexAttribPointer(MATERIAL_FLAGS_ATTRIBUTE_INDEX, MATERIAL_FLAG_COMPONENTS, GL_FLOAT, false, VERTEX_STRIDE_BYTES, MATERIAL_FLAGS_OFFSET_BYTES);
    }

    public void draw(RenderRun run) {
        if (run.indexCount() == 0) return;
        ensureCreated();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        configureAttributes();
        glDrawElements(GL_TRIANGLES, run.indexCount(), GL_UNSIGNED_INT, (long) run.firstIndex() * Integer.BYTES);
    }

    public void dispose() {
        if (disposed) return;
        if (vboId != 0) glDeleteBuffers(vboId);
        if (eboId != 0) glDeleteBuffers(eboId);
        vboId = 0;
        eboId = 0;
        disposed = true;
    }

    public int uploadedVertexCapacity() {
        return uploadedVertexCapacity;
    }

    public int uploadedIndexCapacity() {
        return uploadedIndexCapacity;
    }

    public void disableAttributes() {
        glDisableVertexAttribArray(POSITION_ATTRIBUTE_INDEX);
        glDisableVertexAttribArray(TEX_COORD_ATTRIBUTE_INDEX);
        glDisableVertexAttribArray(COLOR_ATTRIBUTE_INDEX);
        glDisableVertexAttribArray(MATERIAL_FLAGS_ATTRIBUTE_INDEX);
    }
}
