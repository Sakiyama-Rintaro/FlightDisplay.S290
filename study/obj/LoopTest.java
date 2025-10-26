public class LoopTest {
    public static void main(String[] args) {
        int i = 0;
        int j = 0, k = 0;
        ++i;
        while (i < 4) {
            i++;
            

            switch (k % 2) {
                case 0:
                    j = 1;
                    break;
                case 1:
                    j = 2;
                    break;
            }
            k++;

            System.out.print(i + " " + j + ", ");
        }
    }
}
