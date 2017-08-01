/**
 * Created by Arjun Srinivasan on 4/11/17.
 */
import org.junit.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.Assert.*;

public class Proj3Tester {

    @Test
    public void testQuadTree(){
        QuadTree q = new QuadTree(new QuadTreeNode(10,30,30,10,"root"));
        q.insert(new QuadTreeNode(10,30,20,20,""));
        q.insert(new QuadTreeNode(10,20,20,10,""));
        q.insert(new QuadTreeNode(20,20,30,10,""));
        q.insert(new QuadTreeNode(20,30,30,20,""));
        q.insert(new QuadTreeNode(0,0,0,0,""));
        QuadTree q1 = QuadTree.buildTree(new QuadTreeNode(MapServer.ROOT_ULLON,MapServer.ROOT_ULLAT,MapServer.ROOT_LRLON,MapServer.ROOT_LRLAT,"root"));
    }
    @Test
    public void testRaster(){
        Rasterer r = new Rasterer("");
        //test.html
        Map<String,Object> result = r.getMapRaster(makeParamMap(-122.24053369025242, -122.24163047377972, 892.0, 875.0, 37.87655856892288, 37.87548268822065));
        String[][] test = {{"img/2143411.png", "img/2143412.png", "img/2143421.png"}, {"img/2143413.png", "img/2143414.png", "img/2143423.png"}, {"img/2143431.png", "img/2143432.png", "img/2143441.png"}};
        testResults(-122.24212646484375, 7, -122.24006652832031, 37.87538940251607, test, 37.87701580361881, true,result);
        //test1234.html
        result = r.getMapRaster(makeParamMap(-122.20908713544797, -122.3027284165759, 305.0, 300.0, 37.88708748276975, 37.848731523430196));
        String[][] a = new String[][]{{"img/1.png", "img/2.png"}, {"img/3.png","img/4.png"}};
        testResults(-122.2998046875, 1, -122.2119140625, 37.82280243352756, new String[][]{{"img/1.png", "img/2.png"}, {"img/3.png", "img/4.png"}}, 37.892195547244356,  true,result);





    }
    public Map<String, Double> makeParamMap(double lrlon,double ullon, double w, double h, double ullat, double lrlat ){
        HashMap<String,Double> temp = new HashMap<>();
        temp.put("lrlon",lrlon);
        temp.put("ullon",ullon);
        temp.put("ullat",ullat);
        temp.put("lrlat",lrlat);
        temp.put("w",w);
        temp.put("h",h);
        return temp;
    }

    public void testResults(double raster_ul_lon, double depth, double raster_lr_lon, double raster_lr_lat, String[][] render_grid,double raster_ul_lat, boolean query_success, Map<String, Object> results) {
        assertEquals("Checking Upper Left Longitude(x)",raster_ul_lon,results.get("raster_ul_lon"));
        assertEquals("Checking Upper Left Latitude(y)",raster_ul_lat,results.get("raster_ul_lat"));
        assertEquals("Checking Bottom Right Longitude(x)",raster_lr_lon,results.get("raster_lr_lon"));
        assertEquals("Checking Bottom Right Longitude(x)",raster_lr_lat,results.get("raster_lr_lat"));
        assertEquals("Checking query success",query_success,results.get("query_success"));
        assertArrayEquals("Checking String array of files names",render_grid,(String[][])results.get("render_grid"));




    }


    @Test
    public void testQuadTreeNode(){
        QuadTreeNode p = new QuadTreeNode(10,30,30,10,"");
        QuadTreeNode p5 = new QuadTreeNode(10,30,30,10,"");

        QuadTreeNode p1 = new QuadTreeNode(10,30,20,20,"");
        QuadTreeNode p2 = new QuadTreeNode(10,20,20,10,"");
        QuadTreeNode p3 = new QuadTreeNode(20,20,30,10,"");
        QuadTreeNode p4 = new QuadTreeNode(20,30,30,20,"");
        assertEquals(p.getX(),20.0,0);
        assertEquals(p.getY(),20.0,0);
        assertEquals(p1.getX(),15.0,0);
        assertEquals(p1.getY(),25.0,0);
        assertEquals(p2.getX(),15,0);
        assertEquals(p2.getY(),15,0);
        assertEquals(p3.getX(),25,0);
        assertEquals(p3.getY(),15,0);
        assertEquals(p4.getX(),25,0);
        assertEquals(p4.getX(),25,0);
        assertTrue(p.equals(p));
        assertFalse(p.equals(p1));
        assertTrue(p.equals(p5));

    }
}
