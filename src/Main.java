import java.util.Random;

public class Main {
    public static void main(String[] args)  {
        int[] array = new int[10000000];

        Random random = new Random();


        for(int i = 0; i < array.length; i++){
            array[i] = random.nextInt(1000);
        }

        int randomIndex = random.nextInt(array.length);
        array[randomIndex] = -1;

        /*for (int i = 0; i < array.length; i++){
            System.out.print(array[i] + " ");
        }
        System.out.println();*/

        int numThreads = 4; // <-

        int chunkSize = array.length / numThreads;
        int remaining = array.length % numThreads;
        int startIndex = 0;

        for (int i = 0; i < numThreads; i++) {
            int endIndex = startIndex + chunkSize - 1;
            if (remaining > 0) {
                endIndex++;
                remaining--;
            }
            new Thread(new MinFinderThread(startIndex, endIndex, array, numThreads)).start();
            startIndex = endIndex + 1;
        }

        MinFinderThread.waitUntilFinished();
        System.out.println(MinFinderThread.getMinIndex() + " " + MinFinderThread.getMin());
    }
}