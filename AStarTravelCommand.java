package roger.astar;

import roger.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AStarTravelCommand extends CommandBase {

    List<AStarNode> path = new ArrayList<>();
    private static final int BLOCKS_PER_TICK = 1;
    private static int ticks = 0;

    private AStarNode current = null;


    public String getCommandName() {
        return "travel";
    }

    public String getCommandUsage(ICommandSender sender) {
        return null;
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    public void processCommand(ICommandSender sender, String[] args) {
        if(args.length > 2) {
            BlockPos currentPos = Util.getPlayerBlockPos();
            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int z = Integer.parseInt(args[2]);
            BlockPos targetPos = new BlockPos(x,y,z);

            Util.msg("Computing...");
            path = AStarPathFinder.compute(currentPos, targetPos, 10000);
            current = null;
        }
    }

    // TODO
    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END)
            return;

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

        if(player == null) {
            path.clear();
            return;
        }


        if(path.size() > 0) {
            player.motionX = 0;
            player.motionY = 0;
            player.motionZ = 0;


            List<AStarNode> currentPath = new ArrayList<>();
            if(current != null)
                currentPath.add(0, current);

            for (int i = 0 ; i < BLOCKS_PER_TICK && path.size() > 0 ; i++) {
                currentPath.add(path.remove(0));
                if(!isInSameRatio(currentPath)) {
                    path.add(0, currentPath.remove(currentPath.size() - 1));
                    break;
                }
            }

            if(currentPath.size() == 1)
                System.out.println("size is one, current null: " + (current == null));

            current = currentPath.get(currentPath.size() - 1);
            Vec3 currentTarget = current.asVec3(0.5, 0, 0.5);
            player.setPosition(currentTarget.xCoord, currentTarget.yCoord, currentTarget.zCoord);
        }
    }

    private boolean isInSameRatio(List<AStarNode> list) {
        if(list.size() <= 1)
            return true;

        List<AStarNode[]> combinations = calculateCombinations(list);

        AStarNode one = combinations.get(0)[0];
        AStarNode two = combinations.remove(0)[1]; // removes

        Direction direction = null;

        double xDiff = Math.abs(one.getX() - two.getX());
        double yDiff = Math.abs(one.getY() - two.getY());
        double zDiff = Math.abs(one.getZ() - two.getZ());

        if(xDiff > 0)
            direction = Direction.X;

        if(yDiff > 0) {
            if (direction != null)
                return false;
            direction = Direction.Y;
        }

        if(zDiff > 0) {
            if (direction != null)
                return false;
            direction = Direction.Z;
        }


        for(AStarNode[] combination : combinations) {
            AStarNode first = combination[0];
            AStarNode last = combination[1];

            double xD = Math.abs(first.getX() - last.getX());
            double yD = Math.abs(first.getY() - last.getY());
            double zD = Math.abs(first.getZ() - last.getZ());

            switch (Objects.requireNonNull(direction)) {
                case X: {
                    if (yD > 0 || zD > 0)
                        return false;
                    break;
                }
                case Y: {
                    if (xD > 0 || zD > 0)
                        return false;
                    break;
                }
                case Z: {
                    if (xD > 0 || yD > 0)
                        return false;
                    break;
                }
            }
        }

        return true;
    }

    enum Direction {
        X,
        Y,
        Z
    }

    private List<AStarNode[]> calculateCombinations(List<AStarNode> list) {
        List<AStarNode[]> combinations = new ArrayList<>();
        for(int a = 0 ; a < list.size() - 1 ; a++) {
            combinations.add(new AStarNode[] { list.get(a), list.get(a + 1) });
        }
        return combinations;
    }
}