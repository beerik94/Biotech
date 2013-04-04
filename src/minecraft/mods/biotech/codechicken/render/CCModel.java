package biotech.codechicken.render;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import biotech.codechicken.vec.CoordinateSystem;
import biotech.codechicken.vec.ITransformation;
import biotech.codechicken.vec.RightHanded;
import biotech.codechicken.vec.Quat;
import biotech.codechicken.vec.Vector3;

public class CCModel
{
    private static class PositionNormalEntry
    {        
        public Vector3 pos;
        public LinkedList<Vector3> normals = new LinkedList<Vector3>();
        
        public PositionNormalEntry(Vector3 position)
        {
            pos = position;
        }
        
        public boolean positionEqual(Vector3 v)
        {
            return pos.x == v.x && pos.y == v.y && pos.z == v.z;
        }
        
        public PositionNormalEntry addNormal(Vector3 normal)
        {
            normals.add(normal);
            return this;
        }
    }
    
    public final int vertexMode;
    public final int vp;
    public Vertex5[] verts;
    public Vector3[] normals;
    public int[] colours;
    
    private CCModel(int vertexMode)
    {
        if(vertexMode != 7 && vertexMode != 4)
            throw new IllegalArgumentException("Models must be GL_QUADS or GL_TRIANGLES");
        
        this.vertexMode = vertexMode;
        vp = vertexMode == 7 ? 4 : 3;
    }
    
    /**
     * Each pixel corresponds to one unit of position when generating the model
     * @param i Vertex index to start generating at
     * @param x1 The minX bound of the box
     * @param y1 The minY bound of the box
     * @param z1 The minZ bound of the box
     * @param w The width of the box
     * @param h The height of the box
     * @param d The depth of the box
     * @param tx The distance of the top left corner of the texture map from the left in pixels
     * @param ty The distance of the top left corner of the texture map from the top in pixels
     * @param tw The width of the texture in pixels
     * @param th The height of the texture in pixels
     * @param f The scale of the model, pixels per block, normally 16
     * @return The generated model
     */
    public CCModel generateBox(int i, double x1, double y1, double z1, double w, double h, double d, double tx, double ty, double tw, double th, double f)
    {
        double u1, v1, u2, v2;
        double x2 = x1+w;
        double y2 = y1+h;
        double z2 = z1+d;
        x1 /= f; x2 /= f; y1 /= f; y2 /= f; z1 /= f; z2 /= f;
    
        //bottom face
        u1 = (tx + d + w) / tw; v1 = (ty + d) / th;
        u2 = (tx + d*2 + w) / tw; v2 = ty / th;
        verts[i++] = new Vertex5(x1, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z1, u1, v1);
        verts[i++] = new Vertex5(x2, y1, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y1, z2, u2, v2);
    
        //top face
        u1 = (tx + d) / tw; v1 = (ty + d) / th;
        u2 = (tx + d + w) / tw; v2 = ty / th;
        verts[i++] = new Vertex5(x2, y2, z2, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x1, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x1, y2, z2, u1, v2);
    
        //front face
        u1 = (tx + d + w) / tw; v1 = (ty + d) / th;
        u2 = (tx + d) / tw; v2 = (ty + d + h) / th;
        verts[i++] = new Vertex5(x1, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x2, y1, z1, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z1, u2, v2);
    
        //back face
        u1 = (tx + d*2 + w*2) / tw; v1 = (ty + d) / th;
        u2 = (tx + d*2 + w) / tw; v2 = (ty + d + h) / th;
        verts[i++] = new Vertex5(x1, y2, z2, u1, v1);
        verts[i++] = new Vertex5(x1, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x2, y1, z2, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z2, u2, v1);
    
        //left face
        u1 = (tx + d) / tw; v1 = (ty + d) / th;
        u2 = (tx) / tw; v2 = (ty + d + h) / th;
        verts[i++] = new Vertex5(x1, y2, z2, u2, v1);
        verts[i++] = new Vertex5(x1, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x1, y1, z1, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z2, u2, v2);
    
        //right face
        u1 = (tx + d*2 + w) / tw; v1 = (ty + d) / th;
        u2 = (tx + d + w) / tw; v2 = (ty + d + h) / th;
        verts[i++] = new Vertex5(x2, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x2, y1, z1, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y2, z2, u1, v1);
        
        return this;
    }

