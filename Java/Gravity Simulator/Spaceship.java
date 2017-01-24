public class Spaceship {
    public double xxPos;
    public double yyPos;
    public double xxVel;
    public double yyVel;
    public double mass;
    public String imgFileName;
    public Spaceship (double xP, double yP, double xV, double yV, double m, String img){
        xxPos = xP;
        yyPos = yP;
        xxVel = xV;
        yyVel = yV;
        mass = m;
        imgFileName = img;
    }
    public double calcDistance(Planet p) {
        double sq_distance = (xxPos - p.xxPos)*(xxPos - p.xxPos) + (yyPos - p.yyPos)*(yyPos - p.yyPos);
        double distance = Math.sqrt(sq_distance);
        return distance;
    }

    public double calcForceExertedBy(Planet p) {
        double distance = calcDistance(p);
        double force = 6.67e-11*mass*p.mass/(distance*distance);
        return force;
    }

    public double calcForceExertedByX(Planet p){
        double distance = calcDistance(p);
        double force = calcForceExertedBy(p);
        double dx = p.xxPos - xxPos;
        double xforce = force*dx/distance;
        return xforce;
    }

    public double calcForceExertedByY(Planet p){
        double distance = calcDistance(p);
        double force = calcForceExertedBy(p);
        double dy = p.yyPos - yyPos;
        double yforce = force*dy/distance;
        return yforce;
    }

    public double calcNetForceExertedByX(Planet[] planets){
        double total_x_force = 0.0;
        for(Planet planet : planets) {
            if(equals(planet) == false){
                total_x_force = total_x_force + calcForceExertedByX(planet);
            }
        }
        return total_x_force;
    }

    public double calcNetForceExertedByY(Planet[] planets){
        double total_y_force = 0.0;
        for(Planet planet : planets) {
            if(equals(planet) == false){
                total_y_force = total_y_force + calcForceExertedByY(planet);
            }
        }
        return total_y_force;
    }

    public void update(double dt, double xforce, double yforce){
        double acc_net_x = xforce/mass;
        double acc_net_y = yforce / mass;
        xxVel = xxVel + dt * acc_net_x;
        yyVel = yyVel + dt * acc_net_y;
        xxPos= xxPos + dt * xxVel;
        yyPos= yyPos + dt * yyVel;
    }
    public void draw() {
    StdDraw.picture(xxPos, yyPos, "./images/" + imgFileName);
    }
}
