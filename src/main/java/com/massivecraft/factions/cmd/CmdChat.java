package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.perms.Role;
import com.massivecraft.factions.struct.ChatMode;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.TL;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;

public class CmdChat extends FCommand {

    public CmdChat() {
        super();

        this.aliases.add("c");
        this.aliases.add("chat");

        this.optionalArgs.put("mode", "next");

        this.requirements = new CommandRequirements.Builder(Permission.CHAT)
                .memberOnly()
                .noDisableOnLock()
                .brigadier(ChatBrigadier.class)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        if (!FactionsPlugin.getInstance().conf().factions().chat().isFactionOnlyChat()) {
            context.msg(TL.COMMAND_CHAT_DISABLED.toString());
            return;
        }

        String modeString = context.argAsString(0);
        ChatMode modeTarget = context.fPlayer.getChatMode().getNext();

        // If player is cycling through chat modes
        // and he is not atleast a coleader get next one, same for moderator.
        if (modeString == null && modeTarget == ChatMode.COLEADER && !context.fPlayer.getRole().isAtLeast(Role.COLEADER)) {
            modeTarget = modeTarget.getNext();
            if (modeTarget == ChatMode.MOD && !context.fPlayer.getRole().isAtLeast(Role.MODERATOR)) {
                modeTarget = modeTarget.getNext();
            }
        }

        if (modeString != null) {
            char attempt = Character.toLowerCase(modeString.charAt(0));

            switch (attempt) {
                case 'c':
                    if (!context.fPlayer.getRole().isAtLeast(Role.COLEADER)) {
                        context.msg(TL.COMMAND_CHAT_INSUFFICIENTRANK);
                        return;
                    }
                    modeTarget = ChatMode.COLEADER;
                    context.msg(TL.COMMAND_CHAT_MODE_COLEADER);
                    break;
                case 'm':
                    if (!context.fPlayer.getRole().isAtLeast(Role.MODERATOR)) {
                        context.msg(TL.COMMAND_CHAT_INSUFFICIENTRANK);
                        return;
                    }
                    modeTarget = ChatMode.MOD;
                    context.msg(TL.COMMAND_CHAT_MODE_MOD);
                    break;
                case 'p':
                    modeTarget = ChatMode.PUBLIC;
                    context.msg(TL.COMMAND_CHAT_MODE_PUBLIC);
                    break;
                case 'a':
                    modeTarget = ChatMode.ALLIANCE;
                    context.msg(TL.COMMAND_CHAT_MODE_ALLIANCE);
                    break;
                case 'f':
                    modeTarget = ChatMode.FACTION;
                    context.msg(TL.COMMAND_CHAT_MODE_FACTION);
                    break;
                case 't':
                    modeTarget = ChatMode.TRUCE;
                    context.msg(TL.COMMAND_CHAT_MODE_TRUCE);
                    break;
                default:
                    context.msg(TL.COMMAND_CHAT_INVALIDMODE);
                    return;
            }
        }
        context.fPlayer.setChatMode(modeTarget);
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_CHAT_DESCRIPTION;
    }

    protected class ChatBrigadier implements BrigadierProvider {
        @Override
        public ArgumentBuilder<Object, ?> get(ArgumentBuilder<Object, ?> parent) {
            return parent.then(LiteralArgumentBuilder.literal("public"))
                    .then(LiteralArgumentBuilder.literal("mod"))
                    .then(LiteralArgumentBuilder.literal("coleader"))
                    .then(LiteralArgumentBuilder.literal("alliance"))
                    .then(LiteralArgumentBuilder.literal("faction"))
                    .then(LiteralArgumentBuilder.literal("truce"));
        }
    }

}