    /**
     * Generates a box, uv mapped to be the same as a minecraft block with the same bounds
     * @param i The vertex index to start generating at
     * @param x1 minX
     * @param y1 minY
     * @param z1 minZ
     * @param x2 maxX
     * @param y2 maxY
     * @param z2 maxZ
     * @return The generated model. When rendering an icon will need to be supplied for the UV transformation.
     */
    public CCModel generateBlock(int i, double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double u1, v1, u2, v2;
        
        //bottom face
        u1 = x1; v1 = z1;
        u2 = x2; v2 = z2;
        verts[i++] = new Vertex5(x1, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z1, u1, v1);
        verts[i++] = new Vertex5(x2, y1, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y1, z2, u2, v2);
    
        //top face
        u1 = x1; v1 = z1;
        u2 = x2; v2 = z2;
        verts[i++] = new Vertex5(x2, y2, z2, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x1, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x1, y2, z2, u1, v2);
    
        //east face
        u1 = x1; v1 = y1;
        u2 = x2; v2 = y2;
        verts[i++] = new Vertex5(x1, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x2, y1, z1, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z1, u2, v2);
    
        //west face
        u1 = x1; v1 = y1;
        u2 = x2; v2 = y2;
        verts[i++] = new Vertex5(x1, y2, z2, u1, v1);
        verts[i++] = new Vertex5(x1, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x2, y1, z2, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z2, u2, v1);
    
        //north face
        u1 = z1; v1 = y1;
        u2 = z2; v2 = y2;
        verts[i++] = new Vertex5(x1, y2, z2, u2, v1);
        verts[i++] = new Vertex5(x1, y2, z1, u1, v1);
        verts[i++] = new Vertex5(x1, y1, z1, u1, v2);
        verts[i++] = new Vertex5(x1, y1, z2, u2, v2);
    
        //south face
        u1 = z1; v1 = y1;
        u2 = z2; v2 = y2;
        verts[i++] = new Vertex5(x2, y1, z2, u1, v2);
        verts[i++] = new Vertex5(x2, y1, z1, u2, v2);
        verts[i++] = new Vertex5(x2, y2, z1, u2, v1);
        verts[i++] = new Vertex5(x2, y2, z2, u1, v1);
        
        return this;
    }
    
    public CCModel computeNormals()
    {
        return computeNormals(0, verts.length);
    }

    /**
     * Computes the normals of all faces in the model.
     * Uses the cross product of the vectors along 2 sides of the face
     * @param start The first vertex to generate normals for
     * @param length The number of vertices to generate normals for. Note this must be a multiple of 3 for triangles or 4 for quads
     * @return The model
     */
    public CCModel computeNormals(int start, int length)
    {
        if(length%vp != 0 || start%vp != 0)
            throw new IllegalArgumentException("Cannot generate normals across polygons");
        
        if(normals == null)
            normals = new Vector3[verts.length];
        
        for(int k = 0; k < length; k+=vp)
        {
            int i = k + start;
            Vector3 diff1 = verts[i+1].vec.copy().subtract(verts[i].vec);
            Vector3 diff2 = verts[i+vp-1].vec.copy().subtract(verts[i].vec);
            normals[i] = diff1.crossProduct(diff2).normalize();
            for(int d = 1; d < vp; d++)
                normals[i+d] = normals[i].copy();
        }
        
        return this;
    }
    
    /**
     * Computes lighting using the normals and the colour 0xFFFFFF.
     * Per vert colouring will be added when needed.
     * Make sure you have generated your normals on the model first.
     * If you rotate your model after this, the lighting will no longer be valid
     * @param light The light model to calculate
     * @return The model
     */
    public CCModel computeLighting(LightModel light)
    {
        colours = new int[verts.length];
        for(int k = 0; k < verts.length; k++)
            colours[k] = light.apply(0xFFFFFF, normals[k]);
        return this;
    }
    
