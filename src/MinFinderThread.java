public class MinFinderThread implements Runnable {
    private int[] array;
    private int start;
    private int end;
    private static int min = Integer.MAX_VALUE;
    private static int minIndex = -1;
    private static int completedThreads = 0;
    private static int totalThreads;

    private static Object lock = new Object();
    private static Object completeLock = new Object();

    public MinFinderThread(int start, int end, int[] array, int totalThread) {
          this.start = start;
          this.end = end;
          this.array = array;
          totalThreads = totalThread;
    }

    @Override
    public void run() {
        int localMin = Integer.MAX_VALUE;
        int localMinIndex = -1;
        for(int i = start; i <= end; i++){
            if(array[i] < localMin){
                localMin = array[i];
                localMinIndex = i;
            }
        }
        synchronized (lock){
            if(min > localMin){
                min = localMin;
                minIndex = localMinIndex;
            }
        }
        synchronized (completeLock) {
            completedThreads++;
            if (completedThreads == totalThreads) {
                completeLock.notifyAll();
            }
        }
    }

    public static void waitUntilFinished() {
        synchronized (completeLock) {
            while (completedThreads < totalThreads) {
                try {
                    completeLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
    public static int getMin() {
        return min;
    }
    public static  int getMinIndex(){
        return minIndex;
    }
}
