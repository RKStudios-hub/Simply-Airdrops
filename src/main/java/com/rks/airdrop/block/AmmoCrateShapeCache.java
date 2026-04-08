package com.rks.airdrop.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.HashMap;
import java.util.Map;

final class AmmoCrateShapeCache {
    private static final Bounds[] MODEL_BOXES = {
            new Bounds(-2.505245, 2.5, -5.485361, 2.494755, 3.5, 0.014639),
            new Bounds(-8.505245, 0, -4.985361, 8.494755, 5, 5.014639),
            new Bounds(-6.505245, 0, -5.235361, -5.505245, 5.25, 5.264639),
            new Bounds(5.379505, 0, -5.287421, 6.379505, 5.25, 5.212579)
    };

    private static final Map<String, VoxelShape> DETAILED_CACHE = new HashMap<>();

    private AmmoCrateShapeCache() {
    }

    static VoxelShape getDetailedShape(Direction facing, int offsetX, int offsetY, int offsetZ) {
        return DETAILED_CACHE.computeIfAbsent(key(facing, offsetX, offsetY, offsetZ),
                unused -> buildDetailedShape(facing, offsetX, offsetY, offsetZ));
    }

    private static String key(Direction facing, int offsetX, int offsetY, int offsetZ) {
        return facing.getName() + ":" + offsetX + ":" + offsetY + ":" + offsetZ;
    }

    private static VoxelShape buildDetailedShape(Direction facing, int offsetX, int offsetY, int offsetZ) {
        Bounds cell = new Bounds(offsetX * 16, offsetY * 16, offsetZ * 16,
                offsetX * 16 + 16, offsetY * 16 + 16, offsetZ * 16 + 16);
        VoxelShape shape = Shapes.empty();

        for (Bounds box : MODEL_BOXES) {
            Bounds worldBounds = rotateForFacing(box, facing);
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
            double[] rotated = rotateCorner(corner[0], corner[1], corner[2], angle);
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

    private static double[] rotateCorner(double x, double y, double z, double angleDegrees) {
        double radians = Math.toRadians(angleDegrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double localX = x - 8;
        double localZ = z - 8;
        double rotatedX = localX * cos + localZ * sin;
        double rotatedZ = -localX * sin + localZ * cos;
        return new double[]{rotatedX + 8, y, rotatedZ + 8};
    }

    private record Bounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    }
}
