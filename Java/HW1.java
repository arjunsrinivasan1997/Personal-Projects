public class HW1{
  public static int max(int[] m) {
      int value = m[0];
      for(int i =1;i<m.length;i++){
        if (value<m[i]){
          value =m[i];
        }
      }
      return value;
  }
  public static int min(int[] m){
    int value = m[0];
    for(int i =1;i<m.length;i++){
      if (value>m[i]){
        value = m[i];
      }
    }
    return value;
  }
  public static void main(String[] args) {
     int[] numbers = new int[]{9, 2, 15, 2, 22, 10, 6};
     System.out.println(max(numbers));
     System.out.println(min(numbers));
  }
}
