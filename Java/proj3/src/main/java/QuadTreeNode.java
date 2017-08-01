/**
 * Created by Arjun Srinivasan on 4/12/17.
 */
public class QuadTreeNode implements Comparable<QuadTreeNode>{
    private double TLX;
    private double TLY;
    private double BRX;
    private double BRY;
    private double x;
    private double y;
    private double lonDPP;
    private double width = 256;
    QuadTreeNode TL,TR,BL,BR;

    public void setTL(QuadTreeNode TL) {
        this.TL = TL;
    }

    public void setTR(QuadTreeNode TR) {
        this.TR = TR;
    }

    public void setBL(QuadTreeNode BL) {
        this.BL = BL;
    }

    public void setBR(QuadTreeNode BR) {
        this.BR = BR;
    }

    public void setWidth(double width) {
        this.width = width;
        lonDPP = (BRX-TLX) / this.width;
    }

    public String getName() {
        return name;
    }

    String name;
    public QuadTreeNode(double topLeftX, double topLeftY, double bottomRightX, double bottomRightY, String name) {
        TLX = topLeftX;
        TLY = topLeftY;
        BRX = bottomRightX;
        BRY = bottomRightY;
        x = (TLX + BRX) / 2;
        y = (BRY + TLY) / 2;
        this.name = name;
        lonDPP = (BRX-TLX) / width;
    }
    public double getTLX() {
        return TLX;
    }

    public double getTLY() {
        return TLY;
    }

    public double getBRX() {
        return BRX;
    }

    public double getBRY() {
        return BRY;
    }

    public double getX() {
        return x;
    }

    public QuadTreeNode getTL() {
        return TL;
    }

    public QuadTreeNode getTR() {
        return TR;
    }

    public QuadTreeNode getBL() {
        return BL;
    }

    public QuadTreeNode getBR() {
        return BR;
    }

    public double getLonDPP() {
        return lonDPP;
    }

    public double getY() {
        return y;
    }



    @Override
    public int compareTo(QuadTreeNode t){
        if (t.getTLX() < getTLX()) {
            return 1;
        } else if (t.getTLX() > getTLX()) {
            return -1;
        } else {
            return 0;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuadTreeNode that = (QuadTreeNode) o;

        if (Double.compare(that.TLX, TLX) != 0) return false;
        if (Double.compare(that.TLY, TLY) != 0) return false;
        if (Double.compare(that.BRX, BRX) != 0) return false;
        if (Double.compare(that.BRY, BRY) != 0) return false;
        return Double.compare(that.width, width) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(TLX);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(TLY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(BRX);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(BRY);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QuadTreeNode{");
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        sb.append("TLX=").append(TLX);
        sb.append(", TLY=").append(TLY);
        sb.append(", BRX=").append(BRX);
        sb.append(", BRY=").append(BRY);
        sb.append(", x=").append(x);
        sb.append(", y=").append(y);
        sb.append(", lonDPP=").append(lonDPP);
        sb.append(", width=").append(width);
        sb.append(", TL=").append(TL);
        sb.append(", TR=").append(TR);
        sb.append(", BL=").append(BL);
        sb.append(", BR=").append(BR);

        return sb.toString();
    }
}
