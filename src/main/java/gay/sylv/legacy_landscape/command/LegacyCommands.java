package gay.sylv.legacy_landscape.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import gay.sylv.legacy_landscape.api.definitions.effect.MobEffects;
import gay.sylv.legacy_landscape.data_attachment.LegacyAttachments;
import gay.sylv.legacy_landscape.effect.LegacyEffects;
import gay.sylv.legacy_landscape.permission.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static gay.sylv.legacy_landscape.util.Constants.MOD_ID;

@EventBusSubscriber(
	modid = MOD_ID,
	bus = EventBusSubscriber.Bus.GAME
)
public final class LegacyCommands {
	public static final int FAILURE = -1;
	public static final String ROOT_NAME = MOD_ID;
	public static final String ROOT_ALIAS = "legacy";
	public static final String VANISH_NAME = "vanish";
	public static final String VANISH_ALIAS = "v";
	public static final String OMNISCIENT_NAME = "omniscient";

	@SubscribeEvent
	public static void registerCommands(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		Command<CommandSourceStack> executeVanish = ctx -> {
			ServerPlayer player = ctx.getSource().getPlayerOrException();
			Permissions.assertPermission(player, Permissions.VANISH);
			if (!player.hasEffect(MobEffects.evanescence())) {
				boolean addedEffect = LegacyEffects.apply(
					player,
					MobEffects.evanescence(),
					-1,
					0,
					false,
					false
				);
				if (!addedEffect) {
					ctx.getSource().sendFailure(Component.translatable("commands.legacy_landscape.vanish.cannot_be_applied"));
					return FAILURE;
				} else {
					ctx.getSource().sendSuccess(() -> Component.translatable("commands.legacy_landscape.vanish.applied"), true);
				}
			} else {
				boolean removedEffect = player.removeEffect(MobEffects.evanescence());
				if (!removedEffect) {
					ctx.getSource().sendFailure(Component.translatable("commands.legacy_landscape.vanish.cannot_be_removed"));
					return FAILURE;
				} else {
					ctx.getSource().sendSuccess(() -> Component.translatable("commands.legacy_landscape.vanish.removed"), true);
				}
			}

			return Command.SINGLE_SUCCESS;
		};
		Command<CommandSourceStack> executeOmniscient = ctx -> {
			ServerPlayer player = ctx.getSource().getPlayerOrException();
			Permissions.assertPermission(player, Permissions.OMNISCIENT);
			if (!player.hasData(LegacyAttachments.OMNISCIENT)) {
				LegacyAttachments.setOmniscient(player);
				ctx.getSource().sendSuccess(() -> Component.translatable("commands.legacy_landscape.omniscient.applied"), true);
			} else {
				LegacyAttachments.removeOmniscience(player);
				ctx.getSource().sendSuccess(() -> Component.translatable("commands.legacy_landscape.omniscient.removed"), true);
			}

			return Command.SINGLE_SUCCESS;
		};
		LiteralCommandNode<CommandSourceStack> vanish = Commands.literal(VANISH_NAME)
			.executes(executeVanish)
			.build();
		LiteralCommandNode<CommandSourceStack> vanishAlias = Commands.literal(VANISH_ALIAS)
			.executes(executeVanish)
			.build();
		LiteralCommandNode<CommandSourceStack> omniscient = Commands.literal(OMNISCIENT_NAME)
			.executes(executeOmniscient)
			.build();
		LiteralCommandNode<CommandSourceStack> root = dispatcher.register(
			Commands.literal(ROOT_NAME)
				.then(vanish)
				.then(vanishAlias)
				.then(omniscient)
		);
		dispatcher.register(
			Commands.literal(ROOT_ALIAS)
				.redirect(root)
		);
	}
}
