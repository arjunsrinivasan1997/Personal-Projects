/**
 * Created by arjunsrinivasan on 2/12/17.
 */
public class Animal {
    public void speak(Animal a){
        System.out.println("Animal speaks");
    }
    public static void main(String[] args){
        Animal a = new Animal();
        Animal d = new Dog();
        Dog d1= new Dog();
        d.speak(d);
        d.speak(d1);
        System.out.println( d instanceof Dog);

    }
}
