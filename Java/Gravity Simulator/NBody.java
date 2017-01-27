public class NBody {
  public static double readRadius(String filename){
    In file = new In(filename);
    double planet_number = file.readDouble();
    double radius = file.readDouble();
    return radius;
  }
  public static Planet[] readPlanetsAndSpaceShip(String filename) {
    In file = new In(filename);
    int planet_number = file.readInt();
    double advance = file.readDouble();
    //Above lines advances file reader past radius of galaxy
    Planet[] planets = new Planet[planet_number];
    planets[0]=new Spaceship(file.readDouble(), file.readDouble(), file.readDouble(),
                file.readDouble(), file.readDouble(), file.readString());
    for (int i = 1; i < planet_number; i++) {
      planets[i] = new Planet(file.readDouble(), file.readDouble(), file.readDouble(),
                  file.readDouble(), file.readDouble(), file.readString());
    }
    return planets;
}
  public static void main(String[] args) {
    double T = Double.parseDouble(args[0]);
    double dt = Double.parseDouble(args[1]);
    String filename = args[2];

    double radius = readRadius(filename);
    Planet[] planets = readPlanetsAndSpaceShip(filename);
    StdDraw.setScale(-radius, radius);
    StdDraw.picture(0,0,"./images/starfield.jpg");
    for (Planet p : planets) {
      p.draw();
    }
    for (double time = 0; time <= T; time += dt) {
        double[] xForces = new double[planets.length];
        double[] yForces = new double[planets.length];
        for(int i = 0; i < planets.length; i++) {
            xForces[i] = planets[i].calcNetForceExertedByX(planets);
            yForces[i] = planets[i].calcNetForceExertedByY(planets);
        }

        for(int i = 0; i < planets.length; i++) {
            planets[i].update(dt, xForces[i], yForces[i]);
            planets[i].draw();
        }
        StdDraw.show(10);
    }
    StdOut.printf("%d\n", planets.length);
    StdOut.printf("%.2e\n", radius);
    for (int i = 0; i < planets.length; i++) {
        StdOut.printf("%11.4e %11.4e %11.4e %11.4e %11.4e %12s\n",
            planets[i].xxPos, planets[i].yyPos, planets[i].xxVel,
            planets[i].yyVel, planets[i].mass, planets[i].imgFileName);
    }
    }
}
