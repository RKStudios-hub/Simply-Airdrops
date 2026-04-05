package com.rks.airdrop.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

final class MedicCrateShapeCache {
    private static final ShapeBox[] MODEL_BOXES = {
            new ShapeBox(2.5, 0, -2, 13.5, 3, 20, 0, -90, 0, 8.5, 3, 9),
            new ShapeBox(1.9, 1.6, 15, 2.9, 5.1, 17, 0, -90, 0, 8.5, 3, 9),
            new ShapeBox(2.5, 3, -2, 13.5, 6, 20, 0, -90, 0, 8.5, 3, 9),
            new ShapeBox(2, 3, 7, 3.5, 4, 11, 0, -90, 0, 8.5, 3, 9),
            new ShapeBox(1.9, 1.6, 1, 2.9, 5.1, 3, 0, -90, 0, 8.5, 3, 9)
    };

    private static final Map<Direction, Bounds> SOLID_BOUNDS = createSolidBounds();
    private static final Map<String, VoxelShape> DETAILED_CACHE = new HashMap<>();
    private static final Map<String, VoxelShape> SOLID_CACHE = new HashMap<>();

    private MedicCrateShapeCache() {
    }

    static VoxelShape getDetailedShape(Direction facing, int offsetX, int offsetY, int offsetZ) {
        return DETAILED_CACHE.computeIfAbsent(key(facing, offsetX, offsetY, offsetZ),
                unused -> buildDetailedShape(facing, offsetX, offsetY, offsetZ));
    }

    static VoxelShape getSolidShape(Direction facing, int offsetX, int offsetY, int offsetZ) {
        return SOLID_CACHE.computeIfAbsent(key(facing, offsetX, offsetY, offsetZ),
                unused -> buildSolidShape(facing, offsetX, offsetY, offsetZ));
    }

    private static String key(Direction facing, int offsetX, int offsetY, int offsetZ) {
        return facing.getName() + ":" + offsetX + ":" + offsetY + ":" + offsetZ;
    }

    private static VoxelShape buildDetailedShape(Direction facing, int offsetX, int offsetY, int offsetZ) {
        Bounds cell = new Bounds(offsetX * 16, offsetY * 16, offsetZ * 16,
                offsetX * 16 + 16, offsetY * 16 + 16, offsetZ * 16 + 16);
        VoxelShape shape = Shapes.empty();

        for (ShapeBox box : MODEL_BOXES) {
            Bounds worldBounds = rotateForFacing(box.rotatedBounds(), facing);
            Bounds intersection = intersect(worldBounds, cell);
            if (intersection != null) {
                shape = Shapes.or(shape, Block.box(
                        intersection.minX - cell.minX,
                        intersection.minY - cell.minY,
                        intersection.minZ - cell.minZ,
                        intersection.maxX - cell.minX,
                        intersection.maxY - cell.minY,
                        intersection.maxZ - cell.minZ
                ));
            }
        }

        return shape;
    }

    private static VoxelShape buildSolidShape(Direction facing, int offsetX, int offsetY, int offsetZ) {
        Bounds cell = new Bounds(offsetX * 16, offsetY * 16, offsetZ * 16,
                offsetX * 16 + 16, offsetY * 16 + 16, offsetZ * 16 + 16);
        Bounds intersection = intersect(SOLID_BOUNDS.get(facing), cell);
        if (intersection == null) {
            return Shapes.empty();
        }

        return Block.box(
                intersection.minX - cell.minX,
                intersection.minY - cell.minY,
                intersection.minZ - cell.minZ,
                intersection.maxX - cell.minX,
                intersection.maxY - cell.minY,
                intersection.maxZ - cell.minZ
        );
    }

