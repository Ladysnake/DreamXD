package ladysnake.dreamxd;

import baritone.api.BaritoneAPI;
import baritone.api.IBaritoneProvider;
import baritone.api.fakeplayer.FakeServerPlayerEntity;
import baritone.api.schematic.FillSchematic;
import baritone.api.schematic.ReplaceSchematic;
import baritone.api.utils.BlockOptionalMeta;
import baritone.api.utils.BlockOptionalMetaLookup;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.PlayerListS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameMode;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DreamXD implements ModInitializer {
    public static UUID dreamXDUuid;
    public static int tpTimer = new Random().nextInt(20) + 40;
    public static UUID endFramePlayer;
    public static UUID targetPlayer;
    public static final Pattern dreamBad = Pattern.compile("(dream|dreamxd|green teletubby).*(bad|sucks|homeless|dumb|cheated)", Pattern.CASE_INSENSITIVE);
    public static final Pattern forgive = Pattern.compile("(ha)* *jk *(ha)*", Pattern.CASE_INSENSITIVE);
    public static Action action = Action.NONE;
    public static final List<String> smpMembersList = new ArrayList<>();

    public static void isTalkingShit(String message, PlayerEntity playerEntity) {
        Matcher dreamBadMatcher = dreamBad.matcher(message);
        Matcher forgiveMatcher = forgive.matcher(message);
        if (dreamBadMatcher.matches()) {
            action = Action.ATTACK;
            DreamXD.targetPlayer = playerEntity.getUuid();
        }
        if (forgiveMatcher.matches()) {
            action = Action.NONE;
        }
    }

    @Override
    public void onInitialize() {
        smpMembersList.add("Dream");
        smpMembersList.add("DreamXD");
        smpMembersList.add("GeorgeNotFound");
        smpMembersList.add("Callahan");
        smpMembersList.add("Sapnap");
        smpMembersList.add("awesamdude");
        smpMembersList.add("ItsAlyssa");
        smpMembersList.add("Ponk");
        smpMembersList.add("BadBoyHalo");
        smpMembersList.add("TommyInnit");
        smpMembersList.add("Tubbo_");
        smpMembersList.add("ItsFundy");
        smpMembersList.add("Ranboo");
        smpMembersList.add("Punz");
        smpMembersList.add("Purpled");
        smpMembersList.add("WilburSoot");
        smpMembersList.add("jschlatt");
        smpMembersList.add("Skeppy");
        smpMembersList.add("The_Eret");
        smpMembersList.add("JackManifoldTV");
        smpMembersList.add("Nihachu");
        smpMembersList.add("Quackity");
        smpMembersList.add("KarlJacobs");
        smpMembersList.add("HBomb94");
        smpMembersList.add("Technoblade");
        smpMembersList.add("Antfrost");
        smpMembersList.add("Ph1LzA");
        smpMembersList.add("ConnorEatsPants");
        smpMembersList.add("CaptainPuffy");
        smpMembersList.add("Vikkstar123");
        smpMembersList.add("LazarCodeLazar");
        smpMembersList.add("FoolishG");
        smpMembersList.add("hannahxxrose");
        smpMembersList.add("Slimecicle");
        smpMembersList.add("Michaelmcchill");

        ServerTickEvents.END_SERVER_TICK.register(server -> {
            ServerWorld world = server.getOverworld();

            if (action == Action.NONE) {
                if (dreamXDUuid != null) {
                    try {
                        world.getEntity(dreamXDUuid).remove();
                        for (PlayerEntity playerEntity : world.getPlayers().stream().filter(serverPlayerEntity -> serverPlayerEntity.getClass() == ServerPlayerEntity.class).collect(Collectors.toList())) {
                            ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.REMOVE_PLAYER, (ServerPlayerEntity) world.getEntity(dreamXDUuid)));
                            playerEntity.sendMessage(new LiteralText("DreamXD left the game").formatted(Formatting.YELLOW), false);
                        }
                        dreamXDUuid = null;
                        targetPlayer = null;
                        tpTimer = world.random.nextInt(20) + 40;
                    } catch (Exception ignored) {
                    }
                } else {
                    if (world.getTime() % 20 == 0) {
                        List<PlayerEntity> playerEntities = world.getPlayers().stream().filter(serverPlayerEntity -> serverPlayerEntity.getClass() == ServerPlayerEntity.class).collect(Collectors.toList());
                        for (PlayerEntity player : playerEntities) {
                            final IBaritoneProvider provider = BaritoneAPI.getProvider();

                            List<BlockPos> blockPosList = provider.getWorldScanner().scanChunkRadius(provider.getBaritone(player).getPlayerContext(), Collections.singletonList(Blocks.END_PORTAL_FRAME), 64, -128, 1);
                            if (!blockPosList.isEmpty()) {
                                if (((ServerPlayerEntity) player).interactionManager.isSurvivalLike() && blockPosList.get(player.getRandom().nextInt(blockPosList.size())).isWithinDistance(player.getPos(), 10f)) {
                                    FakeServerPlayerEntity dreamXD = new DreamXDPlayerEntity(EntityType.PLAYER, world);
                                    dreamXDUuid = dreamXD.getUuid();
                                    dreamXD.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, tpTimer, 0, false, false));
                                    for (PlayerEntity playerEntity : playerEntities) {
                                        playerEntity.sendMessage(new LiteralText("DreamXD joined the game").formatted(Formatting.YELLOW), false);
                                        // Refreshing tablist for each player
                                        ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, dreamXD));
                                    }
                                    world.spawnEntity(dreamXD);
                                    dreamXD.setGameMode(GameMode.CREATIVE);
                                    dreamXD.getAttributeInstance(ReachEntityAttributes.REACH).setBaseValue(-3);
                                    endFramePlayer = player.getUuid();
                                    action = Action.BREAK_PORTAL;
                                    dreamXD.teleport(player.getX(), player.getY(), player.getZ());
                                    return;
                                }
                            }
                        }

                        if (world.getRandom().nextInt(500) == 0 && !playerEntities.isEmpty()) {
                            FakeServerPlayerEntity dreamXD = new DreamXDPlayerEntity(EntityType.PLAYER, world);
                            dreamXDUuid = dreamXD.getUuid();
                            dreamXD.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, tpTimer, 0, false, false));
                            for (PlayerEntity playerEntity : playerEntities) {
                                playerEntity.sendMessage(new LiteralText("DreamXD joined the game").formatted(Formatting.YELLOW), false);
                                // Refreshing tablist for each player
                                ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, dreamXD));
                            }
                            world.spawnEntity(dreamXD);
                            dreamXD.setGameMode(GameMode.CREATIVE);
                            dreamXD.getAttributeInstance(ReachEntityAttributes.REACH).setBaseValue(-2);
                            action = Action.GIVING_HEAD;

                            PlayerEntity player = playerEntities.get(world.random.nextInt(playerEntities.size()));
                            targetPlayer = player.getUuid();
                            dreamXD.teleport(player.getX(), player.getY(), player.getZ());
                            return;
                        }
                    }
                }
            }

            if (action == Action.GIVING_HEAD && dreamXDUuid != null) {
                final Entity dreamXD = world.getEntity(dreamXDUuid);
                if (dreamXD == null) {
                    action = Action.NONE;
                } else if (world.getPlayerByUuid(targetPlayer) != null) {
                    PlayerEntity player = world.getPlayerByUuid(targetPlayer);

                    if (tpTimer > 0) {
                        tpTimer--;
                    }

                    if (tpTimer == 0 && player.isOnGround()) {
                        world.breakBlock(player.getBlockPos(), true);
                        String chosenMember = smpMembersList.get(world.getRandom().nextInt(smpMembersList.size()));
                        ItemStack playerHead = new ItemStack(Items.PLAYER_HEAD, 64);
                        playerHead.getOrCreateTag().putString("SkullOwner", chosenMember);

                        dreamXD.teleport(player.getX(), player.getY(), player.getZ());
                        ((FakeServerPlayerEntity) dreamXD).setStackInHand(Hand.MAIN_HAND, playerHead);

                        BlockPos placePos = new BlockPos(player.getX(), player.getY(), player.getZ());

                        ((FakeServerPlayerEntity) dreamXD).getBaritone().getBuilderProcess().build("playerhead", new ReplaceSchematic(new FillSchematic(1, 1, 1, new BlockOptionalMeta(world, Blocks.PLAYER_HEAD)), new BlockOptionalMetaLookup(world, Blocks.AIR)), placePos);
                        tpTimer = -1;
                        return;
                    }

                    if (tpTimer == -1 && !((FakeServerPlayerEntity) dreamXD).getBaritone().isActive()) {
                        action = Action.NONE;
                    }
                }
            }

            if (action == Action.BREAK_PORTAL && dreamXDUuid != null) {
                final Entity dreamXD = world.getEntity(dreamXDUuid);
                System.out.println(dreamXD);
                if (dreamXD == null) {
                    action = Action.NONE;
                } else if (!((FakeServerPlayerEntity) dreamXD).getBaritone().isActive() && world.getPlayerByUuid(endFramePlayer) != null) {
                    PlayerEntity player = world.getPlayerByUuid(endFramePlayer);

                    if (tpTimer > 0) {
                        tpTimer--;

                        if (tpTimer == 5) {
                            dreamXD.teleport(player.getX(), player.getY(), player.getZ());
                        }
                        if (tpTimer <= 0) {

                            final IBaritoneProvider provider = BaritoneAPI.getProvider();

                            List<BlockPos> blockPosList = provider.getWorldScanner().scanChunkRadius(provider.getBaritone(player).getPlayerContext(), Collections.singletonList(Blocks.END_PORTAL_FRAME), 64, -128, 1);

                            if (!blockPosList.isEmpty()) {
                                BlockPos origin = blockPosList.get(0);
                                for (BlockPos blockPos : blockPosList) {
                                    if (blockPos.getX() <= origin.getX() && blockPos.getZ() <= origin.getZ()) {
                                        origin = blockPos;
                                    }
                                }
                                ((FakeServerPlayerEntity) dreamXD).getBaritone().getBuilderProcess().build("endframe", new ReplaceSchematic(new FillSchematic(6, 3, 6, new BlockOptionalMeta(world, Blocks.AIR)), new BlockOptionalMetaLookup(world, Blocks.END_PORTAL_FRAME)), origin.add(-1, -1, -1));
                            }
                        }
                    } else {
                        action = Action.NONE;
                    }
                }
            }

            if (action == Action.ATTACK && targetPlayer != null) {
                if (dreamXDUuid != null) {
                    final Entity dreamXD = world.getEntity(dreamXDUuid);
                    if (dreamXD == null) {
                        dreamXDUuid = null;
                        tpTimer = world.random.nextInt(20) + 40;
                    } else if (!((FakeServerPlayerEntity) dreamXD).getBaritone().isActive() && world.getPlayerByUuid(targetPlayer) != null) {
                        PlayerEntity player = world.getPlayerByUuid(targetPlayer);
                        ((FakeServerPlayerEntity) dreamXD).getAttributeInstance(ReachEntityAttributes.REACH).setBaseValue(0);

                        if (tpTimer > 0) {
                            tpTimer--;

                            if (tpTimer == 5) {
                                dreamXD.teleport(player.getX(), player.getY(), player.getZ());
                                dreamXD.setSprinting(true);
                                ((FakeServerPlayerEntity) dreamXD).setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.NETHERITE_SWORD));
                            }
                            if (tpTimer <= 0) {
                                ((FakeServerPlayerEntity) dreamXD).getBaritone().getFollowProcess().follow(entity -> entity == player);
                            }
                        }
                    } else if (((FakeServerPlayerEntity) dreamXD).getAttackCooldownProgress(0.5f) >= 1f && world.getPlayerByUuid(targetPlayer) != null && dreamXD.getBlockPos().getManhattanDistance(world.getPlayerByUuid(targetPlayer).getBlockPos()) <= 3) {
                        ((FakeServerPlayerEntity) dreamXD).swingHand(Hand.MAIN_HAND);
                        ((FakeServerPlayerEntity) dreamXD).attack(world.getPlayerByUuid(targetPlayer));

                        if (world.getPlayerByUuid(targetPlayer) != null && !world.getPlayerByUuid(targetPlayer).isAlive()) {
                            action = Action.NONE;
                        }

                    }
                } else {
                    if (world.getTime() % 20 == 0) {
                        List<PlayerEntity> playerEntities = world.getPlayers().stream().filter(serverPlayerEntity -> serverPlayerEntity.getClass() == ServerPlayerEntity.class).collect(Collectors.toList());
                        PlayerEntity player = world.getPlayerByUuid(targetPlayer);

                        if (player != null && ((ServerPlayerEntity) player).interactionManager.isSurvivalLike()) {
                            FakeServerPlayerEntity dreamXD = new DreamXDPlayerEntity(EntityType.PLAYER, world);
                            dreamXDUuid = dreamXD.getUuid();
                            dreamXD.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, tpTimer, 0, false, false));
                            for (PlayerEntity playerEntity : playerEntities) {
                                playerEntity.sendMessage(new LiteralText("DreamXD joined the game").formatted(Formatting.YELLOW), false);
                                // Refreshing tablist for each player
                                ((ServerPlayerEntity) playerEntity).networkHandler.sendPacket(new PlayerListS2CPacket(PlayerListS2CPacket.Action.ADD_PLAYER, dreamXD));
                            }
                            world.spawnEntity(dreamXD);
                            dreamXD.setGameMode(GameMode.CREATIVE);
                            endFramePlayer = player.getUuid();
                            dreamXD.teleport(player.getX(), player.getY(), player.getZ());
                        }
                    }
                }
            }
        });
    }

    public static enum Action {
        NONE,
        BREAK_PORTAL,
        ATTACK,
        GIVING_HEAD
    }

}