    /**
     * Averages all normals at the same position to produce a smooth lighting effect.
     * @return The model
     */
    public CCModel smoothNormals()
    {
        ArrayList<PositionNormalEntry> map = new ArrayList<PositionNormalEntry>();
        nextvert: for(int k = 0; k < verts.length; k++)
        {
            Vector3 vec = verts[k].vec;
            for(PositionNormalEntry e : map)
                if(e.positionEqual(vec))
                {
                    e.addNormal(normals[k]);
                    continue nextvert;
                }
            
            map.add(new PositionNormalEntry(vec).addNormal(normals[k]));
        }
        
        for(PositionNormalEntry e : map)
        {
            if(e.normals.size() <= 1)
                continue;
            
            Vector3 new_n = new Vector3();
            for(Vector3 n : e.normals)
                new_n.add(n);
            
            new_n.normalize();
            for(Vector3 n : e.normals)
                n.set(new_n);
        }
        
        return this;
    }

    /**
     * Uses quat to rotate all vertices around 0.
     * Normals will be rotated about zero if generated
     * @return The model
     */
    public CCModel rotate(Quat quat)
    {
        return rotate(quat, new Vector3());
    }
    
    /**
     * Uses quat to rotate all vertices around point.
     * Normals will be rotated about zero if generated
     * @return The model
     */
    public CCModel rotate(Quat quat, Vector3 point)
    {
        boolean translate = !point.isZero();
        for(int k = 0; k < verts.length; k++)
        {
            if(translate)
                verts[k].vec
                    .subtract(point)
                    .rotate(quat)
                    .add(point);
            else
                verts[k].vec
                .rotate(quat);
        }
        
        if(normals != null)
            for(int k = 0; k < normals.length; k++)
                quat.rotate(normals[k]);
        
        return this;
    }

    /**
     * Scales all vertices from point
     * @return The model
     */
    public CCModel scale(double f)
    {
        return scale(new Vector3(f, f, f), new Vector3());
    }

    /**
     * Scales all vertices from point
     * @return The model
     */
    public CCModel scale(double f, Vector3 point)
    {
        return scale(new Vector3(f, f, f), point);
    }

    /**
     * Scales all vertices from point
     * @return The model
     */
    public CCModel scale(Vector3 f)
    {
        return scale(f, new Vector3());
    }

    /**
     * Scales all vertices from point
     * @return The model
     */
    public CCModel scale(Vector3 f, Vector3 point)
    {
        boolean translate = !point.isZero();
            
        for(int k = 0; k < verts.length; k++)
        {
            if(translate)
                verts[k].vec
                    .subtract(point)
                    .multiply(f)
                    .add(point);
            else
                verts[k].vec
                .multiply(f);
        }
        
        return this;
    }

    /**
     * Translates all vertices
     * @return The model
     */
    public CCModel translate(Vector3 offset)
    {
        for(int k = 0; k < verts.length; k++)
            verts[k].vec
                .add(offset);
        
        return this;
    }

    /**
     * Renders the entire model
     * @param x The x offset to render at
     * @param y The y offset to render at
     * @param z The z offset to render at
     * @param u The u texture offset
     * @param v The v texture offset
     */
    public void render(double x, double y, double z, double u, double v)
    {
        render(0, verts.length, new Vector3(x, y, z).translation(), u, v);
    }
    
    public void render(double x, double y, double z, Icon icon)
    {
        render(0, verts.length, new Vector3(x, y, z).translation(), new IconTransformation(icon));
    }
    
    public void render(ITransformation t, Icon icon)
    {
        render(0, verts.length, t, new IconTransformation(icon));
    }

    public void render(ITransformation t, double u, double v)
    {
        render(0, verts.length, t, u, v);
    }

    /**
     * Legacy helper function
     */
    public void render(int start, int length, ITransformation t, double u, double v)
    {
        if(u == 0 && v == 0)
            render(start, length, t, null);
        else
            render(start, length, t, new UVTranslation(u, v));
    }
    