    private static Map<Direction, Bounds> createSolidBounds() {
        Map<Direction, Bounds> bounds = new EnumMap<>(Direction.class);
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Bounds combined = null;
            for (ShapeBox box : MODEL_BOXES) {
                Bounds rotated = rotateForFacing(box.rotatedBounds(), direction);
                combined = combined == null ? rotated : union(combined, rotated);
            }
            bounds.put(direction, combined);
        }
        return bounds;
    }

    private static Bounds rotateForFacing(Bounds bounds, Direction facing) {
        if (facing == Direction.NORTH) {
            return bounds;
        }

        double[][] corners = corners(bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
        double minX = Double.POSITIVE_INFINITY;
        double minY = Double.POSITIVE_INFINITY;
        double minZ = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double maxZ = Double.NEGATIVE_INFINITY;

        double angle = switch (facing) {
            case EAST -> -90;
            case SOUTH -> 180;
            case WEST -> 90;
            default -> 0;
        };

        for (double[] corner : corners) {
            double[] rotated = rotateCorner(corner[0], corner[1], corner[2], 0, angle, 0, 8, 0, 8);
            minX = Math.min(minX, rotated[0]);
            minY = Math.min(minY, rotated[1]);
            minZ = Math.min(minZ, rotated[2]);
            maxX = Math.max(maxX, rotated[0]);
            maxY = Math.max(maxY, rotated[1]);
            maxZ = Math.max(maxZ, rotated[2]);
        }

        return new Bounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static Bounds intersect(Bounds a, Bounds b) {
        double minX = Math.max(a.minX, b.minX);
        double minY = Math.max(a.minY, b.minY);
        double minZ = Math.max(a.minZ, b.minZ);
        double maxX = Math.min(a.maxX, b.maxX);
        double maxY = Math.min(a.maxY, b.maxY);
        double maxZ = Math.min(a.maxZ, b.maxZ);

        if (minX >= maxX || minY >= maxY || minZ >= maxZ) {
            return null;
        }

        return new Bounds(minX, minY, minZ, maxX, maxY, maxZ);
    }

    private static Bounds union(Bounds a, Bounds b) {
        return new Bounds(
                Math.min(a.minX, b.minX),
                Math.min(a.minY, b.minY),
                Math.min(a.minZ, b.minZ),
                Math.max(a.maxX, b.maxX),
                Math.max(a.maxY, b.maxY),
                Math.max(a.maxZ, b.maxZ)
        );
    }

    private static double[][] corners(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new double[][]{
                {minX, minY, minZ},
                {minX, minY, maxZ},
                {minX, maxY, minZ},
                {minX, maxY, maxZ},
                {maxX, minY, minZ},
                {maxX, minY, maxZ},
                {maxX, maxY, minZ},
                {maxX, maxY, maxZ}
        };
    }

    private static double[] rotateCorner(double x, double y, double z,
                                         double rotX, double rotY, double rotZ,
                                         double originX, double originY, double originZ) {
        double localX = x - originX;
        double localY = y - originY;
        double localZ = z - originZ;

        double[] afterX = rotateAroundX(localX, localY, localZ, rotX);
        double[] afterY = rotateAroundY(afterX[0], afterX[1], afterX[2], rotY);
        double[] afterZ = rotateAroundZ(afterY[0], afterY[1], afterY[2], rotZ);

        return new double[]{afterZ[0] + originX, afterZ[1] + originY, afterZ[2] + originZ};
    }

    private static double[] rotateAroundX(double x, double y, double z, double angleDegrees) {
        if (angleDegrees == 0) {
            return new double[]{x, y, z};
        }
        double radians = Math.toRadians(angleDegrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new double[]{x, y * cos - z * sin, y * sin + z * cos};
    }

    private static double[] rotateAroundY(double x, double y, double z, double angleDegrees) {
        if (angleDegrees == 0) {
            return new double[]{x, y, z};
        }
        double radians = Math.toRadians(angleDegrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new double[]{x * cos + z * sin, y, -x * sin + z * cos};
    }

    private static double[] rotateAroundZ(double x, double y, double z, double angleDegrees) {
        if (angleDegrees == 0) {
            return new double[]{x, y, z};
        }
        double radians = Math.toRadians(angleDegrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        return new double[]{x * cos - y * sin, x * sin + y * cos, z};
    }

    private record Bounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    }

    private static final class ShapeBox {
        private final double minX;
        private final double minY;
        private final double minZ;
        private final double maxX;
        private final double maxY;
        private final double maxZ;
        private final double rotX;
        private final double rotY;
        private final double rotZ;
        private final double originX;
        private final double originY;
        private final double originZ;

        private ShapeBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ,
                         double rotX, double rotY, double rotZ, double originX, double originY, double originZ) {
            this.minX = minX;
            this.minY = minY;
            this.minZ = minZ;
            this.maxX = maxX;
            this.maxY = maxY;
            this.maxZ = maxZ;
            this.rotX = rotX;
            this.rotY = rotY;
            this.rotZ = rotZ;
            this.originX = originX;
            this.originY = originY;
            this.originZ = originZ;
        }

        private Bounds rotatedBounds() {
            double[][] corners = corners(minX, minY, minZ, maxX, maxY, maxZ);
            double outMinX = Double.POSITIVE_INFINITY;
            double outMinY = Double.POSITIVE_INFINITY;
            double outMinZ = Double.POSITIVE_INFINITY;
            double outMaxX = Double.NEGATIVE_INFINITY;
            double outMaxY = Double.NEGATIVE_INFINITY;
            double outMaxZ = Double.NEGATIVE_INFINITY;

            for (double[] corner : corners) {
                double[] rotated = rotateCorner(corner[0], corner[1], corner[2], rotX, rotY, rotZ, originX, originY, originZ);
                outMinX = Math.min(outMinX, rotated[0]);
                outMinY = Math.min(outMinY, rotated[1]);
                outMinZ = Math.min(outMinZ, rotated[2]);
                outMaxX = Math.max(outMaxX, rotated[0]);
                outMaxY = Math.max(outMaxY, rotated[1]);
                outMaxZ = Math.max(outMaxZ, rotated[2]);
            }

            return new Bounds(outMinX, outMinY, outMinZ, outMaxX, outMaxY, outMaxZ);
        }
    }
}
