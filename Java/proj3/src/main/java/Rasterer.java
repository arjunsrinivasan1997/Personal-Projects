import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    // Recommended: QuadTree instance variable. You'll need to make
    //              your own QuadTree since there is no built-in quadtree in Java.
    private String[][] files;
    private ArrayList<QuadTreeNode> filesList;
    private QuadTree images;

    /**
     * imgRoot is the name of the directory containing the images.
     * You may not actually need this for your class.
     */
    public Rasterer(String imgRoot) {
        // YOUR CODE HERE
        QuadTreeNode root = new QuadTreeNode(MapServer.ROOT_ULLON, MapServer.ROOT_ULLAT,
                MapServer.ROOT_LRLON, MapServer.ROOT_LRLAT, "root");
        images = QuadTree.buildTree(root);
    }

    public static void main(String[] args) {
        HashMap<String, Double> test = new HashMap<>();
        test.put("lrlon", -122.24053369025242);
        test.put("ullon", -122.24163047377972);
        test.put("ullat", 37.87655856892288);
        test.put("lrlat", 37.87548268822065);
        test.put("w", 892.0);
        test.put("h", 875.0);
        Rasterer r = new Rasterer("");
        r.getMapRaster(test);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     * grid is referred to as a "tile".
     * <ul>
     * <li>The tiles collected must cover the most longitudinal distance per pixel
     * (LonDPP) possible, while still covering less than or equal to the amount of
     * longitudinal distance per pixel in the query box for the user viewport size. </li>
     * <li>Contains all tiles that intersect the query bounding box that fulfill the
     * above condition.</li>
     * <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     * </p>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified:
     * "render_grid"   -> String[][], the files to display
     * "raster_ul_lon" -> Number, the bounding upper left longitude of the rastered image <br>
     * "raster_ul_lat" -> Number, the bounding upper left latitude of the rastered image <br>
     * "raster_lr_lon" -> Number, the bounding lower right longitude of the rastered image <br>
     * "raster_lr_lat" -> Number, the bounding lower right latitude of the rastered image <br>
     * "depth"         -> Number, the 1-indexed quadtree depth of the nodes of the rastered image.
     * Can also be interpreted as the length of the numbers in the image
     * string. <br>
     * "query_success" -> Boolean, whether the query was able to successfully complete. Don't
     * forget to set this to true! <br>
     * @see #//REQUIRED_RASTER_REQUEST_PARAMS
     */
    public Map<String, Object> getMapRaster(
            Map<String, Double> params) {
        filesList = new ArrayList<>();
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        QuadTreeNode box = new QuadTreeNode(params.get("ullon"), params.get("ullat"),
                params.get("lrlon"), params.get("lrlat"), "desired box");
        box.setWidth(params.get("w"));
        getMapRasterHelper(images.getRoot(), box);
        QuadTreeNode topLeftCorner = findTopLeftCorner(filesList);
        QuadTreeNode bottomRightCorner = findBottomRightCorner(filesList);
        HashSet<Double> uniqueLatitudes = new HashSet<>();
        HashSet<Double> uniqueLongitudes = new HashSet<>();
        for (QuadTreeNode i : filesList) {
            uniqueLatitudes.add(i.getTLY());
            uniqueLongitudes.add(i.getTLX());
        }
        int rows = uniqueLatitudes.size();
        int columns = uniqueLongitudes.size();
        files = new String[rows][columns];
        files[0][0] = topLeftCorner.getName();
        files[rows - 1][columns - 1] = bottomRightCorner.getName();
        double topLeftDy = topLeftCorner.getTLY() - topLeftCorner.getBRY();
        double topLeftDx = topLeftCorner.getBRX() - topLeftCorner.getTLX();
        int depth = topLeftCorner.getName().length();
        for (QuadTreeNode n : filesList) {
            int dy = (int) Math.abs(Math.rint((topLeftCorner.getTLY() - n.getTLY()) / topLeftDy));
            int dx = (int) Math.abs(Math.rint((topLeftCorner.getTLX() - n.getTLX()) / topLeftDx));
            files[dy][dx] = "img/" + n.getName() + ".png";
        }

        results.put("render_grid", files);
        results.put("raster_ul_lon", topLeftCorner.getTLX());
        results.put("raster_ul_lat", topLeftCorner.getTLY());
        results.put("raster_lr_lon", bottomRightCorner.getBRX());
        results.put("raster_lr_lat", bottomRightCorner.getBRY());
        results.put("depth", depth);
        results.put("query_success", true);
        return results;
    }



    /**
     * Finds the QuadTreeNode with the smallest Top right y coordinate and the largest
     * Top y coordinate.
     * This corresponds to the Top Right Corner of
     * of the rastered image
     *
     * @param nodes Array List of QuadTreeNodes
     * @return QuadTreeNode that is the top right corner of the rastered image
     */
    public QuadTreeNode findTopLeftCorner(ArrayList<QuadTreeNode> nodes) {
        QuadTreeNode temp = nodes.get(0);
        for (int i = 1; i < nodes.size(); i++) {
            QuadTreeNode temp1 = nodes.get(i);
            boolean a = temp.getTLX() >= temp1.getTLX();
            boolean b = temp.getTLY() <= temp1.getTLY();
            if (a && b) {
                temp = temp1;
            }
        }
        return temp;

    }


    /**
     * Finds the QuadTreeNode with the largest bottom right X coordinate and the
     * smallest bottom right y coordinate. This corresponds to the bottom right corner
     * of the rastered image.
     *
     * @param nodes ArrayList of QuadTreeNodes
     * @return QuadTreeNode that is the bottom right corner of the rastered image
     */
    public QuadTreeNode findBottomRightCorner(ArrayList<QuadTreeNode> nodes) {
        QuadTreeNode temp = nodes.get(0);
        for (int i = 1; i < nodes.size(); i++) {
            QuadTreeNode temp1 = nodes.get(i);
            boolean a = temp.getTLX() <= temp1.getTLX();
            boolean b = temp.getTLY() >= temp1.getTLY();
            if (a && b) {
                temp = temp1;
            }
        }
        return temp;
    }

    /**
     * Recursive helper, for get mapRaster that checks if QuadTreeNode n intersects the
     * desired area to be rastered,
     * and if the LonDPP is precise enough.
     * Based off of this information,
     *
     * @param n   QuadTreeNode
     * @param box QuadTreeNode that represents the area that needs to be rastered.
     */
    public void getMapRasterHelper(QuadTreeNode n, QuadTreeNode box) {
        boolean intersects = intersectsTiles(n, box);
        boolean correctLonDPP = lonDPPsmallerThanOrIsLeaf(box.getLonDPP(), n);
        if (intersects && correctLonDPP) {
            filesList.add(n);
        } else if (intersects && !correctLonDPP) {
            getMapRasterHelper(n.getTL(), box);
            getMapRasterHelper(n.getTR(), box);
            getMapRasterHelper(n.getBL(), box);
            getMapRasterHelper(n.getBR(), box);
        }
    }

    /**
     * Determines if the given quadtree node has a lonDPP <= the desired lonDPP
     *
     * @param desiredLonDPP desired lonDPP
     * @param n             quadtree node to be compared to desired lonDPP
     * @return boolean true or false
     */
    public boolean lonDPPsmallerThanOrIsLeaf(double desiredLonDPP, QuadTreeNode n) {
        if (n.name.length() == 7) {
            return true;
        } else {
            return n.getLonDPP() <= desiredLonDPP;
        }
    }

    /**
     * Determines if two quadtreenodes overlap in the coordinate plane
     *
     * @param x Quadtree Node
     * @param y QuadTree node
     * @return true if the quadtree nodes overlap, false otherwise
     */
    public boolean intersectsTiles(QuadTreeNode x, QuadTreeNode y) {
        boolean a = x.getTLX() < y.getBRX();
        boolean b = x.getBRX() > y.getTLX();
        boolean c = x.getTLY() > y.getBRY();
        boolean d = x.getBRY() < y.getTLY();
        return a && b && c && d;
    }
}