    /**
     * Renders vertices start through start+length-1 of the model
     * @param start The first vertex to render
     * @param length The number of vertices to render
     * @param t The transformation to apply to the mat
     * @param u The u texture offset
     * @param v The v texture offset
     */
    public void render(int start, int length, ITransformation t, IUVTransformation u)
    {
        boolean useNormal = CCRenderState.useNormals() && normals != null;
        boolean useColour = CCRenderState.useModelColours() && colours != null;
        Vector3 normal;
        Vertex5 vert;
        Vector3 vec = t == null ? null : new Vector3();
        UV uv = new UV();
        Tessellator tess = Tessellator.instance;
        for(int k = 0; k < length; k++)
        {
            if(useNormal)
            {
                normal = normals[start+k];
                tess.setNormal((float)normal.x, (float)normal.y, (float)normal.z);
            }
            if(useColour)
                tess.setColorOpaque_I(colours[start+k]);
            vert = verts[start+k];
            if(t != null)
                t.transform(vec.set(vert.vec));
            else
                vec = vert.vec;
            uv.set(vert.u, vert.v);
            if(u != null)
                u.transform(uv);
            tess.addVertexWithUV(vec.x, vec.y, vec.z, uv.u, uv.v);
        }
    }

    public static CCModel quadModel(int numVerts)
    {
        return newModel(7, numVerts);
    }
    
    public static CCModel triModel(int numVerts)
    {
        return newModel(4, numVerts);
    }
    
    public static CCModel newModel(int vertexMode, int numVerts)
    {
        CCModel model = newModel(vertexMode);
        model.verts = new Vertex5[numVerts];
        return model;
    }
    
    public static CCModel newModel(int vertexMode)
    {
        return new CCModel(vertexMode);
    }
    
    private static double[] parseDoubles(String s, String token)
    {
        String[] as = s.split(token);
        double[] values = new double[as.length];
        for(int i = 0; i < as.length; i++)
            values[i] = Double.parseDouble(as[i]);
        return values;
    }

    private static void illegalAssert(boolean b, String err)
    {
        if(!b) throw new IllegalArgumentException(err);
    }

    private static void assertMatch(Matcher m, String s)
    {
        m.reset(s);
        illegalAssert(m.matches(), "Malformed line: "+s);
    }

