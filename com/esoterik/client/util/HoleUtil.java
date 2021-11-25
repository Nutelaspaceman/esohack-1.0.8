/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.EntityPlayer
 *  net.minecraft.util.math.BlockPos
 */
package com.esoterik.client.util;

import com.esoterik.client.util.BlockUtil;
import com.esoterik.client.util.Hole;
import com.esoterik.client.util.MixinInterface;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public class HoleUtil
implements MixinInterface {
    public static boolean isInHole(EntityPlayer entityPlayer) {
        BlockPos blockPos = new BlockPos(Math.floor(entityPlayer.field_70165_t), Math.floor(entityPlayer.field_70163_u), Math.floor(entityPlayer.field_70161_v));
        if (BlockUtil.getBlockResistance(blockPos.func_177977_b()) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.func_177984_a()) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos) != BlockUtil.BlockResistance.Blank) {
            return false;
        }
        BlockPos[] touchingBlocks = new BlockPos[]{blockPos.func_177978_c(), blockPos.func_177968_d(), blockPos.func_177974_f(), blockPos.func_177976_e()};
        int validHorizontalBlocks = 0;
        for (BlockPos touching : touchingBlocks) {
            if (BlockUtil.getBlockResistance(touching) == BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(touching) != BlockUtil.BlockResistance.Resistant) continue;
            ++validHorizontalBlocks;
        }
        return validHorizontalBlocks >= 4;
    }

    public static boolean isVoidHole(BlockPos blockPos) {
        return HoleUtil.mc.field_71439_g.field_71093_bK == -1 ? (blockPos.func_177956_o() == 0 || blockPos.func_177956_o() == 127) && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank : blockPos.func_177956_o() == 0 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank;
    }

    public static List<Hole> getHoles(double range, boolean doubles) {
        ArrayList<Hole> holeBlocks = new ArrayList<Hole>();
        for (BlockPos blockPos : BlockUtil.getNearbyBlocks((EntityPlayer)HoleUtil.mc.field_71439_g, range, false)) {
            BlockPos doubleBlock = BlockPos.field_177992_a;
            Facing facing = null;
            int obsidianSides = 0;
            int bedrockSides = 0;
            int airSides = 0;
            if (BlockUtil.getBlockResistance(blockPos.func_177977_b()) == BlockUtil.BlockResistance.Unbreakable) {
                ++bedrockSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177977_b()) == BlockUtil.BlockResistance.Resistant) {
                ++obsidianSides;
            }
            if (BlockUtil.getBlockResistance(blockPos.func_177974_f()) == BlockUtil.BlockResistance.Unbreakable) {
                ++bedrockSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177974_f()) == BlockUtil.BlockResistance.Resistant) {
                ++obsidianSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177974_f()) == BlockUtil.BlockResistance.Blank) {
                ++airSides;
                if (doubleBlock == BlockPos.field_177992_a) {
                    doubleBlock = blockPos.func_177974_f();
                }
                if (facing == null) {
                    facing = Facing.East;
                }
            }
            if (BlockUtil.getBlockResistance(blockPos.func_177968_d()) == BlockUtil.BlockResistance.Unbreakable) {
                ++bedrockSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177968_d()) == BlockUtil.BlockResistance.Resistant) {
                ++obsidianSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177968_d()) == BlockUtil.BlockResistance.Blank) {
                ++airSides;
                if (doubleBlock == BlockPos.field_177992_a) {
                    doubleBlock = blockPos.func_177968_d();
                }
                if (facing == null) {
                    facing = Facing.South;
                }
            }
            if (BlockUtil.getBlockResistance(blockPos.func_177976_e()) == BlockUtil.BlockResistance.Unbreakable) {
                ++bedrockSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177976_e()) == BlockUtil.BlockResistance.Resistant) {
                ++obsidianSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177976_e()) == BlockUtil.BlockResistance.Blank) {
                ++airSides;
                if (doubleBlock == BlockPos.field_177992_a) {
                    doubleBlock = blockPos.func_177976_e();
                }
                if (facing == null) {
                    facing = Facing.West;
                }
            }
            if (BlockUtil.getBlockResistance(blockPos.func_177978_c()) == BlockUtil.BlockResistance.Unbreakable) {
                ++bedrockSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177978_c()) == BlockUtil.BlockResistance.Resistant) {
                ++obsidianSides;
            } else if (BlockUtil.getBlockResistance(blockPos.func_177978_c()) == BlockUtil.BlockResistance.Blank) {
                ++airSides;
                if (doubleBlock == BlockPos.field_177992_a) {
                    doubleBlock = blockPos.func_177978_c();
                }
                if (facing == null) {
                    facing = Facing.North;
                }
            }
            if (bedrockSides == 5 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank) {
                holeBlocks.add(new Hole(blockPos, Hole.Type.Bedrock));
            }
            if (obsidianSides == 5 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank) {
                holeBlocks.add(new Hole(blockPos, Hole.Type.Obsidian));
            }
            if (bedrockSides + obsidianSides == 5 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank) {
                holeBlocks.add(new Hole(blockPos, Hole.Type.Obsidian));
            }
            if (!doubles) continue;
            if (airSides == 1 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank && HoleUtil.checkColliding(doubleBlock, facing, BlockUtil.BlockResistance.Resistant) && BlockUtil.getBlockResistance(doubleBlock) == BlockUtil.BlockResistance.Blank) {
                holeBlocks.add(new Hole(doubleBlock, Hole.Type.Obsidian));
            }
            if (airSides == 1 && BlockUtil.getBlockResistance(blockPos) == BlockUtil.BlockResistance.Blank) {
                if (HoleUtil.checkColliding(doubleBlock, facing, new BlockUtil.BlockResistance[]{BlockUtil.BlockResistance.Unbreakable, BlockUtil.BlockResistance.Resistant}) && BlockUtil.getBlockResistance(doubleBlock) == BlockUtil.BlockResistance.Blank) {
                    holeBlocks.add(new Hole(doubleBlock, Hole.Type.Obsidian));
                }
            }
            if (airSides != 1 || BlockUtil.getBlockResistance(blockPos) != BlockUtil.BlockResistance.Blank || !HoleUtil.checkColliding(doubleBlock, facing, BlockUtil.BlockResistance.Unbreakable) || BlockUtil.getBlockResistance(doubleBlock) != BlockUtil.BlockResistance.Blank) continue;
            holeBlocks.add(new Hole(doubleBlock, Hole.Type.Bedrock));
        }
        return holeBlocks;
    }

    public static boolean checkColliding(BlockPos blockPos, Facing facing, BlockUtil.BlockResistance blockResistance) {
        ArrayList<BlockPos> touchingBlocks = new ArrayList<BlockPos>();
        if (facing == Facing.East) {
            touchingBlocks.add(blockPos.func_177974_f());
            touchingBlocks.add(blockPos.func_177968_d());
            touchingBlocks.add(blockPos.func_177978_c());
        } else if (facing == Facing.West) {
            touchingBlocks.add(blockPos.func_177976_e());
            touchingBlocks.add(blockPos.func_177968_d());
            touchingBlocks.add(blockPos.func_177978_c());
        } else if (facing == Facing.North) {
            touchingBlocks.add(blockPos.func_177974_f());
            touchingBlocks.add(blockPos.func_177976_e());
            touchingBlocks.add(blockPos.func_177978_c());
        } else if (facing == Facing.South) {
            touchingBlocks.add(blockPos.func_177974_f());
            touchingBlocks.add(blockPos.func_177968_d());
            touchingBlocks.add(blockPos.func_177976_e());
        }
        for (BlockPos touchingBlock : touchingBlocks) {
            if (BlockUtil.getBlockResistance(touchingBlock) == blockResistance) continue;
            return false;
        }
        return true;
    }

    public static boolean checkColliding(BlockPos blockPos, Facing facing, BlockUtil.BlockResistance[] blockResistance) {
        ArrayList<BlockPos> touchingBlocks = new ArrayList<BlockPos>();
        if (facing == Facing.East) {
            touchingBlocks.add(blockPos.func_177974_f());
            touchingBlocks.add(blockPos.func_177968_d());
            touchingBlocks.add(blockPos.func_177978_c());
        } else if (facing == Facing.West) {
            touchingBlocks.add(blockPos.func_177976_e());
            touchingBlocks.add(blockPos.func_177968_d());
            touchingBlocks.add(blockPos.func_177978_c());
        } else if (facing == Facing.North) {
            touchingBlocks.add(blockPos.func_177974_f());
            touchingBlocks.add(blockPos.func_177976_e());
            touchingBlocks.add(blockPos.func_177978_c());
        } else if (facing == Facing.South) {
            touchingBlocks.add(blockPos.func_177974_f());
            touchingBlocks.add(blockPos.func_177968_d());
            touchingBlocks.add(blockPos.func_177976_e());
        }
        for (BlockPos touchingBlock : touchingBlocks) {
            if (BlockUtil.getBlockResistance(touchingBlock) == blockResistance[0] && BlockUtil.getBlockResistance(touchingBlock) == blockResistance[1]) continue;
            return false;
        }
        return true;
    }

    public static boolean isObsidianHole(BlockPos blockPos) {
        return !(BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 1, 0)) != BlockUtil.BlockResistance.Blank || HoleUtil.isBedRockHole(blockPos) || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 2, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, -1)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, -1)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.func_177982_a(1, 0, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(1, 0, 0)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.func_177982_a(-1, 0, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(-1, 0, 0)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, 1)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, 1)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.func_177963_a(0.5, 0.5, 0.5)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, -1, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, -1, 0)) != BlockUtil.BlockResistance.Unbreakable);
    }

    public static boolean isBedRockHole(BlockPos blockPos) {
        return BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 1, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 2, 0)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, -1)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.func_177982_a(1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.func_177982_a(-1, 0, 0)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, 1)) == BlockUtil.BlockResistance.Unbreakable && BlockUtil.getBlockResistance(blockPos.func_177963_a(0.5, 0.5, 0.5)) == BlockUtil.BlockResistance.Blank && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, -1, 0)) == BlockUtil.BlockResistance.Unbreakable;
    }

    public static boolean isHole(BlockPos blockPos) {
        return !(BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 1, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 2, 0)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, -1)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, -1)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.func_177982_a(1, 0, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(1, 0, 0)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.func_177982_a(-1, 0, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(-1, 0, 0)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, 1)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, 0, 1)) != BlockUtil.BlockResistance.Unbreakable || BlockUtil.getBlockResistance(blockPos.func_177963_a(0.5, 0.5, 0.5)) != BlockUtil.BlockResistance.Blank || BlockUtil.getBlockResistance(blockPos.func_177982_a(0, -1, 0)) != BlockUtil.BlockResistance.Resistant && BlockUtil.getBlockResistance(blockPos.func_177982_a(0, -1, 0)) != BlockUtil.BlockResistance.Unbreakable);
    }

    public static List<BlockPos> getNearbyHoles(double range) {
        return BlockUtil.getNearbyBlocks((EntityPlayer)HoleUtil.mc.field_71439_g, range, false).stream().filter(HoleUtil::isHole).collect(Collectors.toList());
    }

    public static enum Facing {
        North,
        South,
        East,
        West;

    }
}

