package midiApp.midi;

public class DpcPreset implements Comparable<DpcPreset> {
    private int allLoops = 0;

    public DpcPreset(int[] arrayRepresentation) {
        for (int i = 0; i < 8; i++) {
            allLoops |= arrayRepresentation[i] << i;
        }
    }

    public int[] getArrayRepresentation() {
        int[] a = new int[8];
        for (int i = 0; i < 8; i++) {
            a[i] = (allLoops >> i) % 2;
        }
        return a;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        DpcPreset dpcPreset = (DpcPreset) o;

        return allLoops == dpcPreset.allLoops;

    }

    @Override
    public int hashCode() {
        return allLoops;
    }

    @Override
    public String toString() {
        String s = Integer.toBinaryString(allLoops);
        while (s.length() < 8) {
            s = "0" + s;
        }
        return s;
    }

    @Override
    public int compareTo(DpcPreset o) {
        return Integer.compare(allLoops, o.allLoops);
    }
}