    private static final Pattern vertPattern = Pattern.compile("v(?: ([\\d\\.-]+))+");
    private static final Pattern uvwPattern = Pattern.compile("vt(?: ([\\d\\.-]+))+");
    private static final Pattern normalPattern = Pattern.compile("vn(?: ([\\d\\.-]+))+");
    private static final Pattern polyPattern = Pattern.compile("f(?: ((?:\\d*)(?:/\\d*)(?:/\\d*)))+");
    private static final Matcher vertMatcher = vertPattern.matcher("");
    private static final Matcher uvwMatcher = uvwPattern.matcher("");
    private static final Matcher normalMatcher = normalPattern.matcher("");
    private static final Matcher polyMatcher = polyPattern.matcher("");
    
    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     * @param input An input stream to a obj file
     * @param importSystem The cooridnate system transformation to apply
     * @return A map of group names to models
     * @throws IOException
     */
    public static Map<String, CCModel> parseObjModels(InputStream input, CoordinateSystem importSystem) throws IOException
    {        
        HashMap<String, CCModel> modelMap = new HashMap<String, CCModel>();
        ArrayList<Vector3> verts = new ArrayList<Vector3>();
        ArrayList<Vector3> uvs = new ArrayList<Vector3>();
        ArrayList<Vector3> normals = new ArrayList<Vector3>();
        ArrayList<int[]> triangles = new ArrayList<int[]>();
        String modelName = "unnamed";
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));

        String line;
        while((line = reader.readLine()) != null)
        {
            line = line.replaceAll("\\s+", " ").trim();
            if(line.startsWith("#") || line.length() == 0)
                continue;
            
            if(line.startsWith("v "))
            {
                assertMatch(vertMatcher, line);
                double[] values = parseDoubles(line.substring(2), " ");
                illegalAssert(values.length >= 3, "Vertices must have x, y and z components");
                Vector3 vert = new Vector3(values[0], values[1], values[2]);
                importSystem.convert(vert);
                verts.add(vert);
                continue;
            }
            if(line.startsWith("vt "))
            {
                assertMatch(uvwMatcher, line);
                double[] values = parseDoubles(line.substring(3), " ");
                illegalAssert(values.length >= 2, "Tex Coords must have u, and v components");
                uvs.add(new Vector3(values[0], 1-values[1], 0));
                continue;
            }
            if(line.startsWith("vn "))
            {
                assertMatch(normalMatcher, line);
                double[] values = parseDoubles(line.substring(3), " ");
                illegalAssert(values.length >= 3, "Normals must have x, y and z components");
                Vector3 norm = new Vector3(values[0], values[1], values[2]).normalize();
                importSystem.convert(norm);
                normals.add(norm);
                continue;
            }
            if(line.startsWith("f "))
            {
                assertMatch(polyMatcher, line);
                String[] av = line.substring(2).split(" ");
                illegalAssert(av.length >= 3, "Polygons must have at least 3 vertices");
                int[][] polyVerts = new int[av.length][3];
                for(int i = 0; i < av.length; i++)
                {
                    String[] as = av[i].split("/");
                    for(int p = 0; p < as.length; p++)
                        if(as[p].length() > 0)
                            polyVerts[i][p] = Integer.parseInt(as[p]);
                }
                for(int i = 2; i < av.length; i++)
                {
                    triangles.add(polyVerts[0]);
                    triangles.add(polyVerts[i]);
                    triangles.add(polyVerts[i-1]);
                }
            }
            if(line.startsWith("g "))
            {
                if(!triangles.isEmpty())
                {
                    modelMap.put(modelName, createModel(verts, uvs, normals, triangles));
                    triangles.clear();
                }
                modelName = line.substring(2);
            }
        }
        
        if(!triangles.isEmpty())
            modelMap.put(modelName, createModel(verts, uvs, normals, triangles));
        
        return modelMap;
    }
    
    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     * @param s The name of the obj resource
     * @return A map of group names to models
     */
    public static Map<String, CCModel> parseObjModels(String s)
    {
        return parseObjModels(s, new RightHanded());
    }
    
    /**
     * Parses vertices, texture coords, normals and polygons from a WaveFront Obj file
     * @param s The name of the obj resource
     * @param importSystem The cooridnate system transformation to apply
     * @return A map of group names to models
     */
    public static Map<String, CCModel> parseObjModels(String s, CoordinateSystem importSystem)
    {
        try
        {
            return parseObjModels(CCModel.class.getResourceAsStream(s), importSystem);
        }
        catch(Exception e)
        {
            throw new RuntimeException("failed to load model: "+s, e);
        }
    }

    public static CCModel createModel(List<Vector3> verts, List<Vector3> uvs, List<Vector3> normals, List<int[]> triangles)
    {        
        if(triangles.size() < 3 || triangles.size()%3 != 0)
            throw new IllegalArgumentException("Invalid number of vertices for model: "+triangles.size());
        
        boolean hasNormals = triangles.get(0)[1] > 0;
        CCModel model = CCModel.newModel(4, triangles.size());
        if(hasNormals)
            model.normals = new Vector3[triangles.size()];
        
        for(int i = 0; i < triangles.size(); i++)
        {
            int[] ai = triangles.get(i);
            Vector3 vert = verts.get(ai[0]-1).copy();
            Vector3 uv = ai[1] <= 0 ? new Vector3() : uvs.get(ai[1]-1).copy();
            if(ai[2] > 0 != hasNormals)
                throw new IllegalArgumentException("Normals are an all or nothing deal here.");
            
            model.verts[i] = new Vertex5(vert, uv.x, uv.y);
            if(hasNormals)
                model.normals[i] = normals.get(ai[2]-1).copy();
        }
        
        return model;
    }

    /**
     * Brings the UV coordinates of each face closer to the center UV by d.
     * Useful for fixing texture seams
     */
    public CCModel shrinkUVs(double d)
    {
        for(int k = 0; k < verts.length; k+=vp)
        {
            double au = 0;
            double av = 0;
            for(int i = 0; i < vp; i++)
            {
                au+=verts[k+i].u;
                av+=verts[k+i].v;
            }
            au/=(double)vp;
            av/=(double)vp;
            for(int i = 0; i < vp; i++)
            {
                Vertex5 vert = verts[k+i];
                vert.u += vert.u < au ? d : -d;
                vert.v += vert.v < av ? d : -d;
            }
        }
        return this;
    }
    
    /**
     * @param side1 The side of this model
     * @param side2 The side of the new model
     * @param point The point to rotate around
     * @return A copy of this model rotated to the appropriate side
     */
    public CCModel sidedCopy(int side1, int side2, Vector3 point)
    {
        return rotatedCopy(side_quatsR[side1].copy().multiply(side_quats[side2]), point);
    }

    /**
     * @return A copy of this model, rotated by quat around 0
     */
    public CCModel rotatedCopy(Quat quat)
    {
        return rotatedCopy(quat, new Vector3());
    }

    /**
     * @return A copy of this model, rotated by quat around point
     */
    public CCModel rotatedCopy(Quat quat, Vector3 point)
    {
        CCModel model = newModel(vertexMode, verts.length);
        copy(this, 0, model, 0, model.verts.length);
        return model.rotate(quat, point);
    }

    /**
     * Copies length vertices and normals
     */
    public static void copy(CCModel src, int srcpos, CCModel dest, int destpos, int length)
    {
        for(int k = 0; k < length; k++)
            dest.verts[destpos+k] = src.verts[srcpos+k].copy();
        
        if(src.normals != null)
        {
            if(dest.normals == null)
                dest.normals = new Vector3[dest.verts.length];

            for(int k = 0; k < length; k++)
                dest.normals[destpos+k] = src.normals[srcpos+k].copy();
        }
        
        if(src.colours != null)
        {
            if(dest.colours == null)
                dest.colours = new int[dest.verts.length];
            System.arraycopy(src.colours, srcpos, dest.colours, destpos, length);
        }
    }

    public static Quat[] side_quats = new Quat[]{
        Quat.aroundAxis(1, 0, 0, 0),
        Quat.aroundAxis(1, 0, 0, Math.PI),
        Quat.aroundAxis(1, 0, 0, Math.PI/2),
        Quat.aroundAxis(1, 0, 0,-Math.PI/2),
        Quat.aroundAxis(0, 0, 1,-Math.PI/2),
        Quat.aroundAxis(0, 0, 1, Math.PI/2)};
    public static Quat[] side_quatsR = new Quat[]{
        Quat.aroundAxis(-1, 0, 0, 0),
        Quat.aroundAxis(-1, 0, 0, Math.PI),
        Quat.aroundAxis(-1, 0, 0, Math.PI/2),
        Quat.aroundAxis(-1, 0, 0,-Math.PI/2),
        Quat.aroundAxis(0, 0, -1,-Math.PI/2),
        Quat.aroundAxis(0, 0, -1, Math.PI/2)};

    /**
     * Generate models rotated to the other 5 sides of the block
     * @param models An array of 6 models
     * @param side The side of this model
     * @param point The rotation point
     */
    public static void generateSidedModels(CCModel[] models, int side, Vector3 point)
    {
        for(int s = 0; s < 6; s++)
        {
            if(s == side)
                continue;
            
            models[s] = models[side].sidedCopy(side, s, point);
        }
    }
    
    /**
     * Generate models rotated to the other 3 horizontal of the block
     * @param models An array of 4 models
     * @param side The side of this model
     * @param point The rotation point
     */
    public static void generateSidedModelsH(CCModel[] models, int side, Vector3 point)
    {
        for(int s = 2; s < 6; s++)
        {
            if(s == side)
                continue;

            models[s] = models[side].sidedCopy(side, s, point);
        }
    }
    
    /**
     * Generates copies of faces with clockwise vertices
     * @return The model
     */
    public CCModel generateBackface(int srcpos, int destpos, int length)
    {
        if(srcpos+length > destpos)
            throw new IllegalArgumentException("Overlapping src and dest arrays");
        if(srcpos%vp != 0 || destpos%vp != 0 || length%vp != 0)
            throw new IllegalArgumentException("Vertices do not align with polygons");
                
        int[][] o = new int[][]{{0, 0}, {1, vp-1}, {2, vp-2}, {3, vp-3}};
        for(int i = 0; i < length; i++)
        {
            int b = (i/vp)*vp;
            int d = i%vp;
            int di = destpos+b+o[d][1];
            int si = srcpos+b+o[d][0];
            verts[di] = new Vertex5(verts[si]);
            if(normals != null && normals[si] != null)
                normals[di] = normals[si].copy().multiply(-1);
        }
        return this;
    }

    /**
     * Generates sided copies of vertices into this model.
     * Assumes that your model has been generated at vertex side*(numVerts/6)
     */
    public CCModel generateSidedParts(int side, Vector3 point)
    {
        if(verts.length%(6*vp) != 0)
            throw new IllegalArgumentException("Invalid number of vertices for sided part generation");
        int length = verts.length/6;
        
        for(int s = 0; s < 6; s++)
        {
            if(s == side)
                continue;
            
            generateSidedPart(side, s, point, length*side, length*s, length);
        }
        
        return this;
    }

    /**
     * Generates sided copies of vertices into this model.
     * Assumes that your model has been generated at vertex side*(numVerts/4)
     */
    public CCModel generateSidedPartsH(int side, Vector3 point)
    {
        if(verts.length%(4*vp) != 0)
            throw new IllegalArgumentException("Invalid number of vertices for sided part generation");
        int length = verts.length/4;
        
        for(int s = 2; s < 6; s++)
        {
            if(s == side)
                continue;

            generateSidedPart(side, s, point, length*(side-2), length*(s-2), length);
        }
        
        return this;
    }

    /**
     * Generates a sided copy of verts into this model
     */
    public CCModel generateSidedPart(int side1, int side2, Vector3 point, int srcpos, int destpos, int length)
    {
        return generateRotatedPart(side_quatsR[side1].copy().multiply(side_quats[side2]), point, srcpos, destpos, length);
    }

    /**
     * Generates a rotated copy of verts into this model
     */
    public CCModel generateRotatedPart(Quat quat, Vector3 point, int srcpos, int destpos, int length)
    {
        for(int k = 0; k < length; k++)
        {
            verts[destpos+k] = verts[srcpos+k].copy();
            verts[destpos+k].vec
                .subtract(point)
                .rotate(quat)
                .add(point);
        }
        
        if(normals != null)
            for(int k = 0; k < length; k++)
                normals[destpos+k] = normals[srcpos+k].copy().rotate(quat);
        
        return this;
    }

    public static CCModel combine(Collection<CCModel> models)
    {
        if(models.isEmpty())
            return null;
        
        int numVerts = 0;
        int vertexMode = -1;
        for(CCModel model : models)
        {
            if(vertexMode == -1)
                vertexMode = model.vertexMode;
            if(vertexMode != model.vertexMode)
                throw new IllegalArgumentException("Cannot combine models with different vertex modes");
            
            numVerts+=model.verts.length;
        }
        
        CCModel c_model = newModel(vertexMode, numVerts);
        int i = 0;
        for(CCModel model : models)
        {
            copy(model, 0, c_model, i, model.verts.length);
            i+=model.verts.length;
        }
        
        return c_model;
    }

    public CCModel twoFacedCopy()
    {
        CCModel model = newModel(vertexMode, verts.length*2);
        copy(this, 0, model, 0, verts.length);
        model.generateBackface(0, verts.length, verts.length);
        return model;
    }

    public CCModel copy()
    {
        CCModel model = newModel(vertexMode, verts.length);
        copy(this, 0, model, 0, verts.length);
        return model;
    }
    
    /**
     * @return The average of all vertices, for bones.
     */
    public Vector3 collapse()
    {
        Vector3 v = new Vector3();
        for(Vertex5 vert : verts)
            v.add(vert.vec);
        v.multiply(1/(double)verts.length);
        return v;
    }
}
