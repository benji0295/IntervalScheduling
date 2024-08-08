import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.TreeSet;

public class IntervalScheduling {

    // DO NOT MODIFY
    public static class Interval {
        public int start;
        public int end;
        public int duration;
        public int deadline;
        public int weight;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
            this.duration = end-start;
            this.weight = 1;
            this.deadline = -1;
        }

        public Interval(int start, int end, int weight) {
            this.start = start;
            this.end = end;
            this.duration = end-start;
            this.weight = weight;
            this.deadline = -1;
        }

        public Interval(int start, int end, int duration, int deadline) {
            this.start = -1;
            this.end = -1;
            this.duration = duration;
            this.weight = -1;
            this.deadline = deadline;
        }

        public String toString() {
            String result = new String();
            if (start == -1 || end == -1) {
                result = "{duration: " + this.duration + ", deadline: " + this.deadline + "}";
            } else {
                result = "{start: " + this.start + ", end: " + this.end + ", weight: " + this.weight + "}";
            }
            return result;
        }
    }

    public static class StartTimeSorter implements Comparator<Interval> {
        public int compare(Interval i1, Interval i2) {
            return i1.start - i2.start;
        }
    }

    public static class EndTimeSorter implements Comparator<Interval> {
        public int compare(Interval i1, Interval i2) {
            return i1.end - i2.end;
        }
    }

    // DO NOT MODIFY
    private static ArrayList<Interval> createUnweightedSamples() {
        ArrayList<Interval> result = new ArrayList<Interval>();
        result.add(new Interval(0, 6));
        result.add(new Interval(1, 4));
        result.add(new Interval(3, 5));
        result.add(new Interval(3, 8));
        result.add(new Interval(4, 7));
        result.add(new Interval(5, 9));
        result.add(new Interval(6, 10));
        result.add(new Interval(8, 11));
        return result;
    }

    // DO NOT MODIFY
    private static ArrayList<Interval> createWeightedSamples() {
        ArrayList<Interval> result = new ArrayList<Interval>();
        result.add(new Interval(1, 4, 8));
        result.add(new Interval(3, 5, 7));
        result.add(new Interval(0, 6, 6));
        result.add(new Interval(4, 7, 5));
        result.add(new Interval(3, 8, 4));
        result.add(new Interval(5, 9, 3));
        result.add(new Interval(6, 10, 2));
        result.add(new Interval(8, 11, 1));
        return result;
    }

    // DO NOT MODIFY
    private static ArrayList<Interval> createJobs() {
        ArrayList<Interval> result = new ArrayList<Interval>();
        result.add(new Interval(-1, -1, 3, 6));
        result.add(new Interval(-1, -1, 2, 8));
        result.add(new Interval(-1, -1, 1, 9));
        result.add(new Interval(-1, -1, 4, 9));
        result.add(new Interval(-1, -1, 3, 14));
        result.add(new Interval(-1, -1, 2, 15));
        return result;
    }

    public static boolean isCompatible(Interval i1, Interval i2) {
        if (i1 == null) return true;
        return (i2.start >= i1.end);
    }

    public static ArrayList<Interval> getOptimalUnweightedSchedule(ArrayList<Interval> intervals) {
        // WRITE CODE HERE
        intervals.sort(new EndTimeSorter());

        ArrayList<Interval> optimalSchedule = new ArrayList<>();
        Interval lastAddedInterval = null;

        for (Interval interval : intervals) {
            if (lastAddedInterval == null || isCompatible(lastAddedInterval, interval)) {
                optimalSchedule.add(interval);
                lastAddedInterval = interval;
            }
        }

        return optimalSchedule;
    }

    public static ArrayList<Interval> getOptimalWeightedSchedule(ArrayList<Interval> intervals) {
        // WRITE CODE HERE
        intervals.sort(new EndTimeSorter());

        int n = intervals.size();
        int[] p = new int[n];
        int[] OPT = new int[n + 1];


        for (int i = 0; i < n; i++) {
            p[i] = 0;
            for (int j = i - 1; j >= 0; j--) {
                if (isCompatible(intervals.get(j), intervals.get(i))) {
                    p[i] = j + 1;
                    break;
                }
            }
        }

        // Iterative-Compute-OPT
        OPT[0] = 0;
        for (int j = 1; j <= n; j++) {
            int weightWithCurrent = intervals.get(j - 1).weight + OPT[p[j - 1]];
            int weightWithoutCurrent = OPT[j - 1];
            OPT[j] = Math.max(weightWithCurrent, weightWithoutCurrent);
        }

        ArrayList<Interval> optimalSchedule = new ArrayList<>();
        int i = n;
        while (i > 0) {
            if (OPT[i] != OPT[i - 1]) {
                optimalSchedule.add(intervals.get(i - 1));
                i = p[i - 1];
            } else {
                i--;
            }
        }
        Collections.reverse(optimalSchedule);

        return optimalSchedule;
    }

    public static void main (String[] args) {
        System.out.println("===== Unweighted: ");
        System.out.println(getOptimalUnweightedSchedule(createUnweightedSamples()));

        System.out.println("===== Weighted: ");
        System.out.println(getOptimalWeightedSchedule(createWeightedSamples()));
    }

}
