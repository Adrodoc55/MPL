package de.adrodoc55.minecraft;

import java.util.ArrayList;
import java.util.List;

public class Coordinate3D {

    public static final Coordinate3D SELF = new Coordinate3D(0, 0, 0);
    public static final Coordinate3D UP = new Coordinate3D(0, 1, 0);
    public static final Coordinate3D DOWN = new Coordinate3D(0, -1, 0);
    public static final Coordinate3D NORTH = new Coordinate3D(0, 0, -1);
    public static final Coordinate3D SOUTH = new Coordinate3D(0, 0, 1);
    public static final Coordinate3D WEST = new Coordinate3D(-1, 0, 0);
    public static final Coordinate3D EAST = new Coordinate3D(1, 0, 0);

    public static enum Direction {
        UP(Coordinate3D.UP), DOWN(Coordinate3D.DOWN), NORTH(Coordinate3D.NORTH), SOUTH(
                Coordinate3D.SOUTH), WEST(Coordinate3D.WEST), EAST(
                Coordinate3D.EAST);

        public static Direction valueOf(Coordinate3D coordinate) {
            if (coordinate == null) {
                throw new NullPointerException("coordinate is null");
            }
            for (Direction direction : values()) {
                if (coordinate.equals(direction.toCoordinate())) {
                    return direction;
                }
            }
            throw new IllegalArgumentException(
                    "No enum constant for coordinate" + coordinate);
        }

        private final Coordinate3D relative;

        private Direction(Coordinate3D relative) {
            this.relative = relative;
        }

        public Coordinate3D toCoordinate() {
            return relative;
        }
    }

    private final int x;
    private final int y;
    private final int z;

    public Coordinate3D() {
        this(0, 0, 0);
    }

    public Coordinate3D(Coordinate3D other) {
        this(other.x, other.y, other.z);
    }

    public Coordinate3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Coordinate3D copy() {
        return new Coordinate3D(this);
    }

    public Coordinate3D plus(Coordinate3D other) {
        int x = overflowSaveAddition(this.x, other.x);
        int y = overflowSaveAddition(this.y, other.y);
        int z = overflowSaveAddition(this.z, other.z);
        return new Coordinate3D(x, y, z);
    }

    public Coordinate3D minus(Coordinate3D other) {
        int x = overflowSaveSubstraction(this.x, other.x);
        int y = overflowSaveSubstraction(this.y, other.y);
        int z = overflowSaveSubstraction(this.z, other.z);
        return new Coordinate3D(x, y, z);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Coordinate3D other = (Coordinate3D) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Coordinate3D [x=" + x + ", y=" + y + ", z=" + z + "]";
    }

    public String toAbsoluteString() {
        return x + " " + y + " " + z;
    }

    public String toRelativeString() {
        return "~" + x + " ~" + y + " ~" + z;
    }

    public static int overflowSaveAddition(int a, int b) {
        int c;
        try {
            c = Math.addExact(a, b);
        } catch (ArithmeticException ex) {
            if (a < 0) {
                c = Integer.MIN_VALUE;
            } else {
                c = Integer.MAX_VALUE;
            }
        }
        return c;
    }

    public static int overflowSaveSubstraction(int a, int b) {
        return overflowSaveAddition(a, b * -1);
    }

    public List<Coordinate3D> getAdjacent() {
        List<Coordinate3D> directions = getDirections();
        List<Coordinate3D> possibleCoodinates = new ArrayList<Coordinate3D>(
                directions.size());
        for (Coordinate3D d : directions) {
            Coordinate3D next = plus(d);
            possibleCoodinates.add(next);
        }
        return possibleCoodinates;
    }

    private static final ArrayList<Coordinate3D> directions = new ArrayList<Coordinate3D>(
            6);
    static {
        directions.add(UP);
        directions.add(DOWN);
//        directions.add(NORTH);
//        directions.add(SOUTH);
        directions.add(WEST);
        directions.add(EAST);
    }

    private static List<Coordinate3D> getDirections() {
        return directions;
    }

}
