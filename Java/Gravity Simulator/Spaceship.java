import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Spaceship extends Planet  {
    public Spaceship (double xP, double yP, double xV, double yV, double m, String img) {
        super(xP,yP,xV,yV,m,img);
    }
    public void update(double dt, double xforce, double yforce){
        EventHandler eh = new EventHandler();
        Spaceship.addKeyListener(eh);  
        double acc_net_x = xforce/mass;
        double acc_net_y = yforce / mass;
        xxVel = xxVel + dt * acc_net_x;
        yyVel = yyVel + dt * acc_net_y;
        xxPos= xxPos + dt * xxVel;
        yyPos= yyPos + dt * yyVel;
    }
    /**public void keyReleased(KeyEvent e) {};
    public void keyPressed(KeyEvent e {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
            System.out.println("Left");
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT)
            System.out.println("Right");
        else if (e.getKeyCode() == KeyEvent.VK_UP)
            System.out.println("UP");
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
            System.out.println("Down");
    }
    public void keyTyped(KeyEvent e) {}*/

}
