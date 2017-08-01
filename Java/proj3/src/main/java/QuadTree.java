/** Implementation of a Quadtree.
 * Created by Arjun Srinivasan on 4/11/17.
 */

public class QuadTree {
    private QuadTreeNode root;
    public QuadTree(QuadTreeNode root) {
        this.root = root;
    }


    public static QuadTree buildTree(QuadTreeNode root) {
        QuadTree q = new QuadTree(root);
        QuadTreeNode TL,TR,BL,BR;
        if(root.name.length() == 7 && !root.name.equals("root")){
            return q;
        } else {
            if (root.name.equals("root")){
                ;
                 TL = new QuadTreeNode(root.getTLX(),root.getTLY(),root.getX(),root.getY(),"1");
                 TR = new QuadTreeNode(root.getX(),root.getTLY(),root.getBRX(),root.getY(),"2");
                 BL = new QuadTreeNode(root.getTLX(),root.getY(),root.getX(),root.getBRY(),"3");
                 BR = new QuadTreeNode(root.getX(),root.getY(),root.getBRX(),root.getBRY(),"4");
            } else {
                 TL = new QuadTreeNode(root.getTLX(),root.getTLY(),root.getX(),root.getY(),root.getName()+"1");
                 TR = new QuadTreeNode(root.getX(),root.getTLY(),root.getBRX(),root.getY(),root.getName()+"2");
                 BL = new QuadTreeNode(root.getTLX(),root.getY(),root.getX(),root.getBRY(),root.getName()+"3");
                 BR = new QuadTreeNode(root.getX(),root.getY(),root.getBRX(),root.getBRY(),root.getName()+"4");
            }
            root.setTL(TL);
            root.setTR(TR);
            root.setBL(BL);
            root.setBR(BR);
            /*
            q.insert(TL);
            q.insert(TR);
            q.insert(BL);
            q.insert(BR);*/
            buildTree(root.getTL());
            buildTree(root.getTR());
            buildTree(root.getBL());
            buildTree(root.getBR());
        }
        return q;

    }

    /** Inserts a new node with x-coordinate x and y coordinate y into the quadtree
     */
    public void insert(QuadTreeNode b) {
        insertHelper(root,b);
    }
    public void insertHelper(QuadTreeNode n,QuadTreeNode b){
        switch (compare(n,b.getX(),b.getY())) {
            case 1:
                if (n.TL == null) {
                    n.TL = b;
                } else {
                    insertHelper(n.TL,b);
                }
                break;
            case 2:
                if (n.TR == null) {
                    n.TR = b;
                } else {
                    insertHelper(n.TR,b);
                }
                break;
            case 3:
                if (n.BL == null) {
                    n.BL = b;
                } else {
                    insertHelper(n.BL,b);
                }
                break;
            case 4:
                if (n.BR== null) {
                    n.BR = b;
                } else {
                    insertHelper(n.BR,b);
                }
                break;
        }
    }

    public QuadTreeNode getRoot() {
        return root;
    }

    private int compare(QuadTreeNode current, double x, double y) {
        if (x < current.getX() && y > current.getY()) {
            return 1;
        } else if ( x > current.getX() && y > current.getY()) {
            return 2;
        } else if ( x < current.getX() && y < current.getY()) {
            return 3;
        } else if ( x > current.getX() && y < current.getY()) {
            return 4;
        }
        throw new IllegalArgumentException("ERROR: Could not compare node to given coordinates");
    }
}