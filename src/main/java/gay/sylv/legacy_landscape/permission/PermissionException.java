package gay.sylv.legacy_landscape.permission;

import com.mojang.brigadier.Message;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.neoforged.neoforge.server.permission.nodes.PermissionNode;

public class PermissionException extends RuntimeException {
	public PermissionException(PermissionNode<?> permissionNode) {
		super(
			new CommandSyntaxException(
				new DynamicCommandExceptionType(PermissionException::message),
				message(permissionNode)
			)
		);
	}

	private static Message message(Object object) {
		return () -> "Missing Permissions: " + ((PermissionNode<?>) object).getNodeName();
	}
}
